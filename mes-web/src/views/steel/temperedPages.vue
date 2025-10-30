<script setup lang="ts">
import { ref } from 'vue'
import { fetchTemperingGlassData, TemperingGlassGroup } from '@/api/tempering'
const engineerId = ref('')
const hasData = ref(false)
const divsData = ref<TemperingGlassGroup[]>([])
const getAdjustedRectsForGroup = (group: any[]) => {
  return group.map(rect => {
    const scaleFactor = 1621.78 / 6000
    const scaleFactorY = 700 / 2800
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
    }
  })
}

// 查询数据
const fetchData = async () => {
  try {
    if (!engineerId.value.trim()) {
      ElMessage.error(t('order.enterProjectNumber'))
      return;
    }
    divsData.value = await fetchTemperingGlassData(engineerId.value);
    hasData.value = divsData.value.length > 0;
  } catch (error) {
    hasData.value = false;
  }
};
import { useRoute } from 'vue-router'
const route = useRoute()
engineerId.value=route.query.engineerId || ''
if(engineerId.value!=''){
fetchData()
}
</script>
<template>
  <div style="height: 870px;">
    <div style="display: flex; margin-bottom: 10px;margin-top: 10px; align-items: center;">
      <div style="margin-left: 50px; font-size: large;">
        {{ $t('Mounting.projectNumberColon') }}
      </div>
      <el-input 
        v-model="engineerId" 
        style="margin-left: 20px; width: 240px;" 
        :placeholder="$t('order.enterProjectNumber')" 
      />
      <el-button 
        style="margin-left: 15px;"  
        type="primary" 
        @click="fetchData"
      >
        {{ $t('order.inquire') }}
      </el-button>  
    </div>
    <!-- 数据展示区域（有数据时才显示） -->
    <el-card v-if="hasData">
      <div style="height: 100%; display: flex; flex-direction: column;">
        <div 
          v-for="(group, groupIndex) in divsData" 
          :key="groupIndex" 
          style="display: flex; margin-bottom: 20px;"
        >
          <el-card style="flex: 1; margin-right: 10px;">
            <div class="card-header">
              <span>{{ group.groupName }}</span>
            </div>
            <el-scrollbar height="750px" class="scrollbar-container">
              <div class="content-container">
                <div
                  v-for="(rect, index) in getAdjustedRectsForGroup(group.items)"
                  :key="index"
                  class="rect"
                  :style="{
                    position: 'absolute',
                    top: `${rect.y_axis}px`,
                    left: `${rect.x_axis}px`,
                    width: `${rect.width}px`,
                    height: `${rect.height}px`,
                    backgroundColor: 'lightblue'
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
          </el-card>
        </div>
      </div>
    </el-card>
  </div>
</template>
<style scoped>
.scrollbar-container {
  background-color: #e9e9eb;
  width: 100%;
}
.content-container {
  position: relative;
  width: 100%;
  height: 750px;
}
.rect {
  border: 1px solid #333;
  display: flex;
  align-items: center;
  justify-content: center;
}
.centered-text {
  text-align: center;
  width: 100%;
}
.card-header {
  font-weight: bold;
  margin-bottom: 10px;
  text-align: center;
  font-size: 16px;
}
</style>