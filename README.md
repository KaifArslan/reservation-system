Here’s a clean **`README.md`** you can directly copy into your GitHub repo for your **Restaurant Reservation System** project:

---

# 🍽️ Restaurant Reservation System (Spring Boot + PostgreSQL)

A full-stack **Restaurant Reservation System** built with **Spring Boot**, **PostgreSQL**, and a simple **HTML/CSS/JavaScript frontend**.
The project allows restaurants to manage reservations, check availability, and handle bookings through authenticated admin panels.

---

## 🚀 Features

### Backend (Spring Boot)

* Create, update, delete **restaurants**
* Create, update, cancel **reservations**
* Slot-based table availability system
* JWT-based authentication for restaurant admins
* RESTful APIs

### Frontend (Static HTML + JS)

* Landing page for restaurants
* Admin dashboard:

  * Today’s reservations
  * All reservations
  * Restaurant profile update
  * Slot availability view
* Light/Dark theme toggle

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
* Vanilla JavaScript

---

Here’s a clean **“API Endpoints”** section you can directly paste into your GitHub **README.md**.
It includes your **AuthController**, **RestaurantController**, and **ReservationController**.

---

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

## 📡 API Endpoints

### Reservations

| Method | Endpoint                        | Description              |
| ------ | ------------------------------- | ------------------------ |
| GET    | `/reservations`                 | Get all reservations     |
| GET    | `/reservations/{id}`            | Get reservation by ID    |
| POST   | `/reservations`                 | Create reservation       |
| PUT    | `/reservations/{id}`            | Update reservation       |
| PATCH  | `/reservations/{id}/cancel`     | Cancel reservation       |
| GET    | `/reservations/availability`    | Check slot availability  |
| GET    | `/reservations/available-slots` | Get available time slots |

### Restaurants

| Method | Endpoint            | Description       |
| ------ | ------------------- | ----------------- |
| GET    | `/restaurants/{id}` | Get restaurant    |
| POST   | `/restaurants`      | Create restaurant |
| PUT    | `/restaurants/{id}` | Update restaurant |

---

## 🧪 Sample Testing

You can test the APIs using:

* VS Code HTTP client
* Postman
* Browser fetch()

---

## 📸 Screenshots

 

---

## 💡 Future Improvements

* Role-based access control
* Multiple branch management
* Payment integration
* Real-time availability (WebSockets)
