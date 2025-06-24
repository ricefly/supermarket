<template>
  <div>
    <header class="topbar">
      <span class="logo">joja超市管理系统</span>
    </header>
    <div class="login-container">
      <h2>员工登录</h2>
      <form class="form-group" @submit.prevent="login">
        <input 
          v-model="username" 
          type="text" 
          placeholder="请输入用户名" 
          class="form-input" 
          required
          autocomplete="username"/>
        <input 
          v-model="password" 
          type="password" 
          placeholder="请输入密码" 
          class="form-input" 
          required
          autocomplete="current-password"/>
        <button type="submit" class="login-btn">登录</button>
        <div v-if="msg" class="message" :class="{ 'error': msg !== '登录成功' }">{{ msg }}</div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'EmployeeLogin',
  data() {
    return {
      username: '',
      password: '',
      msg: ''
    }
  },
  methods: {
    login() {
      this.msg = ''
      axios.post('/api/employees/login', {
        username: this.username,
        password: this.password
      }).then(res => {
        if (res.data.code === 0) {
          this.msg = '登录成功'
          localStorage.setItem('employeeToken', res.data.token)
          localStorage.setItem('employeeRole', res.data.role)
          
          // 根据角色跳转到不同页面
          switch(res.data.role) {
            case 'admin':
              this.$router.push('/admin/dashboard')
              break
            case 'manager':
              this.$router.push('/manager/dashboard')
              break
            case 'cashier':
              this.$router.push('/cashier/pos')
              break
            case 'warehouse':
              this.$router.push('/warehouse/inventory')
              break
            default:
              this.$router.push('/employee/dashboard')
          }
        } else {
          this.msg = res.data.msg
        }
      }).catch(error => {
        console.error('登录失败:', error)
        this.msg = error.response?.data?.msg || '登录失败，请稍后重试'
      })
    }
  }
}
</script>

<style scoped>
.topbar {
  width: 100%;
  height: 56px;
  background: #1976D2;
  display: flex;
  align-items: center;
  padding-left: 32px;
  box-sizing: border-box;
  color: #fff;
  font-size: 22px;
  font-weight: bold;
  letter-spacing: 2px;
  margin-bottom: 32px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
.logo {
  user-select: none;
}

.login-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
  border-radius: 8px;
  background: #fff;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-input {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.form-input:focus {
  border-color: #2196F3;
  outline: none;
}

.login-btn {
  padding: 10px;
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.login-btn:hover {
  background-color: #1976D2;
}

.message {
  text-align: center;
  padding: 8px;
  border-radius: 4px;
}

.message.error {
  color: #721c24;
  background-color: #f8d7da;
  border: 1px solid #f5c6cb;
}

.message:not(.error) {
  color: #155724;
  background-color: #d4edda;
  border: 1px solid #c3e6cb;
}
</style> 