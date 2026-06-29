<script setup>
import { ref, onMounted } from 'vue'
import { favoritosApi } from '../api/favoritos'
import { useToast } from '../composables/useToast'
import RecetaCard from '../components/RecetaCard.vue'

const toast = useToast()
const favoritos = ref([])
const cargando = ref(true)

async function cargar() {
  cargando.value = true
  try {
    favoritos.value = await favoritosApi.listar()
  } catch {
    toast.error('No se pudieron cargar tus favoritos')
  } finally {
    cargando.value = false
  }
}
onMounted(cargar)
</script>

<template>
  <section class="contenedor fav-page">
    <h1>Tus favoritos</h1>
    <p class="muted">Las recetas que guardaste para volver a ellas.</p>

    <div v-if="cargando" class="spinner"></div>

    <template v-else>
      <div v-if="favoritos.length" class="grid-recetas">
        <RecetaCard v-for="f in favoritos" :key="f.id" :receta="f.receta" class="aparece" />
      </div>
      <div v-else class="vacio">
        <span class="vacio-icono">♡</span>
        <h3>Todavía no tienes favoritos</h3>
        <p class="muted">Explora recetas y guarda las que más te gusten.</p>
        <RouterLink to="/" class="btn btn-primario">Explorar recetas</RouterLink>
      </div>
    </template>
  </section>
</template>

<style scoped>
.fav-page { padding: 2.2rem 0; }
.fav-page h1 { font-size: 2.6rem; }
.grid-recetas { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 1.6rem; margin-top: 1.6rem; }
.vacio { text-align: center; padding: 4rem 1rem; display: flex; flex-direction: column; align-items: center; gap: 0.6rem; }
.vacio-icono { font-size: 3.5rem; color: var(--terracota); }
.vacio h3 { font-size: 1.5rem; }
.vacio .btn { margin-top: 1rem; }
</style>
