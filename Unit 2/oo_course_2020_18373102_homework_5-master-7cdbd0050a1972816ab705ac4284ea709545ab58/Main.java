import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) throws Exception {
        TimableOutput.initStartTimestamp();
        Mission  mission  = new Mission();
        Request  reporter = new Request(mission);
        Elevator elevator = new Elevator(mission);
        reporter.start();
        elevator.start();
    }
}
