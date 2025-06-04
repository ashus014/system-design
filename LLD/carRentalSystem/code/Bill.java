package LLD.carRentalSystem.code;

class Bill {
    Reservation res;
    boolean isPaid;
    double amount;

    public Bill(Reservation res, boolean isPaid, double amount) {
        this.res = res;
        this.isPaid = isPaid;
        this.amount = amount;
    }
}