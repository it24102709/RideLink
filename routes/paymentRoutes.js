const express = require("express");

const {
  recordPayment,
  getPaymentStatus,
  generateReceipt,
  getReceipt
} = require("../controllers/paymentController");

const router = express.Router();

// Record payment
router.post("/", recordPayment);

// Get payment status
router.get("/:rideId/status", getPaymentStatus);

// Generate receipt
router.post("/:rideId/receipt", generateReceipt);

// Retrieve receipt
router.get("/:rideId/receipt", getReceipt);

module.exports = router;