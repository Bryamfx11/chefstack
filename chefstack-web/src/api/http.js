import axios from 'axios'
import { authService } from '../firebase'

// base de la api (VITE_API_URL en prod)
const baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const http = axios.create({ baseURL })

// adjunta el token de Firebase si hay sesion
http.interceptors.request.use(async (config) => {
  const usuario = authService.usuarioActual()
  if (usuario) {
    config.headers.Authorization = `Bearer ${await usuario.getIdToken()}`
  }
  return config
})

export default http
