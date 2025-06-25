<template>
  <div class="dashboard-layout">
    <MemberTopBar :memberName="memberInfo && memberInfo.name ? memberInfo.name : '会员'" @logout="logout" />
    <div class="main-content">
      <aside class="sidebar">
        <ul>
          <li @click="$router.push('/member-info')">个人信息</li>
          <li @click="$router.push('/member/orders')">历史订单</li>
          <li @click="logout">退出登录</li>
        </ul>
      </aside>
      <div class="dashboard-content">
        <slot />
      </div>
    </div>
  </div>
</template>
<script>
import MemberTopBar from './MemberTopBar.vue'
import axios from 'axios'
export default {
  name: 'MemberLayout',
  components: { MemberTopBar },
  data() {
    return {
      memberInfo: null
    }
  },
  created() {
    const token = localStorage.getItem('token')
    if (token) {
      axios.get('/api/members/info', { headers: { token } }).then(res => {
        if (res.data.code === 0 && res.data.data) {
          this.memberInfo = res.data.data
        }
      })
    }
  },
  methods: {
    logout() {
      localStorage.removeItem('token')
      this.$router.push('/login')
    }
  },
  computed: {
    displayName() {
      if (!this.memberInfo) return ''
      return this.memberInfo.name || this.memberInfo.phoneNumber || '会员'
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
  background: #43a047;
  color: #fff;
}
.dashboard-content {
  flex: 1;
  padding: 40px 40px 0 40px;
}
</style> 