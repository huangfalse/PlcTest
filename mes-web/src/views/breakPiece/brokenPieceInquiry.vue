<template>
  <div style="height: 500px;">
    <div style="display: flex;">
      <el-input v-model="engineerId" style="margin-left: 15px;margin-top: 10px;width: 240px" :placeholder="$t('order.enterProjectNumber')"/>
          <el-button type="primary" style="margin-left: 10px;margin-top: 10px;" @click="sethistorical()">{{$t('order.inquire')}}</el-button>
       <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :size="size"
          :disabled="disabled"
          layout="prev, pager, next, jumper"
          :total="totalPages"
          @current-change="handleCurrentChange"
          style="margin-top: 10px;"
      />
      </div>
    <el-card style="flex: 1;margin-left: 10px;margin-top: 10px;margin-right: 10px;height: 800px;">
      <el-scrollbar height="750px" width="1400px" style="background-color: #e9e9eb;">
        <div style="position: relative;">
       <div
         v-for="(rect, index) in adjustedRects"  
         :key="rect.glassId"  
         class="rect"  
         @click="showDialog(rect.glassId)"  
         :style="{ position: 'absolute', 
         top: `${rect.yaxisa}px`, left: `${rect.xaxisa}px`, width: `${rect.width}px`, height: `${rect.height}px`,
         backgroundColor: rect.isActive ? '#ADFF2F' : getRectColor(rect.state)
          }"  
       >
     <div class="centered-text">
    <div style="font-size: 10px;font-weight: bold;">{{ rect.glassId }}</div>
    <div style="font-size: 10px;font-weight: bold;">{{ rect.flowCardId }}</div>
    <div style="font-size: 15px;font-weight: bold;">{{ rect.widtha }}*{{ rect.heighta }}</div>  
  </div>
  </div> 
   </div>
      </el-scrollbar>
      <el-dialog v-model="blind" top="30vh" width="15%" style="text-align: center;" @close="handleDialogClose">
        <el-select 
            :placeholder="$t('workOrder.lineSelection')"
             clearable
             style="width: 140px;margin-left: 10px;margin-bottom: 10px;"  
             v-model="patternSequence">
               <el-option
                v-for="item in optionsb"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
          </el-select>
        <el-button :disabled="!patternSequence || currentGlassRect?.state === 8 || currentGlassRect?.state === 9" type="warning"
                   plain :icon="Delete" @click="handleDamage(currentGlassId)" style="width: 140px;margin-left: 10px;">
          {{ $t('Mounting.dilapidation') }}
        </el-button>
        <el-button :disabled="!patternSequence || currentGlassRect?.state === 9 || currentGlassRect?.state === 8" type="danger"
                   plain @click="handleManualTake(currentGlassId)" style="width: 140px;margin-top: 10px;">
          <el-icon class="el-icon--right">
            <Upload/>
          </el-icon>
          {{ $t('Mounting.takenAwayManually') }}
        </el-button>
      </el-dialog>
    </el-card>
  </div>
</template>
<script setup lang="ts">
import request from "@/utils/request"
import {ElMessage} from 'element-plus'
import {computed, ref} from 'vue'
import {useI18n} from 'vue-i18n'
const engineerId = ref()
const realwidth = ref('')
const realheight = ref('')
const currentPage = ref('')
const patternSequence = ref()
const totalPages = ref(0)
const pageSize = ref(1)
const currentGlassId = ref(null)
const blind = ref(false)
const raw = ref([])
const rawData = ref([])
const adjustedRects = ref([])
const disabled = false
const size = 'small'
const {t} = useI18n()
let webSocket: WebSocket | null = null;  
let language = ref(localStorage.getItem('lang') || 'zh')
// 显示对话框并设置当前 glassId  
const currentGlassRect = computed(() => {
  return adjustedRects.value.find(rect => rect.glassId === currentGlassId.value);  
}); 
function showDialog(glassId: number) {
  currentGlassId.value = glassId;  
  blind.value = true;  
  adjustedRects.value = adjustedRects.value.map(rect =>  
  rect.glassId === glassId ? { ...rect, isActive: true } : rect  
  ); 
}  
const handleDialogClose = () => {  
  adjustedRects.value = adjustedRects.value.map(rect => ({  
    ...rect,  
    isActive: false  
  }));  
}
// 查询
const sethistorical = async () => {
     const response = await request.post('/cutting/cuttingJob/queryCutTerritoryByEngineerId', {
      engineerId: engineerId.value,
      rawSequence: 1,
      workLine: 1,
      isAll: 0,
    })
    if (response.code === 200) {
      const rawRects = response.data.cutTerritoryVOList;
      rawData.value = response.data;
      totalPages.value = response.data.totalRawSequence;
      realwidth.value = response.data.rawGlassWidth;
      realheight.value = response.data.rawGlassHeight;
      const realx = realwidth.value;
      const realy = realheight.value;
      const scaleFactor =  1621.78/realx; 
      const scaleFactory =  750/realy; 
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
       currentPage.value = 1;
    }  
};
// 点击页码切换
const handleCurrentChange = async(val: number) => {  
    const response = await request.post('/cutting/cuttingJob/queryCutTerritoryByEngineerId', {
      engineerId: engineerId.value,
      rawSequence: currentPage.value,
      workLine: 1,
      isAll: 0,
    })
    if (response.code === 200) {
      const rawRects = response.data.cutTerritoryVOList;
      rawData.value = response.data;
      realwidth.value = response.data.rawGlassWidth;
      realheight.value = response.data.rawGlassHeight;
      const realx = realwidth.value;
      const realy = realheight.value;
      const scaleFactor =  1621.78/realx; 
      const scaleFactory =  750/realy; 
      adjustedRects.value = rawRects.map(rect => ({  
        ...rect,
        xaxisa: rect.xaxis * scaleFactor,
        yaxisa: (realy - (rect.yaxis + rect.edgHeight )) * scaleFactory,
        width: rect.edgWidth * scaleFactor,
        widtha: rect.edgWidth,
        heighta: rect.edgHeight,
        height: rect.edgHeight * scaleFactory,
        state: rect.state
      })); 
    }
};
// 破损
const handleDamage = async () => {
  try  {
    const response = await request.post('/cutting/taskCache/identControls', {
      glassId: currentGlassId.value,
      state: 8,
      line: patternSequence.value,
      remark: '掰片',
      workingProcedure: '切割',
    })
    if (response.code == 200) {
      ElMessage.success(response.message);
      blind.value = false;
      patternSequence.value = ''
      updateRectStatus(currentGlassId.value, 8); 
    } else {
      ElMessage.error(response.msg);
    }
}
catch (error) {
    console.error(error);
  }
}
 // 人工拿走
const handleManualTake = async () => {
  try  {
    const response = await request.post('/cutting/taskCache/identControls', {
      glassId: currentGlassId.value,
      state: 9,
      line: patternSequence.value,
      workingProcedure: '切割',
      remark: '掰片',
    })
    if (response.code == 200) {
      ElMessage.success(response.message);
      blind.value = false;
      patternSequence.value = ''
      updateRectStatus(currentGlassId.value, 9); 
  } else {
      ElMessage.error(response.msg);
    }
}
catch (error) {
    console.error(error);
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
const optionsb = [
  {
    value: 1,
    label: t('order.breakPieceOne'),
  },
  {
    value: 2,
    label: t('order.breakPieceTwo'),
  },
]
// 更新矩形状态  
function updateRectStatus(glassId: string, status: number) {  
  adjustedRects.value.forEach(rect => {  
    if (rect.glassId === glassId) {  
      rect.state = status; // 更新矩形的状态  
    }  
  });  
}
</script>  
<style scoped>  
.rect {
  border: 1px solid black; /* 设置矩形的边框 */  
} 
.centered-text {
  justify-content: center;  
  align-items: center; 
  height: 100%;
}
#rect {
  position: relative; /* 确保箭头可以相对于矩形定位 */  
}  
#arrow {
  position: absolute;  
  top: 70%; /* 箭头位于矩形中间 */  
  left: 200px; /* 箭头在矩形左侧一些距离 */  
  transform: translateY(-50%); /* 垂直居中 */  
  width: 0;  
  height: 0;  
  border-top: 10px solid transparent; /* 上边框 */  
  border-bottom: 10px solid transparent; /* 下边框 */  
  border-right: 20px solid #911005; /* 右边框，形成箭头 */  
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
</style>