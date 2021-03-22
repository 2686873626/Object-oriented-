import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.PersonRequest;

import java.io.IOException;

public class Request extends Thread {
    private Mission mission;
    private volatile boolean con = true;
    private ElevatorInput elevatorInput;
    
    public Request(Mission mission, ElevatorInput elevatorInput) {
        this.mission = mission;
        this.elevatorInput = elevatorInput;
    }
    
    @Override
    public void run() {
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                mission.setEnd();
                break;
            } else {
                // a new valid request
                mission.put(request);
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

