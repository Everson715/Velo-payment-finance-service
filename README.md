# Velo Payment Finance Service

The **Velo Payment Finance Service** is a core microservice in the Velo platform responsible for handling all financial transactions, payments, wallets, and auditing operations. It is built to ensure a robust, ACID-compliant architecture with strict security measures.

## Technologies Used

* **Java**: 17+
* **Framework**: Spring Boot 3.2.x
* **Database**: PostgreSQL with Spring Data JPA
* **Security**: Spring Security & JWT (RBAC)
* **Build Tool**: Maven
* **Other**: Lombok, Spring Boot Actuator

## Features

* **Wallet Operations**: Secure wallet management with pessimistic locking for safe concurrent operations.
* **Payment Processing**: Authorization, capture (with idempotency), refunds, and withdrawals.
* **Security & Auditing**: Role-Based Access Control (RBAC) using JWT, and Correlation ID propagation for distributed tracing and auditing.
* **Transaction Management**: Strict ACID compliance using Spring Data JPA.

## Prerequisites

To build and run this service locally, you will need:
* Java 17 (JDK)
* Maven 3.8+
* PostgreSQL database instance running

## Setup and Installation

1. **Clone the repository** (if not already done).
2. **Configure the Database**:
   Create a PostgreSQL database for the service, for example: `velo_payment_db`.

3. **Environment Variables**:
   Set the following environment variables or configure them in your `application.yml` / `application.properties`:
   * `DB_URL`: JDBC URL for PostgreSQL (e.g., `jdbc:postgresql://localhost:5432/velo_payment_db`)
   * `DB_USERNAME`: Database user
   * `DB_PASSWORD`: Database password
   * `JWT_SECRET`: Secret key used for verifying JSON Web Tokens

## Building and Running

Compile the project and run the tests using Maven:
```bash
mvn clean install
```

Run the application:
```bash
mvn spring-boot:run
```
Alternatively, build the JAR file and run it:
```bash
mvn clean package
java -jar target/payment-finance-service-0.0.1-SNAPSHOT.jar
```

## Project Structure

The project follows a clean, layered architecture:
* `controller/`: REST APIs and endpoints.
* `service/`: Core business logic and transaction management.
* `repository/`: Data access layer with Spring Data JPA.
* `dto/`: Data Transfer Objects for request/response payloads.
* `exception/`: Global exception handling and custom exceptions.
* `security/`: JWT filters, RBAC configurations, and Correlation ID management.
* `model/`: JPA Entities mapping to database tables.
