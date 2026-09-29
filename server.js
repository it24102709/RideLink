require("dotenv").config();

const express = require("express");
const mongoose = require("mongoose");
const swaggerUi = require("swagger-ui-express");

const app = express();
const PORT = 3000;

// Middleware
app.use(express.json());

// Swagger documentation
const swaggerDocument = {
  openapi: "3.0.0",
  info: {
    title: "RideLink Fare & Payment Service API",
    version: "1.0.0",
    description: "API documentation for Fare and Payment Service"
  },
  servers: [
    {
      url: `http://localhost:${PORT}`
    }
  ],
  paths: {
    "/api/fares/estimate": {
      post: {
        summary: "Estimate fare",
        tags: ["Fare"],
        requestBody: {
          required: true,
          content: {
            "application/json": {
              schema: {
                type: "object",
                required: ["rideId", "distance"],
                properties: {
                  rideId: {
                    type: "string",
                    example: "R001"
                  },
                  distance: {
                    type: "number",
                    example: 5
                  }
                }
              }
            }
          }
        },
        responses: {
          201: {
            description: "Fare estimated successfully"
          },
          400: {
            description: "Invalid input"
          }
        }
      }
    },

    "/api/fares/{rideId}/final": {
      put: {
        summary: "Calculate final fare",
        tags: ["Fare"],
        parameters: [
          {
            name: "rideId",
            in: "path",
            required: true,
            schema: {
              type: "string"
            },
            example: "R001"
          }
        ],
        responses: {
          200: {
            description: "Final fare calculated successfully"
          },
          404: {
            description: "Fare not found"
          }
        }
      }
    },

    "/api/payments": {
      post: {
        summary: "Record payment",
        tags: ["Payment"],
        requestBody: {
          required: true,
          content: {
            "application/json": {
              schema: {
                type: "object",
                required: ["rideId", "amount", "paymentMethod"],
                properties: {
                  rideId: {
                    type: "string",
                    example: "R001"
                  },
                  amount: {
                    type: "number",
                    example: 500
                  },
                  paymentMethod: {
                    type: "string",
                    enum: ["CASH", "CARD", "ONLINE"],
                    example: "CARD"
                  }
                }
              }
            }
          }
        },
        responses: {
          201: {
            description: "Payment recorded successfully"
          },
          400: {
            description: "Invalid input"
          }
        }
      }
    },

    "/api/payments/{rideId}/status": {
      get: {
        summary: "Get payment status",
        tags: ["Payment"],
        parameters: [
          {
            name: "rideId",
            in: "path",
            required: true,
            schema: {
              type: "string"
            },
            example: "R001"
          }
        ],
        responses: {
          200: {
            description: "Payment status retrieved successfully"
          },
          404: {
            description: "Payment not found"
          }
        }
      }
    },

    "/api/payments/{rideId}/receipt": {
      post: {
        summary: "Generate receipt",
        tags: ["Receipt"],
        parameters: [
          {
            name: "rideId",
            in: "path",
            required: true,
            schema: {
              type: "string"
            },
            example: "R001"
          }
        ],
        responses: {
          200: {
            description: "Receipt generated successfully"
          },
          404: {
            description: "Payment not found"
          }
        }
      },

      get: {
        summary: "Retrieve receipt",
        tags: ["Receipt"],
        parameters: [
          {
            name: "rideId",
            in: "path",
            required: true,
            schema: {
              type: "string"
            },
            example: "R001"
          }
        ],
        responses: {
          200: {
            description: "Receipt retrieved successfully"
          },
          404: {
            description: "Receipt not found"
          }
        }
      }
    }
  }
};

// Swagger route
app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerDocument));

// MongoDB connection
mongoose
  .connect(process.env.MONGODB_URI)
  .then(() => {
    console.log("MongoDB connected successfully");
  })
  .catch((error) => {
    console.error("MongoDB connection failed:", error.message);
  });

// Fare routes
const fareRoutes = require("./routes/fareRoutes");
app.use("/api/fares", fareRoutes);

// Payment routes
const paymentRoutes = require("./routes/paymentRoutes");
app.use("/api/payments", paymentRoutes);

// Home route
app.get("/", (req, res) => {
  res.send("Fare & Payment Service is working!");
});

// Start server
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});