# 🚀 GUÍA FÁCIL: Despliegue en la Nube para Presentación

## ⚡ OPCIÓN SÚPER FÁCIL (5-10 minutos total)

### 🔥 Backend: **Railway** (Gratuito)
### 🔥 Frontend: **Vercel** (Gratuito)

---

## 📝 PASO A PASO - BACKEND (Railway)

### 1. **Preparar el proyecto**
```bash
# Tu proyecto ya está listo para Railway 👍
# Solo necesitas hacer commit y push a GitHub
```

### 2. **Deploy en Railway** (2 minutos)
1. Ve a: https://railway.app
2. Clic en **"Start a New Project"**
3. Conecta tu cuenta de GitHub
4. Selecciona tu repositorio `floreria`
5. Railway detectará automáticamente que es Spring Boot
6. Clic en **"Deploy"**

### 3. **Configurar variables de entorno** (1 minuto)
En el dashboard de Railway, ve a **Variables** y agrega:
```
SPRING_PROFILES_ACTIVE=railway
DATABASE_URL=postgresql://... (Railway lo genera automáticamente)
FRONTEND_URL=https://tu-app.vercel.app
```

### 4. **¡LISTO!** 🎉
- Tu API estará en: `https://tu-proyecto.up.railway.app`
- Swagger UI: `https://tu-proyecto.up.railway.app/swagger-ui/index.html`

---

## 📝 PASO A PASO - FRONTEND (Vercel)

### 1. **En tu proyecto React**, crear archivo `.env.production`:
```env
REACT_APP_API_URL=https://tu-proyecto.up.railway.app
REACT_APP_STRIPE_PUBLIC_KEY=pk_test_51RDt1pCLhDQ5B8SlsQzug5nJnuEdQyvU2z2S3iHBwIny4bhdVRJpWXOMulobqNDw4sJ81h5KHfLZ0dXcNrzQLJ4600TBCDbp3T
```

### 2. **Deploy en Vercel** (2 minutos)
1. Ve a: https://vercel.com
2. Clic en **"Import Project"**
3. Conecta GitHub y selecciona tu repo de React
4. Vercel detecta automáticamente que es React
5. Clic en **"Deploy"**

### 3. **¡LISTO!** 🎉
- Tu frontend estará en: `https://tu-app.vercel.app`

---

## 🌟 ALTERNATIVAS AÚN MÁS FÁCILES

### **Opción 1: Netlify (Frontend)**
- Arrastra tu carpeta `build/` a https://netlify.com
- ¡Deploy instantáneo!

### **Opción 2: Render (Backend)**
- Similar a Railway pero con más configuración manual
- También gratis para proyectos pequeños

### **Opción 3: Heroku (Ambos)**
- Más conocido pero requiere tarjeta de crédito
- Tiene plan gratuito limitado

---

## 📋 URLs para tu Presentación

Después del deploy tendrás:

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **Frontend** | `https://tu-app.vercel.app` | Tu aplicación React |
| **Backend API** | `https://tu-proyecto.up.railway.app` | API REST |
| **Swagger UI** | `https://tu-proyecto.up.railway.app/swagger-ui` | Documentación API |
| **Base de Datos** | Automática en Railway | PostgreSQL en la nube |

---

## 🎯 VENTAJAS de esta configuración:

✅ **100% Gratuito** para presentaciones
✅ **Deploy automático** desde GitHub
✅ **URLs reales** que puedes mostrar
✅ **Base de datos persistente** en la nube
✅ **Configuración en 5-10 minutos**
✅ **Escalable** si necesitas más recursos

---

## 🚨 TIPS para la Presentación:

1. **Haz el deploy 1 día antes** para probar todo
2. **Guarda las URLs** en un documento
3. **Testa el flujo completo** frontend → backend → BD
4. **Prepara datos de prueba** que se vean bien
5. **Ten un backup local** por si algo falla

---

## 🆘 ¿Problemas?

### Backend no inicia:
- Revisa logs en Railway dashboard
- Verifica variables de entorno

### Frontend no conecta:
- Confirma REACT_APP_API_URL en Vercel
- Verifica CORS en el backend

### Base de datos:
- Railway maneja PostgreSQL automáticamente
- No necesitas configurar nada manualmente

---

**¡Con esto impresionarás a tus profesores! 🚀**

**Tiempo total estimado: 10 minutos** ⏱️
