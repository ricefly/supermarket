<template>
  <WarehouseLayout>
    <div class="loss-main">
      <h2>商品损耗登记</h2>
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
            <td><button @click="openLossDialog(prod)">报损</button></td>
          </tr>
        </tbody>
      </table>
      <div v-if="successMsg" class="success-msg">{{ successMsg }}</div>
      <div v-if="errorMsg" class="error-msg">{{ errorMsg }}</div>
      <!-- 报损弹窗 -->
      <div v-if="showLossDialog" class="dialog-mask">
        <div class="dialog">
          <h3>商品报损登记</h3>
          <div style="margin-bottom:12px;">商品：{{ lossProduct && lossProduct.productName }}（ID: {{ lossProduct && lossProduct.productId }}）</div>
          <div style="margin-bottom:10px;">
            <label style="margin-right:8px;">损耗数量：</label>
            <input v-model.number="lossQuantity" type="number" min="1" :max="lossProduct ? lossProduct.currentStock : 1" placeholder="请输入" style="width:120px;" />
          </div>
          <div style="margin-bottom:16px;">
            <label style="margin-right:8px;">损耗原因：</label>
            <input v-model="lossReason" placeholder="请输入" style="width:220px;" />
          </div>
          <div>
            <button @click="submitLoss">确认登记</button>
            <button @click="showLossDialog=false" style="margin-left:16px;">取消</button>
          </div>
        </div>
      </div>
      <h3 style="margin-top:32px;">历史损耗记录</h3>
      <table class="loss-table">
        <thead>
          <tr>
            <th>商品ID</th>
            <th>损耗数量</th>
            <th>损耗原因</th>
            <th>登记时间</th>
            <th>登记人ID</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="rec in lossRecords" :key="rec.lossRecordId">
            <td>{{ rec.productId }}</td>
            <td>{{ rec.quantityLost }}</td>
            <td>{{ rec.lossReason }}</td>
            <td>{{ formatDate(rec.lossDate) }}</td>
            <td>{{ rec.recordedByEmployeeId }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </WarehouseLayout>
</template>

<script>
import WarehouseLayout from './WarehouseLayout.vue'
import axios from 'axios'
export default {
  name: 'LossRecord',
  components: { WarehouseLayout },
  data() {
    return {
      products: [],
      searchKeyword: '',
      successMsg: '',
      errorMsg: '',
      showLossDialog: false,
      lossProduct: null,
      lossQuantity: 1,
      lossReason: '',
      lossRecords: []
    }
  },
  created() {
    this.fetchProducts()
    this.fetchLossRecords()
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
    openLossDialog(prod) {
      this.lossProduct = prod
      this.lossQuantity = 1
      this.lossReason = ''
      this.showLossDialog = true
    },
    submitLoss() {
      this.successMsg = ''
      this.errorMsg = ''
      if (!this.lossProduct || this.lossQuantity < 1 || !this.lossReason) {
        this.errorMsg = '请输入损耗数量和原因';
        return;
      }
      if (this.lossQuantity > this.lossProduct.currentStock) {
        this.errorMsg = '损耗数量不能大于当前库存';
        return;
      }
      const employeeId = localStorage.getItem('employeeId') || localStorage.getItem('employeeID') || ''
      axios.post(`/api/warehouse/loss?productId=${this.lossProduct.productId}&quantity=${this.lossQuantity}&reason=${encodeURIComponent(this.lossReason)}&employeeId=${employeeId}`)
        .then(res => {
          if (res.data.code === 0) {
            this.successMsg = '登记成功！';
            this.fetchProducts();
            this.fetchLossRecords();
            this.showLossDialog = false;
          } else {
            this.errorMsg = '登记失败';
          }
        }).catch(() => {
          this.errorMsg = '登记失败';
        })
    },
    fetchLossRecords() {
      axios.get('/api/warehouse/loss').then(res => {
        this.lossRecords = res.data
      })
    },
    formatDate(dateStr) {
      if (!dateStr) return '';
      const d = new Date(dateStr);
      return d.getFullYear() + '-' +
        String(d.getMonth() + 1).padStart(2, '0') + '-' +
        String(d.getDate()).padStart(2, '0') + ' ' +
        String(d.getHours()).padStart(2, '0') + ':' +
        String(d.getMinutes()).padStart(2, '0');
    }
  }
}
</script>

<style scoped>
.loss-main { max-width: 1100px; margin: 40px auto; background: #fff; padding: 32px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.search-bar { margin-bottom: 16px; display: flex; gap: 8px; }
.product-table { width: 100%; border-collapse: collapse; margin-bottom: 16px; }
.product-table th, .product-table td { border: 1px solid #eee; padding: 10px 8px; text-align: center; }
.product-table th { background: #f5f6fa; }
.dialog-mask { position: fixed; left: 0; top: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.2); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.dialog { background: #fff; padding: 24px 32px; border-radius: 8px; min-width: 320px; box-shadow: 0 2px 8px rgba(0,0,0,0.13); }
.success-msg { color: #43a047; margin-top: 12px; font-size: 16px; }
.error-msg { color: #e53935; margin-top: 12px; font-size: 16px; }
.loss-table { width: 100%; border-collapse: collapse; margin-top: 18px; }
.loss-table th, .loss-table td { border: 1px solid #eee; padding: 8px 6px; text-align: center; }
.loss-table th { background: #f5f6fa; }
</style> 