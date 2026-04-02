# Finance API

A secure RESTful backend API for personal finance tracking built with **Spring Boot 3**, **Spring Security**, **JWT Authentication**, and **MySQL**.

This project allows users to manage:

- Transactions
- Categories
- User accounts
- Dashboard summary data

It is designed as a backend-only project and tested using **Swagger UI** and **Postman**.

---

## Features

- JWT-based authentication and authorization
- User registration and login
- CRUD operations for transactions
- CRUD operations for categories
- User management APIs
- Dashboard summary endpoint
- Swagger/OpenAPI documentation
- MySQL database integration
- Layered architecture (Controller → Service → Repository)

---

## Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Security**
- **JWT**
- **Spring Data JPA / Hibernate**
- **MySQL**
- **Maven**
- **Swagger / OpenAPI**

---

## Project Structure

\`\`\`bash
src/main/java/com/prash/fintrackapi
├── config
├── controller
├── dto
├── entity
├── exception
├── repository
├── security
├── service
└── FinanceApiApplication.java
\`\`\`

---

## API Documentation

After running the project, Swagger UI is available at:

\`\`\`bash
http://localhost:8080/swagger-ui/index.html
\`\`\`

OpenAPI JSON:

\`\`\`bash
http://localhost:8080/v3/api-docs
\`\`\`

---

## Setup Instructions

### 1. Clone the repository

\`\`\`bash
git clone https://github.com/PrashantBhanage/finance-api.git
cd finance-api
\`\`\`

### 2. Create MySQL database

\`\`\`sql
CREATE DATABASE fintrack_db;
\`\`\`

### 3. Configure application properties

Create your own:

\`\`\`bash
src/main/resources/application.properties
\`\`\`

Example:

\`\`\`properties
spring.datasource.url=jdbc:mysql://localhost:3306/fintrack_db
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080

jwt.secret=your-super-secret-key
jwt.expiration=86400000
\`\`\`

---

## Running the Project

Using Maven Wrapper:

\`\`\`bash
./mvnw spring-boot:run
\`\`\`

Or:

\`\`\`bash
mvn spring-boot:run
\`\`\`

---

## Authentication Flow

### Register
\`\`\`http
POST /api/auth/register
\`\`\`

### Login
\`\`\`http
POST /api/auth/login
\`\`\`

After login, copy the JWT token and authorize in Swagger UI:

\`\`\`text
Bearer <your-token>
\`\`\`

---

## Main Endpoints

### Auth
- \`POST /api/auth/register\`
- \`POST /api/auth/login\`

### Users
- \`GET /api/users\`
- \`GET /api/users/{id}\`
- \`POST /api/users\`
- \`PUT /api/users/{id}\`
- \`DELETE /api/users/{id}\`
- \`PATCH /api/users/{id}/status\`

### Transactions
- \`GET /api/transactions\`
- \`GET /api/transactions/{id}\`
- \`POST /api/transactions\`
- \`PUT /api/transactions/{id}\`
- \`DELETE /api/transactions/{id}\`
- \`GET /api/transactions/filter\`

### Categories
- \`GET /api/categories\`
- \`GET /api/categories/{id}\`
- \`POST /api/categories\`
- \`PUT /api/categories/{id}\`
- \`DELETE /api/categories/{id}\`
- \`GET /api/categories/type/{type}\`

### Dashboard
- \`GET /api/dashboard\`

---

## Testing

This API was tested using:

- **Swagger UI**
- **Postman**

---

## Status

Backend is functional and ready for integration with a frontend client such as:

- React
- Next.js
- Android app
- Any REST client

---

## Author

**Prashant Bhanage**

GitHub: [PrashantBhanage](https://github.com/PrashantBhanage)
