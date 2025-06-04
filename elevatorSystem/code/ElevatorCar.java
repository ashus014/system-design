package elevatorSystem.code;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.TreeSet;

class ElevatorCar {
    int id;
    int currentFloor = 0;
    Display display = new Display();
    Status status = Status.IDLE;
    InternalButton internalButton;
    List<String> door; // placeholder
    TreeSet<Integer> upQueue = new TreeSet<>();
    TreeSet<Integer> downQueue = new TreeSet<>(Collections.reverseOrder());
    Queue<Integer> pendingQueue = new LinkedList<>();
    Direction currentDirection = Direction.UP;

    void addRequest(int floor, Direction direction) {
        if (direction == Direction.UP) {
            upQueue.add(floor);
        } else {
            downQueue.add(floor);
        }
    }

    void processRequests() {
        status = Status.MOVING;

        while (!upQueue.isEmpty() || !downQueue.isEmpty()) {
            if (currentDirection == Direction.UP) {
                while (!upQueue.isEmpty()) {
                    int floor = upQueue.pollFirst();
                    moveTo(floor, Direction.UP);
                }
                currentDirection = Direction.DOWN;
            } else {
                while (!downQueue.isEmpty()) {
                    int floor = downQueue.pollFirst();
                    moveTo(floor, Direction.DOWN);
                }
                currentDirection = Direction.UP;
            }
        }

        status = Status.IDLE;
    }

    void moveTo(int destinationFloor, Direction direction) {
        System.out.println("Elevator moving " + direction + " to floor: " + destinationFloor);
        currentFloor = destinationFloor;
        display.floor = currentFloor;
        display.direction = direction;
        // Simulate door open/close, etc.
    }
}
