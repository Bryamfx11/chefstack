<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { useToast } from '../composables/useToast'

const auth = useAuthStore()
const router = useRouter()
const route = useRoute()
const toast = useToast()

const email = ref('')
const password = ref('')
const procesando = ref(false)

function destino() {
  return route.query.redirigir || '/'
}

async function ingresar() {
  procesando.value = true
  try {
    await auth.ingresar(email.value, password.value)
    toast.exito('¡Bienvenido de nuevo!')
    router.push(destino())
  } catch {
    toast.error('Credenciales incorrectas')
  } finally {
    procesando.value = false
  }
}

async function conGoogle() {
  try {
    await auth.ingresarConGoogle()
    toast.exito('¡Sesión iniciada con Google!')
    router.push(destino())
  } catch {
    toast.error('No se pudo iniciar con Google')
  }
}
</script>

<template>
  <section class="auth">
    <div class="auth-card card aparece">
      <span class="marca-mini">🍴 Chef<span class="acento">Stack</span></span>
      <h1>Hola de nuevo</h1>
      <p class="muted">Ingresa para gestionar tus recetas y favoritos.</p>

      <form @submit.prevent="ingresar" class="form">
        <div>
          <label>Correo electrónico</label>
          <input v-model="email" type="email" required placeholder="tucorreo@ejemplo.com" />
        </div>
        <div>
          <label>Contraseña</label>
          <input v-model="password" type="password" required placeholder="••••••••" />
        </div>
        <button class="btn btn-primario btn-bloque" :disabled="procesando">
          {{ procesando ? 'Ingresando...' : 'Ingresar' }}
        </button>
      </form>

      <div class="separador"><span>o</span></div>
      <button class="btn btn-fantasma btn-bloque" @click="conGoogle">
        <img src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg" alt="" width="18" />
        Continuar con Google
      </button>

      <p class="cambio muted">¿No tienes cuenta? <RouterLink to="/registro" class="acento">Regístrate</RouterLink></p>
    </div>
  </section>
</template>

<style scoped>
.auth { display: grid; place-items: center; padding: 3rem 1rem; }
.auth-card { width: min(440px, 100%); padding: 2.4rem; text-align: center; }
.marca-mini { font-family: var(--display); font-weight: 700; font-size: 1.2rem; }
.acento { color: var(--terracota); }
.auth-card h1 { font-size: 2rem; margin: 1rem 0 0.3rem; }
.form { display: flex; flex-direction: column; gap: 1rem; margin: 1.6rem 0 1.2rem; text-align: left; }
.form button { margin-top: 0.4rem; }
.separador { display: flex; align-items: center; gap: 1rem; color: var(--carbon-suave); font-size: 0.85rem; margin: 1.2rem 0; }
.separador::before, .separador::after { content: ''; flex: 1; height: 1px; background: var(--borde); }
.cambio { margin-top: 1.4rem; font-size: 0.9rem; }
.btn-fantasma { gap: 0.6rem; }
</style>
