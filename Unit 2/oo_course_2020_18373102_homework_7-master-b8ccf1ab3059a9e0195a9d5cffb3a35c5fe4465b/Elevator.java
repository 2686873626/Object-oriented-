import com.oocourse.TimableOutput;
import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class Elevator extends Thread {
    private Mission mission;
    private ArrayList<PersonRequest> request = new ArrayList<>();
    private int location;//floor of elevator
    private boolean open;//the status of door
    private String id;
    private String type;//A,B,C
    
    public Elevator(Mission mission, String id, String type) {
        this.mission = mission;
        location = 1;
        open = false;
        this.id = id;
        this.type = type;
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
            int des = request.get(0).getToFloor();
            switch (type) {
                case "A":
                    break;
                case "B":
                    if (des == 3) {
                        if (location > 3) {
                            des = 5;
                        } else {
                            des = 1;
                        }
                    }
                    break;
                default:
                    if (des > 1 && des < 15 && des % 2 == 0) {
                        if (location > 3) {
                            des = 5;
                        } else {
                            des = 1;
                        }
                    }
            }
            moveTo(des);
            completeRequest();
        }
    }
    
    private void mainRequestGet() {
        if (request.size() != 0) {
            return;
        }
        request = mission.get(location, request, type);
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
        request = mission.get(location, request, type);
        for (int i = size; i < request.size(); i++) {
            Open();
            passengerIn(request.get(i));
        }
    }
    
    private void completeRequest() {
        ArrayList<PersonRequest> temp = (ArrayList<PersonRequest>) request.clone();
        for (PersonRequest travel : temp) {
            boolean transfer;
            int des = travel.getToFloor();
            switch (type) {
                case "A":
                    transfer = (location == 15 || location == 1) && des > 1 && des < 15;
                    break;
                case "B":
                    transfer = ((location == 1 || location == 5) && des == 3) ||
                            (location == 15 && des > 15) || (location == 1 && des == -3);
                    break;
                default:
                    boolean part = (location == 1 || location == 5)
                            && des > 1 && des < 15 && (des % 2 == 0);
                    transfer = (location == 1 && des < 1) || (location == 15 && des > 15) || part;
            }
            if (location == des) {
                Open();
                passengerOut(travel);
                request.remove(travel);
                mission.finish();
            } else if (transfer) {
                Open();
                passengerOut(travel);
                mission.put(new PersonRequest(location, travel.getToFloor(), travel.getPersonId()));
                mission.finish();
                request.remove(travel);
            }
        }
        Close();
    }
    
    private void moveTo(int desFloor) {
        int time = 0;
        switch (type) {
            case "A":
                time = 400;
                break;
            case "B":
                time = 500;
                break;
            default:
                time = 600;
        }
        boolean notStop;
        int up = desFloor > location ? 1 : -1;
        do {
            try {
                sleep(time);
            } catch (Exception e) {
                e.printStackTrace();
            }
            location += up;
            if (location == 0) {
                location += up;
            }
            TimableOutput.println("ARRIVE-" + location + "-" + id);
            switch (type) {
                case "A":
                    notStop = location > 1 && location < 15;
                    break;
                case "B":
                    notStop = location == 3;
                    break;
                default:
                    notStop = location > 1 && location < 15 && (location % 2 == 0);;
            }
        } while (notStop);
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
