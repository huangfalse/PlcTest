<script setup>
import request from "@/utils/request"
import { Search } from "@element-plus/icons-vue"
import { ref, onMounted, reactive, computed } from "vue"
import { useRouter } from "vue-router"
import { useI18n } from 'vue-i18n'
import { ElMessage, ElMessageBox } from 'element-plus'
const { t } = useI18n()
const router = useRouter()
const dialogAddRole = ref(false)
const dialogExitRole = ref(false)
const selectedProjectNoa = ref(null) // 当前选中的角色 
const slot = ref('')
const name = ref('')
const tableData = ref([])
const options = ref([])
const selectedOptions = ref([])
let language = ref(localStorage.getItem('lang') || 'zh')
const cascaderProps = {
  value: 'id',  
  label: 'menuName',  
  multiple: true
};  
const titleSelectJsona = ref({
  processType: [],
})
onMounted(() => {  
  fetchOptionsa('');  
}); 
  // 角色下拉选  
  const fetchOptionsa = async () => {  
  try {  
    const response = await request.post('/plcSend/sys/role/queryRole',{
  key: ''
});  
    if (response.code == 200) {  
     tableData.value = response.data
    } else {  
      ElMessage.warning(response.data);  
    }  
  } catch (error) {  
    console.error('Error fetching options:', error);  
  }  
};
// 获取菜单权限
request.get("/plcSend/sys/menu/nav").then((res) => {
   if (res.code == 200) {
    options.value = res.data.tree
   } else {
    ElMessage.warning(res.msg)
   }
 });
 // 计算属性检查表单是否有效
const isFormValid = computed(() => {
  return name.value.trim() !== '' && 
         selectedOptions.value && 
         selectedOptions.value.length > 0;
});
// 添加
const getTableRowAdd = async () => {
  try {  
    let menuList = [];
    let parentIdMap = {}; // 用于存储已经添加的父项  
    selectedOptions.value.forEach(array => {  
      if (array.length >= 2) {  
        let id = array[0];  
        let parentId = array[1];  
        // 如果id还未作为父项添加，则添加它  
        if (!parentIdMap[id]) {  
          menuList.push({  
            id: id,  
            parentId: 0,
            children: []  
          });  
          parentIdMap[id] = menuList[menuList.length - 1]; // 更新映射  
        }  
        // 向对应的父项添加子项  
        if (!parentIdMap[id].children.some(child => child.id === parentId)) {  
          // 检查是否已存在相同的子项（基于id），避免重复添加  
          parentIdMap[id].children.push({  
            id: parentId,  
            parentId: id  
          });  
        }  
      }  
    });  
    const dataToSend = {  
      name: name.value,
      menuList: menuList
    };  
    const response = await request.post('/plcSend/sys/role/saveRole', dataToSend);
    if (response.code == 200) {
      ElMessage.success(response.message);
      dialogAddRole.value = false;
      name.value = '';
      selectedOptions.value = '';
      fetchOptionsa();
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
  }
};
// 点击编辑
function handleEdit(row) {
  name.value = row.name;  
  dialogExitRole.value = true;
  window.localStorage.setItem('ids', row.id);
  const parentIdMap = {};  
  const menuItemsById = {}; // 快速查找菜单项  
  let topLevelItems = []; // 存储顶级菜单项 
  row.menuList.forEach(item => {  
    const parentId = item.parentId === 0 ? null : item.parentId;
    const menuItem = {  
      id: item.id,  
      parentId: parentId,  
      menuName: item.menuName,  
      children: []  
    };  
    menuItemsById[item.id] = menuItem;  
    // 初始化或更新parentIdMap中的数组  
    if (!parentIdMap[parentId]) {  
      parentIdMap[parentId] = [];  
    }  
    if (parentId === null) {  
      // 顶级菜单项直接添加到topLevelItems  
      topLevelItems.push(menuItem);  
    } else {  
      // 非顶级菜单项添加到parentIdMap中  
      parentIdMap[parentId].push(menuItem);  
    }  
  });  
  for (const parentId in parentIdMap) {  
    if (parentId !== 'null') { // 跳过顶级菜单项  
      parentIdMap[parentId].forEach(child => {  
        // 查找父项并添加子项  
        if (menuItemsById[parentId]) {  
          menuItemsById[parentId].children.push(child);  
        }  
      });  
    }  
  }  
  if (selectedOptions.value !== topLevelItems) {  
    selectedOptions.value = topLevelItems;  
  }
const topLevelItemsWithChildren = topLevelItems; // 包含顶级菜单项及其子菜单项的数组  
let selectedPath = [];
// 循环遍历 topLevelItemsWithChildren 数组
for (let i = 0; i < topLevelItemsWithChildren.length; i++) {
 // 添加顶级菜单项的 id
 selectedPath.push(topLevelItemsWithChildren[i].id);
 // 如果有子菜单项，添加第一个子菜单项的 id
 if (topLevelItemsWithChildren[i].children && topLevelItemsWithChildren[i].children.length > 0) {
  for (let j = 0; j < topLevelItemsWithChildren[i].children.length; j++) {  
            // 将每个子菜单项的 id 添加到 selectedPath 中  
            selectedPath.push(topLevelItemsWithChildren[i].children[j].id);  
        }  
    }  
}
selectedOptions.value = selectedPath;
}
// 编辑
const getTableRowExit = async () => {
  let ids = window.localStorage.getItem('ids')
  try {  
    let menuList = [];
    let parentIdMap = {}; // 用于存储已经添加的父项  
    selectedOptions.value.forEach(array => {  
      if (array.length >= 2) {  
        let id = array[0];  
        let parentId = array[1];  
        // 如果id还未作为父项添加，则添加它  
        if (!parentIdMap[id]) {  
          menuList.push({  
            id: id,  
            parentId: 0, 
            children: []  
          });  
          parentIdMap[id] = menuList[menuList.length - 1]; // 更新映射  
        }  
        // 向对应的父项添加子项  
        if (!parentIdMap[id].children.some(child => child.id === parentId)) {  
          // 检查是否已存在相同的子项（基于id），避免重复添加  
          parentIdMap[id].children.push({  
            id: parentId,  
            parentId: id  
          });  
        }  
      }  
    });  
    const dataToSend = {  
      id:ids,
      name: name.value,
      menuList: menuList
    };  
    const response = await request.post('/plcSend/sys/role/updateRole', dataToSend);
    if (response.code == 200) {
      ElMessage.success(response.message);
      dialogExitRole.value = false;
      name.value = '';
      selectedOptions.value = '';
      fetchOptionsa();
    } else {
      ElMessage.error(response.message);
    }
  } catch (error) {
    console.error(error);
  }
};
function closeDialogAdd(row) {
    dialogAddRole.value = false;
    name.value = '';
    selectedOptions.value = '';
}
function closeDialogExit(row) {
    dialogExitRole.value = false;
    name.value = '';
    selectedOptions.value = '';
}
// 删除 
 const openDelete = async(row) => {  
  try {
    const confirmResult = await ElMessageBox.confirm(  
      t('delivery.deleteRoleWhether'), 
      t('Mounting.prompt'),  
      {  
        confirmButtonText: t('Mounting.yes'), 
        cancelButtonText: t('Mounting.cancel'),
        type: 'warning',  
      } 
    ); 
    if (confirmResult === 'confirm') {
      const response = await request.post("/plcSend/sys/role/delete",[row.id])
    if (response.code === 200) {
      ElMessage.success(response.message);
      fetchOptionsa()
      } else {
      ElMessage.error(response.message);
      }  
    }  
  } catch (error) {
    console.error('发生错误:', error);  
  }  
};
</script>
<template>
  <div>
     <el-button type="primary" style="margin-top: 10px;margin-left: 10px;"  id="searchButton" @click="dialogAddRole = true">{{ $t('delivery.addRole') }}</el-button>
    <el-card style="flex: 1;margin-left: 10px;margin-top: 20px;">
      <div style="width: 98%; height: calc(100% - 35px); overflow-y: auto;">
        <el-table height="750" ref="table"
        :data="tableData" :header-cell-style="{background:'#F2F3F5 ',color:'#1D2129'}">
          <el-table-column prop="name" align="center" :label="$t('delivery.role')" min-width="180" />
          <el-table-column fixed="right" :label="$t('Mounting.operate')" align="center" width="200">
            <template #default="scope">
              <el-button type="text" plain  @click="handleEdit(scope.row)">{{ $t('productStock.edit') }}</el-button>
              <el-button type="text" plain @click="openDelete(scope.row)">{{ $t('Mounting.delete') }}</el-button>
            </template>
        </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
  <!-- 添加角色 -->
  <el-dialog v-model="dialogAddRole" top="23vh" width="37%" :title="$t('productStock.addUsers')" >
    <div style="margin-left: -50px;margin-top: 10px;margin-bottom: 10px;">
            <el-form ref="formRef" label-width="150px">
      <el-form label-width="100px" label-position="right">
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('delivery.roleColon')" :required="true" style="width: 25vw">
                <el-input :placeholder="$t('delivery.roleEnter')" v-model="name" autocomplete="off" :class="{ 'is-required': !name }" style="width: 350px"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
              <el-form-item :label="$t('delivery.MenuPermissionsColon')" :required="true" style="width: 25vw;">
        <el-cascader 
         v-model="selectedOptions" 
         :placeholder="$t('delivery.MenuPermissionsSelection')"
         :class="{ 'is-required': !selectedOptions || selectedOptions.length === 0 }"
         :props="cascaderProps"  
         :options="options" 
          style="width: 350px"
           clearable />
              </el-form-item></div></div>
          </el-col>
        </el-row>
        </el-form>
            </el-form>
          </div>
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="getTableRowAdd" :disabled="!isFormValid">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="closeDialogAdd"> {{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog> 
  <el-dialog v-model="dialogExitRole" top="23vh" width="37%" :title="$t('delivery.editRole')">
    <div style="margin-left: -50px;margin-top: 10px;margin-bottom: 10px;">
            <el-form ref="formRef" label-width="150px">
              <el-form label-width="100px" label-position="right">
        <el-row style="margin-top: -15px;margin-bottom: -2px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
          <el-form-item :label="$t('delivery.roleColon')" :required="true" style="width: 25vw">
                <el-input :placeholder="$t('delivery.roleEnter')" v-model="name" autocomplete="off" :class="{ 'is-required': !name }" style="width: 350px"/>
              </el-form-item></div></div>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <el-col :span="6">
              <div id="dt" style="font-size: 15px;">
        <div>
       <el-form-item :label="$t('delivery.MenuPermissionsColon')" :required="true" style="width: 25vw;">
        <el-cascader 
         v-model="selectedOptions"  
         :placeholder="$t('delivery.MenuPermissionsSelection')"
         :class="{ 'is-required': !selectedOptions || selectedOptions.length === 0 }"
         :props="cascaderProps"  
         :options="options" 
         style="width: 350px"
          clearable />
              </el-form-item></div></div>
          </el-col>
        </el-row>
        </el-form>
            </el-form>
          </div>
    <template #footer>
      <div id="dialog-footer">
        <el-button type="primary" @click="getTableRowExit" :disabled="!isFormValid">
          {{ $t('Mounting.confirm') }}
        </el-button>
        <el-button @click="closeDialogExit">{{ $t('Mounting.cancel') }}</el-button>
      </div>
    </template>
  </el-dialog> 
</template>
<style scoped>
#dt { display:block; float:left;line-height: 20px;margin-left: 100px;}
#dta { display:block; float:left;line-height: 20px;margin-left: 80%;}
#dialog-footer{
  text-align: center;
  margin-top: -15px;
}
#message{
  text-align: center;
  align-items: center;
  color: black;
   width: 200px;
   height: 100px;
   background-color: #337ecc;
   margin-left: 28%;
}
#awatch{
  height: 460px;
}
</style>