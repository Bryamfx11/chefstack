import { ref } from 'vue'

// toasts compartidos (singleton)
const toasts = ref([])
let contador = 0

export function useToast() {
  function mostrar(mensaje, tipo = 'ok') {
    const id = ++contador
    toasts.value.push({ id, mensaje, tipo })
    // se cierra solo a los 3.2s
    setTimeout(() => {
      toasts.value = toasts.value.filter((t) => t.id !== id)
    }, 3200)
  }

  return {
    toasts,
    exito: (m) => mostrar(m, 'ok'),
    error: (m) => mostrar(m, 'error')
  }
}
