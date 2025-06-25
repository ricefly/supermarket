<template>
  <MemberLayout>
    <div class="member-info-container">
      <h2>会员信息</h2>
      <div v-if="memberInfo" class="info-content">
        <div class="info-item">
          <span class="label">手机号：</span>
          <span>{{ memberInfo.phoneNumber }}</span>
        </div>
        <div class="info-item">
          <span class="label">姓名：</span>
          <span>{{ memberInfo.name || '未设置' }}</span>
        </div>
        <div class="info-item">
          <span class="label">会员等级：</span>
          <span>{{ memberInfo.memberLevel || '普通会员' }}</span>
        </div>
        <div class="info-item">
          <span class="label">钱包余额：</span>
          <span>¥{{ memberInfo.walletBalance || '0.00' }}</span>
        </div>
        <div class="info-item">
          <span class="label">返利余额：</span>
          <span>¥{{ memberInfo.rebateBalance || '0.00' }}</span>
        </div>
        <div class="info-item">
          <span class="label">会员状态：</span>
          <span>{{ memberInfo.status === '活跃' ? '正常' : '已注销' }}</span>
        </div>
      </div>
      <div v-else class="loading">加载中...</div>
      <div class="button-group">
        <button @click="showDeactivateConfirm" class="btn deactivate-btn">注销账号</button>
      </div>

      <!-- 注销确认对话框 -->
      <div v-if="showConfirmDialog" class="confirm-dialog">
        <div class="confirm-content">
          <h3>确认注销账号</h3>
          <p>注销后账号将无法登录，余额将被冻结。是否确认注销？</p>
          <div class="confirm-buttons">
            <button @click="cancelDeactivate" class="btn cancel-btn">取消</button>
            <button @click="confirmDeactivate" class="btn confirm-btn">确认注销</button>
          </div>
        </div>
      </div>
    </div>
  </MemberLayout>
</template>

<script>
import axios from 'axios'
import MemberLayout from './MemberLayout.vue'

export default {
  name: 'MemberInfo',
  components: { MemberLayout },
  data() {
    return {
      memberInfo: null,
      showConfirmDialog: false
    }
  },
  created() {
    this.fetchMemberInfo()
  },
  methods: {
    fetchMemberInfo() {
      const token = localStorage.getItem('token')
      if (!token) {
        this.$router.push('/login')
        return
      }
      
      axios.get('/api/members/info', {
        headers: {
          'token': token
        }
      }).then(res => {
        if (res.data.code === 0 || typeof res.data === 'object') {
          this.memberInfo = typeof res.data.data === 'object' ? res.data.data : res.data
        } else {
          localStorage.removeItem('token')
          this.$router.push('/login')
        }
      }).catch(error => {
        console.error('获取会员信息失败:', error)
        localStorage.removeItem('token')
        this.$router.push('/login')
      })
    },
    logout() {
      localStorage.removeItem('token')
      this.$router.push('/login')
    },
    showDeactivateConfirm() {
      this.showConfirmDialog = true
    },
    cancelDeactivate() {
      this.showConfirmDialog = false
    },
    confirmDeactivate() {
      const token = localStorage.getItem('token')
      if (!token) {
        this.$router.push('/login')
        return
      }

      axios.post('/api/members/deactivate', null, {
        headers: {
          'token': token
        }
      }).then(res => {
        if (res.data === '会员已注销') {
          localStorage.removeItem('token')
          this.$router.push('/login')
        } else {
          alert(res.data || '注销失败')
        }
      }).catch(error => {
        console.error('注销失败:', error)
        alert('注销失败，请稍后重试')
      })
    }
  }
}
</script>

<style scoped>
.member-info-container {
  max-width: 600px;
  margin: 50px auto;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
  border-radius: 8px;
  position: relative;
}

.info-content {
  margin: 20px 0;
}

.info-item {
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.label {
  font-weight: bold;
  margin-right: 10px;
  color: #666;
}

.loading {
  text-align: center;
  color: #666;
  padding: 20px;
}

.button-group {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.btn {
  padding: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  flex: 1;
}

.logout-btn {
  background-color: #4CAF50;
  color: white;
}

.logout-btn:hover {
  background-color: #45a049;
}

.deactivate-btn {
  background-color: #f44336;
  color: white;
}

.deactivate-btn:hover {
  background-color: #d32f2f;
}

.confirm-dialog {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.confirm-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 90%;
  max-width: 400px;
}

.confirm-content h3 {
  margin-top: 0;
  color: #f44336;
}

.confirm-buttons {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.cancel-btn {
  background-color: #9e9e9e;
  color: white;
}

.cancel-btn:hover {
  background-color: #757575;
}

.confirm-btn {
  background-color: #f44336;
  color: white;
}

.confirm-btn:hover {
  background-color: #d32f2f;
}
</style> 