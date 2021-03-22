import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;

public class Mission {
    private ArrayList<PersonRequest> mission = new ArrayList<>();
    private volatile boolean end = false;
    private final Object lock = new Object();
    
    public void put(PersonRequest request) {
        synchronized (lock) {
            mission.add(request);
            lock.notifyAll();
        }
    }
    
    public ArrayList<PersonRequest> get(
            int location, ArrayList<PersonRequest> request) {
        synchronized (lock) {
            ArrayList<PersonRequest> temp = request;
            while (temp.size() == 0 && mission.size() == 0 && !end) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (request.size() == 0) { //get main request
                if (mission.size() == 0) {
                    return temp;
                }
                PersonRequest mark = null;
                int min = 20;
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
                    if (temp.size() == 7) {
                        break;
                    }
                    boolean dec = ((travel.getToFloor() - location) *
                            (request.get(0).getToFloor() - location)) > 0;
                    if (travel.getFromFloor() == location && dec) {
                        temp.add(travel);
                        mission.remove(travel);
                    }
                }
            }
            lock.notifyAll();
            return temp;
        }
    }
    
    public void setEnd() {
        synchronized (lock) {
            this.end = true;
            lock.notifyAll();
        }
    }
}
