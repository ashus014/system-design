package elevatorSystem.code;

import java.util.Arrays;

public class ElevatorSystemLOOK {
    public static void main(String[] args) {
        ElevatorCar car = new ElevatorCar();
        ElevatorController controller = new ElevatorController();
        controller.car = car;

        ExternalButtonDispatcher ebd = new ExternalButtonDispatcher();
        ebd.controllers = Arrays.asList(controller);

        InternalButtonDispatcher ibd = new InternalButtonDispatcher();
        ibd.controllers = Arrays.asList(controller);

        // Simulate button presses
        ExternalButton externalButton = new ExternalButton();
        externalButton.dispatcher = ebd;

        externalButton.pressButton(3, Direction.UP);
        externalButton.pressButton(5, Direction.UP);
        externalButton.pressButton(2, Direction.DOWN);

        controller.controlElevatorCar();
    }
}