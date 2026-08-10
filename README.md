# Internship & Campus Hiring Platform

A web-based platform that connects students with companies/recruiters for campus internships and hiring opportunities. Students create profiles, discover eligible drives, and apply; recruiters create and manage hiring drives, review applicants, and update statuses; admins monitor platform activity.

This repository contains the **Review-I / MVP** milestone.

- [Problem Statement](./Problem_Statement.md)
- [Changelog](./CHANGELOG.md)
- [Diagrams](./docs/diagrams/)

## Features

- **Authentication & Authorization**: JWT (HS256) login/register, role-based access (STUDENT / RECRUITER / ADMIN), protected routes on both API and frontend.
- **Student module**: profile creation/update with auto profile-completion %, browse published drives, apply with eligibility checks (published, deadline, min CGPA, department, single application per drive), track application status, withdraw.
- **Recruiter module**: company profile, hiring drive lifecycle `DRAFT → PUBLISHED → CLOSED`, publish-time validation, review applications, update applicant status (`APPLIED → SHORTLISTED → INTERVIEW → SELECTED / REJECTED`).
- **Standard API envelope** `{ success, data, message }` and global exception handling.
- **Swagger/OpenAPI** documentation.
- **CI**: backend build+test (`backend.yml`) and frontend lint+build (`frontend.yml`) on GitHub Actions.

## Tech Stack

- **Frontend:** React 19, JavaScript, Tailwind CSS v4, Axios, React Router 7, Vite 8
- **Backend:** Java 17, Spring Boot 3, Spring Security + JWT, Spring Data JPA, Bean Validation, Lombok, Springdoc OpenAPI
- **Database:** MySQL 8
- **Build/Tooling:** Maven, npm, Git (Conventional Commits)

## Architecture

Layered REST: React → Axios → Spring Boot Controllers → Services → Repositories → MySQL. See [architecture diagram](./docs/diagrams/architecture.md), [ER diagram](./docs/diagrams/er.md), and [class diagram](./docs/diagrams/class.md).

```
React (Tailwind)  --Axios(/api)-->  Spring Boot REST API  -->  MySQL 8
                                      (JWT secured)
```

## Prerequisites

- JDK 17
- Maven 3.9+
- Node.js 20+ / npm
- MySQL 8

## Environment Variables

Copy `backend/.env.example` to `backend/.env` and configure real values. Never commit the real `.env`.

| Variable            | Purpose                                   |
| ------------------- | ----------------------------------------- |
| `DB_URL`            | JDBC URL for MySQL (`campushire` DB)      |
| `DB_USERNAME`       | DB user                                   |
| `DB_PASSWORD`       | DB password                               |
| `JWT_SECRET`        | Secret key for signing JWTs (>= 32 chars) |
| `JWT_EXPIRATION`    | Token lifetime in ms (default 86400000)   |
| `SERVER_PORT`       | Backend port (default 8080)               |
| `FRONTEND_ORIGIN`   | CORS allow-list origin for the frontend   |

## Database Setup

The database is created automatically on first startup when `createDatabaseIfNotExist=true`. Schema is generated from JPA entities (`ddl-auto: update`), and `DataSeeder` seeds demo accounts on an empty database.

## Backend Setup

```bash
cd backend
mvn spring-boot:run
```

Demo accounts (seeded): `admin@example.com`, `student@example.com`, `recruiter@example.com` / `Password@123`.

## Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

The Vite dev server (port 5173) proxies `/api` requests to `http://localhost:8080`.

## Testing

```bash
cd backend
mvn test          # service unit tests (Mockito)

cd frontend
npm run lint      # oxlint
npm run build     # production build
```

## API Documentation

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

### Key endpoints

| Method & path                                          | Role      | Description                          |
| ------------------------------------------------------ | --------- | ------------------------------------ |
| `POST /api/v1/auth/register`, `POST /api/v1/auth/login` | Public    | Register / login, returns JWT        |
| `GET /api/v1/auth/me`                                   | Any       | Current user info                    |
| `GET /api/v1/students/me` / `PUT /api/v1/students/me`   | STUDENT   | Student profile read / update        |
| `GET /api/v1/companies/me` / `PUT /api/v1/companies/me` | RECRUITER | Company profile read / update        |
| `GET /api/v1/drives` / `GET /api/v1/drives/{id}`         | Public    | List / view published drives         |
| `GET|POST /api/v1/recruiter/drives`                     | RECRUITER | List / create drives                 |
| `PUT /api/v1/recruiter/drives/{id}`                     | RECRUITER | Update drive                         |
| `PATCH .../drives/{id}/publish` / `close`                | RECRUITER | Drive lifecycle transitions          |
| `POST /api/v1/applications`                             | STUDENT   | Apply to a published drive           |
| `GET /api/v1/applications/my` / `GET|DELETE /api/v1/applications/{id}` | STUDENT | My applications / withdraw |
| `GET /api/v1/recruiter/drives/{id}/applications`        | RECRUITER | Review applicants                    |
| `PATCH .../applications/{appId}`                        | RECRUITER | Update applicant status              |

## Project Structure

```
internship-campus-hiring-platform/
├─ backend/
│  └─ src/
│     ├─ main/java/com/campushire/
│     │  ├─ config/       # Security, OpenAPI, DataSeeder
│     │  ├─ controller/   # REST endpoints
│     │  ├─ dto/          # request/response objects
│     │  ├─ exception/    # global error handling
│     │  ├─ model/        # entities + enums
│     │  ├─ repository/   # Spring Data JPA
│     │  ├─ security/     # JWT filter, UserDetails
│     │  └─ service/      # business logic
│     └─ test/            # service unit tests
├─ frontend/
│  └─ src/
│     ├─ components/      # Navbar, guards, cards, badges
│     ├─ context/         # AuthContext
│     ├─ lib/             # axios client
│     └─ pages/           # student & recruiter dashboards
├─ docs/diagrams/         # architecture, ER, class
└─ .github/workflows/     # backend + frontend CI
```

## Core Workflows

1. **Student:** Register → Login (JWT) → Complete profile → Browse drives → Apply → Track status → Withdraw.
2. **Recruiter:** Login → Company profile → Create drive (DRAFT) → Publish → Review applicants → Update statuses → Close.
3. **Admin:** Monitor users, companies, and drives.

## Future Enhancements

- Interviews, offers, and notifications (Review-II)
- Email/SMS notifications, cloud resume storage, calendar integration
- AI resume-to-job matching and candidate ranking
- Production deployment and CI/CD

## License

[MIT](./LICENSE)
