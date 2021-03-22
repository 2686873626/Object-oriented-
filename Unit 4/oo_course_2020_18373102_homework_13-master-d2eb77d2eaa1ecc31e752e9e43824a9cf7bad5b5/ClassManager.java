package com.oocourse.uml1.interact.format;

import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassManager {
    
    private UmlClass myClass;
    private HashMap<String, UmlClass> association = new HashMap<>();
    private HashMap<String, UmlAttribute> attributeName = new HashMap<>();
    private ArrayList<UmlAttribute> attributes = new ArrayList<>();
    private HashMap<String, InterfaceManager> interfaceManager = new HashMap<>();
    private HashMap<String, ArrayList<OperationManager>> operationManager = new HashMap<>();
    private ArrayList<OperationManager> operationAll = new ArrayList<>();
    private ClassManager father = null;
    private int associationTimes = 0;
    
    public ClassManager(UmlClass classIn) {
        myClass = classIn;
    }
    
    public void addAssociation(UmlClass element) {
        association.put(element.getId(), element);
    }
    
    public void addTimes() {
        associationTimes++;
    }
    
    public void addAttribute(UmlAttribute attribute) {
        attributes.add(attribute);
        attributeName.put(attribute.getName(),attribute);
    }
    
    public void addFather(ClassManager father) {
        this.father = father;
    }
    
    public void addInterface(InterfaceManager interfaceManager) {
        this.interfaceManager.put(interfaceManager.getId(), interfaceManager);
    }
    
    public void addOperation(OperationManager manager) {
        String name = manager.getName();
        if (operationManager.containsKey(name)) {
            operationManager.get(name).add(manager);
        } else {
            ArrayList<OperationManager> temp = new ArrayList<>();
            temp.add(manager);
            operationManager.put(name, temp);
        }
        operationAll.add(manager);
    }
    
    public int queryMode(boolean[] dec) {
        int result = 0;//0->NON_RETURN,1->RETURN,2->NON_PARAM,3->PARAM
        for (OperationManager travel : operationAll) {
            boolean re = (!dec[0] || !travel.getIfReturn()) && (!dec[1] || travel.getIfReturn());
            boolean param = (!dec[2] || !travel.getIfIn()) && (!dec[3] || travel.getIfIn());
            if (re && param) {
                result++;
            }
        }
        return result;
    }
    
    public int queryAttributeCount(AttributeQueryType queryType) {
        if (queryType == AttributeQueryType.ALL) {
            if (father == null) {
                return attributes.size();
            } else {
                return attributes.size() + father.queryAttributeCount(queryType);
            }
        } else {
            return attributes.size();
        }
    }
    
    public int queryAssociationCount() {
        if (father == null) {
            return associationTimes;
        } else {
            return associationTimes + father.queryAssociationCount();
        }
    }
    
    public void queryAssociatedClassList(HashMap<String, UmlClass> result) {
        result.putAll(association);
        if (father != null) {
            father.queryAssociatedClassList(result);
        }
    }
    
    public Map<Visibility, Integer> queryOperationVisibility(String operationName) {
        Map<Visibility, Integer> result = new HashMap<>();
        int[] times = new int[4];
        for (int i = 0; i < 4; i++) {
            times[i] = 0;
        }
        if (operationManager.containsKey(operationName)) {
            ArrayList<OperationManager> list = operationManager.get(operationName);
            for (OperationManager manager : list) {
                switch (manager.getVisibility()) {
                    case PUBLIC:
                        times[0]++;
                        break;
                    case PROTECTED:
                        times[1]++;
                        break;
                    case PRIVATE:
                        times[2]++;
                        break;
                    case PACKAGE:
                        times[3]++;
                        break;
                    default:
                }
            }
        }
        result.put(Visibility.PUBLIC, times[0]);
        result.put(Visibility.PROTECTED, times[1]);
        result.put(Visibility.PRIVATE, times[2]);
        result.put(Visibility.PACKAGE, times[3]);
        return result;
    }
    
    public Visibility queryAttributeVisibility(String classname, String name, Visibility find)
        throws AttributeDuplicatedException {
        if (attributeName.containsKey(name)) {
            if (find != null) {
                throw new AttributeDuplicatedException(classname, name);
            }
            if (father == null) {
                return attributeName.get(name).getVisibility();
            } else {
                return father.queryAttributeVisibility(classname, name,
                        attributeName.get(name).getVisibility());
            }
        } else {
            if (father == null) {
                return find;
            } else {
                return father.queryAttributeVisibility(classname, name, find);
            }
        }
    }
    
    public String queryTopParentClass() {
        if (father == null) {
            return myClass.getName();
        } else {
            return father.queryTopParentClass();
        }
    }
    
    public void queryInterfaceList(
            HashMap<String, String> result) {
        for (InterfaceManager manager : interfaceManager.values()) {
            if (!result.containsKey(manager.getId())) {
                manager.queryInterfaceList(result);
            }
        }
        if (father != null) {
            father.queryInterfaceList(result);
        }
    }
    
    public void queryInformationNotHidden(ArrayList<AttributeClassInformation> result) {
        for (UmlAttribute attribute : attributes) {
            if (attribute.getVisibility() != Visibility.PRIVATE) {
                result.add(new AttributeClassInformation(attribute.getName(), myClass.getName()));
            }
        }
        if (father != null) {
            father.queryInformationNotHidden(result);
        }
    }
    
}
