import com.oocourse.TimableOutput;
import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class Elevator extends Thread {
    private Mission mission;
    private ArrayList<PersonRequest> request = new ArrayList<>();
    private int location;
    private boolean open;
    private String id;
    
    public Elevator(Mission mission, String id) {
        this.mission = mission;
        location = 1;
        open = false;
        this.id = id;
    }
    
    @Override
    public void run() {
        while (true) {
            mainRequestGet();
            if (request.size() == 0) { //no main work
                break;
            }
            minorRequestGet();
            Close();
            moveTo(request.get(0).getToFloor());
            completeRequest();
        }
    }
    
    private void mainRequestGet() {
        if (request.size() != 0) {
            return;
        }
        request = mission.get(location, request);
        if (request.size() == 0) { return; } //no main work
        PersonRequest mainRequest = request.get(0);
        while (location != mainRequest.getFromFloor()) {
            moveTo(mainRequest.getFromFloor());//get main passenger
        }
        Open();
        passengerIn(mainRequest);
    }
    
    private void minorRequestGet() {
        int size = request.size();
        request = mission.get(location, request);
        for (int i = size; i < request.size(); i++) {
            Open();
            passengerIn(request.get(i));
        }
    }
    
    private void completeRequest() {
        ArrayList<PersonRequest> temp = (ArrayList<PersonRequest>) request.clone();
        for (PersonRequest travel : temp) {
            if (location == travel.getToFloor()) {
                Open();
                passengerOut(travel);
                request.remove(travel);
            }
        }
        Close();
    }
    
    private void moveTo(int desFloor) {
        try {
            sleep(400);
        } catch (Exception e) {
            e.printStackTrace();
        }
        location += (desFloor > location) ? 1 : -1;
        if (location == 0) {
            location += (desFloor > location) ? 1 : -1;
        }
        TimableOutput.println("ARRIVE-" + location + "-" + id);
    }
    
    private void Open() {
        if (!open) {
            TimableOutput.println("OPEN-" + location + "-" + id);
            open = true;
        }
    }
    
    private void Close() {
        if (open) {
            try {
                sleep(400);
            } catch (Exception e) {
                e.printStackTrace();
            }
            TimableOutput.println("CLOSE-" + location + "-" + id);
            open = false;
        }
    }
    
    private void passengerIn(PersonRequest passenger) {
        TimableOutput.println("IN-" + passenger.getPersonId() + "-" + location + "-" + id);
    }
    
    private void passengerOut(PersonRequest passenger) {
        TimableOutput.println("OUT-" + passenger.getPersonId() + "-" + location + "-" + id);
    }
}
