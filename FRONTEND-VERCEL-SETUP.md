# 🚀 Configuración del Frontend en Vercel

## Backend URL disponible
✅ **Backend funcionando en:** `https://pruebaback-production-29ae.up.railway.app`

## 📝 Pasos para configurar el frontend en Vercel

### 1. Variables de entorno en Vercel
Agregar estas variables en el panel de Vercel (Settings > Environment Variables):

```
VITE_BACKEND_URL=https://pruebaback-production-29ae.up.railway.app
VITE_CLERK_PUBLISHABLE_KEY=pk_test_... (tu key de Clerk)
```

### 2. Verificar configuración en el código del frontend
El archivo de configuración del frontend debería usar estas variables:

```javascript
// src/config/api.js
const API_BASE_URL = import.meta.env.VITE_BACKEND_URL || 'http://localhost:8080';

export const apiConfig = {
  baseURL: API_BASE_URL,
  endpoints: {
    auth: '/api/auth',
    products: '/api/products',
    orders: '/api/orders',
    payments: '/api/payments'
  }
};
```

### 3. Endpoints del backend disponibles
- 🔐 **Auth:** `https://pruebaback-production-29ae.up.railway.app/api/auth/`
  - POST `/register` - Registro de usuarios
  - POST `/login` - Login de usuarios
  
- 🛍️ **Products:** `https://pruebaback-production-29ae.up.railway.app/api/products/`
  - GET `/` - Listar productos
  - POST `/` - Crear producto
  - GET `/{id}` - Obtener producto por ID
  
- 📦 **Orders:** `https://pruebaback-production-29ae.up.railway.app/api/orders/`
  - GET `/` - Listar órdenes del usuario
  - POST `/` - Crear nueva orden
  
- 💳 **Payments:** `https://pruebaback-production-29ae.up.railway.app/api/payments/`
  - POST `/create-payment-intent` - Crear intención de pago con Stripe

### 4. CORS configurado para
- ✅ `https://florerifront.vercel.app`
- ✅ `https://florerifront-git-main-matias-projects-a4daceed.vercel.app`
- ✅ `https://florerifront-q9wylpya-matias-projects-a4daceed.vercel.app`
- ✅ `http://localhost:3000` (desarrollo)
- ✅ `http://localhost:5173` (desarrollo)

### 5. Verificar conexión
Una vez configurado, probar estos endpoints desde el frontend:

1. **Health check:** GET `https://pruebaback-production-29ae.up.railway.app/actuator/health`
2. **Test auth:** POST `https://pruebaback-production-29ae.up.railway.app/api/auth/register`

### 6. Comandos para redesplegar en Vercel
```bash
# Si usas Vercel CLI
vercel --prod

# O hacer push a main branch si está conectado con Git
git push origin main
```

## 🎯 Estado actual del stack completo
- ✅ **Backend:** Railway - `https://pruebaback-production-29ae.up.railway.app`
- ✅ **Database:** H2 (temporal) - Funcionando
- ✅ **CORS:** Configurado para Vercel
- 🔄 **Frontend:** Pendiente configurar variable VITE_BACKEND_URL
- 🔄 **Clerk Auth:** Pendiente verificar configuración

## 🚀 ¡Listo para conectar!
Una vez que configures `VITE_BACKEND_URL` en Vercel, tu aplicación debería estar completamente funcional.
