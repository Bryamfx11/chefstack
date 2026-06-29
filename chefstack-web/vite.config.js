import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// Configuracion de Vite: plugin de Vue y servidor de desarrollo en el puerto 5173
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    host: true
  }
})
