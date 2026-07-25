.PHONY: env up down ps logs test backend-build backend-test backend-run

MAVEN_IMAGE ?= maven:3.9.9-eclipse-temurin-21
MAVEN_CACHE_DIR ?= $(CURDIR)/.cache/maven
BACKEND_MAVEN = mkdir -p "$(MAVEN_CACHE_DIR)" && docker run --rm --user "$(shell id -u):$(shell id -g)" --network host -e MAVEN_CONFIG=/tmp/.m2 -v "$(CURDIR)/backend:/workspace" -v "$(MAVEN_CACHE_DIR):/tmp/.m2" -w /workspace $(MAVEN_IMAGE)

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
