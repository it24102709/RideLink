# RideLink

Integrated backend for the IT3130 ride-sharing assignment: four independent Spring Boot applications, each owning a separate MongoDB database. Swagger UI and Postman are the demonstration interfaces. All data and payments are fictional.

Passengers can register, log in, request rides and record simulated payments. Drivers manage operational profiles, vehicles and availability, and update their assigned rides. Administrators manage account roles and status.

## Architecture and technology

| Technology | Purpose |
| --- | --- |
| Java / Spring Boot 3.5.6 / Spring Web | Four independently executable REST applications; the build targets Java 21 |
| Spring Security / JJWT | Signed JWT authentication, roles and ownership checks |
| Spring Data MongoDB / MongoDB | Document persistence with a separate database per service |
| Maven wrapper | Dependencies, compilation, tests and executable JAR packaging |
| springdoc OpenAPI 2.8.13 / Swagger UI | Generated API documentation and interactive testing |
| Lombok | Generated getters, setters and constructors |
| JUnit 5 / Mockito | Unit tests and mocked dependencies |

Services communicate through synchronous REST requests carrying JSON. Driver & Vehicle calls Account to validate driver accounts. Ride Management calls Driver & Vehicle to reserve/release drivers and Fare & Payment to finalize fares. Fare & Payment calls Ride Management to validate a ride before recording payment. Each service accesses its own database.

The root Maven project builds the four modules; it is not a fifth running service. No frontend, API gateway, service registry, message broker or real payment gateway is implemented.

## Ownership and endpoints

Fill in your group number and member details before submission.

| Service | Primary owner / student ID | Port | Database | Swagger |
| --- | --- | --- | --- | --- |
| Account | ____________________ | 8080 | account_service | http://localhost:8080/swagger-ui/index.html |
| Driver & Vehicle | ____________________ | 8081 | driver_vehicle_service | http://localhost:8081/swagger-ui/index.html |
| Ride Management | ____________________ | 8082 | ride_management_service | http://localhost:8082/swagger-ui/index.html |
| Fare & Payment | ____________________ | 8083 | fare_payment_service | http://localhost:8083/swagger-ui/index.html |

## Prerequisites

- JDK 21 or newer; Java 23 was used for local verification.
- MongoDB Community Server 8.x, with a separate database for each service.
- Python 3 for repeatable HTTP integration checks.
- Internet access for the first Maven dependency download.
- Postman or Swagger UI for demonstration.

Maven is provided by the wrapper. Docker and a frontend are not required. The build uses Spring Boot 3.5.6, Spring Security, Spring Data MongoDB, Lombok and springdoc OpenAPI 2.8.13.

## Windows quick start

Use PowerShell for the startup commands. Its prompt begins with `PS`; if using Command Prompt, enter `powershell` first.

On the prepared local computer, run these commands one at a time:

```powershell
cd "C:\Users\User\Downloads\RideLink"
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass -Force
$env:JAVA_HOME = "C:\Program Files\Java\jdk-23"
& "$env:JAVA_HOME\bin\java.exe" -version
```

The execution-policy setting applies only to this PowerShell session. On another computer, replace the project and JDK paths with its actual paths.

If MongoDB is stopped, run `.\scripts\Start-LocalMongo.ps1`. If the four backend services are stopped, run `.\scripts\Start-Services.ps1 -MongoPort 27018`. Wait approximately one minute, then open the Swagger links above. Use already-running project services instead of starting another instance on the same ports.

If service JARs are missing, build them using the instructions below. Stop running services before packaging their JARs on Windows.

## Build and test

From the project directory in PowerShell:

If the applications are already running, stop them first using `.\scripts\Stop-Services.ps1`. Keep MongoDB running.

```powershell
$env:JAVA_HOME = 'C:\path\to\your\jdk'
.\mvnw.cmd -B clean verify
```

On Linux/macOS use `bash mvnw -B clean verify`. The root build tests and packages all four modules. Unit/controller tests use mocks and do not require a database.

JARs are saved in each service's `target/` folder. Editing Java source does not update an already-running JAR; rebuild and restart after source changes. The HTTP integration script is a separate check and is not automatically executed by this Maven command.

If this computer's Java certificate store rejects Maven Central, configure Windows trust for Maven:

```powershell
$env:MAVEN_OPTS = '-Djavax.net.ssl.trustStoreType=Windows-ROOT -Djavax.net.ssl.trustStore=NONE'
```

Certificate verification remains enabled.

## Start and stop

With installed MongoDB running on port 27017:

```powershell
.\scripts\Start-Services.ps1
```

On this computer, where a portable MongoDB was prepared during integration:

```powershell
.\scripts\Start-LocalMongo.ps1
.\scripts\Start-Services.ps1 -MongoPort 27018
```

The portable server and data are in ignored `.runtime/` and are not committed. On another computer supply `Start-LocalMongo.ps1 -MongoBinary 'C:\path\to\mongod.exe'`, or use an installed server.

Start the database first, then Account, Driver & Vehicle, Ride Management and Fare & Payment. The script starts applications in that order; allow about one minute for startup. Logs are in `.runtime/`. Swagger URLs are above; JSON contracts are at `/api-docs` on each port.

```powershell
python scripts/smoke_test.py
.\scripts\Stop-Services.ps1
.\scripts\Stop-LocalMongo.ps1 # only for the portable server started by the helper
```

The smoke script creates unique fictional users/vehicles/rides and records actual successful and negative checks in `docs/test-evidence/integration-results.json`. Repeated runs add demonstration data to the four service databases.

For this computer's data in MongoDB Compass, connect to `mongodb://127.0.0.1:27018`. Open `account_service` > `accounts` for account documents. Clear collection filters and refresh after API changes. Another connection or database can show different records.

## Troubleshooting

| Message or symptom | Action |
| --- | --- |
| `$env:JAVA_HOME` produces a syntax error | Switch from Command Prompt to PowerShell |
| Running scripts is disabled | Run `Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass -Force` in the same PowerShell window |
| Port 27018 is already in use | Check whether the project's MongoDB is already running and reuse it |
| Port 8080–8083 is already in use | Use the existing application, or stop the recorded project services before restarting |
| Missing JAR | Stop services and run `.\mvnw.cmd -B verify` |
| Swagger returns 401 | Log in again and replace the saved bearerAuth token |
| Swagger returns 403 | Check the token's role and ownership of the requested resource |
| New accounts are absent in Compass | Connect to port 27018, open `account_service/accounts`, clear filters and refresh |
| Administrator login fails | Perform the startup administrator setup below; a login request alone does not create the account |
| Editing Java has no effect | Rebuild the application and restart its JAR |

## Configuration

`.env.example` lists the variables. Spring reads shell environment variables directly; it does not automatically load that file. `Start-Services.ps1` creates random development `JWT_SECRET` and `SERVICE_KEY` values in ignored `.runtime/development.env`, and passes the same keys to all services. Existing shell values take precedence. JWT_SECRET requires at least 32 UTF-8 bytes; SERVICE_KEY requires at least 32 characters. Keep them outside Git.

For manual starts set the same keys in all four processes. Set each `*_MONGODB_URI` to a different database. Override `ACCOUNT_SERVICE_URL`, `DRIVER_SERVICE_URL`, `RIDE_SERVICE_URL` and `FARE_SERVICE_URL` when changing hosts/ports. `PORT` changes an individually started service's port.

Optional administrator bootstrap: set `ADMIN_EMAIL` and `ADMIN_PASSWORD` before starting Account Service. The password must contain 12-72 characters. An administrator is created only if the email does not already exist. Public registration accepts PASSENGER and DRIVER only; no fixed administrator password is committed.

To create a local demo administrator, use the same prepared PowerShell window and keep MongoDB running:

```powershell
.\scripts\Stop-Services.ps1
$env:ADMIN_EMAIL = 'admin.demo@example.test'
$env:ADMIN_PASSWORD = Read-Host 'Enter a local demo admin password (12-72 characters)'
.\scripts\Start-Services.ps1 -MongoPort 27018
```

After startup, log in with the email above and the password you entered. Bootstrap does not reset an existing password or promote an existing account with the same email. Use a new email if you need a new administrator.

Existing environment variables take precedence over helper defaults. An already-set `*_MONGODB_URI` is not changed by `-MongoPort`; ensure it refers to the intended port and service database when switching configurations.

## Security and Swagger authorization

- Passwords are stored as salted BCrypt hashes; login compares the original password with the stored hash.
- JWTs are signed using HS256. Protected requests check their signature and expiry. JWT claims are encoded, not encrypted.
- Authentication is stateless: protected requests carry `Authorization: Bearer <token>`.
- Spring Security and ownership checks enforce PASSENGER, DRIVER and ADMIN permissions.
- Internal endpoints require the correct `X-Service-Key` and SERVICE authority.
- Account passwords are write-only in JSON and excluded from responses.
- JWT and service keys come from environment configuration and are not included in this README.

In Swagger, click **Authorize** and paste the complete raw login token into **bearerAuth**, without quotation marks or the `Bearer` prefix. **serviceAuth** takes the internal service key. Credentials saved in one service's Swagger page are not automatically saved in another. Swagger **Logout** clears its saved credentials; it does not immediately revoke the JWT at the backend.

## Account Service endpoints and validation

| Method and endpoint | Access | Purpose |
| --- | --- | --- |
| POST `/api/accounts` | Public | Register a passenger or driver |
| POST `/api/accounts/login` | Public | Authenticate and obtain a JWT |
| GET `/api/accounts/{id}` | Owner or ADMIN | View an account |
| PUT `/api/accounts/{id}` | Owner or ADMIN | Update the account name |
| GET `/api/accounts` | ADMIN | List accounts |
| PUT `/api/accounts/{id}/status` | ADMIN | Set ACTIVE or SUSPENDED status |
| PUT `/api/accounts/{id}/role` | ADMIN | Set PASSENGER, DRIVER or ADMIN role |
| DELETE `/api/accounts/{id}` | ADMIN | Delete an account; successful response is 204 |
| GET `/internal/accounts/{id}` | Service key | Read an active account for service integration |

Registration requires a nonblank name, a valid email format, a password of 8-72 characters and a PASSENGER or DRIVER role. Omitted role defaults to PASSENGER. Emails are normalized to lowercase and must be unique. New accounts have ACTIVE status and a backend-generated ID. Password validation checks length; it does not require uppercase letters, digits or symbols.

Login requires an existing ACTIVE account and matching password. Profile update requires a nonblank name. Role/status updates permit only the documented uppercase values. Missing accounts return 404 after applicable access checks. Internal account lookup returns 409 for an inactive account.

Example fictional passenger registration body:

```json
{
  "name": "Demo Passenger",
  "email": "passenger.demo01@example.test",
  "password": "DemoPass123!",
  "role": "PASSENGER"
}
```

Use a unique email for each registration. Successful registration returns 201. Login uses the same email and original password:

```json
{
  "email": "passenger.demo01@example.test",
  "password": "DemoPass123!"
}
```

Copy `accountId` and `token` from a successful login response. To update that account, enter its ID in the PUT path and use `{"name":"Demo Passenger Updated"}` as the body.

Input checks are primarily in `AccountService.java`. `GlobalExceptionHandler.java` converts validation/business exceptions into HTTP errors. Common codes are 200 success, 201 created, 204 success without a body, 400 invalid input, 401 authentication failure, 403 permission denied, 404 missing record, 409 conflict and 503 dependency unavailable.

## Postman demonstration

For a beginner walkthrough with copyable JSON, expected responses and troubleshooting, follow [the testing guide](docs/testing-guide.md).

To try every operation yourself, use [the complete 41-endpoint Swagger walkthrough](docs/swagger-all-endpoints-guide.md). It includes administrator setup, token and service-key instructions, exact request bodies, expected results, and final cleanup steps.

The completed Swagger browser run has [33 test results with screenshots](docs/test-evidence/swagger-ui-results.md) and an [image gallery](docs/test-evidence/swagger-ui-gallery.html). Every check passed, including the expected rejection responses.

Import `postman-collection.json` and `postman-environment.json`, select **RideLink Local**, and run **RideLink Integrated Workflow** in order. Scripts generate unique fictional emails, future expiry dates, capture IDs/tokens and check responses. The fictional test password is `Fictional-demo-pass-123`, which is demonstration data rather than a deployment credential.

The collection covers registration/login, driver/vehicle preparation, estimation, assignment, acceptance, start, completion, failed/successful simulated payment, receipt and cancellation. Negative cases cover authentication, ownership, availability, transitions and payment validation.

Vehicle `driverId` and ride `driverId` identify a **driver profile**. Driver profile `userId` and ride `driverAccountId` identify the **account**. Passenger identity comes from the requesting JWT. The assigned driver's account token advances the ride.

Fare rule: **LKR 200 + LKR 100 × distance in km**, rounded to two decimals. Distance must be greater than zero and at most 1000 km. Locations/distance are simulated inputs. Payment must equal the final fare calculated from the stored ride distance.

## Documentation

- `docs/architecture.md`: architecture/sequence diagrams, interface choices and limitations.
- `docs/viva-quick-guide.md`: project, framework, security and Account Service explanations for revision.
- `docs/technical-report.md` and `docs/RideLink_Technical_Report.pdf`: editable report and PDF; fill in group/member details and personal contribution statements.
- `docs/openapi/`: contracts exported from the running integrated services.
- `docs/test-evidence/`: actual unit and integration results.
- Local verification passed 70 unit/controller tests, 37 HTTP integration scenarios and 35 Postman/Newman assertions across 32 requests.
- `.github/workflows/ci.yml`: all-service build/tests and HTTP scenarios with MongoDB. Remote CI evidence becomes available after pushing.
- `archive/previous-implementation/`: prior Node prototype and notes, outside the active four-service build.

To fill the report details, edit `docs/technical-report.md`, then run `python scripts/build_report.py` (requires the `reportlab` package). This regenerates the PDF using the saved actual test summaries.

Optional command-line collection verification: install Newman in the ignored runtime folder with `npm install --prefix .runtime/postman --no-save --ignore-scripts newman`, then run `node scripts/run_postman.cjs`. The evidence file stores aggregate results rather than tokens.

## Git workflow

The local integration branch is `feature/integrate-ridelink`. It retains the previous account/driver/fare merges from develop and merges the latest fare and ride branches. Continue on member feature branches, use peer-reviewed PRs into develop, then promote the tested version to main through a reviewed PR. A local candidate release tag identifies the integration snapshot; the group must select its actual submission release after filling personal details. Local checks are not evidence of GitHub Actions execution or peer review.

## Limits and tool-use declaration

REST uses bounded timeouts and explicit errors, but cross-service updates are not a distributed transaction. Orphan reservations after process crashes need durable recovery. Suspension blocks new login; existing tokens remain usable until expiry. Shared keys/local HTTP are intended for the demonstration. See the architecture document for details.

The current Account Service source uses a 15-minute JWT expiry (`900000` milliseconds), while its login response still advertises `expiresIn: 3600`. These values need alignment. An older built JAR may have a different lifetime from edited source; the token's verified `exp` claim determines its actual expiry. Rebuild and restart to apply Java source changes.

AI assistance was used for integration, API alignment, security fixes, tests and documentation. Review the implementation, fill in your own contribution statements, and adapt this declaration to the institute's applicable policy. No contribution, review, remote CI or viva evidence is fabricated.
