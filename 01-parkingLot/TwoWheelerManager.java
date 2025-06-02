import java.util.ArrayList;
import java.util.List;

public class TwoWheelerManager extends ParkingSpotManager {
    private int totalSpots;
    private int occupiedSpots;

    public TwoWheelerManager(int totalSpots) {
        super(createParkingSpots(totalSpots));
        this.totalSpots = totalSpots;
        this.occupiedSpots = 0;
    }

    private static List<ParkingSpot> createParkingSpots(int totalSpots) {
        List<ParkingSpot> spots = new ArrayList<>();
        for (int i = 1; i <= totalSpots; i++) {
            spots.add(new TwoWheelerSpot(i));
        }
        return spots;
    }

    public boolean parkVehicle(Vehicle vehicle) {
        if (!vehicle.getVehicleType().equals("TWO_WHEELER")) {
            System.out.println("Invalid vehicle type for two-wheeler parking");
            return false;
        }

        if (occupiedSpots >= totalSpots) {
            System.out.println("Sorry, two-wheeler parking is full");
            return false;
        }

        ParkingSpot spot = findParkingSpace();
        if (spot != null) {
            spot.parkVehicle(vehicle);
            occupiedSpots++;
            return true;
        }
        return false;
    }

    public boolean removeVehicle(String vehicleNumber) {
        for (ParkingSpot spot : parkingSpots) {
            if (!spot.isEmpty() && spot.getVehicle().getVehicleNumber().equals(vehicleNumber)) {
                spot.removeVehicle();
                occupiedSpots--;
                return true;
            }
        }
        System.out.println("Vehicle not found in two-wheeler parking");
        return false;
    }

    public int getAvailableSpots() {
        return totalSpots - occupiedSpots;
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public double calculateParkingFee(String vehicleNumber, int hours) {
        for (ParkingSpot spot : parkingSpots) {
            if (!spot.isEmpty() && spot.getVehicle().getVehicleNumber().equals(vehicleNumber)) {
                return spot.getPrice() * hours;
            }
        }
        return 0.0;
    }
}