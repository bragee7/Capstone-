# Problem Statement

## 1. Title

**Internship & Campus Hiring Platform**

A web-based platform that connects students with companies/recruiters for campus internships and hiring opportunities.

## 2. Domain

**Education & Recruitment (Campus Placement / Talent Acquisition)**

The platform operates in the intersection of higher education and human-resources technology: it digitises the traditional campus placement process where companies visit colleges, announce hiring drives, and select students.

## 3. Who is the user?

| Role | Description |
| --- | --- |
| **Student** | A college student looking for internship / job opportunities relevant to their department, CGPA, and skills. |
| **Recruiter** | A company representative who creates and manages hiring drives, reviews applicants, and moves them through the selection funnel. |
| **Admin** | A platform administrator who monitors users, companies, and hiring activity for governance and oversight. |

## 4. What problem are we solving?

Traditional campus placement suffers from:

1. **Scattered information** — drive announcements travel via notices, mail, and word-of-mouth; students miss deadlines and eligibility details.
2. **Manual application tracking** — recruiters receive resumes by email and track shortlists in spreadsheets, which is error-prone and opaque.
3. **No eligibility filtering** — recruiters manually check CGPA, department, and skill criteria against every resume.
4. **No single source of truth** — students cannot see where they stand; recruiters cannot easily compare applicants; admins cannot monitor activity.
5. **Duplicate applications** — students may accidentally apply multiple times to the same drive.

The platform centralises drive publishing, eligibility-aware applications, and status tracking in one secure, role-based web application.

## 5. Proposed Solution

A **REST-based web application** with:

- **React + Tailwind CSS frontend** for students, recruiters, and admins.
- **Spring Boot 3 backend** exposing a versioned REST API (`/api/v1`) secured with **JWT**.
- **MySQL 8** persistence with 7 related tables.
- **Role-based dashboards**: students browse eligible published drives and track applications; recruiters create/publish drives, review applicants, and update statuses; admins monitor the platform.

Core business flows delivered at Review-I:

1. **Authentication** — register → login → JWT → role-based dashboard.
2. **Recruiter hiring flow** — create company profile → create drive → publish → student sees it.
3. **Student application flow** — browse published drives → apply (once, before deadline, if eligible) → track status.
4. **Recruiter application management** — view applicants → shortlist → interview → select/reject.

## 6. Core Entities / Database Tables

| Table | Purpose | Key relationships |
| --- | --- | --- |
| `users` | Authentication identity (name, email, bcrypt hash, role) | 1—1 with student/company |
| `students` | Extended student profile (registration no., dept, CGPA, skills, resume) | N—1 user |
| `companies` | Recruiter company profile (name, industry, website, verified) | N—1 user |
| `hiring_drives` | Job/internship drives (eligibility, deadline, compensation, status) | N—1 company |
| `applications` | Student applications to drives (status, cover message, resume) | N—1 student, N—1 drive |
| `interviews` | Interview scheduling for an application | N—1 application |
| `offers` | Offer made on an application | 0..1—1 application |

## 7. User Roles & Permissions

| Role | Permissions |
| --- | --- |
| **STUDENT** | View published drives; update own profile; apply to published drives before deadline if eligible (max once per drive); view own applications only. |
| **RECRUITER** | Create/manage own company profile; create/update/publish/close only their own drives; view applicants for their own drives; update application statuses through valid stages. |
| **ADMIN** | View users, companies, and drives across the platform (monitoring only at Review-I). |

## 8. Success Criteria

- Students can register, log in with JWT, browse published drives, apply once (subject to eligibility and deadline), and track their application status end-to-end.
- Recruiters can create a company profile, create and publish hiring drives, view applicants, and update application status.
- A student cannot apply twice to the same drive, cannot apply after the deadline, and cannot apply to a closed/unpublished drive.
- A recruiter cannot modify another recruiter's drive.
- All important business data persists in MySQL; at least two end-to-end flows work from the UI.
- The system is secured with bcrypt password hashing, JWT, and role-based authorization; secrets are environment-based and never committed.

## 9. Out of Scope

At Review-I the following are **not** implemented (documented for future phases):

- Third-party integrations (email/SMS notifications, cloud resume storage, Google Maps, calendar).
- AI resume-to-job matching and candidate ranking.
- Payment gateways, microservices, message queues, and production deployment/CI-CD.
- Real-time chat and advanced analytics.

## 10. Chosen Track

**Web Application Development (Full-Stack) — Spring Boot + React**

- **Backend:** Java 17, Spring Boot 3, Spring Security + JWT (JJWT), Spring Data JPA / Hibernate, Bean Validation, Lombok, Springdoc OpenAPI (Swagger), MySQL 8, Maven.
- **Frontend:** React, JavaScript, Tailwind CSS, Axios, React Router, Vite.
- **Testing:** JUnit 5 + Mockito.
- **Future scope:** natural AI enhancement (resume-to-job matching / compatibility scoring) and third-party integrations.
