package com.oocourse.uml3.interact.format;

import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlState;
import com.oocourse.uml3.models.elements.UmlStateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MachineManager {
    
    private UmlStateMachine myMachine;
    private UmlRegion myRegion;
    private UmlPseudostate pseudostate;
    private int ifPse = 0;
    private UmlFinalState finalState;
    private int ifFin = 0;
    private HashMap<String, ArrayList<String>> stateName = new HashMap<>();
    private HashMap<String, UmlState> stateId = new HashMap<>();
    private HashMap<String, ArrayList<String>> path = new HashMap<>();
    private ArrayList<TransitionManager> transitions = new ArrayList<>();
    
    public MachineManager(UmlStateMachine machine) {
        myMachine = machine;
    }
    
    public void generateRegion(UmlRegion region) {
        myRegion = region;
    }
    
    public void addState(UmlState stateManager) {
        if (stateName.containsKey(stateManager.getName())) {
            stateName.get(stateManager.getName()).add(stateManager.getId());
        } else {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(stateManager.getId());
            stateName.put(stateManager.getName(), temp);
        }
        stateId.put(stateManager.getId(), stateManager);
    }
    
    public void setPseudoState(UmlPseudostate pseudoState) {
        this.pseudostate = pseudoState;
        ifPse = 1;
    }
    
    public void setFinalState(UmlFinalState finalState) {
        this.finalState = finalState;
        ifFin = 1;
    }
    
    public int queryStateCount() {
        return stateId.size() + ifPse + ifFin;
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
    
    public void checkForUml007() throws UmlRule007Exception {
        if (ifFin == 1 && path.containsKey(finalState.getId())) {
            throw new UmlRule007Exception();
        }
    }
    
    public void checkForUml008() throws UmlRule008Exception {
        if (ifPse == 0) {
            return;
        }
        String firstId = pseudostate.getId();
        if (path.containsKey(firstId) && path.get(firstId).size() > 1) {
            throw new UmlRule008Exception();
        }
    }
   
}
