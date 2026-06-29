import http from './http'

export const recetasApi = {
  listar: (params = {}) => http.get('/api/recetas', { params }).then((r) => r.data),
  obtener: (id) => http.get(`/api/recetas/${id}`).then((r) => r.data),
  crear: (datos) => http.post('/api/recetas', datos).then((r) => r.data),
  actualizar: (id, datos) => http.put(`/api/recetas/${id}`, datos).then((r) => r.data),
  eliminar: (id) => http.delete(`/api/recetas/${id}`)
}
