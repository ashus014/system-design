package LLD.parkingLot;

public class EntranceGate {

    public Ticket parkVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        try {
            ParkingSpotManager manager = ParkingManagerFactory.getParkingManager(vehicle.getVehicleType());

            if (manager.getAvailableSpots() == 0) {
                throw new IllegalStateException("No parking space available for " + vehicle.getVehicleType());
            }

            if (!manager.parkVehicle(vehicle)) {
                throw new IllegalStateException("Failed to park vehicle");
            }

            ParkingSpot spot = manager.getSpotForVehicle(vehicle);
            if (spot == null) {
                throw new IllegalStateException("Vehicle parked but spot not found");
            }

            return new Ticket(vehicle, spot);

        } catch (Exception e) {
            System.err.println("Error parking vehicle: " + e.getMessage());
            throw e;
        }
    }

    public int getAvailableSpots(VehicleType vehicleType) {
        try {
            ParkingSpotManager manager = ParkingManagerFactory.getParkingManager(vehicleType);
            return manager.getAvailableSpots();
        } catch (Exception e) {
            System.err.println("Error getting available spots: " + e.getMessage());
            return 0;
        }
    }
}