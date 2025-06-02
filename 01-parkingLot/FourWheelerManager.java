import java.util.ArrayList;
import java.util.List;

public class FourWheelerManager extends ParkingSpotManager {
    private int totalSpots;
    private int occupiedSpots;

    public FourWheelerManager(int totalSpots) {
        super(createParkingSpots(totalSpots));
        this.totalSpots = totalSpots;
        this.occupiedSpots = 0;
    }

    private static List<ParkingSpot> createParkingSpots(int totalSpots) {
        List<ParkingSpot> spots = new ArrayList<>();
        for (int i = 1; i <= totalSpots; i++) {
            spots.add(new FourWheelerSpot(i));
        }
        return spots;
    }

    public boolean parkVehicle(Vehicle vehicle) {
        if (!vehicle.getVehicleType().equals("FOUR_WHEELER")) {
            System.out.println("Invalid vehicle type for four-wheeler parking");
            return false;
        }

        if (occupiedSpots >= totalSpots) {
            System.out.println("Sorry, four-wheeler parking is full");
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
        System.out.println("Vehicle not found in four-wheeler parking");
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