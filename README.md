# Pharmacy Management System (Final Year Project)

**Stack:** Java 17 + Spring Boot 3.3.2 + Spring Security + Spring Data JPA + Thymeleaf (HTML/CSS/JS) + MySQL

## Features (matches your menu)
1. **Pharmacist Authentication** — Spring Security, DB-backed login, BCrypt-hashed passwords, role-based access (ADMIN / PHARMACIST)
2. **Medicine Management** — list, search by name, add, update, delete, low-stock badges
3. **Supplier Management** — list, add, delete
4. **Record Purchase (Stock In)** — select supplier + medicines + quantities, auto-increments stock
5. **Record Sale (Checkout)** — cart-style checkout, auto-deducts stock, **auto-generates a printable invoice** with store name/ID
6. **Messages** — inbox + send message between staff
7. **Reports** — stock summary, low stock, expiry (next 30 days), today's sales & earnings
8. **Employee Management** — Admin-only: list, add, deactivate employees
9. **OTC Medicine Lookup (new feature)** — pharmacist enters patient age + symptom/disease category, platform ranks and shows the **top 3 non-prescription medicines** with dosage and warnings. Rule-based (age-range + category filter + priority field) — fully explainable in viva, no external AI cost.
10. **Platform Assistant Chatbot** — small floating widget (bottom-right) on every page. Pure frontend, rule-based keyword matching (`static/js/chatbot.js`) that helps pharmacists navigate the platform (e.g. "how do I add a medicine"). This is **not** the OTC recommender — it only assists with using the software itself.

## Project Structure
```
src/main/java/com/pharmacy/pms/
  config/      -> SecurityConfig, DataInitializer (seeds default admin + sample OTC data)
  model/       -> JPA entities (User, Medicine, Supplier, Purchase, Sale, Employee, Message, OTCMedicine, ...)
  repository/  -> Spring Data JPA repositories
  service/     -> business logic (stock deduction on sale, stock increase on purchase, OTC ranking, etc.)
  controller/  -> MVC controllers returning Thymeleaf views
src/main/resources/
  templates/   -> Thymeleaf HTML pages (login, dashboard, medicine, supplier, purchase, sale, employee, messages, reports, otc)
  static/css/style.css -> all styling
  static/js/chatbot.js -> rule-based platform assistant
```

## Setup Instructions

### 1. Prerequisites
- Java 17+ (JDK)
- Maven (or use your IDE's bundled Maven)
- MySQL Server running locally

### 2. Create the database
You don't need to manually create tables — Hibernate does it via `ddl-auto=update`.
Just make sure MySQL is running. The app will auto-create a `pharmacy_db` database
(see `createDatabaseIfNotExist=true` in `application.properties`).

### 3. Configure DB credentials
Edit `src/main/resources/application.properties`:
```
spring.datasource.username=root
spring.datasource.password=root   <-- change to your MySQL password
```

### 4. Run
```bash
mvn spring-boot:run
```
Or open the project in IntelliJ IDEA / Eclipse (Spring Boot support) and run
`PharmacyManagementSystemApplication.java`.

The app starts at: **http://localhost:8080**

### 5. Login
A default admin account is auto-created on first run:
- **Username:** `admin`
- **Password:** `admin123`

(Change this password before your final submission/demo — it's only meant to get you started.)

### 6. Try the OTC feature
10 sample OTC medicines (Fever, Pain Relief, Allergy, Cold & Cough, Digestive) are
auto-seeded so you can demo the lookup immediately — go to **OTC Lookup** in the navbar,
enter an age (e.g. 8) and a category (e.g. "Fever") and hit Search.

## Notes for your viva
- **Why OTC lookup and not just pharmacist memory?** Framed as a *speed + consistency +
  audit-trail* decision-support tool, not a replacement for pharmacist judgment — see the
  disclaimer text already built into the OTC page.
- **Why rule-based, not real AI?** Fully explainable logic (age range + category + priority
  ranking) — no black box, easy to defend under questioning, zero external API cost.
- **Security:** passwords are BCrypt-hashed (never stored in plaintext); `/employees/**` is
  restricted to `ROLE_ADMIN` via `SecurityConfig`.
- **Stock integrity:** `PurchaseService` and `SaleService` are `@Transactional` so stock
  quantity updates and the purchase/sale record are saved atomically.

## Update Log (latest round of changes)

1. **PDF Bill + SMS on checkout** — `PdfInvoiceService` (iText) generates a real downloadable PDF
   invoice from `/sales/{id}/invoice/pdf`. Checkout page now has a live **medicine search bar**
   (client-side JS filter) and an optional "Send bill via SMS" checkbox. SMS is **simulated**
   (see below) — no paid provider account needed to run/demo this project.
2. **Purchase → Reports** — Reports page now includes a **Purchase History** section (supplier,
   date, items, total) and total purchase spend. Purchases were already being saved to the DB;
   this just surfaces them.
3. **Employee section bug fix** — new employees were silently saved as `active = false` (a
   Lombok `@Builder.Default` quirk that only applies via the Builder, not Spring's form binder)
   with no way to reactivate them. Fixed in `EmployeeService.save()`, and an **Activate** button
   was added alongside Deactivate. Flash success/error messages now show on the page after
   add/deactivate/activate.
4. **Working Messages section** — "Broadcast to All Employees" (`/messages/broadcast`) sends a
   (simulated) SMS to every active employee's phone number on file and records it as an in-app
   message. An **SMS Log** page (`/messages/sms-log`) shows every simulated SMS sent (invoices +
   broadcasts) so you can demonstrate it fired correctly in your viva.
5. **Login accounts (Admin)** — new `/users` page (Admin-only) to create/deactivate login
   accounts, kept **separate from Employee HR records** as you specified. Only accounts created
   here (BCrypt-hashed passwords) can log in — this is on top of the DB-backed Spring Security
   auth already in place.

### About the simulated SMS
No real SMS provider (Twilio, etc.) is wired up — that requires a paid account and API keys.
Every "SMS" the app sends is logged to a `sms_log` DB table and printed to the console instead,
so the whole flow (checkout → bill → SMS attempt) works end-to-end for your demo without any
external account. To go live later: open `SmsService.sendSms()` and replace the body with a real
provider call — nothing else in the app needs to change.

## Extending Further (optional, if you have time)
- Add JWT-based REST API layer for a mobile app
- Add SMS/email invoice sending (Twilio/JavaMail) — currently invoice is print/PDF-ready only
- Add `@Version` optimistic locking on `Medicine.quantity` if you expect concurrent checkouts
- Add JUnit + Mockito tests for services (matches your earlier InternSark project style)
