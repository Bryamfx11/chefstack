import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

// requiereAuth = solo con sesion; soloInvitados = solo sin sesion
const routes = [
  { path: '/', name: 'inicio', component: () => import('../views/HomeView.vue') },
  { path: '/recetas/:id', name: 'detalle', component: () => import('../views/RecetaDetalleView.vue'), props: true },
  { path: '/login', name: 'login', component: () => import('../views/LoginView.vue'), meta: { soloInvitados: true } },
  { path: '/registro', name: 'registro', component: () => import('../views/RegistroView.vue'), meta: { soloInvitados: true } },
  { path: '/categorias', name: 'categorias', component: () => import('../views/CategoriasView.vue'), meta: { requiereAuth: true } },
  { path: '/favoritos', name: 'favoritos', component: () => import('../views/FavoritosView.vue'), meta: { requiereAuth: true } },
  { path: '/recetas/nueva', name: 'receta-nueva', component: () => import('../views/RecetaFormView.vue'), meta: { requiereAuth: true } },
  { path: '/recetas/:id/editar', name: 'receta-editar', component: () => import('../views/RecetaFormView.vue'), props: true, meta: { requiereAuth: true } },
  { path: '/:pathMatch(.*)*', redirect: '/' }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior: () => ({ top: 0 })
})

// protege rutas privadas; espera a saber si hay sesion
router.beforeEach(async (to) => {
  const auth = useAuthStore()
  await auth.listo
  if (to.meta.requiereAuth && !auth.autenticado) {
    return { name: 'login', query: { redirigir: to.fullPath } }
  }
  if (to.meta.soloInvitados && auth.autenticado) {
    return { name: 'inicio' }
  }
  return true
})

export default router
