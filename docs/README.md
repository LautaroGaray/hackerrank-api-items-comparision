# Item Service

Small hexagonal-architecture microservice for storing and comparing items.

## Overview

- Language: Java 17
- Framework: Spring Boot
- Architecture: Hexagonal (domain, application, adapters inbound/outbound)
- Storage: JSON files (JsonItemRepository)
- Default port: 8083
- API Documentation: Swagger/OpenAPI 3.0

## Build and run

Build:
```bash
mvn clean install -DskipTests
```

Run:
```bash
mvn spring-boot:run
```

## Swagger

- Swagger UI: http://localhost:8083/swagger-ui.html
- OpenAPI JSON: http://localhost:8083/v3/api-docs

## Endpoints

Base: http://localhost:8083

- POST /auth/login — obtain JWT (public)
- POST /items — create item (requires Authorization)
- PUT /items — update item (requires Authorization)
- GET /items/{id} — get item by id (requires Authorization)
- DELETE /items/{id} — delete item (requires Authorization)
- GET /items/compare?id1={id1}&id2={id2} — compare items (requires Authorization)

## Authentication (JWT)

- Obtain token: POST /auth/login with JSON body: {"username":"admin","password":"adminpass"}
- Use token in requests: header `Authorization: Bearer <token>`

Example login request:
```bash
curl -s -X POST http://localhost:8083/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"adminpass"}' | jq .
```

## Example usage (bash)

1) Login and store token:
```bash
TOKEN=$(curl -s -X POST http://localhost:8083/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"adminpass"}' | jq -r '.token')
```

2) Create item (use stored token):
```bash
curl -s -X POST http://localhost:8083/items \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name":"Laptop",
    "imageUrl":"http://example.com/laptop.jpg",
    "description":"Gaming laptop",
    "price":1500.00,
    "rating":4.5
  }' | jq .
```

3) Compare items:
```bash
curl -s "http://localhost:8083/items/compare?id1=${ID1}&id2=${ID2}" \
  -H "Authorization: Bearer $TOKEN" | jq .
```

4) Get / Update / Delete (include Authorization header):
```bash
curl -s "http://localhost:8083/items/${ID}" -H "Authorization: Bearer $TOKEN" | jq .
curl -s -X PUT http://localhost:8083/items -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d @item.json | jq .
curl -s -X DELETE "http://localhost:8083/items/${ID}" -H "Authorization: Bearer $TOKEN"
```

## Notes

- Keep `app.jwt.secret` out of source control; prefer environment variables or a secret manager.
- Passwords must be stored hashed (BCrypt) in production.
- Use `actuator/mappings` to inspect registered controllers when debugging.
- For troubleshooting, check logs in `logs/item-app.log`.