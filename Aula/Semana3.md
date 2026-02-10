# APUNTES – SEMANA 3

## Objetivo: *Aula+ Lite* pasa a ser una app real (Auth + sesión + Firestore + Home con datos)

### Resultado esperado al final de la semana

*  Login y Register reales con Firebase Auth (email/password).
*  Validación local + errores por campo (TextInputLayout).
*  Feedback con **Snackbar** (no Toast).
*  Loading simple (deshabilitar botones + ProgressIndicator).
*  Sesión persistente: si ya está logueado, entra directo a Home.
*  Logout desde Home y vuelve a Login bien (sin “backstack loco”).
*  Avisos guardados en Firestore por usuario (`users/{uid}/notices/{id}`).
*  Home lee avisos de Firestore y los muestra (listado simple aceptado).

---

## Requisitos previos (para toda la semana)

* Proyecto Android con:

  * **Navigation Component** (grafo + 3 fragments: Login, Register, Home).
  * **Material 3** (theme Material3 + componentes Material).
  * MVVM ya conocido (Repository + ViewModel + UI “tonta” observando estado).

---

# 1 Firebase Auth

---
### 0) Crear cuenta:

Accede a esta [url](https://firebase.google.com) y regístrate (si n ote deja usar una dirección del centro, usa una personal).


### 1) Teoría 

#### 1.1 Qué entra en DI aquí (aunque sea Firebase)

En DI no “evaluamos Firebase”, evaluamos que:

* Diseña un **flujo de pantallas coherente** (Login → Home, Register → Login).
* Da **feedback** correcto (errores y confirmaciones).
* Maneja **estados de UI**:

  * `loading` (bloquear UI mientras se procesa)
  * `error` (mensaje claro y ubicado)
  * `success` (confirmación y navegación)

#### 1.2 Firebase: Auth vs Firestore (idea clara)

* **Firebase Auth** = identidad (quién eres)

  * email/password, Google, etc.
  * te da un usuario con `uid`
* **Firestore** = datos (qué guardas)

  * avisos, tareas, registros…
  * se estructura por colecciones/documentos

**Regla mental**:

> Auth te da **UID**, Firestore guarda datos **bajo ese UID**.

#### 1.3 UX en formularios (lo mínimo obligatorio)

* Validación **local** antes de red:

  * email vacío / sin “@” (mínimo)
  * password corta (mínimo 6)
* Error **en el campo**, no en un Toast genérico.
* Mensajes claros:

  * “El email es obligatorio”
  * “La contraseña debe tener al menos 6 caracteres”
* Feedback de red:

  * credenciales inválidas / email ya en uso / sin conexión

---

### 2) Práctica guiada (Firebase setup + dependencias + base UI)

#### 2.1 Crear proyecto Firebase y conectar Android

1. Firebase Console → **Create project**
2. **Add app → Android**
3. En “Android package name” pon **exactamente** el `namespace`/`applicationId` de *Aula+ Lite* (ejemplo típico):

   * `com.example.aula` *(o el que tengas tú en el proyecto)*
4. Descarga `google-services.json`
5. Selecciona la vista `Project`en Android Studio y colócalo en:
<img width="168" height="390" alt="imagen" src="https://github.com/user-attachments/assets/e4b01e9a-ebb5-4b42-8efa-035b9dae9af8" />


* `Aula[Aula+]/app/google-services.json`

> Importante: va **dentro del módulo `app`**, no en la raíz del proyecto.

---

## 2.2 Configurar Gradle con `libs.versions.toml`

### A) Edita `gradle/libs.versions.toml`

Añade lo siguiente:

```toml
[versions]
firebaseBom = "34.8.0"
googleServices = "4.4.4"

[libraries]
firebase-bom = { module = "com.google.firebase:firebase-bom", version.ref = "firebaseBom" }
firebase-auth = { module = "com.google.firebase:firebase-auth" }
firebase-firestore = { module = "com.google.firebase:firebase-firestore" }


[plugins]
googleServices = { id = "com.google.gms.google-services", version.ref = "googleServices" }
```

**Notas prácticas:**

* `firebase-auth` y `firebase-firestore` **no llevan versión** porque la gestiona el **BOM**.
* La versión del plugin `google-services` sí va aparte.

---

### B) En `app/build.gradle` (Groovy) aplica el plugin y dependencias

En el **módulo app**:

```gradle
plugins {
    alias(libs.plugins.googleServices)
}

dependencies {
    implementation (platform(libs.firebase.bom))
    implementation (libs.firebase.auth)
    implementation (libs.firebase.firestore)
}
```

> Si tu `plugins {}` ya tiene otros aliases (por ejemplo `alias(libs.plugins.android.application)`), perfecto: lo importante es añadir el de **google-services** y las dependencias con **BOM + auth**.

---

### 3) UI Login/Register con Material3 + errores por campo

#### 3.1 Layout recomendado (fragment_login.xml)

* `TextInputLayout` + `TextInputEditText`
* Botón Material
* ProgressIndicator (simple)

Ejemplo base:

```xml
<com.google.android.material.textfield.TextInputLayout
    android:id="@+id/tilEmail"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Email"
    app:boxBackgroundMode="outline">

    <com.google.android.material.textfield.TextInputEditText
        android:id="@+id/etEmail"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:inputType="textEmailAddress" />
</com.google.android.material.textfield.TextInputLayout>

<com.google.android.material.textfield.TextInputLayout
    android:id="@+id/tilPass"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Contraseña"
    app:boxBackgroundMode="outline"
    app:endIconMode="password_toggle">

    <com.google.android.material.textfield.TextInputEditText
        android:id="@+id/etPass"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:inputType="textPassword" />
</com.google.android.material.textfield.TextInputLayout>

<com.google.android.material.button.MaterialButton
    android:id="@+id/btnLogin"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Entrar" />

<com.google.android.material.progressindicator.CircularProgressIndicator
    android:id="@+id/progress"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:visibility="gone"
    android:indeterminate="true" />
```

#### 3.2 Reglas DI para errores

* `tilEmail.setError("...")` si error en email
* `tilPass.setError("...")` si error en password
* limpiar errores cuando el usuario corrige:

  * al pulsar botón, **primero** `til.setError(null)`

---

### PRÁCTICA INCREMENTAL 

**Objetivo**: dejar preparada la base de Firebase y la UI profesional lista para Auth.

Checklist alumno:

*  Firebase creado + app añadida
*  `google-services.json` en `app/`
*  Dependencias + plugin puestos
*  LoginFragment con `TextInputLayout` (email + pass), botón y progress
*  RegisterFragment equivalente (email + pass + repetir pass opcional)
*  Navegación entre login y register funcionando


---

---

# 2  Registro/Login reales + Snackbar + loading

### 1) Teoría 

* Auth es **asíncrono**: tarda, puede fallar.
* Si tarda, el usuario necesita:

  * botón deshabilitado
  * indicador de progreso
* El error debe ser:

  * claro
  * no repetitivo
  * sin “spamear” toasts: mejor Snackbar

---

### 2) Implementación MVVM para Auth (recomendado)

Vamos a crear:

* `AuthRepository`
* `AuthViewModel`
* Estado observable:

  * `loading`
  * `errorMessage` (estado)
  * `navEvent` (evento one-shot)

#### 2.1 AuthRepository (data)

`com.example.aula.data.AuthRepository`

```java
package com.example.aula.data;

import com.google.firebase.auth.FirebaseAuth;

public class AuthRepository {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public FirebaseAuth getAuth() {
        return auth;
    }
}
```

> Repository mínimo: de momento solo centraliza el `FirebaseAuth`.

#### 2.2 AuthViewModel (viewmodel)

`com.example.aula.viewmodel.AuthViewModel`

```java
package com.example.aula.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.aula.data.AuthRepository;
import com.google.firebase.auth.FirebaseAuth;

public class AuthViewModel extends ViewModel {

    private final FirebaseAuth auth;

    private final MutableLiveData<Boolean> _loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> _errorMessage = new MutableLiveData<>(null);

    // Evento de navegación one-shot: "HOME", "LOGIN", etc.
    private final MutableLiveData<String> _navEvent = new MutableLiveData<>(null);

    public AuthViewModel(AuthRepository repo) {
        this.auth = repo.getAuth();
    }

    public LiveData<Boolean> getLoading() { return _loading; }
    public LiveData<String> getErrorMessage() { return _errorMessage; }
    public LiveData<String> getNavEvent() { return _navEvent; }

    public void consumeNavEvent() { _navEvent.setValue(null); }

    public void login(String email, String pass) {
        _errorMessage.setValue(null);

        _loading.setValue(true);
        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    _loading.setValue(false);
                    if (task.isSuccessful()) {
                        _navEvent.setValue("HOME");
                    } else {
                        _errorMessage.setValue(parseAuthError(task.getException()));
                    }
                });
    }

    public void register(String email, String pass) {
        _errorMessage.setValue(null);

        _loading.setValue(true);
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {
                    _loading.setValue(false);
                    if (task.isSuccessful()) {
                        _navEvent.setValue("HOME");
                    } else {
                        _errorMessage.setValue(parseAuthError(task.getException()));
                    }
                });
    }

    private String parseAuthError(Exception e) {
        if (e == null) return "Error desconocido";
        String msg = e.getMessage();
        // Simplificación didáctica: mapeo básico por texto
        if (msg != null) {
            if (msg.contains("password") && msg.contains("invalid")) return "Credenciales inválidas";
            if (msg.contains("no user record")) return "No existe una cuenta con ese email";
            if (msg.contains("email address is already in use")) return "Ese email ya está registrado";
            if (msg.contains("network error")) return "Error de red. Revisa tu conexión";
        }
        return "No se pudo completar la operación";
    }
}
```

> Esto no es “perfecto” (lo ideal sería mapear con códigos), pero para DI es suficiente y claro.

#### 2.3 Factory (porque el VM recibe repo)

`AuthViewModelFactory`

```java
package com.example.aula.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.aula.data.AuthRepository;

public class AuthViewModelFactory implements ViewModelProvider.Factory {

    private final AuthRepository repo;

    public AuthViewModelFactory(AuthRepository repo) {
        this.repo = repo;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(repo);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
```

---

### 3) LoginFragment: validación local + observar estado + navegar

Ejemplo (puntos clave):

```java
public class LoginFragment extends Fragment {

    private AuthViewModel vm;

    public LoginFragment() { super(R.layout.fragment_login); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextInputLayout tilEmail = view.findViewById(R.id.tilEmail);
        TextInputLayout tilPass = view.findViewById(R.id.tilPass);
        TextInputEditText etEmail = view.findViewById(R.id.etEmail);
        TextInputEditText etPass = view.findViewById(R.id.etPass);

        MaterialButton btnLogin = view.findViewById(R.id.btnLogin);
        MaterialButton btnGoRegister = view.findViewById(R.id.btnGoRegister);
        CircularProgressIndicator progress = view.findViewById(R.id.progress);

        AuthRepository repo = new AuthRepository();
        AuthViewModelFactory factory = new AuthViewModelFactory(repo);
        vm = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        // OBSERVAR loading
        vm.getLoading().observe(getViewLifecycleOwner(), isLoading -> {
            boolean loading = Boolean.TRUE.equals(isLoading);
            btnLogin.setEnabled(!loading);
            btnGoRegister.setEnabled(!loading);
            if (loading) {
                progress.show();
            } else {
                progress.hide();
            }
        });

        // OBSERVAR error (Snackbar)
        vm.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
            }
        });

        // OBSERVAR navegación one-shot
        vm.getNavEvent().observe(getViewLifecycleOwner(), event -> {
            if (event == null) return;
            if (event.equals("HOME")) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_login_to_home);
            }
            vm.consumeNavEvent();
        });

        btnGoRegister.setOnClickListener(v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_login_to_register)
        );

        btnLogin.setOnClickListener(v -> {
            // 1) limpiar errores
            tilEmail.setError(null);
            tilPass.setError(null);

            // 2) validar local
            String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
            String pass = etPass.getText() != null ? etPass.getText().toString() : "";

            boolean ok = true;
            if (email.isEmpty()) { tilEmail.setError("Email obligatorio"); ok = false; }
            else if (!email.contains("@")) { tilEmail.setError("Email no válido"); ok = false; }

            if (pass.isEmpty()) { tilPass.setError("Contraseña obligatoria"); ok = false; }
            else if (pass.length() < 6) { tilPass.setError("Mínimo 6 caracteres"); ok = false; }

            if (!ok) return;

            // 3) llamar al VM
            vm.login(email, pass);
        });
    }
}
```

#### RegisterFragment

Es igual, pero llama a `vm.register(...)` y tendrá botón “Volver a login”.

---

### PRÁCTICA INCREMENTAL 

**Objetivo**: Login y Register reales con buen UX.

Checklist alumno:

*  AuthViewModel + Factory + Repo
*  Validación local (email y pass)
*  Error por campo con `TextInputLayout`
*  `Snackbar` para error remoto
*  Loading: deshabilitar botones + mostrar progress
*  Registro navega a Home si OK
*  Login navega a Home si OK

**Comprobar:**

* intento fallido (validación local)
* intento fallido (Firebase)
* intento correcto (navega a Home)
* loading visible

---

# 3 Sesión iniciada + navegación correcta

---

### 1) Teoría: “Mantener sesión iniciada”

Firebase Auth guarda sesión. Si el usuario ya está autenticado:

* `FirebaseAuth.getInstance().getCurrentUser()` **no es null**
* entonces **no debes mostrar Login** otra vez

**Regla DI**:

> La pantalla inicial depende del estado (logueado/no logueado).

### 2) Problema real: startDestination fijo

Tu `nav_graph` tiene `startDestination=loginFragment`.
Eso fuerza Login siempre.

Soluciones típicas:

1. **SplashFragment** (o “LauncherFragment”) que decide y redirige.  Didáctica y limpia
2. Cambiar startDestination dinámicamente (más avanzado)

Vamos con la opción 1: **AuthGateFragment** (pantalla invisible/rápida)

---

### 3) Práctica guiada: AuthGateFragment

#### 3.1 Grafo

Añade fragment `authGateFragment` como startDestination:

```xml
app:startDestination="@id/authGateFragment"
```

Y en el navigation:

```xml
<fragment
    android:id="@+id/authGateFragment"
    android:name="com.example.aula.ui.fragments.AuthGateFragment"
    android:label="AuthGate">
    <action
        android:id="@+id/action_gate_to_login"
        app:destination="@id/loginFragment"
        app:popUpTo="@id/authGateFragment"
        app:popUpToInclusive="true"/>

    <action
        android:id="@+id/action_gate_to_home"
        app:destination="@id/homeFragment"
        app:popUpTo="@id/authGateFragment"
        app:popUpToInclusive="true"/>
</fragment>
```

> `popUpToInclusive=true` para que el gate no quede en backstack.

#### 3.2 Layout del gate

Puede ser un layout vacío con un progress:

`fragment_auth_gate.xml` con un `CircularProgressIndicator` centrado.

#### 3.3 Código AuthGateFragment

```java
public class AuthGateFragment extends Fragment {

    public AuthGateFragment() { super(R.layout.fragment_auth_gate); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_gate_to_home);
        } else {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_gate_to_login);
        }
    }
}
```

---

### 4) Evitar volver atrás a Login tras entrar

Si entras a Home, **el botón atrás no debería volver a Login** en un flujo normal.

La forma sencilla: usar `popUpTo` al navegar a Home desde Login/Register:

En acciones `login_to_home` y `register_to_home`:

```xml
<action
    android:id="@+id/action_login_to_home"
    app:destination="@id/homeFragment"
    app:popUpTo="@id/loginFragment"
    app:popUpToInclusive="true"/>
```

Y análogo en `register_to_home`.

---

### PRÁCTICA INCREMENTAL 


*  AuthGateFragment creado y es startDestination
*  Si hay sesión: entra a Home directo
*  Si no: entra a Login
*  Navegar a Home hace `popUpToInclusive` para no volver atrás

Mini prueba:

* login OK → Home → botón atrás → **sale de app o vuelve donde toque**, pero no a Login
* cerrar y abrir app → entra directo a Home si sigue autenticado

---

---

## 4  Pulir UX + logout correcto

### 1) Mejorar UX no es “decoración”

Pulir UX es:

* error donde ocurre (campo)
* mensajes precisos
* flujo consistente (volver, cancelar)
* logout “limpio” (sin rutas raras)

---

### 2) Práctica

#### 2.1 Errores en campo correcto

En Login/Register:

* si email vacío → `tilEmail.setError(...)`
* si password corta → `tilPass.setError(...)`
* **no uses Snackbar para errores locales**, úsalo para errores de servidor/red.

#### 2.2 Mensajes claros (DI)

Cambia mensajes genéricos tipo “Error” por:

* “Credenciales inválidas”
* “No existe una cuenta con ese email”
* “Email ya registrado”
* “Error de red”

#### 2.3 Botones “Ir a registro / Volver a login”

* Login: “Crear cuenta”
* Register: “Ya tengo cuenta” (vuelve a login)

#### 2.4 Logout desde Home

Botón simple “Cerrar sesión”.

* Acción: `FirebaseAuth.getInstance().signOut()`
* Navegar a Login (pero mejor: a AuthGate para que decida)

**Recomendación**: navegar a `authGateFragment` con `popUpTo` a root.

Ejemplo desde HomeFragment:

```java
btnLogout.setOnClickListener(v -> {
    FirebaseAuth.getInstance().signOut();
    NavHostFragment.findNavController(this)
            .navigate(R.id.action_home_to_authGate);
});
```

Y en el grafo defines `action_home_to_authGate` con:

```xml
<action
    android:id="@+id/action_home_to_authGate"
    app:destination="@id/authGateFragment"
    app:popUpTo="@id/nav_graph"
    app:popUpToInclusive="true"/>
```

> Así “reinicias” el flujo.

---

### Comprobar:

* App entra directa a Home si hay sesión.
* Logout vuelve a Login correctamente.
* Botón atrás no crea rutas raras.

---
