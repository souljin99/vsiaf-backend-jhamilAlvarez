# vsiaf-reportes (Generar Reportes)

Variables de entorno: DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD, CORS_ORIGINS (opcional).
Usa tablas con prefijo rep_ (no choca con otras tablas de la misma base).
La primera vez carga datos de ejemplo (DataSeeder).

Endpoints (todos GET, filtros opcionales: codOfic, codGrupo, codResp, estado, desde, hasta, corte = yyyy-MM-dd):
- /api/reportes/filtros
- /api/reportes/general
- /api/reportes/por-grupo
- /api/reportes/por-oficina
- /api/reportes/por-responsable
- /api/reportes/resumen
- /api/reportes/general/csv
Swagger: /swagger-ui/index.html
