# Trabajo Final: Sistema de Gestión de Eventos y Entradas

## 🎯 Objetivo General

Construir un ecosistema distribuido para la venta y reserva de entradas a eventos. 
El sistema debe sincronizar información en tiempo real con un servidor central (Cátedra) y permitir a los clientes móviles realizar reservas y compras 
de asientos de manera concurrente, garantizando la consistencia de datos mediante bloqueos temporales y actualizaciones asíncronas.

## 🏗️ Arquitectura del Sistema

El sistema está compuesto por tres módulos principales desarrollados por el alumno, que interactúan con los servicios de la Cátedra:

1. **Backend (JHipster/Spring Boot):** Núcleo del negocio. Maneja la persistencia local (MySQL), usuarios, sesiones de compra y orquesta la lógica de venta.
2. **Proxy Service (Spring Boot):** Intermediario de infraestructura. Es el único componente con acceso directo a **Kafka** (para escuchar cambios en eventos) y **Redis** (para consultar el mapa de asientos en tiempo real) de la Cátedra.
3. **Cliente Móvil (Kotlin Multiplatform):** Aplicación Android (y potencial iOS/Desktop) desarrollada con Compose Multiplatform que sirve como interfaz de usuario para la selección de butacas y pago.

---

## 👨‍🎓 Información del Alumno

* **Nombre y Apellido**: Augusto Giuffrida
* **Legajo**: 60137
* **Materia**: Programación II - 2025

---

## 🛠️ Tecnologías y Herramientas

### Backend

* **Framework:** Spring Boot (Generado con JHipster)
* **Seguridad:** Spring Security con JWT
* **Base de Datos:** MySQL 8.0 (Producción/Dev), H2 (Test)
* **ORM:** Hibernate

### Proxy Service

* **Mensajería:** Spring Kafka (Consumer)
* **Comunicación:** RestTemplate para reenvío al Backend

### Cliente Móvil

* **Lenguaje:** Kotlin
* **UI:** Jetpack Compose Multiplatform
* **Red:** Ktor Client
* **Navegación:** Voyager
* **Serialización:** Kotlinx Serialization

### Infraestructura

* **Docker Compose:** Para orquestación de bases de datos y servicios auxiliares.

---

## 🖥️ Instrucciones de Instalación y Ejecución

### 1. Requisitos Previos

* Java 21 JDK instalado.
* Docker corriendo.
* Android Studio (para ejecutar el cliente móvil).
* Acceso a la red de la Cátedra para Kafka/Redis remotos.

### 2. Configuración de Base de Datos e Infraestructura

Desde la carpeta raíz del proyecto, levanta los contenedores necesarios:

```bash
docker compose up -d
```

Esto iniciará MySQL y el Redis local para el manejo de sesiones de usuario.

### 3. Ejecución del Proxy Service

El proxy es necesario para la sincronización de eventos.

```bash
cd proxy
# Asegúrate de configurar la IP de la cátedra en src/main/resources/application.yaml
./mvnw spring-boot:run

```

> **Nota:** El proxy escuchará en el puerto `8081` por defecto.

### 4. Ejecución del Backend

```bash
cd backend
./mvnw spring-boot:run

```

> **Nota:** El backend escuchará en el puerto `8080`.

### 5. Ejecución del Cliente Móvil

Abrir la carpeta `composeApp` en Android Studio.

* Sincronizar Gradle.
* Ejecutar la configuración `composeApp` (Android).

---

## 🔄 Flujos Principales

### Sincronización de Eventos (Kafka)

1. La Cátedra publica un cambio en el tópico `eventos-actualizacion`.
2. El **Proxy** (Consumer Group: `AugustoGiuffrida`) recibe el mensaje.
3. El Proxy envía un POST interno al Backend (`/api/eventos/notificacion-cambio`).
4. El **Backend** consulta la API REST de la Cátedra, obtiene la lista actualizada y sincroniza su base de datos MySQL local (Creación/Actualización).

### Proceso de Compra

1. **Selección:** El usuario elige un evento en la App. El Backend solicita al Proxy (quien consulta al Redis de Cátedra) el estado actual de los asientos.
2. **Bloqueo:** Al confirmar selección, el Backend envía una solicitud de bloqueo a la Cátedra.
3. **Datos:** El usuario carga los nombres de los asistentes.
4. **Venta:** Se confirma la transacción. El Backend registra la venta localmente y notifica a la Cátedra para finalizar la persistencia.

---

## 🌐 Endpoints Principales (Backend)

Estos son los endpoints expuestos por el Backend para el consumo del Cliente Móvil.

### 🔐 Autenticación

| Método | Endpoint | Descripción |
| --- | --- | --- |
| POST | `/api/authenticate` | Login de usuario. Retorna JWT. |
| GET | `/api/account` | Obtener datos del usuario logueado. |

### 📅 Eventos

| Método | Endpoint | Descripción                                 |
| --- | --- |---------------------------------------------|
| GET | `/api/eventos` | Listar eventos disponibles (sincronizados). |
| GET | `/api/eventos/{id}` | Detalle completo de un evento.              |
| POST | `/api/eventos/sincronizar` | Forzar sincronización manual con Cátedra.   |
| POST | `/api/eventos/notificacion-cambio` | Sincronización automatica mediante kafka.   |

### 🎟️ Gestión de Ventas y Asientos

| Método | Endpoint | Descripción |
| --- | --- | --- |
| GET | `/api/asientos-ocupados/{eventoId}` | Consulta (vía Proxy) el mapa de asientos en tiempo real. |
| POST | `/api/gateway/bloquear-asientos` | Solicita bloqueo temporal de butacas en servidor externo. |
| POST | `/api/gateway/realizar-venta` | Confirma la compra, guarda localmente y notifica a la Cátedra. |
| POST | `/api/ventas/reintentar/{id}` | Mecanismo de **Resiliencia**: Reintenta enviar ventas que quedaron en estado `PENDIENTE` por fallos de red. |
| POST | `/api/ventas/bloquear` | Bloqueo: Recibe SolicitudBloqueoDTO. Solicita bloqueo a Cátedra y actualiza sesión local. |
| POST | `/api/ventas/comprar` | Solicita bloqueo temporal de butacas en servidor externo. |


### 🛒 Gestión de Sesión (Redis)

| Método | Endpoint | Recurso Java | Descripción |
| --- | --- | --- | --- |
| GET | `/api/sesion` | `SesionResource` | Recupera el estado actual (evento seleccionado, butacas). |
| POST | `/api/sesion` | `SesionResource` | Actualiza la sesión (ej: al seleccionar/deseleccionar butacas). |
| DELETE | `/api/sesion` | `SesionResource` | Limpia la sesión actual (logout o fin de compra). |
