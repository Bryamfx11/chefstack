import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authService } from '../firebase'

// tope para no esperar a Firebase indefinidamente
const TIMEOUT_AUTH = 2000

export const useAuthStore = defineStore('auth', () => {
  const usuario = ref(null)
  const cargando = ref(true)

  let resolverListo
  const listo = new Promise((resolve) => { resolverListo = resolve })

  const autenticado = computed(() => usuario.value !== null)
  const nombre = computed(() => usuario.value?.displayName || usuario.value?.email || 'Chef')

  function marcarListo() {
    if (cargando.value) {
      cargando.value = false
      resolverListo()
    }
  }

  function inicializar() {
    try {
      authService.observarSesion((u) => {
        usuario.value = u
        marcarListo()
      })
    } catch (e) {
      console.error('Firebase Auth no disponible:', e)
      marcarListo()
    }
    setTimeout(marcarListo, TIMEOUT_AUTH)
  }

  async function registrar(email, password) {
    await authService.registrar(email, password)
  }
  async function ingresar(email, password) {
    await authService.ingresar(email, password)
  }
  async function ingresarConGoogle() {
    await authService.ingresarConGoogle()
  }
  async function salir() {
    await authService.salir()
  }

  return {
    usuario, cargando, listo, autenticado, nombre,
    inicializar, registrar, ingresar, ingresarConGoogle, salir
  }
})
