package com.oocourse.uml2.interact.format;

import com.oocourse.uml2.models.elements.UmlInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class InterfaceManager {
    private UmlInterface myInterface;
    private ArrayList<InterfaceManager> father = new ArrayList<>();
    
    public InterfaceManager(UmlInterface umlInterface) {
        myInterface = umlInterface;
    }
    
    public void addFather(InterfaceManager father) {
        this.father.add(father);
    }
    
    public String getId() {
        return myInterface.getId();
    }
    
    public String  getName() {
        return myInterface.getName();
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
}
