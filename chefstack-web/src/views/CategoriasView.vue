<script setup>
import { ref, onMounted } from 'vue'
import { categoriasApi } from '../api/categorias'
import { useToast } from '../composables/useToast'
import { useConfirm } from '../composables/useConfirm'

const toast = useToast()
const { confirmar } = useConfirm()
const categorias = ref([])
const cargando = ref(true)

// si editandoId tiene valor, estamos editando
const form = ref({ nombre: '', descripcion: '' })
const editandoId = ref(null)

async function cargar() {
  cargando.value = true
  categorias.value = await categoriasApi.listar()
  cargando.value = false
}
onMounted(cargar)

function editar(c) {
  editandoId.value = c.id
  form.value = { nombre: c.nombre, descripcion: c.descripcion || '' }
  window.scrollTo({ top: 0, behavior: 'smooth' })
}
function limpiar() {
  editandoId.value = null
  form.value = { nombre: '', descripcion: '' }
}

async function guardar() {
  try {
    if (editandoId.value) {
      await categoriasApi.actualizar(editandoId.value, form.value)
      toast.exito('Categoría actualizada')
    } else {
      await categoriasApi.crear(form.value)
      toast.exito('Categoría creada')
    }
    limpiar()
    cargar()
  } catch (e) {
    toast.error(e?.response?.data?.mensaje || 'No se pudo guardar')
  }
}

async function eliminar(c) {
  const ok = await confirmar({
    titulo: 'Eliminar categoría',
    mensaje: `¿Eliminar la categoría "${c.nombre}"?`,
    accion: 'Eliminar', peligro: true
  })
  if (!ok) return
  try {
    await categoriasApi.eliminar(c.id)
    toast.exito('Categoría eliminada')
    cargar()
  } catch (e) {
    toast.error(e?.response?.data?.mensaje || 'No se pudo eliminar')
  }
}
</script>

<template>
  <section class="contenedor cat-page">
    <h1>Categorías</h1>
    <p class="muted">Organiza tus recetas en categorías.</p>

    <div class="layout">
      <!-- formulario -->
      <form @submit.prevent="guardar" class="card form">
        <h3>{{ editandoId ? 'Editar categoría' : 'Nueva categoría' }}</h3>
        <div class="campo">
          <label>Nombre *</label>
          <input v-model="form.nombre" required placeholder="Ej: Sopas" />
        </div>
        <div class="campo">
          <label>Descripción</label>
          <input v-model="form.descripcion" placeholder="Opcional" />
        </div>
        <div class="botones">
          <button class="btn btn-primario btn-bloque">{{ editandoId ? 'Guardar' : 'Crear' }}</button>
          <button v-if="editandoId" type="button" class="btn btn-fantasma" @click="limpiar">Cancelar</button>
        </div>
      </form>

      <!-- listado -->
      <div class="lista">
        <div v-if="cargando" class="spinner"></div>
        <div v-else-if="categorias.length" class="cat-items">
          <div v-for="c in categorias" :key="c.id" class="cat-item card">
            <div>
              <h4>{{ c.nombre }}</h4>
              <p class="muted">{{ c.descripcion || 'Sin descripción' }}</p>
              <span class="chip">{{ c.cantidadRecetas }} receta(s)</span>
            </div>
            <div class="cat-acciones">
              <button class="icono" @click="editar(c)" title="Editar">✎</button>
              <button class="icono peligro" @click="eliminar(c)" title="Eliminar">🗑</button>
            </div>
          </div>
        </div>
        <p v-else class="muted">Aún no hay categorías. Crea la primera.</p>
      </div>
    </div>
  </section>
</template>

<style scoped>
.cat-page { padding: 2.2rem 0; }
.cat-page h1 { font-size: 2.6rem; }
.layout { display: grid; grid-template-columns: 340px 1fr; gap: 1.8rem; margin-top: 1.6rem; align-items: start; }
.form { padding: 1.6rem; display: flex; flex-direction: column; gap: 1rem; position: sticky; top: 90px; }
.form h3 { font-size: 1.3rem; }
.botones { display: flex; gap: 0.6rem; }

.cat-items { display: flex; flex-direction: column; gap: 0.9rem; }
.cat-item { padding: 1.2rem 1.4rem; display: flex; justify-content: space-between; align-items: center; gap: 1rem; }
.cat-item h4 { font-size: 1.2rem; margin-bottom: 0.2rem; }
.cat-item p { font-size: 0.9rem; margin-bottom: 0.5rem; }
.cat-acciones { display: flex; gap: 0.4rem; }
.icono { width: 40px; height: 40px; border-radius: var(--radio-sm); background: var(--crema-2); font-size: 1rem; transition: background 0.15s; }
.icono:hover { background: var(--borde); }
.icono.peligro:hover { background: #fbe9e3; }

@media (max-width: 800px) { .layout { grid-template-columns: 1fr; } .form { position: static; } }
</style>
