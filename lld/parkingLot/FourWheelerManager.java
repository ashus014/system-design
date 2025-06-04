package lld.parkingLot;

import java.util.ArrayList;
import java.util.List;

public class FourWheelerManager extends ParkingSpotManager {
    public FourWheelerManager(int capacity) {
        super(createParkingSpots(capacity));
    }

    private static List<ParkingSpot> createParkingSpots(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }

        List<ParkingSpot> spots = new ArrayList<>();
        for (int i = 1; i <= capacity; i++) {
            spots.add(new FourWheelerSpot(i));
        }
        return spots;
    }

    @Override
    public boolean parkVehicle(Vehicle vehicle) {
        if (!isValidVehicleType(vehicle.getVehicleType())) {
            throw new IllegalArgumentException("Invalid vehicle type for four-wheeler parking");
        }
        return super.parkVehicle(vehicle);
    }

    @Override
    protected boolean isValidVehicleType(VehicleType vehicleType) {
        return vehicleType == VehicleType.FOUR_WHEELER;
    }
}