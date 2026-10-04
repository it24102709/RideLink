

Readme · MD
RideLink – Backend Microservices for a Ride-Sharing Platform
Module: IT3130 – Application Development (Group Assignment, 30%) Faculty: Faculty of Computing, Department of Information Technology Group: GroupXX <!-- TODO: replace with your group number --> Release tag (assessed version): v1.0.0 <!-- TODO: confirm tag name -->

RideLink is a fictional ride-sharing platform. This repository contains a backend-only solution made up of four independently executable microservices. All data, locations and payments are fictional or simulated. Swagger UI/OpenAPI and a shared Postman collection are the official interfaces for testing and demonstration.

1. Team and service ownership
#	Microservice	Primary owner	Student ID	Folder
1	Account Service	Member 1 – TODO	TODO	account-service/
2	Driver & Vehicle Service	Dilka K.B.T	IT24101143	driver-vehicle-service-springboot/
3	Ride Management Service	Member 3 – TODO	TODO	ride-management-service/
4	Fare & Payment Service	Member 4 – TODO	TODO	fare-payment-service/
All members take part in architecture, API-contract and integration decisions. Each member owns one service and contributes through their own Git identity.

2. Architecture overview
                 +---------------------+
                 |   Account Service   |  register / login / JWT / roles
                 +----------+----------+
                            ^
          token validation  |  (sync REST)
        +-------------------+-------------------+
        |                   |                   |
+-------+--------+  +-------+--------+  +-------+--------+
| Driver & Vehicle|<-| Ride Management |->| Fare & Payment |
|    Service      |  |    Service      |  |    Service     |
+-------+--------+  +-------+--------+  +-------+--------+
        |                   |                   |
    [Driver DB]         [Ride DB]          [Payment DB]
Database per service: each service owns its own MongoDB database. No service reads or writes another service's database.
Stable identifiers: services refer to each other's data only through IDs (userId, driverId, vehicleId, rideId, paymentId).
Diagrams: the full architecture diagram and sequence diagrams are in docs/ and in the technical report.
Interservice communication
Interaction	From → To	Style	Reason
Token / role validation	Driver, Ride, Fare → Account	Synchronous REST	TODO
Get eligible available drivers	Ride → Driver & Vehicle	Synchronous REST	TODO
Fare estimate / final fare	Ride → Fare & Payment	Synchronous REST	TODO
<!-- TODO: update this table to match what you actually implemented. -->
3. Technology stack
Area	Choice
Language / runtime	Java 17
Framework	Spring Boot 3.2
Build tool	Maven
Database	MongoDB (one database per service)
Security	Spring Security + JWT (role-based authorisation)
API docs	springdoc-openapi (Swagger UI)
Testing	JUnit 5, Mockito, Spring Boot Test
CI	GitHub Actions
<!-- TODO: adjust if other services use a different stack. -->
4. Prerequisites
JDK 17
Apache Maven 3.9+
MongoDB (local instance or MongoDB Atlas)
Git
Postman (to run the shared collection)
Check your setup:

bash
java -version
mvn -version
mongod --version
5. Configuration
No secrets are committed to this repository. Every service reads sensitive values from environment variables.

Variable	Used by	Description	Example (non-sensitive)
MONGODB_URI	all services	MongoDB connection string for that service's own database	mongodb://localhost:27017/ridelink_driver
JWT_SECRET	all services	Shared signing secret for JWT (must be identical across services)	change-me-to-a-long-random-string
JWT_EXPIRATION_MS	Account Service	Token lifetime in milliseconds	3600000
ACCOUNT_SERVICE_URL	Driver, Ride, Fare	Base URL of the Account Service	http://localhost:8081
DRIVER_SERVICE_URL	Ride	Base URL of the Driver & Vehicle Service	http://localhost:8082
FARE_SERVICE_URL	Ride	Base URL of the Fare & Payment Service	http://localhost:8084
SERVER_PORT	all services	Port the service listens on	see table below
Example (Linux / macOS):

bash
export MONGODB_URI="mongodb://localhost:27017/ridelink_driver"
export JWT_SECRET="change-me-to-a-long-random-string"
export ACCOUNT_SERVICE_URL="http://localhost:8081"
Example (Windows PowerShell):

powershell
$env:MONGODB_URI = "mongodb://localhost:27017/ridelink_driver"
$env:JWT_SECRET = "change-me-to-a-long-random-string"
$env:ACCOUNT_SERVICE_URL = "http://localhost:8081"
Use a different MONGODB_URI database name for each service.

6. Ports and endpoint locations
Service	Port	Swagger UI	OpenAPI JSON
Account Service	8081	http://localhost:8081/swagger-ui/index.html	http://localhost:8081/v3/api-docs
Driver & Vehicle Service	8082	http://localhost:8082/swagger-ui/index.html	http://localhost:8082/v3/api-docs
Ride Management Service	8083	http://localhost:8083/swagger-ui/index.html	http://localhost:8083/v3/api-docs
Fare & Payment Service	8084	http://localhost:8084/swagger-ui/index.html	http://localhost:8084/v3/api-docs
<!-- TODO: confirm the ports used by your services. -->
7. Start-up order
Start the services in this order, each in its own terminal:

MongoDB – make sure it is running.
Account Service – the others depend on it for token validation.
Driver & Vehicle Service
Fare & Payment Service
Ride Management Service – depends on the three services above.
bash
# Example: run a service (repeat for each folder)
cd account-service
mvn spring-boot:run
bash
cd driver-vehicle-service-springboot
mvn spring-boot:run
bash
cd fare-payment-service
mvn spring-boot:run
bash
cd ride-management-service
mvn spring-boot:run
Confirm each service is up by opening its Swagger UI (section 6).

8. Running the tests
Unit tests (per service):

bash
cd <service-folder>
mvn clean test
Run all four services' tests from the repository root:

bash
for d in account-service driver-vehicle-service-springboot ride-management-service fare-payment-service; do
  (cd "$d" && mvn clean test) || exit 1
done
Integrated workflow tests: import the Postman files from postman/ and run the collection (see section 10).

9. Sample credentials and test data
All data below is fictional and for demonstration only.

Role	Email	Password
Admin	admin@ridelink.test	Admin@123
Passenger	passenger1@ridelink.test	Passenger@123
Driver	driver1@ridelink.test	Driver@123
<!-- TODO: replace with the accounts your seed data / Postman collection actually creates. -->
Sample locations are plain place names or simulated coordinates, for example pickup Colombo Fort and destination Kandy.

10. API testing with Swagger and Postman
Swagger UI

Open a service's Swagger UI (section 6).
Call POST /api/auth/login on the Account Service and copy the returned token.
Click Authorize in the other services' Swagger UI and paste Bearer <token>.
Postman

Import postman/RideLink.postman_collection.json and postman/RideLink.postman_environment.json.
Select the RideLink environment (it contains only non-sensitive example values).
Run the collection folders in order: Account & Access → Driver Preparation → Fare Estimation → Ride Request & Assignment → Ride Lifecycle → Completion & Payment → Negative Scenarios.
11. Required workflows demonstrated
Account and access – register passenger and driver, log in, role enforcement.
Driver preparation – register vehicle details, set availability, set service area / simulated location.
Fare estimation – estimate for a pickup and destination using the documented fare rule.
Ride request and assignment – create a ride, fetch eligible available drivers, assign a driver.
Ride lifecycle – REQUESTED → ASSIGNED → ACCEPTED → IN_PROGRESS → COMPLETED, plus CANCELLED, with validated transitions.
Completion and payment – final fare calculation, simulated payment, receipt retrieval.
Negative scenarios – no available driver, invalid status transition, unauthorised access, invalid input, failed simulated payment.
Fare calculation rule
TODO: document your rule, e.g.
estimatedFare = baseFare + (distanceKm * ratePerKm) + (durationMin * ratePerMin)
12. Security
JWT-based authentication issued by the Account Service.
Role-based authorisation for PASSENGER, DRIVER and ADMIN operations.
Request validation with Bean Validation (@Valid) and a consistent JSON error format.
Secrets supplied through environment variables only; none committed.
13. Version control workflow
main – always integrated and demonstrable; protected, changes via pull request only.
feature/<service-or-topic> – one branch per feature (e.g. feature/driver-vehicle-service).
Every change is merged through a pull request with at least one peer review.
Commit messages are short and descriptive (e.g. Add availability update endpoint).
The assessed version is identified by the release tag in the header of this README.
14. Continuous integration
GitHub Actions workflow: .github/workflows/ci.yml

Triggers on push and pull_request to main (and feature branches).
Builds and runs unit tests for all four services.
Fails the pipeline if any service fails to build or any test fails.
15. Repository structure
ridelink/
├── account-service/
├── driver-vehicle-service-springboot/
├── ride-management-service/
├── fare-payment-service/
├── postman/
│   ├── RideLink.postman_collection.json
│   └── RideLink.postman_environment.json
├── docs/
│   ├── architecture-diagram.png
│   └── sequence-diagram.png
├── .github/workflows/ci.yml
└── README.md
<!-- TODO: adjust folder names to match the real repository. -->
16. Known limitations
TODO – list honest limitations (e.g. no API gateway, payments simulated only, no frontend).
17. Academic integrity
Any use of generative-AI tools is declared in the appendix of the technical report in line with the institute's academic-integrity policy. External code, diagrams or text are acknowledged in the report.


