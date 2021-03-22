package com.oocourse.uml1.interact.format;

import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;

public class OperationManager {
    
    private UmlOperation myOperation;
    private ArrayList<UmlParameter> parameters = new ArrayList<>();
    private boolean ifReturn = false;
    private boolean ifIn = false;
    
    public OperationManager(UmlOperation operation) {
        myOperation = operation;
    }
    
    public void addParameter(UmlParameter parameter) {
        parameters.add(parameter);
        if (parameter.getDirection() == Direction.IN) {
            ifIn = true;
        } else if (parameter.getDirection() == Direction.RETURN) {
            ifReturn = true;
        }
    }
    
    public boolean getIfReturn() {
        return ifReturn;
    }
    
    public boolean getIfIn() {
        return ifIn;
    }
    
    public String getParentId() {
        return myOperation.getParentId();
    }
    
    public String getName() {
        return myOperation.getName();
    }
    
    public Visibility getVisibility() {
        return myOperation.getVisibility();
    }
}
