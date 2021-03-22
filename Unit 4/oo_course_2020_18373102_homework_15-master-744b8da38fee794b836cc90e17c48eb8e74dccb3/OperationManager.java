package com.oocourse.uml3.interact.format;

import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;

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
    
    public boolean checkForUml005() {
        if (myOperation.getName() == null) {
            return true;
        }
        for (UmlParameter parameter : parameters) {
            if (parameter.getDirection() != Direction.RETURN && parameter.getName() == null) {
                return true;
            }
        }
        return false;
    }
}
