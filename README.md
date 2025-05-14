# 🛒 Spring Boot E-Commerce Backend

## 📌 Overview
This is a production-ready e-commerce backend built with **Spring Boot 3**, following clean architecture principles. It handles core features such as product management, user carts, checkout, and order placement.

The project is fully integrated with:
- **JPA (Hibernate)** for ORM
- **MySQL/PostgreSQL** for persistence
- **Swagger (Springdoc OpenAPI)** for API documentation
- **Stripe**-ready structure for future payment gateway integration

> 🔗 **Inspired by:** [EmbarkX Spring Boot Course - sb-ecom](https://github.com/EmbarkXOfficial/spring-boot-course/tree/main/sb-ecom)

---

## 📚 Table of Contents

1. [Getting Started](#getting-started)
2. [API Documentation](#api-documentation)
3. [Main Features](#main-features)
4. [Project Structure](#project-structure)
5. [Limitations](#limitations)
6. [Future Improvements](#future-improvements)

---

## 🚀 Getting Started

### ✅ Prerequisites:
- Java 17+
- Maven
- MySQL/PostgreSQL (local or containerized)

### 📦 Run Locally
```bash
mvn spring-boot:run
```

OR package the app:
```bash
mvn clean install
java -jar target/spring-ecom.jar
```

---

## 🔍 API Documentation

You can explore all REST APIs:
- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI Spec: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## ✨ Main Features

- ✅ **Product Module**
    - CRUD operations with category linkage
    - Image upload support

- ✅ **User Cart**
    - Add, update, remove items
    - Cart auto-syncs with product updates

- ✅ **Order Module**
    - Checkout from cart
    - Payment details stored (Stripe-ready)
    - Inventory automatically updated

- ✅ **Address Management**
    - Users can add/manage addresses

- ✅ **Validation & Error Handling**
    - Global exception handler with clear messages
    - DTO validation using annotations

- ✅ **Swagger UI**
    - Test all endpoints easily

---

## 🗂️ Project Structure
```
com.ecommerce.project
├── config            # OpenAPI, CORS, etc.
├── controller        # REST endpoints
├── dto / payload     # Request/Response models
├── event             # Event-based cart sync logic
├── exception         # Custom exceptions
├── model             # Entities: Product, Order, Cart, etc.
├── repository        # JPA repositories
├── service           # Business logic
├── util              # Helper classes (e.g. AuthUtils)
```

---

## ⚠️ Limitations

- ❌ Stripe payment is **not integrated**, only placeholders exist
- ❌ Admin panel not implemented
- ❌ No email notifications for order confirmations
- ❌ Unit and integration tests not fully written yet

---

## 🔮 Future Improvements

- 💳 Fully integrate Stripe and/or PayPal payments
- 🛎️ Notification system for order and shipment
- 🧪 Add JUnit tests with MockMvc for key endpoints
- 📦 Dockerize the backend and connect to PostgreSQL container

---

> Built with ❤️ using Spring Boot


