package LLD.parkingLot.code;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingManagerFactory {
    private static final int DEFAULT_TWO_WHEELER_CAPACITY = 100;
    private static final int DEFAULT_FOUR_WHEELER_CAPACITY = 50;

    // Singleton pattern for managers to avoid creating new instances repeatedly
    private static final Map<VehicleType, ParkingSpotManager> managers = new ConcurrentHashMap<>();

    static {
        managers.put(VehicleType.TWO_WHEELER, new TwoWheelerManager(DEFAULT_TWO_WHEELER_CAPACITY));
        managers.put(VehicleType.FOUR_WHEELER, new FourWheelerManager(DEFAULT_FOUR_WHEELER_CAPACITY));
    }

    public static ParkingSpotManager getParkingManager(VehicleType vehicleType) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }

        ParkingSpotManager manager = managers.get(vehicleType);
        if (manager == null) {
            throw new IllegalArgumentException("No parking manager available for vehicle type: " + vehicleType);
        }

        return manager;
    }

    public static void resetManagers() {
        managers.clear();
        managers.put(VehicleType.TWO_WHEELER, new TwoWheelerManager(DEFAULT_TWO_WHEELER_CAPACITY));
        managers.put(VehicleType.FOUR_WHEELER, new FourWheelerManager(DEFAULT_FOUR_WHEELER_CAPACITY));
    }
}