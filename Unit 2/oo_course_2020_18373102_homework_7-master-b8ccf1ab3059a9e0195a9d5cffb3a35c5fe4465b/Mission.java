import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class Mission {
    private ArrayList<PersonRequest> mission = new ArrayList<>();
    private volatile boolean end = false;
    private final Object lock = new Object();
    private static volatile int task = 0;
    
    public void put(PersonRequest request) {
        synchronized (lock) {
            mission.add(request);
            lock.notifyAll();
            task++;
        }
    }
    
    public ArrayList<PersonRequest> get(
            int location, ArrayList<PersonRequest> request, String type) {
        synchronized (lock) {
            ArrayList<PersonRequest> temp = request;
            if (request.size() != 0) {
                temp = giveMinor(location, request, type);
                lock.notifyAll();
                return temp;
            }
            while (mission.size() != 0 || task != 0 || !end) {
                for (PersonRequest travel : mission) {
                    boolean flag;
                    int des = travel.getToFloor();
                    int from = travel.getFromFloor();
                    switch (type) {
                        case "A":
                            flag = (from == 1 || from == 15) && des > 1 && des < 15;
                            flag = flag || (from > 1 && from < 15);
                            break;
                        case "B":
                            flag = ((from == 1 || from == 5) && des == 3) ||
                                    (from == 15 && des > 15) || (from <= 1 && des == -3);
                            flag = flag || from == -3 || from == 3 || from > 15;
                            break;
                        default:
                            boolean part = from != 3 && des > 1 && des < 15 && (des % 2 == 0);
                            flag = (from == 1 && des < 1) ||
                                    (from == 15 && des > 15) || part;
                            flag = flag || (from > 1 && from < 15 && from % 2 == 0) ||
                                    from < 1 || from > 15;
                    }
                    if (!flag) {
                        temp.add(travel);
                        mission.remove(travel);
                        break;
                    }
                }
                if (temp.size() == 0) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    lock.notifyAll();
                    return temp;
                }
            }
            lock.notifyAll();
            return temp;
        }
    }
    
    private ArrayList<PersonRequest> giveMinor(
            int location, ArrayList<PersonRequest> request, String type) {
        ArrayList<PersonRequest> temp = request;
        ArrayList<PersonRequest> tmp = (ArrayList<PersonRequest>) mission.clone();
        int size = type.equals("A") ? 6 :
                (type.equals("B") ? 8 : 7);
        for (PersonRequest travel : tmp) {
            if (temp.size() == size) {
                break;
            }
            boolean dec = ((travel.getToFloor() - location) *
                    (request.get(0).getToFloor() - location)) > 0;
            boolean flag;
            int des = travel.getToFloor();
            switch (type) {
                case "A" :
                    flag = (location == 1 || location == 15) && des > 1 && des < 15;
                    break;
                case "B" :
                    flag = ((location == 1 || location == 5) && des == 3) ||
                            (location == 15 && des > 15) || (location <= 1 && des == -3);
                    break;
                default:
                    boolean part = location != 3 && des > 1 && des < 15 && (des % 2 == 0);
                    flag = (location == 1 && des < 1) || (location == 15 && des > 15) || part;
            }
            if (travel.getFromFloor() == location && dec && !flag) {
                temp.add(travel);
                mission.remove(travel);
            }
        }
        return temp;
    }
    
    public void setEnd() {
        synchronized (lock) {
            this.end = true;
            lock.notifyAll();
        }
    }
    
    public void finish() {
        task--;
    }
}
