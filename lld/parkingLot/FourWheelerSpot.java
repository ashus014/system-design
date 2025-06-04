package lld.parkingLot;

public class FourWheelerSpot extends ParkingSpot {
    private static final double FOUR_WHEELER_RATE = 20.0;

    public FourWheelerSpot(int id) {
        super(id, FOUR_WHEELER_RATE);
    }

    @Override
    protected boolean isValidVehicleType(VehicleType vehicleType) {
        return vehicleType == VehicleType.FOUR_WHEELER;
    }
}