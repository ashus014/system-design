package LLD.carRentalSystem.code;

class Payment {
    Bill bill;

    public Payment(Bill bill) {
        this.bill = bill;
    }

    public void payBill() {
        bill.isPaid = true;
    }
}
