# Backend Scaffold Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Create the Spring Boot core backend scaffold under `backend/`, with Maven wrapper, PostgreSQL/Flyway configuration, a layered health endpoint, tests, Makefile backend commands, and checklist updates.

**Architecture:** The backend is a Maven-based Spring Boot service using controller -> service interface -> service implementation layering. It connects to local PostgreSQL through environment-backed datasource properties and proves migration wiring with a Flyway baseline table. Because the current machine has no local `java` or `mvn`, Makefile backend commands run Maven through a Dockerized JDK 21 Maven image while preserving the Maven wrapper inside `backend/`.

**Tech Stack:** Java 21, Spring Boot 3.x, Maven, Spring Web, Spring Validation, Spring Data JPA, PostgreSQL JDBC, Flyway, Actuator, Lombok, JUnit 5, Mockito, Dockerized Maven.

---

## Preflight Finding

The current workspace does not have Java or Maven installed:

```bash
java -version
mvn -version
```

Expected in current environment: both commands exit 127. Therefore implementation must not depend on local Java/Maven. Use Docker for Maven execution:

```bash
docker run --rm --network host -v "$PWD/backend:/workspace" -w /workspace maven:3.9.9-eclipse-temurin-21 ./mvnw test
```

This still creates a standard Maven backend with `mvnw`, so developers with local Java 21 can also run `cd backend && ./mvnw test`.

---

## File Structure

Create or modify:

- Create/replace backend scaffold:
  - `backend/pom.xml`
  - `backend/mvnw`
  - `backend/mvnw.cmd`
  - `backend/.mvn/wrapper/maven-wrapper.properties`
  - `backend/src/main/java/com/vinaclipai/backend/VinaClipAiBackendApplication.java`
  - `backend/src/main/java/com/vinaclipai/backend/controller/HealthController.java`
  - `backend/src/main/java/com/vinaclipai/backend/service/HealthService.java`
  - `backend/src/main/java/com/vinaclipai/backend/service/impl/HealthServiceImpl.java`
  - `backend/src/main/java/com/vinaclipai/backend/dto/response/HealthResponse.java`
  - `backend/src/main/java/com/vinaclipai/backend/exception/GlobalExceptionHandler.java`
  - `backend/src/main/java/com/vinaclipai/backend/exception/ApiErrorResponse.java`
  - `backend/src/main/resources/application.yml`
  - `backend/src/main/resources/db/migration/V1__baseline.sql`
  - `backend/src/test/java/com/vinaclipai/backend/controller/HealthControllerTest.java`
  - `backend/src/test/java/com/vinaclipai/backend/service/impl/HealthServiceImplTest.java`
- Create `.gitkeep` files for empty package directories:
  - `backend/src/main/java/com/vinaclipai/backend/audit/.gitkeep`
  - `backend/src/main/java/com/vinaclipai/backend/config/.gitkeep`
  - `backend/src/main/java/com/vinaclipai/backend/dto/request/.gitkeep`
  - `backend/src/main/java/com/vinaclipai/backend/entity/.gitkeep`
  - `backend/src/main/java/com/vinaclipai/backend/integration/.gitkeep`
  - `backend/src/main/java/com/vinaclipai/backend/mapper/.gitkeep`
  - `backend/src/main/java/com/vinaclipai/backend/repository/.gitkeep`
  - `backend/src/main/java/com/vinaclipai/backend/repository/impl/.gitkeep`
  - `backend/src/main/java/com/vinaclipai/backend/security/.gitkeep`
  - `backend/src/main/java/com/vinaclipai/backend/workflow/.gitkeep`
- Modify:
  - `Makefile`
  - `PROJECT_CHECKLIST.md`

Do not create backend Dockerfile or add backend service to `docker-compose.yml` in this plan.

---

## Task 1: Generate Maven Spring Boot Base

**Files:**

- Create/replace: `backend/pom.xml`
- Create/replace: `backend/mvnw`
- Create/replace: `backend/mvnw.cmd`
- Create/replace: `backend/.mvn/wrapper/maven-wrapper.properties`

- [ ] **Step 1: Confirm worktree is clean**

Run:

```bash
git status --short
```

Expected: no output.

- [ ] **Step 2: Remove existing backend marker**

Run:

```bash
rm -f backend/.gitkeep
```

Expected: command exits 0.

- [ ] **Step 3: Generate Spring Boot project with Maven wrapper**

Run:

```bash
tmpdir="$(mktemp -d)"
curl -fsSL "https://start.spring.io/starter.zip?type=maven-project&language=java&javaVersion=21&packaging=jar&groupId=com.vinaclipai&artifactId=backend&name=VinaClipAI%20Core%20Backend&packageName=com.vinaclipai.backend&dependencies=web,validation,data-jpa,postgresql,flyway,actuator,lombok" -o "$tmpdir/backend.zip"
unzip -q "$tmpdir/backend.zip" -d "$tmpdir/generated"
rsync -a --delete "$tmpdir/generated/" backend/
```

Expected:

- `backend/pom.xml` exists.
- `backend/mvnw` exists.
- `backend/src/main/java/com/vinaclipai/backend/BackendApplication.java` or an equivalent generated application file exists.

- [ ] **Step 4: Rename generated application class if necessary**

If the generated class is not named `VinaClipAiBackendApplication.java`, rename it:

```bash
generated_app="$(find backend/src/main/java/com/vinaclipai/backend -maxdepth 1 -name '*Application.java' | head -n 1)"
mv "$generated_app" backend/src/main/java/com/vinaclipai/backend/VinaClipAiBackendApplication.java
```

Then replace `backend/src/main/java/com/vinaclipai/backend/VinaClipAiBackendApplication.java` with:

```java
package com.vinaclipai.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VinaClipAiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VinaClipAiBackendApplication.class, args);
    }
}
```

- [ ] **Step 5: Make Maven wrapper executable**

Run:

```bash
chmod +x backend/mvnw
```

Expected: command exits 0.

- [ ] **Step 6: Verify Maven wrapper via Docker**

Run:

```bash
docker run --rm -v "$PWD/backend:/workspace" -w /workspace maven:3.9.9-eclipse-temurin-21 ./mvnw -version
```

Expected: exit 0, output includes Apache Maven and Java 21.

Do not commit yet; later tasks replace generated code and add tests.

---

## Task 2: Add Backend Configuration and Migration

**Files:**

- Create/replace: `backend/src/main/resources/application.yml`
- Create: `backend/src/main/resources/db/migration/V1__baseline.sql`

- [ ] **Step 1: Replace `application.yml`**

Create `backend/src/main/resources/application.yml` with:

```yaml
spring:
  application:
    name: vinaclipai-core-backend
  datasource:
    url: jdbc:postgresql://${POSTGRES_HOST:localhost}:${POSTGRES_PORT:5432}/${POSTGRES_DB:vinaclipai}
    username: ${POSTGRES_USER:vinaclipai}
    password: ${POSTGRES_PASSWORD:vinaclipai_dev_password}
    driver-class-name: org.postgresql.Driver
  flyway:
    enabled: true
    locations: classpath:db/migration
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
    properties:
      hibernate:
        format_sql: true

server:
  port: ${BACKEND_PORT:8081}

management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      probes:
        enabled: true
```

- [ ] **Step 2: Add baseline migration**

Create directory:

```bash
mkdir -p backend/src/main/resources/db/migration
```

Create `backend/src/main/resources/db/migration/V1__baseline.sql` with:

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

- [ ] **Step 3: Verify files exist**

Run:

```bash
ls -la backend/src/main/resources/application.yml backend/src/main/resources/db/migration/V1__baseline.sql
```

Expected: both files exist.

---

## Task 3: Add Health Endpoint Layering

**Files:**

- Create: `backend/src/main/java/com/vinaclipai/backend/dto/response/HealthResponse.java`
- Create: `backend/src/main/java/com/vinaclipai/backend/service/HealthService.java`
- Create: `backend/src/main/java/com/vinaclipai/backend/service/impl/HealthServiceImpl.java`
- Create: `backend/src/main/java/com/vinaclipai/backend/controller/HealthController.java`

- [ ] **Step 1: Create package directories**

Run:

```bash
mkdir -p backend/src/main/java/com/vinaclipai/backend/controller backend/src/main/java/com/vinaclipai/backend/service/impl backend/src/main/java/com/vinaclipai/backend/dto/response
```

Expected: command exits 0.

- [ ] **Step 2: Create health DTO**

Create `backend/src/main/java/com/vinaclipai/backend/dto/response/HealthResponse.java`:

```java
package com.vinaclipai.backend.dto.response;

public record HealthResponse(
    String status,
    String service,
    String database
) {
    public static HealthResponse up() {
        return new HealthResponse("UP", "core-backend", "UP");
    }

    public static HealthResponse down() {
        return new HealthResponse("DOWN", "core-backend", "DOWN");
    }
}
```

- [ ] **Step 3: Create service interface**

Create `backend/src/main/java/com/vinaclipai/backend/service/HealthService.java`:

```java
package com.vinaclipai.backend.service;

import com.vinaclipai.backend.dto.response.HealthResponse;

public interface HealthService {
    HealthResponse check();
}
```

- [ ] **Step 4: Create service implementation**

Create `backend/src/main/java/com/vinaclipai/backend/service/impl/HealthServiceImpl.java`:

```java
package com.vinaclipai.backend.service.impl;

import com.vinaclipai.backend.dto.response.HealthResponse;
import com.vinaclipai.backend.service.HealthService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class HealthServiceImpl implements HealthService {

    private final JdbcTemplate jdbcTemplate;

    public HealthServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public HealthResponse check() {
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        if (result != null && result == 1) {
            return HealthResponse.up();
        }
        return HealthResponse.down();
    }
}
```

- [ ] **Step 5: Create controller**

Create `backend/src/main/java/com/vinaclipai/backend/controller/HealthController.java`:

```java
package com.vinaclipai.backend.controller;

import com.vinaclipai.backend.dto.response.HealthResponse;
import com.vinaclipai.backend.service.HealthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @GetMapping
    public ResponseEntity<HealthResponse> getHealth() {
        HealthResponse response = healthService.check();
        HttpStatus status = "UP".equals(response.status()) ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(status).body(response);
    }
}
```

- [ ] **Step 6: Compile main sources**

Run:

```bash
docker run --rm -v "$PWD/backend:/workspace" -w /workspace maven:3.9.9-eclipse-temurin-21 ./mvnw -q -DskipTests compile
```

Expected: exit 0.

---

## Task 4: Add Global Exception Scaffolding and Empty Package Markers

**Files:**

- Create: `backend/src/main/java/com/vinaclipai/backend/exception/ApiErrorResponse.java`
- Create: `backend/src/main/java/com/vinaclipai/backend/exception/GlobalExceptionHandler.java`
- Create `.gitkeep` files for empty package directories.

- [ ] **Step 1: Create package directories**

Run:

```bash
mkdir -p backend/src/main/java/com/vinaclipai/backend/audit backend/src/main/java/com/vinaclipai/backend/config backend/src/main/java/com/vinaclipai/backend/dto/request backend/src/main/java/com/vinaclipai/backend/entity backend/src/main/java/com/vinaclipai/backend/exception backend/src/main/java/com/vinaclipai/backend/integration backend/src/main/java/com/vinaclipai/backend/mapper backend/src/main/java/com/vinaclipai/backend/repository/impl backend/src/main/java/com/vinaclipai/backend/security backend/src/main/java/com/vinaclipai/backend/workflow
```

Expected: command exits 0.

- [ ] **Step 2: Create API error response**

Create `backend/src/main/java/com/vinaclipai/backend/exception/ApiErrorResponse.java`:

```java
package com.vinaclipai.backend.exception;

import java.time.Instant;

public record ApiErrorResponse(
    String code,
    String message,
    Instant timestamp
) {
    public static ApiErrorResponse internalServerError(String message) {
        return new ApiErrorResponse("INTERNAL_SERVER_ERROR", message, Instant.now());
    }
}
```

- [ ] **Step 3: Create global exception handler**

Create `backend/src/main/java/com/vinaclipai/backend/exception/GlobalExceptionHandler.java`:

```java
package com.vinaclipai.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception exception) {
        ApiErrorResponse response = ApiErrorResponse.internalServerError("Unexpected server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
```

- [ ] **Step 4: Add `.gitkeep` markers**

Run:

```bash
touch backend/src/main/java/com/vinaclipai/backend/audit/.gitkeep
touch backend/src/main/java/com/vinaclipai/backend/config/.gitkeep
touch backend/src/main/java/com/vinaclipai/backend/dto/request/.gitkeep
touch backend/src/main/java/com/vinaclipai/backend/entity/.gitkeep
touch backend/src/main/java/com/vinaclipai/backend/integration/.gitkeep
touch backend/src/main/java/com/vinaclipai/backend/mapper/.gitkeep
touch backend/src/main/java/com/vinaclipai/backend/repository/.gitkeep
touch backend/src/main/java/com/vinaclipai/backend/repository/impl/.gitkeep
touch backend/src/main/java/com/vinaclipai/backend/security/.gitkeep
touch backend/src/main/java/com/vinaclipai/backend/workflow/.gitkeep
```

Expected: command exits 0.

- [ ] **Step 5: Compile main sources**

Run:

```bash
docker run --rm -v "$PWD/backend:/workspace" -w /workspace maven:3.9.9-eclipse-temurin-21 ./mvnw -q -DskipTests compile
```

Expected: exit 0.

---

## Task 5: Add Health Tests

**Files:**

- Create: `backend/src/test/java/com/vinaclipai/backend/controller/HealthControllerTest.java`
- Create: `backend/src/test/java/com/vinaclipai/backend/service/impl/HealthServiceImplTest.java`

- [ ] **Step 1: Create test directories**

Run:

```bash
mkdir -p backend/src/test/java/com/vinaclipai/backend/controller backend/src/test/java/com/vinaclipai/backend/service/impl
```

Expected: command exits 0.

- [ ] **Step 2: Create service test**

Create `backend/src/test/java/com/vinaclipai/backend/service/impl/HealthServiceImplTest.java`:

```java
package com.vinaclipai.backend.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.vinaclipai.backend.dto.response.HealthResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

class HealthServiceImplTest {

    @Test
    void checkReturnsUpWhenDatabaseResponds() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        when(jdbcTemplate.queryForObject("SELECT 1", Integer.class)).thenReturn(1);
        HealthServiceImpl service = new HealthServiceImpl(jdbcTemplate);

        HealthResponse response = service.check();

        assertThat(response.status()).isEqualTo("UP");
        assertThat(response.service()).isEqualTo("core-backend");
        assertThat(response.database()).isEqualTo("UP");
    }

    @Test
    void checkReturnsDownWhenDatabaseResponseIsUnexpected() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        when(jdbcTemplate.queryForObject("SELECT 1", Integer.class)).thenReturn(0);
        HealthServiceImpl service = new HealthServiceImpl(jdbcTemplate);

        HealthResponse response = service.check();

        assertThat(response.status()).isEqualTo("DOWN");
        assertThat(response.database()).isEqualTo("DOWN");
    }
}
```

- [ ] **Step 3: Create controller test**

Create `backend/src/test/java/com/vinaclipai/backend/controller/HealthControllerTest.java`:

```java
package com.vinaclipai.backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vinaclipai.backend.dto.response.HealthResponse;
import com.vinaclipai.backend.service.HealthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthService healthService;

    @Test
    void getHealthReturnsOkWhenServiceIsUp() throws Exception {
        when(healthService.check()).thenReturn(HealthResponse.up());

        mockMvc.perform(get("/api/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.service").value("core-backend"))
            .andExpect(jsonPath("$.database").value("UP"));
    }

    @Test
    void getHealthReturnsServiceUnavailableWhenServiceIsDown() throws Exception {
        when(healthService.check()).thenReturn(HealthResponse.down());

        mockMvc.perform(get("/api/health"))
            .andExpect(status().isServiceUnavailable())
            .andExpect(jsonPath("$.status").value("DOWN"))
            .andExpect(jsonPath("$.database").value("DOWN"));
    }
}
```

- [ ] **Step 4: Run backend tests**

Run:

```bash
docker run --rm -v "$PWD/backend:/workspace" -w /workspace maven:3.9.9-eclipse-temurin-21 ./mvnw test
```

Expected: exit 0 and test summary shows all tests passing.

---

## Task 6: Extend Makefile Backend Commands

**Files:**

- Modify: `Makefile`

- [ ] **Step 1: Replace Makefile with Dockerized backend commands**

Replace `Makefile` with:

```makefile
.PHONY: env up down ps logs test backend-build backend-test backend-run

MAVEN_IMAGE ?= maven:3.9.9-eclipse-temurin-21
BACKEND_MAVEN = docker run --rm --network host -v "$(CURDIR)/backend:/workspace" -w /workspace $(MAVEN_IMAGE)

env:
	@test -f .env || cp .env.example .env

up: env
	docker compose up -d

down:
	docker compose down

ps:
	docker compose ps

logs:
	docker compose logs -f

test:
	docker compose config >/dev/null

backend-build:
	$(BACKEND_MAVEN) ./mvnw clean package

backend-test:
	$(BACKEND_MAVEN) ./mvnw test

backend-run:
	$(BACKEND_MAVEN) ./mvnw spring-boot:run
```

- [ ] **Step 2: Verify dry-run output**

Run:

```bash
make -n backend-test
```

Expected: output includes Dockerized Maven command and `./mvnw test`.

Run:

```bash
make -n backend-run
```

Expected: output includes Dockerized Maven command, `--network host`, and `./mvnw spring-boot:run`.

- [ ] **Step 3: Run backend tests through Makefile**

Run:

```bash
make backend-test
```

Expected: exit 0.

---

## Task 7: Runtime Verification with PostgreSQL

**Files:**

- Validate only.

- [ ] **Step 1: Start infrastructure**

Run:

```bash
make up
```

Expected: exit 0.

- [ ] **Step 2: Start backend**

Run in an interactive command session:

```bash
make backend-run
```

Expected: backend starts on port `8081`, Flyway applies `V1__baseline.sql`, and logs include started application.

- [ ] **Step 3: Verify health endpoint**

From another shell while backend is running:

```bash
curl -s http://localhost:8081/api/health
```

Expected response:

```json
{"status":"UP","service":"core-backend","database":"UP"}
```

- [ ] **Step 4: Stop backend process**

Send `Ctrl+C` to the backend run session.

Expected: backend process exits.

- [ ] **Step 5: Stop infrastructure**

Run:

```bash
make down
```

Expected: exit 0.

---

## Task 8: Update Checklist

**Files:**

- Modify: `PROJECT_CHECKLIST.md`

- [ ] **Step 1: Check completed backend scaffold items**

Update completed items only:

```markdown
- [x] Backend dùng Java + Spring Boot.
- [x] Controller chỉ xử lý HTTP request/response, validation entrypoint và gọi service.
- [x] Service interface định nghĩa nghiệp vụ.
- [x] DTO response tách khỏi entity database.
- [x] Exception handling tập trung qua global exception handler.
- [x] Không trả entity JPA trực tiếp ra API.
- [x] Không để controller gọi repository trực tiếp.
- [x] Dùng PostgreSQL cho dữ liệu nghiệp vụ.
- [x] Dùng migration tool như Flyway hoặc Liquibase.
- [x] Mọi schema change phải có migration.
- [x] Makefile có `make backend-test`.
```

Keep these unchecked:

```markdown
- [ ] Auth/RBAC.
- [ ] Organization/User/Role/Permission.
- [ ] `docker-compose.yml` chạy backend.
- [ ] Makefile có `make migrate`.
- [ ] Makefile có `make seed`.
```

- [ ] **Step 2: Check Phase 1 Spring Boot scaffold**

Under `### Phase 1: Nền tảng hạ tầng và backend core`, change:

```markdown
- [x] Spring Boot scaffold.
- [x] Migration baseline.
```

Do not check Auth/RBAC, Organization/User/Role/Permission, or Audit log.

- [ ] **Step 3: Verify no overclaim**

Run:

```bash
rg -n "Auth/RBAC|Organization/User/Role/Permission|docker-compose.yml` chạy backend|make migrate|make seed|Audit log" PROJECT_CHECKLIST.md
```

Expected: those implementation items remain unchecked except textual headings.

---

## Task 9: Final Verification and Commit

**Files:**

- Stage files changed by Tasks 1-8.

- [ ] **Step 1: Run final test commands**

Run:

```bash
make test
make backend-test
make backend-build
```

Expected: all exit 0.

- [ ] **Step 2: Scan for markers**

Run:

```bash
rg -n "TB[D]|TO[D]O|FIX[M]E|PLACEH[O]LDER|lor[e]m|implement lat[e]r" backend Makefile PROJECT_CHECKLIST.md
```

Expected: exit 1 and no output.

- [ ] **Step 3: Review Git status**

Run:

```bash
git status --short
```

Expected: only backend scaffold, Makefile, and checklist changes.

- [ ] **Step 4: Stage files**

Run:

```bash
git add backend Makefile PROJECT_CHECKLIST.md
```

Expected: command exits 0.

- [ ] **Step 5: Verify staged scope**

Run:

```bash
git diff --cached --name-only
```

Expected: staged files are under `backend/`, plus `Makefile` and `PROJECT_CHECKLIST.md`.

- [ ] **Step 6: Commit**

Run:

```bash
git commit -m "feat: scaffold core backend service"
```

Expected: commit succeeds.

- [ ] **Step 7: Verify clean worktree**

Run:

```bash
git status --short
```

Expected: no output.

---

## Self-Review Notes

- Spec coverage: The plan covers Maven backend scaffold, package layout, PostgreSQL config, Flyway baseline, health endpoint, service/interface layering, global exception scaffolding, tests, Makefile commands, runtime verification, checklist updates, and commit.
- Environment adjustment: The spec expected Maven wrapper usage; this plan preserves the wrapper and routes Makefile commands through Docker because Java/Maven are not installed in the current environment.
- Scope control: The plan does not create auth, RBAC, business entities, backend Dockerfile, or backend compose service.
- Overclaim guard: Checklist items for auth/RBAC, organization, backend compose service, migrate, seed, and audit remain unchecked.
