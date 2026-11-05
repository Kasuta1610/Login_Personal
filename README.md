# Login RPG & Aesthetic — Jetpack Compose

Aplicación móvil Android desarrollada en **Jetpack Compose** para el segundo año de **DAM (Desarrollo de Aplicaciones Multiplataforma)**, diseñada con una temática minimalista y estética de inventario RPG.

###  Características del Proyecto
* **Arquitectura Declarativa Pura:** Sin layouts XML, sin librerías externas de navegación y sin ViewModel complejo. Todo controlado mediante un flujo de estado unidireccional y nativo (`rememberSaveable`).
* **Panel de Identificación (Login):** Interfaz limpia con validación de credenciales en local, campos interactivos para correo y contraseña (con alternancia de visibilidad mediante icono de ojo), y manejo de estados de error visuales.
* **Panel de Bienvenida (Aventurero):** Pantalla de éxito personalizada que recibe el nombre del usuario y presenta un lienzo (`Canvas`) con efectos visuales dinámicos de destellos y explosiones geométricas.
* **Componentes Reutilizables:** Uso de componentes personalizados como `LogoCircular` adaptados bajo los principios de Material Design 3.
