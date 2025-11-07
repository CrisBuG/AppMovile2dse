# AppMovile2dse
unidad 2do semestre app mobiles prueba

## Aplicación Móvil Android con Kotlin y Gradle

Esta es una aplicación móvil básica de Android desarrollada con **Kotlin** y **Gradle**.

### Características

- 🤖 **Android nativo**: Aplicación desarrollada para Android
- 🎨 **Kotlin**: Lenguaje de programación moderno y seguro
- 🔧 **Gradle**: Sistema de construcción con Kotlin DSL
- 📱 **Material Design**: Interfaz de usuario con componentes de Material Design
- ✨ **ViewBinding**: Acceso seguro a las vistas

### Estructura del Proyecto

```
AppMovile2dse/
├── app/                                    # Módulo principal de la aplicación
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/appmovile2dse/
│   │       │   └── MainActivity.kt        # Actividad principal
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml  # Layout de la actividad principal
│   │       │   ├── values/
│   │       │   │   ├── colors.xml         # Definición de colores
│   │       │   │   ├── strings.xml        # Cadenas de texto
│   │       │   │   └── themes.xml         # Temas de la aplicación
│   │       │   ├── drawable/              # Recursos gráficos
│   │       │   └── mipmap-*/              # Iconos de la aplicación
│   │       └── AndroidManifest.xml        # Manifiesto de Android
│   ├── build.gradle.kts                   # Configuración de Gradle del módulo
│   └── proguard-rules.pro                 # Reglas de ProGuard
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties      # Configuración del wrapper de Gradle
├── build.gradle.kts                       # Configuración de Gradle raíz
├── settings.gradle.kts                    # Configuración de módulos
├── gradle.properties                      # Propiedades del proyecto
└── gradlew                                # Script de Gradle wrapper (Linux/Mac)
```

### Requisitos

- **Android Studio**: Arctic Fox (2020.3.1) o superior
- **JDK**: Java Development Kit 8 o superior
- **Android SDK**: API Level 24 (Android 7.0) o superior
- **Gradle**: 8.0 (incluido con el proyecto)

### Configuración del Proyecto

#### Versiones utilizadas:
- **Kotlin**: 1.9.0
- **Android Gradle Plugin**: 8.1.0
- **Compile SDK**: 34
- **Min SDK**: 24
- **Target SDK**: 34

#### Dependencias principales:
- `androidx.core:core-ktx:1.12.0` - Extensiones de Kotlin para Android
- `androidx.appcompat:appcompat:1.6.1` - Compatibilidad con versiones anteriores
- `com.google.android.material:material:1.10.0` - Material Design
- `androidx.constraintlayout:constraintlayout:2.1.4` - Layout constraint

### Cómo ejecutar el proyecto

1. **Clonar el repositorio**:
   ```bash
   git clone https://github.com/CrisBuG/AppMovile2dse.git
   cd AppMovile2dse
   ```

2. **Abrir en Android Studio**:
   - Abre Android Studio
   - Selecciona "Open an Existing Project"
   - Navega hasta la carpeta del proyecto y selecciónala
   - Espera a que Gradle sincronice las dependencias

3. **Ejecutar la aplicación**:
   - Conecta un dispositivo Android o inicia un emulador
   - Haz clic en el botón "Run" (▶️) o presiona `Shift + F10`
   - La aplicación se instalará y ejecutará en el dispositivo/emulador

### Funcionalidad de la Aplicación

La aplicación de demostración incluye:
- **Pantalla principal** con un texto de bienvenida
- **Botón interactivo** que cuenta los clics
- **Interfaz Material Design** con colores personalizados

### Compilación desde línea de comandos

```bash
# Linux/Mac
./gradlew build

# Windows
gradlew.bat build
```

### Generar APK

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease
```

El APK generado se encontrará en: `app/build/outputs/apk/`

### Autor

Desarrollado como proyecto de la unidad 2do semestre de aplicaciones móviles.

### Licencia

Este proyecto es de código abierto y está disponible para fines educativos. 
