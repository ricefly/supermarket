<template>
  <ManagerLayout>
    <h1>经理仪表板</h1>
    <p>欢迎来到经理页面。</p>
  </ManagerLayout>
</template>

<script>
import ManagerLayout from './ManagerLayout.vue'
import axios from 'axios'
export default {
  name: 'ManagerDashboard',
  components: { ManagerLayout },
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
    },
    logout() {
      const token = localStorage.getItem('employeeToken')
      if (token) {
        axios.post('/api/employees/logout', null, {
          headers: { token }
        }).then(() => {
          localStorage.removeItem('employeeToken')
          localStorage.removeItem('employeeRole')
          this.$router.push('/employee/login')
        }).catch(error => {
          console.error('退出登录失败:', error)
        })
      } else {
        this.$router.push('/employee/login')
      }
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