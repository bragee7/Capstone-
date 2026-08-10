# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project adheres to [Semantic Versioning](https://semver.org/).

## [Unreleased]

### Added (MVP / Review-I)

- **Project initialization**
  - Monorepo scaffold for backend (Spring Boot 3) + frontend (React/Vite) + docs.
  - Conventional Commits, `.gitignore`, `LICENSE` (MIT), `.env.example`.
  - CI workflows: `backend.yml` (Maven clean verify) and `frontend.yml` (oxlint + production build).

- **Authentication & Authorization**
  - JWT-based register/login (`HS256`), `GET /auth/me`.
  - Stateless security filter chain with role-based access (STUDENT / RECRUITER / ADMIN).
  - Role route guards on the frontend (`RoleRoute`, `ProtectedRoute`).

- **Student module**
  - `GET/PUT /students/me` with auto profile-completion percentage.
  - Partial update semantics (null fields ignored).

- **Recruiter module**
  - `GET/PUT /companies/me` company profile (partial update allowed).
  - Hiring drive CRUD: create (DRAFT), update, publish (validated), close.
  - Public browsing of `PUBLISHED` drives only.

- **Applications**
  - Student apply with eligibility checks: published status, deadline, min CGPA, department, single application per drive.
  - `GET /applications/my`, `GET /{id}`, `DELETE /{id}` withdraw (only while `APPLIED`).
  - Recruiter review: list drive applications, update status (`SHORTLISTED`, `INTERVIEW`, `SELECTED`, `REJECTED`).

- **Frontend (React)**
  - Auth pages (login/register), student + recruiter dashboards, drive detail with apply, my applications, profile pages, company profile, drive create/edit, applicant review.
  - Vite dev proxy `/api` → `localhost:8080`, Tailwind CSS v4, JWT interceptor.

- **API conventions**
  - Standard envelope `{ success, data, message }` and global exception handling.
  - Swagger/OpenAPI at `/swagger-ui.html` and `/v3/api-docs`.

- **Documentation**
  - `README.md` (setup, run, test, endpoints), `CHANGELOG.md`.
  - `docs/diagrams/` — architecture, ER, and class diagrams (Mermaid).

### Changed

- `Student.cgpa` migrated from `Double` to `BigDecimal` to support `DECIMAL(4,2)` storage (fixes "scale has no meaning" startup failure).

### Fixed

- Hibernate schema creation failing due to leftover legacy tables in the `campushire` database.
- Drive publish endpoint now requires future deadline, job type, and location.
- Ownership checks on drive/applications (403 on cross-recruiter access).

## [0.1.0] — MVP / Review-I milestone

Initial release for Review-I: auth, student + recruiter modules, hiring drives, applications, React frontend, CI, and docs.
