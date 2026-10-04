const mongoose = require("mongoose");

const fareSchema = new mongoose.Schema(
  {
    rideId: {
      type: String,
      required: true
    },

    distance: {
      type: Number,
      required: true
    },

    estimatedFare: {
      type: Number,
      required: true
    },

    finalFare: {
      type: Number,
      default: null
    },

    paymentStatus: {
      type: String,
      enum: ["PENDING", "PAID", "FAILED"],
      default: "PENDING"
    }
  },
  {
    timestamps: true
  }
);

module.exports = mongoose.model("Fare", fareSchema);