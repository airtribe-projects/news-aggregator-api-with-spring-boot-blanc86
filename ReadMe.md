# News Aggregator API

A Spring Boot application that provides personalized news aggregation with JWT authentication.

## Features

- User registration and login with JWT authentication
- Personalized news preferences management
- News fetching from external APIs based on user preferences
- In-memory H2 database for data storage
- Comprehensive error handling and validation
- RESTful API endpoints

## Tech Stack

- **Spring Boot 3.5.3** - Main framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database
- **JWT (JSON Web Tokens)** - Token-based authentication
- **Spring WebFlux** - Asynchronous HTTP client
- **Maven** - Build tool
- **Java 24** - Programming language

## Quick Start

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- News API key (optional, defaults to sample data)

### Installation

1. Clone the repository
2. Navigate to the project directory
3. Update `application.properties` with your News API key:
   ```properties
   news.api.key=your-news-api-key-here
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication

#### Register User
- **URL**: `POST /api/register`
- **Body**:
  ```json
  {
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }
  ```

#### Login User
- **URL**: `POST /api/login`
- **Body**:
  ```json
  {
    "username": "testuser",
    "password": "password123"
  }
  ```
- **Response**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "username": "testuser",
    "email": "test@example.com"
  }
  ```

### User Preferences (Requires Authentication)

#### Get User Preferences
- **URL**: `GET /api/preferences`
- **Headers**: `Authorization: Bearer {token}`

#### Update User Preferences
- **URL**: `PUT /api/preferences`
- **Headers**: `Authorization: Bearer {token}`
- **Body**:
  ```json
  {
    "categories": ["technology", "business", "health"]
  }
  ```

### News (Requires Authentication)

#### Get News Articles
- **URL**: `GET /api/news`
- **Headers**: `Authorization: Bearer {token}`

## Available News Categories

- business
- entertainment
- general
- health
- science
- sports
- technology

## Database

The application uses H2 in-memory database. You can access the H2 console at:
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (empty)

## Configuration

Key configuration properties in `application.properties`:

```properties
# JWT Configuration
jwt.secret=mySecretKey123456789012345678901234567890
jwt.expiration=86400000

# News API Configuration
news.api.key=your-news-api-key-here
news.api.url=https://newsapi.org/v2

# Server Configuration
server.port=8080
```

## Error Handling

The application provides comprehensive error handling for:
- Validation errors (400 Bad Request)
- Authentication errors (401 Unauthorized)
- User not found errors (404 Not Found)
- General server errors (500 Internal Server Error)

## Security Features

- JWT token-based authentication
- Password encryption using BCrypt
- CORS support
- Session management disabled (stateless)
- Protected endpoints requiring valid JWT tokens

## Testing

Use the provided curl commands or Postman collection to test the API endpoints. Make sure to:

1. Register a user first
2. Login to get JWT token
3. Use the token in Authorization header for protected endpoints
4. Set news preferences before fetching news

## Project Structure

```
src/main/java/com/example/newsaggregator/
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   └── NewsController.java
├── dto/
│   └── [DTOs].java
├── entity/
│   └── User.java
├── exception/
│   └── GlobalExceptionHandler.java
├── repository/
│   └── UserRepository.java
├── security/
│   ├── JwtAuthenticationFilter.java
│   ├── JwtUtil.java
│   └── UserDetailsServiceImpl.java
├── service/
│   ├── NewsService.java
│   └── UserService.java
└── NewsAggregatorApplication.java
```

