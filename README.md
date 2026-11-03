# greetings-kotlin

A small CRUD app for practicing Kotlin on Spring MVC. Greetings are stored in memory. There is no database and no login. Restarting the app clears the data.

## Stack

- Java 25
- Kotlin 2.3
- Spring Boot 4.1

The machine must use JDK 25. If `java -version` is not 25, point `JAVA_HOME` at a JDK 25 install before running. Classes compiled for Java 25 will not load on JDK 21.

## Run

```bash
./mvnw spring-boot:run
```

The app listens at `http://localhost:8080`.

Tests and the package:

```bash
./mvnw test
./mvnw package
```

The JAR is `target/greetings-kotlin-0.0.1-SNAPSHOT.jar`.

## API

Every route is under `/api/v1/greetings`.

| Method | Path | Status |
|---|---|---|
| `POST` | `/api/v1/greetings` | `201` creates one. The server assigns `id`. |
| `GET` | `/api/v1/greetings` | `200` lists every greeting, ordered by `id`. |
| `GET` | `/api/v1/greetings/{id}` | `200`, or `404` when the id is missing. |
| `PUT` | `/api/v1/greetings/{id}` | `200` replaces the whole greeting, or `404`. |
| `DELETE` | `/api/v1/greetings/{id}` | `204`, or `404`. |

Request body:

```json
{
  "message": "hello",
  "recipient": "Ann"
}
```

`message` must contain text. When `recipient` is omitted or blank, it is left out of the JSON response. An `id` of `0` or below returns `400`.

## Packages

Code lives under `dev.wachayathorn.greetings_kotlin.greeting`.

| Package | Role |
|---|---|
| `controller` | Accepts HTTP and turns failures into status codes |
| `service` | Creates, reads, replaces, and deletes a greeting |
| `repository` | Holds greetings in a `ConcurrentHashMap` |
| `dto` | The request and response shapes |
| `model` | The greeting stored in memory |

Domain terms are in `CONTEXT.md`. Short comments in the code mark where Kotlin differs from Java.
