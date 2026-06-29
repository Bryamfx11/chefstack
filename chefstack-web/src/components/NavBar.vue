<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { useToast } from '../composables/useToast'

const auth = useAuthStore()
const router = useRouter()
const toast = useToast()
const menuAbierto = ref(false)

async function cerrarSesion() {
  await auth.salir()
  toast.exito('Sesion cerrada')
  router.push('/')
  menuAbierto.value = false
}
</script>

<template>
  <header class="nav">
    <div class="contenedor nav-inner">
      <RouterLink to="/" class="marca" @click="menuAbierto = false">
        <span class="marca-icono">🍴</span>
        <span class="marca-texto">Chef<span class="marca-acento">Stack</span></span>
      </RouterLink>

      <button class="hamburguesa" @click="menuAbierto = !menuAbierto" aria-label="Menu">
        <span :class="{ abierto: menuAbierto }"></span>
      </button>

      <nav class="enlaces" :class="{ abierto: menuAbierto }" @click="menuAbierto = false">
        <RouterLink to="/" class="enlace">Recetas</RouterLink>
        <RouterLink v-if="auth.autenticado" to="/categorias" class="enlace">Categorias</RouterLink>
        <RouterLink v-if="auth.autenticado" to="/favoritos" class="enlace">Favoritos</RouterLink>

        <template v-if="auth.autenticado">
          <RouterLink to="/recetas/nueva" class="btn btn-primario">+ Nueva receta</RouterLink>
          <div class="usuario">
            <span class="avatar">{{ auth.nombre.charAt(0).toUpperCase() }}</span>
            <button class="enlace salir" @click="cerrarSesion">Salir</button>
          </div>
        </template>
        <template v-else>
          <RouterLink to="/login" class="enlace">Ingresar</RouterLink>
          <RouterLink to="/registro" class="btn btn-primario">Crear cuenta</RouterLink>
        </template>
      </nav>
    </div>
  </header>
</template>

<style scoped>
.nav {
  position: sticky; top: 0; z-index: 100;
  background: rgba(251, 246, 239, 0.82);
  backdrop-filter: blur(14px);
  border-bottom: 1px solid var(--borde);
}
.nav-inner { display: flex; align-items: center; justify-content: space-between; height: 70px; }

.marca { display: flex; align-items: center; gap: 0.5rem; font-family: var(--display); font-weight: 700; font-size: 1.4rem; }
.marca-icono { font-size: 1.3rem; }
.marca-acento { color: var(--terracota); }

.enlaces { display: flex; align-items: center; gap: 1.4rem; }
.enlace {
  font-weight: 600; font-size: 0.92rem; color: var(--carbon-suave);
  transition: color 0.15s; position: relative;
}
.enlace:hover, .enlace.router-link-active { color: var(--terracota); }
.salir { background: none; }

.usuario { display: flex; align-items: center; gap: 0.7rem; padding-left: 1rem; border-left: 1px solid var(--borde); }
.avatar {
  width: 34px; height: 34px; border-radius: 50%;
  display: grid; place-items: center;
  background: var(--terracota); color: #fff; font-weight: 700; font-size: 0.9rem;
}

.hamburguesa { display: none; width: 40px; height: 40px; }
.hamburguesa span, .hamburguesa span::before, .hamburguesa span::after {
  content: ''; display: block; width: 22px; height: 2px; background: var(--carbon);
  border-radius: 2px; transition: 0.25s; position: relative;
}
.hamburguesa span::before { position: absolute; top: -7px; }
.hamburguesa span::after { position: absolute; top: 7px; }
.hamburguesa span.abierto { background: transparent; }
.hamburguesa span.abierto::before { top: 0; transform: rotate(45deg); }
.hamburguesa span.abierto::after { top: 0; transform: rotate(-45deg); }

@media (max-width: 860px) {
  .hamburguesa { display: grid; place-items: center; }
  .enlaces {
    position: absolute; top: 70px; left: 0; right: 0;
    flex-direction: column; align-items: flex-start; gap: 1rem;
    background: var(--crema); border-bottom: 1px solid var(--borde);
    padding: 1.4rem 4vw; transform: translateY(-130%); transition: transform 0.3s ease;
  }
  .enlaces.abierto { transform: translateY(0); }
  .usuario { padding-left: 0; border-left: none; border-top: 1px solid var(--borde); padding-top: 1rem; width: 100%; }
}
</style>
