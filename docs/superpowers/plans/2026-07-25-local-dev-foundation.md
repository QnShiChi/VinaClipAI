# Local Development Foundation Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Establish the VinaClipAI local development foundation with preserved MoneyPrinterTurbo compose, VinaClipAI infrastructure services, tracked project directories, environment template, Makefile commands, and checklist updates.

**Architecture:** The root `docker-compose.yml` becomes the default VinaClipAI infrastructure stack. The original MoneyPrinterTurbo compose is preserved as `docker-compose.moneyprinter.yml`. PostgreSQL, Redis, and MinIO run as local dependencies while backend, frontend, and worker codebases remain unscaffolded.

**Tech Stack:** Docker Compose, PostgreSQL, Redis, MinIO, GNU Make, Markdown, Git.

---

## File Structure

This plan creates or modifies these files:

- Rename: `docker-compose.yml` -> `docker-compose.moneyprinter.yml`
  - Preserves the original MoneyPrinterTurbo WebUI/API compose for reference and manual worker investigation.
- Create: `docker-compose.yml`
  - Defines VinaClipAI local infrastructure: PostgreSQL, Redis, MinIO, and MinIO bucket initialization.
- Create: `.env.example`
  - Documents local development environment variables with non-production defaults.
- Modify: `.gitignore`
  - Ensures `.env` is ignored while `.env.example` remains tracked.
- Create: `Makefile`
  - Provides standard project commands: `env`, `up`, `down`, `ps`, `logs`, `test`.
- Create: `.gitkeep` files in:
  - `backend/.gitkeep`
  - `frontend/.gitkeep`
  - `worker/.gitkeep`
  - `infra/.gitkeep`
  - `docs/architecture/.gitkeep`
  - `docs/api/.gitkeep`
  - `docs/runbooks/.gitkeep`
  - `docs/security/.gitkeep`
  - `docs/testing/.gitkeep`
- Modify: `PROJECT_CHECKLIST.md`
  - Marks only the infrastructure foundation items completed by this implementation.

The plan intentionally does not create Spring Boot, React, or worker code.

---

## Task 1: Preserve MoneyPrinterTurbo Compose

**Files:**

- Rename: `docker-compose.yml` -> `docker-compose.moneyprinter.yml`

- [ ] **Step 1: Inspect the current compose file**

Run:

```bash
sed -n '1,220p' docker-compose.yml
```

Expected: output shows the original MoneyPrinterTurbo `webui` and `api` services with containers named `moneyprinterturbo-webui` and `moneyprinterturbo-api`.

- [ ] **Step 2: Rename the compose file**

Run:

```bash
mv docker-compose.yml docker-compose.moneyprinter.yml
```

Expected: command exits 0.

- [ ] **Step 3: Verify the rename**

Run:

```bash
ls -la docker-compose.moneyprinter.yml
```

Expected: file exists and has the original compose content.

- [ ] **Step 4: Verify Git sees a rename after the new root compose is added later**

Run now:

```bash
git status --short
```

Expected at this point: deleted `docker-compose.yml` and untracked `docker-compose.moneyprinter.yml`, or Git may later infer rename after Task 2.

Do not commit yet; Task 2 creates the replacement root compose.

---

## Task 2: Create VinaClipAI Docker Compose Infrastructure

**Files:**

- Create: `docker-compose.yml`

- [ ] **Step 1: Create the new compose file**

Create `docker-compose.yml` with this exact content:

```yaml
services:
  postgres:
    image: postgres:16-alpine
    container_name: vinaclipai-postgres
    restart: unless-stopped
    environment:
      POSTGRES_DB: ${POSTGRES_DB:-vinaclipai}
      POSTGRES_USER: ${POSTGRES_USER:-vinaclipai}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD:-vinaclipai_dev_password}
    ports:
      - "${POSTGRES_PORT:-5432}:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U $${POSTGRES_USER} -d $${POSTGRES_DB}"]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 10s

  redis:
    image: redis:7-alpine
    container_name: vinaclipai-redis
    restart: unless-stopped
    command: ["redis-server", "--appendonly", "yes"]
    ports:
      - "${REDIS_PORT:-6379}:6379"
    volumes:
      - redis_data:/data
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 5s

  minio:
    image: minio/minio:RELEASE.2026-06-02T00-12-00Z
    container_name: vinaclipai-minio
    restart: unless-stopped
    command: ["server", "/data", "--console-address", ":9001"]
    environment:
      MINIO_ROOT_USER: ${MINIO_ROOT_USER:-vinaclipai}
      MINIO_ROOT_PASSWORD: ${MINIO_ROOT_PASSWORD:-vinaclipai_minio_dev_password}
    ports:
      - "${MINIO_API_PORT:-9000}:9000"
      - "${MINIO_CONSOLE_PORT:-9001}:9001"
    volumes:
      - minio_data:/data
    healthcheck:
      test: ["CMD", "mc", "ready", "local"]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 10s

  minio-init:
    image: minio/mc:RELEASE.2026-05-21T16-19-24Z
    container_name: vinaclipai-minio-init
    depends_on:
      minio:
        condition: service_healthy
    environment:
      MINIO_ROOT_USER: ${MINIO_ROOT_USER:-vinaclipai}
      MINIO_ROOT_PASSWORD: ${MINIO_ROOT_PASSWORD:-vinaclipai_minio_dev_password}
      MINIO_BUCKET_MEDIA: ${MINIO_BUCKET_MEDIA:-vinaclipai-media}
    entrypoint:
      - /bin/sh
      - -c
      - |
        mc alias set local http://minio:9000 "$${MINIO_ROOT_USER}" "$${MINIO_ROOT_PASSWORD}"
        mc mb --ignore-existing "local/$${MINIO_BUCKET_MEDIA}"
        mc anonymous set none "local/$${MINIO_BUCKET_MEDIA}"
    restart: "no"

volumes:
  postgres_data:
  redis_data:
  minio_data:
```

- [ ] **Step 2: Validate compose syntax**

Run:

```bash
docker compose config
```

Expected: exit 0 and normalized YAML output that includes services `postgres`, `redis`, `minio`, and `minio-init`.

If Docker Compose attempts to resolve images but Docker daemon is unavailable, record the exact error. Syntax must still be checked once Docker is available before claiming the task complete.

- [ ] **Step 3: Inspect Git status**

Run:

```bash
git status --short
```

Expected: `docker-compose.yml` modified/recreated and `docker-compose.moneyprinter.yml` untracked or detected as rename.

Do not commit yet; environment and Makefile belong to the same foundation implementation commit or smaller commits after verification.

---

## Task 3: Add Environment Template and Git Ignore Rules

**Files:**

- Create: `.env.example`
- Modify: `.gitignore`

- [ ] **Step 1: Create `.env.example`**

Create `.env.example` with this exact content:

```dotenv
COMPOSE_PROJECT_NAME=vinaclipai

POSTGRES_DB=vinaclipai
POSTGRES_USER=vinaclipai
POSTGRES_PASSWORD=vinaclipai_dev_password
POSTGRES_PORT=5432

REDIS_PORT=6379

MINIO_ROOT_USER=vinaclipai
MINIO_ROOT_PASSWORD=vinaclipai_minio_dev_password
MINIO_API_PORT=9000
MINIO_CONSOLE_PORT=9001
MINIO_BUCKET_MEDIA=vinaclipai-media
```

- [ ] **Step 2: Update `.gitignore`**

Add this block near the top of `.gitignore`, after `.DS_Store`:

```gitignore
# Local environment files
.env
.env.*
!.env.example
```

Keep the existing MoneyPrinterTurbo ignore rules below this block.

- [ ] **Step 3: Verify `.env.example` is tracked candidate and `.env` would be ignored**

Run:

```bash
git check-ignore -v .env
```

Expected: output references the `.env` rule in `.gitignore`.

Run:

```bash
git check-ignore -v .env.example
```

Expected: exit 1 and no output, because `.env.example` must not be ignored.

- [ ] **Step 4: Validate compose uses the env defaults**

Run:

```bash
docker compose --env-file .env.example config
```

Expected: exit 0 and output includes project services with the configured ports and environment values.

---

## Task 4: Add Makefile Developer Commands

**Files:**

- Create: `Makefile`

- [ ] **Step 1: Create `Makefile`**

Create `Makefile` with this exact content:

```makefile
.PHONY: env up down ps logs test

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
```

- [ ] **Step 2: Verify Makefile targets are listed**

Run:

```bash
make -n up
```

Expected: dry-run output includes `test -f .env || cp .env.example .env` and `docker compose up -d`.

Run:

```bash
make -n test
```

Expected: dry-run output includes `docker compose config >/dev/null`.

- [ ] **Step 3: Verify `make test` validates compose**

Run:

```bash
make test
```

Expected: exit 0 if Docker Compose is available and the compose file is valid. If Docker is unavailable, record the exact error and run `docker compose config` again when Docker is available.

Do not add destructive targets in this task.

---

## Task 5: Add Target Project Directories

**Files:**

- Create: `backend/.gitkeep`
- Create: `frontend/.gitkeep`
- Create: `worker/.gitkeep`
- Create: `infra/.gitkeep`
- Create: `docs/architecture/.gitkeep`
- Create: `docs/api/.gitkeep`
- Create: `docs/runbooks/.gitkeep`
- Create: `docs/security/.gitkeep`
- Create: `docs/testing/.gitkeep`

- [ ] **Step 1: Create directories**

Run:

```bash
mkdir -p backend frontend worker infra docs/architecture docs/api docs/runbooks docs/security docs/testing
```

Expected: command exits 0.

- [ ] **Step 2: Add `.gitkeep` files**

Create these empty files:

```text
backend/.gitkeep
frontend/.gitkeep
worker/.gitkeep
infra/.gitkeep
docs/architecture/.gitkeep
docs/api/.gitkeep
docs/runbooks/.gitkeep
docs/security/.gitkeep
docs/testing/.gitkeep
```

- [ ] **Step 3: Verify directories**

Run:

```bash
ls -la backend frontend worker infra docs/architecture docs/api docs/runbooks docs/security docs/testing
```

Expected: each directory exists and contains `.gitkeep`.

---

## Task 6: Update Checklist for Completed Foundation Items

**Files:**

- Modify: `PROJECT_CHECKLIST.md`

- [ ] **Step 1: Update architecture checklist items**

In `PROJECT_CHECKLIST.md`, under `## 2. Kiến trúc tổng thể`, change these completed items to checked:

```markdown
- [x] Object Storage dùng MinIO/S3-compatible cho ảnh, video, audio, subtitle, thumbnail và output.
- [x] PostgreSQL là database nghiệp vụ chính.
- [x] Redis dùng cho queue/cache/progress/lock/retry nếu phù hợp.
- [x] Docker Compose quản lý các service local/dev.
```

Leave this item unchecked because the first Makefile only adds baseline commands and does not yet include build, migrate, or clean:

```markdown
- [ ] Makefile cung cấp lệnh chạy, build, test, migrate, logs, clean theo chuẩn thống nhất.
```

Do not check the 3-system split yet. This task creates placeholder directories but does not implement the three systems:

```markdown
- [ ] Tách hệ thống thành 3 khối chính:
  - [ ] `frontend-app`: React + TypeScript.
  - [ ] `core-backend`: Spring Boot.
  - [ ] `video-ai-worker`: Python, kế thừa phần phù hợp từ MoneyPrinterTurbo.
```

- [ ] **Step 2: Update repository structure checklist items**

Under `## 3. Cấu trúc repository mục tiêu`, change all listed directory items to checked after Task 5:

```markdown
- [x] Tạo thư mục `frontend/` cho React + TypeScript.
- [x] Tạo thư mục `backend/` cho Spring Boot.
- [x] Tạo `infra/` hoặc `deploy/` cho Docker, compose, scripts triển khai.
- [x] Tạo `docs/architecture/` cho tài liệu kiến trúc.
- [x] Tạo `docs/api/` cho OpenAPI/API contract.
- [x] Tạo `docs/runbooks/` cho vận hành, backup, restore, incident.
- [x] Tạo `docs/security/` cho threat model và chính sách dữ liệu.
- [x] Tạo `docs/testing/` cho test strategy.
```

Important correction: do not check `Tái định vị phần MoneyPrinterTurbo...` unless this implementation actually moves or wraps worker code. For this task, leave that item unchecked because the design explicitly excludes worker extraction.

The worker repositioning line must remain:

```markdown
- [ ] Tái định vị phần MoneyPrinterTurbo hiện tại thành worker hoặc module Python có ranh giới rõ.
```

- [ ] **Step 3: Update Docker/Makefile checklist items**

Under `## 8. Docker, Makefile và môi trường local`, check only these items:

```markdown
- [x] `docker-compose.yml` chạy PostgreSQL.
- [x] `docker-compose.yml` chạy Redis.
- [x] `docker-compose.yml` chạy MinIO.
- [x] `.env.example` đầy đủ biến môi trường dev.
- [x] Không commit `.env`.
- [x] Makefile có `make up`.
- [x] Makefile có `make down`.
- [x] Makefile có `make logs`.
- [x] Makefile có `make ps`.
- [x] Makefile có `make test`.
```

Leave backend/frontend/worker service, migrate, seed, and clean items unchecked.

- [ ] **Step 4: Update Phase 0**

Under `### Phase 0: Chuẩn hóa repo và tài liệu`, check:

```markdown
- [x] Tạo cấu trúc thư mục mục tiêu.
```

- [ ] **Step 5: Verify checklist did not overclaim**

Run:

```bash
rg -n "Tái định vị phần MoneyPrinterTurbo|backend-test|frontend-test|worker-test|make migrate|make seed|make clean|Spring Boot scaffold|React scaffold" PROJECT_CHECKLIST.md
```

Expected: relevant implementation items remain unchecked unless this plan explicitly completed them.

---

## Task 7: Verify Full Local Foundation

**Files:**

- Validate only; no file changes expected unless a previous task has an error.

- [ ] **Step 1: Verify compose config**

Run:

```bash
docker compose --env-file .env.example config >/tmp/vinaclipai-compose-config.yml
```

Expected: exit 0 and file `/tmp/vinaclipai-compose-config.yml` exists.

- [ ] **Step 2: Verify Makefile dry-run commands**

Run:

```bash
make -n up
```

Expected: output includes `.env` creation guard and `docker compose up -d`.

Run:

```bash
make -n down
```

Expected: output includes `docker compose down`.

- [ ] **Step 3: Verify Git change scope**

Run:

```bash
git status --short
```

Expected: only files from this plan are changed or untracked.

- [ ] **Step 4: Optional runtime verification if Docker daemon is available**

Run:

```bash
make up
docker compose ps
make down
```

Expected if Docker is available:

- `make up` exits 0.
- `docker compose ps` shows `vinaclipai-postgres`, `vinaclipai-redis`, `vinaclipai-minio`, and `vinaclipai-minio-init`.
- `make down` exits 0.

If Docker is unavailable, capture the exact command output and do not claim runtime verification passed.

- [ ] **Step 5: Scan for placeholders**

Run:

```bash
rg -n "TB[D]|TO[D]O|FIX[M]E|PLACEH[O]LDER|lor[e]m|implement lat[e]r" docker-compose.yml docker-compose.moneyprinter.yml .env.example Makefile PROJECT_CHECKLIST.md
```

Expected: exit 1 and no output.

---

## Task 8: Commit Local Development Foundation

**Files:**

- Stage all files changed by Tasks 1-7.

- [ ] **Step 1: Review diff stat**

Run:

```bash
git diff --stat
```

Expected: changed files include:

- `docker-compose.yml`
- `docker-compose.moneyprinter.yml`
- `.env.example`
- `.gitignore`
- `Makefile`
- `.gitkeep` files
- `PROJECT_CHECKLIST.md`

- [ ] **Step 2: Review full diff**

Run:

```bash
git diff
```

Expected: no secret values beyond local development defaults from `.env.example`; no unrelated code changes.

- [ ] **Step 3: Stage files**

Run:

```bash
git add docker-compose.yml docker-compose.moneyprinter.yml .env.example .gitignore Makefile PROJECT_CHECKLIST.md backend/.gitkeep frontend/.gitkeep worker/.gitkeep infra/.gitkeep docs/architecture/.gitkeep docs/api/.gitkeep docs/runbooks/.gitkeep docs/security/.gitkeep docs/testing/.gitkeep
```

Expected: command exits 0.

- [ ] **Step 4: Verify staged scope**

Run:

```bash
git diff --cached --name-only
```

Expected: only the files listed in Step 3.

- [ ] **Step 5: Commit**

Run:

```bash
git commit -m "infra: add local development foundation"
```

Expected: commit succeeds.

- [ ] **Step 6: Verify clean worktree**

Run:

```bash
git status --short
```

Expected: no output.

---

## Self-Review Notes

- Spec coverage: The plan covers compose preservation, new infrastructure compose, `.env.example`, `.gitignore`, Makefile, target directories, checklist updates, verification, and commit.
- Scope: The plan does not scaffold Spring Boot, React, or worker extraction.
- Ambiguity resolved: `docker-compose.yml` root is VinaClipAI default; MoneyPrinterTurbo compose is preserved separately.
- Known implementation caution: Do not check worker repositioning in `PROJECT_CHECKLIST.md` during this task because no worker code is moved.
