<script setup lang="ts">
import {onBeforeUnmount, onMounted, ref} from "vue";
import {ElMessage} from 'element-plus'
import {closeWebSocket, initializeWebSocket} from '@/utils/WebSocketService';
import {useI18n} from 'vue-i18n'
import {useRouter} from 'vue-router'
import {fetchLyBeMessage, updateState} from '@/api/tempering'

const { t } = useI18n()
const router = useRouter()
const adjustedPrompt = ref([]) // 钢化前数据
const adjustedTemper = ref([]) // 钢化中数据
const dialogBaked = ref(false)
const cantakeBaked = ref(true)
const canSelectProjectOut = ref(true)
const currentRect = ref()
const currentGlassId = ref()
const deviceId = ref<string | null>(null)
const glassId = ref<string | null>(null)
const engineerId = ref<string | null>(null)
const temperingLayoutId = ref<string | null>(null)
const currenttemperingFeedSequence = ref()
const activeRectId = ref()
// 获取数据
const fetchData = async () => {
  try {
    const result = await fetchLyBeMessage()
    const tempIndexMessage = result.data.tempIndexMessageVOList?.[0]
    if (tempIndexMessage) {
      // 钢化前
      if (tempIndexMessage.promptList) {
        processPromptData(tempIndexMessage.promptList)
      }
      // 钢化中
      if (tempIndexMessage.temperingGlassInfoList) {
        processTemperData(tempIndexMessage.temperingGlassInfoList)
      }
    }
  } catch (error) {
    console.error('Fetch data error:', error)
  }
}
// 钢化前
const processPromptData = (data) => {
  adjustedPrompt.value = data.map(rect => {
    const existingRect = adjustedPrompt.value.find(r => r.id === rect.id)
    const isActive = existingRect ? existingRect.isActive : false
    const scaleFactor = 1621.78 / 8000
    const scaleFactorY = 223 / 1700
    let adjustedWidth, adjustedHeight, widtha, heighta
    if (rect.width < rect.height) {
      widtha = rect.height
      heighta = rect.width
    } else {
      widtha = rect.width
      heighta = rect.height
    }
    let newX = rect.ycoordinate
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
      x: newX * scaleFactor,
      y: rect.xcoordinate * scaleFactorY,
      width: adjustedWidth,
      height: adjustedHeight,
      widtha: rect.width,
      heighta: rect.height,
      isActive: activeRectId.value === rect.id ? true : isActive
    }
  })
}
// 钢化中
const processTemperData = (data) => {
  adjustedTemper.value = data.map(rect => {
    const existingRect = adjustedTemper.value.find(r => r.id === rect.id)
    const isActive = existingRect ? existingRect.isActive : false
    const scaleFactor = 1621.78 / 6000
    const scaleFactorY = 542 / 2850
    let adjustedWidth, adjustedHeight, widtha, heighta
    if (rect.width < rect.height) {
      widtha = rect.height
      heighta = rect.width
    } else {
      widtha = rect.width
      heighta = rect.height
    }
    let newX = rect.ycoordinate
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
      x: newX * scaleFactor,
      y: rect.xcoordinate * scaleFactorY,
      width: adjustedWidth,
      height: adjustedHeight,
      widtha: rect.width,
      heighta: rect.height,
      isActive: activeRectId.value === rect.id ? true : isActive
    }
  })
}
// WebSocket 消息处理
const socketUrl = `ws://${window.ipConfig.serverUrl}/temperingGlass/webSocket/tempingGlassBefore`
const handleMessage = (data) => {
  const tempIndexMessage = data?.tempIndexMessageVOList?.[0]
  if (tempIndexMessage) {
    if (tempIndexMessage.promptList) {
      processPromptData(tempIndexMessage.promptList)
    }
    if (tempIndexMessage.temperingGlassInfoList) {
      processTemperData(tempIndexMessage.temperingGlassInfoList)
    }
  }
}
// 显示对话框
function showDialogOut(rect) {
  activeRectId.value = rect.id
  deviceId.value = rect.deviceId
  glassId.value = rect.glassId
  engineerId.value = rect.engineerId
  temperingLayoutId.value = rect.temperingLayoutId
  const list = rect.widtha ? adjustedTemper.value : adjustedPrompt.value
  const index = list.findIndex(r => r.id === rect.id)
  if (index !== -1) {
    list.forEach(r => { r.isActive = false })
    list[index].isActive = true
    currentGlassId.value = rect.id
    currenttemperingFeedSequence.value = rect.temperingFeedSequence
    dialogBaked.value = true
    currentRect.value = rect
    canSelectProjectOut.value = rect.state !== 8
  }
}
// 关闭对话框
const handleDialogCloseOut = () => {
  activeRectId.value =
  adjustedTemper.value.forEach(rect => { rect.isActive = false })
  adjustedPrompt.value.forEach(rect => { rect.isActive = false })
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
    ElMessage.success(response.message)
    dialogBaked.value = false
    updateRectColorsOut()
  } catch (error) {
    console.error('Damage out error:', error)
  }
}
// 更新矩形颜色状态
function updateRectColorsOut() {
  adjustedTemper.value.forEach(rect => {
    if (rect.id === currentGlassId.value) {
      rect.state = 8
    }
  })
  adjustedPrompt.value.forEach(rect => {
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
  <div style="height: 860px;">
    <div style="margin-top: 10px; height: 100%; display: flex; flex-direction: column;">
      <!-- 钢化前 -->
     <el-card style="flex: 0 0 30%; margin-left: 10px; margin-right: 10px; margin-bottom: 10px;">
        <div style="height: 100%;">
          <el-scrollbar
          view-style="display: inline-block" height="223px" style="background-color: #e9e9eb; height: 100%; white-space: nowrap;">
            <div style="display: inline-block; min-width: 100%; height: 100%;">
              <div  
                v-for="(rect, index) in adjustedPrompt"
                :key="'prompt-' + index"
                @click="showDialogOut(rect)"  
                class="rect horizontal-rect"  
                :style="{ 
                  display: 'inline-block',
                  marginLeft: index === 0 ? '5px' : '10px',
                  width: `${rect.width}px`, 
                  height: `${rect.height}px`,
                  backgroundColor: rect.isActive ? '#ADFF2F' : getRectColorOut(rect.state),
                  verticalAlign: 'top'
                }">
                <div class="centered-text">
                  <div style="font-size: 10px;font-weight: bold;">{{ rect.glassId }}</div>  
                  <div style="font-size: 10px;font-weight: bold;">{{ rect.flowCardId }}</div>  
                  <div style="font-size: 15px;font-weight: bold;">{{ rect.widtha }}*{{ rect.heighta }}</div>  
                </div>
              </div> 
            </div>
          </el-scrollbar>
        </div>
      </el-card>
      <!-- 钢化中 -->
      <el-card style="flex: 0 0 67%; margin-left: 10px; margin-right: 10px; margin-top: 10px;">
        <div style="height: 100%;">
          <el-scrollbar height="542px" style="background-color: #e9e9eb; height: 100%;">
            <div style="position: relative; max-width: 1400px; height: 100%;">
              <div  
                v-for="(rect, index) in adjustedTemper"  
                :key="'temper-' + index"  
                @click="showDialogOut(rect)"  
                class="rect"  
                :style="{ 
                  position: 'absolute',  
                  top: `${rect.y}px`, 
                  left: `${rect.x}px`,
                  width: `${rect.width}px`, 
                  height: `${rect.height}px`,
                  backgroundColor: rect.isActive ? '#ADFF2F' : getRectColorOut(rect.state) 
                }">
                <div class="centered-text">
                  <div style="font-size: 10px;font-weight: bold;">{{ rect.glassId }}</div>  
                  <div style="font-size: 10px;font-weight: bold;">{{ rect.flowCardId }}</div>  
                  <div style="font-size: 15px;font-weight: bold;">{{ rect.widtha }}*{{ rect.heighta }}</div>
                </div>
              </div> 
            </div>
          </el-scrollbar>
        </div>
      </el-card>
      <el-dialog v-model="dialogBaked" top="30vh" width="15%" style="text-align: center;" @close="handleDialogCloseOut">
        <el-button :disabled="!canSelectProjectOut" type="warning" plain @click="handleDamageOut" style="width: 150px;margin-left: 10px;">
          {{ $t('workOrder.breakage') }}
        </el-button>
        <!-- <el-button :disabled="!cantakeBaked" type="danger" plain @click="takeoutOut" style="width: 150px;margin-left: 10px;margin-top: 10px;">
          <el-icon class="el-icon--right"><Upload /></el-icon>
          {{ $t('workOrder.takeout') }}
        </el-button> -->
      </el-dialog>
    </div>
  </div>
</template>
<style scoped>
.horizontal-rect {
  position: relative;
  top: auto !important;
  left: auto !important;
  /* 添加内联块显示 */
  display: inline-block;
  /* 确保文本垂直居中 */
  vertical-align: middle;
}
/* 确保滚动容器支持水平滚动 */
.el-scrollbar {
  overflow-x: auto;
  overflow-y: hidden;
}
.el-scrollbar__wrap {
  overflow-x: auto;
  overflow-y: hidden;
}
#boxa{
  border: 1px solid rgb(119, 116, 116);
  background-color:  #529b2e;
  text-align: center;
  display: inline-block;
  margin-left: 20px;
}
#boxb{
  border: 1px solid rgb(119, 116, 116);
  background-color:  #a0cfff;
  text-align: center;
  display: inline-block;
  align-items:center;
  justify-content:center;
  margin-left: 20px;
}
#box{
  border: 1px solid black;
  background-color:  #337ecc;
  display:flex;
  align-items:center; 
  justify-content:center;
}
#home-card {
  width: 100%;
  overflow: hidden;
  padding: 10px 0px;
  display: flex;
  flex-wrap: wrap;
  #home-item {
    border-style: solid;
    border-width: 1px;
    border-color: #E4E4E4;
    width: calc(34% - 20px);
    padding: 20px 0px 20px 20px;
    margin-right: 10px;
    margin-bottom: 10px;
    display: flex;
    justify-content: center;
    background: #fff;
    #home-img {
      display: inline-block;
      width: 160px;
      height: 60px;
      margin: 0;
      padding: 0;
    }
      #home-right {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: flex-start;
        margin-left: 10px;
        #home-num {
          font-size: 40px;
          margin: 5px 0;
        }
      }
    }
  }
  .rect {  
  border: 1px solid black; /* 设置矩形的边框 */
  cursor: pointer;
} 
.centered-text {
  display: flex;
  flex-direction: column;
  justify-content: center;  
  align-items: center; 
  height: 100%; /* 确保div占据整个矩形的高度 */
  width: 100%;
} 
.custom-title {  
  font-size: 20px;
  font-weight: bold;
} 
</style>