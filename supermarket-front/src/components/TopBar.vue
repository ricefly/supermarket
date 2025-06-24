<template>
  <header class="topbar">
    <span class="logo">joja超市管理系统</span>
    <div class="right">
      <div class="dropdown-wrapper" v-if="employeeName">
        <span class="employee-name" @click="toggleMenu">欢迎，{{ employeeName }}
          <svg style="width:16px;height:16px;vertical-align:middle;margin-left:2px;" viewBox="0 0 24 24">
            <path fill="currentColor" d="M7,10L12,15L17,10H7Z" />
          </svg>
        </span>
        <div v-if="showMenu" class="dropdown-menu" @click.stop>
          <div class="dropdown-item" @click="logout">退出登录</div>
        </div>
      </div>
      <slot></slot>
    </div>
  </header>
</template>

<script>
export default {
  name: 'TopBar',
  props: {
    employeeName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      showMenu: false
    }
  },
  methods: {
    toggleMenu() {
      this.showMenu = !this.showMenu
    },
    logout() {
      this.showMenu = false
      this.$emit('logout')
    },
    handleClickOutside(e) {
      if (!this.$el.contains(e.target)) {
        this.showMenu = false
      }
    }
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutside)
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside)
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
.right {
  display: flex;
  align-items: center;
  margin-left: auto;
  margin-right: 32px;
  font-size: 16px;
  font-weight: normal;
  position: relative;
}
.employee-name {
  margin-right: 18px;
  font-size: 16px;
  font-weight: normal;
  cursor: pointer;
  user-select: none;
  position: relative;
  padding: 6px 12px;
  border-radius: 4px;
  transition: background 0.2s;
}
.employee-name:hover {
  background: rgba(255,255,255,0.08);
}
.dropdown-wrapper {
  position: relative;
}
.dropdown-menu {
  position: absolute;
  right: 0;
  top: 36px;
  min-width: 120px;
  background: #fff;
  color: #333;
  border-radius: 6px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.13);
  z-index: 1000;
  padding: 6px 0;
  font-size: 15px;
}
.dropdown-item {
  padding: 10px 20px;
  cursor: pointer;
  transition: background 0.2s;
}
.dropdown-item:hover {
  background: #f5f5f5;
  color: #1976D2;
}
</style> 