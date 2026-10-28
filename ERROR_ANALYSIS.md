# Smart Hostel Management System — Error Analysis & Validation Notes

## Source requirements mapped
Based on the supplied SRS practical PDF: secure login, student registration, hostel application, room allocation, fee management, complaint tracking (Open / In Progress / Resolved), profile management, report generation, attendance (9:00–9:30 PM), visitor tracking, out-pass approval, room transfer, and notifications.

## Role/access matrix in this three-actor build
- **Student**: register/sign in, apply for hostel, view/update own profile, submit/view own complaints, request/view own out-passes, view own attendance history where exposed by the page.
- **Warden**: view students, allocate/transfer rooms, update complaint statuses, approve/reject out-passes, record attendance during the specified time window, manage visitor entry/exit, view operational reports.
- **Accountant**: fee records, fee status and collection summary. Fee endpoints are restricted to Accountant.
- Admin is deliberately omitted from the sign-in options because the requested deliverable specifies exactly three actors. The PDF says “Admin alone can generate reports”; in this build, reports are assigned to Warden as a documented scope decision so a three-actor demo has reporting access. For a strict SRS interpretation, add Admin as a fourth actor or change the requirement with instructor approval.

## Validation/error handling checks implemented in the existing application
- Duplicate Student ID / username checks during registration.
- Password minimum length of 8 characters for new registrations; BCrypt password hashes.
- Room allocation checks for missing student/room, existing allocation and room capacity.
- Room transfer checks for current room, destination existence, same-room transfer and destination capacity.
- Fee amount must be greater than zero; student ID must exist.
- Complaint status limited to Open, In Progress, Resolved.
- Out-pass return timestamp must be after departure timestamp.
- Attendance is restricted to 9:00 PM–9:30 PM and duplicate daily attendance is rejected.
- Visitor entry checks permitted time window and student existence.
- Session role checks are applied to role-specific pages/actions; students' complaint and out-pass lists are filtered to their own student ID.

## Known limitations / error-analysis findings
1. **Build verification**: The source package is prepared, but a full Maven compile/run test was not available in this packaging step. Run `mvn clean test` and `mvn spring-boot:run` on a machine with internet access so Maven can download dependencies.
2. **SQLite + Hibernate compatibility**: This build uses `sqlite-jdbc` plus Hibernate community SQLite dialect. If the dependency version is unavailable in your local Maven cache, run Maven online once. Keep the SQLite database file backed up before changing entity schemas.
3. **CSRF/session hardening**: This is a college project demo with session-based role checks, not a production security deployment. Add Spring Security, CSRF protection, session fixation protection and login throttling before real deployment.
4. **Student-specific data**: Complaint and out-pass lists are filtered by student. Further production hardening should ensure all POST actions use a dedicated authenticated principal and add database-level ownership checks.
5. **Notifications**: Email notifications are a requirement in the PDF but require SMTP credentials and are not configured by default. Current app uses on-screen success/error messages.
6. **Daily backup and 99.9% uptime**: These are non-functional SRS targets, not guaranteed by a local single-file demo. Configure scheduled backups and production monitoring separately.
7. **Visitor hours**: The controller uses 09:00–19:00 as a configurable demo window because the PDF says permitted timings but does not specify the exact interval.
8. **Registration trust**: Warden and Accountant registration endpoints exist in the project; for a real deployment, staff accounts should be provisioned/approved by an authorized operator rather than self-registered.
9. **Reports rule discrepancy**: As above, the SRS assigns reports to Admin while this requested three-role app assigns reports to Warden. Confirm this deviation with the instructor.

## Manual test checklist
1. Sign in with each demo account and verify the dashboard changes by role.
2. Try opening `/fees` as Student and Warden; expect redirect to their own dashboard.
3. Try `/rooms` as Accountant and Student; expect redirect.
4. Register a student with duplicate Student ID and verify a clear error.
5. Allocate one student to a room at capacity and verify rejection.
6. Submit an out-pass with return before departure and verify rejection.
7. Mark attendance outside 21:00–21:30 and verify rejection.
8. Record a fee with amount `0` or a negative amount and verify rejection.
9. Confirm the database persists in `./data/shms.db` after application restart.
