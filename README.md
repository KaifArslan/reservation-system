# 🍽️ Restaurant Reservation System (Spring Boot + PostgreSQL)
A full-stack **Restaurant Reservation System** built with **Spring Boot**, **PostgreSQL**, and a simple **HTML/CSS/JavaScript frontend**.
The project allows restaurants to manage reservations, check availability, and handle bookings through authenticated admin panels.


## 🚀 Features

### Core Functionality
- **Real-time Availability Checking** - Dynamic time slot generation based on existing reservations and table capacity
- **Flexible Booking System** - Customers can choose any available time (15-minute intervals), not limited to fixed slots
- **Multi-table Management** - Handles overlapping reservations across multiple tables
- **Business Rules Validation** - Operating hours, party size limits, and reservation duration enforcement

### Authentication & Security
- **JWT-based Authentication** - Stateless token authentication with 24-hour expiration
- **Role-based Access Control** - Secure admin endpoints with Spring Security
- **BCrypt Password Hashing** - One-way password encryption for secure storage

### Frontend (Static HTML + JS)

## Admin Dashboard

- **Reservation Management** - View, create, and cancel reservations, today's bookings, available time slots.
<img width="1919" height="1016" alt="Screenshot 2025-12-10 172553" src="https://github.com/user-attachments/assets/2631e865-ae3b-4895-93ea-432f15adc546" />
<img width="1919" height="907" alt="Screenshot 2025-12-10 172630" src="https://github.com/user-attachments/assets/54ebc02b-23ef-4110-82c8-04822155e4e8" />
<img width="1919" height="914" alt="Screenshot 2025-12-10 172753" src="https://github.com/user-attachments/assets/09a4b5fc-f2b4-461b-b89f-08dec1e4a0a7" />
<img width="1919" height="910" alt="Screenshot 2025-12-10 172727" src="https://github.com/user-attachments/assets/68df1159-1236-4778-95ee-cef35c589bd2" />

- **Restaurant Profile Management** - Update operating hours, table count, and settings
- **Responsive UI** - Clean, modern interface with dark mode support

## Common Reservation Page for Multiple Restaurant
<img width="1919" height="903" alt="Screenshot 2025-12-10 204139" src="https://github.com/user-attachments/assets/47d60672-fc29-47ef-a5eb-efdf696d1078" />
<img width="1917" height="904" alt="Screenshot 2025-12-10 204233" src="https://github.com/user-attachments/assets/ec7cc955-c4a5-4911-b7ac-383f22dc148e" />
<img width="1321" height="688" alt="Screenshot 2025-12-10 204511" src="https://github.com/user-attachments/assets/7d50e3f2-b927-4df3-9c9e-68337a4b5208" />

- Restaurant can have their own landing page and add a link with their Id to the common reservation page the server is providing. 
- ~ http://localhost:8080/reserve.html?restaurantId= 'your restaurant Id'.

## Login And Registration
For accessing Restaurant Admin Page, User need to Login or Register. 
<img width="1919" height="907" alt="Screenshot 2025-12-10 180131" src="https://github.com/user-attachments/assets/d6e4d22a-2d3d-4ffb-a408-2d68b64a5fc7" />
<img width="1919" height="915" alt="Screenshot 2025-12-10 180146" src="https://github.com/user-attachments/assets/9e639886-3227-4ec4-9234-b09b7b5389c5" />

---


## 🛠️ Tech Stack

**Backend**

* Java
* Spring Boot
* Spring Data JPA (Hibernate)
* Spring Security + JWT
* PostgreSQL

**Frontend**

* HTML
* CSS
* JavaScript


## 🔗 API Endpoints

### 🔐 Authentication (`/auth`)

| Method | Endpoint         | Description                             | Auth Required |
| ------ | ---------------- | --------------------------------------- | ------------- |
| POST   | `/auth/login`    | Login restaurant user and get JWT token | ❌             |
| POST   | `/auth/register` | Register new restaurant user            | ❌             |

**Login Request (JSON)**

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Login Response (JSON)**

```json
{
  "token": "jwt-token-here",
  "restaurantId": 1
}
```

---

### 🏬 Restaurants (`/restaurants`)

| Method | Endpoint            | Description             | Auth Required |
| ------ | ------------------- | ----------------------- | ------------- |
| GET    | `/restaurants`      | Get all restaurants     | ✅             |
| POST   | `/restaurants`      | Create new restaurant   | ✅             |
| GET    | `/restaurants/{id}` | Get restaurant by ID    | ✅             |
| PUT    | `/restaurants/{id}` | Update restaurant by ID | ✅             |
| DELETE | `/restaurants/{id}` | Delete restaurant by ID | ✅             |

---

### 📅 Reservations (`/reservations`)

| Method | Endpoint                                  | Description                       | Auth Required |
| ------ | ----------------------------------------- | --------------------------------- | ------------- |
| POST   | `/reservations`                           | Create reservation                | ❌             |
| GET    | `/reservations/{id}`                      | Get reservation by ID             | ✅             |
| GET    | `/reservations`                           | Get all reservations              | ✅             |
| PUT    | `/reservations/{id}`                      | Update reservation                | ✅             |
| DELETE | `/reservations/{id}`                      | Delete reservation                | ✅             |
| GET    | `/reservations/restaurant/{restaurantId}` | Get reservations by restaurant    | ✅             |
| GET    | `/reservations/availability`              | Check time slot availability      | ✅             |
| GET    | `/reservations/available-slots`           | Get available time slots for date | ❌             |
| PATCH  | `/reservations/{id}/cancel`               | Cancel a reservation              | ✅             |

---

### 🧪 Example Query Params

**Check Availability**

```
/reservations/availability?restaurantId=1&date=2025-01-12&time=18:30
```

**Get Available Slots**

```
/reservations/available-slots?restaurantId=1&date=2025-01-12
```

---

### 🔐 Authorization Header (for protected routes)

For protected routes, send JWT in header:

```
Authorization: Bearer <your_token_here>
```

---

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/restaurant-reservation-system.git
cd restaurant-reservation-system
```

### 2. Setup PostgreSQL

Create a database:

```sql
CREATE DATABASE reservation_system;
```

Update your `application.properties`:

```properties
spring.application.name=reservation-system

spring.datasource.url=jdbc:postgresql://localhost:5432/reservation_system
spring.datasource.username=postgres
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Run the Project

Using IntelliJ:

* Open the project
* Run `ReservationSystemApplication.java`

Or via terminal:

```bash
./mvnw spring-boot:run
```

---

## 🔐 Authentication (JWT)

The app uses **JWT authentication**.

### Login Endpoint

```http
POST /auth/login
```

Stores JWT token in browser:

```js
localStorage.setItem("jwtToken", token);
```

All protected routes require:

```
Authorization: Bearer <your_token>
```

---

## 🌐 Frontend Pages

| Page                   | URL                                                           |
| ---------------------- | ------------------------------------------------------------- |
| Restaurant Admin Panel | `http://localhost:8080/restaurant-admin.html?restaurantId=(id no here)` |
| Common Reservation Page | `http://localhost:8080/reserve.html`                 |
| Login page             | `http://localhost:8080/login.html` |
| Registration Page     |`http://localhost:8080/register.html`|

---
 
## 💡 Future Improvements

* Role-based access control
* Multiple branch management
* Payment integration
* Real-time availability (WebSockets)
