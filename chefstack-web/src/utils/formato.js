// dificultad -> etiqueta y estilo
export function dificultadInfo(dificultad) {
  switch (dificultad) {
    case 'FACIL': return { texto: 'Fácil', clase: 'chip-hierba' }
    case 'MEDIA': return { texto: 'Media', clase: 'chip-ambar' }
    case 'DIFICIL': return { texto: 'Difícil', clase: 'chip-terracota' }
    default: return { texto: '—', clase: '' }
  }
}

// imagen por defecto
export const IMAGEN_POR_DEFECTO =
  'https://images.unsplash.com/photo-1495521821757-a1efb6729352?w=800&q=80'

// las imagenes subidas vienen como ruta relativa (/api/uploads/N) -> se resuelven contra el backend
export function imagenSrc(url) {
  if (!url) return IMAGEN_POR_DEFECTO
  if (url.startsWith('http') || url.startsWith('data:')) return url
  const base = import.meta.env.VITE_API_URL || 'http://localhost:8080'
  return base + url
}
