package LLD.parkingLot.code;

public class ParkingLotDemo {

    public static void main(String[] args) {

        EntranceGate entranceGate = new EntranceGate();
        ExitGate exitGate = new ExitGate();

        Vehicle car1 = new Vehicle("DL1ABC1234", VehicleType.FOUR_WHEELER);
        Vehicle bike1 = new Vehicle("MH1XYZ5678", VehicleType.TWO_WHEELER);
        Vehicle car2 = new Vehicle("KA1DEF9012", VehicleType.FOUR_WHEELER);

        Ticket car1Ticket = entranceGate.parkVehicle(car1);
        System.out.println("Car 1 parked at spot: " + car1Ticket.getParkingSpot().getId());

        Ticket bike1Ticket = entranceGate.parkVehicle(bike1);
        System.out.println("Bike 1 parked at spot: " + bike1Ticket.getParkingSpot().getId());

        Ticket car2Ticket = entranceGate.parkVehicle(car2);
        System.out.println("Car 2 parked at spot: " + car2Ticket.getParkingSpot().getId());

        ParkingSpotManager twoWheelerManager =
                ParkingManagerFactory.getParkingManager(VehicleType.TWO_WHEELER);

        ParkingSpotManager fourWheelerManager =
                ParkingManagerFactory.getParkingManager(VehicleType.FOUR_WHEELER);

        System.out.println("Available bike spots: " + twoWheelerManager.getAvailableSpots());
        System.out.println("Available car spots: " + fourWheelerManager.getAvailableSpots());

        try {
            System.out.println("Waiting for 2 hours...");
            Thread.sleep(2000); // Simulate 2 hours parking (in real code use proper time handling)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Available car spots : " + fourWheelerManager.getAvailableSpots());

        // Process exit for car1
        double car1Fee = exitGate.calculateFee(car1Ticket);
        System.out.println("Car 1 fee: ₹" + car1Fee);
        exitGate.processPayment(car1Ticket, car1Fee); // Process payment
        exitGate.removeVehicle(car1Ticket); // Free up the spot
        System.out.println("Car 1 has exited");

        Vehicle car3 = new Vehicle("DLasdBC1234", VehicleType.FOUR_WHEELER);
        Ticket car3Ticket = entranceGate.parkVehicle(car3);
        System.out.println("Car 3 parked at spot: " + car3Ticket.getParkingSpot().getId());


        // Check available spots again
        System.out.println("Available car spots after exit: " + fourWheelerManager.getAvailableSpots());

    }
}
