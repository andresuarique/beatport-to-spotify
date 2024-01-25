# Historial de Cambios

## [v1.0.0] - 25/01/2024

### Características

- **Obtención de Listas de Reproducción:**
  - La aplicación utiliza un scraper con jsoup para obtener las listas de mejores canciones de cada género en Beatport.
  - Los usuarios pueden ingresar a la página principal (`/home`), proporcionar un nombre para la playlist y seleccionar el género deseado.
  - Al hacer clic en "Crear Playlist", la aplicación inicia el proceso de creación de una lista de reproducción en Spotify basada en las canciones obtenidas de Beatport.

- **Visualización de Detalles de la Playlist:**
  - Después de completar el proceso de creación, los usuarios son redirigidos a una pantalla de detalles (`/detail`) que muestra la lista de reproducción creada.
  - Se proporciona una opción para ver la lista en Spotify, lo que permite a los usuarios agregarla a sus playlists en Spotify.
  - También se ofrece la posibilidad de ver la lista original en Beatport, permitiendo a los usuarios explorar la fuente de las canciones.

---
# Tecnologías Utilizadas

- **Backend:**
  - Desarrollado con Java 1.8 y Spring Boot 2.4.3.

- **Frontend:**
  - Desarrollado con Angular 12.