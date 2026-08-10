# Architecture Diagram

```mermaid
flowchart LR
    subgraph Client
        BR[Browser]
    end

    subgraph Frontend["React Frontend (Vite, port 5173)"]
        UI[Pages & Components]
        RC[Route Guards / RoleRoute]
        AX[axios client<br/>lib/api.js]
    end

    subgraph Backend["Spring Boot API (port 8080)"]
        SC[SecurityFilterChain + JWT Filter]
        CTL[Controllers]
        SVC[Services]
        REP[Repositories]
    end

    DB[(MySQL 8<br/>campushire)]

    BR --> UI
    UI --> RC
    RC --> AX
    AX -->|/api/v1/*| SC
    SC -->|authenticated request| CTL
    CTL --> SVC
    SVC --> REP
    REP --> DB
```

## Request Flow (e.g. student applies to a drive)

```mermaid
sequenceDiagram
    participant B as Browser
    participant A as Axios (JWT interceptor)
    participant F as JwtAuthenticationFilter
    participant C as ApplicationController
    participant S as ApplicationService
    participant R as Repositories
    participant D as MySQL

    B->>A: POST /api/v1/applications
    A->>A: attach Bearer token from localStorage
    A->>F: request + Authorization header
    F->>F: validate JWT, set SecurityContext
    F->>C: authenticated request
    C->>S: apply(driveId, studentId)
    S->>R: load drive, load student
    R->>D: SELECT
    S->>S: check published, deadline, eligibility, duplicate
    S->>R: save application
    R->>D: INSERT
    S-->>C: ApplicationResponse
    C-->>A: {success, data, message}
    A-->>B: JSON envelope
```

## Deployment / Runtime Topology

- **Backend**: Spring Boot 3, Tomcat embedded, JWT HS256, CORS allow-list for `FRONTEND_ORIGIN`.
- **Frontend**: Vite dev server proxies `/api` to `http://localhost:8080` (see `vite.config.js`).
- **Database**: MySQL 8, schema created by Hibernate (`ddl-auto: update`), seed via `DataSeeder`.
- **CI**: `backend.yml` (Maven clean verify), `frontend.yml` (lint + build), on push/PR to `main`.
