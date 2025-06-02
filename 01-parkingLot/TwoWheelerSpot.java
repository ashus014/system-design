public class TwoWheelerSpot extends ParkingSpot {

    public TwoWheelerSpot(int id) {
        setId(id);
        setEmpty(true);
        setPrice(10.0); // Base price for two-wheeler parking
    }

    @Override
    public void parkVehicle(Vehicle vehicle) {
        if (!vehicle.getVehicleType().equals("TWO_WHEELER")) {
            System.out.println("This spot is only for two-wheelers");
            return;
        }
        super.parkVehicle(vehicle);
    }
}