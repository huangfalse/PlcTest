<script setup lang="ts">
import request from "@/utils/request"
import {useI18n} from 'vue-i18n'
import {ElMessage} from 'element-plus'
import {computed, onBeforeUnmount, onMounted, ref} from 'vue'
import {closeWebSocket, initializeWebSocket} from '@/utils/WebSocketService'
import {updateTempQueueState} from '@/api/breakPiece'
const blind = ref(false)
const currentGlassId = ref(null)
const inkageEntity = ref('#911005')
const ava = ref('')
const avanum = ref('')
const realwidth = ref('')
const realheight = ref('')
const tableData = ref([])
const adjustedRects = ref([])
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
request.post("/cutting/cuttingJob/queryAllMessageByWorkLine", {
  onlineState: 1,
      isAll: 0,
      workLine: props.lineNumber
}).then((response) => {
  if (response.code == 200) {
    inkageEntity.value = response.data.onlineState === 0 ? '#911005' : 'green';
    const formattedData = response.data.cutHistoryTaskList.map(record => ({
      ...record,
      formattedCreateTime: formatTimestamp(record.createTime),
      formattedUpdateTime: formatTimestamp(record.updateTime),
    }))
    tableData.value = formattedData
    ava.value = response.data.engineerId
      const rawRects = response.data.cutTerritoryVOList;
      realwidth.value = response.data.rawGlassWidth;
      realheight.value = response.data.rawGlassHeight;
      const realx = realwidth.value;
      const realy = realheight.value;
      const scaleFactor =  1621.78/realx;
    const scaleFactory = 550 / realy;
      adjustedRects.value = rawRects.map(rect => ({
        ...rect,
        xaxisa: rect.xaxis * scaleFactor,
        yaxisa: (realy - (rect.yaxis + rect.edgHeight )) * scaleFactory,
        width: rect.edgWidth * scaleFactor,
        widtha: rect.edgWidth,
        heighta: rect.edgHeight ,
        height: rect.edgHeight * scaleFactory,
        state: rect.state
      }));
  } else {
  }
})
const socketUrl = `ws://${window.ipConfig.serverUrl}/api/cutting/webSocket/${props.webSocketPath}`;
const handleMessage = (data: any) => {
  if (data.engineerId != null) {
  ava.value = data.engineerId
  }else{
    ava.value = ''
  }
  if (data.rawSequence != null) {
    avanum.value = data.rawSequence
  }else{
    avanum.value = ''
  }
  // // 联机状态
  // if (data.onlineState != null) {
  //   onlineState.value = data.onlineState === 0 ? '#911005' : 'green';
  // }
  if (data.cutTerritoryVOList != null) {
        realwidth.value = data.rawGlassWidth;
        realheight.value = data.rawGlassHeight;
        const realx = realwidth.value;
        const realy = realheight.value;
        const scaleFactor = 1621.78 / realx;
    const scaleFactory = 550 / realy;
         adjustedRects.value = data.cutTerritoryVOList.map(rect => ({  
        ...rect,
        xaxisa: rect.xaxis * scaleFactor,
        yaxisa: (realy - (rect.yaxis + rect.edgHeight )) * scaleFactory,
        width: rect.edgWidth * scaleFactor,
        widtha: rect.edgWidth,
        heighta: rect.edgHeight,
        height: rect.edgHeight * scaleFactory,
        state: rect.state
      }));
      } else if (data.cutTerritoryVOList == '') {
        adjustedRects.value = [];
      }
}
const currentGlassRect = computed(() => {
  return adjustedRects.value.find(rect => rect.glassId === currentGlassId.value);
});
// 更新矩形状态
function updateRectStatus(glassId: string, status: number) {
  adjustedRects.value.forEach(rect => {
    if (rect.glassId === glassId) {
      rect.state = status; // 更新矩形的状态  
    }
  });
}
// 点击弹窗
function showDialog(glassId: number) {
  currentGlassId.value = glassId;
  blind.value = true;
  adjustedRects.value = adjustedRects.value.map(rect =>
      rect.glassId === glassId ? {...rect, isActive: true} : rect
  );
}
const handleDialogClose = () => {
  adjustedRects.value = adjustedRects.value.map(rect => ({
    ...rect,
    isActive: false
  }));
}
// 破损
const handleDamage = async () => {
  if (!currentGlassId.value) return
  try {
    await updateTempQueueState(currentGlassId.value, props.lineNumber)
    ElMessage.success(response.message)
    blind.value = false
    updateRectStatus(currentGlassId.value, 8)
  } catch (error) {
    console.error('Damage out error:', error)
  }
}
function getRectColor(state: number): string {
  switch (state) {
    case 0:  
      return '#7AC5CD';
    case 1:
      return '#95d475';
    case -1:
      return '#99BBFF';
    case 2:  
      return 'lightblue';
    case 3:  
      return '#eebe77';  
    case 4:  
      return '#CD6090';
    case 8:
      return '#911005';
    case 9:
      return '#4682B4';
    default:
      return '#CDAF95'; 
  }
}

function getStateType(state) {
  switch (state) {
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

function getStateText(state) {
  switch (state) {
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
onMounted(() => {
  socket = initializeWebSocket(socketUrl, handleMessage);
});
onBeforeUnmount(() => {
  closeWebSocket();
});
</script>
<template>
  <div id="dotClass">
    <div>{{ $t('searchOrder.advanceStatus') }}</div>
    <i :style="{ marginTop: '2px', backgroundColor: inkageEntity, width: '18px', height: '18px', borderRadius: '50%', display: 'block' }"></i>
  </div>
  <el-card style="flex: 1;margin-left: 10px;margin-top: 10px;">
    <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
      <el-table ref="table" :data="tableData" :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }"
                height="150">
        <el-table-column :label="$t('Mounting.cutOriginalSequence')" align="center" prop="rawSequence"/>
        <el-table-column :label="$t('Mounting.workLine')" align="center" prop="workLine"/>
        <el-table-column :label="$t('Mounting.projectID')" align="center" prop="engineerId"/>
        <el-table-column :label="$t('Mounting.width')" align="center" prop="rawGlassWidth"/>
        <el-table-column :label="$t('Mounting.height')" align="center" prop="rawGlassHeight"/>
        <el-table-column :label="$t('Mounting.thickness')" align="center" prop="rawGlassThickness"/>
        <el-table-column :label="$t('searchOrder.membraneSystem')" align="center" prop="filmsId"/>
        <el-table-column :label="$t('searchOrder.taskstatus')" align="center" min-width="120" prop="state">
          <template #default="scope">
            <el-tag :type="getStateType(scope.row.state)">
              {{ getStateText(scope.row.state) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('Mounting.creationTime')" align="center" prop="formattedCreateTime" width="160"/>
        <el-table-column :label="$t('searchOrder.updateTime')" align="center" prop="formattedUpdateTime" width="160"/>
      </el-table>
    </div>
  </el-card>
  <div style="height: 500px;">
    <el-card style="flex: 1;margin-left: 10px;margin-top: 10px;margin-right: 10px;height: 600px;">
    <!-- <div style="text-align: center;"> {{ $t('order.heatNumber') }}：{{ ava }}-{{ avanum }} </div> -->
      <el-scrollbar height="550px" style="background-color: #e9e9eb;" width="1400px">
        <div style="position: relative;">
          <div
              v-for="(rect, index) in adjustedRects"
              :key="rect.glassId"
              class="rect"
              @click="showDialog(rect.glassId)"
              :style="{ position: 'absolute',
              top: `${rect.yaxisa}px`, left: `${rect.xaxisa}px`, width: `${rect.width}px`, height: `${rect.height}px`,
              backgroundColor: rect.isActive ? '#ADFF2F' : getRectColor(rect.state)
               }">
            <div class="centered-text">
              <div style="font-size: 10px;font-weight: bold;">{{ rect.glassId }}</div>
              <div style="font-size: 10px;font-weight: bold;">{{ rect.flowCardId }}</div>
              <div style="font-size: 15px;font-weight: bold;">{{ rect.widtha }}*{{ rect.heighta }}</div>
            </div>
          </div>
        </div>
      </el-scrollbar>
      <el-dialog v-model="blind" top="30vh" width="15%" style="text-align: center;" @close="handleDialogClose">
        <el-button :disabled="currentGlassRect?.state === 8 || currentGlassRect?.state === 9" type="warning"
                   plain :icon="Delete" @click="handleDamage(currentGlassId)" style="width: 140px;margin-left: 10px;">
          {{ $t('Mounting.dilapidation') }}
        </el-button>
      </el-dialog>
    </el-card>
  </div>
</template>
<style scoped>
.rect {
  border: 1px solid black; /* 设置矩形的边框 */
}
.centered-text {
  justify-content: center;
  align-items: center;
  height: 100%; /* 确保div占据整个矩形的高度 */
}
#line {
  position: absolute;
  top: 70%; /* 直线位于矩形中间 */
  left: 210px; /* 直线在箭头右侧一些距离 */
  transform: translateY(-50%); /* 垂直居中 */
  height: 2px; /* 直线的高度 */
  width: 240px; /* 直线的长度，根据需要调整 */
  background-color: #911005; /* 直线的颜色 */
}

#dotClass {
  display: flex;
  margin-left: 20px;
  size: 50px;
  margin-top: 20px;
  margin-bottom: 10px;
}
</style>