<template>
  <el-dialog v-model="dialogVisible" :title="$t('film.originalInformation')" top="7vh" width="95%" @close="handleClose">
    <div style="margin-left: 750px;margin-top: 10px;font-size: large;margin-bottom: 10px;">
      {{ $t('Mounting.originalQueueNotBound') }}
    </div>
    <div class="dialog-container">
      <!-- 左侧部分 -->
      <div class="left-side">
        <el-form class="glass-form" label-position="right" label-width="auto" :model="formData" :rules="formRules" ref="formRef">

          <el-row :gutter="20" class="form-row">
            <el-col :span="12">
              <div id="dt" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.stationColon')" :required="true" style="width: 17vw;">
                    <el-input v-model="formData.slot" :disabled="isViewMode" :placeholder="$t('film.stationEnter')"
                              autocomplete="off" class="form-input"/>
                  </el-form-item>
                </div>
              </div>
            </el-col>
              <el-col :span="9">
              <div id="dta" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.shelfNumberColon')" :required="true" style="width: 17vw;">
                    <el-input v-model="formData.shelf" :disabled="isViewMode" :placeholder="$t('film.shelfNumberEnter')"
                              autocomplete="off" class="form-input"/>
                  </el-form-item>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" class="form-row">
            <el-col :span="12">
              <div id="dt" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.widthColon')" :required="true" style="width: 17vw;">
                    <el-input v-model="formData.rawWidth" :placeholder="$t('film.widthEnter')" autocomplete="off"
                              class="form-input"/>
                  </el-form-item>
                </div>
              </div>
            </el-col>
            <el-col :span="9">
              <div id="dta" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.heightColon')" :required="true" style="width: 17vw;">
                    <el-input v-model="formData.rawHeight" :placeholder="$t('film.heightEnter')" autocomplete="off"
                              class="form-input"/>
                  </el-form-item>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" class="form-row">
            <el-col :span="12">
              <div id="dt" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.thicknessColon')" :required="true" style="width: 17vw;">
                    <el-input v-model="formData.rawThickness" :placeholder="$t('film.thicknessEnter')"
                              autocomplete="off" class="form-input"/>
                  </el-form-item>
                </div>
              </div>
            </el-col>
            <el-col :span="9">
              <div id="dta" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.filmsColon')" :required="true" style="width: 17vw;">
                    <el-input v-model="formData.filmsId" :placeholder="$t('film.filmsEnter')" autocomplete="off"
                              class="form-input"/>
                  </el-form-item>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" class="form-row">
            <el-col :span="12">
              <div id="dt" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.originalEdgeColon')" style="width: 17vw;" prop="rawMargin">
                    <el-input 
                      v-model="formData.rawMargin" 
                      :placeholder="rawMarginPlaceholder"
                      autocomplete="off" 
                      class="form-input"
                      @blur="validateRawMargin"
                    />
                  </el-form-item>
                </div>
              </div>
            </el-col>
            <el-col :span="9">
              <div id="dta" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.remainQuantityColon')" :required="true" style="width: 17vw;">
                    <el-input v-model="formData.remainQuantity" :placeholder="$t('film.remainQuantityEnter')"
                              autocomplete="off" class="form-input"/>
                  </el-form-item>
                </div>
              </div>
            </el-col>
          </el-row>
          <el-row :gutter="20" class="form-row">
            <el-col :span="12">
              <div id="dt" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.manufacturerColon')" :required="true" style="width: 17vw;">
                    <el-input v-model="formData.manufacturer" autocomplete="off" class="form-input"/>
                  </el-form-item>
                </div>
              </div>
            </el-col>
            <el-col :span="9">
              <div id="dta" style="font-size: 15px;">
                <div>
                  <el-form-item :label="$t('film.originalFilmNumberColon')" :required="true" style="width: 17vw;">
                    <el-input v-model="formData.rawNumber" autocomplete="off" class="form-input"/>
                  </el-form-item>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-form>
        <div id="dialog-footer" style="margin-top: 20px;">
          <el-button type="primary" @click="EditClick">
            {{ $t('Mounting.confirm') }}
          </el-button>
          <el-button @click="handleClose">{{ $t('Mounting.cancel') }}</el-button>
        </div>
        <el-button style="display: block; margin: 20px auto;" type="primary" @click="handleDelete">{{
            $t('film.shelfEmpty')
          }}
        </el-button>
      </div>
      <!-- 右侧部分 -->
      <div class="right-side">
        <div style="display: flex;">
          <el-input v-model="rawWidth" style="margin-left: 10px;margin-bottom: 10px;width: 240px;" :placeholder="$t('film.widthEnter')" />
          <el-input v-model="rawHeight" style="margin-left: 10px;margin-bottom: 10px;width: 240px;" :placeholder="$t('film.heightEnter')"/>
          <el-input v-model="rawThickness" style="margin-left: 10px;margin-bottom: 10px;width: 240px;" :placeholder="$t('film.thicknessEnter')"/>
          <el-input v-model="filmsId" style="margin-left: 10px;margin-bottom: 10px;width: 240px;" :placeholder="$t('film.filmsEnter')"/>
          <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="fetchFlowCardSearch">
            {{$t('order.inquire')}}</el-button>
        </div>
        <el-table ref="table" :data="tableData" :header-cell-style="{ background: '#F2F3F5 ', color: '#1D2129' }"
                  style="width: 100%;height: 90%;">
          <el-table-column prop="rawWidth" align="center" :label="$t('Mounting.originalWidth')" min-width="80" />
          <el-table-column prop="rawHeight" align="center" :label="$t('film.height')" min-width="80" />
          <el-table-column prop="rawThickness" align="center" :label="$t('film.thickness')" min-width="80" />
          <el-table-column prop="filmsId" align="center" :label="$t('searchOrder.membraneSystem')" min-width="80" />
          <!-- <el-table-column prop="rawMargin" align="center" :label="$t('film.originalEdge')" min-width="100" /> -->
          <el-table-column prop="manufacturer" align="center" :label="$t('film.manufacturer')" min-width="100" />
          <el-table-column prop="rawNumber" align="center" :label="$t('film.originalFilmNumber')" min-width="100" />
          <el-table-column prop="remainQuantity" align="center" :label="$t('film.remainQuantity')" min-width="100" />
          <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center" width="100">
            <template #default="scope">
              <el-button type="text" plain @click="handleAddRow(scope.row)">{{ $t('Mounting.add') }}</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </el-dialog>
</template>
<script lang="ts" setup>
import request from "@/utils/request"
import type {PropType} from 'vue'
import {ref, watch, computed, nextTick} from 'vue'
import {ElMessage, type FormInstance} from 'element-plus'
import {useRouter} from "vue-router"
import {useI18n} from 'vue-i18n'
const {t} = useI18n()
const loading = ref(false)
const router = useRouter()
const rawWidth = ref('')
const rawHeight = ref('')
const rawThickness = ref('')
const filmsId = ref('')
const tableData = ref<any[]>([])
const formRef = ref<FormInstance>()
interface FormData {
  slot?: string
  deviceId?: string
  shelf?: string
  rawWidth?: string
  rawHeight?: string
  rawThickness?: string
  filmsId?: string
  rawMargin?: string
  remainQuantity?: string
  manufacturer?: string
  rawNumber?: string
  id?: string
  isRightSide?: boolean
}
interface TableRow {
  rawWidth?: string
  rawHeight?: string
  rawThickness?: string
  filmsId?: string
  rawMargin?: string
  manufacturer?: string
  rawNumber?: string
  remainQuantity?: string
  [key: string]: any
}
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  rowData: {
    type: Object as PropType<any>,
    default: () => ({})
  },
  tableDataQueryUnBind: {
    type: Array as PropType<TableRow[]>,
    default: () => []
  },
  isViewMode: {
    type: Boolean,
    default: false
  }
})
const emit = defineEmits(['update:visible', 'confirm', 'add-row', 'delete-row', 'close', 'clean'])
const dialogVisible = ref(props.visible)
const formData = ref<FormData>({})
// 计算属性：根据是否是右侧设置不同的占位符
const rawMarginPlaceholder = computed(() => {
  return props.rowData?.isRightSide 
    ? t('film.rightRawMarginPlaceholder') // 请输入200-2000的值
    : t('film.leftRawMarginPlaceholder')  // 请输入大于等于2500的值
})
// 计算属性：根据是否是右侧设置不同的验证规则
const formRules = computed(() => {
  const rawMarginRule = props.rowData?.isRightSide ? 
    [
      { 
        validator: (rule: any, value: any, callback: any) => {
          if (value && value !== '') {
            const numValue = Number(value)
            if (isNaN(numValue) || numValue < 200 || numValue >= 2000) {
              callback(new Error(t('film.rightRawMarginError'))) // 右侧原片边距必须大于等于200且小于2000
            } else {
              callback()
            }
          } else {
            callback()
          }
        }, 
        trigger: 'blur' 
      }
    ] : 
    [
      { 
        validator: (rule: any, value: any, callback: any) => {
          if (value && value !== '') {
            const numValue = Number(value)
            if (isNaN(numValue) || numValue < 2500) {
              callback(new Error(t('film.leftRawMarginError'))) // 左侧原片边距必须大于等于2500
            } else {
              callback()
            }
          } else {
            callback()
          }
        }, 
        trigger: 'blur' 
      }
    ]
  return {
    rawMargin: rawMarginRule
  }
})
// 监听props.visible变化
watch(() => props.visible, async (val) => {
  dialogVisible.value = val
  if (val) {
    tableData.value = [...props.tableDataQueryUnBind]
    // 当弹窗打开时，如果有rowData.id，则调用接口获取数据
    if (props.rowData?.id) {
      await fetchRawDetailsById(props.rowData.id)
    }
  }
})
const fetchRawDetailsById = async (id: string) => {
  if (!id) return
  loading.value = true
  try {
      const response = await request.post("/glassStorage/rawGlassStorageDetails/queryRawDetailsById", id)
    if (response.code === 200 && response.data) {
      // 将接口返回的数据设置到formData中
      const data = response.data
      formData.value = {
        slot: data.slot || '',
        deviceId: data.deviceId || '',
        shelf: data.shelf || '',
        rawWidth: data.rawWidth || '',
        rawHeight: data.rawHeight || '',
        rawThickness: data.rawThickness || '',
        filmsId: data.filmsId || '',
        rawMargin: data.rawMargin || (props.rowData?.isRightSide ? '200' : '2500'),
        remainQuantity: data.remainQuantity || '',
        manufacturer: data.manufacturer || '',
        rawNumber: data.rawNumber || '',
        id: data.id || '',
        isRightSide: props.rowData?.isRightSide || false
      }
    } else {
      ElMessage.error(response.message || t('film.fetchDataFailed'))
    }
  } catch (error) {
    ElMessage.error(t('film.fetchDataFailed'))
  } finally {
    loading.value = false
  }
}
// 监听dialogVisible变化，同步到父组件
watch(dialogVisible, (val) => {
  emit('update:visible', val)
})
// 监听rowData变化
watch(() => props.rowData, (val) => {
  if (val) {
    // 根据点击的是左侧还是右侧设置不同的默认值
    const defaultRawMargin = val.isRightSide ? '200' : '2500'
    formData.value = {
      slot: val.slot || '',
      deviceId: val.deviceId || '',
      shelf: val.shelf || '',
      rawWidth: val.rawWidth || '',
      rawHeight: val.rawHeight || '',
      rawThickness: val.rawThickness || '',
      filmsId: val.filmsId || '',
      rawMargin: val.rawMargin || defaultRawMargin, // 设置不同的默认值
      remainQuantity: val.remainQuantity || '',
      manufacturer: val.manufacturer || '',
      rawNumber: val.rawNumber || '',
      id: val.id || '',
      isRightSide: val.isRightSide || false
    }
  }
}, { immediate: true, deep: true })
// 监听props.tableDataQueryUnBind变化，更新本地表格数据
watch(() => props.tableDataQueryUnBind, (val) => {
  tableData.value = [...val]
}, { immediate: true })
// 手动验证原片边距
const validateRawMargin = () => {
  if (!formData.value.rawMargin) return true
  const numValue = Number(formData.value.rawMargin)
  if (props.rowData?.isRightSide) {
    // 右侧验证：200 <= value < 2000
    if (numValue < 200 || numValue >= 2000) {
      ElMessage.error(t('film.rightRawMarginError'))
      return false
    }
  } else {
    // 左侧验证：value >= 2500
    if (numValue < 2500) {
      ElMessage.error(t('film.leftRawMarginError'))
      return false
    }
  }
  return true
}
const handleAddRow = (row: TableRow) => {
  // 根据当前是左侧还是右侧设置不同的默认值
  const defaultRawMargin = props.rowData?.isRightSide ? '200' : '2500'
  formData.value = {
    ...formData.value,
    rawWidth: row.rawWidth,
    rawHeight: row.rawHeight,
    rawThickness: row.rawThickness,
    filmsId: row.filmsId,
    rawMargin: row.rawMargin || defaultRawMargin, // 使用行数据中的值或默认值
    remainQuantity: row.remainQuantity,
    manufacturer: row.manufacturer,
    rawNumber: row.rawNumber,
  }
}
const EditClick = async () => {
  // 验证表单
  if (formRef.value) {
    try {
      await formRef.value.validate()
      // 额外验证原片边距
      if (!validateRawMargin()) {
        return
      }
      emit('confirm', formData.value)
    } catch (error) {
      ElMessage.error(t('film.checkData'))
    }
  } else {
    // 如果没有表单引用，只验证原片边距
    if (!validateRawMargin()) {
      return
    }
    emit('confirm', formData.value)
  }
}
const deleteWareHousing = (row: TableRow) => {
  emit('delete-row', row)
}
const handleClose = () => {
  emit('close')
  const { slot, deviceId, shelf } = formData.value
  formData.value = { slot, deviceId, shelf }
  rawWidth.value = ''
  rawHeight.value = ''
  rawThickness.value = ''
  filmsId.value = ''
}
const handleDelete = () => {
  emit('clean')
}
const fetchFlowCardSearch = async () => {
  try {
    const response = await request.post('glassStorage/rawGlassStorageDetails/queryUnBindRawGlassList',{
      rawWidth: rawWidth.value,
      rawHeight: rawHeight.value,
      rawThickness: rawThickness.value,
      filmsId: filmsId.value
    });
    if (response.code == 200) {
      ElMessage.success(response.message);
      tableData.value = response.data;
    } else {
      ElMessage.error();
    }
  } catch (error) {
  }
};
</script>
<style scoped>
.dialog-container {
  display: flex;
  height: 70vh;
}
.left-side {
  width: 40%;
  padding: 0 10px;
  overflow-y: auto;
}
.right-side {
  width: 60%;
  padding: 0 10px;
}
#dialog-footer {
  text-align: center;
  margin-top: -15px;
}
.form-row {
  margin-bottom: 12px; /* 表单行间距 */
}
.glass-form {
  margin-left: 20px;
}
.glass-form :deep(.el-form-item__label) {
  /* max-width: 180px;  */
  margin-right: -10px;
}
.form-input {
  width: 200px !important;
}
</style>