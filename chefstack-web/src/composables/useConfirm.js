import { ref } from 'vue'

const estado = ref(null)

export function useConfirm() {
  // abre el modal y resuelve true/false segun la eleccion
  function confirmar({ titulo = '¿Confirmar?', mensaje = '', accion = 'Aceptar', peligro = false } = {}) {
    return new Promise((resolve) => {
      estado.value = { titulo, mensaje, accion, peligro, resolve }
    })
  }

  function responder(valor) {
    estado.value?.resolve(valor)
    estado.value = null
  }

  return { estado, confirmar, responder }
}
