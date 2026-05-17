# Signal API

A Spring Boot REST API for managing signals with JWT authentication.

## Prerequisites

- Java 24
- Maven
- PostgreSQL (local or remote)

## Setup

1. Clone the repository:
```bash
git clone https://github.com/Dot-KT/JSignal.git
cd JSignal
```

2. Set environment variables (or use `script.bat` on Windows):
```bash
set DATABASE_URL=jdbc:postgresql://localhost:5432/signaldb
set DATABASE_USERNAME=postgres
set DATABASE_PASSWORD=your_password
set JWT_SECRET=my-super-secret-key-that-is-at-least-256-bits-long-for-hs256
```

3. Run the application:
```bash
mvn spring-boot:run
```

The server starts at `http://localhost:8080`.

---

## Authentication

All `/api/v1/signals/**` endpoints require a JWT token. First register and login to get one.

### Register

**POST** `http://localhost:8080/api/v1/auth/register`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "username": "admin",
  "email": "admin@signal.com",
  "password": "password123"
}
```

**Response** `201 Created`:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6...",
  "username": "admin",
  "email": "admin@signal.com"
}
```

### Login

**POST** `http://localhost:8080/api/v1/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body:**
```json
{
  "username": "admin",
  "password": "password123"
}
```

**Response** `200 OK`:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6...",
  "username": "admin",
  "email": "admin@signal.com"
}
```

### Using the Token

Copy the `token` from the login/register response and include it in all subsequent requests:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6...
```

---

## Signal Endpoints

All endpoints below require the `Authorization: Bearer <token>` header.

### Create a Signal

**POST** `http://localhost:8080/api/v1/signals`

```json
{
  "type": "robbery",
  "status": "active",
  "text": "Armed robbery at Shell garage, two suspects fled on foot",
  "rawText": "armed robbery shell garage two suspects fled on foot",
  "latitude": -26.1076,
  "longitude": 28.0567,
  "reporterPhone": "+27834567890",
  "reference": "SIG-2026-0001",
  "address": "45 Rivonia Road, Sandton",
  "occurredAt": "2026-05-12T15:30:00Z",
  "hasCctv": true,
  "communityId": "sandton-central",
  "images": ["https://example.com/evidence1.jpg"],
  "suspects": [
    {
      "name": "Unknown Male",
      "description": "Tall, black hoodie, blue jeans",
      "phoneNumber": null,
      "imageUrl": null
    }
  ],
  "vehicles": [
    {
      "type": "hatchback",
      "brand": "Volkswagen",
      "model": "Polo",
      "color": "silver",
      "license": "GP 456 XYZ",
      "direction": "southbound",
      "movement": "parked then fled",
      "extra": null
    }
  ],
  "tags": ["armed", "robbery", "urgent"],
  "metadata": {
    "reportPath": "whatsapp",
    "reportAction": "new_report",
    "reportPathLabel": "WhatsApp Bot"
  },
  "isPrimary": true,
  "flowCompleted": true,
  "isUnderReview": false,
  "isLinkedToIncident": false,
  "isActive": true,
  "priorityScore": 9,
  "reporterId": "reporter-5521",
  "externalMessageId": "wa-msg-88431",
  "submissionTime": "2026-05-12T15:35:00Z"
}
```

### Get All Signals

**GET** `http://localhost:8080/api/v1/signals`

### Get Signal by ID

**GET** `http://localhost:8080/api/v1/signals/{id}`

### Search & Filter Signals

**GET** `http://localhost:8080/api/v1/signals?search=robbery&type=robbery&status=active&StartDate=2026-01-01T00:00:00Z&EndDate=2026-12-31T23:59:59Z&hasPhotoAttached=true&limit=20`

| Param | Description |
|-------|-------------|
| `search` | Search text, rawText, address, reference |
| `type` | Filter by signal type |
| `status` | Filter by status |
| `StartDate` | Signals created on or after (ISO 8601) |
| `EndDate` | Signals created on or before (ISO 8601) |
| `hasPhotoAttached` | `true` or `false` |
| `communityId` | Filter by community |
| `activeOnly` | `true` for active signals only |
| `cursorAt` | Cursor timestamp for pagination |
| `cursorId` | Cursor ID for pagination |
| `limit` | Page size (default 20) |

### Update a Signal

**PUT** `http://localhost:8080/api/v1/signals/{id}`

### Delete a Signal

**DELETE** `http://localhost:8080/api/v1/signals/{id}`

### Deactivate a Signal

**PATCH** `http://localhost:8080/api/v1/signals/{id}/deactivate`

---

## Swagger UI

Access the interactive API docs at:

```
http://localhost:8080/swagger-ui/index.html
```

New additions
date filter. custom range.
search by: signal ID, location, category.
Story book. (frontend tool .leanr it.)