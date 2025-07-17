# 🌸 Florería App - Deployment Guide

## 🚀 Backend (Railway)

### Variables de Entorno Requeridas:
```bash
JWT_SECRET=tu_jwt_secret_muy_seguro_de_al_menos_32_caracteres
STRIPE_SECRET_KEY=sk_test_tu_clave_secreta_stripe
STRIPE_PUBLIC_KEY=pk_test_tu_clave_publica_stripe
FRONTEND_URL=https://florerifront.vercel.app
```

### Pasos para desplegar:
1. Conecta tu repositorio de GitHub a Railway
2. Railway detectará automáticamente el Dockerfile
3. Configura las variables de entorno en el panel de Railway
4. Si usas PostgreSQL, Railway configurará automáticamente DATABASE_URL

### Health Check:
- Endpoint: `/actuator/health`
- El sistema está configurado para health checks automáticos

---

## 🌐 Frontend (Vercel)

### Variables de Entorno Requeridas:
```bash
NEXT_PUBLIC_API_URL=https://tu-proyecto.railway.app
NEXT_PUBLIC_STRIPE_PUBLIC_KEY=pk_test_tu_clave_publica_stripe
```

### Pasos para desplegar:
1. Conecta tu repositorio de frontend a Vercel
2. Configura las variables de entorno en Vercel
3. Actualiza NEXT_PUBLIC_API_URL con la URL real de Railway

---

## 🔗 Conexión Frontend-Backend

El CORS ya está configurado para permitir:
- `https://florerifront.vercel.app`
- `https://florerifront-git-main-matias-projects-a4daceed.vercel.app`
- `https://florerifront-q9wylpya-matias-projects-a4daceed.vercel.app`

---

## 📝 Notas Importantes

1. **Seguridad**: Todos los secretos están usando variables de entorno
2. **Base de Datos**: PostgreSQL configurado para producción
3. **Stripe**: Configurado para test/producción según las claves
4. **CORS**: Configurado para todos los dominios de Vercel
