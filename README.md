# Smart Hostel Management System (SHMS)

College project built with Java 17, Spring Boot, Maven, Thymeleaf, Spring Data JPA and SQLite.

## Run on Windows
1. Install JDK 17 and Maven 3.9+.
2. Open PowerShell in this folder (the folder containing `pom.xml`).
3. Run `mvn clean test` and then `mvn spring-boot:run`.
4. Open `http://localhost:8080`.

SQLite file is created at `./data/shms.db`. Keep this folder when backing up project data.

## Demo credentials
| Actor | Username | Password |
|---|---|---|
| Student | `student` | `Student@123` |
| Warden | `warden` | `Warden@123` |
| Accountant | `accountant` | `Accountant@123` |

The application seeds these accounts on first launch if the respective tables are empty. Change demo passwords before sharing outside your classroom.

## Role ownership
- Student: own profile, hostel application, own complaints and out-pass requests, attendance view.
- Warden: room allocation/transfer, complaints, attendance, visitors, out-pass decisions and operational reports.
- Accountant: fee records, fee status and collection summary.

See `ERROR_ANALYSIS.md` for SRS mapping, validations, known limitations, and manual test checklist. This is an academic demo; email delivery, scheduled backups, production-grade Spring Security/CSRF controls and deployment monitoring still need configuration.
