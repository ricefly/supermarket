<template>
  <div>
    <TopBar :employeeName="employeeInfo && employeeInfo.name" />
    <div class="inventory-content">
      <h1>仓库库存管理</h1>
      <p>欢迎来到仓库页面。</p>
    </div>
  </div>
</template>

<script>
import TopBar from '../TopBar.vue'
import axios from 'axios'
export default {
  name: 'WarehouseInventory',
  components: { TopBar },
  data() {
    return {
      employeeInfo: null
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
    }
  }
}
</script>

<style scoped>
.inventory-content {
  max-width: 900px;
  margin: 40px auto 0 auto;
  padding: 20px;
}
</style> 