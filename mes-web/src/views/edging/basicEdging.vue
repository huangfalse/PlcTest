<script setup>
import request from "@/utils/request"
import {onBeforeUnmount, onMounted, onUnmounted, reactive, ref} from "vue";
import {useRouter} from "vue-router"
import {host, WebSocketHost} from '@/utils/constants'
import {closeWebSocket, initializeWebSocket} from '@/utils/WebSocketService';
import {ElMessage, ElMessageBox} from 'element-plus'
import {useI18n} from 'vue-i18n'

const { t } = useI18n()
const router = useRouter()
const tableData = ref([])
const timeRange = ref([])
const selectValue = reactive([])
let language = ref(localStorage.getItem('lang') || 'zh')
let socket = null;
const currentPage2 = ref(1)
const pageSize = ref(13) // 跟你接口参数保持一致
const totalRecords = ref(0)
const disabled = ref(false) // 可选
const props = defineProps({
  // 线路
  lineNumber: {
    type: Number,
    required: true
  },
  // websockect
  webSocketPath: {
    type: String,
    required: true
  },
  //   工作状态
  workingProcedure: {
    type: String,
    required: true
  },
})
const socketUrl = `ws://${WebSocketHost}:${host}/edging/webSocket/${props.webSocketPath}`;
const handleMessage = (data) => {
  const formattedTasks = data.queueTaskList.records.map(item => {
    if (item && item.createTime) {
      item.formattedCreateTime = formatTimestamp(item.createTime);
    }
    return item;
  });
  tableData.value = formattedTasks
};
const handlePageChange2 = (newPage) => {
  currentPage2.value = newPage
  setEdgGlassInfoRequest(newPage)
}
const setEdgGlassInfoRequest = async (page = 1) => {
  let stateList = []
  let cellList = []
  if (selectValue[0] !== undefined && selectValue[0] !== "") {
    cellList[0] = selectValue[0];
  }

  if (selectValue[1] !== undefined && selectValue[1] !== "") {
    stateList[0] = selectValue[1];
  }

  if (stateList.length === 0) {
    stateList = [0, 1]
  }
  if (cellList.length === 0) {
    cellList = [1, 2]
  }

  // 获取当前日期
  let currentDate = new Date();
// 获取最近两天的日期
  let twoDaysAgo = new Date(currentDate);
  twoDaysAgo.setDate(currentDate.getDate() - 2);


  let startTime = timeRange.value[0];
  let endTime = timeRange.value[1];
  if (!startTime) {
    startTime = twoDaysAgo.toISOString().slice(0, 19).replace('T', ' ');
  }
  if (!endTime) {
    endTime = currentDate.toISOString().slice(0, 19).replace('T', ' ');
  }
  let pageSize = 13
  // 发送请求
  const response = await request.post("/edging/edgQueueInfo/queryQueueInfoPage", {
    lineList: cellList,
    stateList: stateList,
    startTime,
    endTime,
    pageNo: page,           // 关键：用当前页
    pageSize: pageSize,     // 关键：用pageSize变量
  })
  if (response.code === 200) {
    ElMessage.success(response.message);
    tableData.value = response.data.records;
    totalRecords.value = response.data.total || 0 // 关键：赋值总条数
    // closeWebSocket(socket);
  } else {
    ElMessage.error(response.message);
  }
}
// 破损
const handleTake = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
      t('workOrder.breakMessageWhether'),
        t('workOrder.prompt'),
      {
        confirmButtonText: t('workOrder.yes'),
        cancelButtonText: t('workOrder.cancel'),
        type: 'warning',
      }
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/edging/edgQueueInfo/isEdgDamage", {
        glassId: row.glassId,
        stateList: [8],
        lineList: [row.workLine],
        workingProcedure: props.workingProcedure,

      })
      if (response.code === 200) {
        ElMessage.success(response.message);
      } else {
        ElMessage.error(response.msg);
      }
    }
  } catch (error) {
  }
};
// 重发 
const resendEdgTask = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('workOrder.taskBeforeEdgingWhether'),
        t('workOrder.prompt'),
      {
        confirmButtonText: t('workOrder.yes'),
        cancelButtonText: t('workOrder.cancel'),
        type: 'warning',
      }
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/edging/edgQueueInfo/resendEdgTask", {
        glassId: row.glassId,
        lineList: [row.workLine],
      })
      if (response.code === 200) {
        ElMessage.success(response.message);
      } else {
        ElMessage.error(response.msg);
      }
    }
  } catch (error) {
  }
};
onMounted(() => {
  socket = initializeWebSocket(socketUrl, handleMessage);
});
onUnmounted(() => {
  if (socket) {
    closeWebSocket(socket);
  }
});
function getStatusTypeTask(state) {
  switch (state) {
    case 0:
      return 'info';
    case 1:
      return 'success';
    case 2:
      return 'danger';
    case 8:
      return 'danger';
  }
}
function getStatusTextTask(state) {
  switch (state) {
    case 0:
      return t('workOrder.edging');
    case 1:
      return t('workOrder.finishEdging');
    case 2:
      return t('workOrder.finishEdging');
    case 8:
      return t('workOrder.breakage');
  }
}
function formatTimestamp(timestamp) {
  const date = new Date(timestamp);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，需要加1，并补零
  const day = String(date.getDate()).padStart(2, '0'); // 补零
  const hours = String(date.getHours()).padStart(2, '0'); // 补零（如果需要显示时间）
  const minutes = String(date.getMinutes()).padStart(2, '0'); // 补零（如果需要显示时间）
  const seconds = String(date.getSeconds()).padStart(2, '0'); // 补零（如果需要显示时间）
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}
function formatCreateTime(row) {
  if (!row.createTime) return '';
  const date = new Date(row.createTime);
  const pad = (n) => (n < 10 ? '0' + n : n);
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ` +
      `${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`;
}
onBeforeUnmount(() => {
  closeWebSocket();
});
</script>
<template>
  <div style="height: 500px;">
    <div>
      <el-card style="flex: 1;margin-left: 10px;margin-top: 20px;">
        <div style="display: flex;">
          <el-select v-model="selectValue[0]" :placeholder="$t('workOrder.lineSelection')" clearable filterable
                     style="margin-left: 20px;margin-bottom: 10px;">
            <el-option :label="$t('workOrder.edgingOne')" value="1"></el-option>
            <el-option :label="$t('workOrder.edgingTwo')" value="2"></el-option>
          </el-select>
          <el-select v-model="selectValue[1]" :placeholder="$t('workOrder.stateSelection')" clearable filterable
                     style="margin-left: 20px;margin-bottom: 10px;">
            <el-option :label="$t('workOrder.edging')" value="0"></el-option>
            <!-- <el-option :label="$t('workOrder.edging')" value="1"></el-option>   -->
            <el-option :label="$t('workOrder.finishEdging')" value="1"></el-option>
          </el-select>
          <span class="demonstration" style="margin-left: 20px;margin-top: 3px;">{{ $t('workOrder.time') }}</span>
          <el-date-picker v-model="timeRange" :end-placeholder="$t('workOrder.endTime')"
                          :start-placeholder="$t('workOrder.startTime')" range-separator="至" style="margin-left: 15px;"
                          type="datetimerange" value-format="YYYY-MM-DD hh:mm:ss">
          </el-date-picker>
          <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="setEdgGlassInfoRequest()">{{
              $t('order.inquire')
            }}
          </el-button>
        </div>
        <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
          <el-table ref="table" :data="tableData" :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }"
                    height="700" @selection-change="handleSelectionChange">
            <el-table-column :label="$t('Mounting.glassID')" align="center" min-width="180" prop="glassId"/>
            <el-table-column :label="$t('Mounting.width')" align="center" min-width="120" prop="width"/>
            <el-table-column :label="$t('Mounting.height')" align="center" min-width="80" prop="height"/>
            <el-table-column :label="$t('Mounting.thickness')" align="center" min-width="120" prop="thickness"/>
            <el-table-column :label="$t('workOrder.glassMembraneSystem')" align="center" min-width="120"
                             prop="filmsId"/>
            <el-table-column :label="$t('workOrder.line')" align="center" min-width="120" prop="workLine"/>
            <el-table-column :formatter="formatCreateTime" :label="$t('workOrder.time')" align="center" min-width="120"
                             prop="createTime"/>
            <el-table-column :label="$t('Mounting.state')" align="center" prop="state" width="200">
              <template #default="scope">
                <el-tag :type="getStatusTypeTask(scope.row.state)">
                  {{ getStatusTextTask(scope.row.state) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column :label="$t('Mounting.operate')" align="center" fixed="right" width="200">
              <template #default="scope">
                <el-button plain type="text" @click="handleTake(scope.row)">{{
                  $t('workOrder.breakage')
                }}
                </el-button>
                <el-button plain type="text" @click="resendEdgTask(scope.row)">{{
                  $t('workOrder.Resend')
                }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="margin-top: 20px;margin-left: 40%;">
            <el-pagination v-model:current-page="currentPage2" :disabled="disabled" :page-size="pageSize" :size="large"
                           :total="totalRecords" layout="prev, pager, next, jumper" style="margin-top: 10px;"
                           @current-change="handlePageChange2"/>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>
<style scoped>
#dt {
  display: block;
  float: left;
  line-height: 20px;
  margin-left: 100px;
}

#dta {
  display: block;
  float: left;
  line-height: 20px;
  margin-left: 80%;
}

#dialog-footer {
  text-align: center;
  margin-top: -15px;
}

#message {
  text-align: center;
  align-items: center;
  color: black;
  width: 200px;
  height: 100px;
  background-color: #337ecc;
  margin-left: 28%;
}

#awatch {
  height: 460px;
}
</style>