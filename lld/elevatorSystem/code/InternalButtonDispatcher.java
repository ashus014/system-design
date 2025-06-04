package lld.elevatorSystem.code;

import java.util.List;

class InternalButtonDispatcher {
    List<ElevatorController> controllers;

    void submitRequest(int floor) {
        // Assuming direction UP as placeholder
        controllers.get(0).submitNewRequest(floor, Direction.UP);
    }
}