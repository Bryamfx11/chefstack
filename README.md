# 🍴 ChefStack

Aplicación full-stack de gestión de recetas de cocina. Permite explorar, crear, editar y
organizar recetas con sus ingredientes, categorías, tiempos y calorías, con autenticación de
usuarios y recetas favoritas por persona.

| Capa | Tecnología |
|------|------------|
| Backend | Spring Boot 3 · Java 21 · Maven · Spring Data JPA · MySQL |
| Frontend | Vue 3 (Composition API) · Vite · Pinia · Vue Router · Axios |
| Autenticación | Firebase Authentication (email/contraseña + Google) |
| Nutrición | API externa Spoonacular (calorías) |

---

## 📁 Estructura del proyecto

```
chefstack/
├── chefstack-api/        # Backend REST (Spring Boot)
│   └── src/main/java/com/chefstack/api/
│       ├── model/        # entidades JPA
│       ├── dto/          # objetos de transferencia
│       ├── mapper/       # conversión entidad ⇄ DTO
│       ├── repository/   # acceso a datos (Spring Data)
│       ├── service/      # lógica de negocio (interfaz + impl)
│       ├── controller/   # endpoints REST
│       ├── security/     # validación de token Firebase
│       ├── client/       # cliente de Spoonacular
│       ├── exception/    # manejo global de errores
│       └── config/       # CORS, OpenAPI, seeder, etc.
├── chefstack-web/        # Frontend SPA (Vue 3)
│   └── src/
│       ├── api/          # llamadas a la API (axios)
│       ├── firebase/     # configuración de auth
│       ├── stores/       # estado global (Pinia)
│       ├── router/       # rutas y guards
│       ├── views/        # páginas
│       └── components/   # componentes reutilizables
└── docs/                 # documentación (PDF) y guías
```

---

## 🚀 Cómo ejecutar en local

### Requisitos
- Java 21, Maven 3.9+
- Node 20+
- MySQL 8 corriendo en local

### 1. Base de datos
Crea la base de datos (las tablas se generan solas al arrancar):
```sql
CREATE DATABASE chefstack_db;
```

### 2. Backend
```bash
cd chefstack-api
# copia la plantilla de secretos y pon tu API key de Spoonacular
cp config/secrets.properties.example config/secrets.properties
# ajusta usuario/clave de MySQL si hace falta (por defecto root sin clave)
mvn spring-boot:run
```
La API queda en `http://localhost:8080` y la documentación Swagger en
`http://localhost:8080/swagger-ui.html`.

### 3. Frontend
```bash
cd chefstack-web
npm install
npm run dev
```
La app queda en `http://localhost:5173`.

---

## 🔐 Variables y secretos

| Dónde | Variable | Para qué |
|-------|----------|----------|
| `chefstack-api/config/secrets.properties` | `spoonacular.api-key` | API de nutrición |
| Backend (env, prod) | `SPRING_DATASOURCE_URL/USERNAME/PASSWORD` | conexión MySQL |
| Frontend (`.env`) | `VITE_API_URL` | URL del backend |

> Los secretos reales nunca se suben al repositorio (ver `.gitignore`).

---

## 📚 API

Todos los endpoints están documentados en Swagger (`/swagger-ui.html`).

- `GET/POST/PUT/DELETE /api/categorias`
- `GET/POST/PUT/DELETE /api/recetas` — admite `?categoriaId=` y `?buscar=`
- `GET/POST/PUT/DELETE /api/ingredientes`
- `GET/POST/DELETE /api/favoritos` — del usuario autenticado
- `POST /api/uploads` — subir imagen

La lectura (GET) es pública; crear/editar/eliminar requiere iniciar sesión.

---

## 🧪 Pruebas

El backend incluye pruebas de integración (JUnit + MockMvc). Para correrlas:
```bash
cd chefstack-api && mvn test
```

---

## ☁️ Despliegue

- **Backend + MySQL**: Railway
- **Frontend**: Vercel

La integración continua (GitHub Actions) compila backend y frontend en cada push.
Los detalles del despliegue están en `docs/`.
