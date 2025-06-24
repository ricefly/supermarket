import { createRouter, createWebHistory } from 'vue-router'
import UserLogin from '../components/member/Login.vue'
import MemberInfo from '../components/member/MemberInfo.vue'
import UserRegister from '../components/member/Register.vue'
import EmployeeLogin from '../components/employee/Login.vue'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'UserLogin',
    component: UserLogin
  },
  {
    path: '/register',
    name: 'UserRegister',
    component: UserRegister
  },
  {
    path: '/member-info',
    name: 'MemberInfo',
    component: MemberInfo
  },
  {
    path: '/employee/login',
    name: 'EmployeeLogin',
    component: EmployeeLogin
  },
  {
    path: '/admin/dashboard',
    name: 'AdminDashboard',
    component: () => import('../components/admin/Dashboard.vue'),
    meta: { requiresAuth: true, role: 'admin' }
  },
  {
    path: '/manager/dashboard',
    name: 'ManagerDashboard',
    component: () => import('../components/manager/Dashboard.vue'),
    meta: { requiresAuth: true, role: 'manager' }
  },
  {
    path: '/cashier/pos',
    name: 'CashierPOS',
    component: () => import('../components/cashier/POS.vue'),
    meta: { requiresAuth: true, role: 'cashier' }
  },
  {
    path: '/warehouse/inventory',
    name: 'WarehouseInventory',
    component: () => import('../components/warehouse/Inventory.vue'),
    meta: { requiresAuth: true, role: 'warehouse' }
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 检查是否是员工路由
  if (to.path.startsWith('/admin') || to.path.startsWith('/manager') || 
      to.path.startsWith('/cashier') || to.path.startsWith('/warehouse')) {
    const token = localStorage.getItem('employeeToken')
    const role = localStorage.getItem('employeeRole')
    
    if (!token) {
      next('/employee/login')
      return
    }
    
    // 检查角色权限
    if (to.meta.role && to.meta.role !== role) {
      next('/employee/login')
      return
    }
  } else if (to.path !== '/login' && to.path !== '/register' && to.path !== '/employee/login') {
    // 会员路由检查
    const token = localStorage.getItem('token')
    if (!token) {
      next('/login')
      return
    }
  }
  
  next()
})

export default router
