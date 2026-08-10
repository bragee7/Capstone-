# Class / Module Diagram

```mermaid
classDiagram
    class AuthController {
        +POST /auth/register
        +POST /auth/login
        +GET /auth/me
    }
    class StudentController {
        +GET /students/me
        +PUT /students/me
    }
    class CompanyController {
        +GET /companies/me
        +PUT /companies/me
    }
    class DriveController {
        +GET /drives
        +GET /drives/{id}
    }
    class RecruiterDriveController {
        +GET /recruiter/drives
        +POST /recruiter/drives
        +PUT /recruiter/drives/{id}
        +PATCH /recruiter/drives/{id}/publish
        +PATCH /recruiter/drives/{id}/close
        +GET /recruiter/drives/{id}/applications
        +PATCH /recruiter/drives/{id}/applications/{appId}
    }
    class ApplicationController {
        +POST /applications
        +GET /applications/my
        +GET /applications/{id}
        +DELETE /applications/{id}
    }

    AuthController --> AuthService
    StudentController --> StudentService
    CompanyController --> CompanyService
    DriveController --> DriveService
    RecruiterDriveController --> DriveService
    RecruiterDriveController --> ApplicationService
    ApplicationController --> ApplicationService

    class AuthService {
        +register()
        +login()
        +getCurrentUser()
    }
    class StudentService {
        +getProfile()
        +updateProfile()
    }
    class CompanyService {
        +getProfile()
        +updateProfile()
    }
    class DriveService {
        +getPublished()
        +getPublishedById()
        +create()
        +update()
        +publish()
        +close()
    }
    class ApplicationService {
        +apply()
        +getMyApplications()
        +getById()
        +withdraw()
        +getByDrive()
        +updateStatus()
    }

    class JwtService {
        +generateToken()
        +validateToken()
        +extractUsername()
    }
    class JwtAuthenticationFilter {
        +doFilterInternal()
    }
    class SecurityConfig {
        +securityFilterChain()
    }
    class UserDetailsServiceImpl {
        +loadUserByUsername()
    }

    AuthService --> UserRepository
    AuthService --> JwtService
    AuthService --> SecurityConfig
    StudentService --> StudentRepository
    CompanyService --> CompanyRepository
    DriveService --> HiringDriveRepository
    DriveService --> CompanyRepository
    ApplicationService --> ApplicationRepository
    ApplicationService --> HiringDriveRepository
    ApplicationService --> StudentRepository

    class User {
        +Long id
        +String email
        +String password
        +Role role
    }
    class Student {
        +String fullName
        +String department
        +BigDecimal cgpa
        +int graduationYear
        +String skills
        +String resumeUrl
    }
    class Company {
        +String companyName
        +String industry
        +String location
        +String website
        +String description
    }
    class HiringDrive {
        +String title
        +JobType jobType
        +BigDecimal minCgpa
        +String eligibleDepartments
        +LocalDate applicationDeadline
        +DriveStatus status
    }
    class Application {
        +ApplicationStatus status
        +String coverLetter
        +LocalDateTime createdAt
    }

    User <|-- Student
    User <|-- Company
    Company --> HiringDrive
    Student --> Application
    HiringDrive --> Application
```

## Layering

- **Controller layer**: thin, delegates to services, returns `ApiResponse<T>` envelope.
- **Service layer**: business rules (eligibility, deadline, duplicate checks, ownership).
- **Repository layer**: Spring Data JPA interfaces.
- **Security**: stateless JWT filter + role-based `@PreAuthorize`-style guards (see `SecurityConfig`).
- **Entities**: `User` root; `Student`, `Company` profiles; `HiringDrive`, `Application` core; `Interview`, `Offer` scaffolded for next milestone.
