<script setup>
import { useToast } from '../composables/useToast'
const { toasts } = useToast()
</script>

<template>
  <div class="toast-zona">
    <TransitionGroup name="toast">
      <div v-for="t in toasts" :key="t.id" class="toast" :class="t.tipo === 'ok' ? 'toast-ok' : 'toast-error'">
        {{ t.mensaje }}
      </div>
    </TransitionGroup>
  </div>
</template>

<style scoped>
.toast-enter-active, .toast-leave-active { transition: all 0.3s ease; }
.toast-enter-from, .toast-leave-to { opacity: 0; transform: translateX(40px); }
</style>
