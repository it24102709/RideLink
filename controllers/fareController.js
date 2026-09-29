const Fare = require("../models/Fare");

// Estimate Fare
const estimateFare = async (req, res) => {
  try {
    const { rideId, distance } = req.body;

    // Input validation
    if (
      !rideId ||
      typeof rideId !== "string" ||
      rideId.trim() === "" ||
      typeof distance !== "number" ||
      !Number.isFinite(distance) ||
      distance <= 0
    ) {
      return res.status(400).json({
        message: "Valid rideId and positive distance are required"
      });
    }

    // Fare calculation rule
    // Fare = 100 + (Distance × 80)
    const baseFare = 100;
    const perKmRate = 80;

    const estimatedFare = baseFare + distance * perKmRate;

    const fare = await Fare.create({
      rideId: rideId.trim(),
      distance,
      estimatedFare
    });

    res.status(201).json({
      message: "Fare estimated successfully",
      fare
    });
  } catch (error) {
    res.status(500).json({
      message: "Server error",
      error: error.message
    });
  }
};


// Final Fare Calculation
const calculateFinalFare = async (req, res) => {
  try {
    const { rideId } = req.params;

    if (!rideId || rideId.trim() === "") {
      return res.status(400).json({
        message: "Valid rideId is required"
      });
    }

    const fare = await Fare.findOne({
      rideId: rideId.trim()
    });

    if (!fare) {
      return res.status(404).json({
        message: "Fare not found for this ride"
      });
    }

    fare.finalFare = fare.estimatedFare;

    await fare.save();

    res.status(200).json({
      message: "Final fare calculated successfully",
      fare
    });
  } catch (error) {
    res.status(500).json({
      message: "Server error",
      error: error.message
    });
  }
};


module.exports = {
  estimateFare,
  calculateFinalFare
};