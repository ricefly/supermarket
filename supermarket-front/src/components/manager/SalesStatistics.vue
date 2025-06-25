<template>
  <ManagerLayout>
    <div class="sales-statistics">
      <h2>销售统计</h2>
      <el-form :inline="true" class="filter-form" @submit.prevent>
        <el-form-item label="年份">
          <el-select v-model="year" placeholder="请选择年份" style="width: 120px">
            <el-option v-for="y in yearOptions" :key="y" :label="y" :value="y" />
          </el-select>
        </el-form-item>
        <el-form-item label="月份">
          <el-select v-model="month" placeholder="请选择月份" style="width: 100px">
            <el-option v-for="m in 12" :key="m" :label="m" :value="m" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchMonthlyStatistics">查询月度</el-button>
          <el-button @click="fetchYearlyStatistics">查询年度</el-button>
        </el-form-item>
      </el-form>
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else>
        <div v-if="statistics">
          <el-card class="stat-card" shadow="hover">
            <template #header>销量最大商品</template>
            <div v-if="statistics.maxSalesProduct">
              <span>商品ID：{{ statistics.maxSalesProduct.productId }}</span>，
              <span>销量：{{ statistics.maxSalesProduct.totalQuantity }}</span>，
              <span>销售额：￥{{ statistics.maxSalesProduct.totalAmount.toFixed(2) }}</span>
            </div>
            <div v-else>暂无数据</div>
          </el-card>
          <el-card class="stat-card" shadow="hover">
            <template #header>销售额最大商品</template>
            <div v-if="statistics.maxRevenueProduct">
              <span>商品ID：{{ statistics.maxRevenueProduct.productId }}</span>，
              <span>销量：{{ statistics.maxRevenueProduct.totalQuantity }}</span>，
              <span>销售额：￥{{ statistics.maxRevenueProduct.totalAmount.toFixed(2) }}</span>
            </div>
            <div v-else>暂无数据</div>
          </el-card>
          <el-table :data="statistics.productStats" stripe style="width: 100%; margin-top: 24px">
            <el-table-column prop="productId" label="商品ID" width="120" />
            <el-table-column prop="totalQuantity" label="销售量" width="120" />
            <el-table-column prop="totalAmount" label="销售额(元)" width="160">
              <template #default="scope">
                <span v-if="scope && scope.row && scope.row.totalAmount != null">
                  ￥{{ Number(scope.row.totalAmount).toFixed(2) }}
                </span>
                <span v-else>--</span>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div v-else class="no-data">暂无统计数据</div>
      </div>
    </div>
  </ManagerLayout>
</template>

<script>
import ManagerLayout from './ManagerLayout.vue'
import axios from 'axios'
export default {
  name: 'SalesStatistics',
  components: { ManagerLayout },
  data() {
    const now = new Date()
    return {
      year: now.getFullYear(),
      month: now.getMonth() + 1,
      yearOptions: [now.getFullYear(), now.getFullYear() - 1],
      statistics: null,
      loading: false
    }
  },
  methods: {
    fetchMonthlyStatistics() {
      this.loading = true
      axios.get('/api/manager/statistics/monthly', {
        params: { year: this.year, month: this.month },
        headers: { token: localStorage.getItem('employeeToken') }
      }).then(res => {
        this.statistics = res.data
      }).finally(() => {
        this.loading = false
      })
    },
    fetchYearlyStatistics() {
      this.loading = true
      axios.get('/api/manager/statistics/yearly', {
        params: { year: this.year },
        headers: { token: localStorage.getItem('employeeToken') }
      }).then(res => {
        this.statistics = res.data
      }).finally(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
.sales-statistics {
  padding: 32px;
}
.filter-form {
  margin-bottom: 24px;
}
.stat-card {
  display: inline-block;
  width: 320px;
  margin-right: 24px;
  margin-bottom: 24px;
  vertical-align: top;
}
.loading {
  color: #409EFF;
  font-size: 18px;
  margin: 32px 0;
}
.no-data {
  color: #999;
  font-size: 16px;
  margin: 32px 0;
}
</style> 