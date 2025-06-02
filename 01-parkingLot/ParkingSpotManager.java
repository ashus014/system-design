package 01-parkingLot;

public class ParkingSpotManager {
    List<ParkingSpot> parkingSpots;

    ParkingSpotManager(List<ParkingSpot> parkingSpots) {
        this.parkingSpots = parkingSpots;
    }

    public ParkingSpot findParkingSpace() {
        return parkingSpots.stream()
        .filter(ParkingSpot::isEmpty)
        .findFirst()
        .orElse(null);
    }

    public void addParkingSpace(ParkingSpot parkingSpot) {
        parkingSpots.add(parkingSpot);
    }

    public void removeParkingSpace(ParkingSpot parkingSpot) {
        parkingSpots.remove(parkingSpot);
    }

    public void parkVehicle(Vehicle vehicle) {
        ParkingSpot parkingSpot = findParkingSpace();
        if (parkingSpot == null) {
            System.out.println("No parking space available");
            return;
        }
        parkingSpot.parkVehicle(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        ParkingSpot parkingSpot = findParkingSpace();
        if (parkingSpot == null) {
            System.out.println("No parking space available");
            return;
        }
    }
    
}
