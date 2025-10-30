<script setup>
import useUserInfoStore from '@/stores/userInfo'
import userInfo from '@/stores/userInfo'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router';  
import { useI18n } from 'vue-i18n'
import { ref, watch, onMounted, provide } from 'vue'
const globalDate = ref('');
const store = userInfo()
const isCollapse = ref(false);  
const router = useRouter()
const userStore = useUserInfoStore()
const { t } = useI18n()
let language = ref(localStorage.getItem('lang') || 'zh')
let userInfoStr = window.localStorage.getItem('userInfo')
let token = userInfo; // 提取 token  
// 退出登录方法  
function quit() {
  store.$patch({
              user: null,
              })
  router.push('/login')  
}
  function replaceChineseWithEnglish(menuData) {  
      // 定义中英文对照关系对象
       const translation  = {
    '上片机': 'Loading Machine',
    '上片一线': 'First line on film',
    '上片二线': 'Second line on film',
    '掰片/识别': 'Breaking /Identification',
    '掰片一线': 'Broken piece line',
    '掰片二线': 'Broken pieces with two lines',
    '卧式缓存': 'Horizontal cache',
    '卧式缓存一号线': 'Horizontal Cache 1',
    '卧式缓存二号线': 'Horizontal Cache 2',
    '磨边前卧式缓存': 'Horizontal buffer before edge grinding',
    '下片卧式缓存': 'Offline horizontal cache',
    '磨边（冷加工）': 'Grinding edge (cold processing)',
    '磨边(冷加工)': 'Grinding edge (cold processing)',
    '1线磨边(冷加工)': '1-line edge grinding (cold processing)',
    '2线磨边(冷加工)': '2-line edge grinding (cold processing)',
    '大理片笼': 'Buffer system',
    '钢化': 'Tempering',
    '钢化查询': 'Tempered steel inquiry',
    '钢化前': 'Before Tempering',
    '钢化中': 'Tempering',
    '钢化后': 'After Tempering',
    '下片台': 'Next Stage',
    '系统管理': 'System Management',
    '用户管理': 'User Management',
    '权限管理': 'Rights Management',
    '角色管理': 'Roles',
    '系统配置': 'system configuration',
    '可视化系统': 'visualization system',
    '报工管理': 'Job reporting management',
    '仓储中心': 'Storage Center',
    '原片仓储': 'Original film storage',
    '中空': 'Hollow',
    '中空一线': 'Hollow Line',
    '中空二线': 'Hollow Second Line',
    '中空三线': 'Hollow three line',
    '中空理片笼': 'Hollow Film processing cage',
    '折铝框一线': '1 Fold aluminum frame',
    '折铝框二线': '2 Fold aluminum frame',
    '折铝框三线': '3 Fold aluminum frame',
    '大屏': 'Big screen',
    '大屏显示': 'Large Screen Display',
     };  
  menuData.forEach(menu => {  
    menu.menuName = translation[menu.menuName] || menu.menuName;  
      menu.children && menu.children.forEach(submenu => {  
        submenu.menuName = translation[submenu.menuName] || submenu.menuName;  
      });
  });  
} 
function replaceChineseWithRussian(menuData) {  
      // 定义中俄文对照关系对象
   const translation  = {
    '上片机': 'Стол загрузки',
    '上片一线': 'Первая линия пленки',
    '上片二线': 'Вторая линия фильма',
    '掰片/识别': 'Разлом/идентификация',
    '掰片一线': 'Разбейте линию детали',
    '掰片二线': 'Разбиваем вторую линию',
    '卧式缓存': 'Горизонтальный буфер ',
    '卧式缓存一号线': 'Горизонтальная строка кэша 1',
    '卧式缓存二号线': 'Горизонтальная строка кэша 2',
    '磨边前卧式缓存': 'Горизонтальный буфер перед шлифовкой',
    '下片卧式缓存': 'Горизонтальный буфер стекла разгрузки',
    '磨边（冷加工）': 'Шлифовать края (холодная обработка)',
    '磨边(冷加工)': 'Шлифовать края (холодная обработка)',
    '1线磨边(冷加工)': 'Шлифовать края 1 линии (холодная обработка)',
    '2线磨边(冷加工)': 'Шлифовать края 2 линии (холодная обработка)',
    '大理片笼': 'Буферная система',
    '钢化': 'Закалка',
    '钢化查询': 'Запрос на темперирование',
    '钢化前': 'Перед закалки',
    '钢化中': 'Отпуск',
    '钢化后': 'После закалки',
    '下片台': 'Стол разгрузки',
    '系统管理': 'Управление системой',
    '用户管理': 'Управление пользователем',
    '权限管理': 'Управление полномочиями',
    '角色管理': 'Управление ролью',
    '系统配置': 'system configuration',
    '可视化系统': 'Система визуализации',
    '报工管理': 'Управление отчетами',
    '仓储中心': 'Складской центр',
    '原片仓储': 'Хранение оригинальной пленки',
    '中空': 'полый',
    '中空一线': 'Полая линия',
    '中空二线': 'Полая вторая линия',
    '中空三线': 'Пустые три линии',
    '中空理片笼': 'Сепаратор для полых деталей',
    '折铝框一线': 'Линейка фальцевых алюминиевых рам',
    '折铝框二线': 'Сложите алюминиевую раму двумя проводами',
    '折铝框三线': 'Сложите алюминиевую раму тремя линиями',
    '大屏': 'Большой экран',
    '大屏显示': 'Большой экран',
     };  
  menuData.forEach(menu => {  
    menu.menuName = translation[menu.menuName] || menu.menuName;
      menu.children && menu.children.forEach(submenu => {  
        submenu.menuName = translation[submenu.menuName] || submenu.menuName;  
      });
  });  
} 
//提取菜单模块列表
let menuList = $ref([])
request.get('/plcSend/sys/menu/getMenuTree').then((res) => {
  if (res.code == 200) {
    menuList = res.data
      if (language.value === 'en') {
        replaceChineseWithEnglish(menuList);
      }
      if (language.value === 'py') {
        replaceChineseWithRussian(menuList);
      }
  } else {
    ElMessage.warning(res.msg)
    router.push('/login')
  }
})
request.post('/plcSend/sys/menu/getNowDate').then((res) => {
  if (res.code == 200) {
      window.localStorage.setItem('getglobalDate', res.data)
  } else {
    ElMessage.warning(res.msg)
  }
})
const menu = ref(null)
let indexKey = ref(null)
function handleOpen(key) {
  indexKey.value = key
}
watch(
    indexKey,
    (newVal, oldVal) => {
      if (oldVal !== newVal && oldVal !== null) {
        menu.value.close(oldVal)
      }
    },
    {deep: true}
)
router.beforeEach((to, from) => {
})
let openFlag = $ref(null)
//原始菜单打开
const openMenu = (menuID) => {
  if (menuID === openFlag) {
    openFlag = null
  } else {
    openFlag = menuID
  }
}
function toggleCollapse() {  
  isCollapse.value = !isCollapse.value;  
} 
onMounted(() => {
});
  let getglobalDate = window.localStorage.getItem('getglobalDate')
  provide('globalDate', getglobalDate);
</script>
<template>
  <div id="all">
    <el-container>
      <el-header>
        <div style="height: 100%;width: 100%;display: flex;background-color: #fff;">
          <img src="/northGlass.ico"
               alt=""  @click="toggleCollapse"
               style="max-width: 100%;max-height: 100%">
          <h3 style="margin: 1rem;font-weight: bold;width: 33vw;"> {{ $t('main.welcome') }}{{ userName }}{{ $t('main.useNorthGlassMESSystem') }}</h3>
          <span style="height: 70%;width: 78vw;margin-top: 1rem;">
            <el-button class="sys-quit"
                       @click="quit"
                       round>
              <el-icon size="large">
                <SwitchButton size=""/>{{ $t('main.quit') }}
              </el-icon>
            </el-button>
          </span>
        </div>
      </el-header>
      <div id="line"></div>
      <el-container>
      <el-aside :width="isCollapse ? '0px' : '200px'" style="background-color: #fff;">
        <el-row :span="20">
            <el-menu :default-active="activePath" class="el-menu-vertical-demo" >
              <div class="menu">
            <div v-for="items in menuList">
              <div class='menu_title' v-show="!isCollapse"
                   @click="openMenu(items.id)">
                <span>☰</span>
                {{ items.menuName }}
              </div>
              <ul class='enter-x-left'
                  v-show="openFlag==items.id">
                <li v-for="menuItem in items.children"
                    style="margin-bottom:2px">
                  <router-link :to="{path:menuItem.url}">
                    {{ menuItem.menuName }}
                  </router-link>
                  </li>
                  </ul>
                  </div>
                  </div>
            </el-menu>
          </el-row>
</el-aside>
<el-main>
          <div id="main">
            <router-view></router-view>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<style scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
  text-align: left;
}
#all {
  background-color: #eee;
  height: 100%;
}
.el-container {
  height: 100vh;
  width: 99vw;
}
* {
  padding: 0;
  margin: 0;
}
:deep(.sys-quit) {
  float: right;
  margin-right: 1rem;
  width: 5rem;
}
/* 横线 */
#line {
  float: right;
  width: 100%;
  height: 1px;
  margin-bottom: 0.5em;
  background: #d4c4c4;
  position: relative;
  text-align: center;
}
:deep(span) {
  margin-right: 0;
}
.el-collapse {
  font-size: 16px;
  font-weight: bold;
}
#main {
  width: 99%;
  float: right;
  height: 99%;
  background-color: #fff;
}
.menu {
  width: 138px;
  height: 33px;
  line-height: 25px;
  color: #000000;
  font-size: 16px;
  padding-left: 15px;
  padding-right: 2px;
}
ul {
  height: auto;
  margin: 1px auto;
  text-align: center;
}
ul li {
  height: 28px;
  width: 180px;
  line-height: 30px;
  color: #000000;
  padding-left: 36px;
  cursor: pointer;
  overflow: hidden;
  text-align: left;
}
ul li:hover {
  color: #ffffff;
}
ul li a {
  text-decoration: none;
  color: #000000;
  font-size: 14px;
}
ul li a:hover {
  color: #1087ff;
}
.item_divider {
  width: 118px;
  height: 0px;
  margin-bottom: 2px;
}
.item a {
  width: 138px;
  height: 35px;
  display: block;
  text-decoration: none;
  color: white;
  font-size: 14px;
  text-decoration: none;
}
.menu_title {
  width: 180px;
  height: 45px;
  line-height: 55px;
  color: rgb(43, 42, 42);
  font-size: 17px;
  padding-left: 15px;
  transition: all 0.3s ease;
  cursor: pointer;
  position: relative;
  font-weight: bold;
  overflow: hidden;
}
.menu_title:hover {
  color: #3b9af9;
}
.indicator {
  display: block;
  width: 40px;
  height: 35px;
  font-weight: bold;
  position: absolute;
  right: 0px;
  top: 0px;
  transition: all 0.3s ease;
  text-align: center;
}
/* 动画效果 */
.enter-x-left {
  z-index: 9;
  opacity: 0;
  animation: enter-x-left 0.4s ease-in-out 0.3s;
  animation-fill-mode: forwards;
  transform: translateX(-50px);
  transition: all 0.3s ease;
}
.enter-x-left:nth-child(1) {
  animation-delay: 0.1s;
}
.enter-x-left:nth-child(2) {
  animation-delay: 0.2s;
}
.enter-x-left:nth-child(3) {
  animation-delay: 0.3s;
}
.enter-x-left:nth-child(4) {
  animation-delay: 0.4s;
}
@keyframes enter-x-left {
  to {
    opacity: 1;
    transform: translateX(0);
  }
}
.header-left{
  margin-left: -350px;
}
</style>