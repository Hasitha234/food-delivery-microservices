# 🚀 Quick Start Guide - How to Run Delivery Service

## Prerequisites
- Java 17+ installed
- Maven installed
- Windows PowerShell or Command Prompt

## Step 1: Build the Project

```powershell
cd "C:\Users\dulaksha\OneDrive\Desktop\food-delivery-microservices"
mvn clean install -DskipTests
```

**What happens:**
✓ Downloads all dependencies
✓ Compiles all Java code
✓ Creates JAR files in target/ folders
✓ Installs dependencies locally

**Success message:** `BUILD SUCCESS`

---

## Step 2: Run the Delivery Service

```powershell
java -jar "C:\Users\dulaksha\OneDrive\Desktop\food-delivery-microservices\delivery-service\target\delivery-service-1.0.0.jar"
```

**What you should see:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/

 :: Spring Boot ::                (v3.3.5)

2026-03-31T12:57:28.404+05:30 INFO c.f.delivery.DeliveryServiceApplication  : Started DeliveryServiceApplication in 3.53 seconds
```

✅ **Service is now running on http://localhost:8084**

---

## Step 3: Test the API

### Option A: Use Swagger UI (Easy - No Code!)

Open your browser and go to:
```
http://localhost:8084/swagger-ui.html
```

You'll see all the endpoints. Click "Try it out" to test them!

---

### Option B: Use PowerShell Commands

#### Test 1: Create a Delivery

```powershell
$headers = @{"Content-Type"="application/json"}
$body = @{
    orderId="ORD-001"
    restaurantId="REST-001"
    customerId="CUST-001"
    pickupAddress="123 Main St"
    deliveryAddress="456 Oak Ave"
    notes="Deliver after 5 PM"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8084/api/deliveries" `
  -Method POST `
  -Headers $headers `
  -Body $body `
  -UseBasicParsing | Select-Object -ExpandProperty Content | ConvertFrom-Json
```

**Save the deliveryId from response** - you'll need it for other tests!

---

#### Test 2: Get All Deliveries

```powershell
Invoke-WebRequest -Uri "http://localhost:8084/api/deliveries" `
  -UseBasicParsing | Select-Object -ExpandProperty Content | ConvertFrom-Json
```

---

#### Test 3: Get Single Delivery

```powershell
#Replace with your deliveryId from Test 1
$deliveryId = "your-delivery-id-here"

Invoke-WebRequest -Uri "http://localhost:8084/api/deliveries/$deliveryId" `
  -UseBasicParsing | Select-Object -ExpandProperty Content | ConvertFrom-Json
```

---

#### Test 4: Check Delivery Status

```powershell
$deliveryId = "your-delivery-id-here"

Invoke-WebRequest -Uri "http://localhost:8084/api/deliveries/$deliveryId/status" `
  -UseBasicParsing | Select-Object -ExpandProperty Content | ConvertFrom-Json
```

---

#### Test 5: Update Delivery Status to OUT_FOR_DELIVERY

```powershell
$deliveryId = "your-delivery-id-here"

Invoke-WebRequest -Uri "http://localhost:8084/api/deliveries/$deliveryId/status?status=OUT_FOR_DELIVERY" `
  -Method PUT `
  -UseBasicParsing | Select-Object -ExpandProperty Content | ConvertFrom-Json
```

---

#### Test 6: Update Delivery Status to DELIVERED

```powershell
$deliveryId = "your-delivery-id-here"

Invoke-WebRequest -Uri "http://localhost:8084/api/deliveries/$deliveryId/status?status=DELIVERED" `
  -Method PUT `
  -UseBasicParsing | Select-Object -ExpandProperty Content | ConvertFrom-Json
```

---

#### Test 7: Assign Delivery Agent

```powershell
$deliveryId = "your-delivery-id-here"

Invoke-WebRequest -Uri "http://localhost:8084/api/deliveries/$deliveryId/assign-agent?agentId=AGENT-001" `
  -Method PUT `
  -UseBasicParsing | Select-Object -ExpandProperty Content | ConvertFrom-Json
```

---

### Option C: Use curl (If you have curl installed)

```bash
# Create delivery
curl -X POST http://localhost:8084/api/deliveries \
  -H "Content-Type: application/json" \
  -d '{"orderId":"ORD-001","restaurantId":"REST-001","customerId":"CUST-001","pickupAddress":"123 Main St","deliveryAddress":"456 Oak Ave"}'

# Get all deliveries
curl http://localhost:8084/api/deliveries

# Get single delivery
curl http://localhost:8084/api/deliveries/{deliveryId}

# Update status
curl -X PUT "http://localhost:8084/api/deliveries/{deliveryId}/status?status=OUT_FOR_DELIVERY"
```

---

## API Endpoints Reference

| Method | Endpoint | Purpose | Status |
|--------|----------|---------|--------|
| POST | `/api/deliveries` | Create delivery | ✅ Working |
| GET | `/api/deliveries` | Get all deliveries | ✅ Working |
| GET | `/api/deliveries/{id}` | Get single delivery | ✅ Working |
| GET | `/api/deliveries/by-order/{orderId}` | Get deliveries for order | ✅ Working |
| GET | `/api/deliveries/{id}/status` | Get delivery status | ✅ Working |
| PUT | `/api/deliveries/{id}/status?status=STATUS` | Update status | ✅ Working |
| PUT | `/api/deliveries/{id}/assign-agent?agentId=ID` | Assign agent | ✅ Working |

---

## Expected Responses

### Success Response (HTTP 200/201)
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
    "notes": "Deliver after 5 PM"
}
```

### Error Response (HTTP 404)
```json
{
    "status": 404,
    "message": "Delivery not found with ID: invalid-id",
    "timestamp": "2026-03-31T12:59:00.000000"
}
```

### Error Response (HTTP 400)
```json
{
    "status": 400,
    "message": "Validation failed: {fieldName=error message}",
    "timestamp": "2026-03-31T12:59:00.000000"
}
```

---

## Troubleshooting

### Port 8084 already in use
```powershell
# Find process using port 8084
Get-NetTCPConnection -LocalPort 8084

# Kill the process (replace PID)
Stop-Process -Id {PID} -Force
```

### Build fails
```powershell
# Clean and rebuild
mvn clean
mvn install -DskipTests
```

### JAR not found
Make sure you're in the correct directory and the build was successful:
```powershell
dir delivery-service/target/*.jar
```

### Service won't start
Check Java version:
```powershell
java -version
# Should show Java 17 or higher
```

---

## File Structure Created

```
food-delivery-microservices/
├── pom.xml (parent - defines version, dependencies)
├── delivery-service/
│   ├── pom.xml (service-specific config)
│   ├── target/
│   │   └── delivery-service-1.0.0.jar (executable - run this!)
│   └── src/
│       ├── main/
│       │   ├── java/com/fooddelivery/delivery/
│       │   │   ├── DeliveryServiceApplication.java
│       │   │   ├── controller/
│       │   │   ├── service/
│       │   │   ├── repository/
│       │   │   ├── domain/
│       │   │   ├── dto/
│       │   │   ├── exception/
│       │   │   └── config/
│       │   └── resources/
│       │       └── application.yml
```

---

## What's Running?

When the service is running:

✅ **Tomcat Web Server** on port 8084
✅ **Spring Context** with all beans
✅ **REST API** ready for requests
✅ **Swagger UI** for documentation
✅ **In-memory database** (ConcurrentHashMap) storing data

All in a simple `.jar` file!

---

## Stop the Service

In the terminal where service is running:
- Press `Ctrl + C` to stop

Data is cleared (it's in-memory). When you start again, everything is fresh.

---

## Next Steps

1. ✅ Run the service
2. ✅ Test all endpoints
3. 📖 Read the detailed explanation in `DELIVERY_SERVICE_EXPLAINED.md`
4. 🔧 Modify the code to add more features
5. 💾 Add database persistence (later)
6. 🌐 Connect to API Gateway (later)

