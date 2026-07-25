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
