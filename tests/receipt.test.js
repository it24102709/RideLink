describe("Receipt Service", () => {
  test("should generate a receipt ID", () => {
    const paymentId = "12345";

    const receiptId = `REC-${paymentId}`;

    expect(receiptId).toBe("REC-12345");
  });
});