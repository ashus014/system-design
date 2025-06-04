package LLD.elevatorSystem.code;

class InternalButton {
    InternalButtonDispatcher dispatcher;

    void pressButton(int floor) {
        dispatcher.submitRequest(floor);
    }
}