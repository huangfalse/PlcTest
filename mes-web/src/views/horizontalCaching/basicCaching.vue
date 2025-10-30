<script setup>
import request from "@/utils/request"
import {inject, onBeforeUnmount, onMounted, onUnmounted, ref} from "vue"
import {closeWebSocket, initializeWebSocket} from '@/utils/WebSocketService'
import {ElMessage, ElMessageBox} from 'element-plus'
import {useI18n} from 'vue-i18n'

const globalDate = inject('globalDate')
const tableDataEngineer = ref([])
const tableDataGlass = ref([])
const adjustedRects = ref([])
const timeRange = ref([])
const taskState = ref('')
const taskType = ref('')
const glassId = ref('')
const iframeUrl = ref('');
const dialogHistory = ref(false)
const {t} = useI18n()
let language = ref(localStorage.getItem('lang') || 'zh')
let socket = null;
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
})
const socketUrl = `ws://${window.ipConfig.serverUrl}/api/horizontal/webSocket/${props.webSocketPath}`;
const handleMessage = (data) => {
  const cageInfo = data.currentTask;


  if (cageInfo && cageInfo.createTime) {
    cageInfo.formattedCreateTime = formatTimestamp(cageInfo.createTime);
  }
  if (cageInfo && cageInfo.updateTime) {
    cageInfo.formattedupdateTime = formatTimestamp(cageInfo.updateTime);
  }

  //   工程表
  tableDataEngineer.value = data.cageDetails

  //   进片出片表
  if (cageInfo != null) {
    tableDataGlass.value = cageInfo;
  } else {
    tableDataGlass.value = [];
  }


  //   小片显示
  // adjustedRects.value = data.EdgStorageCageinfos[0].map((rect, index) => ({  
  //     ...rect,
  //     id: rect.id * 9,
  //     showRect: !!data.EdgStorageCageinfos[0][index]?.details[0]
  //   })); 
};

// 格式化后端时间并计算一周前的时间
const defaultTime = ref < [Date, Date] > ([new Date(), new Date()]);
function parseAndSetTime() {
  const backendTime = new Date(globalDate);
  const oneWeekAgo = new Date(backendTime.getTime() - 7 * 24 * 60 * 60 * 1000); // 减去7天
  oneWeekAgo.setHours(0, 0, 0, 0);
  timeRange.value = [formatTimestamp(oneWeekAgo), formatTimestamp(backendTime)];
  window.localStorage.setItem('startTime', formatTimestamp(oneWeekAgo))
}
// 格式化时间戳为年月日时间字符串的函数
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
const rowClassName = ({ row }) => {
  // 根据 enableState 设置行的 CSS 类
  return row.slot % 2 === 0 ? 'success-row' : '';
};
// 样式部分
const styles = `
  .success-row {
    --el-table-tr-bg-color: var(--el-color-info-light-9);
  }
`;
// 动态添加样式到组件
const styleElement = document.createElement('style');
styleElement.textContent = styles;
document.head.appendChild(styleElement);
// 历史任务
const handleHistorical = (row) => {
  dialogHistory.value = true;
  // iframeUrl.value = `/horizontalCaching/horizontalHistory`;
  // iframeUrl.value = `#/horizontalCaching/horizontalHistory?time=${Date.now()}`;
  iframeUrl.value = `${window.location.origin}/#/horizontalCaching/horizontalHistory`;
};
const changeEnableState = async (row) => {
  const newState = row.enableState === 1 ? 0 : 1;
  // 发送请求更新状态
  try {

    const response = await request.post("/horizontal/horizontalSheetCage/updateCageState", {
      id: row.id,
      enableState: newState, // 传递新的状态
    });

  } catch (error) {
    ElMessage.error($t('searchOrder.operationCancelled'));
  }
};
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
      const response = await request.post("/horizontal/horizontalSheetCageDetails/updateDetail", {
        glassId: row.glassId,
        state: 8,
        line: props.lineNumber,
        slot: row.slot,
        workingProcedure: '卧式理片笼',
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
// 拿走 
const handleBindRack = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('searchOrder.deleteMessageWhether'),
        t('searchOrder.prompt'),
        {
          confirmButtonText: t('workOrder.yes'),
        cancelButtonText: t('workOrder.cancel'),
          type: 'warning',
        }
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/horizontal/horizontalSheetCageDetails/updateDetail", {
        glassId: row.glassId,
        state: 4,
        slot: row.slot,
        line: props.lineNumber,
        workingProcedure: '卧式理片笼',
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
// 任务重置
const handleTask = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('searchOrder.taskResetWhether'),
        t('workOrder.prompt'),
        {
          confirmButtonText: t('workOrder.yes'),
        cancelButtonText: t('workOrder.cancel'),
          type: 'warning',
        }
    );

    const response = await request.post("/horizontal/horizontalSheetCageDetails/updateTask", {
      workLine: 1,
      glassId: row.glassId,
      state: 5
    })
  } catch (error) {
    console.error('handleSuccessfull error:', error);
  }
};
// 任务成功处理
const handleSuccessfull = async (row) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('searchOrder.successfullyProcessedWhether'),
        t('workOrder.prompt'),
        {
          confirmButtonText: t('workOrder.yes'),
        cancelButtonText: t('workOrder.cancel'),
          type: 'warning',
        }
    );
    if (confirmResult === 'confirm') {
      const response = await request.post("/horizontal/horizontalSheetCageDetails/updateTask", {
        workLine: 1,
        glassId: row.glassId,
        state: 1
      })
      if (response.code === 200) {
        ElMessage.success(response.message);
      } else {
        ElMessage.error(response.msg);
      }
    }
  } catch (error) {
    console.error('handleSuccessfull error:', error);
  }
};
//  // 删除 
//  const handleDelete = async(row) => {  
//   try {
//     const confirmResult = await ElMessageBox.confirm(  
//       t('searchOrder.deleteMessageWhether'), 
//       t('searchOrder.prompt'),  
//       {  
//         confirmButtonText: t('searchOrder.yes'), 
//         cancelButtonText: t('searchOrder.cancel'),
//         type: 'warning',  
//       } 
//     ); 
//     if (confirmResult === 'confirm') {
//       const response = await request.post("/cacheGlass/edgStorageCageDetails/removeEdgDetails?glassId="+encodeURIComponent(row.glassId))
//     if (response.code === 200) {
//      ElMessage.success(response.message);
//       } else {
//       ElMessage.error(response.message);
//       }  
//     }  
//   } catch (error) {
//   }  
// };
function getStatusTypeEnable(taskType) {
  switch (taskType) {
    case 1:
      return 'info';
    case 2:
      return 'success';
    case 3:
      return 'danger';
  }
}
function getStatusTextEnable(taskType) {
  switch (taskType) {
    case 1:
      return t('sorter.advanceTask');
    case 2:
      return t('sorter.outputTasks');
    case 3:
      return t('sorter.straightTasks');
  }
}
function getStatusTypeTask(taskState) {
  switch (taskState) {
    case 0:
      return 'primary';
    case 1:
      return 'warning';
    case 2:
      return 'success';
    case 3:
      return 'danger';
    case 7:
      return 'warning';
    case 8:
      return 'danger';
  }
}
function getStatusTextTask(taskState) {
  switch (taskState) {
    case 0:
      return t('Mounting.pending');//待执行 
    case 1:
      return t('Mounting.inAction');//执行中
    case 2:
      return t('film.missionAccomplished');//任务完成
    case 3:
      return t('film.taskFailed');//任务失败
    case 7:
      return t('Mounting.paused');//暂停
    case 8:
      return t('Mounting.dilapidation');//任务破损
  }
}
onMounted(() => {
  socket = initializeWebSocket(socketUrl, handleMessage);
  parseAndSetTime();
});
onUnmounted(() => {
  if (socket) {
    closeWebSocket(socket);
  }
});
onBeforeUnmount(() => {
  closeWebSocket();
});
const dialogVisible = ref(false)
const showCageDialog = () => {
  dialogVisible.value = true
}
</script>
<template>
  <div style="height: 500px;">
    <el-card style="flex: 1;margin-left: 10px;margin-top: 10px;">
      <el-button style="margin-bottom: 15px;margin-left: 20px;" type="primary" @click="handleHistorical">{{
          $t('searchOrder.historicaltasks')
        }}
      </el-button>
      <el-button style="margin-bottom: 15px;margin-left: 20px;" type="primary" @click="showCageDialog()">{{
          $t('searchOrder.cageDetails')
        }}
      </el-button>
      <el-dialog v-model="dialogVisible" :title="$t('searchOrder.cageDetails')" top="5vh" width="80%"
                 @close="handleDialogClose">
        <div style="height: 740px; overflow-y: auto; position: relative; width: 100%;">
          <div
              style="position: absolute; margin-top: 28px; left: 400px; display: flex; z-index: 10;white-space: nowrap;">
            <div>{{ $t('searchOrder.projectnumber') }}</div>
            <div style="margin-left: 135px;">{{ $t('Mounting.processCards') }}</div>
            <div style="margin-left: 152px;">{{ $t('Mounting.glassID') }}</div>
            <div style="margin-left: 135px;">{{ $t('searchOrder.width') }}</div>
            <div style="margin-left: 169px;">{{ $t('Mounting.height') }}</div>
            <div style="margin-left: 162px;">{{ $t('searchOrder.thickness') }}</div>
            <div style="margin-left: 135px;">{{ $t('Mounting.operate') }}</div>
          </div>
          <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
            <el-table ref="table" :data="tableDataEngineer"
                      :header-cell-style="{ background: '#F2F3F5', color: '#1D2129' }" row-key="id"
                      style="margin-top: 20px; height: 670px;">
              <el-table-column :label="$t('searchOrder.gridNumber')" align="center" min-width="10" prop="slot"/>
              <el-table-column :label="$t('searchOrder.enabledStatus')" align="center" min-width="10"
                               prop="enableState">
                <template #default="scope">
                  <el-tag :type="scope.row.enableState === 1 ? 'success' : 'danger'"
                          @click="changeEnableState(scope.row)">
                    {{ scope.row.enableState === 1 ? $t('searchOrder.enable') : $t('searchOrder.disable') }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column>
                <template #default="props">
                  <el-table :data="props.row.details" :header-cell-style="{ background: '#F2F3F5', color: '#1D2129' }"
                            style="width: 100%;">
                    <el-table-column align="center" min-width="80" prop="engineerId"/>
                    <el-table-column align="center" min-width="80" prop="flowCardId"/>
                    <el-table-column align="center" min-width="80" prop="glassId"/>
                    <el-table-column align="center" min-width="80" prop="width"/>
                    <el-table-column align="center" min-width="80" prop="height"/>
                    <el-table-column align="center" min-width="80" prop="thickness"/>
                    <el-table-column align="center" fixed="right" width="150">
                      <template #default="scope">
                        <el-button plain type="text" @click="handleTake(scope.row)">{{
                            $t('workOrder.breakage')
                          }}
                        </el-button>
                        <el-button plain type="text" @click="handleBindRack(scope.row)">{{
                            $t('Mounting.delete')
                          }}
                        </el-button>
                        <!-- <el-button type="text" plain @click="handleDelete(scope.row)">{{ $t('Mounting.delete')}} </el-button> -->
                      </template>
                    </el-table-column>
                  </el-table>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-dialog>
      <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;margin-top: 15px;">
        <el-table ref="table" :data="tableDataGlass" :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }"
                  height="200" @selection-change="handleSelectionChange">
          <el-table-column :label="$t('Mounting.glassID')" align="center" min-width="80" prop="glassId"/>
          <!-- <el-table-column :label="$t('searchOrder.outGlassID')" align="center" min-width="80" prop="glassId"/> -->
          <el-table-column :label="$t('searchOrder.currentLayerNumber')" align="center" min-width="90"
                           prop="startSlot"/>
          <el-table-column :label="$t('searchOrder.layer')" align="center" min-width="120" prop="targetSlot"/>
          <el-table-column :label="$t('searchOrder.taskstatus')" align="center" min-width="80" prop="taskState">
            <template #default="scope">
              <el-tag :type="getStatusTypeTask(scope.row.taskState)">
                {{ getStatusTextTask(scope.row.taskState) }}
              </el-tag>
            </template>


          </el-table-column>
          <el-table-column :label="$t('searchOrder.taskType')" align="center" min-width="80" prop="taskType">
            <template #default="scope">
              <el-tag :type="getStatusTypeEnable(scope.row.taskType)">
                {{ getStatusTextEnable(scope.row.taskType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column :label="$t('Mounting.creationTime')" align="center" min-width="120"
                           prop="formattedCreateTime"/>
          <el-table-column :label="$t('searchOrder.updateTime')" align="center" min-width="120"
                           prop="formattedupdateTime"/>

          <el-table-column :label="$t('Mounting.operate')" align="center" min-width="120">
            <template v-slot="scope">
              <el-button size="mini" type="primary" @click="handleSuccessfull(scope.row)">{{
                  $t('film.missionAccomplished')
                }}
              </el-button>
              <el-button size="mini" type="danger" @click="handleTask(scope.row)">{{ $t('film.resetTask') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
    <div class="awatch">
      <img alt="" src="/woshihuancun.png"
           style="width: 50%;height: 100%;margin-left: 25%;position: relative;margin-top: 115px;">
      <div style="position: relative; width: 500px;height: 95px;margin-top: -246px;margin-left: 615px;">
        <div v-for="(rect, index) in adjustedRects" v-show="rect.showRect" :key="rect" :style="{
          position: 'absolute',
          top: `${rect.id}px`, left: `-85px`, width: `435px`, height: `3px`,
          backgroundColor: '#409EFF'
        }">
        </div>
      </div>
    </div>
  </div>
  <!-- 历史任务 -->
  <el-dialog v-model="dialogHistory" top="5vh" width="95%" @close="iframeUrl = ''">
    <iframe :src="iframeUrl" frameborder="0" height="750px" marginheight="2000px" marginwidth="2000px"
            width="100%"></iframe>
  </el-dialog>
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

.awatch {
  height: 350px;
  width: 1500px;
  max-width: 100%;
  margin-left: 10%
}
</style>
