<template>
  <ManagerLayout>
    <div class="product-manage">
      <h2>商品管理</h2>
      <div class="search-bar">
        <input v-model="searchKeyword" placeholder="输入商品名称或种类查询" @keyup.enter="fetchProducts" />
        <button @click="fetchProducts">查询</button>
        <button @click="resetSearch">重置</button>
      </div>
      <button class="add-btn" @click="showAddDialog = true">新增商品</button>
      <table class="product-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>编号</th>
            <th>名称</th>
            <th>种类</th>
            <th>规格</th>
            <th>进货价</th>
            <th>零售价</th>
            <th>库存量</th>
            <th>库存位置</th>
            <th>低库存预警</th>
            <th>状态</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="prod in products" :key="prod.productId">
            <td>{{ prod.productId }}</td>
            <td>{{ prod.productCode }}</td>
            <td>{{ prod.productName }}</td>
            <td>{{ prod.category }}</td>
            <td>{{ prod.specification }}</td>
            <td>{{ prod.purchasePrice }}</td>
            <td>{{ prod.retailPrice }}</td>
            <td>{{ prod.currentStock }}</td>
            <td>{{ prod.stockLocation }}</td>
            <td>{{ prod.lowStockThreshold }}</td>
            <td>{{ prod.isActive ? '在售' : '下架' }}</td>
            <td>
              <button @click="editProduct(prod)">编辑</button>
              <button @click="offProduct(prod)" :disabled="!prod.isActive">下架</button>
            </td>
          </tr>
        </tbody>
      </table>
      <!-- 新增商品弹窗 -->
      <div v-if="showAddDialog" class="dialog-mask">
        <div class="dialog">
          <h3>新增商品</h3>
          <form @submit.prevent="addProduct">
            <input v-model="addForm.productCode" placeholder="商品编号" required />
            <input v-model="addForm.productName" placeholder="商品名称" required />
            <input v-model="addForm.category" placeholder="种类" required />
            <input v-model="addForm.specification" placeholder="规格" />
            <input v-model.number="addForm.purchasePrice" type="number" step="0.01" placeholder="进货价" required />
            <input v-model.number="addForm.retailPrice" type="number" step="0.01" placeholder="零售价" required />
            <input v-model.number="addForm.currentStock" type="number" placeholder="库存量" required />
            <input v-model="addForm.stockLocation" placeholder="库存位置" />
            <input v-model.number="addForm.lowStockThreshold" type="number" placeholder="低库存预警值" />
            <button type="submit">提交</button>
            <button type="button" @click="showAddDialog = false">取消</button>
          </form>
        </div>
      </div>
      <!-- 编辑商品弹窗 -->
      <div v-if="showEditDialog" class="dialog-mask">
        <div class="dialog">
          <h3>编辑商品</h3>
          <form @submit.prevent="updateProduct" class="form-vertical">
            <div class="form-row">
              <label>商品编号：</label>
              <input v-model="editForm.productCode" placeholder="商品编号" required />
            </div>
            <div class="form-row">
              <label>商品名称：</label>
              <input v-model="editForm.productName" placeholder="商品名称" required />
            </div>
            <div class="form-row">
              <label>种类：</label>
              <input v-model="editForm.category" placeholder="种类" required />
            </div>
            <div class="form-row">
              <label>规格：</label>
              <input v-model="editForm.specification" placeholder="规格" />
            </div>
            <div class="form-row">
              <label>进货价：</label>
              <input v-model.number="editForm.purchasePrice" type="number" step="0.01" placeholder="进货价" required />
            </div>
            <div class="form-row">
              <label>零售价：</label>
              <input v-model.number="editForm.retailPrice" type="number" step="0.01" placeholder="零售价" required />
            </div>
            <div class="form-row">
              <label>库存量：</label>
              <input v-model.number="editForm.currentStock" type="number" placeholder="库存量" required />
            </div>
            <div class="form-row">
              <label>库存位置：</label>
              <input v-model="editForm.stockLocation" placeholder="库存位置" />
            </div>
            <div class="form-row">
              <label style="width:auto; min-width:110px;">低库存预警值：</label>
              <input v-model.number="editForm.lowStockThreshold" type="number" placeholder="低库存预警值" />
            </div>
            <div class="form-row">
              <label>状态：</label>
              <select v-model="editForm.isActive">
                <option :value="true">在售</option>
                <option :value="false">下架</option>
              </select>
            </div>
            <div style="margin-top: 16px; text-align: right;">
              <button type="submit">保存</button>
              <button type="button" @click="showEditDialog = false">取消</button>
            </div>
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
  name: 'ProductManage',
  components: { ManagerLayout },
  data() {
    return {
      products: [],
      showAddDialog: false,
      showEditDialog: false,
      addForm: {
        productCode: '',
        productName: '',
        category: '',
        specification: '',
        purchasePrice: '',
        retailPrice: '',
        currentStock: '',
        stockLocation: '',
        lowStockThreshold: ''
      },
      editForm: {
        productId: '',
        productCode: '',
        productName: '',
        category: '',
        specification: '',
        purchasePrice: '',
        retailPrice: '',
        currentStock: '',
        stockLocation: '',
        lowStockThreshold: '',
        isActive: true
      },
      searchKeyword: ''
    }
  },
  created() {
    this.fetchProducts()
  },
  methods: {
    fetchProducts() {
      axios.get('/api/manager/products', {
        params: { keyword: this.searchKeyword }
      }).then(res => {
        this.products = res.data
      })
    },
    resetSearch() {
      this.searchKeyword = ''
      this.fetchProducts()
    },
    addProduct() {
      // 简单表单校验
      if (!this.addForm.productCode || !this.addForm.productName || !this.addForm.category) {
        alert('商品编号、名称、种类为必填项');
        return;
      }
      if (this.addForm.purchasePrice === '' || this.addForm.retailPrice === '' || this.addForm.currentStock === '') {
        alert('进货价、零售价、库存量为必填项');
        return;
      }
      if (this.addForm.purchasePrice < 0 || this.addForm.retailPrice < 0) {
        alert('价格不能为负数');
        return;
      }
      if (this.addForm.currentStock < 0) {
        alert('库存量不能为负数');
        return;
      }
      if (Number(this.addForm.purchasePrice) > Number(this.addForm.retailPrice)) {
        alert('进货价不能大于零售价');
        return;
      }
      axios.post('/api/manager/products', this.addForm).then(() => {
        this.showAddDialog = false
        this.fetchProducts()
        this.addForm = {
          productCode: '', productName: '', category: '', specification: '',
          purchasePrice: '', retailPrice: '', currentStock: '', stockLocation: '', lowStockThreshold: ''
        }
      })
    },
    editProduct(prod) {
      this.editForm = { ...prod }
      this.showEditDialog = true
    },
    updateProduct() {
      // 简单表单校验
      if (!this.editForm.productCode || !this.editForm.productName || !this.editForm.category) {
        alert('商品编号、名称、种类为必填项');
        return;
      }
      if (this.editForm.purchasePrice === '' || this.editForm.retailPrice === '' || this.editForm.currentStock === '') {
        alert('进货价、零售价、库存量为必填项');
        return;
      }
      if (this.editForm.purchasePrice < 0 || this.editForm.retailPrice < 0) {
        alert('价格不能为负数');
        return;
      }
      if (this.editForm.currentStock < 0) {
        alert('库存量不能为负数');
        return;
      }
      if (Number(this.editForm.purchasePrice) > Number(this.editForm.retailPrice)) {
        alert('进货价不能大于零售价');
        return;
      }
      axios.put(`/api/manager/products/${this.editForm.productId}`, this.editForm).then(() => {
        this.showEditDialog = false
        this.fetchProducts()
      })
    },
    offProduct(prod) {
      axios.post(`/api/manager/products/${prod.productId}/off`).then(() => {
        this.fetchProducts()
      })
    }
  }
}
</script>
<style scoped>
.product-manage {
  max-width: 1400px;
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
.product-table {
  width: 100%;
  min-width: 1300px;
  border-collapse: collapse;
  margin-bottom: 24px;
}
.product-table th, .product-table td {
  border: 1px solid #eee;
  padding: 10px 8px;
  text-align: center;
}
.product-table th {
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
.dialog label {
  display: block;
  margin-bottom: 10px;
  font-weight: 500;
}
.form-vertical .form-row {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}
.form-vertical label {
  width: 110px;
  min-width: 110px;
  font-weight: 500;
  margin-right: 10px;
  text-align: right;
  white-space: nowrap;
}
.form-vertical input,
.form-vertical select {
  flex: 1;
  min-width: 0;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 15px;
}
</style> 