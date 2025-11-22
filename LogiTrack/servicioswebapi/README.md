# LogiTrack Distribution - API REST

### Configuración de Base de Datos

La configuración se encuentra en `src/main/resources/config/.env`:

```properties
DB_DRIVER=org.postgresql.Driver
DB_URL=jdbc:postgresql://localhost:5432/serviciosweb
DB_USER=postgres
DB_PASSWORD=admin123

HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect
HIBERNATE_DDL=update
HIBERNATE_SHOW_SQL=true
HIBERNATE_FORMAT_SQL=true
```

### Compilar y Desplegar

```bash
# Compilar el proyecto
mvn clean package

# Desplegar en WildFly
mvn wildfly:deploy

# Redesplegar (si ya está desplegado)
mvn wildfly:redeploy
```

### URL Base de la API

```
http://localhost:8080/servicioswebapi/api
```

## Entidades del Sistema

### Customer (Cliente)
Clientes pequeños o medianos que realizan pedidos a LogiTrack.

**Atributos:**
- `customerId` - Identificador interno (generado automáticamente)
- `fullName` - Nombre del cliente o comercio
- `taxId` - NIT o número fiscal (único, requerido)
- `email` - Correo de contacto
- `address` - Dirección física
- `active` - Indica si el cliente sigue activo (true/false)

### Product (Producto)
Productos ofrecidos por la empresa.

**Atributos:**
- `productId` - Identificador del producto (generado automáticamente)
- `name` - Nombre del producto
- `description` - Descripción breve
- `price` - Precio unitario
- `category` - Categoría del producto (ej: "Beverages", "Snacks")
- `active` - Indica si el producto está habilitado para venta

### Order (Orden)
Representa una orden de compra realizada por un cliente.

**Atributos:**
- `orderId` - Identificador de la orden (generado automáticamente)
- `customerId` - Referencia al cliente que realizó la orden
- `orderDate` - Fecha en que se generó la orden
- `status` - Estado actual: `PENDING`, `PROCESSING`, `COMPLETED`, `CANCELLED`
- `totalAmount` - Total de la orden (calculado automáticamente con los items)

### OrderItem (Detalle de Orden)
Cada línea dentro de una orden, representa un producto y su cantidad.

**Atributos:**
- `orderItemId` - Identificador del item (generado automáticamente)
- `orderId` - ID de la orden asociada
- `productId` - ID del producto
- `quantity` - Cantidad solicitada
- `unitPrice` - Precio unitario (copiado desde Product.price al momento de la orden)
- `subtotal` - Resultado de quantity × unitPrice (calculado automáticamente)

### Payment (Pago)
Registra pagos realizados por los clientes respecto a sus órdenes.

**Atributos:**
- `paymentId` - Identificador del pago (generado automáticamente)
- `orderId` - Orden a la que aplica el pago
- `paymentDate` - Fecha del pago
- `amount` - Monto pagado
- `method` - Método de pago: `Cash`, `Card`, `Transfer`

## Reglas de Negocio

✅ Una Order no puede crearse sin un Customer válido  
✅ Un OrderItem no puede crearse sin un Product activo  
✅ El estado inicial de una orden siempre será `PENDING`  
✅ El `totalAmount` de la orden se calcula automáticamente a partir de sus items  
✅ Los `Payment.amount` no pueden exceder el total de la orden ni generar saldo negativo  
✅ Un cliente inactivo no puede crear nuevas órdenes  
✅ El `unitPrice` se copia del producto al momento de crear el OrderItem  
✅ El `subtotal` se calcula como quantity × unitPrice

## Endpoints de la API

### Gestión de Productos

#### 1. Listar todos los productos
```
GET /api/products
```

#### 2. Obtener producto por ID
```
GET /api/products/{id}
```

#### 3. Consultar productos por categoría
```
GET /api/products/category/{category}
```

#### 4. Crear producto
```
POST /api/products
Content-Type: application/json

{
  "name": "Coca Cola 2L",
  "description": "Bebida gaseosa",
  "price": 15.50,
  "category": "Beverages",
  "active": true
}
```

#### 5. Actualizar producto
```
PUT /api/products/{id}
Content-Type: application/json

{
  "name": "Coca Cola 2L",
  "description": "Bebida gaseosa refrescante",
  "price": 16.00,
  "category": "Beverages",
  "active": true
}
```

#### 6. Eliminar producto
```
DELETE /api/products/{id}
```

---

### Gestión de Clientes

#### 1. Listar todos los clientes
```
GET /api/customers
```

#### 2. Obtener cliente por ID
```
GET /api/customers/{id}
```

#### 3. Consultar cliente por NIT
```
GET /api/customers/taxid/{taxId}
```

#### 4. Registrar cliente
```
POST /api/customers
Content-Type: application/json

{
  "fullName": "Tienda El Ahorro",
  "taxId": "12345678-9",
  "email": "contacto@elahorro.com",
  "address": "Zona 10, Ciudad de Guatemala",
  "active": true
}
```

#### 5. Actualizar información del cliente
```
PUT /api/customers/{id}
Content-Type: application/json

{
  "fullName": "Tienda El Ahorro S.A.",
  "email": "ventas@elahorro.com",
  "address": "Zona 10, Ciudad de Guatemala",
  "active": true
}
```

#### 6. Desactivar cliente
```
PATCH /api/customers/{id}/deactivate
```

---

### Órdenes de Compra

#### 1. Listar todas las órdenes
```
GET /api/orders
```

#### 2. Obtener orden por ID
```
GET /api/orders/{id}
```

#### 3. Consultar órdenes por cliente
```
GET /api/orders/customer/{customerId}
```

#### 4. Consultar órdenes por estado
```
GET /api/orders/status/{status}
```
Estados válidos: `PENDING`, `PROCESSING`, `COMPLETED`, `CANCELLED`

#### 5. Crear orden
```
POST /api/orders
Content-Type: application/json

{
  "customerId": 1,
  "orderDate": "2024-11-20",
  "items": [
    {
      "productId": 1,
      "quantity": 10
    },
    {
      "productId": 2,
      "quantity": 5
    }
  ]
}
```

#### 6. Cambiar estado de una orden
```
PATCH /api/orders/{id}/status?status=PROCESSING
```

#### 7. Eliminar orden
```
DELETE /api/orders/{id}
```

---

### Pagos

#### 1. Obtener pago por ID
```
GET /api/payments/{id}
```

#### 2. Consultar pagos de una orden
```
GET /api/payments/order/{orderId}
```

#### 3. Registrar un pago
```
POST /api/payments
Content-Type: application/json

{
  "orderId": 1,
  "paymentDate": "2024-11-20",
  "amount": 155.00,
  "method": "Cash"
}
```

#### 4. Eliminar pago
```
DELETE /api/payments/{id}
```

---

## Ejemplo Completo de Flujo en Postman

### Paso 1: Crear Productos

**Método:** `POST`  
**URL:** `http://localhost:8080/servicioswebapi/api/products`  
**Headers:**
```
Content-Type: application/json
```
**Body (raw JSON):**
```json
{
  "name": "Coca Cola 2L",
  "description": "Bebida gaseosa",
  "price": 15.50,
  "category": "Beverages",
  "active": true
}
```
**Respuesta esperada:** `201 Created` con el producto creado incluyendo su `productId`

Repite para crear más productos:
```json
{
  "name": "Papas Fritas 200g",
  "description": "Snack salado",
  "price": 8.75,
  "category": "Snacks",
  "active": true
}
```

```json
{
  "name": "Agua Pura 1L",
  "description": "Agua purificada",
  "price": 5.00,
  "category": "Beverages",
  "active": true
}
```

---

### Paso 2: Listar Productos

**Método:** `GET`  
**URL:** `http://localhost:8080/servicioswebapi/api/products`  
**Headers:** Ninguno necesario  
**Respuesta esperada:** `200 OK` con array de productos

---

### Paso 3: Crear Clientes

**Método:** `POST`  
**URL:** `http://localhost:8080/servicioswebapi/api/customers`  
**Headers:**
```
Content-Type: application/json
```
**Body (raw JSON):**
```json
{
  "fullName": "Tienda El Ahorro",
  "taxId": "12345678-9",
  "email": "contacto@elahorro.com",
  "address": "Zona 10, Ciudad de Guatemala",
  "active": true
}
```
**Respuesta esperada:** `201 Created` con el cliente creado incluyendo su `customerId`

Crea otro cliente:
```json
{
  "fullName": "Supermercado La Economía",
  "taxId": "98765432-1",
  "email": "ventas@laeconomia.com",
  "address": "Zona 1, Ciudad de Guatemala",
  "active": true
}
```

---

### Paso 4: Buscar Cliente por NIT

**Método:** `GET`  
**URL:** `http://localhost:8080/servicioswebapi/api/customers/taxid/12345678-9`  
**Respuesta esperada:** `200 OK` con los datos del cliente

---

### Paso 5: Crear una Orden

**Método:** `POST`  
**URL:** `http://localhost:8080/servicioswebapi/api/orders`  
**Headers:**
```
Content-Type: application/json
```
**Body (raw JSON):**
```json
{
  "customerId": 1,
  "orderDate": "2024-11-20",
  "items": [
    {
      "productId": 1,
      "quantity": 10
    },
    {
      "productId": 2,
      "quantity": 5
    }
  ]
}
```
**Respuesta esperada:** `201 Created` con la orden creada, incluyendo:
- `orderId`
- `totalAmount` calculado automáticamente
- `status: "PENDING"`
- Lista de `items` con sus subtotales

---

### Paso 6: Consultar Órdenes por Cliente

**Método:** `GET`  
**URL:** `http://localhost:8080/servicioswebapi/api/orders/customer/1`  
**Respuesta esperada:** `200 OK` con array de órdenes del cliente

---

### Paso 7: Actualizar Estado de Orden

**Método:** `PATCH`  
**URL:** `http://localhost:8080/servicioswebapi/api/orders/1/status?status=PROCESSING`  
**Respuesta esperada:** `200 OK` con la orden actualizada

---

### Paso 8: Registrar un Pago

**Método:** `POST`  
**URL:** `http://localhost:8080/servicioswebapi/api/payments`  
**Headers:**
```
Content-Type: application/json
```
**Body (raw JSON):**
```json
{
  "orderId": 1,
  "paymentDate": "2024-11-20",
  "amount": 100.00,
  "method": "Cash"
}
```
**Respuesta esperada:** `201 Created` con el pago registrado

---

### Paso 9: Consultar Pagos de una Orden

**Método:** `GET`  
**URL:** `http://localhost:8080/servicioswebapi/api/payments/order/1`  
**Respuesta esperada:** `200 OK` con array de pagos de la orden

---

### Paso 10: Actualizar un Producto

**Método:** `PUT`  
**URL:** `http://localhost:8080/servicioswebapi/api/products/1`  
**Headers:**
```
Content-Type: application/json
```
**Body (raw JSON):**
```json
{
  "name": "Coca Cola 2L",
  "description": "Bebida gaseosa refrescante",
  "price": 16.00,
  "category": "Beverages",
  "active": true
}
```
**Respuesta esperada:** `200 OK` con el producto actualizado

---

### Paso 11: Desactivar un Cliente

**Método:** `PATCH`  
**URL:** `http://localhost:8080/servicioswebapi/api/customers/2/deactivate`  
**Respuesta esperada:** `200 OK` con el cliente desactivado (`active: false`)

---

### Paso 12: Eliminar un Producto

**Método:** `DELETE`  
**URL:** `http://localhost:8080/servicioswebapi/api/products/3`  
**Respuesta esperada:** `204 No Content`

---

## 📊 Códigos de Respuesta HTTP

| Código | Significado | Cuándo se usa |
|--------|-------------|---------------|
| `200 OK` | Operación exitosa | GET, PUT, PATCH exitosos |
| `201 Created` | Recurso creado exitosamente | POST exitoso |
| `204 No Content` | Eliminación exitosa | DELETE exitoso |
| `400 Bad Request` | Datos inválidos o regla de negocio violada | Validación fallida |
| `404 Not Found` | Recurso no encontrado | ID inexistente |
| `409 Conflict` | Conflicto (ej: NIT duplicado) | Violación de unicidad |
| `500 Internal Server Error` | Error del servidor | Error inesperado |

---

## Casos de Prueba y Validación

### Intentar crear orden con cliente inactivo (debe fallar)

1. Desactivar el cliente:
   - **PATCH** `http://localhost:8080/servicioswebapi/api/customers/1/deactivate`

2. Intentar crear orden:
   - **POST** `http://localhost:8080/servicioswebapi/api/orders`
   - **Resultado esperado:** `400 Bad Request` con mensaje de error

### Intentar pago que excede el total (debe fallar)

- **POST** `http://localhost:8080/servicioswebapi/api/payments`
```json
{
  "orderId": 1,
  "paymentDate": "2024-11-20",
  "amount": 999999.00,
  "method": "Cash"
}
```
- **Resultado esperado:** `400 Bad Request` con mensaje de error

### Intentar crear cliente con NIT duplicado (debe fallar)

- **POST** `http://localhost:8080/servicioswebapi/api/customers`
```json
{
  "fullName": "Otra Tienda",
  "taxId": "12345678-9",
  "email": "otra@tienda.com",
  "address": "Zona 5",
  "active": true
}
```
- **Resultado esperado:** `409 Conflict` con mensaje de error

---


