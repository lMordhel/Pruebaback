# 🚀 CONFIGURACIÓN PARA VERCEL - FRONTEND VITE

## 📋 Variables de entorno que debes configurar en Vercel:

1. Ve a tu proyecto en Vercel → Settings → Environment Variables
2. Agrega estas variables:

### Variables obligatorias:
```
VITE_BACKEND_URL = https://[tu-proyecto].railway.app
VITE_CLERK_PUBLISHABLE_KEY = pk_test_YXdha2UtdGVycmllci00LmNsZXJrLmFjY291bnRzLmRldiQ
VITE_FRONTEND_URL = https://florerifront.vercel.app
```

### Variables opcionales (si usas Stripe en frontend):
```
VITE_STRIPE_PUBLIC_KEY = pk_test_[tu_clave_publica]
```

## 🔧 Pasos para deployment:

1. **En Railway** (cuando esté listo):
   - Copia la URL de tu aplicación
   - Ejemplo: `https://floreria-production-xxxx.railway.app`

2. **En Vercel**:
   - Actualiza `VITE_BACKEND_URL` con la URL de Railway
   - Redeploy tu aplicación

3. **Verificar CORS**:
   - El backend ya está configurado para aceptar conexiones desde Vercel
   - URLs permitidas: `https://florerifront.vercel.app`

## 📡 Endpoints principales del backend:

- Health Check: `GET /actuator/health`
- API Base: `/api/v1/`
- Productos: `/api/v1/products`
- Auth: `/api/v1/auth`
- Pagos: `/api/v1/payments`

## 🐛 Troubleshooting:

Si tienes errores de CORS:
1. Verifica que la URL en `VITE_BACKEND_URL` sea correcta
2. Asegúrate de incluir `https://` en la URL
3. Revisa que no haya espacios extra en las variables
