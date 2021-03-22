import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        String[] id = {"A", "B", "C"};
        Mission mission = new Mission();
        Producer producer = new Producer(mission);
        producer.start();
        for (int i = 0; i < 3; i++) {
            Elevator elevator = new Elevator(mission, id[i], id[i]);
            elevator.start();
        }
    }
}