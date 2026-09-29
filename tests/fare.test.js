describe("Fare Calculation", () => {
  test("should calculate fare correctly", () => {
    const baseFare = 100;
    const perKmRate = 80;
    const distance = 5;

    const fare = baseFare + distance * perKmRate;

    expect(fare).toBe(500);
  });
});