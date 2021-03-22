import com.oocourse.TimableOutput;
import com.oocourse.elevator2.ElevatorInput;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] id = {"A", "B", "C", "D", "E"};
        TimableOutput.initStartTimestamp();
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        int elevatorNum = elevatorInput.getElevatorNum();
        Mission  mission  = new Mission();
        Request  reporter = new Request(mission, elevatorInput);
        reporter.start();
        for (int i = 0; i < elevatorNum; i++) {
            Elevator elevator = new Elevator(mission, id[i]);
            elevator.start();
        }
    }
}
