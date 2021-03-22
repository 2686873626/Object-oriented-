package com.oocourse.uml3.interact.format;

import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InterfaceManager {
    private UmlInterface myInterface;
    private ArrayList<InterfaceManager> father = new ArrayList<>();
    private ArrayList<UmlAttribute> attributeList = new ArrayList<>();
    private ArrayList<OperationManager> operationManagers = new ArrayList<>();
    
    public InterfaceManager(UmlInterface umlInterface) {
        myInterface = umlInterface;
    }
    
    public void addFather(InterfaceManager father) {
        this.father.add(father);
    }
    
    public void addAttribute(UmlAttribute attribute) {
        attributeList.add(attribute);
    }
    
    public void addOperation(OperationManager operationManager) {
        operationManagers.add(operationManager);
    }
    
    public String getId() {
        return myInterface.getId();
    }
    
    public String  getName() {
        return myInterface.getName();
    }
    
    public ArrayList<InterfaceManager> getFather() {
        return father;
    }
    
    public UmlInterface getMyInterface() {
        return myInterface;
    }
    
    public void queryInterfaceList(
            HashMap<String, String> result) {
        result.put(getId(), getName());
        for (InterfaceManager travel : father) {
            if (!result.containsKey(travel.getId())) {
                travel.queryInterfaceList(result);
            }
        }
    }
    
    public boolean queryUml004(HashSet<String> arrival) {
        if (arrival.contains(myInterface.getId())) {
            return true;
        }
        arrival.add(myInterface.getId());
        for (InterfaceManager manager : father) {
            if (manager.queryUml004(arrival)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkForUml005() {
        if (myInterface.getName() == null) {
            return true;
        }
        for (UmlAttribute attribute : attributeList) {
            if (attribute.getName() == null) {
                return true;
            }
        }
        for (OperationManager operationManager : operationManagers) {
            if (operationManager.checkForUml005()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkForUml006() {
        for (UmlAttribute attribute : attributeList) {
            if (attribute.getVisibility() != Visibility.PUBLIC) {
                return true;
            }
        }
        return false;
    }
}
