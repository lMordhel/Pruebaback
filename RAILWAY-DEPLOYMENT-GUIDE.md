# 🚀 CONFIGURACIÓN PARA RAILWAY - BACKEND SPRING BOOT

## 📋 Variables de entorno que debes configurar en Railway:

Ve a tu proyecto en Railway → Variables tab → Add las siguientes:

### Variables obligatorias:
```
SPRING_PROFILES_ACTIVE = railway
PORT = 8080
```

### Variables de base de datos (Railway las configura automáticamente si tienes PostgreSQL):
```
DATABASE_URL = [Railway la configura automáticamente]
DATABASE_USERNAME = [Railway la configura automáticamente]  
DATABASE_PASSWORD = [Railway la configura automáticamente]
DATABASE_DRIVER = org.postgresql.Driver
DATABASE_PLATFORM = org.hibernate.dialect.PostgreSQLDialect
```

### Variables de seguridad:
```
JWT_SECRET = [genera_una_clave_secreta_jwt_de_32_caracteres]
```

### Variables de Stripe:
```
STRIPE_SECRET_KEY = sk_test_[tu_clave_secreta]
STRIPE_PUBLIC_KEY = pk_test_[tu_clave_publica]
```

### Variables de frontend:
```
FRONTEND_URL = https://florerifront.vercel.app
```

## 🔍 Verificar deployment:

1. **Health Check**: `https://[tu-url].railway.app/actuator/health`
2. **API Docs**: `https://[tu-url].railway.app/swagger-ui.html`

## 📡 URLs importantes:
- Swagger UI: `/swagger-ui.html`
- Health Check: `/actuator/health`
- API Base: `/api/v1/`
