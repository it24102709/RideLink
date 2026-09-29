const Payment = require("../models/Payment");

// Record Payment
const recordPayment = async (req, res) => {
  try {
    const { rideId, amount, paymentMethod } = req.body;

    // Input validation
    if (
      !rideId ||
      typeof rideId !== "string" ||
      rideId.trim() === "" ||
      typeof amount !== "number" ||
      !Number.isFinite(amount) ||
      amount <= 0 ||
      !paymentMethod ||
      !["CASH", "CARD", "ONLINE"].includes(paymentMethod)
    ) {
      return res.status(400).json({
        message:
          "Valid rideId, positive amount and valid paymentMethod are required"
      });
    }

    const payment = await Payment.create({
      rideId: rideId.trim(),
      amount,
      paymentMethod,
      paymentStatus: "PAID"
    });

    res.status(201).json({
      message: "Payment recorded successfully",
      payment
    });
  } catch (error) {
    res.status(500).json({
      message: "Server error",
      error: error.message
    });
  }
};


// Get Payment Status
const getPaymentStatus = async (req, res) => {
  try {
    const { rideId } = req.params;

    if (!rideId || rideId.trim() === "") {
      return res.status(400).json({
        message: "Valid rideId is required"
      });
    }

    const payment = await Payment.findOne({
      rideId: rideId.trim()
    });

    if (!payment) {
      return res.status(404).json({
        message: "Payment not found for this ride"
      });
    }

    res.status(200).json({
      rideId: payment.rideId,
      paymentStatus: payment.paymentStatus
    });
  } catch (error) {
    res.status(500).json({
      message: "Server error",
      error: error.message
    });
  }
};


// Generate Receipt
const generateReceipt = async (req, res) => {
  try {
    const { rideId } = req.params;

    if (!rideId || rideId.trim() === "") {
      return res.status(400).json({
        message: "Valid rideId is required"
      });
    }

    const payment = await Payment.findOne({
      rideId: rideId.trim()
    });

    if (!payment) {
      return res.status(404).json({
        message: "Payment not found for this ride"
      });
    }

    const receipt = {
      receiptId: `REC-${payment._id}`,
      rideId: payment.rideId,
      amount: payment.amount,
      paymentMethod: payment.paymentMethod,
      paymentStatus: payment.paymentStatus,
      generatedAt: new Date()
    };

    res.status(200).json({
      message: "Receipt generated successfully",
      receipt
    });
  } catch (error) {
    res.status(500).json({
      message: "Server error",
      error: error.message
    });
  }
};


// Retrieve Receipt
const getReceipt = async (req, res) => {
  try {
    const { rideId } = req.params;

    if (!rideId || rideId.trim() === "") {
      return res.status(400).json({
        message: "Valid rideId is required"
      });
    }

    const payment = await Payment.findOne({
      rideId: rideId.trim()
    });

    if (!payment) {
      return res.status(404).json({
        message: "Receipt not found for this ride"
      });
    }

    const receipt = {
      receiptId: `REC-${payment._id}`,
      rideId: payment.rideId,
      amount: payment.amount,
      paymentMethod: payment.paymentMethod,
      paymentStatus: payment.paymentStatus,
      generatedAt: payment.createdAt
    };

    res.status(200).json({
      message: "Receipt retrieved successfully",
      receipt
    });
  } catch (error) {
    res.status(500).json({
      message: "Server error",
      error: error.message
    });
  }
};


module.exports = {
  recordPayment,
  getPaymentStatus,
  generateReceipt,
  getReceipt
};