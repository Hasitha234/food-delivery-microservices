# 🚀 Food Delivery Microservices - Delivery Service

This is a **complete, working Spring Boot microservice** for managing food delivery operations. It's part of a larger food delivery system with multiple independent services.

## ✅ Status: FULLY FUNCTIONAL & TESTED

- ✅ **All 7 API endpoints working**
- ✅ **Compiled without errors**
- ✅ **Running on port 8084**
- ✅ **Full Swagger UI documentation**
- ✅ **All CRUD operations working**
- ✅ **Error handling implemented**
- ✅ **Ready for integration**

---

## 📋 What This Service Does

The Delivery Service handles everything related to order delivery:

### Core Features
- **Assign Delivery to Order**: Create a new delivery when an order is placed
- **View All Deliveries**: Get list of all deliveries in the system
- **View Single Delivery**: Get detailed info about a specific delivery
- **Track Delivery Status**: Check current status (PENDING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED)
- **Update Status**: Change delivery status as it progresses
- **Get Deliveries by Order**: Find all deliveries for a specific order
- **Assign Delivery Agent**: Assign a driver/agent to handle the delivery

---

## 🎯 Technology Stack

| Component | Technology |
|-----------|------------|
| **Framework** | Spring Boot 3.3.5 |
| **Language** | Java 17 |
| **Build Tool** | Maven 3 |
| **Database** | In-memory (ConcurrentHashMap) - for now |
| **Documentation** | Swagger/OpenAPI 3 |
| **Server** | Embedded Tomcat |
| **Port** | 8084 |

---

## 📂 Project Structure

```
delivery-service/
├── pom.xml
├── target/
│   └── delivery-service-1.0.0.jar          (Executable!)
├── src/
│   ├── main/
│   │   ├── java/com/fooddelivery/delivery/
│   │   │   ├── DeliveryServiceApplication.java     (Entry point)
│   │   │   ├── controller/
│   │   │   │   └── DeliveryController.java         (API endpoints)
│   │   │   ├── service/
│   │   │   │   └── DeliveryService.java            (Business logic)
│   │   │   ├── repository/
│   │   │   │   └── DeliveryRepository.java         (Data access)
│   │   │   ├── domain/
│   │   │   │   ├── Delivery.java                   (Entity)
│   │   │   │   └── DeliveryStatus.java             (Status enum)
│   │   │   ├── dto/
│   │   │   │   ├── DeliveryRequest.java            (Input validation)
│   │   │   │   ├── DeliveryResponse.java           (API response)
│   │   │   │   ├── DeliveryStatusResponse.java     (Status response)
│   │   │   │   └── UpdateDeliveryStatusRequest.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java     (Error handling)
│   │   │   │   ├── DeliveryNotFoundException.java
│   │   │   │   └── ApiErrorResponse.java
│   │   │   └── config/
│   │   │       └── OpenApiConfig.java              (Swagger config)
│   │   └── resources/
│   │       └── application.yml                     (Configuration)
```

---

## 🚀 Quick Start

### Prerequisites
- Java 17+ → `java -version`
- Maven installed → `mvn -version`

### 1. Build

```bash
cd food-delivery-microservices
mvn clean install -DskipTests
```

**Takes ~30 seconds on first build (downloads dependencies)**

### 2. Run

```bash
java -jar delivery-service/target/delivery-service-1.0.0.jar
```

**Expected output:**
```
Started DeliveryServiceApplication in X seconds
Tomcat started on port 8084
```

### 3. Test

**Option A: Swagger UI (Easiest)**
```
Open: http://localhost:8084/swagger-ui.html
Click "Try it out" on any endpoint
```

**Option B: PowerShell**
```powershell
# Create delivery
$headers = @{"Content-Type"="application/json"}
$body = @{orderId="ORD-001"; customerId="CUST-001"; restaurantId="REST-001"; pickupAddress="123 Main St"; deliveryAddress="456 Oak Ave"} | ConvertTo-Json
Invoke-WebRequest -Uri "http://localhost:8084/api/deliveries" -Method POST -Headers $headers -Body $body -UseBasicParsing

# Get all
Invoke-WebRequest -Uri "http://localhost:8084/api/deliveries" -UseBasicParsing
```

---

## 📚 API Endpoints

### Create Delivery
```http
POST /api/deliveries
Content-Type: application/json

{
  "orderId": "ORD-001",
  "restaurantId": "REST-001",
  "customerId": "CUST-001",
  "pickupAddress": "123 Main St",
  "deliveryAddress": "456 Oak Ave",
  "notes": "ring doorbell"
}
```

**Response (HTTP 201):**
```json
{
  "deliveryId": "abc123",
  "orderId": "ORD-001",
  "status": "PENDING",
  "createdAt": "2026-03-31T12:57:55",
  ...
}
```

### Get All Deliveries
```http
GET /api/deliveries
```

### Get Single Delivery
```http
GET /api/deliveries/{deliveryId}
```

### Get Deliveries by Order
```http
GET /api/deliveries/by-order/{orderId}
```

### Get Delivery Status
```http
GET /api/deliveries/{deliveryId}/status
```

### Update Status
```http
PUT /api/deliveries/{deliveryId}/status?status=OUT_FOR_DELIVERY
```

Valid statuses: `PENDING`, `OUT_FOR_DELIVERY`, `DELIVERED`, `CANCELLED`

### Assign Delivery Agent
```http
PUT /api/deliveries/{deliveryId}/assign-agent?agentId=AGENT-001
```

---

## 🏗️ Architecture

### Layered Design

```
┌─────────────────────────────────────┐
│      HTTP Requests                  │
│      (From Client)                  │
└────────────────┬────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│   🎯 CONTROLLER LAYER               │
│   - Receive HTTP requests           │
│   - Validate input                  │
│   - Return HTTP responses           │
│   - File: DeliveryController.java   │
└────────────────┬────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│   💼 SERVICE LAYER                  │
│   - Business logic                  │
│   - Create/Update/Delete operations │
│   - Status transitions              │
│   - File: DeliveryService.java      │
└────────────────┬────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│   💾 REPOSITORY LAYER               │
│   - Data access                     │
│   - Save/Retrieve data              │
│   - File: DeliveryRepository.java   │
└────────────────┬────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│   💿 DATA STORAGE                   │
│   - ConcurrentHashMap (in-memory)   │
│   - Later: Database (MySQL/Postgres)│
└─────────────────────────────────────┘
```

### Why This Design?

| Benefit | Reason |
|---------|--------|
| **Separation of Concerns** | Each layer has one responsibility |
| **Testability** | Can test each layer independently |
| **Reusability** | Service can be used by multiple controllers |
| **Maintainability** | When requirements change, only one layer affected |
| **Scalability** | Can swap storage layer (in-memory → database) without changing service/controller |

---

## 🛠️ How It Works (Conceptually)

### Example: Creating a Delivery

```
1. Client sends:
   POST /api/deliveries
   {orderId: "ORD-001", ...}

2. DeliveryController.createDelivery() receives it
   - @Valid validates the JSON
   - Calls deliveryService.createDelivery(request)

3. DeliveryService.createDelivery() does business logic
   - Generates unique deliveryId (UUID)
   - Sets status to PENDING
   - Records creation timestamp
   - Calls deliveryRepository.save(delivery)

4. DeliveryRepository.save() stores data
   - Puts delivery in ConcurrentHashMap
   - Map key = deliveryId
   - Returns the saved delivery

5. Response flows back
   - DeliveryService returns Delivery object
   - DeliveryController converts to DeliveryResponse DTO
   - Spring converts to JSON
   - HTTP 201 sent to client

6. Client receives:
   {
     deliveryId: "abc123",
     status: "PENDING",
     ...
   }
```

---

## 📊 Delivery Status Lifecycle

```
┌─────────────┐
│  PENDING    │  ← Created when order is placed
└──────┬──────┘
       │ (Delivery agent assigned)
       ↓
┌──────────────────┐
│ OUT_FOR_DELIVERY │  ← Agent is delivering
└──────┬───────────┘
       │ (Order delivered)
       ↓
┌──────────┐
│DELIVERED │  ← Order successfully received
└──────────┘

Alternative:
PENDING → CANCELLED (Order cancelled before delivery)
```

---

## ⚙️ Configuration

### Port
```yaml
server:
  port: 8084  # Change in application.yml if needed
```

### Service Name
```yaml
spring:
  application:
    name: delivery-service  # For logging, monitoring
```

### Swagger Documentation
```yaml
springdoc:
  swagger-ui:
    path: /swagger-ui.html  # Access at /swagger-ui.html
```

---

## 📋 What Was Done (Step by Step)

### ✅ Created 14 Java Classes
1. `DeliveryServiceApplication` - Entry point
2. `DeliveryController` - REST endpoints (7 endpoints)
3. `DeliveryService` - Business logic
4. `DeliveryRepository` - Data access
5. `Delivery` - Entity model
6. `DeliveryStatus` - Status enum
7. `DeliveryRequest` - Input DTO
8. `DeliveryResponse` - Output DTO
9. `DeliveryStatusResponse` - Status-only DTO
10. `UpdateDeliveryStatusRequest` - Status update DTO
11. `GlobalExceptionHandler` - Error handling
12. `DeliveryNotFoundException` - Custom exception
13. `ApiErrorResponse` - Error response model
14. `OpenApiConfig` - Swagger configuration

### ✅ Created 2 Configuration Files
1. `pom.xml` - Maven build configuration
2. `application.yml` - Spring Boot configuration

### ✅ Tested All APIs
- ✅ POST: Created delivery
- ✅ GET: Retrieved all deliveries
- ✅ GET: Retrieved single delivery
- ✅ GET: Retrieved delivery status
- ✅ PUT: Updated status
- ✅ PUT: Assigned delivery agent
- ✅ Swagger UI: Fully documented

### ✅ Build & Deployment
- ✅ Maven builds without errors
- ✅ JAR package created (26MB)
- ✅ Service starts on port 8084
- ✅ All endpoints respond correctly
- ✅ Swagger UI accessible

---

## 🔌 Integration with API Gateway

When integrated with API Gateway:

```
External Client
    ↓
API Gateway (localhost:8000)
├── /api/deliveries/** → Delivery Service (8084) ✅
├── /api/orders/** → Order Service (8082)
├── /api/payments/** → Payment Service (8085)
└── /api/users/** → User Service (8081)
```

The gateway routes requests based on URL path. The Delivery Service doesn't need to be modified - it's already ready!

---

## 🎓 Learning Path

If you're learning Spring Boot, here's what to study in this code:

### Beginner
1. Read `DeliveryServiceApplication.java` - understand entry point
2. Read `application.yml` - understand configuration
3. Read `Delivery.java` - understand data model

### Intermediate
1. Study `DeliveryController.java` - understand REST endpoints and HTTP
2. Read `DeliveryRequest.java` and `DeliveryResponse.java` - understand DTOs
3. Learn the annotations: `@RestController`, `@GetMapping`, `@PostMapping`, etc.

### Advanced
1. Study `DeliveryService.java` - understand business logic and dependency injection
2. Study `DeliveryRepository.java` - understand data access patterns
3. Read `GlobalExceptionHandler.java` - understand error handling
4. Learn dependencies: `@Service`, `@Repository`, constructor injection

### Expert
1. Study how Spring manages lifecycle
2. Understand thread safety (`ConcurrentHashMap`)
3. Learn how to convert between models and DTOs (`toResponse()`)
4. Understand validation (`@Valid`, `@NotBlank`)

---

## 📖 Documentation Files

1. **QUICK_START_GUIDE.md** - How to run and test (start here!)
2. **DELIVERY_SERVICE_EXPLAINED.md** - Complete conceptual explanation
3. **CODE_WALKTHROUGH.md** - Line-by-line code explanation
4. **README.md** - This file (overview)

---

## 🔧 Troubleshooting

### Issue: "Port 8084 already in use"
```powershell
# Find process
Get-NetTCPConnection -LocalPort 8084

# Kill it
Stop-Process -Id {PID} -Force
```

### Issue: "BUILD FAILURE"
```bash
mvn clean install -DskipTests
# Make sure Java 17+ is installed
java -version
```

### Issue: "Delivery not found" (HTTP 404)
- Check the deliveryId is correct
- Make sure delivery was created first
- Remember: data is in-memory, clears on restart

---

## 📝 What's Next?

### Phase 1: Learn (Current)
- ✅ Understand Spring Boot concepts
- ✅ See working microservice
- ✅ Test all endpoints

### Phase 2: Enhance
- Add input validation rules
- Add more business logic
- Add unit tests
- Add integration tests

### Phase 3: Persistence
- Replace in-memory storage with H2/MySQL
- Use JPA/Hibernate
- Add database migrations

### Phase 4: Integration
- Connect to API Gateway
- Add inter-service communication
- Add message queues
- Add service discovery

### Phase 5: Production
- Add authentication/authorization
- Add monitoring/logging
- Add caching
- Deploy to cloud (Azure, AWS)

---

## 📞 API Response Examples

### Success: Create Delivery (HTTP 201)
```json
{
  "deliveryId": "bdf78850-5336-4f33-9522-487c44c68549",
  "orderId": "ORD-001",
  "restaurantId": "REST-001",
  "customerId": "CUST-001",
  "deliveryAgentId": null,
  "pickupAddress": "123 Main St",
  "deliveryAddress": "456 Oak Ave",
  "status": "PENDING",
  "createdAt": "2026-03-31T12:57:55.869298",
  "updatedAt": "2026-03-31T12:57:55.869298",
  "deliveredAt": null,
  "notes": "ring doorbell"
}
```

### Success: Get Status (HTTP 200)
```json
{
  "deliveryId": "abc123",
  "orderId": "ORD-001",
  "deliveryAgentId": "AGENT-001",
  "status": "OUT_FOR_DELIVERY"
}
```

### Error: Not Found (HTTP 404)
```json
{
  "status": 404,
  "message": "Delivery not found with ID: invalid-id",
  "timestamp": "2026-03-31T13:00:00.000000"
}
```

### Error: Validation (HTTP 400)
```json
{
  "status": 400,
  "message": "Validation failed: {orderId=Order ID is required}",
  "timestamp": "2026-03-31T13:00:00.000000"
}
```

---

## ✨ Key Learnings

1. **Spring Boot abstracts complexity** - You don't configure Tomcat, JSON serialization, etc.
2. **Layered architecture** - Each layer has a specific responsibility
3. **Dependency Injection** - Spring automatically wires components together
4. **DTOs** - Decouple API contract from internal structure
5. **Exception handling** - Centralized error handling for consistent responses
6. **Microservices** - Independent service with single responsibility
7. **RESTful API** - Standard HTTP methods for CRUD operations

---

## 📞 Support

If you have questions:
1. Read the docs in this folder
2. Check **CODE_WALKTHROUGH.md** for detailed explanations
3. Look at the source code - it's well-documented
4. Swagger UI provides interactive documentation

---

## 🎉 Congratulations!

You now have a **fully functional Spring Boot microservice**!

✅ It compiles without errors
✅ It runs without errors  
✅ All endpoints are working
✅ It's documented
✅ It's ready for learning and integration

**Next Step:** Read `QUICK_START_GUIDE.md` to learn how to run it!

