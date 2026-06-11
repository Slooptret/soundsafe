# 🔊 SoundSafe

> Monitorea tu exposición al ruido en tiempo real y protege tu salud auditiva.

![Estado](https://img.shields.io/badge/Estado-En%20desarrollo-yellow)
![Platform](https://img.shields.io/badge/Platform-Android-green)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple)
![API](https://img.shields.io/badge/API%20mínima-26%20(Android%208.0)-blue)

---

## 📋 Tabla de contenidos

- [El problema](#-el-problema)
- [Objetivo de la aplicación](#-objetivo-de-la-aplicación)
- [Historias de usuario MVP](#-historias-de-usuario-mvp)
- [Tecnología utilizada](#-tecnología-utilizada)
- [Instalación](#-instalación)
- [Capturas de pantalla](#-capturas-de-pantalla)
- [Estado actual del proyecto](#-estado-actual-del-proyecto)

---

## 🚨 El problema

La exposición prolongada a niveles elevados de ruido es una de las principales causas de pérdida auditiva permanente a nivel mundial. Sin embargo, la mayoría de las personas no saben cuándo el ambiente que los rodea supera los límites seguros establecidos por la OMS (85 dB para exposición prolongada).

Situaciones cotidianas como conciertos, transporte público, fábricas o incluso el uso de auriculares pueden generar daños auditivos irreversibles de forma silenciosa. El problema no es solo la intensidad del sonido, sino la **duración de la exposición acumulada** — un dato que ninguna app de medición básica registra ni alerta.

---

## 🎯 Objetivo de la aplicación

Desarrollar una aplicación Android que permita a los usuarios **monitorear en tiempo real los niveles de ruido ambiental**, registrar el historial de exposición por sesión, y recibir alertas cuando se superen los umbrales de seguridad auditiva — todo desde su teléfono, sin hardware adicional.

### Objetivos específicos

1. Capturar niveles de presión sonora (dB SPL) en tiempo real usando el micrófono del dispositivo.
2. Clasificar el ruido en cuatro niveles: Silencioso (0–30 dB), Moderado (31–60 dB), Ruidoso (61–85 dB) y Peligroso (+85 dB).
3. Generar alertas visuales cuando el nivel supere el umbral configurado por el usuario.
4. Registrar sesiones de monitoreo con nombre de lugar, promedio de dB y duración.
5. Almacenar el historial de sesiones localmente para consulta y exportación posterior.

---

## 👤 Historias de usuario MVP

| # | Como... | Quiero... | Para... |
|---|---------|-----------|---------|
| US-01 | Usuario general | Ver el nivel de ruido actual en dB SPL en tiempo real | Saber si el ambiente es seguro para mis oídos |
| US-02 | Asistente a conciertos | Recibir una alerta cuando el ruido supere +85 dB | Tomar acción antes de dañar mi audición |
| US-03 | Usuario frecuente | Consultar el historial de sesiones con filtros por nivel | Identificar patrones de exposición peligrosa |
| US-04 | Usuario personalizado | Configurar mis propios umbrales de alerta por nivel | Adaptar la app a mi situación auditiva específica |
| US-05 | Usuario registrado | Ver mi nombre de perfil en la pantalla de configuración | Confirmar que mis datos están guardados correctamente |
| US-06 | Usuario en medición | Ver cuántas sesiones y alertas acumulé hoy | Tener conciencia de mi exposición diaria total |
| US-07 | Usuario con historial | Exportar o eliminar mis datos de sesiones | Gestionar mi información personal desde la app |

---

## 🛠️ Tecnología utilizada

| Componente | Tecnología |
|---|---|
| **Lenguaje** | Kotlin |
| **IDE** | Android Studio |
| **UI** | Jetpack Compose + Material Design 3 |
| **Arquitectura** | MVVM (Model-View-ViewModel) |
| **Base de datos local** | Room Database |
| **Preferencias** | DataStore |
| **Captura de audio** | AudioRecord (Android SDK) |
| **Gestión de estado** | StateFlow + ViewModel (Jetpack) |
| **Diseño UI/UX** | Figma |
| **Control de versiones** | Git + GitHub |

### Arquitectura MVVM

```
View — Jetpack Compose (MonitoringFragment, HistoryFragment, SettingsFragment)
    └── ViewModel (SoundViewModel)
            └── UseCase (NoiseMonitorUseCase)
                    ├── Repository (SesionRepository)
                    │       └── Room Database
                    │           (Usuario, ConfiguracionUsuario, Sesion, Alerta)
                    └── MicrophoneRecorder (AudioRecord)
```

### Clasificación de niveles de ruido

| Nivel | Rango | Color |
|---|---|---|
| 🟢 Silencioso | 0 – 30 dB | Verde |
| 🟡 Moderado | 31 – 60 dB | Amarillo |
| 🟠 Ruidoso | 61 – 85 dB | Naranja |
| 🔴 Peligroso | +85 dB | Rojo |

---

## ⚙️ Instalación

### Requisitos previos

- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17
- Android SDK API 26 o superior
- Git instalado

### Pasos

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/Slooptret/soundsafe.git
   ```

2. **Abre el proyecto en Android Studio**
   ```
   File → Open → selecciona la carpeta soundsafe/
   ```

3. **Sincroniza las dependencias de Gradle**
   ```
   Tools → Android → Sync Project with Gradle Files
   ```

4. **Ejecuta la aplicación**
   - En emulador: selecciona un AVD con API 26+ y presiona ▶ Run
   - En dispositivo físico: activa la Depuración USB y conecta tu teléfono

### Permisos requeridos

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

> ⚠️ El permiso `RECORD_AUDIO` es solicitado en tiempo de ejecución la primera vez que se inicia una sesión de monitoreo.

---

## 📱 Capturas de pantalla

El prototipo de SoundSafe cuenta con tres pantallas principales diseñadas en Figma con Material Design 3:

### MonitoringFragment — Pantalla principal de medición

> Muestra el nivel de ruido en tiempo real (dB SPL), los umbrales de alerta configurados, el resumen del día (sesiones y alertas) y las últimas sesiones registradas.

![MonitoringFragment](docs/screenshots/monitoring_fragment.png)

---

### HistoryFragment — Historial de sesiones

> Lista completa de sesiones ordenadas por fecha con filtros visuales por nivel de ruido (Silencioso, Moderado, Ruidoso, Peligroso). Cada sesión muestra el lugar, promedio en dB y duración.

![HistoryFragment](docs/screenshots/history_fragment.png)

---

### SettingsFragment — Configuración

> Permite ajustar los umbrales de alerta para cada nivel de ruido mediante sliders, ver el perfil del usuario y gestionar los datos (exportar historial o eliminar todos los datos).

![SettingsFragment](docs/screenshots/settings_fragment.png)

---

> 📐 Prototipo completo disponible en [Figma](https://www.figma.com/design/8ULlv7cqWYHU15vCTfMw2X/SoundSafe_Prototipo_Esteban_Aumala)

---

## 📊 Estado actual del proyecto

```
Fase 1 — Planificación y diseño        ██████████ 100% ✅
Fase 2 — Diseño UI/UX en Figma         ██████████ 100% ✅
Fase 3 — Configuración del entorno     ██████████ 100% ✅
Fase 4 — Implementación de UI          ░░░░░░░░░░   0% 🔜
Fase 5 — Lógica de negocio             ░░░░░░░░░░   0% 🔜
Fase 6 — Base de datos local           ░░░░░░░░░░   0% 🔜
Fase 7 — Pruebas                       ░░░░░░░░░░   0% 🔜
Fase 8 — Presentación final            ░░░░░░░░░░   0% 🔜
```

### ✅ Completado
- Definición de arquitectura MVVM con componentes nombrados
- Modelo de datos ER (4 entidades: Usuario, ConfiguracionUsuario, Sesion, Alerta)
- 7 historias de usuario del MVP
- Objetivos generales y 5 objetivos específicos
- Diseño UI/UX completo en Figma: paleta MD3, 3 pantallas principales con navegación por Bottom Navigation Bar
- Sistema de clasificación de 4 niveles de ruido con colores MD3
- Configuración inicial del proyecto en Android Studio
- Repositorio Git inicializado y conectado a GitHub

### 🔜 Próximos pasos
- Implementar `MicrophoneRecorder` con `AudioRecord`
- Construir `MonitoringFragment` con medidor dB SPL en Jetpack Compose
- Configurar Room Database con las 4 entidades del modelo ER
- Implementar `SoundViewModel` con `StateFlow`
- Construir `HistoryFragment` con filtros por nivel de ruido
- Construir `SettingsFragment` con sliders de configuración de umbrales
- Integrar sistema de alertas con `NoiseMonitorUseCase`

---

## 👨‍💻 Autor

**Esteban Aumala**
- GitHub: [@Slooptret](https://github.com/Slooptret)

---

## 📄 Licencia

Este proyecto fue desarrollado como parte de un proyecto académico universitario.

---

*Última actualización: Junio 2026*
