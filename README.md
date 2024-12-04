# Proyecto: Planificador de Vacaciones Android

## Descripción del Proyecto

Este proyecto tiene como objetivo desarrollar una aplicación Android para la planificación de vacaciones, 
permitiendo a los usuarios agregar lugares que desean visitar, ver estos lugares en un mapa, tomar fotos relacionadas con los lugares, 
y almacenar todos los datos en una base de datos SQLite en el dispositivo. Además, la aplicación integrará un servicio web para mostrar 
los costos en dólares estadounidenses (USD) en lugar de la moneda local (CLP). También contará con soporte multilenguaje en inglés y español.

## Requisitos Específicos

1. **Planificación de Lugares**
   - Los usuarios podrán agregar lugares a visitar durante su viaje.
   - Cada lugar debe contener la siguiente información:
     - Nombre del lugar
     - Orden de visita
     - URL de una imagen de referencia (que se mostrará utilizando la librería Coil)
     - Latitud y longitud del lugar
     - Costo de alojamiento
     - Costo de transporte (si aplica)
     - Comentarios adicionales.

2. **Visualización de Mapa**
   - Se utilizará la librería **osmdroid** para mostrar los lugares planificados.
   - En el mapa se colocarán marcadores en las coordenadas de cada lugar.

3. **Agregar Fotos desde la Aplicación**
   - Los usuarios podrán tomar una fotografía desde la aplicación para cada lugar visitado.
   - Las fotos tomadas estarán vinculadas a cada lugar y sus URIs serán almacenadas en la base de datos.

4. **Persistencia de Datos**
   - Todos los datos relacionados con los lugares, imágenes de referencia y fotografías tomadas se almacenarán en una base de datos **SQLite** en el dispositivo del usuario.
   - Las imágenes serán almacenadas como URIs en la base de datos.

5. **Integración de Web Service**
   - La aplicación consumirá un servicio web para obtener la tasa de cambio actual entre la moneda local (CLP) y el dólar estadounidense (USD).
   - Se utilizará el servicio web de **mindicador.cl** para obtener la tasa de cambio y mostrar los costos en USD.

6. **Multilenguaje**
   - La aplicación debe soportar dos idiomas: **español** e **inglés**.
   - Los textos estáticos de la interfaz, como botones y etiquetas, deben estar traducidos y ser cambiables según el idioma seleccionado.

7. **Interfaz de Usuario**
   - La aplicación debe incluir al menos tres pantallas:
     - **Pantalla de Listado de Lugares**: Muestra una lista de los lugares planificados.
     - **Pantalla de Detalles del Lugar**: Muestra información detallada de un lugar específico.
     - **Pantalla de Edición del Lugar**: Permite editar los datos de un lugar.

## Tecnologías Utilizadas

- **Kotlin**: Lenguaje de programación utilizado.
- **SQLite**: Base de datos local para almacenar los datos de los lugares, imágenes y fotos.
- **osmdroid**: Librería para mostrar el mapa y colocar marcadores.
- **Coil**: Librería para cargar imágenes desde una URL.
- **Retrofit / HttpURLConnection**: Para consumir el servicio web de tasas de cambio.
- **Jetpack Compose**: Para construir la interfaz de usuario.
- **Multilenguaje en Android**: Usando recursos de strings para soportar español e inglés.

## Instrucciones de Uso

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/Yistler/PlanVacaciones.git

![Pantalla principal](https://raw.githubusercontent.com/Yistler/PlanVacaciones/refs/heads/main/img/detalle.png)
![Pantalla agregar](https://raw.githubusercontent.com/Yistler/PlanVacaciones/refs/heads/main/img/formulario.png)
![Pantalla editar](https://raw.githubusercontent.com/Yistler/PlanVacaciones/refs/heads/main/img/lista.png)
