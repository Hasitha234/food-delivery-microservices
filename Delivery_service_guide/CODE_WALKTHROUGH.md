# 📖 Code Walkthrough - Understanding Each Component

## Table of Contents
1. [Main Application Entry Point](#main-application-entry-point)
2. [Controller Layer](#controller-layer)
3. [Service Layer](#service-layer)
4. [Repository Layer](#repository-layer)
5. [Domain Models](#domain-models)
6. [DTOs](#dtos)
7. [Exception Handling](#exception-handling)
8. [Configuration](#configuration)

---

## Main Application Entry Point

### File: `DeliveryServiceApplication.java`

```java
package com.fooddelivery.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeliveryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryServiceApplication.class, args);
    }
}
```

**What each line does:**

```java
@SpringBootApplication
```
- Magic annotation that tells Spring Boot to:
  - Scan this package and sub-packages for components
  - Auto-configure everything it can
  - Enable component scanning, auto-configuration, property support

```java
public static void main(String[] args) {
```
- Entry point for Java application
- When you run: `java -jar delivery-service-1.0.0.jar`, this method is executed

```java
SpringApplication.run(DeliveryServiceApplication.class, args);
```
- Launches Spring Boot application
- Starts embedded Tomcat server
- Creates the Spring context and initializes all beans
- Application becomes ready to accept requests

---

## Controller Layer

### File: `DeliveryController.java`

The controller is the "API endpoint" layer. It:
1. Receives HTTP requests
2. Validates input
3. Calls service methods
4. Returns HTTP responses

```java
@RestController
@RequestMapping("/api/deliveries")
@Tag(name = "Delivery Service", description = "...")
public class DeliveryController {
    
    private final DeliveryService deliveryService;
    
    // Constructor injection (dependency injection)
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }
```

**Annotations explained:**

| Annotation | Purpose |
|------------|---------|
| `@RestController` | Makes this class handle REST API requests |
| `@RequestMapping("/api/deliveries")` | Base URL for all endpoints in this controller |
| `@Tag(...)` | Swagger documentation metadata |

### Endpoint 1: Create Delivery

```java
@PostMapping
@ResponseStatus(HttpStatus.CREATED)
@Operation(summary = "Assign delivery to order")
public DeliveryResponse createDelivery(@Valid @RequestBody DeliveryRequest request) {
    return toResponse(deliveryService.createDelivery(request));
}
```

**Breaking it down:**

```java
@PostMapping
```
- Listens for HTTP POST requests to `/api/deliveries`
- POST = create new resource

```java
@ResponseStatus(HttpStatus.CREATED)
```
- Returns HTTP 201 (Created) instead of default 200

```java
@Valid @RequestBody DeliveryRequest request
```
- `@RequestBody`: Converts JSON request body to DeliveryRequest object
- `@Valid`: Validates the request against constraints (e.g., @NotBlank)
- If validation fails, Spring automatically returns HTTP 400 with validation error

```java
return toResponse(deliveryService.createDelivery(request));
```
- Calls service to create delivery
- Converts Delivery object to DeliveryResponse DTO
- Returns JSON

### Endpoint 2: Get All Deliveries

```java
@GetMapping
@Operation(summary = "View all deliveries")
public List<DeliveryResponse> getAllDeliveries() {
    return deliveryService.getAllDeliveries().stream()
            .map(this::toResponse)
            .toList();
}
```

- `@GetMapping`: Listens for GET to `/api/deliveries`
- `.stream().map(this::toResponse).toList()`: Converts each Delivery to DeliveryResponse
- Returns list of deliveries as JSON array

### Endpoint 3: Get Single Delivery

```java
@GetMapping("/{deliveryId}")
@Operation(summary = "View single delivery")
public DeliveryResponse getDelivery(@PathVariable("deliveryId") String deliveryId) {
    return toResponse(deliveryService.getDeliveryById(deliveryId));
}
```

- `/{deliveryId}`: URL parameter - e.g., `/api/deliveries/abc123`
- `@PathVariable`: Extracts `abc123` from URL
- If delivery not found, service throws DeliveryNotFoundException
- Global exception handler catches it and returns HTTP 404

### Endpoint 4: Update Status

```java
@PutMapping("/{deliveryId}/status")
@Operation(summary = "Update delivery status")
public DeliveryResponse updateDeliveryStatus(
        @PathVariable("deliveryId") String deliveryId,
        @RequestParam("status") DeliveryStatus newStatus) {
    return toResponse(deliveryService.updateDeliveryStatus(deliveryId, newStatus));
}
```

- `@PutMapping`: HTTP PUT = update existing resource
- `@RequestParam`: Query parameter - e.g., `?status=OUT_FOR_DELIVERY`
- Calls service to update status
- Returns updated delivery

### Helper Method: Convert to Response

```java
private DeliveryResponse toResponse(Delivery delivery) {
    DeliveryResponse response = new DeliveryResponse();
    response.setDeliveryId(delivery.getDeliveryId());
    response.setOrderId(delivery.getOrderId());
    // ... set all fields
    return response;
}
```

- Converts internal Delivery object to external DeliveryResponse DTO
- Why? Keeps API contract separate from internal structure
- You can return different fields or structure to API vs internal

---

## Service Layer

### File: `DeliveryService.java`

The service layer contains **all business logic**. Controller calls service, service does the work.

```java
@Service
public class DeliveryService {
    
    private final DeliveryRepository deliveryRepository;
    
    // Constructor injection
    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }
```

`@Service`: Marks this as a Spring service bean (automatically managed by Spring)

### Method 1: Create Delivery

```java
public Delivery createDelivery(DeliveryRequest request) {
    LocalDateTime now = LocalDateTime.now();
    
    // Create new Delivery object
    Delivery delivery = new Delivery();
    delivery.setDeliveryId(UUID.randomUUID().toString());
    delivery.setOrderId(request.getOrderId());
    delivery.setRestaurantId(request.getRestaurantId());
    delivery.setCustomerId(request.getCustomerId());
    delivery.setPickupAddress(request.getPickupAddress());
    delivery.setDeliveryAddress(request.getDeliveryAddress());
    delivery.setNotes(request.getNotes());
    
    // Set default values
    delivery.setStatus(DeliveryStatus.PENDING);
    delivery.setCreatedAt(now);
    delivery.setUpdatedAt(now);
    
    // Save and return
    return deliveryRepository.save(delivery);
}
```

**Step by step:**
1. Get current time
2. Create new Delivery object
3. Copy fields from request to delivery
4. Set default status (PENDING)
5. Record timestamps
6. Save to repository
7. Return the saved delivery

### Method 2: Get Delivery by ID (with Error Handling)

```java
public Delivery getDeliveryById(String deliveryId) {
    return deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new DeliveryNotFoundException(deliveryId));
}
```

- Calls repository to find delivery
- `.orElseThrow()`: If not found, throw exception
- Exception handler catches it and returns HTTP 404

### Method 3: Update Status

```java
public Delivery updateDeliveryStatus(String deliveryId, DeliveryStatus newStatus) {
    Delivery delivery = getDeliveryById(deliveryId);  // Get existing
    delivery.setStatus(newStatus);                     // Update status
    delivery.setUpdatedAt(LocalDateTime.now());        // Update timestamp
    
    // If marked as delivered, record delivery time
    if (newStatus == DeliveryStatus.DELIVERED) {
        delivery.setDeliveredAt(LocalDateTime.now());
    }
    
    return deliveryRepository.save(delivery);          // Save changes
}
```

**Key points:**
- Business logic: only allow transitions
- Record delivery time when marked DELIVERED
- Update the "updated" timestamp

---

## Repository Layer

### File: `DeliveryRepository.java`

The repository handles **data persistence**. Currently uses in-memory storage (ConcurrentHashMap).

```java
@Repository
public class DeliveryRepository {
    
    // In-memory storage
    private final ConcurrentMap<String, Delivery> deliveries = new ConcurrentHashMap<>();
```

`@Repository`: Marks this as a data access component

`ConcurrentHashMap`: Thread-safe map (important for multi-threaded web servers)

### Method: Save (Create or Update)

```java
public Delivery save(Delivery delivery) {
    deliveries.put(delivery.getDeliveryId(), delivery);
    return delivery;
}
```

- Stores delivery in map using ID as key
- Returns the saved object

### Method: Find by ID

```java
public Optional<Delivery> findById(String deliveryId) {
    return Optional.ofNullable(deliveries.get(deliveryId));
}
```

`Optional`: Handles null safely
- If found: `Optional.of(delivery)`
- If not found: `Optional.empty()`

Service uses:
```java
.orElseThrow(() -> new DeliveryNotFoundException(...))
```

### Method: Find All

```java
public List<Delivery> findAll() {
    return List.copyOf(deliveries.values());
}
```

`List.copyOf()`: Creates immutable copy (prevents external modification)

### Future: Replace with Database

Later, replace ConcurrentHashMap with JPA:

```java
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, String> {
    // JPA provides save(), findById(), findAll() automatically!
}
```

The service layer code **doesn't change** - only repository changes!

---

## Domain Models

### File: `Delivery.java`

Represents a single delivery data structure.

```java
public class Delivery {
    
    private String deliveryId;           // Unique ID
    private String orderId;              // Foreign key to order
    private String restaurantId;         // Foreign key to restaurant
    private String customerId;           // Foreign key to customer
    private String deliveryAgentId;      // Assigned agent
    private String pickupAddress;        // Where to pick up
    private String deliveryAddress;      // Where to deliver
    private DeliveryStatus status;       // Current status
    private LocalDateTime createdAt;     // When created
    private LocalDateTime updatedAt;     // Last update
    private LocalDateTime deliveredAt;   // When delivered
    private String notes;                // Delivery notes
    
    // Constructors, getters, setters...
}
```

**Fields explained:**

| Field | Type | Purpose |
|-------|------|---------|
| `deliveryId` | String | Unique ID (generated as UUID) |
| `orderId` | String | Links to Order service |
| `restaurantId` | String | Links to Restaurant service |
| `customerId` | String | Links to Customer service |
| `deliveryAgentId` | String | Assigned driver/agent |
| `pickupAddress` | String | Restaurant address |
| `deliveryAddress` | String | Customer's address |
| `status` | Enum | Current state |
| `createdAt` | LocalDateTime | Creation time |
| `updatedAt` | LocalDateTime | Last modified time |
| `deliveredAt` | LocalDateTime | Completion time |
| `notes` | String | Special instructions |

### File: `DeliveryStatus.java`

Enum (enumeration) of valid statuses:

```java
public enum DeliveryStatus {
    PENDING,              // Just created, waiting to be assigned
    OUT_FOR_DELIVERY,     // Agent is delivering
    DELIVERED,            // Successfully delivered
    CANCELLED             // Cancelled order
}
```

**Why enum?** Prevents invalid values:
```java
delivery.setStatus(DeliveryStatus.PENDING);  // ✅ Valid
delivery.setStatus("PENDING");               // ❌ Would fail (type mismatch)
delivery.setStatus("INVALID");               // ❌ Compile error
```

---

## DTOs

### File: `DeliveryRequest.java`

Input data transfer object. Received from client in POST request.

```java
public class DeliveryRequest {
    
    @NotBlank(message = "Order ID is required")
    private String orderId;
    
    @NotBlank(message = "Restaurant ID is required")
    private String restaurantId;
    
    // ... more fields
}
```

**Annotations:**
- `@NotBlank`: Field cannot be empty
- `message`: Error message if validation fails

**Why separate?** 
- API doesn't expose all fields (e.g., `deliveryId`, `createdAt`)
- Client can only send what makes sense
- Server generates ID and timestamps

### File: `DeliveryResponse.java`

Output data transfer object. Sent back to client in response body.

```java
public class DeliveryResponse {
    private String deliveryId;
    private String orderId;
    // ... all fields, including generated ones
}
```

**Why separate?**
- API returns complete delivery info
- Decouples API response format from internal structure
- Can return different formats to different clients

### File: `DeliveryStatusResponse.java`

Minimal response with only status info.

```java
public class DeliveryStatusResponse {
    private String deliveryId;
    private String orderId;
    private String deliveryAgentId;
    private DeliveryStatus status;
}
```

**Why?** When client only needs status, don't send full delivery object. Faster, smaller response.

---

## Exception Handling

### File: `DeliveryNotFoundException.java`

Custom exception for when delivery not found:

```java
public class DeliveryNotFoundException extends RuntimeException {
    
    private final String deliveryId;
    
    public DeliveryNotFoundException(String deliveryId) {
        super("Delivery not found with ID: " + deliveryId);
        this.deliveryId = deliveryId;
    }
    
    public String getDeliveryId() {
        return deliveryId;
    }
}
```

**Why custom exception?** 
- Specific error handling
- Can catch this specific type
- Carries additional info (deliveryId)

### File: `GlobalExceptionHandler.java`

Catches all exceptions and returns proper HTTP responses:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DeliveryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleDeliveryNotFound(DeliveryNotFoundException ex) {
        return new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
    }
```

**Flow:**
```
DeliveryNotFoundException thrown
        ↓
GlobalExceptionHandler catches it
        ↓
@ExceptionHandler method called
        ↓
Returns HTTP 404 with error JSON
        ↓
Client receives error response
```

**Other handlers:**

```java
@ExceptionHandler(MethodArgumentNotValidException.class)
// Handles validation errors (HTTP 400)

@ExceptionHandler(Exception.class)
// Catches any other exception (HTTP 500)
```

---

## Configuration

### File: `OpenApiConfig.java`

Configures Swagger/OpenAPI documentation:

```java
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Delivery Service API")
                        .version("1.0.0")
                        .description("REST API for managing food order deliveries")
                        .contact(new Contact()
                                .name("Food Delivery Team")
                                .email("support@fooddelivery.com")));
    }
}
```

**What it does:**
- Creates custom OpenAPI bean
- Sets title, version, description
- Makes Swagger UI pretty and documented
- Accessible at `/swagger-ui.html`

### File: `application.yml`

Application configuration:

```yaml
server:
  port: 8084                    # Tomcat port
  
spring:
  application:
    name: delivery-service      # Service name
    
springdoc:
  swagger-ui:
    path: /swagger-ui.html      # Swagger endpoint
```

**What it configs:**
- Server port (port 8084)
- Spring application name (for logs, monitoring)
- Swagger path

---

## Dependency Injection Flow

How Spring "wires" everything together:

```
1. Spring scans all classes
   ├── Finds @SpringBootApplication
   ├── Finds @RestController (DeliveryController)
   ├── Finds @Service (DeliveryService)
   └── Finds @Repository (DeliveryRepository)

2. Creates instances (beans)
   ├── Creates DeliveryRepository instance
   ├── Creates DeliveryService instance
   │   └── Looks at constructor: needs DeliveryRepository
   │   └── Spring: "I have that!" → injects it
   └── Creates DeliveryController instance
       └── Looks at constructor: needs DeliveryService
       └── Spring: "I have that!" → injects it

3. Result:
   DeliveryController -uses-> DeliveryService -uses-> DeliveryRepository
   
   All wired automatically!
```

**Without Spring Bot (manual):**
```java
DeliveryRepository repo = new DeliveryRepository();
DeliveryService service = new DeliveryService(repo);
DeliveryController ctrl = new DeliveryController(service);
// Have to do this for every component!
```

**With Spring Boot:**
```java
// Spring does it automatically!
@Autowired
private DeliveryService service;  // Already wired!
```

---

## Request/Response Flow Diagram

```
Client sends HTTP request
        ↓
@RestController receives request
        ↓
@PathVariable, @RequestBody, @RequestParam extracted
        ↓
@Valid validation performed
        ↓
Handler method called (e.g., createDelivery)
        ↓
Call DeliveryService method
        ↓
DeliveryService calls DeliveryRepository methods
        ↓
DeliveryRepository returns data / throws exception
        ↓
DeliveryService returns result / throws exception
        ↓
Controller converts to DTO (toResponse method)
        ↓
Spring converts to JSON
        ↓
HTTP 200/201 with JSON body sent
        ↓
Client receives response

OR if exception:

Exception thrown (e.g., DeliveryNotFoundException)
        ↓
GlobalExceptionHandler catches it
        ↓
Returns HttpStatus + error message
        ↓
Spring converts to JSON
        ↓
HTTP 404 with error JSON sent
        ↓
Client receives error response
```

---

## Summary: How All Pieces Work Together

```
API Request
    ↓
┌─────────────────────────────────┐
│   DeliveryController            │  ← Handles HTTP
│   - @GetMapping /api/deliveries │
│   - @PostMapping /api/deliveries├────────┐
└─────────────────────────────────┘        │
                                           ↓
┌─────────────────────────────────┐    Calls
│   DeliveryService              │
│   - createDelivery()            │  ← Business Logic
│   - getDeliveryById()           │
│   - updateDeliveryStatus()      ├────────┐
└─────────────────────────────────┘        │
                                           ↓
┌─────────────────────────────────┐    Uses
│   DeliveryRepository            │
│   - save()                      │  ← Data Access
│   - findById()                  │
│   - findAll()             ├─────→ ConcurrentHashMap (In-memory)
└─────────────────────────────────┘

All error handling via GlobalExceptionHandler
All docs via Swagger UI
All config via application.yml
```

---

## What to Modify

### Add a new field to Delivery:
1. Add to `Delivery.java` domain
2. Add to `DeliveryRequest.java` DTO (input)
3. Add to `DeliveryResponse.java` DTO (output)
4. Update `toResponse()` method in controller
5. Rebuild and test

### Add a new endpoint:
1. Add method to `DeliveryController.java`
2. Use `@GetMapping`, `@PostMapping`, etc.
3. Call appropriate service method
4. Return response DTO
5. Exception handler catches errors automatically

### Add validation:
1. Add `@NotBlank`, `@NotNull`, etc. to DTO
2. Use `@Valid` in controller method
3. Spring handles the rest!

