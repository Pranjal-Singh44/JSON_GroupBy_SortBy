# JSON Dataset API – FreightFox Assignment

This is a Spring Boot application that allows users to insert, group, and sort dynamic JSON records in named datasets. The data is stored in an in-memory H2 database, and all interaction is done via REST APIs.

---

## How to Run the Application

### Requirements

- Java 17+
- Maven
- Internet connection (for Maven dependencies)

### Steps

1. **Clone the repository**

   ```bash
   git clone <your-repo-url>
   cd <your-repo-folder>
   ```

2. **Build and run the application**

   ```bash
   mvn spring-boot:run
   ```

3. **Access H2 Console** (optional)
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:json_dataset_db`
   - Username: `sa` (no password)
   - Click "Connect" to view data

---

## API Endpoints

### 1. Insert JSON Record

- **URL:** `POST /api/dataset/{datasetName}/record`
- **Description:** Inserts a single JSON object into the specified dataset.
- **Request Body (JSON):**

```json
{
  "id": 1,
  "name": "John Doe",
  "age": 30,
  "department": "Engineering"
}
```

- **Example:**

```bash
POST http://localhost:8080/api/dataset/employee_dataset/record
```

---

### 2. Query Dataset with Group By

- **URL:** `GET /api/dataset/{datasetName}/query?groupBy=fieldName`
- **Description:** Groups records in the dataset by the specified field.
- **Example:**

```bash
GET http://localhost:8080/api/dataset/employee_dataset/query?groupBy=department
```

- **Response:**

```json
{
  "groupedRecords": {
    "Engineering": [ {...}, {...} ],
    "Marketing": [ {...} ]
  }
}
```

---

### 3. Query Dataset with Sort By

- **URL:** `GET /api/dataset/{datasetName}/query?sortBy=fieldName&order=asc|desc`
- **Description:** Sorts records in the dataset by the specified field in ascending or descending order.
- **Example:**

```bash
GET http://localhost:8080/api/dataset/employee_dataset/query?sortBy=age&order=desc
```

- **Response:**

```json
{
  "sortedRecords": [
    { "age": 45, ... },
    { "age": 30, ... },
    { "age": 22, ... }
  ]
}
```

---

## Error Handling

The API returns meaningful error messages:

```json
{
  "error": "Empty dataset: employee_dataset",
  "status": 404,
  "timestamp": "2025-07-05T13:10:58.012Z"
}
```

---

## Technologies Used

- Spring Boot
- H2 In-Memory Database
- Jackson (JSON)
- Maven
- Lombok

---

---

## Tests

This project includes unit tests using **JUnit 5** and **Mockito**.

### Covered Test Classes:

- `DatasetServiceImplTest.java`
  - Tests for insert, query by group and sort, and empty dataset error
- `DatasetControllerTest.java`
  - Tests for API responses via controller endpoints

### How to Run Tests

From your project root, run:

```bash
mvn test
```

Test results will appear in the console or under `target/surefire-reports/`.

---

## Notes

- This app uses an **in-memory H2 database** — data will be lost after restarting the app.
- The app auto-creates required tables using JPA and Hibernate.
- H2 Console is enabled for visual inspection of records.

---
