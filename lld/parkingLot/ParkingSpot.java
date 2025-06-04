package lld.parkingLot;

public abstract class ParkingSpot {
    private final int id;
    private boolean isEmpty;
    private Vehicle vehicle;
    private final double hourlyRate;

    public ParkingSpot(int id, double hourlyRate) {
        if (id <= 0) {
            throw new IllegalArgumentException("Spot ID must be positive");
        }
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative");
        }
        this.id = id;
        this.isEmpty = true;
        this.hourlyRate = hourlyRate;
    }

    public int getId() {
        return id;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public synchronized void parkVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (!isEmpty) {
            throw new IllegalStateException("Parking spot " + id + " is already occupied");
        }
        if (!isValidVehicleType(vehicle.getVehicleType())) {
            throw new IllegalArgumentException("Invalid vehicle type for this spot");
        }

        this.vehicle = vehicle;
        this.isEmpty = false;
        System.out.println("Vehicle " + vehicle.getVehicleNumber() + " parked in spot " + id);
    }

    public synchronized void removeVehicle() {
        if (isEmpty) {
            throw new IllegalStateException("Parking spot " + id + " is already empty");
        }

        String vehicleNumber = this.vehicle.getVehicleNumber();
        this.vehicle = null;
        this.isEmpty = true;
        System.out.println("Vehicle " + vehicleNumber + " removed from spot " + id);
    }

    protected abstract boolean isValidVehicleType(VehicleType vehicleType);
}