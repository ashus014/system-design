package LLD.carRentalSystem.code;

import java.util.Date;

class Reservation {
    int reservationID;
    User user;
    Vehicle vehicle;
    Date bookingDate;
    Date bookedFrom;
    Date bookedTill;
    long fromTimeStamp;
    long toTimeStamp;
    Location pickupLocation;
    Location dropLocation;
    ReservationStatus obj;

    public Reservation(int reservationID, User user, Vehicle vehicle, Date bookingDate, Date bookedFrom,
                       Date bookedTill, long fromTimeStamp, long toTimeStamp,
                       Location pickupLocation, Location dropLocation, ReservationStatus obj) {
        this.reservationID = reservationID;
        this.user = user;
        this.vehicle = vehicle;
        this.bookingDate = bookingDate;
        this.bookedFrom = bookedFrom;
        this.bookedTill = bookedTill;
        this.fromTimeStamp = fromTimeStamp;
        this.toTimeStamp = toTimeStamp;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.obj = obj;
    }
}