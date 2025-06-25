<template>
  <MemberLayout>
    <div class="order-history-main">
      <h2>历史订单</h2>
      <table class="order-table">
        <thead>
          <tr>
            <th>订单ID</th>
            <th>下单时间</th>
            <th>总金额</th>
            <th>状态</th>
            <th>支付方式</th>
            <th>返利金额</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.orderId">
            <td>{{ order.orderId }}</td>
            <td>{{ formatDate(order.orderDate) }}</td>
            <td>{{ order.totalAmount }}</td>
            <td>{{ order.orderStatus }}</td>
            <td>{{ order.paymentMethod }}</td>
            <td>{{ order.rebateEarned }}</td>
          </tr>
        </tbody>
      </table>
      <div v-if="orders.length === 0" class="empty-tip">暂无历史订单</div>
    </div>
  </MemberLayout>
</template>

<script>
import MemberLayout from './MemberLayout.vue'
import axios from 'axios'
export default {
  name: 'OrderHistory',
  components: { MemberLayout },
  data() {
    return {
      orders: []
    }
  },
  created() {
    this.fetchOrders()
  },
  methods: {
    fetchOrders() {
      const token = localStorage.getItem('token')
      if (!token) return
      axios.get('/api/members/orders', { headers: { token } }).then(res => {
        if (Array.isArray(res.data)) {
          this.orders = res.data
        }
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
.order-history-main { max-width: 900px; margin: 40px auto; background: #fff; padding: 32px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.order-table { width: 100%; border-collapse: collapse; margin-bottom: 16px; }
.order-table th, .order-table td { border: 1px solid #eee; padding: 10px 8px; text-align: center; }
.order-table th { background: #f5f6fa; }
.empty-tip { color: #999; text-align: center; margin-top: 24px; font-size: 16px; }
</style> 