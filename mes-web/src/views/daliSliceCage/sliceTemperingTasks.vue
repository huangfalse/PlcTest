<script lang="ts" setup>
import {useI18n} from 'vue-i18n'
import {useRouter} from "vue-router"
import request from "@/utils/request"
import {onMounted, ref} from "vue";
import {ElMessage, ElMessageBox} from 'element-plus'

const {t} = useI18n()
const router = useRouter()
const language = ref(localStorage.getItem('lang') || 'zh')
const timeRange = ref([])
const tableData = ref([])
const currentPage = ref(1)
const pageSize = ref(20)
const totalRecords = ref(0)
const engineerId = ref('')
const isForce = ref('')
const state = ref('')
const targetSlot = ref('')
const startSlot = ref('')
const dialogVisible = ref(false)
const editForm = ref({
  deviceId: 1,
  engineerId: '',
  temperingLayoutId: 0,
  isForce: 0,
  state: 0,
  target: 0
})
const currentRow = ref<any>()
const getglobalDate = window.localStorage.getItem('getglobalDate')
const fetchTemperingTasks = async (page: number = 1) => {
  try {
    const startTime = window.localStorage.getItem('startTime') || ''
    const endTime = getglobalDate || ''
    const response = await request.post("/verticalGlass/TemperingTask/queryTemperingTask", {
      pageNo: page,
      pageSize: pageSize.value,
      engineerId: engineerId.value,
      deviceId: 1,
      target: targetSlot.value || 0,
      isForce: isForce.value,
      state: state.value,
      startTime: timeRange.value ? formatTimestamp(timeRange.value[0]) : startTime,
      endTime: timeRange.value ? formatTimestamp(timeRange.value[1]) : endTime
    })
    if (response.code === 200) {
      const formattedData = response.data.records.map(record => ({
        ...record,
        formattedCreateTime: formatTimestamp(record.createTime),
        formattedUpdateTime: formatTimestamp(record.updateTime),
      }))
      tableData.value = formattedData
      totalRecords.value = response.data.total
      currentPage.value = page
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error(response.message)
  }
}
// 编辑方法
const handleEdit = (row: any) => {
  currentRow.value = row
  editForm.value = {
    deviceId: row.deviceId,
    engineerId: row.engineerId,
    temperingLayoutId: row.temperingLayoutId,
    isForce: row.isForce,
    state: row.state,
    target: row.target
  }
  dialogVisible.value = true
}
// 提交编辑
const submitEdit = async () => {
  try {
    const response = await request.post("/verticalGlass/TemperingTask/updateTemperingTask", {
      ...editForm.value,
      id: currentRow.value.id
    })
    if (response.code === 200) {
      ElMessage.success(response.message)
      dialogVisible.value = false
      fetchTemperingTasks(currentPage.value)
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    ElMessage.error(response.message)
  }
}
// 取消工程
const cancelProject = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('searchOrder.projectCancelWhether'),
        t('Mounting.prompt'),
        {
          confirmButtonText: t('Mounting.yes'),
          cancelButtonText: t('Mounting.cancel'),
          type: 'warning',
        }
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/verticalGlass/TemperingTask/cancelTemperingTask", {
        state: 7,
        deviceId: 1,
        engineerId: row.engineerId,
        temperingLayoutId: 0
      });
      if (response.code === 200) {
        ElMessage.success(response.message);
      } else {
        ElMessage.error(response.message);
      }
    }
  } catch (error) {
  }
  fetchTemperingTasks(1)
};
// 取消此炉
const cancelFurnace = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('searchOrder.furnaceCancelWhether'),
        t('Mounting.prompt'),
        {
          confirmButtonText: t('Mounting.yes'),
          cancelButtonText: t('Mounting.cancel'),
          type: 'warning',
        }
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/verticalGlass/TemperingTask/cancelTemperingTask", {
        state: 7,
        deviceId: 1,
        engineerId: row.engineerId,
        temperingLayoutId: row.temperingLayoutId
      });
      if (response.code === 200) {
        ElMessage.success(response.message);
      } else {
        ElMessage.error(response.message);
      }
    }
  } catch (error) {
  }
  fetchTemperingTasks(1)
};
const handlePageChange = (newPage: number) => {
  fetchTemperingTasks(newPage)
}
const handleSearch = () => {
  currentPage.value = 1
  fetchTemperingTasks(1)
}
const handleInputStartSlot = (value: string) => {
  startSlot.value = /^\d*$/.test(value) ? value : ''
}
const handleInputTargetSlot = (value: string) => {
  targetSlot.value = /^\d*$/.test(value) ? value : ''
}

function formatTimestamp(timestamp: any): string {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

const shortcuts = [
  {
    text: t('datepicker.lastWeek'),
    value: () => {
      const end = new Date(getglobalDate || Date.now())
      const start = new Date(end)
      start.setHours(0, 0, 0, 0)
      start.setDate(start.getDate() - 7)
      timeRange.value = [start, end]
      window.localStorage.setItem('startTime', formatTimestamp(start))
    }
  },
  {
    text: t('datepicker.lastMonth'),
    value: () => {
      const end = new Date(getglobalDate || Date.now())
      const start = new Date(end)
      start.setHours(0, 0, 0, 0)
      start.setMonth(start.getMonth() - 1)
      timeRange.value = [start, end]
      window.localStorage.setItem('startTime', formatTimestamp(start))
    }
  },
  {
    text: t('datepicker.lastThreeMonths'),
    value: () => {
      const end = new Date(getglobalDate || Date.now())
      const start = new Date(end)
      start.setHours(0, 0, 0, 0)
      start.setMonth(start.getMonth() - 3)
      timeRange.value = [start, end]
      window.localStorage.setItem('startTime', formatTimestamp(start))
    }
  }
]

function getIsForceTagType(isForce: number) {
  return isForce === 0 ? 'warning' : 'danger'
}

function getIsForceText(isForce: number) {
  return isForce === 0 ? t('hollow.notMandatory') : t('hollow.mandatory')
}

function getStateTagType(state: number) {
  switch (state) {
    case 0:
      return 'primary'
    case 1:
      return 'danger'
    case 2:
      return 'success'
    case 7:
      return 'info'
    default:
      return 'info'
  }
}

function getStateText(state: number) {
  switch (state) {
    case 0:
      return t('Mounting.notStarted')
    case 1:
      return t('Mounting.inAction')
    case 2:
      return t('film.finish')
    case 7:
      return t('workOrder.cancel')
    default:
      return t('common.unknown')
  }
}

onMounted(() => {
  const end = new Date(getglobalDate || Date.now())
  const start = new Date(end)
  start.setHours(0, 0, 0, 0)
  start.setDate(start.getDate() - 7)
  timeRange.value = [start, end]
  window.localStorage.setItem('startTime', formatTimestamp(start))
  fetchTemperingTasks(1)
})
</script>
<template>
  <div>
    <div class="search-container">
      <el-input v-model="engineerId" :placeholder="$t('order.enterProjectNumber')" clearable
                style="margin-left: 10px;width: 200px;"/>
      <el-select
          v-model="isForce"
          :placeholder="$t('hollow.forcedFilmingWhether')"
          clearable
          style="margin-left: 10px; width: 200px;"
      >
        <el-option :label="$t('hollow.notMandatory')" :value="0"/>
        <el-option :label="$t('hollow.mandatory')" :value="1"/>
      </el-select>
      <el-select
          v-model="state"
          :placeholder="$t('Mounting.state')"
          clearable
          style="margin-left: 10px; width: 200px;"
      >
        <el-option :label="$t('Mounting.notStarted')" :value="0"/>
        <el-option :label="$t('Mounting.inAction')" :value="1"/>
        <el-option :label="$t('film.finish')" :value="2"/>
        <el-option :label="$t('workOrder.cancel')" :value="7"/>
      </el-select>
      <el-date-picker
          v-model="timeRange"
          :end-placeholder="$t('workOrder.endTime')"
          :shortcuts="shortcuts"
          :start-placeholder="$t('workOrder.startTime')"
          range-separator="至"
          style="margin-left: 15px;"
          type="datetimerange"
      />
      <el-button
          style="margin-left: 10px;"
          type="primary"
          @click="handleSearch"
      >
        {{ $t('order.inquire') }}
      </el-button>
    </div>
    <el-table
        ref="table"
        :data="tableData"
        :header-cell-style="{ background: '#F2F3F5', color: '#1D2129' }"
        style="margin-top: 20px;height: 550px;width: 1770px;"
    >
      <el-table-column :label="$t('Mounting.deviceNumber')" align="center" min-width="80" prop="deviceId"/>
      <el-table-column :label="$t('searchOrder.projectnumber')" align="center" min-width="80" prop="engineerId"/>
      <el-table-column :label="$t('Mounting.temperedLayoutID')" align="center" min-width="80" prop="temperingLayoutId"/>
      <el-table-column :label="$t('searchOrder.target')" align="center" min-width="80" prop="target"/>
      <el-table-column
          :label="$t('hollow.forcedFilmingWhether')"
          align="center"
          min-width="80"
      >
        <template #default="scope">
          <el-tag :type="getIsForceTagType(scope.row.isForce)">
            {{ getIsForceText(scope.row.isForce) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
          :label="$t('Mounting.state')"
          align="center"
          min-width="80"
      >
        <template #default="scope">
          <el-tag :type="getStateTagType(scope.row.state)">
            {{ getStateText(scope.row.state) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('Mounting.creationTime')" align="center" min-width="100" prop="formattedCreateTime"/>
      <el-table-column :label="$t('searchOrder.updateTime')" align="center" min-width="100" prop="formattedUpdateTime"/>
      <el-table-column :label="$t('Mounting.operate')" align="center" fixed="right" width="400">
        <template #default="scope">
          <el-button :disabled="scope.row.state === 2 || scope.row.state === 7" plain type="text"
                     @click="handleEdit(scope.row)">{{ $t('productStock.exit') }}
          </el-button>
          <el-button :disabled="scope.row.state !== 0" plain type="text" @click="cancelProject(scope.row)">
            {{ $t('searchOrder.projectCancel') }}
          </el-button>
          <el-button :disabled="scope.row.state !== 0" plain type="text" @click="cancelFurnace(scope.row)">
            {{ $t('searchOrder.furnaceCancel') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="$t('productStock.exit')" width="40%">
      <el-form :model="editForm" label-width="120px">
        <el-form-item :label="$t('Mounting.deviceNumber')">
          <el-input v-model="editForm.deviceId" disabled/>
        </el-form-item>
        <el-form-item :label="$t('searchOrder.projectnumber')">
          <el-input v-model="editForm.engineerId" disabled/>
        </el-form-item>
        <el-form-item :label="$t('Mounting.temperedLayoutID')">
          <el-input v-model="editForm.temperingLayoutId" disabled/>
        </el-form-item>
        <el-form-item :label="$t('searchOrder.target')">
          <el-input v-model="editForm.target"/>
        </el-form-item>
        <el-form-item :label="$t('hollow.forcedFilmingWhether')">
          <el-select v-model="editForm.isForce" style="width: 100%">
            <el-option :label="$t('hollow.notMandatory')" :value="0"/>
            <el-option :label="$t('hollow.mandatory')" :value="1"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('Mounting.state')">
          <el-select v-model="editForm.state" style="width: 100%">
            <el-option :label="$t('Mounting.notStarted')" :value="0"/>
            <el-option :label="$t('Mounting.inAction')" :value="1"/>
            <el-option :label="$t('film.finish')" :value="2"/>
            <el-option :label="$t('workOrder.cancel')" :value="7"/>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div style="display: flex; justify-content: center; align-items: center;">
          <el-button @click="dialogVisible = false">{{ $t('Mounting.cancel') }}</el-button>
          <el-button type="primary" @click="submitEdit">{{ $t('Mounting.confirm') }}</el-button>
        </div>
      </template>
    </el-dialog>
    <div class="pagination-container">
      <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="totalRecords"
          layout="prev, pager, next, jumper"
          @current-change="handlePageChange"
      />
    </div>
  </div>
</template>
<style scoped>
.search-container {
  display: flex;
  width: 100%;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>