<template>
  <div class="login-container">
    <h2>会员登录</h2>
    <form class="form-group" @submit.prevent="login" autocomplete="on">
      <input 
        v-model="phoneNumber" 
        type="tel" 
        placeholder="请输入手机号" 
        class="form-input" 
        required
        autocomplete="tel"/>
      <input 
        v-model="password" 
        type="password" 
        placeholder="请输入密码" 
        class="form-input" 
        required
        autocomplete="current-password"/>
      <button type="submit" class="login-btn">登录</button>
      <div v-if="msg" class="message" :class="{ 'error': msg !== '登录成功' }">{{ msg }}</div>
      <div class="register-link">
        还没有账号？<a href="#" @click.prevent="$router.push('/register')">立即注册</a>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'UserLogin',
  data() {
    return {
      phoneNumber: '',
      password: '',
      msg: ''
    }
  },
  methods: {
    login() {
      this.msg = ''
      axios.post('/api/members/loginWithToken', {
        phoneNumber: this.phoneNumber,
        password: this.password
      }).then(res => {
        if (res.data.code === 0) {
          localStorage.setItem('token', res.data.token)
          this.msg = '登录成功'
          this.$router.push('/member-info')
        } else {
          this.msg = res.data.msg || '登录失败'
        }
      }).catch(error => {
        this.msg = error.response?.data?.msg || '请求失败'
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
  border-radius: 8px;
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

.login-btn {
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.login-btn:hover {
  background-color: #45a049;
}

.message {
  text-align: center;
  margin-top: 10px;
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

.register-link {
  text-align: center;
  margin-top: 10px;
}

.register-link a {
  color: #4CAF50;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style> 