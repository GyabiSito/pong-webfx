# Pong WebFX

[Jugá acá!](https://pong.gyabisito.dev/)

Versión web del juego **Pong**, implementado en Java y compilado a JavaScript mediante **WebFX + TeaVM**.

El objetivo del proyecto es portar un Pong hecho originalmente con JavaFX a una versión ejecutable en navegador, manteniendo la mayor parte posible de la lógica de juego en Java.

## Descripción

Este proyecto implementa un Pong con arquitectura separada por capas:

* **Dominio:** modelos, reglas, colisiones, puntuación y dificultad.
* **Aplicación:** caso de uso principal del juego, estado de entrada y snapshots.
* **Infraestructura WebFX:** renderizado con JavaFX/WebFX, entrada por teclado y loop de juego.
* **Target Web:** compilación a JavaScript usando TeaVM.

El juego se ejecuta en navegador a partir del módulo `pong-teavm-js`.

## Tecnologías utilizadas

* Java
* Maven
* JavaFX API
* WebFX
* TeaVM
* HTML / JavaScript generado
* Python HTTP Server para prueba local

## Estructura del proyecto

```text
pong-webfx/
├── pom.xml
├── webfx.xml
├── pong/
│   ├── pom.xml
│   ├── webfx.xml
│   └── src/
│       └── main/
│           ├── java/
│           │   ├── module-info.java
│           │   └── com/taller/pong/
│           │       ├── Main.java
│           │       ├── application/
│           │       ├── domain/
│           │       │   ├── constants/
│           │       │   ├── model/
│           │       │   └── rules/
│           │       └── infrastructure/webfx/
│           └── resources/
├── pong-teavm-js/
│   ├── pom.xml
│   └── src/
├── pong-openjfx/
├── pong-gwt/
├── pong-gluon/
└── pong-teavm-wasm/
```

## Módulos principales

### `pong`

Contiene el código fuente principal del juego.

Incluye:

* lógica de dominio;
* reglas de colisión;
* control de puntuación;
* estado del juego;
* entrada de teclado;
* renderizado usando WebFX/JavaFX;
* clase principal de la aplicación.

### `pong-teavm-js`

Target web del proyecto.

Compila el módulo `pong` a JavaScript usando TeaVM. Es el módulo que se usa para generar la versión navegable del juego.

### Otros módulos generados

WebFX también genera otros targets:

* `pong-openjfx`
* `pong-gwt`
* `pong-gluon`
* `pong-teavm-wasm`

Para este proyecto, el target utilizado principalmente es:

```text
pong-teavm-js
```

## Requisitos

Se necesita tener instalado:

* Java compatible con el proyecto
* Maven
* WebFX CLI
* Python, solo para levantar un servidor local simple

Comandos útiles para verificar:

```powershell
java -version
mvn -version
webfx --help
python --version
```

## Compilar el proyecto para web

Desde la raíz del proyecto:

```powershell
cd C:\Users\Josec\OneDrive\Escritorio\pong-webfx
```

Ejecutar:

```powershell
mvn clean package -Pteavm-js -pl pong-teavm-js -am
```

El parámetro importante es:

```powershell
-Pteavm-js
```

Ese profile activa la compilación TeaVM real y genera el JavaScript necesario para ejecutar el juego en navegador.

Sin ese profile, Maven puede generar el `index.html`, pero no necesariamente genera `classes.js`, provocando errores como:

```text
GET /classes.js 404
Uncaught ReferenceError: main is not defined
```

## Ejecutar en navegador

Después de compilar correctamente, entrar a la carpeta generada:

```powershell
cd .\pong-teavm-js\target\pong-teavm-js-1.0.0
```

Levantar un servidor local:

```powershell
python -m http.server 8080
```

Abrir en el navegador:

```text
http://localhost:8080
```

## Comando completo de ejecución local

Desde la raíz del proyecto:

```powershell
mvn clean package -Pteavm-js -pl pong-teavm-js -am
cd .\pong-teavm-js\target\pong-teavm-js-1.0.0
python -m http.server 8080
```

Luego abrir:

```text
http://localhost:8080
```

## Clase principal

La aplicación WebFX utiliza como clase principal:

```java
com.taller.pong.infrastructure.webfx.WebFxGameApp
```

El archivo `Main.java` delega el arranque a JavaFX/WebFX:

```java
package com.taller.pong;

import com.taller.pong.infrastructure.webfx.WebFxGameApp;
import javafx.application.Application;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Application.launch(WebFxGameApp.class, args);
    }
}
```

## Configuración del módulo

El archivo `module-info.java` debe proveer la aplicación JavaFX/WebFX:

```java
module com.taller.pong {
    requires javafx.controls;
    requires javafx.graphics;

    exports com.taller.pong;
    exports com.taller.pong.application;
    exports com.taller.pong.domain.constants;
    exports com.taller.pong.domain.model;
    exports com.taller.pong.domain.rules;
    exports com.taller.pong.infrastructure.webfx;

    provides javafx.application.Application
            with com.taller.pong.infrastructure.webfx.WebFxGameApp;
}
```

## Errores comunes

### `Unresolved module for package javafx.application`

Este error suele aparecer cuando el proyecto WebFX no está correctamente inicializado o cuando se ejecuta `webfx init` / `webfx create` desde una carpeta incorrecta.

El flujo correcto para crear un proyecto WebFX limpio es:

```powershell
mkdir pong-webfx
cd pong-webfx

webfx init com.taller:pong-webfx:1.0.0
webfx create application pong
```

No se debe ejecutar `webfx create application` directamente desde el Escritorio, porque WebFX puede crear `pom.xml` y `webfx.xml` en la carpeta equivocada.

### `GET /classes.js 404`

Significa que el HTML se generó, pero TeaVM no generó el JavaScript final.

Solución:

```powershell
mvn clean package -Pteavm-js -pl pong-teavm-js -am
```

### `main is not defined`

Normalmente aparece junto con el error anterior. El navegador intenta llamar a `main`, pero el archivo JavaScript generado por TeaVM no fue cargado porque `classes.js` no existe o no se está sirviendo desde la carpeta correcta.

### `cannot find symbol method main(String[])`

Si aparece este error:

```text
cannot find symbol
symbol: method main(java.lang.String[])
location: class com.taller.pong.infrastructure.webfx.WebFxGameApp
```

significa que `Main.java` está intentando ejecutar:

```java
WebFxGameApp.main(args);
```

La solución es usar:

```java
Application.launch(WebFxGameApp.class, args);
```

## Notas de desarrollo

El código original del juego fue adaptado para WebFX separando la lógica de dominio de la infraestructura visual.

La lógica principal del juego no debería depender directamente de WebFX. Las clases específicas de WebFX deberían mantenerse dentro de:

```text
com.taller.pong.infrastructure.webfx
```

Esto permite mantener el dominio más limpio y facilita futuras adaptaciones.

## Build validado

Comando validado:

```powershell
mvn clean package -Pteavm-js -pl pong-teavm-js -am
```

Resultado esperado:

```text
Reactor Summary:

pong-webfx    SUCCESS
pong          SUCCESS
pong-teavm-js SUCCESS

BUILD SUCCESS
```

## Estado actual

* Proyecto WebFX creado correctamente.
* Código del Pong integrado en el módulo `pong`.
* Compilación TeaVM JS funcionando.
* Ejecución local en navegador funcionando mediante `python -m http.server`.
