# Rabobank - Power of Attorney API

This project is a backend service built with **Java 17** and **Spring Boot 3**, developed as part of the **Rabobank Authorizations Assignment**. It provides a REST API to manage **Power of Attorney (POA)** relationships between account holders.

## 🧩 Project Structure

The project consists of the following modules:

- **API**  
  Responsible for exposing the REST endpoints and connecting to the domain and data layers.

- **Domain**  
  Contains the core business logic and domain models such as `Account`, `Authorization`, and `PowerOfAttorney`.

- **Data**  
  Handles data persistence using **embedded MongoDB**. Repositories and data access logic are implemented here.

---

## ✅ Features

- Create a **Power of Attorney** for a grantee to access a grantor's account.
- Retrieve all **accessible accounts** (read or write) for a given grantee.
- Use **Swagger UI** for easy API testing and documentation.
- Postman collection provided for quick testing.

---

## 🚀 Technologies Used

- **Java 17**
- **Spring Boot 3**
- **Embedded MongoDB**
- **Lombok**
- **Spring Validation**
- **Swagger**
- **Postman**

---

### Prerequisites
- Java 17 JDK
- Maven
- Docker (for running MongoDB)

## 📦 Getting Started

### Installation
1.  **Clone the repository:**
```bash
git clone [https://github.com/didemkacmazdemir/PoAManager.git]
cd PoAManager
```

2.  **Build Project:**
```bash
mvn clean install
```

3.  **Start the application and embeded mongo db containers:**

```bash
docker compose up --build
  ```
4.  **Insert Data:**

```bash
curl -X POST \
http://localhost:8080/api/v1/power-of-attorney \
-H 'Content-Type: application/json' \
-d '{
"grantorName": "John Doe",
"granteeName": "didem",
"authorization": "WRITE",
"accountNumber": "NL91RABO0123456789"
}'

```

5.  **Read Data:**

```bash
curl -X GET http://localhost:8080/api/v1/accounts/didem
```
