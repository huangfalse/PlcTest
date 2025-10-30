<script lang="ts" setup>
import request from "@/utils/request"
import { useI18n } from 'vue-i18n'
import { useRouter } from "vue-router"
import { inject, nextTick, onBeforeUnmount, onMounted, onUnmounted, reactive, ref } from "vue";
import { closeWebSocket, initializeWebSocket } from '@/utils/WebSocketService';
import { ElMessage, ElMessageBox } from 'element-plus'

const EngineeringInfo = ref([]);
const DetailsInfo = ref([]);
const dialogFormVisible = ref(false)
const state = ref(false)
const flowCardInfo = ref([])
const config = ref({})
// 选择预览工程
const selectproject = (result) => {
  dialogFormVisible.value = true;
  state.value = result;
  if (result) {
    queryEngineering();
  } else {
    queryCageEngineering();
  }
  flowCardInfo.value = [];
  selectedProjectNo.value = "";
};
// 查询配置
const queryProductConfig = async () => {
  try {
    const response = await request.post('/verticalGlass/verticalSheetCageDetails/queryProductConfig');
    if (response.code == 200) {
      config.value = response.data;
      // ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
}
// 查询工程信息
const queryCageEngineering = async () => {
  try {
    const response = await request.post('/verticalGlass/verticalSheetCageDetails/queryEngineering');
    if (response.code == 200) {
      EngineeringInfo.value = response.data;
      // ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
}
// 查询工程信息
const queryEngineering = async () => {
  try {
    const response = await request.post('/verticalGlass/verticalSheetCageDetails/queryEngineeringNew');
    // const response = await request.post('/loadGlass/engineering/queryNotFinishEngineerIdList');
    if (response.code == 200) {
      EngineeringInfo.value = response.data;
      // ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
}

// 查询理片笼信息
const queryDetails = async () => {
  try {
    const response = await request.post('/verticalGlass/verticalSheetCageDetails/queryDetails');
    if (response.code == 200) {
      DetailsInfo.value = response.data;
      // ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
}

// 查询工程流程卡信息
const queryEngineeringFlowCard = async () => {

  try {
    const response = await request.post('/verticalGlass/verticalSheetCageDetails/queryEngineeringFlowCard', {
      engineerId: selectedProjectNo.value
    });
    if (response.code == 200) {
      flowCardInfo.value = response.data;
      // ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
}
queryDetails()
queryProductConfig()

const selectedProjectNo = ref('')
const handlesure = async () => {
  if (!selectedProjectNo.value) {
    // ElMessage.error(t('Mounting.selectProject'))
    ElMessage.error("请选择工程")
    return
  }
  try {
    let responseUrl = '';
    if (state.value) {
      responseUrl = '/verticalGlass/verticalSheetCageDetails/importEngineering'
    } else {
      responseUrl = '/verticalGlass/verticalSheetCageDetails/removeEngineering'
    }
    const response = await request.post(responseUrl, {
      engineerId: selectedProjectNo.value,
      // filmRemove: parseInt(filmRemove.value, 10),
    })
    window.localStorage.setItem('engineeringIda', selectedProjectNo.value);
    if (response.code == 200) {
      ElMessage.success(response.message);
      dialogFormVisible.value = false;
      selectedProjectNo.value = '';
      queryDetails()
      queryProductConfig()
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
  }
}

const showConfiguration = ref(false)
const showConfig = () => {
  showConfiguration.value = true;
  queryProductConfig()
}


const optionsTemp = ref(
  [{
    value: 0,
    label: 'Tempering1'
  }, {
    value: 1,
    label: 'Tempering2'
  }, {
    value: 2,
    label: 'Tempering3'
  }, {
    value: 3,
    label: 'Tempering4'
  }]
)

const options = ref(
  [{
    value: 0,
    label: 'P-CNC'
  }, {
    value: 1,
    label: 'P-Temp'
  }, {
    value: 2,
    label: 'P-TempIG'
  }]
)

//修改线路配置
const changeWorkLine = async (productType, workLine) => {

  try {
    const response = await request.get('/verticalGlass/verticalSheetCageDetails/changeWorkLine?productType=' + productType + '&workLine=' + workLine);
    if (response.code == 200) {
      ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
  queryProductConfig()
}

//修改笼子配置
const changeCage = async (productType, slot) => {

  try {
    const response = await request.get('/verticalGlass/verticalSheetCageDetails/changeCage?productType=' + productType + '&slot=' + slot);
    if (response.code == 200) {
      ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
  queryProductConfig()
}

//修改笼子配置
const changeTemp = async (productType, slot) => {

  try {
    const response = await request.get('/verticalGlass/verticalSheetCageDetails/changeTemp?productType=' + productType + '&slot=' + slot);
    if (response.code == 200) {
      ElMessage.success(response.message);
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
  }
}


</script>
<template>
  <!-- 添加外层容器，设置垂直排列 -->
  <div class="outer-container">
    <el-card style="width: 1700px;margin: 0 auto;">
      <div style="display: flex; justify-content: space-around;  height: 130px;">
        <div style="width: 25%;">
          <el-button style="margin-top: 25px;margin-left: 100px;" type="primary" @click="showConfig()">
            구성
            <!-- {{ $t('Mounting.selectPreviewProject') }} -->
          </el-button>
        </div>
        <div style="width: 25%;">
          <div class="product">P-CNC</div>
          <div style="font-size: 14px;">보관 범위:
            <!-- {{ config?.workLineConfigVOS?.[0]?.workCage?.join(', ') || '' }} -->
            <label v-for="(item, index) in config?.workLineConfigVOS?.[0]?.workCage">
              {{ item }}({{ config?.workLineConfigVOS?.[0]?.cageThicknessConfig[index] }}mm),
            </label>
          </div>
          <div>남은 최대 용량:{{ (config?.workLineConfigVOS?.[0]?.remainArea || 0).toFixed(2) }}m² ({{
            (config?.workLineConfigVOS?.[0]?.remainSlot || 0) }})</div>
        </div>
        <div style="width: 25%;">
          <div class="product">P-Temp</div>
          <!-- <div style="font-size: 14px;">보관 범위:{{ config?.workLineConfigVOS?.[1]?.workCage?.join(', ') || '' }}</div> -->

          <label v-for="(item, index) in config?.workLineConfigVOS?.[1]?.workCage">
            {{ item }}({{ config?.workLineConfigVOS?.[1]?.cageThicknessConfig[index] }}mm),
          </label>
          <div>남은 최대 용량:{{ (config?.workLineConfigVOS?.[1]?.remainArea || 0).toFixed(2) }}m² ({{
            (config?.workLineConfigVOS?.[1]?.remainSlot || 0) }})</div>
        </div>
        <div style="width: 25%;">
          <div class="product">P-TempIG</div>
          <!-- <div style="font-size: 14px;">보관 범위:{{ config?.workLineConfigVOS?.[2]?.workCage?.join(', ') || '' }}</div> -->

          <label v-for="(item, index) in config?.workLineConfigVOS?.[2]?.workCage">
            {{ item }}({{ config?.workLineConfigVOS?.[2]?.cageThicknessConfig[index] }}mm),
          </label>
          <div>남은 최대 용량:{{ (config?.workLineConfigVOS?.[2]?.remainArea || 0).toFixed(2) }}m² ({{
            (config?.workLineConfigVOS?.[2]?.remainSlot || 0) }})</div>
        </div>
      </div>
    </el-card>
    <div
      style="width: 1900px;margin: 0 auto; height: 725px;overflow: auto ;display: flex; justify-content: space-around;margin-top: 50px;">
      <div>
        <el-button style="margin-top: 400px;margin-right: 15px;" type="primary" @click="selectproject(true)">
          프로젝트 정보 가져오기
          <!-- {{ $t('Mounting.selectPreviewProject') }} -->
        </el-button>
      </div>
      <div class="custom-table">
        <!-- 循环对象的键值对，key为"1"、"2"等，value为对应的数据数组 -->
        <div v-for="(items, key) in DetailsInfo" :key="key" class="table-row">
          <!-- 行号（使用键作为行号） -->
          <div class="row-number">{{ key }}</div>
          <!-- 循环当前键对应的数组，渲染每个单元格 -->
          <div v-show="item.glassId != null" v-for="(item, index) in items" :key="item.id" class="table-cell" :style="{
            width: `${(item.width > item.height ? item.width : item.height) * 0.337}px`,
            border: '1px solid black',
            marginLeft: index > 0 ? `${item.gap * 0.337}px` : '0px'
          }">
            <div>
              <!-- 展示需要的字段，这里示例展示几个常用字段 -->
              <div>{{ item.flowCardSequence }} {{ item.width }}×{{ item.height }}×{{ item.thickness }}</div>
              <div>{{ item.glassId }} {{ item.flowCardId }}/{{ item.layer }}</div>
            </div>
          </div>
        </div>
      </div>
      <div>
        <el-button style="margin-top: 400px;margin-left: 15px;" type="primary" @click="selectproject(false)">
          프로젝트 정보 제거
          <!-- {{ $t('Mounting.selectPreviewProject') }} -->
        </el-button>
      </div>
    </div>
  </div>



  <!-- 选择预览工程 -->
  <el-dialog v-model="dialogFormVisible" top="24vh" width="55%">
    <div class="flex-container" style="margin-left: 100px;margin-bottom: 10px;">
      <el-form-item label="프로젝트 번호" :required="true">
        <el-select v-model="selectedProjectNo" @change="queryEngineeringFlowCard();" filterable clearable
          placeholder="프로젝트를 선택하십시오." style="width: 220px">
          <el-option v-for="item in EngineeringInfo" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-card>
          <div style="width: 200px;height: 40px;margin: 0 auto;">
            {{ selectedProjectNo }}
          </div>
          <div style="width: 700px;height: 50px;">
            <span>
              TotalArea： {{ flowCardInfo && flowCardInfo.length > 0 ? (flowCardInfo[0].glassTotalArea || 0).toFixed(2) +
                'm²'
                : '' }}
            </span>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <span style="margin-right: 0px;">
              TotalCount： {{ flowCardInfo && flowCardInfo.length > 0 ? (flowCardInfo[0].glassTotal || "")
                : '' }}
            </span>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <span style="margin-right: 0px;">
              EngineerName： {{ flowCardInfo && flowCardInfo.length > 0 ? (flowCardInfo[0].engineerName || "")
                : '' }}
            </span>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <span style="margin-right: 0px;">
              EngineerThickness： {{ flowCardInfo && flowCardInfo.length > 0 ? (flowCardInfo[0].glassThickness || "")
                : '' }}
            </span>
          </div>
          <el-table height="320px" ref="table" border :data="flowCardInfo"
            :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }">

            <el-table-column prop="productName" align="center" label="제품 이름" min-width="250" />
            <el-table-column prop="flowCardId" align="center" label="프로세스 카드 번호" min-width="160" />
            <el-table-column prop="layer" align="center" label="레이어 번호" min-width="80" />
            <el-table-column prop="productSortNumber" align="center" label="일련번호" min-width="80" />
            <el-table-column prop="width" align="center" label="폭" min-width="80" />
            <el-table-column prop="height" align="center" label="높음" min-width="80" />
            <el-table-column prop="glassCount" align="center" label="수량" min-width="80" />
          </el-table>
        </el-card>
      </el-form-item>
    </div>
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="handlesure">
          확인
        </el-button>
        <el-button @click="dialogFormVisible = false">취소</el-button>
      </div>
    </template>
  </el-dialog>

  <el-dialog v-model="showConfiguration" top="2vh" width="85%" height="80%">
    <div style="display: flex;justify-content: space-around;height: 800px;">
      <div style="width: 20%;">
        <div class="conifg-left">
          <div class="product">P-CNC</div>
          <div>Proportion：{{ config?.workLineConfigVOS?.[0]?.percent }}%</div>
          <div v-for="(item, arrIndex) in config?.workLineConfigVOS?.[0]?.thicknessArea" :key="arrIndex">
            <div v-for="(value, key, objIndex) in item" :key="objIndex">
              {{ key }}mm: {{ value }}%
            </div>
          </div>
        </div>
        <div class="conifg-left">
          <div class="product">P-Temp</div>
          <div>Proportion：{{ config?.workLineConfigVOS?.[1]?.percent }}%</div>
          <div v-for="(item, arrIndex) in config?.workLineConfigVOS?.[1]?.thicknessArea" :key="arrIndex">
            <div v-for="(value, key, objIndex) in item" :key="objIndex">
              {{ key }}mm: {{ value }}%
            </div>
          </div>
        </div>
        <div class="conifg-left">
          <div class="product">P-TempIG</div>
          <div>Proportion：{{ config?.workLineConfigVOS?.[2]?.percent }}%</div>
          <div v-for="(item, arrIndex) in config?.workLineConfigVOS?.[2]?.thicknessArea" :key="arrIndex">
            <div v-for="(value, key, objIndex) in item" :key="objIndex">
              {{ key }}mm: {{ value }}%
            </div>
          </div>
        </div>
      </div>
      <div style="width: 22%;">
        <div class="cutting-div">
          Cutting1
          <el-card class="cutting-card">
            <el-checkbox :checked="config?.workLineConfigVOS?.[0]?.workLine?.[0]"
              @click="changeWorkLine(0, 0)">P-CNC</el-checkbox>
            <el-checkbox :checked="config?.workLineConfigVOS?.[1]?.workLine?.[0]"
              @click="changeWorkLine(1, 0)">P-Temp</el-checkbox>
            <el-checkbox :checked="config?.workLineConfigVOS?.[2]?.workLine?.[0]"
              @click="changeWorkLine(2, 0)">P-TempIG</el-checkbox>
          </el-card>
        </div>
        <div class="cutting-div">
          Cutting2
          <el-card class="cutting-card">
            <el-checkbox :checked="config?.workLineConfigVOS?.[0]?.workLine?.[1]"
              @click="changeWorkLine(0, 1)">P-CNC</el-checkbox>
            <el-checkbox :checked="config?.workLineConfigVOS?.[1]?.workLine?.[1]"
              @click="changeWorkLine(1, 1)">P-Temp</el-checkbox>
            <el-checkbox :checked="config?.workLineConfigVOS?.[2]?.workLine?.[1]"
              @click="changeWorkLine(2, 1)">P-TempIG</el-checkbox>
          </el-card>
        </div>
        <div class="cutting-div">
          Cutting3
          <el-card class="cutting-card">
            <el-checkbox :checked="config?.workLineConfigVOS?.[0]?.workLine?.[2]"
              @click="changeWorkLine(0, 2)">P-CNC</el-checkbox>
            <el-checkbox :checked="config?.workLineConfigVOS?.[1]?.workLine?.[2]"
              @click="changeWorkLine(1, 2)">P-Temp</el-checkbox>
            <el-checkbox :checked="config?.workLineConfigVOS?.[2]?.workLine?.[2]"
              @click="changeWorkLine(2, 2)">P-TempIG</el-checkbox>
          </el-card>
        </div>
        <div class="cutting-div">
          Cutting4
          <el-card class="cutting-card">
            <el-checkbox :checked="config?.workLineConfigVOS?.[0]?.workLine?.[3]"
              @click="changeWorkLine(0, 3)">P-CNC</el-checkbox>
            <el-checkbox :checked="config?.workLineConfigVOS?.[1]?.workLine?.[3]"
              @click="changeWorkLine(1, 3)">P-Temp</el-checkbox>
            <el-checkbox :checked="config?.workLineConfigVOS?.[2]?.workLine?.[3]"
              @click="changeWorkLine(2, 3)">P-TempIG</el-checkbox>
          </el-card>
        </div>
        <div class="cutting-div">
          Cutting5
          <el-card class="cutting-card">
            <el-checkbox :checked="config?.workLineConfigVOS?.[0]?.workLine?.[4]"
              @click="changeWorkLine(0, 4)">P-CNC</el-checkbox>
            <el-checkbox :checked="config?.workLineConfigVOS?.[1]?.workLine?.[4]"
              @click="changeWorkLine(1, 4)">P-Temp</el-checkbox>
            <el-checkbox :checked="config?.workLineConfigVOS?.[2]?.workLine?.[4]"
              @click="changeWorkLine(2, 4)">P-TempIG</el-checkbox>
          </el-card>
        </div>
      </div>
      <div style="width: 25%;">
        <div>
          <div class="cage-div" v-for="(item, index) in config.cage">
            <el-select v-model="config.cage[index]" placeholder="请选择" class="cage-slot" style="min-width: 120px;"
              @change="changeCage(config.cage[index], index + 1)">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
            &nbsp;
            <span class="slot">{{ config.cageThickness[index] }}</span>
            &nbsp;
            <el-select v-model="config.temp[index]" placeholder="请选择" class="cage-temp"
              @change="changeTemp(config.temp[index], index + 1)">
              <el-option v-for="item in optionsTemp" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </div>
        </div>
      </div>
      <div style="width: 28%;">
        <div class="temp-div">
          8000x3300
          <el-card class="temp-card">
          </el-card>
        </div>
        <div class="temp-div">
          8000x2440
          <el-card class="temp-card">
          </el-card>
        </div>
        <div class="temp-div">
          8000x3300
          <el-card class="temp-card">
          </el-card>
        </div>
        <div class="temp-div">
          8000x2850
          <el-card class="temp-card">
          </el-card>
        </div>
      </div>
    </div>
  </el-dialog>
</template>
<style scoped>
.outer-container {
  /* 默认就是块级元素，会自动垂直排列子元素 */
  /* 可以添加一些额外样式，如宽度限制 */
  width: 100%;
  max-width: 1920px;
  /* 限制最大宽度，避免超出屏幕 */
}

.custom-table {
  border: 1px solid #e5e7eb;
  border-radius: 4px;
  width: 100%;
  box-sizing: border-box;
  overflow: scroll;
}

.table-row {
  display: flex;
  border-bottom: 1px solid #e5e7eb;
  background-color: #fff;
}

/* 行号列样式 */
.row-number {
  width: 70px;
  /* 宽度和高度相同才能形成正圆 */
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-right: none;
  /* 移除右边框，避免影响圆形外观 */
  background-color: #409eff;
  /* 蓝色背景，更像圆形标签 */
  color: white;
  /* 白色文字，与蓝色背景对比 */
  font-weight: 600;
  border-radius: 50%;
  /* 关键：设置为50%实现圆形 */
  margin: 0 10px;
  /* 增加左右间距，避免紧贴单元格 */
  flex-shrink: 0;
}

/* 单元格样式 */
.table-cell {
  /* flex: 1; */
  padding: 12px;
  border-right: 1px solid #e5e7eb;
  min-width: 200px;
  /* 避免内容过窄 */
}

/* 最后一个单元格去掉右边框 */
.table-cell:last-child {
  border-right: none;
}

/* 交替行背景色，提升可读性 */
.table-row:nth-child(even) {
  background-color: #f9fafb;
}

/* 单元格内文字样式优化 */
.table-cell div {
  margin-bottom: 1px;
  font-size: 14px;
}

.table-cell div:first-child {
  font-weight: 500;
  color: #1e40af;
}

.conifg-left {
  height: 260px;
  /* border: 1px solid #eee; */
  /* 边框仅用于可视化，可删除 */
  display: flex;
  /* 开启 flex 布局 */
  flex-direction: column;
  /* 子元素垂直排列（默认是水平 row） */
  justify-content: center;
  /* 垂直方向居中（子元素整体在容器垂直中间） */
  align-items: center;
}

.product {
  color: blue;
  font-size: 18px;
}

.cutting-div {
  margin-top: 50px;
}

.cutting-card {
  border: 1px solid #409EFF;
}

.slot {
  display: inline-block;
  width: 100px;
  height: 30px;
  border: 1px solid #000;
  text-align: center;
  line-height: 30px;
  font-weight: bold;
}

.temp-div {
  margin-top: 50px;
}

.temp-card {
  border: 1px solid #409EFF;
  height: 100px;
  width: 400px;
}

.cage-slot {
  width: 100px;
}

.cage-temp {
  width: 120px;
}

.cage-div {
  margin-top: 10px;
}
</style>