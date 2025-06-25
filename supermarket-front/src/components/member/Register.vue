<template>
  <div>
    <MemberTopBar />
    <div class="register-container">
      <h2>会员注册</h2>
      <form class="form-group" @submit.prevent="register">
        <input 
          v-model="phoneNumber" 
          type="tel" 
          placeholder="请输入手机号" 
          class="form-input" 
          required
          pattern="^1[3-9]\d{9}$"
          autocomplete="tel"/>
        <input 
          v-model="password" 
          type="password" 
          placeholder="请输入密码" 
          class="form-input" 
          required
          minlength="6"
          autocomplete="new-password"/>
        <input 
          v-model="confirmPassword" 
          type="password" 
          placeholder="请确认密码" 
          class="form-input" 
          required
          minlength="6"
          autocomplete="new-password"/>
        <input 
          v-model="name" 
          type="text" 
          placeholder="请输入姓名（选填）" 
          class="form-input"
          maxlength="50"/>
        <button type="submit" class="register-btn">注册</button>
        <div v-if="msg" class="message" :class="{ 'error': msg !== '注册成功' }">{{ msg }}</div>
        <div class="login-link">
          已有账号？<a href="#" @click.prevent="$router.push('/login')">立即登录</a>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import MemberTopBar from './MemberTopBar.vue'

export default {
  name: 'UserRegister',
  components: { MemberTopBar },
  data() {
    return {
      phoneNumber: '',
      password: '',
      confirmPassword: '',
      name: '',
      msg: ''
    }
  },
  methods: {
    register() {
      this.msg = ''
      
      // 验证手机号格式
      if (!/^1[3-9]\d{9}$/.test(this.phoneNumber)) {
        this.msg = '请输入正确的手机号'
        return
      }
      
      // 验证密码长度
      if (this.password.length < 6) {
        this.msg = '密码长度不能少于6位'
        return
      }
      
      // 验证两次密码是否一致
      if (this.password !== this.confirmPassword) {
        this.msg = '两次输入的密码不一致'
        return
      }

      axios.post('/api/members/register', {
        phoneNumber: this.phoneNumber,
        password: this.password,
        name: this.name || null
      }).then(res => {
        if (res.data === '注册成功') {
          this.msg = '注册成功'
          // 延迟1秒后跳转到登录页
          setTimeout(() => {
            this.$router.push('/login')
          }, 1000)
        } else {
          this.msg = res.data || '注册失败'
        }
      }).catch(error => {
        this.msg = error.response?.data || '注册失败，请稍后重试'
      })
    }
  }
}
</script>

<style scoped>
.register-container {
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

.form-input:focus {
  border-color: #4CAF50;
  outline: none;
}

.register-btn {
  padding: 10px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.register-btn:hover {
  background-color: #45a049;
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

.login-link {
  text-align: center;
  margin-top: 10px;
}

.login-link a {
  color: #4CAF50;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style> 