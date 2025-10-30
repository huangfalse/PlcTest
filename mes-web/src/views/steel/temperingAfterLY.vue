<script setup lang="ts">
import {onBeforeUnmount, onMounted, ref} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import {closeWebSocket, initializeWebSocket} from '@/utils/WebSocketService';
import {confirmPieceComplete, fetchLyMessage, updateState} from '@/api/tempering'

const {t} = useI18n()
const dialogBaked = ref(false)
const activeRectId = ref<string | null>(null)
const tempIndexMessageVOList = ref<Record<string, any[]>>({})
const divsData = ref<any[][]>([])
const currentGlassId = ref<string | null>(null)
const currenttemperingFeedSequence = ref<number | null>(null)
const canSelectProjectOut = ref(true)
const currentRect = ref<any>(null)
const deviceId = ref<string | null>(null)
const glassId = ref<string | null>(null)
const engineerId = ref<string | null>(null)
const temperingLayoutId = ref<string | null>(null)
// 获取数据
const fetchData = async () => {
  try {
    const result = await fetchLyMessage()
    tempIndexMessageVOList.value = result.data.tempIndexMessageVOList
    processReceivedData()
  } catch (error) {
    console.error('Fetch data error:', error)
  }
}
// WebSocket连接
const socketUrl = `ws://${window.ipConfig.serverUrl}/temperingGlass/webSocket/tempingGlassAfter`
const handleMessage = (data: any) => {
  if (data.tempIndexMessageVOList) {
    tempIndexMessageVOList.value = data.tempIndexMessageVOList
    processReceivedData()
  }
}
// 处理接收到的数据，转换为divsData格式
const processReceivedData = () => {
  divsData.value = Object.entries(tempIndexMessageVOList.value).map(([key, group]) => {
    const glassList = group.temperingGlassInfoList || []
    return glassList.map(rect => ({
      ...rect,
      groupKey: key // 保留组键用于显示
    }))
  }
  )
}
// 获取调整后的矩形（根据组索引）
const getAdjustedRectsForGroup = (group: any[]) => {
  return group.map(rect => {
    const scaleFactor = 1611.78 / 6000
    const scaleFactorY = 750 / 2750
    let adjustedWidth, adjustedHeight, widtha, heighta
    let newX = rect.ycoordinate
    if (rect.width < rect.height) {
      widtha = rect.height
      heighta = rect.width
    } else {
      widtha = rect.width
      heighta = rect.height
    }
    if (rect.angle === 0) {
      adjustedWidth = widtha * scaleFactor
      adjustedHeight = heighta * scaleFactorY
      newX = 6000 - (rect.ycoordinate + widtha)
    } else {
      adjustedWidth = heighta * scaleFactor
      adjustedHeight = widtha * scaleFactorY
      newX = 6000 - (rect.ycoordinate + heighta)
    }
    return {
      ...rect,
      x_axis: newX * scaleFactor,
      y_axis: rect.xcoordinate * scaleFactorY,
      width: adjustedWidth,
      height: adjustedHeight,
      widtha: rect.width,
      heighta: rect.height,
      isActive: activeRectId.value === rect.id // 计算是否激活
    }
  })
}
// 显示对话框
function showDialogOut(rect: any) {
  activeRectId.value = rect.id
  currentGlassId.value = rect.id
  deviceId.value = rect.deviceId
  glassId.value = rect.glassId
  engineerId.value = rect.engineerId
  temperingLayoutId.value = rect.temperingLayoutId
  currenttemperingFeedSequence.value = rect.temperingFeedSequence
  currentRect.value = rect
  canSelectProjectOut.value = rect.state !== 8
  dialogBaked.value = true
}
// 关闭对话框
const handleDialogCloseOut = () => {
  activeRectId.value = null
  dialogBaked.value = false
}
// 破损处理
const handleDamageOut = async () => {
  if (!deviceId.value) {
    return
  }
  if (!glassId.value) {
    return
  }
  if (!engineerId.value) {
    return
  }
  if (!temperingLayoutId.value) {
    return
  }
  try {
    await updateState(deviceId.value, glassId.value, engineerId.value, temperingLayoutId.value)
    ElMessage.success()
    dialogBaked.value = false
    updateRectColorsOut()
  } catch (error) {
    console.error('Handle damage error:', error)
  }
}
// 下片完成
const confirmMarkingComplete = async (group) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        t('processCard.nextPieceCompleteWhether'),
        t('Mounting.prompt'),
        {
          confirmButtonText: t('Mounting.confirm'),
          cancelButtonText: t('Mounting.cancel'),
          type: 'warning',
        }
    );
    if (confirmResult === 'confirm') {
      const engineerId = group[0]?.engineerId;
      const temperingLayoutId = group[0]?.temperingLayoutId;
      await confirmPieceComplete(engineerId, temperingLayoutId);
      ElMessage.success();
      await fetchData();
    }
  } catch (error) {
  }
};
// 更新矩形颜色状态
function updateRectColorsOut() {
  getAdjustedRectsForGroup.value.forEach(rect => {  
    if (rect.id === currentGlassId.value) {  
      rect.state = 8
    }
  })  
}
// 获取矩形颜色
function getRectColorOut(state) {  
  switch (state) {  
    case 3: return '#eebe77'  
    case 4: return '#CD6090'
    case 8: return '#911005' 
    case 9: return '#4682B4' 
    default: return '#CDAF95' 
  }  
}
onMounted(() => {
  fetchData()
  initializeWebSocket(socketUrl, handleMessage)
})
onBeforeUnmount(() => {
  closeWebSocket()
})
</script>
<template>
  <div style="height: 500px;">
    <div style="margin-top: 10px;">
   <div style="display: flex;margin-left: 50%;margin-bottom: 10px;">
  </div>
   <el-card style="flex: 1; margin-right: 10px;margin-left: 20px;">
          <div v-for="(group, groupIndex) in divsData" :key="groupIndex" class="row">  
            <div class="card-header">
               <span>{{ group[0]?.engineerId || 'N/A' }}-{{ group[0]?.temperingLayoutId || 'N/A' }}</span>
              <el-button style="margin-left: 700px;margin-top: 5px;" @click="confirmMarkingComplete(group)">
                {{ $t('processCard.nextPieceComplete') }}
              </el-button>
            </div>
            <el-scrollbar class="scrollbar-container" height="750px">
              <div class="content-container">
                  <div
                    v-for="(rect, index) in getAdjustedRectsForGroup(group)"
                    :key="rect.id"
                    @click="showDialogOut(rect)"  
                    class="rect"
                    :class="{ 'active-rect': rect.isActive }"
                    :style="{
                      position: 'absolute',
                      top: `${rect.y_axis}px`,
                      left: `${rect.x_axis}px`,
                      width: `${rect.width}px`,
                      height: `${rect.height}px`,
                      backgroundColor: rect.isActive ? '#ADFF2F' : getRectColorOut(rect.state),
                      cursor: 'pointer'
                    }"
                  >
                    <div class="centered-text">
                      <div style="font-size: 10px; font-weight: bold;">{{ rect.flowCardId }}</div>  
                      <div style="font-size: 10px; font-weight: bold;">{{ rect.glassId }}</div>  
                      <div style="font-size: 15px; font-weight: bold;">{{ rect.widtha }}*{{ rect.heighta }}</div>  
                    </div>
                  </div>
              </div>
            </el-scrollbar>
          </div>
        </el-card>
        <el-dialog 
          v-model="dialogBaked" 
          top="30vh" 
          width="15%" 
          style="text-align: center;" 
          @close="handleDialogCloseOut"
        >
          <el-button 
            :disabled="!canSelectProjectOut" 
            type="warning" 
            plain 
            @click="handleDamageOut"
            style="width: 150px; margin-left: 10px;"
          >
            {{ $t('Mounting.dilapidation') }}
          </el-button>
        </el-dialog> 
    </div>
  </div>
</template>
<style scoped>
.card-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
  height: 870px;
  overflow-y: auto;
}
.data-card {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.scrollbar-container {
  background-color: #e9e9eb;
  width: 100%;
}
.content-container {
  position: relative;
  width: 100%;
  height: 550px;
}
.rect {
  border: 1px solid #333;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s;
}
.rect:hover {
  background-color: #409EFF !important;
  color: white;
}
.active-rect {
  background-color: #409EFF !important;
  color: white;
}
.centered-text {
  text-align: center;
  width: 100%;
}
.card-header {
  font-weight: bold;
  margin-bottom: 10px;
  margin-left: 670px;
  /* text-align: center; */
  font-size: 16px;
}
</style>