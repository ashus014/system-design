package LLD.parkingLot;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ParkingSpotManager {
    protected final List<ParkingSpot> parkingSpots;
    protected final int totalSpots;
    protected final Map<Vehicle, ParkingSpot> vehicleToSpotMap;
    private final Object lock = new Object();

    public ParkingSpotManager(List<ParkingSpot> parkingSpots) {
        if (parkingSpots == null || parkingSpots.isEmpty()) {
            throw new IllegalArgumentException("Parking spots list cannot be null or empty");
        }
        this.parkingSpots = new ArrayList<>(parkingSpots);
        this.totalSpots = parkingSpots.size();
        this.vehicleToSpotMap = new ConcurrentHashMap<>();
    }

    public ParkingSpot findParkingSpace() {
        synchronized (lock) {
            return parkingSpots.stream()
                    .filter(ParkingSpot::isEmpty)
                    .findFirst()
                    .orElse(null);
        }
    }

    public boolean parkVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        synchronized (lock) {
            // Check if vehicle is already parked
            if (vehicleToSpotMap.containsKey(vehicle)) {
                throw new IllegalStateException("Vehicle is already parked");
            }

            ParkingSpot spot = findParkingSpace();
            if (spot == null) {
                return false;
            }

            try {
                spot.parkVehicle(vehicle);
                vehicleToSpotMap.put(vehicle, spot);
                return true;
            } catch (Exception e) {
                System.err.println("Failed to park vehicle: " + e.getMessage());
                return false;
            }
        }
    }

    public boolean removeVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        synchronized (lock) {
            ParkingSpot spot = vehicleToSpotMap.get(vehicle);
            if (spot == null) {
                return false;
            }

            try {
                spot.removeVehicle();
                vehicleToSpotMap.remove(vehicle);
                return true;
            } catch (Exception e) {
                System.err.println("Failed to remove vehicle: " + e.getMessage());
                return false;
            }
        }
    }

    public int getAvailableSpots() {
        synchronized (lock) {
            return (int) parkingSpots.stream()
                    .mapToInt(spot -> spot.isEmpty() ? 1 : 0)
                    .sum();
        }
    }

    public int getOccupiedSpots() {
        return totalSpots - getAvailableSpots();
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public ParkingSpot getSpotForVehicle(Vehicle vehicle) {
        return vehicleToSpotMap.get(vehicle);
    }

    public boolean isVehicleParked(Vehicle vehicle) {
        return vehicleToSpotMap.containsKey(vehicle);
    }

    protected abstract boolean isValidVehicleType(VehicleType vehicleType);
}