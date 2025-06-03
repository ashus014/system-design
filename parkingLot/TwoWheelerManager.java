package parkingLot;

import java.util.ArrayList;
import java.util.List;

public class TwoWheelerManager extends ParkingSpotManager {
    public TwoWheelerManager(int capacity) {
        super(createParkingSpots(capacity));
    }

    private static List<ParkingSpot> createParkingSpots(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }

        List<ParkingSpot> spots = new ArrayList<>();
        for (int i = 1; i <= capacity; i++) {
            spots.add(new TwoWheelerSpot(i));
        }
        return spots;
    }

    @Override
    public boolean parkVehicle(Vehicle vehicle) {
        if (!isValidVehicleType(vehicle.getVehicleType())) {
            throw new IllegalArgumentException("Invalid vehicle type for two-wheeler parking");
        }
        return super.parkVehicle(vehicle);
    }

    @Override
    protected boolean isValidVehicleType(VehicleType vehicleType) {
        return vehicleType == VehicleType.TWO_WHEELER;
    }
}