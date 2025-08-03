
# Building Maintenance App

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/MartinGalvanCastro/Building-Maintenance)

Wiki Docs with Architecture and Design details: https://deepwiki.com/MartinGalvanCastro/Building-Maintenance

---

## Future Improvements

- Enrich app business rules
- Add unit tests for Frontend
- Add Terraform infrastructure for deployment in AWS
- Add CI/CD pipeline with GitHub Actions

## Tech Stack

- **Backend:** Java 21, Spring Boot 3, Hibernate, PostgreSQL
- **Frontend:** React 19, Vite, TypeScript, React Query, React Router, TailwindCSS
- **API:** OpenAPI (codegen)
- **Testing:** JUnit (backend), Vitest (frontend)
- **Containerization:** Docker, Docker Compose

---

## How to Run (Development & Production)

### Prerequisites
- Docker & Docker Compose installed

### Quick Start (All Services)

1. Clone the repository:
   ```sh
   git clone https://github.com/MartinGalvanCastro/Building-Maintenance.git
   cd Building-Maintenance
   ```
2. Build and start all services:
   ```sh
   docker compose up --build
   ```
3. The services will be available at:
   - Frontend: http://localhost (port 80)
   - Backend API: http://localhost:8080/api
   - Backend Swagger Docs: http://localhost:8080/api/swagger-ui/index.html
   - PostgreSQL: localhost:5432 (user: myuser, pass: secret, db: mydatabase)

### Environment Variables
- Frontend: set API URL in `.env` (default: `VITE_API_URL=http://localhost:8080/api`)
- Backend: profile is set to `docker` by default in compose

---

## Admin Credentials (Default)

- **Username:** a@a.com
- **Password:** 123456


You can change these in `backend/src/main/resources/application.yaml` under `security.sudo`.

**Note:** To log in as a Technician or Resident, their user accounts must first be created by an Admin. Use the Admin credentials above to create new Technician or Resident users via the app interface/Swagger UI/ HTTP Calls (Postman or Curl).

- Resident need to be linked to a residential complex

---

## Useful Commands

- Run backend only:
  ```sh
  docker compose up backend db
  ```
- Run frontend only:
  ```sh
  docker compose up frontend
  ```

---
