<template>
  <div class="dashboard-layout">
    <TopBar :employeeName="employeeInfo && employeeInfo.name" @logout="logout" />
    <div class="main-content">
      <aside class="sidebar">
        <ul>
          <li @click="$router.push('/manager/dashboard')">首页</li>
          <li @click="$router.push('/manager/products')">商品管理</li>
          <li @click="$router.push('/manager/orders')">订单管理</li>
          <li @click="$router.push('/manager/statistics')">统计报表</li>
          <li @click="$router.push('/manager/employees')">员工管理</li>
        </ul>
      </aside>
      <div class="dashboard-content">
        <slot />
      </div>
    </div>
  </div>
</template>
<script>
import TopBar from '../TopBar.vue'
import axios from 'axios'
export default {
  name: 'ManagerLayout',
  components: { TopBar },
  data() {
    return { employeeInfo: null }
  },
  created() {
    const token = localStorage.getItem('employeeToken')
    if (token) {
      axios.get('/api/employees/info', { headers: { token } }).then(res => {
        if (res.data.code === 0) this.employeeInfo = res.data.data
      })
    }
  },
  methods: {
    logout() {
      localStorage.removeItem('employeeToken')
      localStorage.removeItem('employeeRole')
      this.$router.push('/employee/login')
    }
  }
}
</script>
<style scoped>
.dashboard-layout {
  min-height: 100vh;
  background: #f5f6fa;
}
.main-content {
  display: flex;
}
.sidebar {
  width: 200px;
  background: #fff;
  border-right: 1px solid #eee;
  padding: 0;
  min-height: calc(100vh - 56px);
  margin-top: 0;
}
.sidebar ul {
  list-style: none;
  padding: 30px 0 0 0;
  margin: 0;
}
.sidebar li {
  padding: 16px 32px;
  cursor: pointer;
  font-size: 16px;
  color: #333;
  transition: background 0.2s, color 0.2s;
}
.sidebar li:hover {
  background: #1976D2;
  color: #fff;
}
.dashboard-content {
  flex: 1;
  padding: 40px 40px 0 40px;
}
</style> 