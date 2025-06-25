<template>
  <WarehouseLayout>
    <h1>仓库库存管理</h1>
    <p>欢迎来到仓库页面。</p>
  </WarehouseLayout>
</template>

<script>
import WarehouseLayout from './WarehouseLayout.vue'
import axios from 'axios'
export default {
  name: 'WarehouseInventory',
  components: { WarehouseLayout },
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
.dashboard-content {
  flex: 1;
  padding: 40px 40px 0 40px;
}
</style> 