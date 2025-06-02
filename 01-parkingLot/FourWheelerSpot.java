public class FourWheelerSpot extends ParkingSpot {

    public FourWheelerSpot(int id) {
        setId(id);
        setEmpty(true);
        setPrice(20.0); // Base price for four-wheeler parking
    }

    @Override
    public void parkVehicle(Vehicle vehicle) {
        if (!vehicle.getVehicleType().equals("FOUR_WHEELER")) {
            System.out.println("This spot is only for four-wheelers");
            return;
        }
        super.parkVehicle(vehicle);
    }
}