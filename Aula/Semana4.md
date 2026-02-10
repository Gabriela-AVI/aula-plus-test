#  SEMANA 4 · LUNES

## Material Design profesional: **Tema importado + aplicación real**

---

## 0. Contexto y objetivo del día

Hasta ahora, *Aula+ Lite*:

* Usa **Material Design**, pero:

  * con colores por defecto,
  * sin identidad visual propia,
  * sin coherencia global controlada.
* Los componentes son Material, pero **el diseño no está centralizado**.

En una app real:

> **La identidad visual NO se decide pantalla a pantalla.
> Se define en un tema global.**

El objetivo de esta sesión es **profesionalizar la interfaz**, sin tocar lógica ni Firebase.

---

## 1. Qué es realmente un tema en Android (idea clara)

Un **tema** define de forma global:

* Colores principales
* Colores secundarios
* Fondo
* Colores de texto
* Apariencia de botones, inputs, diálogos, etc.

Un tema **NO es un layout**
Un tema **NO es un drawable**
Un tema **NO es código Java**

Un tema es un **conjunto de recursos** que Android aplica automáticamente.

---

## 2. Material Design 3 y por qué usamos Theme.Material3.DayNight

En este proyecto usamos **Material Design 3 (Material You)**.

Características clave:

* Componentes modernos
* Mejor accesibilidad
* Soporte nativo de **modo claro / oscuro**
* Colores derivados automáticamente

### Regla DI importante

> Si la app no soporta Dark Mode correctamente,
> **la interfaz no es profesional**, aunque “funcione”.

---

## 3. Generar un tema Material 3

Android Studio **NO es la herramienta adecuada** para diseñar un tema.

Google proporciona una herramienta específica:

 **Material Theme Builder (Material 3)**
[https://m3.material.io/theme-builder](https://m3.material.io/theme-builder)

---

### 3.1 Qué hacemos en el Theme Builder

1. Elegir **Material 3**
2. Definir:

   * Color primario
   * Color secundario
   * (opcional) color terciario
3. Comprobar:

   * Contraste
   * Legibilidad
4. Exportar para **Android (XML)**

> No se evalúa estética, se evalúa **coherencia y legibilidad**.

---

## 4. Archivos que genera un tema Material

Al exportar, el Theme Builder genera varios archivos XML.

Los importantes para nosotros son:

* `colors.xml`
* `themes.xml`
* `themes.xml` (night)

No hace falta entenderlos al 100%, pero **sí saber para qué sirven**.

---

## 5. Importar el tema en el proyecto Android

### 5.1 Dónde van los archivos

Copiar los archivos exportados en:

```
app/src/main/res/values/
app/src/main/res/values-night/
```

Normalmente:

* `values/colors.xml`
* `values/themes.xml`
* `values-night/themes.xml`

 **No mezclar** con otros proyectos
 
 **No renombrar recursos sin saber lo que se hace**

---

## 6. El archivo `themes.xml` (clave del día)

Ejemplo típico (simplificado):

```xml
<resources>

    <style name="Theme.AulaPlus"
        parent="Theme.Material3.DayNight.NoActionBar">

        <!-- Colores principales -->
        <item name="colorPrimary">@color/md_theme_primary</item>
        <item name="colorOnPrimary">@color/md_theme_onPrimary</item>

        <item name="colorSecondary">@color/md_theme_secondary</item>
        <item name="colorOnSecondary">@color/md_theme_onSecondary</item>

        <item name="colorBackground">@color/md_theme_background</item>
        <item name="colorOnBackground">@color/md_theme_onBackground</item>

    </style>

</resources>
```

### Ideas clave

* **parent**: define el comportamiento base (Material3 + DayNight)
* Los colores vienen de `colors.xml`
* Android aplica esto **a toda la app**

---

## 7. Aplicar el tema a la aplicación

Un tema **no hace nada** si no se aplica.

### 7.1 AndroidManifest.xml

En la etiqueta `<application>`:

```xml
<application
    android:theme="@style/Theme.AulaPlus"
    ... >
```

O, si se quiere solo para una Activity:

```xml
<activity
    android:name=".ui.MainActivity"
    android:theme="@style/Theme.AulaPlus" />
```

 En *Aula+ Lite*, lo correcto es aplicarlo **a la aplicación completa**.

---

## 8. Verificación inmediata (obligatoria)

Tras aplicar el tema:

* Botones Material cambian de color
* Inputs (`TextInputLayout`) usan los nuevos colores
* Snackbar respeta el esquema de color
* Fondo y textos son coherentes

Si **no cambia nada**, algo está mal:

* Tema no aplicado
* Colores no referenciados
* Error en nombres

---

## 9. Tema ≠ Layout (error común)

Cambiar el tema:

* **NO rompe MVVM**
* **NO rompe Firebase**
* **NO rompe Navigation**
* **NO obliga a tocar Java**

Esto es **diseño de interfaces**, no lógica.

---

## 10. PRÁCTICA INCREMENTAL

### Objetivo

Dotar a *Aula+ Lite* de una **identidad visual propia**, coherente y profesional.

### Tareas obligatorias

1. Generar un tema Material 3 con Theme Builder
2. Importar los archivos XML al proyecto
3. Crear el estilo `Theme.AulaPlus`
4. Aplicarlo a la aplicación
5. Ejecutar y verificar cambios visibles

---

## 11. Pulir la UI con el tema aplicado (sin añadir lógica)

Con el tema ya activo, revisamos pantallas:

### Login / Register

* `TextInputLayout`:

  * label visible
  * error legible
* Botón principal claramente reconocible
* Colores con contraste suficiente

### Home

* Lista visible sobre fondo correcto
* Texto legible en modo claro
* Snackbar con color coherente

---

## 12. Comprobaciones finales (obligatorias)

El alumno debe comprobar:

* La app **no ha perdido funcionalidad**
* Login y Register siguen funcionando
* Navigation sigue correcta
* Firebase no se ha tocado
* El cambio es **solo visual**


---

## Splash Screen profesional + Dark Mode + cambio de modo desde la app

---

## 0. Contexto del día

*Aula+ Lite* ya dispone de:

* Un **tema Material 3 propio**, importado correctamente.
* Identidad visual coherente y centralizada.
* Componentes Material aplicados sin colores hardcodeados.
* Base preparada para **modo claro / oscuro**.

En esta sesión se trabajan **dos aspectos clave del diseño de interfaces móviles profesionales**:

1. **Pantalla de arranque real (Splash Screen oficial)**
2. **Gestión correcta del modo oscuro**
3. **Control manual del modo Día/Noche desde la aplicación**

Todo lo visto hoy afecta **exclusivamente a la interfaz**, no a la lógica de negocio.

---

## 1. Splash Screen: qué es y qué NO es

### 1.1 Qué NO es un Splash Screen profesional

No se considera profesional:

* Una Activity extra con una imagen
* Un `Handler.postDelayed()`
* Una pantalla falsa que “espera” unos segundos
* Lógica manual para simular una carga

Ese enfoque está **obsoleto** y penaliza UX.

---

### 1.2 Qué es un Splash Screen profesional

Desde Android 12 (API 31), Google define un **sistema oficial**:

 **SplashScreen API**

Características:

* Gestionado por el sistema
* Instantáneo
* Sin Activities extra
* Sin retrasos artificiales
* Integrado con el tema de la aplicación

> El Splash Screen **no es una pantalla**,
> es una **fase visual del arranque de la app**.

---

## 2. Funcionamiento del Splash Screen API

El sistema realiza automáticamente:

1. Arranque de la aplicación
2. Muestra del Splash definido en el tema
3. Carga de la Activity real
4. Transición automática al contenido

Todo esto ocurre **sin lógica compleja ni layouts propios**.

---

## 3. Dependencia necesaria

En `app/build.gradle`:

```gradle
dependencies {
    implementation "androidx.core:core-splashscreen:1.0.1"
}
```

Si ya existe, **no se duplica**.

---

## 4. Tema específico para el Splash Screen

El Splash **no usa el tema general de la app**.
Necesita un **tema exclusivo**, cuyo único propósito es el arranque.

### 4.1 Definición del tema Splash

En `res/values/themes.xml`:

```xml
<style name="Theme.AulaPlus.Splash"
    parent="Theme.SplashScreen">

    <item name="windowSplashScreenBackground">
        @color/md_theme_primary
    </item>

    <item name="windowSplashScreenAnimatedIcon">
        @drawable/ic_launcher_foreground
    </item>

    <item name="postSplashScreenTheme">
        @style/Theme.AulaPlus
    </item>

</style>
```

### Idea clave

* El Splash **no define layouts**
* Todo es **configuración de tema**
* El sistema aplica automáticamente el tema real tras el arranque

---

## 5. Aplicar el tema Splash

En `AndroidManifest.xml`, solo a la Activity inicial:

```xml
<activity
    android:name=".ui.MainActivity"
    android:theme="@style/Theme.AulaPlus.Splash">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

---

## 6. Código mínimo en MainActivity

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    SplashScreen.installSplashScreen(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
}
```

No es necesario añadir más código.

---

## 7. Verificación del Splash Screen

Al ejecutar la app:

* Se muestra el icono
* Fondo con color del tema
* Transición limpia a Login / AuthGate
* Sin pantallas en blanco ni retardos

Si hay parpadeos, la configuración es incorrecta.

---

## 8. Dark Mode: concepto correcto

### 8.1 Qué NO es Dark Mode

No es correcto:

* Cambiar colores manualmente
* Usar `if/else` en Java
* Duplicar layouts

Eso rompe la escalabilidad y la coherencia visual.

---

### 8.2 Dark Mode correcto en Android

Se basa en:

* `Theme.Material3.DayNight`
* Recursos alternativos en `values-night`
* Decisión centralizada por el sistema

---

## 9. Tema base preparado para Dark Mode

En `themes.xml`:

```xml
<style name="Theme.AulaPlus"
    parent="Theme.Material3.DayNight.NoActionBar">
```

Esto es **obligatorio**.

---

## 10. Archivo `values-night/themes.xml`

```xml
<resources>

    <style name="Theme.AulaPlus"
        parent="Theme.Material3.DayNight.NoActionBar">

        <item name="colorPrimary">@color/md_theme_primary</item>
        <item name="colorOnPrimary">@color/md_theme_onPrimary</item>

        <item name="colorBackground">@color/md_theme_background</item>
        <item name="colorOnBackground">@color/md_theme_onBackground</item>

    </style>

</resources>
```

Android aplica automáticamente estos valores cuando el sistema está en modo oscuro.

---

## 11. Comprobación del Dark Mode

Pruebas obligatorias:

* Texto legible
* Fondos correctos
* Botones visibles
* Snackbar con contraste
* Inputs claros

Si algo no se ve bien, **se corrige en el tema**, no en Java.

---

# 12. Cambio manual de modo Día / Noche desde la app

Hasta ahora, el modo oscuro dependía **solo del sistema**.
Ahora añadimos una mejora profesional:

> **El usuario puede elegir el modo desde la aplicación.**

Esto es habitual en apps reales y perfectamente compatible con Material 3.

---

## 13. Cómo se controla el modo en Android

Android expone una API global:

```java
AppCompatDelegate.setDefaultNightMode(...)
```

Valores:

```java
MODE_NIGHT_YES
MODE_NIGHT_NO
MODE_NIGHT_FOLLOW_SYSTEM
```

Características importantes:

* El cambio es **global**
* La Activity se **recrea automáticamente**
* El ViewModel **no pierde estado**

---

## 14. Arquitectura correcta para esta funcionalidad

 No guardar el modo en la Activity
 No cambiar colores manualmente

 Enfoque correcto:

* La UI lanza el evento
* La preferencia se guarda
* Android aplica el tema adecuado

---

## 15. Repositorio de preferencias

`com.example.aula.data.SettingsRepository`

```java
public class SettingsRepository {

    private static final String PREFS_NAME = "settings";
    private static final String KEY_DARK_MODE = "dark_mode";

    private final SharedPreferences prefs;

    public SettingsRepository(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setDarkMode(boolean enabled) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public boolean isDarkModeEnabled() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }
}
```

---

## 16. ViewModel de ajustes

```java
public class SettingsViewModel extends ViewModel {

    private final SettingsRepository repo;
    private final MutableLiveData<Boolean> _darkMode = new MutableLiveData<>();

    public SettingsViewModel(SettingsRepository repo) {
        this.repo = repo;
        _darkMode.setValue(repo.isDarkModeEnabled());
    }

    public LiveData<Boolean> getDarkMode() {
        return _darkMode;
    }

    public void setDarkMode(boolean enabled) {
        repo.setDarkMode(enabled);
        _darkMode.setValue(enabled);
    }
}
```

---

## 17. Switch Material en la UI

Ejemplo:

```xml
<com.google.android.material.materialswitch.MaterialSwitch
    android:id="@+id/switchDarkMode"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Modo oscuro" />
```

---

## 18. Conexión del Switch con el ViewModel

```java
switchDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
    vm.setDarkMode(isChecked);

    AppCompatDelegate.setDefaultNightMode(
        isChecked
            ? AppCompatDelegate.MODE_NIGHT_YES
            : AppCompatDelegate.MODE_NIGHT_NO
    );
});
```

---

## 19. Aplicar la preferencia al arrancar la app

En `MainActivity`, **antes de `setContentView`**:

```java
SettingsRepository repo = new SettingsRepository(this);

AppCompatDelegate.setDefaultNightMode(
    repo.isDarkModeEnabled()
        ? AppCompatDelegate.MODE_NIGHT_YES
        : AppCompatDelegate.MODE_NIGHT_NO
);
```

---

## 20. Pruebas obligatorias

* Cambiar el switch → la app cambia de modo
* La app se recrea sin perder estado
* Cerrar y abrir → se mantiene el modo elegido
* Firebase y navegación siguen funcionando

---

## 21. PRÁCTICA INCREMENTAL

### Obligatoria

1. Implementar Splash Screen oficial
2. Verificar Dark Mode automático
3. Añadir switch Día/Noche
4. Guardar preferencia del usuario
5. Aplicar el modo al arrancar la app


---


# MARTES 

## Etiquetado semántico, comprensión de la UI y uso correcto de `strings.xml`

---

## 0. Contexto general

*Aula+ Lite* se ha construido siguiendo buenas prácticas de arquitectura, diseño y usabilidad:

* Componentes **Material Design 3**
* Tema propio centralizado
* Soporte **Day / Night**
* Navegación coherente
* Pantallas funcionales y conectadas a datos reales

En este punto, la aplicación **funciona correctamente**, pero una interfaz profesional no se evalúa únicamente por su funcionamiento, sino por:

* su **claridad**
* su **consistencia**
* su **capacidad de ser entendida por cualquier usuario**, independientemente del modo de interacción

Este bloque introduce los **fundamentos de accesibilidad en Android**, centrados en el **significado semántico de los elementos visuales** y en cómo Android interpreta una interfaz más allá de lo que se ve en pantalla.

---

## 1. Accesibilidad en Android: concepto fundamental

Android no “ve” la interfaz como una persona.

Una persona interpreta:

* colores
* iconos
* posición
* jerarquía visual

Android, en cambio, necesita:

* **texto**
* **roles**
* **descripciones**
* **orden lógico**

Si estos elementos no están correctamente definidos, tecnologías como lectores de pantalla, sistemas de navegación por foco o herramientas de análisis **no pueden interpretar la interfaz correctamente**.

Por eso, la accesibilidad **no es una funcionalidad extra**, sino una **parte del diseño de interfaces**.

---

## 2. Qué es `contentDescription`

`android:contentDescription` es un atributo que permite asignar a un elemento visual un **significado textual** que no está necesariamente visible en pantalla.

Este significado se utiliza para:

* describir iconos
* explicar la función de botones sin texto
* aportar contexto a imágenes informativas
* permitir que tecnologías de asistencia interpreten la interfaz

Es importante entender que:

> `contentDescription` **no describe cómo se ve algo**,
> describe **qué representa o qué acción realiza**.

---

## 3. Qué es `strings.xml` y por qué es obligatorio usarlo

### 3.1 Qué es `strings.xml`

`strings.xml` es un archivo de recursos situado en:

```
res/values/strings.xml
```

Su función es **centralizar todos los textos de la aplicación** en un único lugar, en lugar de escribirlos directamente en layouts o código.

Ejemplo:

```xml
<resources>
    <string name="app_name">Aula+</string>
    <string name="cd_add_notice">Añadir aviso</string>
    <string name="cd_logout">Cerrar sesión</string>
</resources>
```

---

### 3.2 Para qué se usa `strings.xml`

Se utiliza para:

* mantener **coherencia textual**
* facilitar **cambios globales**
* preparar la app para **traducciones**
* evitar textos duplicados o inconsistentes
* separar **contenido** de **estructura**

En interfaces profesionales, **ningún texto se escribe “a mano” en el layout** sin pasar por `strings.xml`.

---

### 3.3 Relación con accesibilidad

Las descripciones de accesibilidad **también son texto**, por lo que:

* deben estar centralizadas
* deben ser reutilizables
* deben poder revisarse fácilmente

Por eso, **todas las `contentDescription` deben definirse en `strings.xml`**.

---

## 4. Cuándo es obligatorio usar `contentDescription`

### 4.1 Elementos interactivos sin texto visible

Si un elemento:

* es **clicable**
* realiza una **acción**
* y **no muestra texto visible**

entonces **debe** tener una `contentDescription`.

Ejemplo: botón de añadir con icono.

```xml
<ImageButton
    android:id="@+id/btnAddNotice"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:src="@drawable/ic_add"
    android:contentDescription="@string/cd_add_notice"
    android:background="?attr/selectableItemBackgroundBorderless" />
```

Este atributo permite que el sistema comunique claramente la acción al usuario.

---

### 4.2 FloatingActionButton (FAB)

El FAB es un caso muy común y **obligatorio** de describir, ya que su función suele ser crítica.

```xml
<com.google.android.material.floatingactionbutton.FloatingActionButton
    android:id="@+id/fabAdd"
    app:srcCompat="@drawable/ic_add"
    android:contentDescription="@string/cd_add_notice" />
```

Sin esta descripción, el botón existe visualmente, pero **no semánticamente**.

---

## 5. Cuándo NO usar `contentDescription`

### 5.1 Elementos con texto visible

Si un componente ya muestra texto visible (por ejemplo, un botón con `android:text`), ese texto **ya actúa como etiqueta semántica**.

Incorrecto:

```xml
<Button
    android:text="Cerrar sesión"
    android:contentDescription="Cerrar sesión" />
```

Esto genera **duplicación innecesaria**.

Correcto:

```xml
<Button
    android:text="@string/logout" />
```

---

### 5.2 Imágenes puramente decorativas

Una imagen decorativa:

* no aporta información funcional
* no representa una acción
* no cambia el significado de la pantalla

Debe indicarse explícitamente que **no es relevante para accesibilidad**.

```xml
<ImageView
    android:src="@drawable/bg_wave"
    android:contentDescription="@null"
    android:importantForAccessibility="no" />
```

Esto evita que la imagen:

* reciba foco
* sea anunciada
* interfiera en la navegación

---

## 6. Cómo escribir buenas descripciones (criterio profesional)

### 6.1 Describir la acción, no el elemento gráfico

Mal:

* “Icono de suma”
* “Imagen de logout”
* “Botón redondo”

Bien:

* “Añadir aviso”
* “Cerrar sesión”
* “Abrir ajustes”

La descripción debe responder a:

> ¿Qué ocurre si interactúo con esto?

---

### 6.2 Lenguaje claro y neutral

Buenas prácticas:

* usar verbos en infinitivo
* evitar tecnicismos
* evitar referencias visuales (“izquierda”, “icono”)
* mantener consistencia terminológica

Ejemplo:

```xml
<string name="cd_open_settings">Abrir ajustes</string>
```

---

## 7. Casos habituales en *Aula+ Lite*

### 7.1 Botón de logout en la barra superior

```xml
<ImageButton
    android:id="@+id/btnLogout"
    android:src="@drawable/ic_logout"
    android:contentDescription="@string/cd_logout"
    android:background="?attr/selectableItemBackgroundBorderless" />
```

---

### 7.2 Imágenes en elementos de lista

En un `RecyclerView`:

* Si la imagen **representa información**, se describe.
* Si es solo decorativa, se excluye.

Decorativa:

```xml
<ImageView
    android:src="@drawable/ic_notice"
    android:contentDescription="@null"
    android:importantForAccessibility="no" />
```

---

## 8. Relación con los estados de interfaz

Una interfaz no siempre muestra contenido final. Puede encontrarse en estados como:

* carga
* sin datos
* error
* contenido disponible

Cada estado debe ser **comprensible sin depender solo de lo visual**.

Ejemplo correcto en estado de carga:

```xml
<TextView
    android:text="Cargando avisos…"
    android:visibility="visible" />
```

El texto aporta significado semántico que un indicador gráfico no transmite por sí solo.

---

## 9. Errores comunes a evitar

* Usar `contentDescription` para describir colores o formas
* Repetir textos visibles
* Dejar iconos interactivos sin describir
* Hacer accesibles elementos puramente decorativos
* Escribir descripciones vagas o genéricas

---

## 10. Criterio final de calidad

Una interfaz bien diseñada:

* comunica intención
* no depende exclusivamente del aspecto visual
* puede ser interpretada correctamente por el sistema

El etiquetado semántico es una **responsabilidad del diseño de interfaces**, no de la lógica ni de la arquitectura de la aplicación.

Este trabajo sienta la base para la **auditoría de accesibilidad y calidad** del siguiente bloque, donde se validará la interfaz mediante herramientas automáticas y revisión sistemática.

---

## Práctica · Accesibilidad básica y etiquetado semántico

**Obligatoria**

Implementar mejoras de accesibilidad en *Aula+ Lite* sin modificar la lógica de negocio.

---

**Obligatoria**

* Identificar elementos interactivos sin texto visible en todas las pantallas.
* Sustituye los botones de `Añadir aviso` por un FAB (Floating Action Button).
* Añadir `contentDescription` donde proceda.
* Centralizar todas las descripciones en `strings.xml`.
* Eliminar duplicaciones entre texto visible y `contentDescription`.
* Verificar que los estados de carga, error o lista vacía incluyen información textual comprensible.

---



