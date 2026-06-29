import http from './http'

export const categoriasApi = {
  listar: () => http.get('/api/categorias').then((r) => r.data),
  obtener: (id) => http.get(`/api/categorias/${id}`).then((r) => r.data),
  crear: (datos) => http.post('/api/categorias', datos).then((r) => r.data),
  actualizar: (id, datos) => http.put(`/api/categorias/${id}`, datos).then((r) => r.data),
  eliminar: (id) => http.delete(`/api/categorias/${id}`)
}
