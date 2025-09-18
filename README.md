# 🛒 Spring Boot Project: E-Commerce Backend Application

This is a **Spring Boot (Java)** based backend project designed as an **E-Commerce website**.

---

## 🛍 Project Features

- **User Module**
    - Browse products
    - Add items to cart
    - Proceed to checkout

- **Seller Module**
    - Manage products (add, edit, delete listings)

- **Authentication**
    - Login and Signup
    - OTP-based Gmail verification *(available on `otp-based` branch)*

- **Database Management**
    - Uses **MySQL** for persistent storage (users, products, orders, cart)

- **Caching**
    - **Main Branch** → Configured with **Redis Cluster**
    - **otp-based Branch** → Uses **Standalone Redis** for OTP verification

---

## 🚀 Prerequisites

Ensure the following are installed before running the project:

- **Java 17+**
- **Maven 3.8+**
- **MySQL → running locally or accessible remotely**
- **Redis → either standalone or cluster setup**
- **WSL (Windows Subsystem for Linux) → required only if you are on Windows and want to run Redis inside WSL**

## ⚙️ Setup Instructions

1. ```Clone the Repository
   git clone <your-repo-url>
   cd <your-project-folder>

2. Select a Git Branch

- For main branch (Redis Cluster + MySQL):
```
    - git checkout main
```
- For otp-based branch (OTP-based Gmail verification with standalone Redis):

```    
    - git checkout otp-based
```

3. Configure MySQL Database

Create a new database in MySQL (example: spring_app).
Update application.properties or application.yml with your DB credentials:
```
    spring.datasource.url=jdbc:mysql://localhost:3306/spring_app
    spring.datasource.username=root
    spring.datasource.password=yourpassword
    spring.jpa.hibernate.ddl-auto=update
```

4. Configure Redis

### 🛠 Running Redis on WSL (for Windows Users)
1. Install Redis
```
   sudo apt update
   sudo apt install redis-server
```
2. Start Redis Server
```
   sudo service redis-server start
```
3. Verify Redis is Running
```
   redis-cli ping
```

Expected response:
PONG


ℹ️ For Redis Cluster, multiple Redis instances must be started and clustered.
For Standalone Redis, only one instance is required.

▶️ Running the Project

From the project’s root directory, run:
```
    mvn clean install
    mvn spring-boot:run
```

The backend will now be available at:
http://localhost:8080
