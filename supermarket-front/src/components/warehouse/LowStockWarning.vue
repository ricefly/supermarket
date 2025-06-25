<template>
  <WarehouseLayout>
    <div class="lowstock-main">
      <h2>低库存预警</h2>
      <table class="product-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>种类</th>
            <th>规格</th>
            <th>库存量</th>
            <th>预警值</th>
            <th>库存位置</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="prod in lowStockProducts" :key="prod.productId" class="low-stock-row">
            <td>{{ prod.productId }}</td>
            <td>{{ prod.productName }}</td>
            <td>{{ prod.category }}</td>
            <td>{{ prod.specification }}</td>
            <td>
              {{ prod.currentStock }}
              <span class="low-stock-icon">⚠️</span>
            </td>
            <td>{{ prod.lowStockThreshold }}</td>
            <td>{{ prod.stockLocation }}</td>
          </tr>
        </tbody>
      </table>
      <div v-if="lowStockProducts.length === 0" style="margin-top:24px; color:#43a047; font-size:16px;">暂无低库存商品</div>
    </div>
  </WarehouseLayout>
</template>

<script>
import WarehouseLayout from './WarehouseLayout.vue'
import axios from 'axios'
export default {
  name: 'LowStockWarning',
  components: { WarehouseLayout },
  data() {
    return {
      lowStockProducts: []
    }
  },
  created() {
    this.fetchProducts()
  },
  methods: {
    fetchProducts() {
      axios.get('/api/manager/products').then(res => {
        this.lowStockProducts = res.data.filter(p => p.isActive && p.lowStockThreshold != null && p.currentStock <= p.lowStockThreshold)
      })
    }
  }
}
</script>

<style scoped>
.lowstock-main { max-width: 900px; margin: 40px auto; background: #fff; padding: 32px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.product-table { width: 100%; border-collapse: collapse; margin-bottom: 16px; }
.product-table th, .product-table td { border: 1px solid #eee; padding: 10px 8px; text-align: center; }
.product-table th { background: #f5f6fa; }
.low-stock-row { background: #fffbe6 !important; color: #d4380d; }
.low-stock-icon { color: #faad14; margin-left: 4px; }
</style> 