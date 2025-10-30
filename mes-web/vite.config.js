
import vue from '@vitejs/plugin-vue'
import ReactivityTransform from '@vue-macros/reactivity-transform/vite'
import AutoImport from 'unplugin-auto-import/vite'
import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
export default defineConfig({
  plugins: [
    vue(),
    ReactivityTransform(),
    AutoImport({
      imports: [
        'vue-i18n',
      ],
      dts: './auto-imports.d.ts',
      eslintrc: {
        enabled: false, // 配置更新时临时设为true,
      },
    }),
  ],
  resolve: {
    alias: {
      'vue-i18n': 'vue-i18n/dist/vue-i18n.cjs.js',
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }, 
  server: {
    port: 801,
    host: true,
    open: false,
    https: false,
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:88/',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/'),
      },
    },
  },
  configureServer: ({ middlewares }) => {
    middlewares.use(
      '/mesModuleTools',
      createProxyMiddleware({
        target: 'ws://localhost:8081',
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/mesModuleTools': '',
        },
      })
    );
  },
});