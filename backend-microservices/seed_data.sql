-- =============================================================================
-- BLOG PLATFORM — SEED DATA SCRIPT
-- =============================================================================
-- Database   : blogdb (PostgreSQL)
-- Services   : userprofile-service + post-service (shared DB)
-- Inspired by: Medium / Dev.to / Hashnode
--
-- TABLES:
--   userprofile-service: categories, userprofile, user_categories, followers
--   post-service:        post, post_details, post_comment, post_votes,
--                        comment_votes
--
-- USAGE:
--   ./seed.sh               — runs against Docker container (default)
--   ./seed.sh localhost      — runs against local PostgreSQL
--   psql -U user -d blogdb -f seed_data.sql  — direct psql
--
-- PASSWORDS (BCrypt, strength 10):
--   Admin@123  → used for admin
--   Author@123 → used for all author accounts
--   Reader@123 → used for reader accounts
--
-- Run this script multiple times safely — it truncates first.
-- =============================================================================

\set ON_ERROR_STOP on
\echo '==> Starting blog seed data...'

-- ─────────────────────────────────────────────────────────────────────────────
-- CLEAN SLATE  (order respects FK constraints)
-- ─────────────────────────────────────────────────────────────────────────────
\echo '--> Truncating tables...'

TRUNCATE TABLE
    comment_votes,
    post_votes,
    post_comment,
    post_details,
    post,
    user_categories,
    followers,
    userprofile,
    categories
RESTART IDENTITY CASCADE;

-- Reset Hibernate sequences so IDs start clean
DO $$
DECLARE
    seq TEXT;
BEGIN
    FOR seq IN
        SELECT sequence_name FROM information_schema.sequences
        WHERE sequence_schema = 'public'
    LOOP
        EXECUTE format('ALTER SEQUENCE %I RESTART WITH 1', seq);
    END LOOP;
END $$;

-- ─────────────────────────────────────────────────────────────────────────────
-- 1. CATEGORIES  (userprofile-service → categories)
-- ─────────────────────────────────────────────────────────────────────────────
\echo '--> Inserting categories...'

INSERT INTO categories (id, name, description, created_at, updated_at) VALUES
(1,  'Technology',        'Latest trends in hardware, software, and emerging tech', CURRENT_DATE - 120, CURRENT_DATE),
(2,  'Programming',       'Code tutorials, best practices, and language deep-dives', CURRENT_DATE - 115, CURRENT_DATE),
(3,  'Web Development',   'Frontend, backend, APIs, and full-stack engineering',     CURRENT_DATE - 110, CURRENT_DATE),
(4,  'DevOps',            'CI/CD, containers, cloud infrastructure, and SRE',        CURRENT_DATE - 105, CURRENT_DATE),
(5,  'Machine Learning',  'AI, deep learning, data science, and LLM engineering',   CURRENT_DATE - 100, CURRENT_DATE),
(6,  'Career',            'Job hunting, interviews, productivity, and growth tips',  CURRENT_DATE - 95,  CURRENT_DATE),
(7,  'Open Source',       'Contributing to OSS, licences, and community building',  CURRENT_DATE - 90,  CURRENT_DATE),
(8,  'Security',          'AppSec, penetration testing, and secure coding',          CURRENT_DATE - 85,  CURRENT_DATE);

-- ─────────────────────────────────────────────────────────────────────────────
-- 2. USERS  (userprofile-service → userprofile)
-- ─────────────────────────────────────────────────────────────────────────────
-- Passwords (BCrypt strength 10):
--   Admin@123  → $2a$10$x/NVr3oPxU8PQ5VCE92cx.ID5aazZzSea11QkB8gMmcvMBwO1iqjm
--   Author@123 → $2a$10$uimuwbYQlhJp1yrEgnU7..BeXZD6ZSW6Dbd1oyEFTfDTLlVWE3LBC
--   Reader@123 → $2a$10$O.YBR3crkmzKt.vVtoJ6xu0d1MUK6QT9fLgUe7Em6Owz9YVPqbuca
-- ─────────────────────────────────────────────────────────────────────────────
\echo '--> Inserting users...'

INSERT INTO userprofile (id, username, first_name, last_name, password, email, is_admin, is_author, biography, image_url, created_at, updated_at) VALUES

-- Admin
(10, 'admin_ness', 'Admin', 'Ness',
 '$2a$10$x/NVr3oPxU8PQ5VCE92cx.ID5aazZzSea11QkB8gMmcvMBwO1iqjm',
 'admin@techblog.io', true, true,
 'Platform administrator. Keeping the lights on and the spam off.',
 'https://api.dicebear.com/7.x/initials/svg?seed=AdminNess',
 CURRENT_DATE - 200, CURRENT_DATE),

-- Authors
(11, 'sarah_writes', 'Sarah', 'Chen',
 '$2a$10$uimuwbYQlhJp1yrEgnU7..BeXZD6ZSW6Dbd1oyEFTfDTLlVWE3LBC',
 'sarah.chen@techblog.io', false, true,
 'Senior Software Engineer at a FAANG. I write about Java, Spring Boot, and distributed systems. Author of "Microservices in Practice".',
 'https://api.dicebear.com/7.x/avataaars/svg?seed=SarahChen',
 CURRENT_DATE - 180, CURRENT_DATE),

(12, 'alex_dev', 'Alex', 'Rodriguez',
 '$2a$10$uimuwbYQlhJp1yrEgnU7..BeXZD6ZSW6Dbd1oyEFTfDTLlVWE3LBC',
 'alex.rodriguez@techblog.io', false, true,
 'Full-stack developer obsessed with clean architecture and DX. React, Node, and occasionally Rust when I want to feel pain.',
 'https://api.dicebear.com/7.x/avataaars/svg?seed=AlexRodriguez',
 CURRENT_DATE - 160, CURRENT_DATE),

(13, 'priya_tech', 'Priya', 'Patel',
 '$2a$10$uimuwbYQlhJp1yrEgnU7..BeXZD6ZSW6Dbd1oyEFTfDTLlVWE3LBC',
 'priya.patel@techblog.io', false, true,
 'DevOps Engineer and Kubernetes enthusiast. I automate everything that can be automated and then automate the automation.',
 'https://api.dicebear.com/7.x/avataaars/svg?seed=PriyaPatel',
 CURRENT_DATE - 140, CURRENT_DATE),

(14, 'marcus_ml', 'Marcus', 'Thompson',
 '$2a$10$uimuwbYQlhJp1yrEgnU7..BeXZD6ZSW6Dbd1oyEFTfDTLlVWE3LBC',
 'marcus.thompson@techblog.io', false, true,
 'ML Engineer turned LLM wrangler. I build things with PyTorch, fine-tune models, and explain why your chatbot said that.',
 'https://api.dicebear.com/7.x/avataaars/svg?seed=MarcusThompson',
 CURRENT_DATE - 120, CURRENT_DATE),

(15, 'jessica_sec', 'Jessica', 'Kim',
 '$2a$10$uimuwbYQlhJp1yrEgnU7..BeXZD6ZSW6Dbd1oyEFTfDTLlVWE3LBC',
 'jessica.kim@techblog.io', false, true,
 'Application Security Engineer. CTF player, bug bounty hunter, and certified OWASP enthusiast. I find holes you didn''t know existed.',
 'https://api.dicebear.com/7.x/avataaars/svg?seed=JessicaKim',
 CURRENT_DATE - 100, CURRENT_DATE),

(16, 'tom_reader', 'Tom', 'Walker',
 '$2a$10$O.YBR3crkmzKt.vVtoJ6xu0d1MUK6QT9fLgUe7Em6Owz9YVPqbuca',
 'tom.walker@example.com', false, false,
 'Junior developer, avid reader, coffee addict.',
 'https://api.dicebear.com/7.x/avataaars/svg?seed=TomWalker',
 CURRENT_DATE - 80, CURRENT_DATE),

(17, 'amy_learns', 'Amy', 'Johnson',
 '$2a$10$O.YBR3crkmzKt.vVtoJ6xu0d1MUK6QT9fLgUe7Em6Owz9YVPqbuca',
 'amy.johnson@example.com', false, false,
 'CS student, aspiring backend engineer. Reading everything I can to break into tech.',
 'https://api.dicebear.com/7.x/avataaars/svg?seed=AmyJohnson',
 CURRENT_DATE - 60, CURRENT_DATE),

(18, 'dev_nina', 'Nina', 'Okonkwo',
 '$2a$10$uimuwbYQlhJp1yrEgnU7..BeXZD6ZSW6Dbd1oyEFTfDTLlVWE3LBC',
 'nina.okonkwo@techblog.io', false, true,
 'Open source contributor and technical writer. I demystify complex systems with diagrams and plain English.',
 'https://api.dicebear.com/7.x/avataaars/svg?seed=NinaOkonkwo',
 CURRENT_DATE - 50, CURRENT_DATE);

-- ─────────────────────────────────────────────────────────────────────────────
-- 3. USER → CATEGORY SUBSCRIPTIONS  (user_categories join table)
-- ─────────────────────────────────────────────────────────────────────────────
\echo '--> Inserting user category subscriptions...'

INSERT INTO user_categories (user_id, category_id) VALUES
-- sarah_writes: Programming, Web Development, Technology
(11, 1), (11, 2), (11, 3),
-- alex_dev: Web Development, Programming, Open Source
(12, 3), (12, 2), (12, 7),
-- priya_tech: DevOps, Technology, Security
(13, 4), (13, 1), (13, 8),
-- marcus_ml: Machine Learning, Technology, Programming
(14, 5), (14, 1), (14, 2),
-- jessica_sec: Security, Technology, DevOps
(15, 8), (15, 1), (15, 4),
-- nina_okonkwo: Open Source, Programming, Web Development
(18, 7), (18, 2), (18, 3),
-- Readers subscribe to a mix
(16, 2), (16, 3), (16, 6),
(17, 2), (17, 5), (17, 6);

-- ─────────────────────────────────────────────────────────────────────────────
-- 4. FOLLOW RELATIONSHIPS  (followers join table)
-- Column semantics: user_id = the person being followed, follower_id = who follows them
-- ─────────────────────────────────────────────────────────────────────────────
\echo '--> Inserting follow relationships...'

INSERT INTO followers (user_id, follower_id) VALUES
-- Everyone follows sarah_writes (star author)
(11, 12), (11, 13), (11, 14), (11, 15), (11, 16), (11, 17), (11, 18),
-- alex_dev followed by readers + peers
(12, 16), (12, 17), (12, 13), (12, 18),
-- priya followed by devops-interested users
(13, 14), (13, 16), (13, 18),
-- marcus followed by ML enthusiasts + readers
(14, 16), (14, 17), (14, 15),
-- jessica followed by security watchers
(15, 13), (15, 16), (15, 17),
-- nina followed by open-source crowd
(18, 12), (18, 17), (18, 16),
-- mutual follows between authors
(12, 11), (13, 11), (14, 12);

-- ─────────────────────────────────────────────────────────────────────────────
-- 4b. FIX COLUMN TYPES
-- @Lob on String with older Hibernate/PostgreSQL creates oid columns instead of
-- text. Convert them so plain string literals can be inserted.
-- ─────────────────────────────────────────────────────────────────────────────
\echo '--> Fixing oid → text column types (safe to run on already-text columns)...'

DO $$
BEGIN
    IF (SELECT data_type FROM information_schema.columns
        WHERE table_name = 'post' AND column_name = 'title') = 'oid' THEN
        ALTER TABLE post ALTER COLUMN title TYPE TEXT USING title::text;
    END IF;
    IF (SELECT data_type FROM information_schema.columns
        WHERE table_name = 'post' AND column_name = 'short_description') = 'oid' THEN
        ALTER TABLE post ALTER COLUMN short_description TYPE TEXT USING short_description::text;
    END IF;
    IF (SELECT data_type FROM information_schema.columns
        WHERE table_name = 'post_details' AND column_name = 'content') = 'oid' THEN
        ALTER TABLE post_details ALTER COLUMN content TYPE TEXT USING content::text;
    END IF;
END $$;

-- ─────────────────────────────────────────────────────────────────────────────
-- 5. POSTS  (post-service → post)
-- ─────────────────────────────────────────────────────────────────────────────
\echo '--> Inserting posts...'

INSERT INTO post (id, title, short_description, image_url, category, comment_count,
                  up_vote_count, down_vote_count, created_by, created_at, updated_at) VALUES

-- Sarah Chen's posts (Java / Microservices expert)
(100,
 'Building Event-Driven Microservices with Spring Boot and RabbitMQ',
 'A practical walkthrough of designing loosely-coupled services that communicate asynchronously. We''ll cover exchanges, queues, dead-letter patterns, and how to avoid losing messages when things go wrong.',
 'https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=800',
 'Programming', 14, 312, 8, 'sarah_writes',
 CURRENT_DATE - 75, CURRENT_DATE - 1),

(101,
 'Spring Security 6 JWT Authentication — The Definitive Guide',
 'Everything you need to implement stateless JWT auth in a Spring Boot 3 application. We cover RSA key pairs, the SecurityFilterChain, method-level security, and a ready-to-run Docker Compose setup.',
 'https://images.unsplash.com/photo-1555949963-ff9fe0c870eb?w=800',
 'Programming', 21, 489, 12, 'sarah_writes',
 CURRENT_DATE - 50, CURRENT_DATE),

(102,
 'Database Per Service vs Shared Database: A Microservices Trade-off',
 'A deep dive into one of the most contested microservices decisions. Pros and cons of each approach, how Hibernate helps, and which pattern this very codebase uses — and why.',
 'https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=800',
 'Technology', 9, 201, 19, 'sarah_writes',
 CURRENT_DATE - 30, CURRENT_DATE),

-- Alex Rodriguez's posts (Full-stack / React)
(103,
 'React 19 Actions: Async State Management Without Redux',
 'The new Actions API in React 19 lets you handle async mutations directly in forms and components. I''ll show you how to replace 90% of your Redux boilerplate with useTransition, useOptimistic, and the new use() hook.',
 'https://images.unsplash.com/photo-1633356122544-f134324a6cee?w=800',
 'Web Development', 18, 567, 23, 'alex_dev',
 CURRENT_DATE - 65, CURRENT_DATE - 2),

(104,
 'Clean Architecture in Node.js: Ports, Adapters, and Why It Matters',
 'Hexagonal architecture sounds academic until your codebase becomes untestable spaghetti. Here''s a practical Node.js implementation with real dependency injection, layered testing, and a folder structure you''ll actually understand six months later.',
 'https://images.unsplash.com/photo-1549923746-c502d488b3ea?w=800',
 'Web Development', 11, 334, 14, 'alex_dev',
 CURRENT_DATE - 40, CURRENT_DATE),

-- Priya Patel's posts (DevOps / K8s)
(105,
 'Kubernetes Probes Deep Dive: Liveness, Readiness, and Startup',
 'Misconfigured probes cause more production incidents than almost anything else. This guide explains exactly when each probe fires, what happens when they fail, and how to tune thresholds so you don''t evict healthy pods.',
 'https://images.unsplash.com/photo-1667372393119-3d4c48d07fc9?w=800',
 'DevOps', 16, 428, 11, 'priya_tech',
 CURRENT_DATE - 55, CURRENT_DATE - 3),

(106,
 'GitHub Actions for Java: Build, Test, and Deploy to AWS ECS in 15 Minutes',
 'A complete CI/CD pipeline from scratch. We''ll cache Gradle dependencies, run unit and integration tests in parallel, build a Docker image, push to ECR, and deploy a new task definition — all in one reusable workflow.',
 'https://images.unsplash.com/photo-1618401471353-b98afee0b2eb?w=800',
 'DevOps', 13, 356, 7, 'priya_tech',
 CURRENT_DATE - 25, CURRENT_DATE),

-- Marcus Thompson's posts (ML / LLMs)
(107,
 'Fine-Tuning LLaMA 3 on Your Own Data with QLoRA — No A100 Required',
 'You don''t need a $10,000 GPU cluster to fine-tune a 7B model. I''ll show you how to use QLoRA and the Hugging Face PEFT library to fine-tune on a single RTX 3090 in under four hours, with evaluation and a served endpoint.',
 'https://images.unsplash.com/photo-1677442135703-1787eea5ce01?w=800',
 'Machine Learning', 24, 712, 31, 'marcus_ml',
 CURRENT_DATE - 45, CURRENT_DATE - 1),

(108,
 'RAG vs Fine-Tuning: Choosing the Right LLM Adaptation Strategy',
 'Both Retrieval-Augmented Generation and fine-tuning can improve LLM performance on domain-specific tasks — but they solve different problems. This comparison covers latency, cost, freshness, and which to use for enterprise applications.',
 'https://images.unsplash.com/photo-1620712943543-bcc4688e7485?w=800',
 'Machine Learning', 19, 583, 22, 'marcus_ml',
 CURRENT_DATE - 20, CURRENT_DATE),

-- Jessica Kim's posts (Security)
(109,
 'OWASP Top 10 2025: What Changed and How to Remediate Each Issue',
 'The updated OWASP Top 10 reshuffled the rankings and added two new categories. I''ll walk through each vulnerability class with a real-world exploitation example, the relevant Spring Security defence, and a unit test that proves you''re safe.',
 'https://images.unsplash.com/photo-1563986768609-322da13575f3?w=800',
 'Security', 12, 398, 9, 'jessica_sec',
 CURRENT_DATE - 35, CURRENT_DATE),

-- Nina Okonkwo's posts (Open Source / Writing)
(110,
 'How I Got My First Pull Request Merged into a 10k-Star GitHub Project',
 'Contributing to open source feels intimidating until you realize maintainers desperately need help with documentation, tests, and small bug fixes. Here''s my step-by-step process from finding a good first issue to having my PR merged in 48 hours.',
 'https://images.unsplash.com/photo-1556075798-4825dfaaf498?w=800',
 'Open Source', 17, 445, 6, 'dev_nina',
 CURRENT_DATE - 18, CURRENT_DATE),

(111,
 'Writing Technical Docs That Developers Actually Read',
 'Bad documentation is the number-one reason developers abandon your library. I''ll share the Divio documentation framework (tutorials, how-tos, explanations, references), how to structure your README for scanners, and tools that keep docs in sync with code.',
 'https://images.unsplash.com/photo-1455390582262-044cdead277a?w=800',
 'Career', 8, 267, 4, 'dev_nina',
 CURRENT_DATE - 8, CURRENT_DATE);

-- ─────────────────────────────────────────────────────────────────────────────
-- 6. POST DETAILS  (post-service → post_details)
-- Shared primary key with post (MapsId — same id value)
-- ─────────────────────────────────────────────────────────────────────────────
\echo '--> Inserting post details (full article content)...'

INSERT INTO post_details (id, content, created_by, created_at, updated_at) VALUES

(100,
'## Introduction

Event-driven architecture is no longer a buzzword — it is the backbone of resilient, scalable microservice systems. In this post we will build two Spring Boot services that talk to each other exclusively through RabbitMQ messages.

## Why Event-Driven?

When Service A calls Service B synchronously via HTTP, a failure in B instantly degrades A. With messaging, A publishes an event and moves on. B consumes it when it is ready. If B is down, the message waits.

## Setting Up RabbitMQ with Spring AMQP

```java
@Configuration
public class RabbitConfig {
    public static final String POST_EXCHANGE  = "post.exchange";
    public static final String POST_QUEUE     = "post.created.queue";
    public static final String ROUTING_KEY    = "post.created";
    public static final String DLQ            = "post.created.dlq";

    @Bean TopicExchange postExchange() {
        return new TopicExchange(POST_EXCHANGE);
    }

    @Bean Queue postQueue() {
        return QueueBuilder.durable(POST_QUEUE)
            .withArgument("x-dead-letter-exchange", "")
            .withArgument("x-dead-letter-routing-key", DLQ)
            .build();
    }

    @Bean Binding binding(Queue q, TopicExchange ex) {
        return BindingBuilder.bind(q).to(ex).with(ROUTING_KEY);
    }
}
```

## Publishing an Event

```java
@Service
@RequiredArgsConstructor
public class PostEventPublisher {
    private final RabbitTemplate rabbit;

    public void publishPostCreated(Post post) {
        PostCreatedEvent event = PostCreatedEvent.from(post);
        rabbit.convertAndSend(
            RabbitConfig.POST_EXCHANGE,
            RabbitConfig.ROUTING_KEY,
            event
        );
    }
}
```

## Dead-Letter Queues

Never lose a message. Configure a DLQ so that messages that fail processing after the retry limit land somewhere safe for inspection.

## Testing with Testcontainers

```java
@SpringBootTest
@Testcontainers
class PostEventPublisherTest {
    @Container
    static RabbitMQContainer rabbit =
        new RabbitMQContainer("rabbitmq:3.12-management");
    // ...
}
```

## Conclusion

Event-driven patterns add operational complexity but pay dividends in resilience. Start with a single exchange and one queue, add dead-lettering from day one, and measure before optimising.
',
 'sarah_writes', CURRENT_DATE - 75, CURRENT_DATE - 1),

(101,
'## Why JWT in Spring Boot 3?

Spring Security 6 dropped the legacy `WebSecurityConfigurerAdapter` and moved everything to a functional `SecurityFilterChain`. Combined with the new OAuth2 resource server support, JWT auth is cleaner than ever.

## Generating RSA Key Pairs

```bash
openssl genrsa -out private.pem 2048
openssl rsa -in private.pem -pubout -out public.pem
```

Place both in `src/main/resources/certs/`.

## Configuring Spring Security

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final RsaKeyProperties rsaKeys;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
            .oauth2ResourceServer(o -> o.jwt(Customizer.withDefaults()))
            .authorizeHttpRequests(a -> a
                .requestMatchers("/api/v1/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .build();
    }

    @Bean JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    @Bean JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey())
            .privateKey(rsaKeys.privateKey()).build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(jwk)));
    }
}
```

## Issuing a Token

```java
@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtEncoder encoder;

    public String generateToken(Authentication auth) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("techblog")
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .subject(auth.getName())
            .claim("roles", auth.getAuthorities())
            .build();
        return encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
```

## Method-Level Security

Annotate your controllers with `@PreAuthorize("hasRole(''ADMIN'')")` and enable it with `@EnableMethodSecurity` on your config class.

## Docker Compose

```yaml
services:
  blog-postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: blogdb
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
```

This exact pattern is what the blog platform in this post uses. Check the source on GitHub.
',
 'sarah_writes', CURRENT_DATE - 50, CURRENT_DATE),

(102,
'## The Problem with a Shared Database

When two microservices share a database, their schemas are coupled. A migration in Service A can break Service B. You lose independent deployability — the key promise of microservices.

## Why This Platform Uses a Shared DB Anyway

We made a deliberate pragmatic choice: the `userprofile-service` and `post-service` both connect to `blogdb`. Here is why it is still acceptable at our current scale:

1. **Separate schemas** — userprofile tables and post tables have no foreign key constraints between them. The `post.created_by` column stores a username string, not a FK.
2. **Independent connection pools** — each service has its own Hikari pool capped at 5 connections.
3. **DDL isolation** — each service only runs migrations on its own tables via `ddl-auto: update`.

## When to Split

You should split to separate databases when:
- Teams independently deploy services on different schedules
- One service needs a different storage engine (e.g., post-service moving to Elasticsearch)
- Schema churn in one service is breaking the other

## The Saga Pattern for Cross-Service Transactions

Without shared DB, distributed transactions become sagas. The Choreography Saga works well with our RabbitMQ setup — each service publishes completion events that trigger the next step, with compensating transactions on failure.

## Conclusion

Neither pattern is universally correct. Start with a shared database, enforce logical boundaries through code, and split only when the pain of coupling outweighs the operational cost of a second database.
',
 'sarah_writes', CURRENT_DATE - 30, CURRENT_DATE),

(103,
'## What Are React 19 Actions?

React 19 introduces a first-class concept of "Actions" — functions that can be async and are triggered by user interactions. The runtime handles pending state, errors, and optimistic updates for you.

## useTransition + async function

```jsx
function LikeButton({ postId }) {
  const [isPending, startTransition] = useTransition();

  function handleClick() {
    startTransition(async () => {
      await likePost(postId);
    });
  }

  return (
    <button onClick={handleClick} disabled={isPending}>
      {isPending ? "Liking..." : "Like"}
    </button>
  );
}
```

No Redux. No `useState` for loading. The transition handles it.

## useOptimistic

```jsx
function CommentList({ comments }) {
  const [optimisticComments, addOptimistic] = useOptimistic(comments);

  async function submitComment(text) {
    addOptimistic({ id: "temp", text, pending: true });
    await postComment(text);
  }
  // render optimisticComments
}
```

The optimistic update appears instantly. If the server call fails, it rolls back automatically.

## The new `use()` hook

```jsx
function Post({ postPromise }) {
  const post = use(postPromise); // suspends until resolved
  return <article>{post.title}</article>;
}
```

`use()` can also read context conditionally — the first hook that can be used inside `if` blocks.

## When to Still Use Redux

Global auth state, complex offline sync, and DevTools-driven debugging still benefit from Redux Toolkit. But for server mutation state, Actions are now the idiomatic choice.
',
 'alex_dev', CURRENT_DATE - 65, CURRENT_DATE - 2),

(104,
'## What is Hexagonal Architecture?

Also called Ports and Adapters, it defines your application as a hexagon with:
- **Core domain** — pure business logic with zero framework dependencies
- **Ports** — interfaces the core exposes (inbound) or requires (outbound)
- **Adapters** — concrete implementations (HTTP controllers, database repos, message queues)

## Folder Structure

```
src/
  domain/          ← pure business rules, no Node/Express
    post.entity.ts
    post.repository.ts  ← outbound port (interface)
    post.service.ts
  application/     ← use case orchestration
    create-post.use-case.ts
  adapters/
    inbound/
      http/        ← Express controllers
    outbound/
      postgres/    ← TypeORM repositories (adapters)
      rabbit/      ← event publishers
```

## The Repository Port

```typescript
// domain/post.repository.ts — just an interface
export interface PostRepository {
  save(post: Post): Promise<Post>;
  findById(id: string): Promise<Post | null>;
  findByCategory(cat: string): Promise<Post[]>;
}
```

## The Postgres Adapter

```typescript
// adapters/outbound/postgres/postgres-post.repository.ts
@Injectable()
export class PostgresPostRepository implements PostRepository {
  constructor(private readonly ds: DataSource) {}

  async save(post: Post): Promise<Post> {
    return this.ds.getRepository(PostEntity).save(post.toPersistence());
  }
}
```

## Testing the Use Case in Pure Node

```typescript
const fakeRepo: PostRepository = { save: jest.fn(), ... };
const useCase = new CreatePostUseCase(fakeRepo);
await useCase.execute(input);
expect(fakeRepo.save).toHaveBeenCalledWith(expect.objectContaining({ title: input.title }));
```

No database, no HTTP, instant tests.
',
 'alex_dev', CURRENT_DATE - 40, CURRENT_DATE),

(105,
'## The Three Probe Types

| Probe | Question | Failure action |
|---|---|---|
| Liveness | Is the container alive? | Kill and restart |
| Readiness | Is the container ready for traffic? | Remove from Service endpoints |
| Startup | Has the app finished starting? | Give it more time before liveness kicks in |

## Why Startup Probes Matter

Without a startup probe, Kubernetes runs the liveness probe immediately. A slow JVM startup (Spring Boot can take 30+ seconds) will cause the pod to be killed in a restart loop before it ever serves traffic.

```yaml
startupProbe:
  httpGet:
    path: /actuator/health/liveness
    port: 9010
  failureThreshold: 30
  periodSeconds: 5   # allows up to 150s for startup
```

## Correct Readiness for a Database-Dependent Service

```yaml
readinessProbe:
  httpGet:
    path: /actuator/health/readiness
    port: 9010
  initialDelaySeconds: 10
  periodSeconds: 5
  failureThreshold: 3
```

Spring Boot Actuator''s readiness indicator automatically checks all registered `@Component` beans that implement `HealthIndicator`, including DataSource connectivity.

## Common Mistake: Using the Same Endpoint for Both

If your liveness and readiness point to the same `/health` endpoint and your DB goes down, Kubernetes will kill and restart the pod. But restarting doesn''t fix a broken DB — you just cycle pods endlessly. Keep them separate with Spring Boot''s `/health/liveness` and `/health/readiness` split.

## The Minimum Viable Probe Config

```yaml
livenessProbe:
  httpGet: { path: /actuator/health/liveness, port: 9010 }
  periodSeconds: 10
  failureThreshold: 3
readinessProbe:
  httpGet: { path: /actuator/health/readiness, port: 9010 }
  periodSeconds: 5
  failureThreshold: 3
startupProbe:
  httpGet: { path: /actuator/health/liveness, port: 9010 }
  failureThreshold: 30
  periodSeconds: 5
```
',
 'priya_tech', CURRENT_DATE - 55, CURRENT_DATE - 3),

(106,
'## The Complete Workflow File

```yaml
name: Build and Deploy

on:
  push:
    branches: [main]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - uses: actions/setup-java@v4
        with: { java-version: 21, distribution: temurin }

      - name: Cache Gradle packages
        uses: actions/cache@v4
        with:
          path: |
            ~/.gradle/caches
            ~/.gradle/wrapper
          key: gradle-${{ hashFiles("**/*.gradle*") }}

      - name: Run tests
        run: ./gradlew test --parallel

      - name: Build Docker image
        run: |
          docker build -t $ECR_REGISTRY/$ECR_REPO:$GITHUB_SHA .

      - name: Push to ECR
        env:
          AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
          AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
        run: |
          aws ecr get-login-password --region us-east-1 | docker login ...
          docker push $ECR_REGISTRY/$ECR_REPO:$GITHUB_SHA

      - name: Deploy new ECS task
        run: |
          aws ecs update-service --cluster blog-cluster \
            --service post-service --force-new-deployment
```

## Parallelising Tests

Split integration tests into a separate job so unit tests fail fast:

```yaml
jobs:
  unit-tests:
    runs-on: ubuntu-latest
    steps:
      - run: ./gradlew test
  integration-tests:
    needs: unit-tests
    services:
      postgres:
        image: postgres:16
        env: { POSTGRES_DB: testdb, ... }
    steps:
      - run: ./gradlew integrationTest
```

## Caching Impact

Without Gradle cache: ~4 min 20 sec. With cache: ~1 min 10 sec. The `hashFiles` key invalidates only when your build files change.
',
 'priya_tech', CURRENT_DATE - 25, CURRENT_DATE),

(107,
'## What is QLoRA?

QLoRA (Quantized Low-Rank Adaptation) lets you fine-tune a large model by:
1. Quantising base weights to 4-bit (NF4 format) — dramatically cuts VRAM
2. Training only small "adapter" weight matrices (LoRA) — dramatically cuts trainable parameters

A 7B model that normally needs 56 GB VRAM fine-tunes on 12 GB with QLoRA.

## Setup

```bash
pip install transformers peft bitsandbytes accelerate datasets
```

## Loading the Model in 4-bit

```python
from transformers import AutoModelForCausalLM, BitsAndBytesConfig

bnb_config = BitsAndBytesConfig(
    load_in_4bit=True,
    bnb_4bit_use_double_quant=True,
    bnb_4bit_quant_type="nf4",
    bnb_4bit_compute_dtype=torch.bfloat16,
)

model = AutoModelForCausalLM.from_pretrained(
    "meta-llama/Meta-Llama-3-8B",
    quantization_config=bnb_config,
    device_map="auto",
)
```

## Attaching LoRA Adapters

```python
from peft import LoraConfig, get_peft_model

lora_config = LoraConfig(
    r=16,
    lora_alpha=32,
    target_modules=["q_proj", "v_proj"],
    lora_dropout=0.05,
    bias="none",
    task_type="CAUSAL_LM",
)

model = get_peft_model(model, lora_config)
model.print_trainable_parameters()
# trainable params: 6,815,744 || all params: 6,745,088,000 || 0.10%
```

## Training with SFTTrainer

```python
from trl import SFTTrainer

trainer = SFTTrainer(
    model=model,
    train_dataset=dataset,
    dataset_text_field="text",
    max_seq_length=2048,
    args=TrainingArguments(
        per_device_train_batch_size=4,
        gradient_accumulation_steps=4,
        num_train_epochs=3,
        learning_rate=2e-4,
        fp16=True,
        output_dir="./qlora-llama3",
    ),
)
trainer.train()
```

## Serving the Fine-Tuned Model

```python
from peft import PeftModel

base = AutoModelForCausalLM.from_pretrained("meta-llama/Meta-Llama-3-8B")
model = PeftModel.from_pretrained(base, "./qlora-llama3")
model = model.merge_and_unload()  # fuse for faster inference
```

Total training time on RTX 3090: ~3 h 40 min for 10k samples at 3 epochs.
',
 'marcus_ml', CURRENT_DATE - 45, CURRENT_DATE - 1),

(108,
'## The Core Difference

**RAG** retrieves external knowledge at inference time. **Fine-tuning** bakes knowledge into weights during training.

## When to Use RAG

- You need **fresh, frequently updated** data (news, docs, support tickets)
- Your dataset is too large to fit into a context window but small enough for a vector DB
- You want **source attribution** — knowing which document the answer came from
- You cannot afford compute for fine-tuning

```python
# RAG pipeline sketch
query_embedding = embed(user_query)
docs = vector_db.similarity_search(query_embedding, k=5)
context = "\n".join(doc.text for doc in docs)
response = llm.generate(f"Context:\n{context}\n\nQuestion: {user_query}")
```

## When to Use Fine-Tuning

- You want the model to adopt a **specific style or format** (JSON output, medical terminology)
- You have a **small, high-quality** labelled dataset (<100k examples)
- **Latency** is critical — RAG adds retrieval RTT to every request
- The knowledge is stable and won''t change frequently

## The Hybrid Approach

Many production systems combine both: fine-tune for style/format, add RAG for factual grounding.

| | RAG | Fine-tuning | Hybrid |
|---|---|---|---|
| Knowledge freshness | ✅ Live | ❌ Stale | ✅ Live |
| Inference latency | ⚠️ + retrieval | ✅ Fast | ⚠️ + retrieval |
| Training cost | Free | 💰💰 | 💰💰 |
| Hallucination risk | Low (cited) | Medium | Low |

## Recommendation

Start with RAG. It is faster to iterate, cheaper, and easier to debug. Fine-tune only when RAG cannot meet your style or latency requirements.
',
 'marcus_ml', CURRENT_DATE - 20, CURRENT_DATE),

(109,
'## What Changed in the 2025 Update

The 2025 OWASP Top 10 makes three major structural changes:
1. **A01 Broken Access Control** remains #1 — still the most widespread
2. **A07 Identification & Authentication** merged with session management issues
3. **NEW: A10 Server-Side Request Forgery (SSRF)** elevated from "next watch"

## A01 — Broken Access Control: Spring Security Fix

```java
// Vulnerable — relies only on URL pattern
http.authorizeHttpRequests(a -> a
    .requestMatchers("/api/admin/**").hasRole("ADMIN")
    .anyRequest().authenticated()
);

// Safer — add method-level security too
@PreAuthorize("hasRole(''ADMIN'') and #userId == authentication.name")
public UserProfile getProfile(String userId) { ... }
```

## A03 — Injection: Parameterised Queries

```java
// VULNERABLE
String sql = "SELECT * FROM post WHERE title = ''" + userInput + "''";

// SAFE — Spring Data JPA
@Query("SELECT p FROM Post p WHERE p.title = :title")
List<Post> findByTitle(@Param("title") String title);
```

## A10 — SSRF: Allowlisting Outbound Calls

```java
private static final Set<String> ALLOWED_HOSTS = Set.of("api.trusted.com");

public String fetch(String url) {
    URI uri = URI.create(url);
    if (!ALLOWED_HOSTS.contains(uri.getHost())) {
        throw new SecurityException("SSRF blocked: " + uri.getHost());
    }
    return restTemplate.getForObject(url, String.class);
}
```

## Writing the Regression Test

```java
@Test
void ssrf_blocked_for_internal_host() {
    assertThatThrownBy(() -> service.fetch("http://169.254.169.254/latest/meta-data"))
        .isInstanceOf(SecurityException.class);
}
```

Ship the test before you ship the fix. If it goes red in CI, someone removed your defence.
',
 'jessica_sec', CURRENT_DATE - 35, CURRENT_DATE),

(110,
'## Step 1: Find the Right Issue

Filter by `good first issue` AND `help wanted`. The first label means "simple enough for a newcomer", the second means "we actually want external contributions here". Both together is your sweet spot.

I found my issue in a Spring Boot starter library: a test was using a deprecated API and the maintainer had left a TODO comment.

## Step 2: Read CONTRIBUTING.md Before Touching Code

Every serious project has one. Mine required:
- Signing a CLA (5 minutes)
- Running `./gradlew check` before pushing
- Linking the issue number in the PR description

Missing any of these gets your PR closed without review.

## Step 3: Fork, Branch, Fix

```bash
git clone https://github.com/YOUR_USERNAME/the-project
git checkout -b fix/deprecated-test-api-1234
# make the change — 12 lines in my case
git commit -m "test: replace deprecated MockMvc with MockMvcTester (fixes #1234)"
git push origin fix/deprecated-test-api-1234
```

## Step 4: Write the PR Description

```markdown
## Problem
`MockMvcBuilders.standaloneSetup()` is deprecated in Spring 6.2.
Fixes #1234

## Solution
Replace with `MockMvcTester.of()` per the migration guide.

## Testing
`./gradlew test` passes locally on JDK 21.
```

## Result

The maintainer responded in 4 hours, asked one clarifying question, and merged in 48 hours. My name is now in the release notes of a project used by 40,000 developers.

The fix was 12 lines. The career signal is worth far more.
',
 'dev_nina', CURRENT_DATE - 18, CURRENT_DATE),

(111,
'## The Divio Documentation Framework

Every codebase needs four types of docs — and most have only one.

| Type | Answers | Analogy |
|---|---|---|
| **Tutorial** | "How do I get started?" | Cooking lesson |
| **How-to guide** | "How do I do X?" | Recipe |
| **Explanation** | "Why does it work this way?" | Essay |
| **Reference** | "What does this parameter do?" | Encyclopedia |

Most libraries only ship a reference. That is why nobody can figure out how to use them.

## README Structure That Works

```markdown
# Your Library Name

One sentence: what problem it solves.

## Quick Start (Tutorial)
<!-- Working example in 10 lines or fewer -->

## Common Tasks (How-to)
- [How to authenticate](./docs/how-to-auth.md)
- [How to handle errors](./docs/how-to-errors.md)

## Architecture (Explanation)
<!-- Why the API is designed this way -->

## API Reference
<!-- Auto-generated from code — keep it short here -->
```

## Tools That Keep Docs in Sync

- **OpenAPI / Swagger** — generated from annotations, never drifts from the API
- **Doctest / Doctest-ts** — runs code examples in your docs as tests
- **Stale bot** — flags docs files unchanged for >6 months

## The One Rule

If a developer reads your README and cannot run your library in 10 minutes, the README has failed. Everything else is commentary.
',
 'dev_nina', CURRENT_DATE - 8, CURRENT_DATE);

-- ─────────────────────────────────────────────────────────────────────────────
-- 7. COMMENTS  (post-service → post_comment)
-- ─────────────────────────────────────────────────────────────────────────────
\echo '--> Inserting comments...'

INSERT INTO post_comment (id, post_id, review, up_vote_count, down_vote_count, created_by, created_at, updated_at) VALUES

-- Comments on post 100 (RabbitMQ)
(200, 100, 'This is exactly what I needed. The dead-letter queue section saved me hours of debugging in production last week.', 45, 1, 'alex_dev',        CURRENT_DATE - 70, CURRENT_DATE - 70),
(201, 100, 'Great article! One thing worth adding — the connection factory retry config. Without it, a broker restart during heavy load will drop connections silently.', 38, 0, 'priya_tech',      CURRENT_DATE - 68, CURRENT_DATE - 68),
(202, 100, 'Do you have a GitHub repo with the full example? I''d love to see the consumer side.', 12, 0, 'tom_reader',       CURRENT_DATE - 66, CURRENT_DATE - 66),
(203, 100, 'How does this compare to Kafka for this use case? We''re evaluating both right now.', 28, 2, 'marcus_ml',        CURRENT_DATE - 64, CURRENT_DATE - 64),

-- Comments on post 101 (JWT)
(204, 101, 'Finally a guide that shows the RSA key setup end-to-end. Every other post just skips that part and assumes you already have certs.', 67, 0, 'tom_reader',       CURRENT_DATE - 45, CURRENT_DATE - 45),
(205, 101, 'The `use()` in SecurityConfig is elegant. I''ve been wiring this manually for years — didn''t realise Spring Boot autoconfigures the decoder from the PEM file.', 51, 1, 'alex_dev',        CURRENT_DATE - 43, CURRENT_DATE - 43),
(206, 101, 'Worth noting that you should validate the `iss` and `aud` claims in production. JWTs signed with your key are valid unless you check what they''re *for*.', 89, 0, 'jessica_sec',      CURRENT_DATE - 41, CURRENT_DATE - 41),
(207, 101, 'The method-level security part is the piece I always forget. Good reminder.', 33, 0, 'priya_tech',      CURRENT_DATE - 39, CURRENT_DATE - 39),
(208, 101, 'Can you show how to pass the username through the JWT so `@CreatedBy` picks it up from the SecurityContext?', 44, 0, 'amy_learns',       CURRENT_DATE - 37, CURRENT_DATE - 37),

-- Comments on post 103 (React 19)
(209, 103, 'useOptimistic is the game-changer here. I implemented it in our feed last month and the perceived performance difference is night and day.', 78, 2, 'tom_reader',       CURRENT_DATE - 60, CURRENT_DATE - 60),
(210, 103, 'Curious how `use()` interacts with Suspense boundaries. Does it need a Suspense wrapper the same way `React.lazy` does?', 42, 0, 'dev_nina',         CURRENT_DATE - 58, CURRENT_DATE - 58),
(211, 103, 'I''ve been using TanStack Query for all of this. Is there a reason to switch to native Actions now?', 56, 3, 'jessica_sec',      CURRENT_DATE - 56, CURRENT_DATE - 56),
(212, 103, 'The Redux section at the end is important. People will read this and throw Redux away entirely, which is a mistake for large auth-heavy apps.', 61, 4, 'priya_tech',      CURRENT_DATE - 54, CURRENT_DATE - 54),

-- Comments on post 105 (K8s Probes)
(213, 105, 'The "same endpoint for both" mistake cost us a 45-minute outage. Pod restart loop because the DB was slow, not dead. This post should be required reading for all K8s users.', 93, 0, 'alex_dev',        CURRENT_DATE - 50, CURRENT_DATE - 50),
(214, 105, 'I''d add: set `terminationGracePeriodSeconds` to at least 60s on JVM workloads. The default 30s is often not enough for Spring to gracefully drain.', 71, 0, 'sarah_writes',     CURRENT_DATE - 48, CURRENT_DATE - 48),
(215, 105, 'How does this work with Spring Native / GraalVM? Startup time is <1s so the startup probe thresholds change dramatically.', 29, 0, 'marcus_ml',        CURRENT_DATE - 46, CURRENT_DATE - 46),
(216, 105, 'Excellent breakdown of the probe types. Bookmarked.', 18, 0, 'amy_learns',       CURRENT_DATE - 44, CURRENT_DATE - 44),

-- Comments on post 107 (QLoRA)
(217, 107, 'Just followed this and got it running on a 3080. Had to drop batch size to 2 but otherwise worked first try. Thank you!', 112, 1, 'amy_learns',       CURRENT_DATE - 40, CURRENT_DATE - 40),
(218, 107, 'The merge_and_unload() step at the end is critical for anyone serving this model — LoRA inference with separate weights is noticeably slower.', 88, 0, 'jessica_sec',      CURRENT_DATE - 38, CURRENT_DATE - 38),
(219, 107, 'How do you evaluate quality after fine-tuning? Any benchmarks you recommend beyond perplexity?', 54, 0, 'dev_nina',         CURRENT_DATE - 36, CURRENT_DATE - 36),
(220, 107, 'Would love to see a follow-up on merging multiple LoRA adapters for different tasks.', 67, 0, 'sarah_writes',     CURRENT_DATE - 34, CURRENT_DATE - 34),

-- Comments on post 109 (OWASP)
(221, 109, 'The SSRF allowlist example is exactly how we fixed a critical finding from our last pentest. Simple and effective.', 74, 0, 'priya_tech',      CURRENT_DATE - 30, CURRENT_DATE - 30),
(222, 109, 'The "ship the test before the fix" advice is gold. We call it Security TDD internally.', 91, 0, 'alex_dev',        CURRENT_DATE - 28, CURRENT_DATE - 28),

-- Comments on post 110 (OSS contribution)
(223, 110, 'This mirrors my experience exactly. The CONTRIBUTING.md is where 80% of first-time contributors fail. Read it first, always.', 58, 0, 'sarah_writes',     CURRENT_DATE - 15, CURRENT_DATE - 15),
(224, 110, 'I work on a 15k-star project. We are DESPERATE for people to fix deprecated tests and improve docs. Please send more contributors like you.', 102, 0, 'marcus_ml',        CURRENT_DATE - 13, CURRENT_DATE - 13),
(225, 110, 'What CLA tool did they use? Some CLAs are 10-page legal documents that take days to process.', 23, 0, 'tom_reader',       CURRENT_DATE - 11, CURRENT_DATE - 11),

-- Comments on post 111 (Tech Docs)
(226, 111, 'The Divio framework changed how I think about docs. My team now categorises every page before writing it. Huge improvement in quality.', 84, 0, 'alex_dev',        CURRENT_DATE - 6, CURRENT_DATE - 6),
(227, 111, 'Doctest is underrated. We have 40 code examples in our docs and they all run in CI now. Zero drift since we added it.', 63, 0, 'priya_tech',      CURRENT_DATE - 4, CURRENT_DATE - 4);

-- ─────────────────────────────────────────────────────────────────────────────
-- 8. POST VOTES  (post-service → post_votes)
-- score: +1 = upvote, -1 = downvote
-- ─────────────────────────────────────────────────────────────────────────────
\echo '--> Inserting post votes...'

INSERT INTO post_votes (id, post_id, score, created_by, created_at, updated_at) VALUES
-- Votes on post 100 (RabbitMQ)
(300, 100,  1, 'alex_dev',     CURRENT_DATE - 74, CURRENT_DATE - 74),
(301, 100,  1, 'priya_tech',   CURRENT_DATE - 73, CURRENT_DATE - 73),
(302, 100,  1, 'marcus_ml',    CURRENT_DATE - 72, CURRENT_DATE - 72),
(303, 100,  1, 'tom_reader',   CURRENT_DATE - 71, CURRENT_DATE - 71),
(304, 100,  1, 'amy_learns',   CURRENT_DATE - 70, CURRENT_DATE - 70),
(305, 100, -1, 'jessica_sec',  CURRENT_DATE - 69, CURRENT_DATE - 69),

-- Votes on post 101 (JWT) — highest voted
(306, 101,  1, 'alex_dev',     CURRENT_DATE - 48, CURRENT_DATE - 48),
(307, 101,  1, 'priya_tech',   CURRENT_DATE - 47, CURRENT_DATE - 47),
(308, 101,  1, 'marcus_ml',    CURRENT_DATE - 46, CURRENT_DATE - 46),
(309, 101,  1, 'jessica_sec',  CURRENT_DATE - 45, CURRENT_DATE - 45),
(310, 101,  1, 'tom_reader',   CURRENT_DATE - 44, CURRENT_DATE - 44),
(311, 101,  1, 'amy_learns',   CURRENT_DATE - 43, CURRENT_DATE - 43),
(312, 101, -1, 'dev_nina',     CURRENT_DATE - 42, CURRENT_DATE - 42),

-- Votes on post 103 (React 19)
(313, 103,  1, 'sarah_writes', CURRENT_DATE - 63, CURRENT_DATE - 63),
(314, 103,  1, 'priya_tech',   CURRENT_DATE - 62, CURRENT_DATE - 62),
(315, 103,  1, 'jessica_sec',  CURRENT_DATE - 61, CURRENT_DATE - 61),
(316, 103,  1, 'tom_reader',   CURRENT_DATE - 60, CURRENT_DATE - 60),
(317, 103,  1, 'amy_learns',   CURRENT_DATE - 59, CURRENT_DATE - 59),
(318, 103, -1, 'dev_nina',     CURRENT_DATE - 58, CURRENT_DATE - 58),

-- Votes on post 105 (K8s Probes)
(319, 105,  1, 'sarah_writes', CURRENT_DATE - 53, CURRENT_DATE - 53),
(320, 105,  1, 'alex_dev',     CURRENT_DATE - 52, CURRENT_DATE - 52),
(321, 105,  1, 'marcus_ml',    CURRENT_DATE - 51, CURRENT_DATE - 51),
(322, 105,  1, 'tom_reader',   CURRENT_DATE - 50, CURRENT_DATE - 50),
(323, 105, -1, 'jessica_sec',  CURRENT_DATE - 49, CURRENT_DATE - 49),

-- Votes on post 107 (QLoRA) — second highest
(324, 107,  1, 'sarah_writes', CURRENT_DATE - 43, CURRENT_DATE - 43),
(325, 107,  1, 'alex_dev',     CURRENT_DATE - 42, CURRENT_DATE - 42),
(326, 107,  1, 'priya_tech',   CURRENT_DATE - 41, CURRENT_DATE - 41),
(327, 107,  1, 'jessica_sec',  CURRENT_DATE - 40, CURRENT_DATE - 40),
(328, 107,  1, 'tom_reader',   CURRENT_DATE - 39, CURRENT_DATE - 39),
(329, 107,  1, 'amy_learns',   CURRENT_DATE - 38, CURRENT_DATE - 38),
(330, 107, -1, 'dev_nina',     CURRENT_DATE - 37, CURRENT_DATE - 37),

-- Votes on newer posts
(331, 108,  1, 'sarah_writes', CURRENT_DATE - 18, CURRENT_DATE - 18),
(332, 108,  1, 'alex_dev',     CURRENT_DATE - 17, CURRENT_DATE - 17),
(333, 108,  1, 'jessica_sec',  CURRENT_DATE - 16, CURRENT_DATE - 16),
(334, 108,  1, 'tom_reader',   CURRENT_DATE - 15, CURRENT_DATE - 15),
(335, 109,  1, 'alex_dev',     CURRENT_DATE - 33, CURRENT_DATE - 33),
(336, 109,  1, 'priya_tech',   CURRENT_DATE - 32, CURRENT_DATE - 32),
(337, 109,  1, 'marcus_ml',    CURRENT_DATE - 31, CURRENT_DATE - 31),
(338, 110,  1, 'marcus_ml',    CURRENT_DATE - 16, CURRENT_DATE - 16),
(339, 110,  1, 'priya_tech',   CURRENT_DATE - 15, CURRENT_DATE - 15),
(340, 110,  1, 'jessica_sec',  CURRENT_DATE - 14, CURRENT_DATE - 14),
(341, 111,  1, 'sarah_writes', CURRENT_DATE - 7,  CURRENT_DATE - 7),
(342, 111,  1, 'priya_tech',   CURRENT_DATE - 6,  CURRENT_DATE - 6),
(343, 102,  1, 'alex_dev',     CURRENT_DATE - 28, CURRENT_DATE - 28),
(344, 102, -1, 'marcus_ml',    CURRENT_DATE - 27, CURRENT_DATE - 27),
(345, 104,  1, 'jessica_sec',  CURRENT_DATE - 38, CURRENT_DATE - 38),
(346, 104,  1, 'sarah_writes', CURRENT_DATE - 37, CURRENT_DATE - 37),
(347, 106,  1, 'alex_dev',     CURRENT_DATE - 23, CURRENT_DATE - 23),
(348, 106,  1, 'sarah_writes', CURRENT_DATE - 22, CURRENT_DATE - 22);

-- ─────────────────────────────────────────────────────────────────────────────
-- 9. COMMENT VOTES  (post-service → comment_votes)
-- ─────────────────────────────────────────────────────────────────────────────
\echo '--> Inserting comment votes...'

INSERT INTO comment_votes (id, comment_id, score, created_by, created_at, updated_at) VALUES
-- Votes on popular comments
(400, 206,  1, 'sarah_writes', CURRENT_DATE - 40, CURRENT_DATE - 40),  -- jessica's JWT security note
(401, 206,  1, 'priya_tech',   CURRENT_DATE - 40, CURRENT_DATE - 40),
(402, 206,  1, 'alex_dev',     CURRENT_DATE - 39, CURRENT_DATE - 39),
(403, 213,  1, 'sarah_writes', CURRENT_DATE - 49, CURRENT_DATE - 49),  -- K8s outage story
(404, 213,  1, 'priya_tech',   CURRENT_DATE - 48, CURRENT_DATE - 48),
(405, 213,  1, 'marcus_ml',    CURRENT_DATE - 48, CURRENT_DATE - 48),
(406, 217,  1, 'sarah_writes', CURRENT_DATE - 39, CURRENT_DATE - 39),  -- QLoRA worked on 3080
(407, 217,  1, 'priya_tech',   CURRENT_DATE - 38, CURRENT_DATE - 38),
(408, 217,  1, 'alex_dev',     CURRENT_DATE - 37, CURRENT_DATE - 37),
(409, 222,  1, 'jessica_sec',  CURRENT_DATE - 27, CURRENT_DATE - 27),  -- Security TDD
(410, 222,  1, 'priya_tech',   CURRENT_DATE - 26, CURRENT_DATE - 26),
(411, 224,  1, 'amy_learns',   CURRENT_DATE - 12, CURRENT_DATE - 12),  -- maintainer reply
(412, 224,  1, 'tom_reader',   CURRENT_DATE - 11, CURRENT_DATE - 11),
(413, 204,  1, 'priya_tech',   CURRENT_DATE - 44, CURRENT_DATE - 44),  -- RSA key setup comment
(414, 204,  1, 'alex_dev',     CURRENT_DATE - 43, CURRENT_DATE - 43),
(415, 209,  1, 'sarah_writes', CURRENT_DATE - 59, CURRENT_DATE - 59),  -- useOptimistic comment
(416, 209,  1, 'priya_tech',   CURRENT_DATE - 58, CURRENT_DATE - 58),
(417, 214, -1, 'tom_reader',   CURRENT_DATE - 47, CURRENT_DATE - 47),  -- downvote on terminationGrace
(418, 226,  1, 'marcus_ml',    CURRENT_DATE - 5,  CURRENT_DATE - 5),   -- Divio framework comment
(419, 227,  1, 'sarah_writes', CURRENT_DATE - 3,  CURRENT_DATE - 3);   -- Doctest comment

-- ─────────────────────────────────────────────────────────────────────────────
-- 10. ADVANCE SEQUENCES so Hibernate doesn''t collide with our manual IDs
-- ─────────────────────────────────────────────────────────────────────────────
\echo '--> Advancing sequences past seeded IDs...'

DO $$
DECLARE
    seq_name TEXT;
    max_val  BIGINT := 1000;  -- safe starting point above all our manual IDs
BEGIN
    FOR seq_name IN
        SELECT sequence_name FROM information_schema.sequences
        WHERE sequence_schema = 'public'
    LOOP
        EXECUTE format(
            'SELECT setval(%L, GREATEST(%s, (SELECT last_value FROM %I)))',
            seq_name, max_val, seq_name
        );
    END LOOP;
END $$;

-- ─────────────────────────────────────────────────────────────────────────────
-- VERIFICATION SUMMARY
-- ─────────────────────────────────────────────────────────────────────────────
\echo ''
\echo '======================================================'
\echo '  SEED COMPLETE — row counts:'
\echo '======================================================'

SELECT 'categories'    AS "table", COUNT(*) AS rows FROM categories
UNION ALL SELECT 'userprofile',   COUNT(*) FROM userprofile
UNION ALL SELECT 'user_categories', COUNT(*) FROM user_categories
UNION ALL SELECT 'followers',     COUNT(*) FROM followers
UNION ALL SELECT 'post',          COUNT(*) FROM post
UNION ALL SELECT 'post_details',  COUNT(*) FROM post_details
UNION ALL SELECT 'post_comment',  COUNT(*) FROM post_comment
UNION ALL SELECT 'post_votes',    COUNT(*) FROM post_votes
UNION ALL SELECT 'comment_votes', COUNT(*) FROM comment_votes
ORDER BY 1;

\echo ''
\echo '  Credentials:'
\echo '    Admin:   username=admin_ness  password=Admin@123'
\echo '    Authors: password=Author@123'
\echo '    Readers: password=Reader@123'
\echo '======================================================'
