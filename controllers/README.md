# RideLink – Fare & Payment Service

## Overview

The Fare & Payment Service is one of the four microservices in the RideLink ride-sharing platform.

This service is responsible for:

* Fare estimation
* Final fare calculation
* Simulated payment recording
* Payment status management
* Receipt generation
* Receipt retrieval

## Technologies Used

* Node.js
* Express.js
* MongoDB
* Mongoose
* Swagger / OpenAPI
* Jest

## Project Structure

```text
demo/
├── controllers/
│   ├── fareController.js
│   └── paymentController.js
│
├── models/
│   ├── Fare.js
│   └── Payment.js
│
├── routes/
│   ├── fareRoutes.js
│   └── paymentRoutes.js
│
├── tests/
│   ├── fare.test.js
│   ├── payment.test.js
│   └── receipt.test.js
│
├── server.js
├── package.json
├── .env
└── README.md
```

## Fare Calculation Rule

The service uses the following documented fare calculation rule:

**Fare = Base Fare + (Distance × Rate per km)**

* Base Fare = 100
* Rate per km = 80

For example, for a 5 km ride:

**Fare = 100 + (5 × 80) = 500**

## API Endpoints

### Fare APIs

| Method | Endpoint                    | Description          |
| ------ | --------------------------- | -------------------- |
| POST   | `/api/fares/estimate`       | Estimate fare        |
| PUT    | `/api/fares/{rideId}/final` | Calculate final fare |

### Payment APIs

| Method | Endpoint                         | Description        |
| ------ | -------------------------------- | ------------------ |
| POST   | `/api/payments`                  | Record payment     |
| GET    | `/api/payments/{rideId}/status`  | Get payment status |
| POST   | `/api/payments/{rideId}/receipt` | Generate receipt   |
| GET    | `/api/payments/{rideId}/receipt` | Retrieve receipt   |

## Payment Methods

The service supports:

* CASH
* CARD
* ONLINE

## Payment Status

The supported payment statuses are:

* PENDING
* PAID
* FAILED

## Validation and Error Handling

The service validates required input values before processing requests.

Examples of handled errors include:

* Invalid ride ID
* Empty ride ID
* Invalid distance
* Negative distance
* Invalid payment amount
* Invalid payment method
* Fare not found
* Payment not found

Appropriate HTTP status codes such as `400`, `404`, `201`, and `200` are returned based on the request result.

## API Documentation

Swagger UI is available at:

```text
http://localhost:3000/api-docs
```

## Testing

Unit tests are implemented using Jest.

The current test coverage includes:

* Fare calculation
* Payment method validation
* Receipt ID generation

Run the tests using:

```bash
npm test
```

Current test result:

```text
Test Suites: 3 passed, 3 total
Tests:       3 passed, 3 total
```

## Environment Variables

The MongoDB connection string is stored in the `.env` file.

Example:

```text
MONGODB_URI=your_mongodb_connection_string
```

Sensitive credentials should not be committed to Git.

## Running the Service

Install dependencies:

```bash
npm install
```

Start the server:

```bash
node server.js
```

The service runs on:

```text
http://localhost:3000
```
