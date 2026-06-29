<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { recetasApi } from '../api/recetas'
import { categoriasApi } from '../api/categorias'
import { uploadsApi } from '../api/uploads'
import { useToast } from '../composables/useToast'
import { imagenSrc } from '../utils/formato'

const props = defineProps({ id: { type: String, default: null } })
const router = useRouter()
const toast = useToast()

const editando = computed(() => props.id !== null)
const categorias = ref([])
const guardando = ref(false)
const subiendo = ref(false)
const preview = computed(() => imagenSrc(form.value.imagenUrl))

// sube el archivo elegido y guarda su url en el formulario
async function onArchivo(e) {
  const archivo = e.target.files[0]
  if (!archivo) return
  if (archivo.size > 5 * 1024 * 1024) {
    toast.error('La imagen no puede superar 5MB')
    return
  }
  subiendo.value = true
  try {
    form.value.imagenUrl = await uploadsApi.subir(archivo)
    toast.exito('Imagen subida')
  } catch {
    toast.error('No se pudo subir la imagen')
  } finally {
    subiendo.value = false
  }
}

const form = ref({
  nombre: '', descripcion: '', instrucciones: '',
  tiempoPreparacion: null, dificultad: 'FACIL', porciones: null,
  imagenUrl: '', categoriaId: null,
  ingredientes: [{ nombre: '', cantidad: '', unidad: '' }]
})

onMounted(async () => {
  categorias.value = await categoriasApi.listar()
  if (!editando.value && categorias.value.length) {
    form.value.categoriaId = categorias.value[0].id
  }
  if (editando.value) {
    const r = await recetasApi.obtener(props.id)
    form.value = {
      nombre: r.nombre, descripcion: r.descripcion || '', instrucciones: r.instrucciones || '',
      tiempoPreparacion: r.tiempoPreparacion, dificultad: r.dificultad, porciones: r.porciones,
      imagenUrl: r.imagenUrl || '', categoriaId: r.categoria?.id,
      ingredientes: r.ingredientes.length
        ? r.ingredientes.map((i) => ({ nombre: i.nombre, cantidad: i.cantidad, unidad: i.unidad }))
        : [{ nombre: '', cantidad: '', unidad: '' }]
    }
  }
})

function agregarIngrediente() {
  form.value.ingredientes.push({ nombre: '', cantidad: '', unidad: '' })
}
function quitarIngrediente(i) {
  form.value.ingredientes.splice(i, 1)
}

async function guardar() {
  guardando.value = true
  try {
    // quita ingredientes vacios
    const payload = {
      ...form.value,
      ingredientes: form.value.ingredientes.filter((i) => i.nombre.trim())
    }
    if (editando.value) {
      await recetasApi.actualizar(props.id, payload)
      toast.exito('Receta actualizada')
    } else {
      const creada = await recetasApi.crear(payload)
      toast.exito('Receta creada')
      return router.push(`/recetas/${creada.id}`)
    }
    router.push(`/recetas/${props.id}`)
  } catch (e) {
    toast.error(e?.response?.data?.mensaje || 'No se pudo guardar la receta')
  } finally {
    guardando.value = false
  }
}
</script>

<template>
  <section class="contenedor form-page">
    <RouterLink to="/" class="volver">← Cancelar</RouterLink>
    <h1>{{ editando ? 'Editar receta' : 'Nueva receta' }}</h1>

    <form @submit.prevent="guardar" class="formulario card">
      <div class="campo">
        <label>Nombre *</label>
        <input v-model="form.nombre" required placeholder="Ej: Lasaña boloñesa" />
      </div>

      <div class="campo">
        <label>Descripción</label>
        <input v-model="form.descripcion" placeholder="Una frase que abra el apetito" />
      </div>

      <div class="fila-3">
        <div class="campo">
          <label>Categoría *</label>
          <select v-model="form.categoriaId" required>
            <option v-for="c in categorias" :key="c.id" :value="c.id">{{ c.nombre }}</option>
          </select>
        </div>
        <div class="campo">
          <label>Dificultad *</label>
          <select v-model="form.dificultad" required>
            <option value="FACIL">Fácil</option>
            <option value="MEDIA">Media</option>
            <option value="DIFICIL">Difícil</option>
          </select>
        </div>
        <div class="campo">
          <label>Tiempo (min)</label>
          <input v-model.number="form.tiempoPreparacion" type="number" min="1" placeholder="30" />
        </div>
      </div>

      <div class="campo medio">
        <label>Porciones</label>
        <input v-model.number="form.porciones" type="number" min="1" placeholder="4" />
      </div>

      <div class="campo">
        <label>Imagen</label>
        <div class="imagen-bloque">
          <div class="preview" :class="{ vacio: !form.imagenUrl }">
            <img v-if="form.imagenUrl" :src="preview" alt="Vista previa" />
            <span v-else>📷</span>
          </div>
          <div class="imagen-acciones">
            <label class="btn btn-fantasma subir">
              {{ subiendo ? 'Subiendo...' : 'Subir imagen' }}
              <input type="file" accept="image/*" @change="onArchivo" hidden :disabled="subiendo" />
            </label>
            <span class="o-sep">o pega una URL</span>
            <input v-model="form.imagenUrl" placeholder="https://..." />
          </div>
        </div>
      </div>

      <div class="campo">
        <label>Instrucciones</label>
        <textarea v-model="form.instrucciones" rows="5" placeholder="Paso a paso de la preparación..."></textarea>
      </div>

      <!-- ingredientes -->
      <div class="ingredientes-bloque">
        <div class="between">
          <label>Ingredientes</label>
          <button type="button" class="btn btn-fantasma mini" @click="agregarIngrediente">+ Añadir</button>
        </div>
        <div v-for="(ing, i) in form.ingredientes" :key="i" class="ingrediente-fila">
          <input v-model="ing.nombre" placeholder="Ingrediente" />
          <input v-model="ing.cantidad" placeholder="Cant." class="corto" />
          <input v-model="ing.unidad" placeholder="Unidad" class="corto" />
          <button type="button" class="quitar" @click="quitarIngrediente(i)" title="Quitar">✕</button>
        </div>
      </div>

      <button class="btn btn-primario btn-bloque guardar" :disabled="guardando">
        {{ guardando ? 'Guardando...' : (editando ? 'Guardar cambios' : 'Crear receta') }}
      </button>
    </form>
  </section>
</template>

<style scoped>
.form-page { max-width: 760px; padding: 2rem 0 1rem; }
.volver { font-weight: 600; color: var(--carbon-suave); font-size: 0.9rem; }
.volver:hover { color: var(--terracota); }
.form-page h1 { font-size: 2.4rem; margin: 0.8rem 0 1.4rem; }

.formulario { padding: 2rem; display: flex; flex-direction: column; gap: 1.2rem; }
.fila-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 1.2rem; }
.fila-3 { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 1.2rem; }
.medio { max-width: 220px; }

.imagen-bloque { display: flex; gap: 1rem; align-items: stretch; }
.preview {
  width: 120px; height: 120px; flex-shrink: 0; border-radius: var(--radio-sm);
  overflow: hidden; border: 1.5px solid var(--borde-fuerte); background: var(--crema);
  display: grid; place-items: center;
}
.preview img { width: 100%; height: 100%; object-fit: cover; }
.preview.vacio span { font-size: 2rem; opacity: 0.5; }
.imagen-acciones { display: flex; flex-direction: column; gap: 0.6rem; justify-content: center; flex: 1; }
.subir { align-self: flex-start; cursor: pointer; }
.o-sep { font-size: 0.8rem; color: var(--carbon-suave); }

.ingredientes-bloque { border-top: 1px solid var(--borde); padding-top: 1.2rem; display: flex; flex-direction: column; gap: 0.7rem; }
.ingrediente-fila { display: grid; grid-template-columns: 1fr 90px 90px 38px; gap: 0.5rem; align-items: center; }
.corto { text-align: center; }
.quitar {
  width: 38px; height: 38px; border-radius: var(--radio-sm);
  background: #fbe9e3; color: var(--terracota-osc); font-weight: 700; transition: background 0.15s;
}
.quitar:hover { background: #f6d6cc; }
.mini { padding: 0.4rem 0.9rem; font-size: 0.82rem; }
.guardar { margin-top: 0.6rem; height: 50px; }

@media (max-width: 640px) {
  .fila-2, .fila-3 { grid-template-columns: 1fr; }
  .ingrediente-fila { grid-template-columns: 1fr 1fr; }
}
</style>
