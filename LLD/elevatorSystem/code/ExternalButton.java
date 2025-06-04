package LLD.elevatorSystem.code;

class ExternalButton {
    ExternalButtonDispatcher dispatcher;

    void pressButton(int floor, Direction direction) {
        dispatcher.submitRequest(floor, direction);
    }
}