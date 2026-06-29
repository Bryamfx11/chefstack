<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { recetasApi } from '../api/recetas'
import { favoritosApi } from '../api/favoritos'
import { useAuthStore } from '../stores/auth'
import { useToast } from '../composables/useToast'
import { useConfirm } from '../composables/useConfirm'
import { dificultadInfo, imagenSrc } from '../utils/formato'

const props = defineProps({ id: { type: String, required: true } })
const router = useRouter()
const auth = useAuthStore()
const toast = useToast()
const { confirmar } = useConfirm()

const receta = ref(null)
const cargando = ref(true)
const esFavorita = ref(false)

const dif = computed(() => receta.value ? dificultadInfo(receta.value.dificultad) : {})
const imagen = computed(() => imagenSrc(receta.value?.imagenUrl))

onMounted(async () => {
  try {
    receta.value = await recetasApi.obtener(props.id)
    if (auth.autenticado) {
      const favs = await favoritosApi.listar()
      esFavorita.value = favs.some((f) => f.receta.id === receta.value.id)
    }
  } catch {
    toast.error('No se encontró la receta')
    router.push('/')
  } finally {
    cargando.value = false
  }
})

async function alternarFavorito() {
  try {
    if (esFavorita.value) {
      await favoritosApi.eliminar(receta.value.id)
      esFavorita.value = false
      toast.exito('Quitada de favoritos')
    } else {
      await favoritosApi.agregar(receta.value.id)
      esFavorita.value = true
      toast.exito('¡Guardada en favoritos!')
    }
  } catch {
    toast.error('No se pudo actualizar favoritos')
  }
}

async function eliminar() {
  const ok = await confirmar({
    titulo: 'Eliminar receta',
    mensaje: `¿Seguro que quieres eliminar "${receta.value.nombre}"? Esta acción no se puede deshacer.`,
    accion: 'Eliminar', peligro: true
  })
  if (!ok) return
  try {
    await recetasApi.eliminar(receta.value.id)
    toast.exito('Receta eliminada')
    router.push('/')
  } catch {
    toast.error('No se pudo eliminar')
  }
}
</script>

<template>
  <div v-if="cargando" class="spinner"></div>

  <article v-else-if="receta" class="contenedor detalle">
    <RouterLink to="/" class="volver">← Volver a recetas</RouterLink>

    <div class="detalle-grid">
      <!-- imagen + acciones -->
      <div class="col-media">
        <div class="foto"><img :src="imagen" :alt="receta.nombre" /></div>

        <div v-if="auth.autenticado" class="acciones">
          <button class="btn btn-primario btn-bloque" @click="alternarFavorito">
            {{ esFavorita ? '♥ En favoritos' : '♡ Guardar en favoritos' }}
          </button>
          <div class="acciones-fila">
            <RouterLink :to="`/recetas/${receta.id}/editar`" class="btn btn-fantasma btn-bloque">Editar</RouterLink>
            <button class="btn btn-peligro btn-bloque" @click="eliminar">Eliminar</button>
          </div>
        </div>
        <p v-else class="muted nota-login">
          <RouterLink to="/login" class="enlace-login">Inicia sesión</RouterLink> para guardar esta receta.
        </p>
      </div>

      <!-- contenido -->
      <div class="col-info">
        <span v-if="receta.categoria" class="chip chip-terracota">{{ receta.categoria.nombre }}</span>
        <h1>{{ receta.nombre }}</h1>
        <p class="descripcion muted">{{ receta.descripcion }}</p>

        <div class="datos">
          <div class="dato"><span class="dato-num">{{ receta.tiempoPreparacion || '—' }}</span><span class="dato-lbl">minutos</span></div>
          <div class="dato"><span class="dato-num">{{ receta.porciones || '—' }}</span><span class="dato-lbl">porciones</span></div>
          <div class="dato"><span class="dato-num">{{ receta.calorias || '—' }}</span><span class="dato-lbl">kcal</span></div>
          <div class="dato"><span class="chip" :class="dif.clase">{{ dif.texto }}</span><span class="dato-lbl">dificultad</span></div>
        </div>

        <section class="bloque">
          <h2>Ingredientes</h2>
          <ul class="ingredientes">
            <li v-for="ing in receta.ingredientes" :key="ing.id">
              <span class="check">✓</span>
              <span><strong>{{ ing.cantidad }} {{ ing.unidad }}</strong> {{ ing.nombre }}</span>
            </li>
            <li v-if="!receta.ingredientes.length" class="muted">Sin ingredientes registrados.</li>
          </ul>
        </section>

        <section class="bloque">
          <h2>Preparación</h2>
          <p class="instrucciones">{{ receta.instrucciones || 'Sin instrucciones registradas.' }}</p>
        </section>
      </div>
    </div>
  </article>
</template>

<style scoped>
.detalle { padding: 2rem 0 1rem; }
.volver { font-weight: 600; color: var(--carbon-suave); font-size: 0.9rem; }
.volver:hover { color: var(--terracota); }

.detalle-grid { display: grid; grid-template-columns: 0.9fr 1.1fr; gap: 2.6rem; margin-top: 1.4rem; }

.foto { border-radius: var(--radio); overflow: hidden; box-shadow: var(--sombra-md); aspect-ratio: 1; }
.foto img { width: 100%; height: 100%; object-fit: cover; }

.acciones { margin-top: 1.2rem; display: flex; flex-direction: column; gap: 0.7rem; }
.acciones-fila { display: flex; gap: 0.7rem; }
.nota-login { margin-top: 1.2rem; text-align: center; }
.enlace-login { color: var(--terracota); font-weight: 600; }

.col-info h1 { font-size: clamp(2rem, 4.5vw, 3rem); margin: 0.7rem 0; }
.descripcion { font-size: 1.06rem; }

.datos { display: flex; gap: 1.6rem; flex-wrap: wrap; margin: 1.6rem 0; padding: 1.2rem 0; border-top: 1px solid var(--borde); border-bottom: 1px solid var(--borde); }
.dato { display: flex; flex-direction: column; align-items: center; gap: 0.25rem; }
.dato-num { font-family: var(--display); font-size: 1.7rem; font-weight: 600; color: var(--terracota); }
.dato-lbl { font-size: 0.76rem; text-transform: uppercase; letter-spacing: 0.05em; color: var(--carbon-suave); }

.bloque { margin-top: 1.8rem; }
.bloque h2 { font-size: 1.5rem; margin-bottom: 0.9rem; }
.ingredientes { list-style: none; display: flex; flex-direction: column; gap: 0.6rem; }
.ingredientes li { display: flex; align-items: flex-start; gap: 0.7rem; font-size: 0.98rem; }
.check { color: var(--hierba); font-weight: 700; }
.instrucciones { white-space: pre-line; line-height: 1.7; font-size: 1rem; }

@media (max-width: 820px) {
  .detalle-grid { grid-template-columns: 1fr; gap: 1.6rem; }
}
</style>
