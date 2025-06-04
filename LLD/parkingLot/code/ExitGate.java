package LLD.parkingLot.code;

import java.time.Duration;
import java.time.LocalDateTime;

public class ExitGate {

    public double calculateFee(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        LocalDateTime exitTime = LocalDateTime.now();
        Duration duration = Duration.between(ticket.getEntryTime(), exitTime);

        // Calculate hours, rounding up partial hours
        long totalMinutes = duration.toMinutes();
        long hours = (totalMinutes + 59) / 60; // Ceiling division

        if (hours < 1) {
            hours = 1; // Minimum 1 hour charge
        }

        ParkingSpot spot = ticket.getParkingSpot();
        double fee = spot.getHourlyRate() * hours;

        System.out.println("Parking duration: " + totalMinutes + " minutes (" + hours + " hours)");
        System.out.println("Hourly rate: ₹" + spot.getHourlyRate());
        System.out.println("Total fee: ₹" + fee);

        return fee;
    }

    public void processPayment(Ticket ticket, double amount) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Payment amount cannot be negative");
        }

        double fee = calculateFee(ticket);
        if (amount < fee) {
            throw new IllegalArgumentException("Insufficient payment. Required: ₹" + fee + ", Provided: ₹" + amount);
        }

        // Process payment logic here
        double change = amount - fee;
        System.out.println("Payment processed successfully. Change: ₹" + change);
    }

    public void removeVehicle(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        try {
            Vehicle vehicle = ticket.getVehicle();
            ParkingSpotManager manager = ParkingManagerFactory.getParkingManager(vehicle.getVehicleType());

            if (!manager.removeVehicle(vehicle)) {
                throw new IllegalStateException("Failed to remove vehicle - vehicle not found");
            }

            System.out.println("Vehicle " + vehicle.getVehicleNumber() + " successfully removed from parking");

        } catch (Exception e) {
            System.err.println("Error removing vehicle: " + e.getMessage());
            throw e;
        }
    }

    public void processExit(Ticket ticket, double payment) {
        processPayment(ticket, payment);
        removeVehicle(ticket);
    }
}