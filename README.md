# Employee Management System

This project is a Spring Boot application designed to manage employees and departments within a company. It provides RESTful APIs for adding employees and retrieving eligible employees for bonuses based on specific criteria.

## Features

- **Add Employees**: Endpoint to add multiple employees in bulk.
- **Retrieve Eligible Employees**: Endpoint to fetch employees eligible for bonuses based on a given date.
- **Department Management**: Supports associating employees with departments.
- **Error Handling**: Provides basic error handling for unexpected scenarios.

## Technologies Used

- **Spring Boot**: Framework for creating production-grade Spring-based applications.
- **Spring Data JPA**: Simplifies the implementation of data access layer.
- **MySQL Database**: SQL database for development and testing purposes.
- **Lombok**: Java library to reduce boilerplate code.
- **JUnit 5**: Testing framework for unit testing.

## Project Structure

- **Controller Layer**: Handles incoming HTTP requests and delegates business logic to the service layer.
- **Service Layer**: Contains business logic, interacts with repositories for data access.
- **Model Layer**: Defines entities (`Employee` and `Department`) and their relationships.
- **Exception Handling**: Basic exception handling for better error reporting.

## Setup

To run this project locally, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone <repository_url>
   cd employee-management-system
   ```

2. **Build the project**:
   ```bash
   ./gradlew build
   ```

3. **Run the application**:
   ```bash
   ./gradlew bootRun
   ```
   The application will start on `http://localhost:8080`.

4. **Access API endpoints**:
   - Add employees: `POST http://localhost:8080/tci/employee-bonus`
   - Get eligible employees: `GET http://localhost:8080/tci/employee-bonus?date=<dateString>`

## API Documentation

- **POST `/tci/employee-bonus`**
  - Request Body: JSON payload with a key `"employees"` containing a list of employees.
  - Response: Returns status `201 Created` on success or `400 Bad Request` if the payload is invalid.

- **GET `/tci/employee-bonus?date=<dateString>`**
  - Query Parameters: `date` (required) - Date string in format `"MMM-dd-yyyy"`.
  - Response: Returns JSON formatted response with eligible employees categorized by currency. Possible status codes: `200 OK` or `500 Internal Server Error`.

## Additional Notes

- Ensure the date format for the `date` query parameter in the GET request (`MMM-dd-yyyy`) matches the expected format.
- Error handling is currently minimal and can be extended based on specific use cases and exceptions encountered.
