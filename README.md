# Item Service

Small hexagonal-architecture microservice for storing and comparing items.

## Overview

- Language: Java 17
- Framework: Spring Boot
- Architecture: Hexagonal (domain, application, adapters inbound/outbound)
- Storage: JSON files (JsonItemRepository)
- Default port: 8083

## Project layout (high level)

- com.items.domain — domain models, domain exceptions, domain validators, inbound/outbound ports
- com.items.application — application services / use-case implementations
- com.items.infraestructure.adapters.inbound.rest — HTTP controllers, exception handlers
- com.items.infraestructure.adapters.outbound.persistence — JSON file repository adapter
- com.items.infraestructure.config — Spring beans configuration
- src/main/resources — application.properties and logging configuration

## Prerequisites

- Java 17+
- Maven (mvn) available in PATH or use the project mvnw wrapper
- Optional: jq for pretty JSON in examples

## Build and run

From project root:

Build:
```bash
mvn clean install -DskipTests
```

Run (local):
```bash
mvn spring-boot:run
```

Or with wrapper:
```bash
./mvnw clean install -DskipTests
./mvnw spring-boot:run
```

If you run behind a proxy, set environment variables before mvn:
```bash
export HTTP_PROXY="http://user:pass@proxy.host:3128"
export HTTPS_PROXY="$HTTP_PROXY"
```

## Configuration

application.properties keys used:
- server.port — application port (default 8083)
- app.items.storage.path — folder where JSON files are stored (default: data/items)
- logging.file.name — optional log file

## Endpoints

Base URL: http://localhost:8083

- POST /items — create item
- PUT /items — update item
- GET /items/{id} — get item by id
- DELETE /items/{id} — delete item by id
- GET /items/compare?id1={id1}&id2={id2} — compare two items

Notes:
- id is a String in the domain model. The validator enforces non-null/non-blank id unless policy allows generation.
- JSON files for items are stored under `app.items.storage.path`.

## Example payloads

Create item (JSON):
```json
{
  "id": null,
  "name": "Item A",
  "imageUrl": "http://example.com/a.jpg",
  "description": "Gaming laptop",
  "price": 1500.00,
  "rating": 4.5
}
```

Update item (JSON):
```json
{
  "id": "11111111-1111-1111-1111-111111111111",
  "name": "Item A Updated",
  "imageUrl": "http://example.com/a.jpg",
  "description": "Updated description",
  "price": 1600.00,
  "rating": 4.7
}
```

Compare response sample:
```json
{
  "bestPriceItemId":"11111111-1111-1111-1111-111111111111",
  "bestPrice": 1500.0,
  "bestRankedItem":"22222222-2222-2222-2222-222222222222",
  "bestRating": 4.8,
  "differences":{
    "priceDifference":"100.00",
    "ratingDifference":"0.3"
  }
}
```

## Example curl commands

Create two items (use quotes carefully in bash/Git Bash):
```bash
resp1=$(curl -s -X POST http://localhost:8083/items \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Item A","imageUrl":"http://x","description":"desc A","price":100.0,"rating":4.2}')

resp2=$(curl -s -X POST http://localhost:8083/items \
  -H "Content-Type: application/json" \
  -d '{"id":null,"name":"Item B","imageUrl":"http://x","description":"desc B","price":150.0,"rating":4.8}')
```

Extract ids (jq recommended):
```bash
id1=$(echo "$resp1" | jq -r '.id')
id2=$(echo "$resp2" | jq -r '.id')
```

Compare:
```bash
curl -s "http://localhost:8083/items/compare?id1=${id1}&id2=${id2}" | jq .
```

Get:
```bash
curl -s "http://localhost:8083/items/${id1}" | jq .
```

Update:
```bash
updated=$(echo "$resp1" | jq '.name="Item A Updated"')
curl -s -X PUT http://localhost:8083/items -H "Content-Type: application/json" -d "$updated"
```

Delete:
```bash
curl -s -X DELETE "http://localhost:8083/items/${id2}"
```

## Thunder Client / HTTP client

You can import requests using a `.http` or `.json` collection. Example file `thunder-requests.http` (placed in project root) contains sample requests for Create/Get/Compare/Update/Delete.

## Validation and errors

- Domain validation is implemented in ItemValidator. It checks:
  - item not null
  - id not null/blank (policy can be changed to allow auto-generation)
  - price not null and non-negative
  - rating not null and between 0.0 and 5.0
  - name not null/blank
- Custom exceptions:
  - InvalidItemException (400)
  - ItemNotFoundException (404)
- GlobalExceptionHandler logs full exceptions and returns sanitized JSON error responses.

Example error response:
```json
{
  "code":"INVALID_REQUEST",
  "message":"Item price cannot be negative"
}
```

## Logging

Enable file logging in application.properties:
```
logging.level.root=INFO
logging.level.com.items=DEBUG
logging.file.name=logs/item-app.log
```
Tail logs during development:
```bash
tail -f logs/item-app.log
```

## Troubleshooting

- 404 on /items:
  - Ensure main application class is annotated with @SpringBootApplication and scanBasePackages includes `com.items` or main is in package `com.items`.
  - Check `server.servlet.context-path` in application.properties.

- 404 on /actuator/health:
  - Add `spring-boot-starter-actuator` dependency and in application.properties:
    ```
    management.endpoints.web.exposure.include=health,info,mappings
    management.endpoint.health.show-details=always
    ```
  - Restart app after adding dependency.

- Maven plugin resolution errors:
  - If behind a corporate proxy, set HTTP_PROXY/HTTPS_PROXY env vars or configure `~/.m2/settings.xml` with proxy/mirrors.
  - To force metadata refresh:
    ```bash
    mvn -U clean install
    ```

- JSON parse errors (400 / HttpMessageNotReadableException):
  - Validate request body is valid JSON (use `jq .` to check).
  - Set Content-Type: application/json in the client.
  - Ensure numeric types match domain expectations (price -> number/decimal, rating -> number).

## Design notes

- Hexagonal architecture: controllers are inbound adapters; repository is outbound adapter; ItemService orchestrates use-cases; domain contains validation and exceptions.
- Repository persists each Item as a JSON file named by item id under the configured storage path.
- Domain exceptions are unchecked (RuntimeException) to avoid propagating checked exceptions through ports.

## Diagrams (Mermaid)

Flowchart (architecture):
```mermaid
flowchart LR
  A[REST Controller] --> B[Application Service (ItemService)]
  B --> C[Domain (Item, Validator, Exceptions)]
  B --> D[Outbound Port: ItemRepositoryPort]
  D --> E[JsonItemRepository (file storage)]
```

Entity (simplified):
```mermaid
erDiagram
  ITEM {
    string id PK
    string name
    string imageUrl
    string description
    decimal price
    double rating
  }
```

Sequence (compare use-case):
```mermaid
sequenceDiagram
  participant Client
  participant Controller
  participant Service
  participant Repository
  Client->>Controller: GET /items/compare?id1&id2
  Controller->>Service: compare(id1,id2)
  Service->>Repository: findById(id1)
  Repository-->>Service: Item1
  Service->>Repository: findById(id2)
  Repository-->>Service: Item2
  Service-->>Controller: ComparisionResult
  Controller-->>Client: 200 OK (result JSON)
```

## API contract summary

- All endpoints use JSON.
- Standard HTTP codes: 200 OK, 201 Created (if implemented), 400 Bad Request, 404 Not Found, 500 Internal Server Error.
- Use domain validator for request validation; use GlobalExceptionHandler for mapping exceptions to HTTP responses.

## Contributing / Development

- Keep domain validation inside `com.items.domain`.
- Keep controllers thin; map DTOs to domain objects here if you add DTOs.
- Keep repository adapter isolated; it implements the outbound port only.

## Generating Javadoc

```bash
mvn javadoc:javadoc
# output at target/site/apidocs
```

## Contact / Notes

This README provides runnable examples, troubleshooting steps, and diagrams for quick onboarding. Adjust configuration and validation policy as needed for your use case.