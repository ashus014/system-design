class ParkingSpot {

    private int id;
    private boolean isEmpty;
    private Vehicle vehicle;
    private double price;

    // Getters
    public int getId() {
        return id;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public double getPrice() {
        return price;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void parkVehicle(Vehicle vehicle) {
        if (isEmpty) {
            this.vehicle = vehicle;
            this.isEmpty = false;
            System.out.println("Vehicle parked successfully in spot " + id);
        } else {
            System.out.println("Parking spot " + id + " is already occupied");
        }
    }

    public void removeVehicle() {
        this.vehicle = null;
        this.isEmpty = true;
        System.out.println("Vehicle removed from spot " + id);
    }
}