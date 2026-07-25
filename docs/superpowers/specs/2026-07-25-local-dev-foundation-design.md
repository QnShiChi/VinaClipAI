# Local Development Foundation Design

## Context

VinaClipAI has been redirected from a MoneyPrinterTurbo-centered repository into a production-oriented platform for Vietnamese propaganda clip production. The current repository still contains the original MoneyPrinterTurbo Python API, Streamlit WebUI, Dockerfile, and `docker-compose.yml`.

The approved architecture is hybrid:

- React + TypeScript frontend for the production UI.
- Spring Boot backend for core business workflow, RBAC, approval, audit, and persistence.
- Python Video/AI Worker derived from the useful parts of MoneyPrinterTurbo.
- PostgreSQL, Redis, and MinIO for local infrastructure.

The immediate task is to establish the local development foundation without scaffolding business code yet.

## Decision

Use the root `docker-compose.yml` as the default VinaClipAI local stack. Preserve the original MoneyPrinterTurbo compose file by renaming it to `docker-compose.moneyprinter.yml`.

This keeps the root developer workflow aligned with the README and checklist:

```bash
docker compose up
make up
```

will target VinaClipAI infrastructure, not the old MoneyPrinterTurbo WebUI/API.

## Scope

This foundation task creates project structure and local infrastructure only. It does not implement backend APIs, frontend screens, database migrations, or worker extraction.

Included:

- Preserve the original MoneyPrinterTurbo compose file.
- Create a new VinaClipAI `docker-compose.yml`.
- Add PostgreSQL, Redis, MinIO, and MinIO bucket initialization.
- Add `.env.example` for local development.
- Add a root `Makefile` with safe developer commands.
- Create empty target directories with `.gitkeep`.
- Update `PROJECT_CHECKLIST.md` for only the items completed by this task.

Excluded:

- Spring Boot scaffold.
- React scaffold.
- Python worker refactor or code movement.
- Database migration files.
- Production deployment hardening.
- Secret manager integration.
- Backend/frontend container images.

## Repository Structure

Create the following directories:

```text
backend/
frontend/
worker/
infra/
docs/architecture/
docs/api/
docs/runbooks/
docs/security/
docs/testing/
```

Each empty directory should contain `.gitkeep` so Git tracks the intended layout.

MoneyPrinterTurbo source files remain in place for now. Moving or wrapping them into `worker/` will be a separate task after the worker boundary is specified.

## Compose Design

The new root `docker-compose.yml` defines local infrastructure services:

- `postgres`
  - Image: official PostgreSQL image.
  - Exposes `${POSTGRES_PORT:-5432}:5432`.
  - Uses database/user/password from `.env`.
  - Stores data in a named Docker volume.
  - Adds a healthcheck.

- `redis`
  - Image: official Redis image.
  - Exposes `${REDIS_PORT:-6379}:6379`.
  - Stores data in a named Docker volume if persistence is enabled by the image command.
  - Adds a healthcheck.

- `minio`
  - Image: official MinIO image.
  - Exposes API and console ports from `.env`.
  - Stores data in a named Docker volume.
  - Adds a healthcheck.

- `minio-init`
  - Uses the MinIO client image.
  - Waits for MinIO to be reachable.
  - Creates the local media bucket defined by `MINIO_BUCKET_MEDIA`.
  - Runs idempotently so repeated `make up` does not fail when the bucket already exists.

The compose file should not include backend/frontend/worker services until those codebases exist.

## Environment Design

Create `.env.example` with local-only defaults:

```text
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

Do not create or commit `.env`. Developers copy `.env.example` to `.env` locally.

Update `.gitignore` if necessary so `.env` stays untracked while `.env.example` remains tracked.

## Makefile Design

Add a root `Makefile` with these commands:

```bash
make env
make up
make down
make ps
make logs
make test
```

Command behavior:

- `make env`: creates `.env` from `.env.example` only if `.env` does not exist.
- `make up`: ensures `.env` exists, then runs `docker compose up -d`.
- `make down`: runs `docker compose down`.
- `make ps`: runs `docker compose ps`.
- `make logs`: runs `docker compose logs -f`.
- `make test`: runs checks that are valid at this phase, such as `docker compose config`.

No destructive clean command is added in this first task.

## Data and Secret Handling

The local defaults are development credentials only. They are acceptable in `.env.example` because they are not production secrets.

Rules:

- `.env` must not be committed.
- Production credentials must not be placed in `.env.example`.
- Docker named volumes hold local data.
- Removing local volumes is intentionally not automated in this first task.

## Error Handling

The foundation should fail early and clearly:

- `docker compose config` must fail if the compose file is invalid.
- `make up` must fail if Docker is unavailable.
- `minio-init` must be idempotent and safe to rerun.
- Healthchecks provide status for PostgreSQL, Redis, and MinIO.

## Verification

Minimum verification:

```bash
docker compose config
make ps
git status --short
```

If Docker daemon is available, also run:

```bash
make up
docker compose ps
make down
```

The task is complete only if the relevant commands exit successfully, or if unavailable Docker daemon state is reported explicitly with command output.

## Checklist Updates

After implementation, update `PROJECT_CHECKLIST.md` for completed items only:

- Root compose now manages local/dev infrastructure.
- PostgreSQL service exists.
- Redis service exists.
- MinIO service exists.
- `.env.example` exists.
- Makefile baseline commands exist.
- Target directory structure exists.

Do not mark backend, frontend, worker, migration, or production hardening items as complete.

## Commit

The implementation task should commit with:

```bash
infra: add local development foundation
```

The spec task commits separately with:

```bash
docs: design local development foundation
```
