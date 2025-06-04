package parkingLot;

import java.time.LocalDateTime;
import java.util.Objects;

public class Ticket {
    private final String ticketId;
    private final LocalDateTime entryTime;
    private final Vehicle vehicle;
    private final ParkingSpot parkingSpot;

    public Ticket(Vehicle vehicle, ParkingSpot parkingSpot) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (parkingSpot == null) {
            throw new IllegalArgumentException("Parking spot cannot be null");
        }

        this.ticketId = generateTicketId(vehicle, parkingSpot);
        this.entryTime = LocalDateTime.now();
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
    }

    private String generateTicketId(Vehicle vehicle, ParkingSpot spot) {
        return vehicle.getVehicleNumber() + "_" + spot.getId() + "_" + System.currentTimeMillis();
    }

    public String getTicketId() {
        return ticketId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Ticket ticket = (Ticket) obj;
        return Objects.equals(ticketId, ticket.ticketId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId='" + ticketId + '\'' +
                ", entryTime=" + entryTime +
                ", vehicle=" + vehicle +
                ", spotId=" + parkingSpot.getId() +
                '}';
    }
}