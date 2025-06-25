<template>
  <ManagerLayout>
    <div class="employee-manage">
      <h2>员工管理</h2>
      <div class="search-bar">
        <input v-model="searchKeyword" placeholder="输入用户名或姓名查询" @keyup.enter="fetchEmployees" />
        <button @click="fetchEmployees">查询</button>
        <button @click="resetSearch">重置</button>
      </div>
      <button class="add-btn" @click="showAddDialog = true">新增员工</button>
      <table class="employee-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>姓名</th>
            <th>角色</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="emp in employees" :key="emp.employeeId">
            <td>{{ emp.employeeId }}</td>
            <td>{{ emp.username }}</td>
            <td>{{ emp.name }}</td>
            <td>{{ emp.role }}</td>
            <td>{{ emp.status }}</td>
            <td>
              <button @click="editEmployee(emp)">编辑</button>
              <button @click="disableEmployee(emp)" :disabled="emp.status === '禁用'">禁用</button>
            </td>
          </tr>
        </tbody>
      </table>

      <!-- 新增员工弹窗 -->
      <div v-if="showAddDialog" class="dialog-mask">
        <div class="dialog">
          <h3>新增员工</h3>
          <form @submit.prevent="addEmployee">
            <input v-model="addForm.username" placeholder="用户名" required />
            <input v-model="addForm.name" placeholder="姓名" required />
            <input v-model="addForm.password" type="password" placeholder="密码" required />
            <select v-model="addForm.role" required>
              <option value="cashier">收银员</option>
              <option value="warehouse">仓库管理员</option>
              <option value="admin">管理员</option>
            </select>
            <button type="submit">提交</button>
            <button type="button" @click="showAddDialog = false">取消</button>
          </form>
        </div>
      </div>

      <!-- 编辑员工弹窗 -->
      <div v-if="showEditDialog" class="dialog-mask">
        <div class="dialog">
          <h3>编辑员工</h3>
          <form @submit.prevent="updateEmployee">
            <input v-model="editForm.name" placeholder="姓名" required />
            <select v-model="editForm.role" required>
              <option value="cashier">收银员</option>
              <option value="warehouse">仓库管理员</option>
              <option value="admin">管理员</option>
            </select>
            <select v-model="editForm.status" required>
              <option value="在职">在职</option>
              <option value="禁用">禁用</option>
            </select>
            <button type="submit">保存</button>
            <button type="button" @click="showEditDialog = false">取消</button>
          </form>
        </div>
      </div>
    </div>
  </ManagerLayout>
</template>

<script>
import ManagerLayout from './ManagerLayout.vue'
import axios from 'axios'
export default {
  name: 'EmployeeManage',
  components: { ManagerLayout },
  data() {
    return {
      employees: [],
      showAddDialog: false,
      showEditDialog: false,
      addForm: {
        username: '',
        name: '',
        password: '',
        role: 'cashier'
      },
      editForm: {
        employeeId: '',
        name: '',
        role: '',
        status: ''
      },
      searchKeyword: ''
    }
  },
  created() {
    this.fetchEmployees()
  },
  methods: {
    fetchEmployees() {
      axios.get('/api/manager/employees', {
        params: { keyword: this.searchKeyword }
      }).then(res => {
        this.employees = res.data
      })
    },
    resetSearch() {
      this.searchKeyword = ''
      this.fetchEmployees()
    },
    addEmployee() {
      axios.post('/api/manager/employees', {
        username: this.addForm.username,
        name: this.addForm.name,
        passwordHash: this.addForm.password, // 实际项目应加密
        role: this.addForm.role,
        status: '活跃'
      }).then(() => {
        this.showAddDialog = false
        this.fetchEmployees()
        this.addForm = { username: '', name: '', password: '', role: 'cashier' }
      })
    },
    editEmployee(emp) {
      this.editForm = { ...emp }
      this.showEditDialog = true
    },
    updateEmployee() {
      axios.put(`/api/manager/employees/${this.editForm.employeeId}`, {
        name: this.editForm.name,
        role: this.editForm.role,
        status: this.editForm.status
      }).then(() => {
        this.showEditDialog = false
        this.fetchEmployees()
      })
    },
    disableEmployee(emp) {
      axios.post(`/api/manager/employees/${emp.employeeId}/disable`).then(() => {
        this.fetchEmployees()
      })
    }
  }
}
</script>

<style scoped>
.employee-manage {
  max-width: 900px;
  margin: 40px auto;
  background: #fff;
  padding: 32px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
.add-btn {
  margin-bottom: 16px;
  padding: 8px 20px;
  background: #1976D2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.employee-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 24px;
}
.employee-table th, .employee-table td {
  border: 1px solid #eee;
  padding: 10px 8px;
  text-align: center;
}
.employee-table th {
  background: #f5f6fa;
}
.dialog-mask {
  position: fixed;
  left: 0; top: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.dialog {
  background: #fff;
  padding: 24px 32px;
  border-radius: 8px;
  min-width: 320px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.13);
}
.dialog input, .dialog select {
  display: block;
  width: 100%;
  margin-bottom: 16px;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 15px;
}
.dialog button {
  margin-right: 12px;
  padding: 8px 18px;
  background: #1976D2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.search-bar {
  margin-bottom: 16px;
  display: flex;
  gap: 8px;
}
.search-bar input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.search-bar button {
  padding: 8px 16px;
  background: #1976D2;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style> 