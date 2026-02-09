## 💵 Backend Billetera Virtual
API REST para la gestión de una billetera virtual. El proyecto ha sido desarrollado bajo los principios de Arquitectura Hexagonal (Puertos y Adaptardores), garantizando un dominio de negocio independiente de las tecnologías externas y una alta testabilidad.

## 🛠️ Stack Tecnológico
- **Lenguaje:** Java 21
- **Framework:** Spring Boot 3.4.12
- **Seguridad:** Spring Security & JWT (JSON Web Tokens)
- **Base de Datos:** PostgreSQL
- **Persistencia:** Spring Data JPA
- **Mapeo de Entidades:** MapStruct
- **Construcción:** Maven

## 🏗️ Arquitectura del Proyecto
El proyecto utiliza un diseño orientado a dominios (Features) en lugar de capas técnicas globales. Esto permite que cada módulo sea independiente, facilitando el mantenimiento y la escalabilidad del sistema. Cada módulo aplica internamente el patrón de Arquitectura Hexagonal.
```text
src/main/java/com/billetera_virtual/
├── auth/         # Gestión de sesiones y seguridad
├── user/         # Gestión de perfiles y datos personales
├── account/      # Lógica financiera (Saldo, CVU)
└── transaction/  # Orquestación de transferencias, depósitos e historial
```
### Estructura interna de cada módulo
- **domain**: El corazón del módulo. Contiene las reglas de negocio, modelos de dominio y los **puertos** de entrada y salida. No tiene dependencias externas.
- **application**: Contiene los casos de uso (Servicios). Orquesta la lógica llamando a los puertos de dominio.
- **infra**: Contiene los **adaptadores**. Aquí es donde el módulo se conecta con el "mundo real": persistencia (JPA), seguridad (Spring Security) o servicios externos.

## 🎯 Funcionalidades Destacadas
**Generacion automática de alias:** 

Sistema inspirado en el estándar bancario argentino. Al registrarse el sistema genera un alias de forma aleatoria combinando 3 palabras (ej: `CASA.BOSQUE.GATO`).

- **Implementación:** Uso de `ResourceLoader` para cargar diccionarios de palabras desde el `classpath`.

**Algoritmo de CVU:**

Generación de **Clave Virtual Uniforme (CVU)** de 22 dígitos.

- **Estructura:** Código de PSP + Dígito de control + Identificador de sucursal + Número de cuenta + Dígito verificador.
- **Integridad:** Garantizada mediante el uso de IDs incrementales de base de datos para asegurar unicidad absoluta.

**Transferencias Transaccionales:**

Las operaciones de transferencia entre cuentas están protegidas por la anotación `@Transactional` de Spring.

- **Seguridad:** Si el descuento de saldo o la persistencia del comprobante fallan, se realiza un *rollback* automático para proteger el dinero de los usuarios.

**Historial de Transacciones:**

Las transacciones podran ser visualizadas en un historial, el cual podra filtrarse por monto o tipo.

- **Formato**: Se implementó Pagination para optimizar el tráfico de red y los tiempos de respuesta del servidor

## 🔗 Endpoints Principales
**Autenticación e Identidad**

- `POST /auth/register`: Registro de usuario y creación automática de cuenta con CVU y alias.
- `POST /auth/login`: Autenticación con JWT.

**Cuentas y Usuarios**

- `GET /user/profile`: Obtiene datos del usuario autenticado.
- `GET /account/me`: Obtiene datos de la cuenta del usuario autenticado
- `PUT /user/`: Actualización personalizada de los datos del usuario.

**Transacciones**

- `POST /transaction/transfer`: Realiza una transferencia validando saldo y existencia del alias.
- `GET /transaction/history` : Obtiene el historial paginado con filtros opcionales de monto.
