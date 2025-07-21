# practice-spring-boot-feb-2025

## 🚀 Spring Boot Practice Project

This repository contains a Spring Boot project with PostgreSQL integration. Below are important details regarding common errors and solutions, as well as PostgreSQL connection setup using SQL Developer.

---

## 🔧 Fix for "null value in column 'id' violates not-null constraint" Error

If you encounter the following error while running your Spring Boot application:

```
null value in column 'id' violates not-null constraint
```

It means that the `id` column in your PostgreSQL `users` table is not set to auto-generate values.

### 🛠 Solution
Run the following SQL command in your PostgreSQL database to make the `id` column auto-increment:

```sql
ALTER TABLE users ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY;
```

---

## ✅ How to Connect PostgreSQL in SQL Developer

SQL Developer is mainly designed for Oracle databases, but you can connect to PostgreSQL using a JDBC driver.

### Steps to Connect:

1️⃣ **Download PostgreSQL JDBC Driver**
- Visit: [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/)
- Download the latest `.jar` file.

2️⃣ **Configure SQL Developer**
- Open SQL Developer.
- Go to **Tools** → **Preferences**.
- Expand **Database** → Click **Third Party JDBC Drivers**.
- Click **Add Entry** → Select the downloaded PostgreSQL JDBC `.jar` file → Click **OK**.

3️⃣ **Create a New Connection**
- Click **New Connection**.
- Enter a Connection Name (e.g., `PostgreSQL_DB`).
- Choose **Database Type** (select `PostgreSQL`).
- Set **Username** and **Password** (as per your PostgreSQL setup).
- **Hostname**: `localhost` (or your database host).
- **Port**: `5432` (default for PostgreSQL).
- Choose the appropriate database.
- Click **Test** to verify the connection.

---

### 💡 Need Help?
If you run into any issues, feel free to open an issue or contribute by submitting a pull request!

Happy Coding! 🚀

