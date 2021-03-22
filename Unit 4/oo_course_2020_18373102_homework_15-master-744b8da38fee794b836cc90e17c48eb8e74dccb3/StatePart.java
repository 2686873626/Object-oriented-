package com.oocourse.uml3.interact.format;

import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlFinalState;
import com.oocourse.uml3.models.elements.UmlOpaqueBehavior;
import com.oocourse.uml3.models.elements.UmlPseudostate;
import com.oocourse.uml3.models.elements.UmlRegion;
import com.oocourse.uml3.models.elements.UmlState;
import com.oocourse.uml3.models.elements.UmlStateMachine;
import com.oocourse.uml3.models.elements.UmlTransition;
import java.util.ArrayList;
import java.util.HashMap;

public class StatePart {
    private HashMap<String, ArrayList<String>> machineName = new HashMap<>();
    private HashMap<String, MachineManager> machineId = new HashMap<>();
    private ArrayList<UmlState> stateList = new ArrayList<>();
    private ArrayList<UmlRegion> regionList = new ArrayList<>();
    private ArrayList<UmlEvent> eventList = new ArrayList<>();
    private HashMap<String, TransitionManager> transitionId = new HashMap<>();
    private ArrayList<UmlOpaqueBehavior> behaviorList = new ArrayList<>();
    private ArrayList<UmlFinalState> finalStateList = new ArrayList<>();
    private ArrayList<UmlPseudostate> pseudostateList = new ArrayList<>();
    
    public void addElement(UmlElement element) {
        if (element instanceof UmlStateMachine) {
            initMachine(element);
        } else if (element instanceof UmlState) {
            stateList.add((UmlState) element);
        } else if (element instanceof UmlFinalState) {
            finalStateList.add((UmlFinalState) element);
        } else if (element instanceof UmlPseudostate) {
            pseudostateList.add((UmlPseudostate) element);
        } else if (element instanceof UmlRegion) {
            regionList.add((UmlRegion) element);
        } else if (element instanceof UmlEvent) {
            eventList.add((UmlEvent) element);
        } else if (element instanceof UmlTransition) {
            transitionId.put(element.getId(), new TransitionManager((UmlTransition) element));
        } else if (element instanceof UmlOpaqueBehavior) {
            behaviorList.add((UmlOpaqueBehavior) element);
        }
    }
    
    private void initMachine(UmlElement element) {
        String name = element.getName();
        String id = element.getId();
        if (machineName.containsKey(name)) {
            machineName.get(name).add(id);
        } else {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(id);
            machineName.put(name, temp);
        }
        machineId.put(id, new MachineManager((UmlStateMachine) element));
    }
    
    public void fillState() {
        generateRegion();
    }
    
    private void generateRegion() {
        HashMap<String, String> translate = new HashMap<>();//region -> machine
        for (UmlRegion region : regionList) {
            String parent = region.getParentId();
            translate.put(region.getId(), parent);
            machineId.get(parent).generateRegion(region);
        }
        for (UmlOpaqueBehavior behavior : behaviorList) {
            transitionId.get(behavior.getParentId()).addBehavior(behavior);
        }
        for (UmlEvent event : eventList) {
            transitionId.get(event.getParentId()).addEvent(event);
        }
        for (UmlState element : stateList) {
            machineId.get(translate.get(element.getParentId())).addState(element);
        }
        for (UmlPseudostate pseudostate : pseudostateList) {
            machineId.get(translate.get(pseudostate.getParentId())).setPseudoState(pseudostate);
        }
        for (UmlFinalState finalState : finalStateList) {
            machineId.get(translate.get(finalState.getParentId())).setFinalState(finalState);
        }
        for (TransitionManager transitionManager : transitionId.values()) {
            machineId.get(translate.get(transitionManager.getParentId()))
                    .addTransition(transitionManager);
        }
    }
    
    private MachineManager searchMachine(String machineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!this.machineName.containsKey(machineName)) {
            throw new StateMachineNotFoundException(machineName);
        }
        ArrayList<String> list = this.machineName.get(machineName);
        if (list.size() > 1) {
            throw new StateMachineDuplicatedException(machineName);
        }
        return machineId.get(list.get(0));
    }
    
    public int getStateCount(String stateMachineName)
            throws StateMachineDuplicatedException, StateMachineNotFoundException {
        MachineManager machineManager = searchMachine(stateMachineName);
        return machineManager.queryStateCount();
    }
    
    public int getTransitionCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        MachineManager machineManager = searchMachine(stateMachineName);
        return machineManager.queryTransitionCount();
    }
    
    public int getSubsequentStateCount(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        MachineManager machineManager = searchMachine(stateMachineName);
        return machineManager.querySubsequentStateCount(stateMachineName, stateName);
    }
    
    public void checkForUml007() throws UmlRule007Exception {
        for (MachineManager machineManager : machineId.values()) {
            machineManager.checkForUml007();
        }
    }
    
    public void checkForUml008() throws UmlRule008Exception {
        for (MachineManager machineManager : machineId.values()) {
            machineManager.checkForUml008();
        }
    }
}
