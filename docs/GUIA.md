
### 1\. `RestTemplate` (Tu navegador web para código)

**¿Qué es?**
Es un cliente HTTP sincrónico. Piensa en él como un navegador web (como Chrome) pero para tu código Java. Te permite hacer peticiones (GET, POST, PUT, DELETE) a otros servidores web o APIs REST.

**¿Para qué sirve?**

* Se conecta a una URL externa.
* Envía datos (headers, body).
* Recibe la respuesta.
* **Lo más importante:** Convierte automáticamente el JSON que recibe en Objetos Java (DTOs) usando una librería interna (Jackson).

**En tu Proyecto (Backend):**
Lo usas en el **Backend** para hablar con el **Servicio de la Cátedra**.

* **Configuración:** En `RestTemplateConfig.java`, configuraste un `RestTemplate` que automáticamente "inyecta" el Token JWT de la cátedra en cada petición.
* **Uso:** En `EventoServiceImpl.java`, lo usas para traer los eventos:
  ```java
  // Le dices: "Ve a esta URL (GET) y convierte el JSON que recibas en un array de EventoCatedraDTO"
  restTemplate.getForObject(CATEDRA_EVENTOS_URL, EventoCatedraDTO[].class);
  ```
  Sin `RestTemplate`, tendrías que abrir una conexión HTTP a mano, leer el stream de bytes, y parsear el JSON manualmente, lo cual sería mucho código.

-----

### 2\. `RedisTemplate` (Tu conector para Redis)

**¿Qué es?**
Es una clase de ayuda que simplifica el acceso a bases de datos **Redis**. Redis no usa SQL, usa estructuras de datos clave-valor. `RedisTemplate` te da métodos de alto nivel para interactuar con estas estructuras sin tener que lidiar con los comandos crudos de Redis o la serialización de bytes.

**¿Para qué sirve?**

* Maneja la conexión con el servidor Redis.
* Traduce (serializa) tus objetos Java a un formato que Redis entienda (generalmente Strings o Bytes) y viceversa.
* Ofrece operaciones específicas para tipos de datos: `opsForValue()` (Strings simples), `opsForHash()` (Mapas/Hashes), `opsForList()` (Listas), etc.

**En tu Proyecto (Proxy):**
Lo usas en el **Proxy** para leer el estado de los asientos del **Redis de la Cátedra**.

* **Configuración:** En `RedisConfig.java`, lo configuraste para que trate tanto las claves como los valores como `String` (`StringRedisSerializer`).
* **Uso:** En `AsientoService.java`, lo usas para leer un Hash:
  ```java
  // Le dices: "Dame todas las entradas (campo y valor) del Hash guardado en esta clave"
  redisTemplate.opsForHash().entries(claveRedis);
  ```
  Esto te devuelve un `Map` de Java directamente, ocultando la complejidad del protocolo de red de Redis.

-----

### Diferencia Clave

| Característica | `RestTemplate` | `RedisTemplate` |
| :--- | :--- | :--- |
| **Objetivo** | Comunicarse con **APIs Web (HTTP/REST)** | Comunicarse con **Base de Datos Redis** |
| **Protocolo** | HTTP / HTTPS | Protocolo RESP (Redis Serialization Protocol) |
| **Tu uso** | `Backend` -\> `API Cátedra` (Obtener eventos) | `Proxy` -\> `Redis Cátedra` (Ver asientos ocupados) |
| **Tipo de operación** | Petición/Respuesta (Request/Response) | Lectura/Escritura de datos (Key-Value) |