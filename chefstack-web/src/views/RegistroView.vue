<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { useToast } from '../composables/useToast'

const auth = useAuthStore()
const router = useRouter()
const toast = useToast()

const email = ref('')
const password = ref('')
const confirmar = ref('')
const procesando = ref(false)

async function registrar() {
  if (password.value.length < 6) {
    toast.error('La contraseña debe tener al menos 6 caracteres')
    return
  }
  if (password.value !== confirmar.value) {
    toast.error('Las contraseñas no coinciden')
    return
  }
  procesando.value = true
  try {
    await auth.registrar(email.value, password.value)
    toast.exito('¡Cuenta creada! Bienvenido a ChefStack')
    router.push('/')
  } catch (e) {
    const msg = e?.code === 'auth/email-already-in-use' ? 'Ese correo ya está registrado' : 'No se pudo crear la cuenta'
    toast.error(msg)
  } finally {
    procesando.value = false
  }
}

async function conGoogle() {
  try {
    await auth.ingresarConGoogle()
    toast.exito('¡Cuenta lista con Google!')
    router.push('/')
  } catch {
    toast.error('No se pudo continuar con Google')
  }
}
</script>

<template>
  <section class="auth">
    <div class="auth-card card aparece">
      <span class="marca-mini">🍴 Chef<span class="acento">Stack</span></span>
      <h1>Crea tu cuenta</h1>
      <p class="muted">Guarda tus recetas y arma tu recetario.</p>

      <form @submit.prevent="registrar" class="form">
        <div>
          <label>Correo electrónico</label>
          <input v-model="email" type="email" required placeholder="tucorreo@ejemplo.com" />
        </div>
        <div>
          <label>Contraseña</label>
          <input v-model="password" type="password" required placeholder="Mínimo 6 caracteres" />
        </div>
        <div>
          <label>Confirmar contraseña</label>
          <input v-model="confirmar" type="password" required placeholder="Repite tu contraseña" />
        </div>
        <button class="btn btn-primario btn-bloque" :disabled="procesando">
          {{ procesando ? 'Creando...' : 'Crear cuenta' }}
        </button>
      </form>

      <div class="separador"><span>o</span></div>
      <button class="btn btn-fantasma btn-bloque" @click="conGoogle">
        <img src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg" alt="" width="18" />
        Continuar con Google
      </button>

      <p class="cambio muted">¿Ya tienes cuenta? <RouterLink to="/login" class="acento">Ingresa</RouterLink></p>
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
