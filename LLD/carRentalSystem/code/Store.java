package LLD.carRentalSystem.code;

import java.util.ArrayList;
import java.util.List;

class Store {
    int storeID;
    VehicleInventoryMgmt vehicleInventoryMgmt;
    Location location;
    List<Reservation> reservations = new ArrayList<>();

    public Store(int storeID, VehicleInventoryMgmt vehicleInventoryMgmt, Location location) {
        this.storeID = storeID;
        this.vehicleInventoryMgmt = vehicleInventoryMgmt;
        this.location = location;
    }
}
