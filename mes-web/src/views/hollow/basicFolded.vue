<script setup>
import request from '@/utils/request'
import { onMounted, ref, nextTick } from "vue"
import { useRouter } from "vue-router"
import { ElMessage, ElMessageBox } from 'element-plus'
import { useI18n } from 'vue-i18n'
const { t } = useI18n()
const router = useRouter()
const carouselData = ref([])
const activeIndex = ref('')
const isDialogVisible = ref(false)
const selectedItem = ref(null)
const selectedLayer = ref(null)
const maxLayer = ref(0)
const carouselRef = ref(null) // 👈 ref 用于访问组件实例
const language = ref(localStorage.getItem('lang') || 'zh')
const props = defineProps({
  // 线路
  lineNumber: {
    type: Number,
    required: true
  },
})
const handleSure = async () => {
  try {
    const response = await request.post('/hollowGlass/hollowGlassQueueInfo/queryHollowGlassQueueInfoByLine', {
      cell: props.lineNumber,
    });
    if (response.code === 200) {
      carouselData.value = response.data.map((item) => ({
        width: item.width,
        height: item.height,
        state: item.state,
        flowCardId: item.flowCardId,
        thickness: item.thickness,
        hollowSequence: item.hollowSequence,
        layer: item.layer,
        relationId: item.relationId
      }));
      // 查找 state === -1 的项
      const targetIndex = carouselData.value.findIndex((item) => item.state === -1);
      const finalIndex = targetIndex !== 1 ? targetIndex : 0;
      // 等待 DOM 渲染完成后切换页面
      await nextTick();
      activeIndex.value = finalIndex;
      carouselRef.value?.setActiveItem(finalIndex); // 强制更新 Element Plus 的轮播组件
    } else {
      console.error('接口返回错误:', response.message || '未知错误');
    }
  } catch (error) {
    console.error('请求失败:', error);
  }
};
// 破损
const handleDilapidationClick = (item) => {
  if (item.layer > 0) {
    selectedItem.value = item;
    maxLayer.value = item.layer;
    selectedLayer.value = null; // 重置选择
    isDialogVisible.value = true;
  } else {
    ElMessage.warning('当前项没有层信息');
  }
};
// 弹窗确认
const confirmDilapidation = async () => {
  if (!selectedLayer.value) {
    ElMessage.warning('请选择一个层数');
    return;
  }
  try {
    const response = await request.post('/hollowGlass/hollowGlassQueueInfo/confirmBorder', {
      hollowSequence: selectedItem.value.hollowSequence,
      relationId: selectedItem.value.relationId,
      layer: selectedLayer.value,
      state: 8,
    });
    ElMessage.success(`已选择层数：${selectedLayer.value}`);
    isDialogVisible.value = false;
    handleSure()
      } catch (error) {
    console.error('破损操作失败:', error);
    ElMessage.error('破损操作失败，请重试');
  }
};
// 确认
const handleButtonClick = async (item) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
      t('hollow.clickWhether'),
      t('Mounting.prompt'),
      {
        confirmButtonText: t('Mounting.yes'),
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',
      }
    );
    if (confirmResult === 'confirm') {
      const response = await request.post('/hollowGlass/hollowGlassQueueInfo/confirmBorder', {
      hollowSequence: item.hollowSequence,
      relationId: item.relationId,
      state: 2,
    })
      if (response.code === 200) {
        ElMessage.success(response.message);
        handleSure()
    } else {
        ElMessage.error(response.msg);
      }
    }
  } catch (error) {
    console.error('发生错误:', error);
  }
};
onMounted(() => {
  handleSure()
});
const handleCarouselChange = (index) => {
  activeIndex.value = index;
};
const getColorByState = (state) => {
  switch (state) {
    case -1: return "gray";
    case 0: return "gray";
    case 1: return "gray";
    case 2: return "#95d475";
    case 8: return "#911005";
    default: return "#CDAF95";
  }
};
</script>
<template>
  <div style="height: 500px;">
    <div>
      <el-card style="flex: 1;margin-left: 10px;margin-top: 20px;">
  <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
    <div class="block text-center" m="t-4">
      <el-carousel ref="carouselRef" :active-index="activeIndex" @change="handleCarouselChange" height="790px"
        :autoplay="false">
        <el-carousel-item v-for="(item, index) in carouselData" :key="index">
          <div style="display: flex; justify-content: center; align-items: center; height: 100%;">
                  <div class="carousel-item-content" :style="{
                    width: `${item.width*0.5}px`,
                    height: `${item.height*0.3}px`,
                    backgroundColor: getColorByState(item.state),
                  }">
                    <div style="color: #FAFCFF;font-size:18px;text-align: center; display: flex; flex-direction: column; justify-content: center; height: 87%;">
                      <p>{{ item.flowCardId }}</p>
                      <p>{{ $t('film.widthColon') }}{{ item.width }}</p>
                      <p>{{ $t('film.heightColon') }}{{ item.height }}</p>
                      <p>{{ $t('film.thicknessColon') }}{{ item.thickness }}</p>
                      <p>{{ $t('hollow.logarithmColon') }}{{ item.hollowSequence }}</p>
                    </div>
                 <div style="position: absolute; bottom: 20px; right: 20px;">
                <el-button size="large" @click="handleDilapidationClick(item)">{{ $t('Mounting.dilapidation') }}</el-button>
                <el-button size="large" :disabled="item.state === 1" @click="handleButtonClick(item)">{{ $t('Mounting.yes') }}</el-button>
              </div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>
  </div>
  <el-dialog v-model="isDialogVisible" width="20%" top="30vh" style="text-align: center;">
    <div>
      <el-select v-model="selectedLayer" clearable :placeholder="$t('hollow.layerNumber')" style="width: 50%;">
        <el-option v-for="n in maxLayer" :key="n" :label="n" :value="n"></el-option>
      </el-select>
    </div>
    <div slot="footer" style="margin-top: 15px;">
      <el-button @click="isDialogVisible = false">{{ $t('Mounting.cancel') }}</el-button>
      <el-button type="primary" :disabled="!selectedLayer"  @click="confirmDilapidation">{{ $t('Mounting.confirm') }}</el-button>
    </div>
  </el-dialog>
</el-card>
    </div>
  </div>
</template>
<style scoped>
 .carousel-item-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  border: 1px solid #ccc;
  margin: 10px auto;
  color: white;
  text-align: center;
  padding-bottom: 0px;
  box-sizing: border-box;
}
.demonstration {
  color: var(--el-text-color-secondary);
}
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 550px;
  margin: 0;
  text-align: center;
}
.el-carousel__item:nth-child(2n) {
  background-color: #d3dce6;
}
.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>