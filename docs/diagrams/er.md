# Entity Relationship Diagram

```mermaid
erDiagram
    USERS ||--o| STUDENTS : "is a"
    USERS ||--o| COMPANIES : "is a"

    COMPANIES ||--o{ HIRING_DRIVES : "owns"
    STUDENTS ||--o{ APPLICATIONS : "submits"
    HIRING_DRIVES ||--o{ APPLICATIONS : "receives"
    HIRING_DRIVES ||--o{ INTERVIEWS : "schedules"
    APPLICATIONS ||--o| INTERVIEWS : "leads to"
    HIRING_DRIVES ||--o{ OFFERS : "extends"
    APPLICATIONS ||--o| OFFERS : "leads to"

    USERS {
        bigint id PK
        varchar email UK
        varchar password
        enum role "STUDENT | RECRUITER | ADMIN"
        datetime created_at
    }

    STUDENTS {
        bigint id PK
        bigint user_id FK
        varchar full_name
        varchar department
        decimal cgpa
        int graduation_year
        varchar skills
        varchar resume_url
        boolean profile_completed
    }

    COMPANIES {
        bigint id PK
        bigint user_id FK
        varchar company_name
        varchar industry
        varchar location
        varchar website
        varchar description
    }

    HIRING_DRIVES {
        bigint id PK
        bigint company_id FK
        varchar title
        varchar description
        enum job_type "FULL_TIME | INTERNSHIP"
        varchar location
        decimal min_cgpa
        varchar eligible_departments
        int open_positions
        date application_deadline
        enum status "DRAFT | PUBLISHED | CLOSED"
    }

    APPLICATIONS {
        bigint id PK
        bigint student_id FK
        bigint drive_id FK
        enum status "APPLIED | SHORTLISTED | INTERVIEW | SELECTED | REJECTED"
        text cover_letter
        datetime created_at
    }

    INTERVIEWS {
        bigint id PK
        bigint application_id FK
        bigint drive_id FK
        datetime scheduled_at
        enum mode "ONLINE | OFFLINE"
        varchar location_or_link
        enum status "SCHEDULED | COMPLETED | CANCELLED"
        text feedback
    }

    OFFERS {
        bigint id PK
        bigint drive_id FK
        bigint application_id FK
        varchar package_details
        enum status "PENDING | ACCEPTED | DECLINED"
        date offer_date
    }
```

## Notes

- `users` is the authentication root; `students` and `companies` extend it via a one-to-one `user_id` foreign key.
- A student may submit **one application per drive** (enforced in `ApplicationService`).
- `hiring_drives` visibility: `PUBLISHED` drives are public; drafts are recruiter-only.
- The `Interview` and `Offer` entities exist in the schema for the next milestone (Review-II).
