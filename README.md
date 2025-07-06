# Weather App

Una aplicación de clima simple y bonita para Android.

## Descripción

Weather App proporciona información meteorológica en tiempo real para tu ubicación actual y cualquier otra ciudad del mundo. Con una interfaz de usuario limpia e intuitiva, puedes obtener fácilmente el pronóstico del tiempo para los próximos días.

## Características

*   **Clima Actual:** Obtén la temperatura actual, humedad, velocidad del viento y condiciones climáticas.
*   **Pronóstico por Hora:** Visualiza un pronóstico detallado por hora para las próximas 24 horas.
*   **Pronóstico Diario:** Planifica con antelación con un pronóstico del tiempo para 7 días.
*   **Búsqueda de Ciudades:** Encuentra información del tiempo para cualquier ciudad del mundo.
*   **Guardar Ubicaciones:** Guarda tus ubicaciones favoritas para un acceso rápido.
*   **Interfaz de Usuario Limpia:** Interfaz estéticamente agradable y fácil de usar.


## Tecnologías Utilizadas

*   **Kotlin:** Lenguaje de programación oficial para el desarrollo de Android.
*   **Jetpack Compose:** Kit de herramientas moderno para construir interfaces de usuario nativas de Android.
*   **Retrofit:** Un cliente HTTP seguro para Android y Java.
*   **Coroutines:** Para programación asíncrona.
*   **Dagger/Hilt:** Para inyección de dependencias.
*   **Room:** Para almacenamiento en base de datos local (guardar ubicaciones).
*   **API de OpenWeatherMap:** Para obtener los datos del clima.

## Configuración e Instalación

1.  **Clona el repositorio:**
    ```bash
    git clone https://github.com/tu-usuario/weather-app.git
    cd weather-app
    ```

2.  **Obtén una Clave de API:**
    Esta aplicación utiliza la [API de OpenWeatherMap](https://openweathermap.org/api) para obtener datos del clima. Necesitas obtener tu propia clave de API.
    *   Ve a [OpenWeatherMap](https://openweathermap.org/) y crea una cuenta.
    *   Navega a la pestaña de claves API y genera una nueva clave.

3.  **Añade tu Clave de API:**
    *   Crea un archivo llamado `local.properties` en el directorio raíz del proyecto.
    *   Añade la siguiente línea a `local.properties`:
        ```properties
        OPENWEATHERMAP_API_KEY="TU_CLAVE_DE_API"
        ```
    *   El proyecto está configurado para leer esta clave desde `build.gradle.kts`.

4.  **Compila y Ejecuta:**
    *   Abre el proyecto en Android Studio.
    *   Deja que Gradle sincronice las dependencias.
    *   Compila y ejecuta la aplicación en un emulador o un dispositivo físico.

## Contribuciones

¡Las contribuciones son bienvenidas! Si tienes alguna idea, sugerencia o informe de error, por favor abre un issue o crea un pull request.

1.  Haz un Fork del Proyecto
2.  Crea tu Rama de Característica (`git checkout -b feature/AmazingFeature`)
3.  Confirma tus Cambios (`git commit -m 'Add some AmazingFeature'`)
4.  Empuja a la Rama (`git push origin feature/AmazingFeature`)
5.  Abre un Pull Request

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - mira el archivo `LICENSE` para más detalles.

