# Pharmacy Management System
 
**Stack:** Java 17 + Spring Boot 3.3.2 + Spring Security + Spring Data JPA + Thymeleaf + MySQL
 
**Live Demo:** https://pharmacy-management-system-production-a6a5.up.railway.app/login
 
## Features
 
- **Authentication** — Spring Security, DB-backed login, BCrypt-hashed passwords, role-based access (ADMIN / PHARMACIST)
- **Medicine Management** — list, search, add, update, delete, low-stock badges
- **Supplier Management** — list, add, delete
- **Purchase (Stock In)** — select supplier + medicines + quantities, auto-increments stock
- **Sale (Checkout)** — cart-style checkout, auto-deducts stock, generates a printable PDF invoice
- **Messages** — inbox, direct messages, and broadcast to all employees (simulated SMS)
- **Reports** — stock summary, low stock, expiry alerts, daily sales/earnings, purchase history
- **Employee Management** — Admin-only: add, activate/deactivate employees
- **User Accounts** — Admin-only login account management, separate from employee HR records
- **OTC Medicine Lookup** — rule-based recommender: enter patient age + symptom category, get ranked non-prescription medicine suggestions with dosage/warnings
- **Platform Assistant Chatbot** — rule-based floating widget that helps navigate the app
