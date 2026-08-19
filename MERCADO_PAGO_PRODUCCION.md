# Mercado Pago - paso a producción

El proyecto queda en modo de prueba por defecto. Para habilitar cobros reales configura las variables de entorno del backend:

- `MERCADOPAGO_ACCESS_TOKEN`: Access Token de PRODUCCIÓN de tu aplicación de Mercado Pago.
- `MERCADOPAGO_MODE=production`: hace que Checkout Pro use `init_point` real en lugar del enlace sandbox.
- `MERCADOPAGO_FRONTEND_URL`: URL pública HTTPS del frontend, sin slash final.
- `MERCADOPAGO_NOTIFICATION_URL`: URL pública HTTPS del backend terminada en `/api/pedidos/mercadopago/webhook`.
- `MERCADOPAGO_WEBHOOK_SECRET`: firma secreta configurada en Webhooks de tu aplicación de Mercado Pago.

No guardes ninguna de estas credenciales en Angular, GitHub ni archivos públicos.

## Flujo implementado

1. Home Style crea un pedido `PENDIENTE` y una preferencia Checkout Pro.
2. Mercado Pago recibe el pago.
3. El comprador vuelve a `/pago/resultado`.
4. El frontend pide al backend sincronizar el `payment_id` contra la API de Mercado Pago.
5. El webhook también recibe cambios de estado y valida su firma cuando se configura el secret.
6. Solo un pago consultado como `approved` y cuyo monto coincide con el pedido cambia el pedido a `PAGADO` y descuenta inventario.

Antes de usar dinero real realiza una compra completa en pruebas y configura el evento de pagos en Webhooks.
