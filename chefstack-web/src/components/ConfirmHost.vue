<script setup>
import { useConfirm } from '../composables/useConfirm'
const { estado, responder } = useConfirm()
</script>

<template>
  <Transition name="modal">
    <div v-if="estado" class="overlay" @click.self="responder(false)">
      <div class="modal card" role="dialog" aria-modal="true">
        <h3>{{ estado.titulo }}</h3>
        <p class="muted">{{ estado.mensaje }}</p>
        <div class="acciones">
          <button class="btn btn-fantasma" @click="responder(false)">Cancelar</button>
          <button class="btn" :class="estado.peligro ? 'btn-peligro-solido' : 'btn-primario'" @click="responder(true)">
            {{ estado.accion }}
          </button>
        </div>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
.overlay {
  position: fixed; inset: 0; z-index: 1100;
  background: rgba(39, 29, 20, 0.5); backdrop-filter: blur(3px);
  display: grid; place-items: center; padding: 1.5rem;
}
.modal { width: min(420px, 100%); padding: 1.8rem; text-align: left; }
.modal h3 { font-size: 1.4rem; margin-bottom: 0.5rem; }
.modal p { font-size: 0.96rem; line-height: 1.5; }
.acciones { display: flex; justify-content: flex-end; gap: 0.7rem; margin-top: 1.6rem; }
.btn-peligro-solido { background: var(--terracota); color: #fff; }
.btn-peligro-solido:hover { background: var(--terracota-osc); }

.modal-enter-active, .modal-leave-active { transition: opacity 0.2s ease; }
.modal-enter-active .modal, .modal-leave-active .modal { transition: transform 0.22s cubic-bezier(0.22,1,0.36,1); }
.modal-enter-from, .modal-leave-to { opacity: 0; }
.modal-enter-from .modal, .modal-leave-to .modal { transform: translateY(16px) scale(0.97); }
</style>
