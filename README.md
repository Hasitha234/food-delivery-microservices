# Food Delivery Microservices
### IT4020 - Modern Topics in IT | Assignment 2

## Group Members
| Name | Service |
|------|---------|
| Hasitha | User Service |
| Manohara | Restaurant Service |
| Dinuri | Menu Service |
| Hasini | Order Service |
| Shenal | Payment Service |
| Dulaksha | Delivery Service |

## Project Structure
```text
food-delivery-microservices/
├── api-gateway/
├── user-service/
├── restaurant-service/
├── menu-service/
├── order-service/
├── payment-service/
└── delivery-service/
```

## Completed Coding Part
This repository currently includes:

- `payment-service` implemented with Spring Boot 3 and Java 17
- `api-gateway` configured with Spring Cloud Gateway
- Swagger/OpenAPI enabled for direct service access and gateway access

## Payment Service Features
- Make payment
- View payment by ID
- Check payment status
- View all payments for demo/testing

## Ports
- API Gateway: `8080`
- Payment Service: `8085`

Using the API Gateway avoids exposing separate backend ports to frontend clients. The client can call the gateway on port `8080`, and the gateway routes the request internally to the correct microservice.

## Run the Project
Open two terminals from the project root.

### 1. Start Payment Service
```bash
mvn -pl payment-service spring-boot:run
```

### 2. Start API Gateway
```bash
mvn -pl api-gateway spring-boot:run
```

## Payment API Endpoints
### Direct payment-service access
- Swagger UI: `http://localhost:8085/swagger-ui.html`
- OpenAPI docs: `http://localhost:8085/v3/api-docs`
- Make payment: `POST http://localhost:8085/api/payments`
- Check payment status: `GET http://localhost:8085/api/payments/status/{paymentId}`

### Access through API Gateway
- Gateway Swagger UI: `http://localhost:8080/swagger-ui.html`
- Payment Swagger docs via gateway: `http://localhost:8080/payment-service/v3/api-docs`
- Payment Swagger UI via gateway: `http://localhost:8080/payment-service/swagger-ui.html`
- Make payment via gateway: `POST http://localhost:8080/payment-service/api/payments`
- Check payment status via gateway: `GET http://localhost:8080/payment-service/api/payments/status/{paymentId}`

## Sample Payment Request
```json
{
  "orderId": "ORDER-1001",
  "userId": "USER-2001",
  "amount": 2500.00,
  "currency": "LKR",
  "paymentMethod": "CARD"
}
```

## Screenshot Checklist for the Assignment
- Payment Service Swagger UI opened directly
- Payment Service Swagger/OpenAPI through API Gateway
- `POST /api/payments` response
- `GET /api/payments/status/{paymentId}` response

## Tech Stack
- Java 17
- Spring Boot 3
- Spring Cloud Gateway
- Swagger / OpenAPI 3
- Maven
