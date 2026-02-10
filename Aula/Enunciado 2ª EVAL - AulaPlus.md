# EXAMEN PRÁCTICO

---

## Contexto del examen

Se parte de la aplicación desarrollada durante el segundo trimestre del curso.
El objetivo del examen es realizar una serie de mejoras y ampliaciones sobre dicha aplicación, aplicando criterios de diseño de interfaces, accesibilidad, uso de Material Design y arquitectura MVVM.

No se debe crear una aplicación nueva.

---

## Ejercicio 1. Tema Material y Splash Screen (3 puntos)

Se proporciona una nueva imagen que debe utilizarse en la pantalla de inicio de la aplicación (Splash Screen).

### 1.1 Creación del tema (1,5 puntos)

* Crear un tema propio basado en Material Design.
* El tema debe ser coherente con la imagen proporcionada.Puedes verla en la carpeta res\drawables con el nombre de nuevoicono.png.
* La aplicación debe mantener coherencia visual tanto en modo claro como en modo oscuro.

### 1.2 Aplicación del tema (1 punto)

* Aplicar correctamente el tema a toda la aplicación.
* Configurar el tema específico utilizado en la Splash Screen.

### 1.3 Funcionamiento de la Splash Screen (0,5 puntos)

* La Splash Screen debe mostrarse correctamente al iniciar la aplicación con el nuevo icono, sin que desentone con los colores del nuevo tema.

### ENTREGA:

Un video mostrando el funcionamiento de la aplicación con nuevo tema, splash screen.

---

## Ejercicio 2. ProfileFragment siguiendo MVVM (4 puntos)

Se debe añadir un nuevo fragment denominado **ProfileFragment**, accesible desde el **HomeFragment** mediante un elemento de la interfaz.

### 2.1 Interfaz y navegación (1,5 puntos)

* El acceso desde el HomeFragment debe funcionar correctamente.
* La interfaz del ProfileFragment debe utilizar componentes Material.
* La disposición de los elementos debe ser clara y coherente.

### 2.2 Funcionalidad del ProfileFragment (1,5 puntos)

El ProfileFragment debe permitir:

* Mostrar información identificativa del usuario autenticado (UID de Firebase) La función necesaria está en el repositorio.
* Activar y desactivar el modo oscuro de la aplicación.
* Cerrar la sesión del usuario (logout).

### 2.3 Arquitectura MVVM (1 punto)

* El fragment debe seguir estrictamente el patrón MVVM.
* La lógica de negocio no debe residir en la vista.
* El uso de ViewModel y Repository debe ser correcto.

  ### ENTREGA:

 Un video mostrando:
 
* La navegación desde home a profile.
* Se debe probar el cambio de tema día-noche.
* UID de usuario
* Cerrar sesión funcional
  
 Capturas de pantalla del código de ProfileFragment.java ProfileViewModel.java, si hiciese falta, ProfileViewModelFactory.java y fragment_profile.xml. 
 
---

## Ejercicio 3. Accesibilidad y uso de recursos (3 puntos)

Se deben realizar las modificaciones necesarias para mejorar la accesibilidad de la aplicación y el uso correcto de los recursos.

### 3.1 Accesibilidad (1,5 punto)

* Los elementos visuales e interactivos deben ser accesibles. (ContentDescription)
* La aplicación debe cumplir criterios básicos de accesibilidad (contrastes adecuados en versión día y noche).

### 3.2 Uso de recursos de texto y tamaños adecuados (1,5 punto)

* Todo el texto visible en la aplicación debe gestionarse mediante recursos.
* No debe quedar texto literal incrustado en layouts ni en código.
* El tamaño de los widgets debe ser adecuado (podéis usar layout inspector/lint para comprobar)

 ### ENTREGA:

 Un video mostrando:
 
* Que se cumplen criterios de accesibilidad

Capturas de todos los archivos que reflejen que se han centralizado los recursos de sistema y que se cumple con accesibilidad.

---

## Resumen de puntuaciones de la parte práctica

| Ejercicio                             | Puntuación |
| ------------------------------------- | ---------- |
| Ejercicio 1. Tema y Splash Screen     | 3 puntos   |
| Ejercicio 2. ProfileFragment y MVVM   | 4 puntos   |
| Ejercicio 3. Accesibilidad y recursos | 3 puntos   |

