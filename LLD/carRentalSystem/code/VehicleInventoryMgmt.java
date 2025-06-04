package LLD.carRentalSystem.code;

import java.util.ArrayList;
import java.util.List;

class VehicleInventoryMgmt {
    List<Vehicle> list = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) {
        list.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        list.remove(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return list;
    }
}
