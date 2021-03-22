package com.oocourse.uml3.interact.format;

import com.oocourse.uml3.models.elements.UmlEvent;
import com.oocourse.uml3.models.elements.UmlOpaqueBehavior;
import com.oocourse.uml3.models.elements.UmlTransition;

import java.util.ArrayList;

public class TransitionManager {
    private UmlTransition myTransition;
    private ArrayList<UmlOpaqueBehavior> behaviorList = new ArrayList<>();
    private ArrayList<UmlEvent> eventList = new ArrayList<>();
    
    public TransitionManager(UmlTransition transition) {
        myTransition = transition;
    }
    
    public void addEvent(UmlEvent event) {
        eventList.add(event);
    }
    
    public void addBehavior(UmlOpaqueBehavior behavior) {
        behaviorList.add(behavior);
    }
    
    public String getSource() {
        return myTransition.getSource();
    }
    
    public String getTarget() {
        return myTransition.getTarget();
    }
    
    public String getParentId() {
        return myTransition.getParentId();
    }
}
