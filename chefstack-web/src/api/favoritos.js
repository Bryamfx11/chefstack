import http from './http'

export const favoritosApi = {
  listar: () => http.get('/api/favoritos').then((r) => r.data),
  agregar: (recetaId) => http.post(`/api/favoritos/${recetaId}`).then((r) => r.data),
  eliminar: (recetaId) => http.delete(`/api/favoritos/${recetaId}`)
}
