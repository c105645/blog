# Blog Platform — Backend Microservices

A production-ready, cloud-native blogging platform built with **Spring Boot 3.x** and **Spring Cloud**. The system is decomposed into four independent microservices that communicate through a centralised API gateway, backed by PostgreSQL, Redis, and RabbitMQ.

---

## Architecture Overview

```
Client
  │
  ▼
Edge Service (port 9000)               ← API Gateway + Rate Limiter + Circuit Breaker
  ├──▶ UserProfile Service (port 9010) ← Auth, User Management, Categories
  └──▶ Post Service (port 9020)        ← Posts, Comments, Votes
  
Config Service (port 8888)             ← Centralised Spring Cloud Config Server

Backing Services
  ├── PostgreSQL  (port 5432)          ← Primary database
  ├── Redis       (port 6379)          ← Session store + Rate limiting
  └── RabbitMQ    (port 5672 / 15672)  ← Message broker
```

---

## Services

| Service | Port | Description |
|---|---|---|
| `config-service` | `8888` | Spring Cloud Config Server — serves configuration from a Git repo |
| `userprofile-service` | `9010` | JWT auth (RSA), user profiles, follow/unfollow, categories |
| `post-service` | `9020` | Blog posts, comments, upvotes/downvotes, search |
| `edge-service` | `9000` | Spring Cloud Gateway — routing, Redis rate limiting, Resilience4j circuit breakers |

---

## Tech Stack

- **Java 17** · **Spring Boot 3.0.x** · **Spring Cloud 2022.0.1**
- **Spring Cloud Config** — externalised, Git-backed configuration
- **Spring Cloud Gateway** — reactive API gateway with Redis rate limiting
- **Spring Security OAuth2 Resource Server** — JWT with RSA key pair
- **Spring Data JPA + PostgreSQL** — persistence (Hibernate 6)
- **MapStruct 1.5.3** — compile-time DTO mapping
- **Lombok 1.18.26** — boilerplate reduction
- **Springdoc OpenAPI 2.1.0** — Swagger UI
- **Resilience4j** — circuit breaker on downstream calls
- **Redis** — session management and rate limiting
- **RabbitMQ** — async messaging
- **Docker Compose** · **Kubernetes** · **Tilt** — deployment options

---

## Prerequisites

| Tool | Minimum Version |
|---|---|
| JDK | 17 |
| Docker & Docker Compose | 20.x / 2.x |

---

## Quick Start — Two Commands

### Step 1 — Build all services

```bash
./build.sh
```

This builds a layered JAR for each service (skipping tests). You only need to re-run this when code changes.

### Step 2 — Run the full stack

```bash
./run.sh
```

This builds Docker images from the layered Dockerfiles and starts all containers. On subsequent runs it reuses cached image layers — only the changed application layer is rebuilt.

---

## Service URLs

| Service | URL |
|---|---|
| Edge Gateway | http://localhost:9000 |
| UserProfile Service | http://localhost:9010 |
| Post Service | http://localhost:9020 |
| Config Server | http://localhost:8888 |
| RabbitMQ Management UI | http://localhost:15672 (guest / guest) |

### Swagger UI

| Service | URL |
|---|---|
| UserProfile | http://localhost:9010/swagger-ui/index.html |
| Post | http://localhost:9020/swagger-ui/index.html |

### API Routes via Edge Gateway

| Prefix | Routed to |
|---|---|
| `/api/v1/userprofile/**` | `userprofile-service` |
| `/api/v1/post/**` | `post-service` |

---

## Docker Commands

```bash
# View running containers
docker compose -f blog-deployments/docker/docker-compose.yml ps

# Tail logs for a specific service
docker compose -f blog-deployments/docker/docker-compose.yml logs -f edge-service

# Stop all containers (keeps volumes)
docker compose -f blog-deployments/docker/docker-compose.yml down

# Stop and wipe the database volume
docker compose -f blog-deployments/docker/docker-compose.yml down -v

# Rebuild a single service image after a code change
cd post-service && ./gradlew bootJar -x test
docker compose -f blog-deployments/docker/docker-compose.yml up --build -d post-service
```

---

## How the Layered Docker Build Works

Each service uses a two-stage Dockerfile that mirrors what Spring Boot Buildpacks does internally:

```
Stage 1 — Extract layers from the JAR
  java -Djarmode=layertools -jar app.jar extract
      │
      ├── dependencies/          (all released Maven/Gradle deps)
      ├── spring-boot-loader/    (Spring Boot launcher classes)
      ├── snapshot-dependencies/ (any -SNAPSHOT deps)
      └── application/           (your compiled classes + resources)

Stage 2 — Copy layers in cache-optimal order
  COPY dependencies/          ← cached unless deps change
  COPY spring-boot-loader/    ← cached almost always
  COPY snapshot-dependencies/ ← cached unless snapshots change
  COPY application/           ← only layer rebuilt on code changes
```

Because Docker rebuilds from the first changed layer downward, a code-only change skips re-downloading all dependencies — rebuilds are fast after the first run.

---

## Configuration

### Externalised Config (Spring Cloud Config)

`config-service` fetches configuration from a remote Git repository configured in `config-service/src/main/resources/application.yml`. To use your own config repo:

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/<your-org>/config-repo.git
          username: <git-username>
          password: <git-token>
          default-label: main
```

### Environment Variables

| Variable | Default | Used by |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/blogdb` | `userprofile-service`, `post-service` |
| `SPRING_DATASOURCE_USERNAME` | `user` | `userprofile-service`, `post-service` |
| `SPRING_DATASOURCE_PASSWORD` | `password` | `userprofile-service`, `post-service` |
| `SPRING_CLOUD_CONFIG_URI` | `http://localhost:8888` | all services |
| `USERPROFILE_SERVICE_URL` | `http://localhost:9010` | `edge-service` |
| `POST_SERVICE_URL` | `http://localhost:9020` | `edge-service` |

### RSA Key Pair (JWT)

`userprofile-service` issues JWTs using its private key. All other services validate them using the public key. Place both under `src/main/resources/certs/` in each service:

```
src/main/resources/certs/
├── private.pem   ← userprofile-service only
└── public.pem    ← all services
```

Generate a fresh pair:

```bash
openssl genrsa -out private.pem 2048
openssl rsa -in private.pem -pubout -out public.pem
```

---

## Project Structure

```
backend-microservices/
├── build.sh                          ← Build all service JARs (run first)
├── run.sh                            ← Start the full Docker stack
│
├── blog-deployments/
│   └── docker/
│       └── docker-compose.yml        ← Full-stack deployment
│
├── config-service/                   ← Spring Cloud Config Server
│   ├── Dockerfile                    ← Layered build
│   └── build.gradle
│
├── userprofile-service/              ← Auth + User management
│   ├── Dockerfile
│   └── build.gradle
│
├── post-service/                     ← Posts, Comments, Votes
│   ├── Dockerfile
│   └── build.gradle
│
└── edge-service/                     ← API Gateway
    ├── Dockerfile
    └── build.gradle
```

---

## Troubleshooting

**`./build.sh` fails with `gradlew: Permission denied`**
```bash
find . -name "gradlew" -exec chmod +x {} \;
```

**Services fail to connect to config server on startup**
`config-service` must be fully up before other services start. The `depends_on` in `docker-compose.yml` handles ordering, but allow ~30 seconds on first startup for the config server to be ready.

**`post-service` or `userprofile-service` can't connect to PostgreSQL**
Check the postgres container is healthy: `docker compose logs blog-postgres`. On first run the database takes a few seconds to initialise.

**Edge service returns 503 / circuit breaker open**
A downstream service is unavailable. Check: `docker compose logs userprofile-service` or `docker compose logs post-service`.

**JWT 401 Unauthorized**
The `public.pem` in `post-service` and `edge-service` must match the `private.pem` in `userprofile-service`. All three must share the same RSA key pair.

**Rate limiter returns 429 Too Many Requests**
Default is 10 req/s (burst 20). Configured in `edge-service/src/main/resources/application.yml` under `spring.cloud.gateway.default-filters`.
