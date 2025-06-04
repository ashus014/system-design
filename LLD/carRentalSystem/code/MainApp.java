package LLD.carRentalSystem.code;

import java.util.Date;

public class MainApp {
    public static void main(String[] args) {
        VehicleRentalSystem system = new VehicleRentalSystem();

        // Create user
        User user = new User(1, "Ashu", "DL12345");
        system.addUser(user);

        // Create vehicle
        Vehicle vehicle = new Vehicle(101, 1001, VehicleType.CAR, "Toyota", "Corolla", 50000,
                new Date(), 15, 1800, 1000, 100, 4, Status.ACTIVE);

        // Create location and store
        Location loc = new Location("MG Road", "Pune", "Maharashtra", 411001);
        VehicleInventoryMgmt inventory = new VehicleInventoryMgmt();
        inventory.addVehicle(vehicle);
        Store store = new Store(1, inventory, loc);
        system.addStore(store);

        // Book a reservation
        Reservation reservation = new Reservation(1, user, vehicle, new Date(), new Date(),
                new Date(System.currentTimeMillis() + 86400000),
                System.currentTimeMillis(), System.currentTimeMillis() + 86400000,
                loc, loc, ReservationStatus.SCHEDULED);

        store.reservations.add(reservation);

        // Create a bill
        Bill bill = new Bill(reservation, false, 1000);

        // Pay the bill
        Payment payment = new Payment(bill);
        payment.payBill();

        System.out.println("User " + user.userName + " booked vehicle " + vehicle.modelName +
                " and payment status isPaid = " + bill.isPaid);
    }
}