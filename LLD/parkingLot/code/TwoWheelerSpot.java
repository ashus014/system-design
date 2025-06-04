package LLD.parkingLot.code;

public class TwoWheelerSpot extends ParkingSpot {
    private static final double TWO_WHEELER_RATE = 10.0;

    public TwoWheelerSpot(int id) {
        super(id, TWO_WHEELER_RATE);
    }

    @Override
    protected boolean isValidVehicleType(VehicleType vehicleType) {
        return vehicleType == VehicleType.TWO_WHEELER;
    }
}
