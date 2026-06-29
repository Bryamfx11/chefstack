<script setup>
import { computed } from 'vue'
import { dificultadInfo, imagenSrc } from '../utils/formato'

const props = defineProps({ receta: { type: Object, required: true } })
const dif = computed(() => dificultadInfo(props.receta.dificultad))
const imagen = computed(() => imagenSrc(props.receta.imagenUrl))
</script>

<template>
  <RouterLink :to="`/recetas/${receta.id}`" class="receta-card">
    <div class="foto-wrap">
      <img :src="imagen" :alt="receta.nombre" loading="lazy" />
      <span v-if="receta.categoria" class="chip categoria-flotante">{{ receta.categoria.nombre }}</span>
      <span class="ver">Ver receta →</span>
    </div>
    <div class="cuerpo">
      <div class="metas-top">
        <span class="chip" :class="dif.clase">{{ dif.texto }}</span>
        <span v-if="receta.tiempoPreparacion" class="meta">{{ receta.tiempoPreparacion }} min</span>
      </div>
      <h3 class="titulo">{{ receta.nombre }}</h3>
      <p class="desc muted">{{ receta.descripcion }}</p>
      <div class="pie-card">
        <span v-if="receta.calorias" class="kcal">{{ receta.calorias }} <small>kcal</small></span>
        <span class="flecha">→</span>
      </div>
    </div>
  </RouterLink>
</template>

<style scoped>
.receta-card {
  position: relative;
  display: flex; flex-direction: column;
  background: var(--papel); border: 1px solid var(--borde); border-radius: var(--radio);
  overflow: hidden; box-shadow: var(--sombra-sm);
  transition: transform 0.28s cubic-bezier(0.22,1,0.36,1), box-shadow 0.28s, border-color 0.28s;
}
.receta-card::before {
  content: ''; position: absolute; top: 0; left: 0; right: 0; height: 3px;
  background: linear-gradient(90deg, var(--terracota), var(--ambar));
  transform: scaleX(0); transform-origin: left; transition: transform 0.3s ease; z-index: 3;
}
.receta-card:hover { transform: translateY(-6px); box-shadow: var(--sombra-md); border-color: var(--borde-fuerte); }
.receta-card:hover::before { transform: scaleX(1); }

.foto-wrap { position: relative; aspect-ratio: 4 / 3; overflow: hidden; }
.foto-wrap::after {
  content: ''; position: absolute; inset: 0;
  background: linear-gradient(to top, rgba(39,29,20,0.28), transparent 42%);
}
.foto-wrap img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.6s cubic-bezier(0.22,1,0.36,1); }
.receta-card:hover .foto-wrap img { transform: scale(1.07); }
.categoria-flotante {
  position: absolute; top: 0.8rem; left: 0.8rem; z-index: 2;
  background: rgba(39, 29, 20, 0.8); color: var(--crema); backdrop-filter: blur(4px);
}
.ver {
  position: absolute; bottom: 0.8rem; left: 0.9rem; z-index: 2;
  color: #fff; font-weight: 700; font-size: 0.82rem; letter-spacing: 0.02em;
  opacity: 0; transform: translateY(6px); transition: opacity 0.25s, transform 0.25s;
}
.receta-card:hover .ver { opacity: 1; transform: translateY(0); }

.cuerpo { padding: 1.1rem 1.25rem 1.25rem; display: flex; flex-direction: column; gap: 0.45rem; }
.metas-top { display: flex; align-items: center; gap: 0.7rem; }
.metas-top .meta { font-size: 0.78rem; font-weight: 700; color: var(--carbon-suave); text-transform: uppercase; letter-spacing: 0.04em; }
.metas-top .meta::before { content: '· '; color: var(--borde-fuerte); }

.titulo { font-size: 1.32rem; line-height: 1.12; margin-top: 0.1rem; }
.desc {
  font-size: 0.9rem; line-height: 1.45;
  display: -webkit-box; -webkit-line-clamp: 2; line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;
}
.pie-card {
  display: flex; align-items: center; justify-content: space-between;
  margin-top: 0.5rem; padding-top: 0.8rem; border-top: 1px solid var(--borde);
}
.kcal { font-family: var(--display); font-weight: 600; font-size: 1.15rem; color: var(--terracota); }
.kcal small { font-family: var(--texto); font-size: 0.72rem; font-weight: 600; color: var(--carbon-suave); }
.flecha {
  width: 32px; height: 32px; border-radius: 50%; display: grid; place-items: center;
  background: var(--crema-2); color: var(--carbon); font-weight: 700;
  transition: background 0.22s, color 0.22s, transform 0.22s;
}
.receta-card:hover .flecha { background: var(--terracota); color: #fff; transform: translateX(2px); }
</style>
