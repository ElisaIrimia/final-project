# AdoptiHub - Pet Adoption Platform

AdoptiHub is a full-stack Spring Boot 3 application for managing animal adoption workflows. It includes a public adoption catalog, user adoption applications, an admin dashboard, role-based security, a REST API, validation, exception handling, JPA relationships, and automated tests.

## Tech Stack

- Java 17
- Spring Boot 3.3.2
- Spring Web MVC
- Spring Data JPA and Hibernate
- Spring Security with BCrypt passwords
- Thymeleaf
- H2 in-memory database
- JUnit 5, Mockito, MockMvc

## Domain Model

- `AppUser`: registered users with `ROLE_USER` or `ROLE_ADMIN`
- `Shelter`: adoption shelters that host pets
- `Pet`: adoptable animal profiles
- `AdoptionApplication`: requests submitted by users for specific pets

Relationships:

- One shelter has many pets
- One pet has many adoption applications
- One user has many adoption applications

## Demo Accounts

- Admin: `admin` / `admin123`
- User: `elisa` / `user123`

## Run Locally

```powershell
.\mvnw.cmd spring-boot:run
```

Open:

- Web UI: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:adoptihub`

## REST API Examples

Public:

- `GET /api/pets`
- `GET /api/pets/{id}`

Authenticated user:

- `GET /api/applications/mine`
- `POST /api/applications/pets/{petId}`

Admin:

- `POST /api/pets`
- `PUT /api/pets/{id}`
- `DELETE /api/pets/{id}`
- `GET /api/admin/applications`
- `PUT /api/admin/applications/{id}/{status}`
- `GET /api/admin/shelters`
- `POST /api/admin/shelters`

## Tests

```powershell
.\mvnw.cmd test
```

Current coverage includes:

- Spring context test
- Service unit test with Mockito
- API and security integration test with MockMvc
