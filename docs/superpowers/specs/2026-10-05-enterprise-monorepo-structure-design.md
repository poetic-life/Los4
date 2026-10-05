# LOS Enterprise Monorepo Structure Design

## 1. Objective

Reorganize the existing LA Clippers fan-site repository into one clearly layered monorepo. The repository root remains `los`, while the user frontend, primary backend, admin frontend, and admin backend become independently buildable applications under `apps/`.

The migration must preserve all current source changes and application behavior. It is a structural cleanup, not a feature rewrite.

## 2. Target Structure

```text
los/
|-- apps/
|   |-- web/                 # Vue 3 user-facing application
|   |-- api/                 # Spring Boot primary API
|   |-- admin-web/           # Vue 3 administration application
|   `-- admin-api/           # Spring Boot administration API
|-- docs/
|   |-- architecture/        # Architecture and service-boundary documentation
|   `-- superpowers/         # Design and implementation records
|-- infra/
|   `-- database/            # Database initialization assets
|-- scripts/                 # Cross-platform development/build entry points
|-- .editorconfig
|-- .gitattributes
|-- .gitignore
`-- README.md
```

## 3. Source-to-Target Mapping

| Current location | Target location | Notes |
| --- | --- | --- |
| Root `src/`, `public/`, `style/`, frontend config and package files | `apps/web/` | Preserve the current user frontend as one Vite application. |
| `Los/src/` and `Los/pom.xml` | `apps/api/` | Preserve the primary Spring Boot service and port `8081`. |
| `admin-web/` source and project files | `apps/admin-web/` | Exclude generated `node_modules/` and `dist/` content. Preserve port `3001`. |
| `LosAdmin/src/` and `LosAdmin/pom.xml` | `apps/admin-api/` | Preserve the admin Spring Boot service and port `8082`. |
| `Los/init.sql` | `infra/database/init.sql` | Treat database bootstrap SQL as infrastructure, not application source. |
| Empty legacy `backend/` directory | Remove | It has no source content and conflicts with the real backend location. |

Generated Maven `target/`, frontend `node_modules/`, frontend `dist/`, IDE metadata, logs, and local environment files are not part of the source tree and will be ignored globally.

## 4. Application Boundaries

### User frontend (`apps/web`)

- Owns the public site, member center, shopping, community, news, roster, schedule, and messaging UI.
- Calls the primary API through `/api` and loads backend-hosted media through `/uploads`.
- Its development server remains on port `8080` and proxies to `http://localhost:8081`.

### Primary backend (`apps/api`)

- Owns public and authenticated user-facing business APIs.
- Owns JPA entities, repositories, JWT authentication, synchronization services, and seed data for the main application.
- Runs on port `8081` and uses the shared MySQL `los` database.

### Admin frontend (`apps/admin-web`)

- Owns dashboard and management interfaces for users, products, posts, comments, and orders.
- Calls the admin API through `/api` and loads shared media through the primary API `/uploads` endpoint.
- Its development server remains on port `3001`; API requests proxy to `http://localhost:8082`.

### Admin backend (`apps/admin-api`)

- Owns administrator authentication and management APIs.
- Runs on port `8082` and connects to the same MySQL `los` database so administration changes are visible to the public application.

## 5. Configuration and Data Flow

The browser applications communicate only with their corresponding backend through Vite development proxies. Production deployments must reproduce the same `/api` and `/uploads` routes through a reverse proxy.

Database credentials continue to come from `DB_USERNAME` and `DB_PASSWORD`. Default development values remain compatible with the existing configuration, while documentation will state that real credentials and JWT secrets must be supplied outside source control for production.

No domain model, REST path, database table, authentication rule, or port is intentionally changed by this migration.

## 6. Repository Standards

- The repository root contains orchestration and documentation only; application-specific files live inside their application directory.
- Each application has a short README covering responsibility, prerequisites, commands, ports, and dependencies.
- Root documentation provides a dependency map and canonical start/build sequence.
- `.gitignore` uses recursive patterns so generated files are excluded at every application depth.
- `.editorconfig` and `.gitattributes` establish UTF-8 text, consistent indentation, and predictable line endings across Windows and CI environments.
- Root scripts provide discoverable commands for installing frontend dependencies, building all applications, and starting individual services without introducing an additional package manager workspace layer.

## 7. Migration Safety

- Existing modified and untracked source files are moved without rewriting their content unless a path-dependent configuration requires adjustment.
- Moves use Git-aware operations for tracked files when practical, preserving history as renames.
- Generated directories may be removed from the working tree because they can be recreated from lockfiles and Maven descriptors; source assets are retained.
- No database data, user uploads, or application behavior is deleted.
- A post-migration inventory verifies that every source file has exactly one destination.

## 8. Verification

The migration is complete when:

1. Both Vue applications install and build from their new directories.
2. Both Maven applications compile and run their test suites from their new directories.
3. Vite proxy targets and backend ports still match the four-service topology.
4. No generated `node_modules/`, `dist/`, or `target/` content is tracked or presented as project source.
5. Documentation contains no obsolete `backend/`, `Los/`, or `LosAdmin/` startup paths.
6. `git status` shows the reorganization as intentional moves plus the new repository-standard files, without accidental source loss.

## 9. Out of Scope

- Rewriting application features or visual design.
- Consolidating the two Spring Boot services into one process.
- Migrating from Maven to Gradle or introducing a Java multi-module parent project.
- Introducing Docker, Kubernetes, CI/CD pipelines, or production secrets in this cleanup.
- Changing API contracts or the database schema.
