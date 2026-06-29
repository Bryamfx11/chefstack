<script setup>
import { ref, onMounted, watch } from 'vue'
import { recetasApi } from '../api/recetas'
import { categoriasApi } from '../api/categorias'
import { useToast } from '../composables/useToast'
import RecetaCard from '../components/RecetaCard.vue'

const toast = useToast()
const recetas = ref([])
const categorias = ref([])
const cargando = ref(true)
const busqueda = ref('')
const categoriaActiva = ref(null)
let debounce

async function cargarRecetas() {
  cargando.value = true
  try {
    const params = {}
    if (categoriaActiva.value) params.categoriaId = categoriaActiva.value
    if (busqueda.value.trim()) params.buscar = busqueda.value.trim()
    recetas.value = await recetasApi.listar(params)
  } catch {
    toast.error('No se pudieron cargar las recetas')
  } finally {
    cargando.value = false
  }
}

onMounted(async () => {
  try {
    categorias.value = await categoriasApi.listar()
  } catch { /* no critico */ }
  await cargarRecetas()
})

// debounce de la busqueda
watch(busqueda, () => {
  clearTimeout(debounce)
  debounce = setTimeout(cargarRecetas, 350)
})

function filtrarPor(id) {
  categoriaActiva.value = categoriaActiva.value === id ? null : id
  cargarRecetas()
}
</script>

<template>
  <!-- HERO -->
  <section class="hero">
    <div class="contenedor hero-grid">
      <div class="hero-texto aparece">
        <span class="eyebrow">Recetario digital</span>
        <h1>
          Cocina algo<br /><span class="resaltado">memorable</span> hoy.
        </h1>
        <p class="muted hero-sub">
          Explora, guarda y organiza tus recetas favoritas. Cada plato con sus ingredientes,
          su tiempo y hasta sus calorías estimadas.
        </p>
        <div class="buscador-hero">
          <span class="lupa">🔍</span>
          <input v-model="busqueda" type="text" placeholder="Busca por nombre: brownie, pasta..." />
        </div>
      </div>
      <div class="hero-deco aparece" aria-hidden="true">
        <div class="plato p1">🥘</div>
        <div class="plato p2">🍝</div>
        <div class="plato p3">🧁</div>
      </div>
    </div>
  </section>

  <!-- FILTROS -->
  <section class="contenedor filtros" v-if="categorias.length">
    <button class="filtro" :class="{ activo: categoriaActiva === null }" @click="filtrarPor(null)">Todas</button>
    <button
      v-for="c in categorias" :key="c.id"
      class="filtro" :class="{ activo: categoriaActiva === c.id }"
      @click="filtrarPor(c.id)"
    >{{ c.nombre }}</button>
  </section>

  <!-- LISTADO -->
  <section class="contenedor seccion">
    <div v-if="cargando" class="spinner"></div>

    <template v-else>
      <div v-if="recetas.length">
        <div class="lista-head">
          <span class="eyebrow">El recetario</span>
          <h2>{{ recetas.length }} {{ recetas.length === 1 ? 'receta' : 'recetas' }} para inspirarte</h2>
        </div>
        <div class="grid-recetas">
          <RecetaCard v-for="r in recetas" :key="r.id" :receta="r" class="aparece" />
        </div>
      </div>
      <div v-else class="vacio">
        <span class="vacio-icono">🍽️</span>
        <h3>No encontramos recetas</h3>
        <p class="muted">Prueba con otra búsqueda o limpia los filtros.</p>
      </div>
    </template>
  </section>
</template>

<style scoped>
.hero { padding: 3rem 0 1rem; }
.hero-grid { display: grid; grid-template-columns: 1.15fr 0.85fr; align-items: center; gap: 2rem; }
.hero h1 { font-size: clamp(2.8rem, 6.2vw, 4.6rem); font-weight: 600; margin: 1.2rem 0; letter-spacing: -0.03em; }
.resaltado { color: var(--terracota); font-style: italic; font-weight: 500; position: relative; white-space: nowrap; }
.resaltado::after {
  content: '';
  position: absolute; left: 0; right: 0; bottom: -0.08em; height: 0.34em;
  background: no-repeat center / 100% 100%
    url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 300 18'%3E%3Cpath d='M3 12 C 60 4, 130 4, 180 9 S 270 15, 297 7' fill='none' stroke='%23e8853d' stroke-width='5' stroke-linecap='round'/%3E%3C/svg%3E");
  opacity: 0.85;
}
.hero-sub { font-size: 1.08rem; max-width: 46ch; }

.lista-head { margin-bottom: 1.6rem; }
.lista-head h2 { font-size: clamp(1.6rem, 3vw, 2.1rem); margin-top: 0.5rem; }

.buscador-hero { position: relative; margin-top: 1.6rem; max-width: 440px; }
.buscador-hero .lupa { position: absolute; left: 1rem; top: 50%; transform: translateY(-50%); }
.buscador-hero input { padding-left: 2.6rem; height: 54px; border-radius: 100px; box-shadow: var(--sombra-sm); }

.hero-deco { position: relative; height: 300px; }
.plato {
  position: absolute; font-size: 4.5rem;
  background: var(--papel); border: 1px solid var(--borde); border-radius: 50%;
  width: 130px; height: 130px; display: grid; place-items: center; box-shadow: var(--sombra-md);
}
.p1 { top: 20px; right: 90px; animation: flotar 5s ease-in-out infinite; }
.p2 { top: 120px; right: 0; width: 110px; height: 110px; font-size: 3.6rem; animation: flotar 6s ease-in-out infinite 0.4s; }
.p3 { top: 150px; right: 150px; width: 96px; height: 96px; font-size: 3rem; animation: flotar 5.5s ease-in-out infinite 0.8s; }
@keyframes flotar { 0%, 100% { transform: translateY(0); } 50% { transform: translateY(-14px); } }

.filtros { display: flex; gap: 0.6rem; flex-wrap: wrap; padding-top: 1rem; }
.filtro {
  font-weight: 600; font-size: 0.86rem; padding: 0.5rem 1.1rem; border-radius: 100px;
  background: var(--papel); border: 1.5px solid var(--borde); color: var(--carbon-suave);
  transition: all 0.16s;
}
.filtro:hover { border-color: var(--terracota); color: var(--terracota); }
.filtro.activo { background: var(--carbon); color: var(--crema); border-color: var(--carbon); }

.grid-recetas { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 1.6rem; }

.vacio { text-align: center; padding: 4rem 1rem; }
.vacio-icono { font-size: 3.5rem; display: block; margin-bottom: 1rem; }
.vacio h3 { font-size: 1.5rem; }

@media (max-width: 860px) {
  .hero-deco { display: none; }
  .hero-grid { grid-template-columns: 1fr; }
}
</style>
