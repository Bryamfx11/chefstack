import http from './http'

export const uploadsApi = {
  // sube un archivo de imagen y devuelve su url
  subir: (archivo) => {
    const form = new FormData()
    form.append('archivo', archivo)
    return http.post('/api/uploads', form, { headers: { 'Content-Type': 'multipart/form-data' } })
      .then((r) => r.data.url)
  }
}
