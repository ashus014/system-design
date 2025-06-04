package LLD.elevatorSystem.code;

import java.util.List;

class ExternalButtonDispatcher {
    List<ElevatorController> controllers;

    void submitRequest(int floor, Direction direction) {
        // Basic selection strategy for demo
        controllers.get(0).submitNewRequest(floor, direction);
    }
}