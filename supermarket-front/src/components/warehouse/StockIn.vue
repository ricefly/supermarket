<template>
  <WarehouseLayout>
    <div class="stockin-main">
      <h2>商品入库</h2>
      <div class="search-bar">
        <input v-model="searchKeyword" placeholder="输入商品名称或种类查询" @keyup.enter="fetchProducts" style="width:200px;" />
        <button @click="fetchProducts">查询</button>
        <button @click="resetSearch">重置</button>
      </div>
      <table class="product-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>种类</th>
            <th>规格</th>
            <th>库存量</th>
            <th>库存位置</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="prod in products" :key="prod.productId">
            <td>{{ prod.productId }}</td>
            <td>{{ prod.productName }}</td>
            <td>{{ prod.category }}</td>
            <td>{{ prod.specification }}</td>
            <td>{{ prod.currentStock }}</td>
            <td>{{ prod.stockLocation }}</td>
            <td style="display:flex;align-items:center;gap:6px;">
              <input v-model.number="stockInMap[prod.productId]" type="number" min="1" placeholder="数量" style="width:60px;" />
              <button @click="stockIn(prod.productId)">入库</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="successMsg" class="success-msg">{{ successMsg }}</div>
      <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
    </div>
  </WarehouseLayout>
</template>

<script>
import WarehouseLayout from './WarehouseLayout.vue'
import axios from 'axios'
export default {
  name: 'StockIn',
  components: { WarehouseLayout },
  data() {
    return {
      products: [],
      searchKeyword: '',
      stockInMap: {}, // productId -> 入库数量
      successMsg: '',
      errorMsg: ''
    }
  },
  created() {
    this.fetchProducts()
  },
  methods: {
    fetchProducts() {
      axios.get('/api/manager/products', { params: { keyword: this.searchKeyword } }).then(res => {
        this.products = res.data.filter(p => p.isActive)
      })
    },
    resetSearch() {
      this.searchKeyword = ''
      this.fetchProducts()
    },
    stockIn(productId) {
      this.successMsg = ''
      this.errorMsg = ''
      const quantity = this.stockInMap[productId]
      if (!quantity || quantity < 1) {
        this.errorMsg = '请输入正确的入库数量';
        return;
      }
      axios.post(`/api/warehouse/products/${productId}/stockin?quantity=${quantity}`)
        .then(res => {
          if (res.data.code === 0) {
            this.successMsg = '入库成功！';
            this.fetchProducts();
            this.stockInMap[productId] = 1;
          } else {
            this.errorMsg = '入库失败';
          }
        }).catch(() => {
          this.errorMsg = '入库失败';
        })
    }
  }
}
</script>

<style scoped>
.stockin-main { max-width: 1100px; margin: 40px auto; background: #fff; padding: 32px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.search-bar { margin-bottom: 16px; display: flex; gap: 8px; }
.product-table { width: 100%; border-collapse: collapse; margin-bottom: 16px; }
.product-table th, .product-table td { border: 1px solid #eee; padding: 10px 8px; text-align: center; }
.product-table th { background: #f5f6fa; }
.success-msg { color: #43a047; margin-top: 12px; font-size: 16px; }
.error-msg { color: #e53935; margin-top: 12px; font-size: 16px; }
</style> 