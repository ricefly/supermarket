<template>
  <ManagerLayout>
    <div class="order-manage">
      <h2>订单管理</h2>
      <div class="search-bar">
        <input v-model="searchOrderId" type="number" placeholder="订单ID" style="width:120px;" />
        <input v-model="searchMemberId" type="number" placeholder="会员ID" style="width:120px;" />
        <button @click="fetchOrders">查询</button>
        <button @click="resetSearch">重置</button>
      </div>
      <table class="order-table">
        <thead>
          <tr>
            <th>订单ID</th>
            <th>收银员ID</th>
            <th>会员ID</th>
            <th>下单时间</th>
            <th>总金额</th>
            <th>状态</th>
            <th>支付方式</th>
            <th>返利金额</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.orderId">
            <td>{{ order.orderId }}</td>
            <td>{{ order.cashierEmployeeId }}</td>
            <td>{{ order.memberId }}</td>
            <td>{{ formatDate(order.orderDate) }}</td>
            <td>{{ order.totalAmount }}</td>
            <td>{{ order.orderStatus }}</td>
            <td>{{ order.paymentMethod }}</td>
            <td>{{ order.rebateEarned }}</td>
            <td>
              <button @click="showOrderItems(order)">查看订单项</button>
            </td>
          </tr>
        </tbody>
      </table>
      <!-- 订单项弹窗 -->
      <div v-if="showItemsDialog" class="dialog-mask">
        <div class="dialog">
          <h3>订单项详情（订单ID：{{ currentOrderId }}）</h3>
          <table class="item-table">
            <thead>
              <tr>
                <th>商品名称</th>
                <th>数量</th>
                <th>单价</th>
                <th>小计</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in orderItems" :key="item.orderItemId">
                <td>{{ getProductName(item.productId) }}</td>
                <td>{{ item.quantity }}</td>
                <td>{{ item.unitPrice }}</td>
                <td>{{ item.itemTotal }}</td>
              </tr>
            </tbody>
          </table>
          <div style="margin-top: 16px; text-align: right;">
            <button @click="showItemsDialog = false">关闭</button>
          </div>
        </div>
      </div>
    </div>
  </ManagerLayout>
</template>
<script>
import ManagerLayout from './ManagerLayout.vue'
import axios from 'axios'
export default {
  name: 'OrderManage',
  components: { ManagerLayout },
  data() {
    return {
      orders: [],
      orderItems: [],
      showItemsDialog: false,
      currentOrderId: null,
      searchOrderId: '',
      searchMemberId: '',
      productMap: {} // productId -> productName
    }
  },
  created() {
    this.fetchOrders()
    this.fetchProducts()
  },
  methods: {
    fetchOrders() {
      axios.get('/api/manager/orders', {
        params: {
          orderId: this.searchOrderId,
          memberId: this.searchMemberId
        }
      }).then(res => {
        this.orders = res.data
      })
    },
    fetchProducts() {
      axios.get('/api/manager/products').then(res => {
        this.productMap = {}
        res.data.forEach(prod => {
          this.productMap[prod.productId] = prod.productName
        })
      })
    },
    getProductName(productId) {
      return this.productMap[productId] || ''
    },
    resetSearch() {
      this.searchOrderId = ''
      this.searchMemberId = ''
      this.fetchOrders()
    },
    showOrderItems(order) {
      this.currentOrderId = order.orderId
      axios.get(`/api/manager/orders/${order.orderId}/items`).then(res => {
        this.orderItems = res.data
        this.showItemsDialog = true
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
.order-manage {
  max-width: 1400px;
  margin: 40px auto;
  background: #fff;
  padding: 32px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
.order-table, .item-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 24px;
}
.order-table th, .order-table td, .item-table th, .item-table td {
  border: 1px solid #eee;
  padding: 10px 8px;
  text-align: center;
}
.order-table th, .item-table th {
  background: #f5f6fa;
}
.dialog-mask {
  position: fixed;
  left: 0; top: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.dialog {
  background: #fff;
  padding: 24px 32px;
  border-radius: 8px;
  min-width: 400px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.13);
}
.search-bar {
  margin-bottom: 16px;
  display: flex;
  gap: 8px;
}
.search-bar input {
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.search-bar button {
  padding: 8px 16px;
  background: #1976D2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style> 