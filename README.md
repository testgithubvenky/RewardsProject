# RewardsProject
#  Rewards Program API

This is a Spring Boot-based RESTful API for a customer rewards program. It tracks customer transactions and calculates reward points based on transaction amounts. The system provides endpoints to retrieve transaction history and reward summaries for individual customers.

##  Features

- Retrieve all customer transactions
- Filter transactions by customer and month
- View recent transactions (last 3 months)
- Calculate and summarize reward points per customer

##  Project Structure

- **Controller**: `RewardsController` — Handles HTTP requests and maps them to service methods.
- **Service**: `RewardService` — Contains business logic for transaction retrieval and reward calculation.
- **DTO**: `RewardSummaryDTO` — Data Transfer Object for reward summaries.
- **Model**: `Transaction` — Represents a customer transaction.
- **Repository**: `TransactionRepository` — Interface for database operations (assumed to be a Spring Data JPA repository).

##  Reward Calculation Logic

Reward points are calculated based on the amount spent in a transaction. The logic is encapsulated in a utility class `RewardCalculator` (not shown here). Typically, the rules might be:

- 2 points for every dollar spent over $100
- 1 point for every dollar spent over $50 up to $100

##  API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/rewards/transactions/recent` | Get all transactions from the last 3 months |
| `GET` | `/rewards/transactions/{customerId}` | Get all transactions for a specific customer |
| `GET` | `/rewards/transactions/{customerId}/{month}` | Get transactions for a customer in a specific month (`YYYY-MM` format) |
| `GET` | `/rewards/summary/{customerId}` | Get reward summary (monthly and total points) for a customer |

##  Technologies Used

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- H2 / MySQL (assumed for persistence)
- Maven 

##  Getting Started

1. Clone the repository
2. Configure your database in `application.properties`
3. Run the application using your IDE or `mvn spring-boot:run`
4. Access the API at `http://localhost:8080/rewards/`

## Notes

- Ensure the `RewardCalculator` class is implemented and injected properly.
- Exception handling is done using a custom `ResourceNotFoundException`.
