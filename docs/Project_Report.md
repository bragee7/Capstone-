# Project Report — Internship & Campus Hiring Platform

**Milestone:** Review-I / MVP
**Date:** 2026-08-11
**Branch:** `main`

## 1. Executive Summary

The Internship & Campus Hiring Platform MVP is complete and verified. It connects students and recruiters for campus internships and hiring: students register, complete a profile, browse published hiring drives, and apply; recruiters manage company profiles, create/publish/close hiring drives, review applications, and update applicant statuses. The system is a JWT-secured, layered REST application (Spring Boot 3 + React + MySQL) with two verified end-to-end workflows, a unit-tested backend, a linted/built frontend, and CI for both.

## 2. Deliverables

| Area | Deliverable | Status |
| ---- | ----------- | ------ |
| Backend | Spring Boot 3 REST API, JWT auth, role-based access | Done |
| Student module | Profile CRUD + completion %, drive browse, apply/withdraw | Done |
| Recruiter module | Company profile, drive lifecycle, applicant review | Done |
| Applications | Apply rules (eligibility, deadline, duplicate), status tracking | Done |
| Frontend | React + Tailwind app for both roles, protected routes | Done |
| Tests | 17 service unit tests (Mockito), 18/18 E2E integration checks | Pass |
| CI/CD | `backend.yml` (Maven verify) + `frontend.yml` (lint+build) | Done |
| Docs | README, CHANGELOG, architecture/ER/class diagrams | Done |

## 3. Architecture

```
React (Vite, 5173) --Axios /api--> Spring Boot (8080) --> MySQL 8
     |                    |                    |
  guards/context      JWT filter       JPA repositories
```

- **Security:** stateless JWT (HS256) filter; roles `STUDENT`, `RECRUITER`, `ADMIN`; CORS allow-list for `FRONTEND_ORIGIN`.
- **API contract:** uniform envelope `{ success, data, message }`; global exception handler → JSON errors.
- **Frontend:** axios client with Bearer-token interceptor, 401 auto-logout, role-route guards.

See `docs/diagrams/` for architecture, ER, and class diagrams.

## 4. Key Features Verified End-to-End

1. **Student flow:** register → login → complete profile (100%) → browse published drive → apply → status `APPLIED` → recruiter `SHORTLISTED` → student sees it → withdrawal blocked.
2. **Recruiter flow:** register → company profile → create drive (`DRAFT`) → `PUBLISH` → review applicant → `SHORTLISTED` → `REJECTED` on second applicant → application count reflects.
3. **Business rules enforced:** duplicate application (400), publish requires future deadline + job type + location, eligibility (CGPA, department) checked, withdraw only while `APPLIED`.

## 5. Quality Gates

| Gate | Result |
| ---- | ------ |
| Backend unit tests (`mvn test`) | 17/17 pass |
| Integration suite (through Vite proxy) | 18/18 pass |
| Frontend lint (oxlint) | Pass (1 benign warning) |
| Frontend production build | Pass |
| Git history | 11 conventional commits |

## 6. Running the Project

```bash
# Backend
cd backend && mvn spring-boot:run        # http://localhost:8080 (Swagger at /swagger-ui.html)

# Frontend
cd frontend && npm install && npm run dev # http://localhost:5173
```

Seeded accounts: `student@example.com`, `recruiter@example.com`, `admin@example.com` / `Password@123`.

## 7. Scope Notes & Next Milestones (Review-II)

- `Interview` and `Offer` entities are scaffolded in the schema for the next milestone but have no endpoints/UI yet.
- Recommended next: interviews & offers workflows, admin dashboard, notifications, cloud resume storage, production deployment.

## 8. Risk / Issue Log

- `Student.cgpa` used `Double` → failed MySQL `DECIMAL` mapping at startup; fixed with `BigDecimal`.
- Leftover legacy tables in the shared `campushire` DB blocked Hibernate schema sync; resolved by dropping them (documented in CHANGELOG).
- `findstr`/bare `powershell` unavailable on this machine's PATH; all scripting uses full paths (documented for future sessions).
