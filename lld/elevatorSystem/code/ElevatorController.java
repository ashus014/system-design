package lld.elevatorSystem.code;

class ElevatorController {
    ElevatorCar car;

    void submitNewRequest(int floor, Direction direction) {
        car.addRequest(floor, direction);
    }

    void controlElevatorCar() {
        car.processRequests();
    }
}