# Bajaj Finserv Health (BFHL) REST API

A production-ready Spring Boot 3 REST API that accepts a mixed array of strings (numbers, alphabets, special characters) and returns categorized, processed results.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.3.4 |
| Build | Maven |
| Utilities | Lombok |
| Testing | JUnit 5, Mockito, MockMvc |

---

## Project Structure

```text
src/main/java/com/example/demo/
├── controller/
│   └── BFHLController.java       # POST /bfhl endpoint
├── dto/
│   ├── BFHLRequest.java          # Input: { "data": [...] }
│   └── BFHLResponse.java         # Full categorized response
├── service/
│   ├── BFHLService.java          # Interface
│   └── BFHLServiceImpl.java      # All business logic
├── util/
│   └── Constants.java            # User identity config (editable)
└── DemoApplication.java          # Entry point
```

---

## API Endpoint

### `POST /bfhl`

**Request Body:**
```json
{
  "data": ["a", "1", "334", "4", "R", "$"]
}
```

**Response:**
```json
{
  "is_success": true,
  "user_id": "shreyansh_sharma_12102005",
  "email": "sharmashreyansh340@gmail.com",
  "roll_number": "21BCE1210",
  "odd_numbers": ["1"],
  "even_numbers": ["334", "4"],
  "alphabets": ["A", "R"],
  "special_characters": ["$"],
  "sum": "339",
  "concat_string": "Ra"
}
```

---

## Business Logic

| Rule | Detail |
|---|---|
| Numbers | Separated into odd/even lists; all values remain strings |
| Alphabets | Returned in UPPERCASE |
| Special chars | Any token that is not purely numeric or purely alphabetic |
| Sum | Arithmetic sum of all numeric tokens, returned as string |
| concat_string | All alpha chars concatenated → reversed → alternating caps (upper, lower, upper…) |
| user_id | Fixed DOB-based constant: `shreyansh_sharma_12102005` |

---

## How to Run Locally

**Prerequisites:** Java 17+, Maven 3.8+

```bash
# Clone the repo
git clone https://github.com/shreyanshsharma-1210/Bajaj_api_round.git
cd Bajaj_api_round

# Run the application
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080/bfhl`.

**Test with curl:**
```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -d '{"data": ["a", "1", "334", "4", "R", "$"]}'
```

---

## How to Build JAR

```bash
./mvnw clean package
```

The runnable JAR will be at:
```
target/demo-0.0.1-SNAPSHOT.jar
```

Run it directly:
```bash
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

---

## Running Tests

```bash
./mvnw test
```

Test coverage includes:
- `BFHLServiceImplTest` — unit tests for all business logic (9 test cases)
- `BFHLControllerTest` — MockMvc tests for the POST /bfhl endpoint (3 test cases)

---

## Configuration

To change the user identity, edit `src/main/java/com/example/demo/util/Constants.java`:

```java
public static final String USER_ID = "shreyansh_sharma_12102005";
public static final String EMAIL = "sharmashreyansh340@gmail.com";
public static final String ROLL_NUMBER = "21BCE1210";
```

---

## Hosting on Railway / Render

1. Build the JAR: `./mvnw clean package`
2. Push the repo to GitHub
3. On Railway/Render, set the start command to:
   ```
   java -jar target/demo-0.0.1-SNAPSHOT.jar
   ```
4. Set the port via environment variable if needed:
   ```
   SERVER_PORT=8080
   ```
5. The `/bfhl` endpoint will be live at your deployment URL.

---

## License

For assignment use only.
