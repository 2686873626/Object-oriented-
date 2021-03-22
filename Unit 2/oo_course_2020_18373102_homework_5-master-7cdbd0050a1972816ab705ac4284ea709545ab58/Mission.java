import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Mission {
    private ArrayList<PersonRequest> mission = new ArrayList<>();
    private volatile boolean end = false;
    
    public synchronized void put(PersonRequest request) {
        mission.add(request);
        notifyAll();
    }
    
    public synchronized ArrayList<PersonRequest> get(
            int location, ArrayList<PersonRequest> request) {
        ArrayList<PersonRequest> temp = request;
        if (temp.size() == 0 && mission.size() == 0  && !end) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (request.size() == 0) { //get main request
            if (mission.size() == 0) { return temp; }
            PersonRequest mark = null;
            int min = 15;
            for (PersonRequest travel : mission) {
                if (Math.abs(travel.getFromFloor() - location) < min) {
                    min = Math.abs(travel.getFromFloor() - location);
                    mark = travel;
                }
            }
            temp.add(mark);
            mission.remove(mark);
        } else {
            ArrayList<PersonRequest> tmp = (ArrayList<PersonRequest>) mission.clone();
            for (PersonRequest travel : tmp) {
                boolean dec = ((travel.getToFloor() - location) *
                        (request.get(0).getToFloor() - location)) > 0;
                if (travel.getFromFloor() == location && dec) {
                    temp.add(travel);
                    mission.remove(travel);
                }
            }
        }
        notifyAll();
        return temp;
    }
    
    public synchronized void setEnd() {
        this.end = true;
        notifyAll();
    }
}
