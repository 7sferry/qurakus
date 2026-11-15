# qurakus

A user service built with Clean Architecture on Quarkus and Hibernate ORM with Panache. It is a Quarkus port of `so-clean` (user registration and user detail) laid out like `qlaundry`: one Maven module per layer, every feature split into use case, request, response, gateway and presenter.

## Features

- **User registration**: a username (5 to 50 characters, unique) and a birth date (at least 18 years old). A new user is active.
- **User detail**: look up an active user by username. The birth date comes back formatted, e.g. `Friday, 09 May 1997`.

## Tech stack

| Layer | Technology |
|---|---|
| Runtime | Java 25, Quarkus 3.40 (Quarkus REST + Jackson, ArC CDI, YAML config) |
| API docs | SmallRye OpenAPI 3.1 + Swagger UI |
| Persistence | Hibernate ORM with Panache (repository pattern), PostgreSQL |
| Architecture | Clean Architecture, one Maven module per layer (`domain` → `core` → `gateway` → `web-service`) |
| Ids | ULID (`utils/identity-generator`) |
| Validation | Jakarta Bean Validation (Hibernate Validator) on request records, plus checks in domain records |
| Tests | JUnit 5, Mockito (BDD), AssertJ |

## Repository layout

```
user-service/
├── user-domain/        com.ferry.user.domain       value objects (Username, BirthDate), User, exceptions. No framework.
├── user-core/          com.ferry.user.core         use cases + Gateway/Presenter interfaces (ports). No Quarkus, no JPA.
├── user-gateway/       com.ferry.user.gateway      Panache entity, repository and Jpa*Gateway adapters.
└── user-web-service/   com.ferry.user.webservice   Quarkus app: controllers, Web*Presenter, exception mapping, CDI wiring.
utils/
└── identity-generator/ com.ferry.utils.generator   IdGenerator / UlidGenerator
```

Dependencies only point inward: `web-service` → `gateway` → `core` → `domain`.

## Getting started

### Prerequisites

- Java 25 (Maven is bundled as `./mvnw`)
- PostgreSQL on `localhost:5432`, user `postgres`, password `12345`

### 1. Database

Create the database:

```sql
CREATE DATABASE so_clean_qurakus;
```

Hibernate creates and updates the tables in its `public` schema (`schema-management.strategy: update`) the first time the service starts.

### 2. Build

From the repo root:

```bash
./mvnw install                  # builds every module and runs the unit tests
./mvnw install -DskipTests      # without the tests
```

On Windows without Git Bash, use `mvnw.cmd` instead of `./mvnw`.

### 3. Run in dev mode

```bash
./mvnw quarkus:dev -pl user-service/user-web-service
```

The Quarkus build-analytics prompt is turned off for this repo by `-Dquarkus.analytics.disabled=true` in `.mvn/maven.config`.

The service listens on **http://localhost:8004**. Dev mode live-reloads changes in every module, not just the web-service. The Dev UI is at http://localhost:8004/q/dev-ui.

### 4. Run the packaged app

```bash
./mvnw install -DskipTests
java -jar user-service/user-web-service/target/quarkus-app/quarkus-run.jar
```

`target/quarkus-app/` is the whole app (the jar plus `lib/`), so copy the folder, not just the jar. For a single über-jar, add `-Dquarkus.package.jar.type=uber-jar` and run `target/user-web-service-1.0.0-runner.jar`.

### 5. Run in Docker (optional)

The Dockerfiles live in `user-service/user-web-service/src/main/docker/`. Build the app first, then build the image from the module folder:

```bash
./mvnw install -DskipTests
cd user-service/user-web-service
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/user-web-service-jvm .
docker run -i --rm -p 8004:8004 \
  -e QUARKUS_DATASOURCE_JDBC_URL='jdbc:postgresql://host.docker.internal:5432/so_clean_qurakus' \
  quarkus/user-web-service-jvm
```

Inside a container, `localhost` is the container itself, so point the datasource at the host as shown.

## API

There is no authentication, so both endpoints are under `/public/`.

```
POST /public/user/registration   {username, birthDate}   → 201 {id, username, status, joinedAt}
GET  /public/user/detail         ?username=              → 200 {id, username, status, birthDate, joinedAt}
```

```bash
curl -i -X POST http://localhost:8004/public/user/registration \
  -H 'Content-Type: application/json' \
  -d '{"username":"budi_santoso","birthDate":"1997-05-09"}'

curl -i 'http://localhost:8004/public/user/detail?username=budi_santoso'
```

- `birthDate` is ISO `yyyy-MM-dd`. `joinedAt` is epoch milliseconds. `status` is `Active` or `Inactive`.
- Errors are RFC 7807 problem details (`application/problem+json`): `{type, title, status, detail, instance}`.

| Status | When |
|---|---|
| 400 | missing body, blank or missing field, username shorter than 5 or longer than 50, under 18, malformed JSON or date |
| 404 | no active user with that username, or unknown path |
| 409 | username already taken |
| 500 | anything else (`detail` is always `Internal Error`; the cause is in the log) |

## OpenAPI

The spec is generated from the code by `quarkus-smallrye-openapi` (OpenAPI 3.1):

| Where | What |
|---|---|
| http://localhost:8004/q/openapi | the spec as YAML (`?format=json` for JSON), in every mode |
| http://localhost:8004/q/swagger-ui | Swagger UI, dev mode only |
| `user-service/user-web-service/target/openapi/openapi.{yaml,json}` | the spec as files, written on every build |

- Operations, responses and examples come from MicroProfile OpenAPI annotations on the controllers, the `*RestResponse` records and `ProblemDetail`, all in `user-web-service`. `user-core` and `user-domain` carry no OpenAPI annotations.
- The request body schema is read from the core request record, including its Bean Validation constraints (`@NotBlank`, `@NotNull` → `required`).
- A new endpoint gets `@Tag`, `@Operation` and one `@APIResponse` per status it can return, with `ProblemDetail` as the error body under `application/problem+json`.

## Configuration

Everything is in `user-service/user-web-service/src/main/resources/application.yaml` with dev values:

| Key | Value |
|---|---|
| `quarkus.http.port` | `8004` |
| `quarkus.datasource.jdbc.url` | `jdbc:postgresql://localhost:5432/so_clean_qurakus` |
| `quarkus.datasource.username` / `password` | `postgres` / `12345` |
| `quarkus.hibernate-orm.schema-management.strategy` | `update` |
| `quarkus.hibernate-orm.physical-naming-strategy` | `CamelCaseToUnderscoresNamingStrategy` (`birthDate` → `birth_date`) |
| `quarkus.log.file.path` | `logs/user-web-service.log`, rotated at 10 MB and daily, 30 archives |
| `quarkus.smallrye-openapi.*` | spec title, version, description, and `store-schema-directory: target/openapi` |

Override any key with an environment variable, e.g. `QUARKUS_DATASOURCE_PASSWORD`, `QUARKUS_DATASOURCE_JDBC_URL`, `QUARKUS_HTTP_PORT`.

## Tests

```bash
./mvnw test
```

The tests are unit tests for every use case in `user-core`, with mocked gateways and presenters. They need no database.

## Architecture and conventions

The rules are the ones used in `qlaundry`, adapted from Spring Boot to Quarkus.

### Feature shape

Every feature (e.g. `user/registration`) has the same files across the layers:

- `core`: `XxxUseCase` (interface, `void execute(XxxRequest request, XxxPresenter presenter)`), `DefaultXxxUseCase` (plain POJO), `XxxRequest` / `XxxResponse` (records), `XxxGateway` (persistence port), `XxxPresenter` (output port)
- `gateway`: `JpaXxxGateway` implements `XxxGateway`
- `web-service`: `XxxController`, `WebXxxPresenter` (builds the `RestResponse`), `XxxRestResponse` (the JSON body)

Controllers are thin: build a `WebXxxPresenter`, call `useCase.execute(request, presenter)`, return `presenter.getRestResponse()`.

### Wiring

- Use cases and gateways are plain classes with no CDI annotations. They are produced in `UserWebConfig` with `@Produces @ApplicationScoped` methods (gateway bean, then use case bean). This is the Quarkus counterpart of qlaundry's `@Bean` methods. Register every new use case there.
- The Panache repository (`UserPanacheRepository`, `PanacheRepositoryBase<UserJpa, String>`) is the only CDI bean in `user-gateway`. `user-gateway` builds a Jandex index (`jandex-maven-plugin`) so Quarkus finds the entity and the repository inside the module jar. A new module with entities or beans needs the same plugin.
- Controllers use `@Transactional` (Jakarta). Quarkus has no per-method isolation level, so the Postgres default (read committed) applies.

### Domain and validation

- Domain types are records that validate in their compact constructor (`Username`, `BirthDate`), with static factories (`User.register(...)`) and `xxxValue()` unwrap helpers.
- Request records implement `UserValidation`, and every use case starts with `request.validate()` (Bean Validation annotations: `@NotBlank`, `@NotNull`). Domain rules (length, age) run after it when the value objects are built.
- Exceptions map to HTTP statuses in `UserWebExceptionHandler` (`@ServerExceptionMapper`).

### Persistence

- Entities: `@Getter @Setter @EqualsAndHashCode(of = "id") @Entity @Table(name = "snake_plural")`, named `XxxJpa`.
- `String id` is a ULID from `IdGenerator.generateId()` in the gateway, never generated by the database.
- Audit block: `version` (`@Version Integer`), `deleted`, `createdBy`, `createdAt`, `updatedBy`, `updatedAt`; ids are `length = 50` and the created columns are `updatable = false`. A self-registered user's `createdBy` and `updatedBy` are its own id.
- Entities map to and from the domain in static `construct(...)` methods, using accessors only.
- Writes use `persistAndFlush` so the returned domain object carries the database `@Version`.
- Username uniqueness is checked by the use case and enforced by the `uk_users_username` constraint. The check counts deleted rows too, because the constraint does.

### Endpoints

- Every endpoint reachable without a token is under `/public/`, and nothing else may be. Today that is every endpoint, because there is no authentication yet.

### Code style

- No explanatory comments in code. Documentation lives in this README.
- Every file carries the `Made by [MR Ferry™]` banner; tabs; `){` and `if(...){` with no space before the brace or paren.
- Unit tests: one `Default*UseCaseTest` per use case, shipped in the same change. Each test builds its own data inline (no shared fixture helpers; class constants are fine), BDDMockito (`willReturn(...).given(...)`, `then(mock).should()`), AssertJ `thenSoftly`, `@Captor` fields, and typed matchers (`any(User.class)`, never a bare `any()`).
