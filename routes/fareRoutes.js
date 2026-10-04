const express = require("express");

const {
  estimateFare,
  calculateFinalFare
} = require("../controllers/fareController");

const router = express.Router();

router.post("/estimate", estimateFare);

router.put("/:rideId/final", calculateFinalFare);

module.exports = router;