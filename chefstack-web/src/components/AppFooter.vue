<script setup>
const anio = new Date().getFullYear()
</script>

<template>
  <footer class="pie">
    <div class="contenedor pie-inner">
      <div class="pie-marca">
        <span class="marca-texto">Chef<span class="acento">Stack</span></span>
        <p class="muted">Tu recetario digital. Cocina, guarda y comparte.</p>
      </div>
      <p class="muted pequeno">© {{ anio }} ChefStack · Hecho con cariño en la cocina</p>
    </div>
  </footer>
</template>

<style scoped>
.pie { margin-top: 3rem; border-top: 1px solid var(--borde); background: var(--crema-2); }
.pie-inner { display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 1rem; padding: 2rem 0; }
.marca-texto { font-family: var(--display); font-weight: 700; font-size: 1.3rem; }
.acento { color: var(--terracota); }
.pie-marca p { margin-top: 0.2rem; font-size: 0.9rem; }
.pequeno { font-size: 0.82rem; }
</style>
