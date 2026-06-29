import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { useAuthStore } from './stores/auth'
import './assets/styles.css'

const app = createApp(App)
app.use(createPinia())

// arranca el observador de sesion antes de montar
const auth = useAuthStore()
auth.inicializar()

app.use(router)
app.mount('#app')
