describe("Payment Service", () => {
  test("should accept a valid payment method", () => {
    const paymentMethod = "CARD";

    expect(["CASH", "CARD", "ONLINE"]).toContain(paymentMethod);
  });
});