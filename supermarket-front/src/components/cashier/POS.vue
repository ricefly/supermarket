<template>
  <CashierLayout>
    <div class="pos-main">
      <h1>收银员POS界面</h1>
      <div class="pos-section">
        <div class="input-row">
          <input v-model="inputProductId" type="number" placeholder="商品编号" style="width:120px;" @keyup.enter="addProduct" />
          <input v-model.number="inputQuantity" type="number" min="1" placeholder="数量" style="width:80px;" />
          <button @click="addProduct">添加商品</button>
        </div>
        <table class="product-table">
          <thead>
            <tr>
              <th>商品ID</th>
              <th>商品名称</th>
              <th>单价</th>
              <th>数量</th>
              <th>小计</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, idx) in cartItems" :key="item.productId">
              <td>{{ item.productId }}</td>
              <td>{{ item.productName }}</td>
              <td>{{ item.unitPrice }}</td>
              <td><input v-model.number="item.quantity" type="number" min="1" style="width:60px;" @change="updateItemTotal(idx)" /></td>
              <td>{{ (item.unitPrice * item.quantity).toFixed(2) }}</td>
              <td><button @click="removeItem(idx)">删除</button></td>
            </tr>
          </tbody>
        </table>
        <div class="input-row">
          <input v-model="memberPhone" placeholder="会员手机号（可选）" style="width:180px;" />
          <select v-model="paymentMethod" style="width:140px;">
            <option value="">选择支付方式</option>
            <option value="cash">现金</option>
            <option value="bank">银行卡</option>
            <option value="wechat">微信</option>
            <option value="alipay">支付宝</option>
          </select>
        </div>
        <div class="summary-row">
          <span>总价：<b>{{ totalAmount.toFixed(2) }}</b> 元</span>
          <button @click="settleOrder" :disabled="cartItems.length===0 || !paymentMethod" style="margin-left:32px;">结算</button>
        </div>
        <div class="rebate-row" v-if="memberPhone">
          返利金额：<b>{{ rebateAmount }}</b> 元
        </div>
        <div v-if="orderSuccessId" class="success-msg">订单结算成功，订单号：{{ orderSuccessId }}</div>
      </div>
    </div>
  </CashierLayout>
</template>

<script>
import CashierLayout from './CashierLayout.vue'
import axios from 'axios'
export default {
  name: 'CashierPOS',
  components: { CashierLayout },
  data() {
    return {
      employeeInfo: null,
      inputProductId: '',
      inputQuantity: 1,
      cartItems: [],
      memberPhone: '',
      paymentMethod: '',
      orderSuccessId: null
    }
  },
  computed: {
    totalAmount() {
      return this.cartItems.reduce((sum, item) => sum + item.unitPrice * item.quantity, 0)
    },
    rebateAmount() {
      if (!this.memberPhone) return ''
      return (this.totalAmount * 0.01).toFixed(2)
    }
  },
  created() {
    this.fetchEmployeeInfo()
  },
  methods: {
    fetchEmployeeInfo() {
      const token = localStorage.getItem('employeeToken')
      if (!token) {
        this.$router.push('/employee/login')
        return
      }
      axios.get('/api/employees/info', {
        headers: { token }
      }).then(res => {
        if (res.data.code === 0) {
          this.employeeInfo = res.data.data
        } else {
          this.$router.push('/employee/login')
        }
      }).catch(() => {
        this.$router.push('/employee/login')
      })
    },
    addProduct() {
      if (!this.inputProductId || this.inputQuantity < 1) return
      // 通过商品ID精确查找
      axios.get(`/api/manager/products/${this.inputProductId}`).then(res => {
        const prod = res.data && res.data.productId ? res.data : (res.data && res.data[0]);
        if (!prod) {
          alert('未找到该商品')
          return
        }
        if (prod.isActive === false) {
          alert('该商品已下架或不可售')
          return
        }
        // 检查购物车是否已存在
        const exist = this.cartItems.find(item => item.productId == prod.productId)
        if (exist) {
          exist.quantity += this.inputQuantity
        } else {
          this.cartItems.push({
            productId: prod.productId,
            productName: prod.productName,
            unitPrice: prod.retailPrice,
            quantity: this.inputQuantity
          })
        }
        this.inputProductId = ''
        this.inputQuantity = 1
      })
    },
    updateItemTotal(idx) {
      if (this.cartItems[idx].quantity < 1) this.cartItems[idx].quantity = 1
    },
    removeItem(idx) {
      this.cartItems.splice(idx, 1)
    },
    settleOrder() {
      if (!this.employeeInfo) return
      const items = this.cartItems.map(item => ({
        productId: item.productId,
        quantity: item.quantity,
        unitPrice: item.unitPrice,
        itemTotal: (item.unitPrice * item.quantity).toFixed(2)
      }))
      axios.post('/api/cashier/orders/settle', {
        items,
        cashierEmployeeId: this.employeeInfo.employeeId,
        memberPhone: this.memberPhone,
        paymentMethod: this.paymentMethod
      }).then(res => {
        if (res.data.code === 0) {
          this.orderSuccessId = res.data.orderId
          this.cartItems = []
          this.memberPhone = ''
          this.paymentMethod = ''
        } else {
          alert('结算失败')
        }
      }).catch(() => {
        alert('结算失败')
      })
    }
  }
}
</script>

<style scoped>
.pos-main { max-width: 900px; margin: 40px auto; background: #fff; padding: 32px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.pos-section { margin-bottom: 32px; }
.input-row { margin-bottom: 12px; display: flex; gap: 12px; align-items: center; }
.product-table { width: 100%; border-collapse: collapse; margin-bottom: 16px; }
.product-table th, .product-table td { border: 1px solid #eee; padding: 8px 6px; text-align: center; }
.product-table th { background: #f5f6fa; }
.summary-row { margin-top: 12px; font-size: 18px; display: flex; align-items: center; }
.success-msg { color: #43a047; margin-top: 16px; font-size: 16px; }
.rebate-row { margin-top: 6px; font-size: 16px; color: #1976D2; }
</style> 