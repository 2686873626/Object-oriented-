import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

import java.io.IOException;

public class Producer extends Thread {
    private Mission mission;
    private volatile boolean con = true;
    
    public Producer(Mission mission) {
        this.mission = mission;
    }
    
    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                mission.setEnd();
                break;
            } else {
                // a new valid request
                if (request instanceof PersonRequest) {
                    mission.put((PersonRequest) request);
                } else {
                    eleProduce((ElevatorRequest) request);
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void eleProduce(ElevatorRequest request) {
        Elevator elevator = new Elevator(
                mission, request.getElevatorId(), request.getElevatorType());
        elevator.start();
    }
}

