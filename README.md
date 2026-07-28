# Internship & Campus Hiring Platform

## Project Overview

A web-based platform that connects students with companies/recruiters for campus internships and hiring opportunities. Students create profiles, discover eligible drives, and apply; recruiters create and manage hiring drives, review applicants, and update statuses; admins monitor platform activity.

This repository contains the **Review-I / MVP** milestone of the project.

> Full documentation is completed at the end of the MVP phase. See [Problem_Statement.md](./Problem_Statement.md) and [CHANGELOG.md](./CHANGELOG.md).

## Features

- JWT-based authentication with role-based dashboards (STUDENT / RECRUITER / ADMIN)
- Student profiles, published-drive browsing, and single-application enforcement with eligibility + deadline checks
- Recruiter company profiles, hiring drive lifecycle (DRAFT → PUBLISHED → CLOSED)
- Application status tracking (APPLIED → SHORTLISTED → INTERVIEW → SELECTED / REJECTED)
- Swagger/OpenAPI documentation
- Standard API response envelope and global exception handling

## Tech Stack

- **Frontend:** React, JavaScript, Tailwind CSS, Axios, React Router, Vite
- **Backend:** Java 17, Spring Boot 3, Spring Security + JWT, Spring Data JPA, Bean Validation, Lombok, Springdoc OpenAPI
- **Database:** MySQL 8
- **Build/Tooling:** Maven, npm, Git (Conventional Commits)

## Architecture

Layered REST architecture: React → Axios → Spring Boot Controllers → Services → Repositories → MySQL.

```
React (Tailwind)  --Axios-->  Spring Boot REST API  -->  MySQL 8
                               (JWT secured)
```

Diagrams: `docs/diagrams/` (architecture, ER, class).

## Getting Started

_Setup instructions are documented in the final README at the end of the MVP phase._

## Prerequisites

- JDK 17
- Maven 3.9+
- Node.js 20+ / npm
- MySQL 8

## Environment Variables

Copy `.env.example` and configure database + JWT values. Never commit real secrets.

## Backend Setup

```bash
cd backend
mvn spring-boot:run
```

## Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

## Database Setup

See `.env.example` and the `application-dev.yml` profile. The database is created automatically on first startup when `createDatabaseIfNotExist=true`.

## API Documentation

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Project Structure

See [Problem_Statement.md](./Problem_Statement.md) and `docs/diagrams/`.

## Core Workflows

1. **Student:** Register → Login (JWT) → Browse drives → Apply → Track status.
2. **Recruiter:** Login → Company profile → Create drive → Publish → Review applicants → Update status.
3. **Admin:** Monitor users, companies, and drives.

## Future Enhancements

- Email/SMS notifications, cloud resume storage, Google Maps, calendar integration
- AI resume-to-job matching and candidate ranking
- Production deployment and CI/CD

## License

[MIT](./LICENSE)
