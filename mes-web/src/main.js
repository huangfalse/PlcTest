import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'   /** Pinia持久化插件 **/
import App from './App.vue'   /** Vue应用根组件 **/
import router from './router'   /** 导入VueRouter实例 **/
import ElementPlus from 'element-plus'   /** 导入ElementPlus组件库 **/
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'   /** 导入ElementPlus组件库其中文语言包 **/
import i18n from '@/lang'   /** 导入国际化支持配置 **/
import ECharts from 'vue-echarts'   /** 导入ECharts及其Vue封装组件 **/
import './assets/main.css'   /** 导入样式文件 **/
import 'element-plus/dist/index.css'   /** 导入样式文件 **/
import 'babel-polyfill'   /** 导入 Babel Polyfill **/
import * as echarts from 'echarts'   /** 导入ECharts及其Vue封装组件 **/
import * as ElementPlusIconsVue from '@element-plus/icons-vue'   /** 导入ElementPlus的图标库 **/
import { createApp } from 'vue'   /** 导入Vue的核心函数**/
import { createPinia } from 'pinia'   /** 导入Pinia的核心函数**/
import { CanvasRenderer } from 'echarts/renderers'   /** 导入ECharts及其Vue封装组件 **/
const pinia = createPinia()
const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.use(pinia)
app.use(router)
app.use(i18n)
app.use(ElementPlus,{
    locale: zhCn,
})
echarts.use([CanvasRenderer]);
pinia.use(piniaPluginPersistedstate)
app.mount('#app')
app.component('v-chart', ECharts);
app.config.globalProperties.$echarts = echarts;