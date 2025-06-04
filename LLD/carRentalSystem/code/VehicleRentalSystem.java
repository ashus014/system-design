package LLD.carRentalSystem.code;

import java.util.ArrayList;
import java.util.List;

class VehicleRentalSystem {
    List<User> users = new ArrayList<>();
    List<Store> stores = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
    }

    public void addStore(Store store) {
        stores.add(store);
    }
}