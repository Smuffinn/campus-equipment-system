# Campus Equipment Loan System

A Spring Boot application for managing equipment loans in a campus environment.

## Features

- **Student Management**: Create, read, update, and delete student records
- **Equipment Management**: Manage equipment inventory with availability tracking
- **Loan Management**: Handle equipment loans with due dates and return tracking
- **Penalty Calculation**: Automatic penalty calculation for overdue loans
- **REST API**: Complete RESTful API for all operations

## Entities

### Student
- ID, Student Number, Name, Email
- Unique constraints on student number and email
- Validation for required fields and email format

### Equipment
- ID, Name, Type, Serial Number, Availability Status
- Unique constraint on serial number
- Convenience methods for availability management

### Loan
- ID, Equipment, Student, Start Date, Due Date, Return Date, Status
- Status: ACTIVE, RETURNED, OVERDUE
- Automatic overdue detection and penalty calculation

## API Endpoints

### Students
- `GET /api/students` - Get all students
- `GET /api/students/{id}` - Get student by ID
- `GET /api/students/studentNo/{studentNo}` - Get student by student number
- `POST /api/students` - Create new student
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Delete student
- `GET /api/students/{id}/loans` - Get loans for student
- `GET /api/students/{id}/loans/active` - Get active loans for student

### Equipment
- `GET /api/equipment` - Get all equipment
- `GET /api/equipment/{id}` - Get equipment by ID
- `GET /api/equipment/available` - Get available equipment
- `GET /api/equipment/type/{type}` - Get equipment by type
- `GET /api/equipment/available/type/{type}` - Get available equipment by type
- `GET /api/equipment/serial/{serialNumber}` - Get equipment by serial number
- `POST /api/equipment` - Create new equipment
- `PUT /api/equipment/{id}` - Update equipment
- `DELETE /api/equipment/{id}` - Delete equipment
- `POST /api/equipment/{id}/mark-unavailable` - Mark as unavailable
- `POST /api/equipment/{id}/mark-available` - Mark as available
- `GET /api/equipment/available/count` - Get count of available equipment

### Loans
- `GET /api/loans` - Get all loans
- `GET /api/loans/{id}` - Get loan by ID
- `POST /api/loans` - Create new loan
- `POST /api/loans/{id}/return` - Return loan
- `POST /api/loans/{id}/extend?days={days}` - Extend loan
- `GET /api/loans/student/{studentId}` - Get loans for student
- `GET /api/loans/overdue` - Get overdue loans
- `GET /api/loans/{id}/penalty` - Calculate penalty for loan

## Business Rules

- Students can have maximum 2 active loans
- Default loan period is 7 days
- Loan extensions are limited to 1-7 additional days
- Penalty rate: $5 per day for overdue items
- Equipment becomes unavailable when loaned out
- Equipment becomes available again when returned

## Database

- Uses H2 in-memory database for development
- Database console available at: `http://localhost:8080/h2-console`
- Connection details:
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: `password`

## Sample Data

The application automatically creates sample data on startup:
- 5 sample students
- 10 sample equipment items (laptops, projectors, cameras, tablets, microphone)

## Running the Application

### Prerequisites
- Java 11 or higher
- Maven (or use the included Maven wrapper)

### Steps
1. Navigate to the project directory
2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   Or on Windows:
   ```cmd
   mvnw.cmd spring-boot:run
   ```
3. Access the application at `http://localhost:8080`
4. Access H2 console at `http://localhost:8080/h2-console`

## Example Usage

### Create a Loan
```bash
POST /api/loans
Content-Type: application/json

{
  "student": {"id": 1},
  "equipment": {"id": 1}
}
```

### Return a Loan
```bash
POST /api/loans/1/return
```

### Get Available Equipment
```bash
GET /api/equipment/available
```

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                    REST API Layer                           │
├─────────────────┬─────────────────┬─────────────────────────┤
│ StudentController│ EquipmentController│   LoanController      │
│   /api/students │  /api/equipment │    /api/loans          │
└─────────────────┴─────────────────┴─────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────────┐
│                   Service Layer                             │
├─────────────────┬─────────────────┬─────────────────────────┤
│  StudentService │ EquipmentService│    LoanService          │
│                 │                 │  (+ Rules interface)    │
└─────────────────┴─────────────────┴─────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────────┐
│                 Repository Layer                            │
├─────────────────┬─────────────────┬─────────────────────────┤
│StudentRepository│EquipmentRepository│   LoanRepository       │
│  (JPA)          │    (JPA)        │      (JPA)             │
└─────────────────┴─────────────────┴─────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────────┐
│                    Entity Layer                             │
├─────────────────┬─────────────────┬─────────────────────────┤
│    Student      │    Equipment    │       Loan              │
│  - id           │  - id           │  - id                   │
│  - studentNo    │  - name         │  - student              │
│  - name         │  - type         │  - equipment            │
│  - email        │  - serialNumber │  - startDate            │
│                 │  - availability │  - dueDate              │
│                 │                 │  - returnDate           │
│                 │                 │  - status               │
└─────────────────┴─────────────────┴─────────────────────────┘
                           │
┌─────────────────────────────────────────────────────────────┐
│                H2 In-Memory Database                        │
└─────────────────────────────────────────────────────────────┘
```

## SOLID Principles Implementation

### Single Responsibility Principle (SRP)
- **Controllers**: Each controller handles only one entity type (Student, Equipment, Loan)
- **Services**: Each service manages business logic for one domain
- **Repositories**: Each repository handles data access for one entity
- **Entities**: Each entity represents a single business concept

### Open/Closed Principle (OCP)
- **Rules Interface**: `Rules` interface allows for different penalty calculation strategies
- **DailyRules**: Concrete implementation that can be extended without modifying existing code
- **Service Layer**: New business rules can be added through new service methods

### Liskov Substitution Principle (LSP)
- **Rules Implementation**: `DailyRules` can be substituted wherever `Rules` interface is expected
- **Repository Interfaces**: All repository implementations can be substituted with their interfaces

### Interface Segregation Principle (ISP)
- **Rules Interface**: Small, focused interface with single method `calculatePenalty()`
- **Repository Interfaces**: JPA repositories provide only necessary data access methods
- **Service Interfaces**: Each service exposes only relevant business operations

### Dependency Inversion Principle (DIP)
- **Service Dependencies**: Services depend on repository interfaces, not concrete implementations
- **Controller Dependencies**: Controllers depend on service interfaces through dependency injection
- **Rules Usage**: `LoanService` depends on `Rules` interface, not concrete `DailyRules` class

## Technology Stack

- Spring Boot 3.5.5
- Spring Data JPA
- Spring Web
- Spring Validation
- H2 Database
- Java 17
- Maven
