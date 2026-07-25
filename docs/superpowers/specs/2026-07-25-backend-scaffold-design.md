# Backend Scaffold Design

## Context

VinaClipAI has a local infrastructure foundation in place:

- PostgreSQL, Redis, and MinIO are defined in the root `docker-compose.yml`.
- `.env.example` documents development connection values.
- `Makefile` provides baseline infrastructure commands.
- `backend/` currently exists only as a tracked placeholder directory.

The next step is to scaffold the Spring Boot core backend service. This task creates the backend foundation only; it does not implement authentication, RBAC, organization management, activity workflow, asset management, or render job orchestration.

## Decision

Use Maven for the backend build.

Rationale:

- Maven is common and predictable for Spring Boot enterprise projects.
- XML configuration is verbose but stable and easy for agents/developers to inspect.
- It works well with standard Spring Boot plugin workflows.
- It keeps the initial backend scaffold straightforward.

## Backend Technology

Use:

- Java 21.
- Spring Boot 3.x.
- Maven wrapper.
- Spring Web.
- Spring Validation.
- Spring Data JPA.
- PostgreSQL JDBC driver.
- Flyway.
- Spring Boot Actuator.
- Lombok, if the generated project includes annotation processing cleanly.

## Scope

Included:

- Create a Maven-based Spring Boot project under `backend/`.
- Add a package layout aligned with the project coding rules.
- Configure PostgreSQL through environment-backed Spring properties.
- Add Flyway baseline migration.
- Add `GET /api/health`.
- Add a service interface and implementation for health checks.
- Add DTO response for health output.
- Add global exception scaffolding.
- Add backend test(s) for health endpoint/service.
- Add Makefile commands for backend build, test, and run.
- Update checklist only for completed scaffold items.

Excluded:

- Auth/RBAC.
- User, role, permission, or organization domain.
- Business entities for activity, asset, script, storyboard, review, approval, or publication.
- Backend Dockerfile.
- Backend service in `docker-compose.yml`.
- Production secret manager.
- Full API contract document.

## Package Structure

Create the backend package under:

```text
com.vinaclipai.backend
```

Target structure:

```text
backend/
  pom.xml
  mvnw
  mvnw.cmd
  .mvn/wrapper/
  src/main/java/com/vinaclipai/backend/
    VinaClipAiBackendApplication.java
    config/
    controller/
    service/
    service/impl/
    repository/
    repository/impl/
    entity/
    dto/request/
    dto/response/
    mapper/
    exception/
    audit/
    security/
    workflow/
    integration/
  src/main/resources/
    application.yml
    db/migration/V1__baseline.sql
  src/test/java/com/vinaclipai/backend/
```

Empty package directories should include `.gitkeep` only where no Java class exists yet.

## Layering Rules

The scaffold must establish the code style expected for future backend modules:

- Controllers handle HTTP input/output only.
- Controllers call service interfaces.
- Service implementations contain business logic.
- DTOs are separate from entities.
- Entities are not returned directly from APIs.
- Repositories are not called directly from controllers.
- Integration adapters are isolated under `integration`.
- Exceptions are translated centrally.

For the health endpoint:

```text
HealthController -> HealthService -> HealthServiceImpl -> DataSource/JdbcTemplate validation
```

## API Design

Create:

```http
GET /api/health
```

Successful response:

```json
{
  "status": "UP",
  "service": "core-backend",
  "database": "UP"
}
```

If database validation fails, return HTTP 503 with:

```json
{
  "status": "DOWN",
  "service": "core-backend",
  "database": "DOWN"
}
```

For this scaffold, the health response should be explicit and simple rather than exposing the full Actuator payload publicly.

## Configuration

Use `backend/src/main/resources/application.yml`.

Defaults:

- App name: `vinaclipai-core-backend`.
- Server port: `8081`.
- API prefix: controllers use `/api`.
- PostgreSQL host defaults to `localhost`.
- PostgreSQL port defaults to `5432`.
- PostgreSQL database defaults to `vinaclipai`.
- PostgreSQL user defaults to `vinaclipai`.
- PostgreSQL password defaults to `vinaclipai_dev_password`.

Environment variable names:

```text
POSTGRES_HOST
POSTGRES_PORT
POSTGRES_DB
POSTGRES_USER
POSTGRES_PASSWORD
BACKEND_PORT
```

Spring datasource URL:

```text
jdbc:postgresql://${POSTGRES_HOST:localhost}:${POSTGRES_PORT:5432}/${POSTGRES_DB:vinaclipai}
```

## Database Migration

Add:

```text
backend/src/main/resources/db/migration/V1__baseline.sql
```

The baseline migration should create a small technical table to prove Flyway is running, for example:

```sql
CREATE TABLE IF NOT EXISTS schema_version_marker (
    id BIGSERIAL PRIMARY KEY,
    marker VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

INSERT INTO schema_version_marker (marker)
VALUES ('backend_baseline')
ON CONFLICT (marker) DO NOTHING;
```

This is not a business table. It only validates the migration pipeline.

## Makefile Changes

Extend the root `Makefile` with:

```bash
make backend-build
make backend-test
make backend-run
```

Expected behavior:

- `backend-build`: runs `cd backend && ./mvnw clean package`.
- `backend-test`: runs `cd backend && ./mvnw test`.
- `backend-run`: runs `cd backend && ./mvnw spring-boot:run`.

Keep existing infrastructure targets.

## Verification

Minimum verification:

```bash
make backend-test
make backend-build
```

Runtime verification with infrastructure running:

```bash
make up
make backend-run
curl -s http://localhost:8081/api/health
```

Expected response contains:

```json
"status":"UP"
```

Stop the backend process after runtime verification and run:

```bash
make down
```

## Checklist Updates

Update `PROJECT_CHECKLIST.md` only for completed scaffold items:

- Backend uses Java + Spring Boot.
- Baseline PostgreSQL migration pipeline exists.
- DTO response separation exists for health.
- Service interface and implementation pattern exists.
- Controller calls service rather than repository.
- `make backend-test` exists.

Do not mark auth, RBAC, business modules, backend Docker service, or production hardening as complete.

## Commit

Implementation commit:

```bash
feat: scaffold core backend service
```

Spec commit:

```bash
docs: design backend scaffold
```
