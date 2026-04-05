# FinTrack API — Finance Data Processing and Access Control Backend

A secure, role-based REST API backend for a finance dashboard system, built with **Java 17**, **Spring Boot 3**, **Spring Security**, **JWT**, and **MySQL**.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5 |
| Security | Spring Security + JWT (JJWT) |
| Database | MySQL 8 |
| ORM | Spring Data JPA / Hibernate |
| Validation | Jakarta Validation |
| API Docs | Swagger UI / OpenAPI 3.1 |
| Build | Maven |

---

## Architecture

```
Request → JwtAuthenticationFilter → SecurityConfig → Controller → Service → Repository → MySQL
```

Layered architecture with clear separation of concerns:
- **Controller** — HTTP request handling
- **Service** — Business logic
- **Repository** — Data access via JPA
- **Security** — JWT filter + role enforcement

---

## Roles and Access Control

| Role | Permissions |
|---|---|
| VIEWER | Read transactions, view dashboard |
| ANALYST | Read transactions, view dashboard, filter records |
| ADMIN | Full access — manage users, create/update/delete transactions |

Access control is enforced at two levels:
1. `SecurityConfig` — route-level restrictions
2. `@PreAuthorize` annotations — method-level restrictions

---

## Project Structure

```
src/main/java/com/prash/fintrackapi/
├── config/          # SecurityConfig
├── controller/      # AuthController, TransactionController, UserController, DashboardController, CategoryController
├── dto/             # Request/Response DTOs
├── exception/       # GlobalExceptionHandler
├── model/           # User, Transaction, Category, Role, UserStatus, TransactionType
├── repository/      # JPA Repositories
├── security/        # JwtAuthenticationFilter, CustomUserDetailsService
├── service/         # Business logic services
└── util/            # JwtUtil
```

---

## Setup Instructions

### Prerequisites
- Java 17
- MySQL 8
- Maven

### 1. Clone the repository
```bash
git clone https://github.com/PrashantBhanage/finance-api.git
cd finance-api
```

### 2. Create MySQL database
```sql
CREATE DATABASE fintrack_db;
CREATE USER 'finuser'@'localhost' IDENTIFIED BY 'yourpassword';
GRANT ALL PRIVILEGES ON fintrack_db.* TO 'finuser'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Configure application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fintrack_db
spring.datasource.username=finuser
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080

jwt.secret=your-super-secret-key-minimum-32-characters-long
jwt.expiration=86400000
```

### 4. Run the application
```bash
./mvnw spring-boot:run
```

### 5. Access Swagger UI
```
http://localhost:8080/swagger-ui/index.html
```

---

## Authentication Flow

1. Register → `POST /api/auth/register`
2. Login → `POST /api/auth/login` → receive JWT token
3. Authorize in Swagger → paste token (without "Bearer " prefix)
4. All protected endpoints now accessible based on role

---

## API Endpoints

### Auth (Public)
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login and get JWT token |

### Transactions (Authenticated)
| Method | Endpoint | Description | Role |
|---|---|---|---|
| GET | /api/transactions | Get all transactions | All |
| GET | /api/transactions/{id} | Get transaction by ID | All |
| POST | /api/transactions | Create transaction | ADMIN |
| PUT | /api/transactions/{id} | Update transaction | ADMIN |
| DELETE | /api/transactions/{id} | Delete transaction | ADMIN |
| GET | /api/transactions/filter | Filter by date/type/category | All |

### Dashboard (Authenticated)
| Method | Endpoint | Description |
|---|---|---|
| GET | /api/dashboard | Total income, expenses, net balance, category totals, monthly trends |

### Users (ADMIN only)
| Method | Endpoint | Description |
|---|---|---|
| GET | /api/users | Get all users |
| GET | /api/users/{id} | Get user by ID |
| POST | /api/users | Create user |
| PUT | /api/users/{id} | Update user |
| DELETE | /api/users/{id} | Delete user |
| PATCH | /api/users/{id}/status | Activate/deactivate user |

### Categories (Authenticated)
| Method | Endpoint | Description |
|---|---|---|
| GET | /api/categories | Get all categories |
| POST | /api/categories | Create category |
| PUT | /api/categories/{id} | Update category |
| DELETE | /api/categories/{id} | Delete category |

---

## Dashboard Response Example

```json
{
  "totalIncome": 5000,
  "totalExpense": 150.5,
  "netBalance": 4849.5,
  "categoryTotals": {
    "Salary": 5000,
    "Shopping": -150.5
  },
  "recentTransactions": [...],
  "monthlySummary": {
    "2026-04": 4849.5,
    "2026-03": 0
  }
}
```

---

## Assumptions and Design Decisions

- Each transaction belongs to a specific user — users only see their own data
- Passwords are encrypted using BCrypt
- JWT tokens expire after 24 hours (configurable)
- `ddl-auto=update` is used for development — schema is auto-managed by Hibernate
- Categories are shared across users (not user-specific)
- Soft delete was not implemented — hard delete used for simplicity
- ANALYST role has same read permissions as VIEWER in this implementation

---

## Error Handling

All errors return consistent JSON:
```json
{
  "status": 400,
  "error": "Bad Request",
  "message": "User with email already exists",
  "timestamp": "2026-04-05T10:00:00"
}
```

HTTP status codes used correctly — 200, 201, 400, 401, 403, 404.

---

## Author

**Prashant Bhanage**  
GitHub: [PrashantBhanage](https://github.com/PrashantBhanage)
```
