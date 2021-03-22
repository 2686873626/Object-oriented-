package com.oocourse.uml2.interact.format;

import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlStateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MachineManager {
    
    private UmlStateMachine myMachine;
    private UmlRegion myRegion;
    private HashMap<String, ArrayList<String>> stateName = new HashMap<>();
    private HashMap<String, UmlElement> stateId = new HashMap<>();
    private HashMap<String, ArrayList<String>> path = new HashMap<>();
    private ArrayList<TransitionManager> transitions = new ArrayList<>();
    
    public MachineManager(UmlStateMachine machine) {
        myMachine = machine;
    }
    
    public void generateRegion(UmlRegion region) {
        myRegion = region;
    }
    
    public void addState(UmlElement stateManager) {
        if (stateName.containsKey(stateManager.getName())) {
            stateName.get(stateManager.getName()).add(stateManager.getId());
        } else {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(stateManager.getId());
            stateName.put(stateManager.getName(), temp);
        }
        stateId.put(stateManager.getId(), stateManager);
    }
    
    public int queryStateCount() {
        return stateId.size();
    }
    
    public void addTransition(TransitionManager transitionManager) {
        String source = transitionManager.getSource();
        String target = transitionManager.getTarget();
        if (path.containsKey(source)) {
            path.get(source).add(target);
        } else {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(target);
            path.put(source, temp);
        }
        transitions.add(transitionManager);
    }
    
    public int queryTransitionCount() {
        return transitions.size();
    }
    
    public int querySubsequentStateCount(String machineName, String stateName)
        throws StateNotFoundException, StateDuplicatedException {
        if (!this.stateName.containsKey(stateName)) {
            throw new StateNotFoundException(machineName, stateName);
        }
        ArrayList<String> temp = this.stateName.get(stateName);
        if (temp.size() > 1) {
            throw new StateDuplicatedException(machineName, stateName);
        }
        HashSet<String> arrival = new HashSet<>();
        ArrayList<String> queue = new ArrayList<>();
        queue.add(temp.get(0));
        while (queue.size() != 0) {
            String id = queue.get(0);
            queue.remove(0);
            if (!path.containsKey(id)) {
                continue;
            }
            for (String canGet : path.get(id)) {
                if (!arrival.contains(canGet)) {
                    arrival.add(canGet);
                    queue.add(canGet);
                }
            }
        }
        return arrival.size();
    }
}
