# Personal Movie Quote API

A simple RESTful API built in Kotlin with Spring Boot for managing and exploring famous movie quotes. This backend application allows users to add, list, retrieve, delete, and search quotes, as well as view a "quote of the day" that changes based on the current date. Data is stored in-memory (no database required).

## Features

- Add new quotes with text, character, movie, and release year.
- List all quotes.
- Retrieve a specific quote by ID.
- Delete a quote by ID.
- Search quotes by character name or movie title (case-insensitive substring match; optional parameters).
- Get the quote of the day (deterministic selection based on date; consistent throughout the day, changes daily).

## Prerequisites

- Java 17 or higher.
- Gradle (included in the project via wrapper).

## Running the Application

1. Clone the repository:
   ```
   git clone https://github.com/aaayushm23/Moviequotes.git
   cd Moviequotes
   ```
2. Build the project:
   ```
   ./gradlew build  # On Unix-like systems
   .\gradlew.bat build  # On Windows
   ```
3. Run the app:
   ```
   ./gradlew bootRun  # On Unix-like systems
   .\gradlew.bat bootRun  # On Windows
   ```
   - The API will be available at http://localhost:8080.
   - Data resets on restart (in-memory storage).

## Running Tests

The project includes automated tests to ensure reliability, covering essential logic with a mix of unit tests (isolated service methods) and integration tests (full API endpoints via MockMvc). Tests handle valid/invalid inputs, edge cases (e.g., empty lists, not found), and key paths. There are 15 tests in total (including the default Spring context load test).

- **Unit Tests (QuoteServiceTest.kt)**: Focus on isolated business logic in the service layer.

  - `add quote and get by id`: Adds a quote, verifies ID assignment, and retrieves it by ID.
  - `delete quote`: Adds a quote, deletes it, and confirms it's no longer retrievable.
  - `search quotes`: Adds multiple quotes, searches by character (substring match), and verifies filtered results.
  - `quote of the day - empty list`: Returns null when no quotes exist.
  - `quote of the day - with quotes`: Adds quotes and verifies a quote is returned (date-based selection).

- **Integration Tests (QuoteControllerTest.kt)**: Simulate HTTP requests to endpoints, testing full flow including validation and status codes.

  - `add quote - valid`: POSTs a valid quote, expects 201 Created.
  - `add quote - invalid`: POSTs with blank fields/invalid year, expects 400 Bad Request.
  - `get all quotes`: GETs the list, expects 200 OK (empty array initially).
  - `get quote by id - not found`: GETs non-existent ID, expects 404 Not Found.
  - `search quotes`: Adds a quote, searches by character, expects 200 OK with matches.
  - `get daily quote - no content if empty`: GETs daily with no quotes, expects 204 No Content.
  - `delete quote`: Adds a quote, deletes it (204 No Content), then retries delete (404 Not Found).

- **Default Test (MoviequotesApplicationTests.kt)**: Verifies the Spring application context loads correctly (basic sanity check).

To run tests:

```
./gradlew test  # On Unix-like systems
.\gradlew.bat test  # On Windows
```

- Tests use JUnit and Spring Boot Test framework.
- View results in `build/reports/tests/test/index.html` (includes pass/fail details, stack traces if any).

## Endpoints

All endpoints are under `/api/quotes`. Use tools like Postman or curl for testing.

| Method | Endpoint           | Description                      | Request Body/Params                                                         | Response (Success)                  | Notes                                                               |
| ------ | ------------------ | -------------------------------- | --------------------------------------------------------------------------- | ----------------------------------- | ------------------------------------------------------------------- |
| POST   | /api/quotes        | Add a new quote                  | JSON: `{ "text": "...", "character": "...", "movie": "...", "year": 1984 }` | 201 Created; JSON with ID           | Validation: Non-blank fields, year ≥1900 (400 Bad Request on fail). |
| GET    | /api/quotes        | List all quotes                  | None                                                                        | 200 OK; JSON array of quotes        | Empty array if no quotes.                                           |
| GET    | /api/quotes/{id}   | Get quote by ID                  | None                                                                        | 200 OK; JSON quote / 404 Not Found  | Replace {id} with actual ID.                                        |
| DELETE | /api/quotes/{id}   | Delete quote by ID               | None                                                                        | 204 No Content / 404 Not Found      | 204 on success; 404 if not found.                                   |
| GET    | /api/quotes/search | Search by character and/or movie | Query params: ?character=...&movie=... (optional)                           | 200 OK; JSON array of matches       | Case-insensitive substring search; empty array if no matches.       |
| GET    | /api/quotes/daily  | Get quote of the day             | None                                                                        | 200 OK; JSON quote / 204 No Content | Date-based selection; 204 if no quotes.                             |

## Notes

- **In-Memory Storage**: Quotes are stored in a list; data is lost on app restart.
- **Error Handling**: Proper HTTP codes (e.g., 400 for validation errors, 404 for not found).
- **Quote of the Day Logic**: Uses date-seeded random index for daily consistency (changes tomorrow if quotes exist).
- **Dependencies**: Spring Boot Starter Web, Validation; Kotlin reflect.
- **GitHub Repo**: https://github.com/aaayushm23/Moviequotes (includes all code and this README).

For questions or issues, refer to the code or open an issue on GitHub.
