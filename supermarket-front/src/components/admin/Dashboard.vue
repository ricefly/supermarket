<template>
  <div>
    <TopBar :employeeName="employeeInfo && employeeInfo.name" @logout="logout" />
    <div class="dashboard-content">
      <div class="employee-info" v-if="employeeInfo">
        <h2>员工信息</h2>
        <div class="info-item">
          <span class="label">用户名：</span>
          <span>{{ employeeInfo.username }}</span>
        </div>
        <div class="info-item">
          <span class="label">姓名：</span>
          <span>{{ employeeInfo.name }}</span>
        </div>
        <div class="info-item">
          <span class="label">角色：</span>
          <span>{{ employeeInfo.role }}</span>
        </div>
        <div class="info-item">
          <span class="label">状态：</span>
          <span>{{ employeeInfo.status }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import TopBar from '../TopBar.vue'

export default {
  name: 'AdminDashboard',
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
      }).catch(error => {
        console.error('获取员工信息失败:', error)
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
      }
    }
  }
}
</script>

<style scoped>
.dashboard-content {
  max-width: 900px;
  margin: 40px auto 0 auto;
  padding: 20px;
}

.logout-btn {
  padding: 8px 16px;
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.logout-btn:hover {
  background-color: #c82333;
}

.employee-info {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.info-item {
  margin: 10px 0;
}

.label {
  font-weight: bold;
  margin-right: 10px;
}
</style> 