package com.oocourse.uml3.interact.format;

import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.AttributeQueryType;
import com.oocourse.uml3.interact.common.OperationQueryType;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.ConflictQueryTypeException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ClassPart {
    private HashMap<String, ArrayList<String>> className = new HashMap<>();//name->id
    private HashMap<String, ClassManager> classId = new HashMap<>();//id->classManager
    private HashMap<String, InterfaceManager> interfaceId = new HashMap<>();//id->interfaceManager
    private HashMap<String, UmlElement> total = new HashMap<>();//id->class or interface
    private HashMap<String, UmlAssociationEnd> associationEnds = new HashMap<>();//id->End
    private HashMap<String, OperationManager> operations = new HashMap<>();//id->operation
    private ArrayList<UmlAssociation> associationList = new ArrayList<>();
    private ArrayList<UmlAttribute> attributeList = new ArrayList<>();
    private ArrayList<UmlGeneralization> generalizationList = new ArrayList<>();
    private ArrayList<UmlInterfaceRealization> interfaceRealizationList = new ArrayList<>();
    private ArrayList<UmlParameter> parameterList = new ArrayList<>();
    private HashSet<String> arrival = new HashSet<>();
    
    public void addElement(UmlElement element) {
        if (element instanceof UmlClass) {
            initClass(element);
        } else if (element instanceof UmlInterface) {
            interfaceId.put(element.getId(), new InterfaceManager((UmlInterface)element));
            total.put(element.getId(), element);
        } else if (element instanceof UmlAssociation) {
            associationList.add((UmlAssociation) element);
        } else if (element instanceof UmlAssociationEnd) {
            associationEnds.put(element.getId(), (UmlAssociationEnd) element);
        } else if (element instanceof UmlAttribute) {
            attributeList.add((UmlAttribute) element);
        } else if (element instanceof UmlGeneralization) {
            generalizationList.add((UmlGeneralization) element);
        } else if (element instanceof UmlInterfaceRealization) {
            interfaceRealizationList.add((UmlInterfaceRealization) element);
        } else if (element instanceof UmlOperation) {
            operations.put(element.getId(), new OperationManager((UmlOperation) element));
        } else if (element instanceof UmlParameter) {
            parameterList.add((UmlParameter) element);
        }
    }
    
    private void initClass(UmlElement element) {
        String name = element.getName();
        String id = element.getId();
        if (className.containsKey(name)) {
            className.get(name).add(id);
        } else {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(id);
            className.put(name, temp);
        }
        classId.put(id, new ClassManager((UmlClass) element));
        total.put(id, element);
    }
    
    public void fillClass() {
        dealAssociation();
        dealAttribute();
        dealGeneralization();
        dealInterfaceRealization();
        generateOperation();
    }
    
    private void dealAssociation() {
        for (UmlAssociation association : associationList) {
            UmlAssociationEnd end1 = associationEnds.get(association.getEnd1());
            UmlAssociationEnd end2 = associationEnds.get(association.getEnd2());
            String id1 = end1.getReference();
            String id2 = end2.getReference();
            UmlElement class1 = total.get(id1);
            UmlElement class2 = total.get(id2);
            if (class1 instanceof UmlClass) {
                ClassManager manager1 = classId.get(id1);
                if (class2 instanceof UmlClass) {
                    manager1.addAssociation((UmlClass) class2);
                }
                manager1.addAssociationAll(end2);
            }
            if (class2 instanceof UmlClass) {
                ClassManager manager2 = classId.get(id2);
                if (class1 instanceof UmlClass) {
                    manager2.addAssociation((UmlClass) class1);
                }
                manager2.addAssociationAll(end1);
            }
        }
    }
    
    private void dealAttribute() {
        for (UmlAttribute attribute : attributeList) {
            String parent = attribute.getParentId();
            if (classId.containsKey(parent)) {
                classId.get(parent).addAttribute(attribute);
            } else if (interfaceId.containsKey(parent)) {
                interfaceId.get(parent).addAttribute(attribute);
            }
        }
    }
    
    private void dealGeneralization() {
        for (UmlGeneralization generalization : generalizationList) {
            String sourceId = generalization.getSource();
            String targetId = generalization.getTarget();
            if (total.get(sourceId) instanceof UmlClass) {
                classId.get(sourceId).addFather(classId.get(targetId));
            } else {
                interfaceId.get(sourceId).addFather(interfaceId.get(targetId));
            }
        }
    }
    
    private void dealInterfaceRealization() {
        for (UmlInterfaceRealization interfaceRealization : interfaceRealizationList) {
            String sourceId = interfaceRealization.getSource();
            String targetId = interfaceRealization.getTarget();
            classId.get(sourceId).addInterface(interfaceId.get(targetId));
        }
    }
    
    private void generateOperation() {
        for (UmlParameter parameter : parameterList) {
            operations.get(parameter.getParentId()).addParameter(parameter);
        }
        for (OperationManager operationManager : operations.values()) {
            String parentId = operationManager.getParentId();
            if (classId.containsKey(parentId)) {
                classId.get(parentId).addOperation(operationManager);
            } else if (interfaceId.containsKey(parentId)) {
                interfaceId.get(parentId).addOperation(operationManager);
            }
        }
    }
    
    private ClassManager searchClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!this.className.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        ArrayList<String> list = this.className.get(className);
        if (list.size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return classId.get(list.get(0));
    }
    
    public int getClassOperationCount(String className, OperationQueryType[] queryTypes)
            throws ClassNotFoundException, ClassDuplicatedException, ConflictQueryTypeException {
        boolean[] dec = new boolean[4];//0->NON_RETURN,1->RETURN,2->NON_PARAM,3->PARAM
        for (int i = 0; i < 4; i++) {
            dec[i] = false;
        }
        for (OperationQueryType travel : queryTypes) {
            switch (travel) {
                case NON_RETURN:
                    dec[0] = true;
                    break;
                case RETURN:
                    dec[1] = true;
                    break;
                case NON_PARAM:
                    dec[2] = true;
                    break;
                case PARAM:
                    dec[3] = true;
                    break;
                default:
            }
        }
        ClassManager manager = searchClass(className);
        if ((dec[0] && dec[1]) || (dec[2] && dec[3])) {
            throw new ConflictQueryTypeException();
        }
        return manager.queryMode(dec);
    }
    
    public int getClassCount() {
        return classId.size();
    }
    
    public int getClassAttributeCount(String className, AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        ClassManager manager = searchClass(className);
        return manager.queryAttributeCount(queryType);
    }
    
    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        ClassManager manager = searchClass(className);
        return manager.queryAssociationCount();
    }
    
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        ClassManager manager = searchClass(className);
        HashMap<String, UmlClass> result = new HashMap<>();
        manager.queryAssociatedClassList(result);
        List<String> fin = new ArrayList<>();
        for (UmlClass travel : result.values()) {
            fin.add(travel.getName());
        }
        return fin;
    }
    
    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        ClassManager manager = searchClass(className);
        return manager.queryOperationVisibility(operationName);
    }
    
    public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        ClassManager manager = searchClass(className);
        Visibility result = manager.queryAttributeVisibility(className, attributeName, null);
        if (result == null) {
            throw new AttributeNotFoundException(className, attributeName);
        }
        return result;
    }
    
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        ClassManager manager = searchClass(className);
        return manager.queryTopParentClass();
    }
    
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        ClassManager manager = searchClass(className);
        HashMap<String, String> result = new HashMap<>();
        manager.queryInterfaceList(result);
        return new ArrayList<>(result.values());
    }
    
    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        ClassManager manager = searchClass(className);
        ArrayList<AttributeClassInformation> result = new ArrayList<>();
        manager.queryInformationNotHidden(result);
        return result;
    }
    
    public void checkForUml001() throws UmlRule001Exception {
        HashSet<AttributeClassInformation> result = new HashSet<>();
        for (ClassManager classManager : classId.values()) {
            classManager.checkForUml001(result);
        }
        if (result.size() != 0) {
            throw new UmlRule001Exception(result);
        }
    }
    
    public void checkForUml002() throws UmlRule002Exception {
        HashSet<UmlClassOrInterface> circle = new HashSet<>();
        FindCircle(circle);
        if (circle.size() != 0) {
            throw new UmlRule002Exception(circle);
        }
    }
    
    private void FindCircle(HashSet<UmlClassOrInterface> circle) {
        for (ClassManager manager : classId.values()) {
            if (circle.contains(manager.getMyClass())) {
                continue;
            }
            arrival.clear();
            HashSet<UmlClassOrInterface> temp = new HashSet<>();
            classDfs(manager.getId(), manager, temp, circle);
        }
        for (InterfaceManager manager : interfaceId.values()) {
            if (circle.contains(manager.getMyInterface())) {
                continue;
            }
            arrival.clear();
            HashSet<UmlClassOrInterface> temp = new HashSet<>();
            interfaceDfs(manager.getId(), manager, temp, circle);
        }
    }
    
    private boolean classDfs(String end, ClassManager manager,
                          HashSet<UmlClassOrInterface> temp, HashSet<UmlClassOrInterface> circle) {
        arrival.add(manager.getId());
        temp.add(manager.getMyClass());
        ClassManager father = manager.getFather();
        if (father == null) {
            return false;
        }
        if (father.getId().equals(end)) {
            circle.addAll(temp);
            return true;
        } else if (!arrival.contains(father.getId())) {
            if (classDfs(end, father, temp, circle)) {
                return true;
            }
            temp.remove(manager.getMyClass());
        }
        return false;
    }
    
    private boolean interfaceDfs(String end, InterfaceManager manager
            , HashSet<UmlClassOrInterface> temp, HashSet<UmlClassOrInterface> circle) {
        arrival.add(manager.getId());
        temp.add(manager.getMyInterface());
        ArrayList<InterfaceManager> father = manager.getFather();
        for (InterfaceManager travel : father) {
            if (travel.getId().equals(end)) {
                circle.addAll(temp);
                return true;
            } else if (!arrival.contains(travel.getId())) {
                if (interfaceDfs(end, travel, temp, circle)) {
                    return true;
                }
                temp.remove(travel.getMyInterface());
            }
        }
        return false;
    }
    
    public void checkForUml003() throws UmlRule003Exception {
        HashSet<UmlClassOrInterface> result = new HashSet<>();
        for (InterfaceManager manager : interfaceId.values()) {
            arrival.clear();
            if (dfs003(manager)) {
                result.add(manager.getMyInterface());
            }
        }
        if (result.size() != 0) {
            throw new UmlRule003Exception(result);
        }
    }
    
    private boolean dfs003(InterfaceManager manager) {
        arrival.add(manager.getId());
        ArrayList<InterfaceManager> father = manager.getFather();
        for (InterfaceManager travel : father) {
            if (arrival.contains(travel.getId())) {
                return true;
            }
            if (dfs003(travel)) {
                return true;
            }
        }
        return false;
    }
    
    public void checkForUml004() throws UmlRule004Exception {
        HashSet<UmlClassOrInterface> result = new HashSet<>();
        for (ClassManager manager : classId.values()) {
            arrival.clear();
            if (manager.queryUml004(arrival)) {
                result.add(manager.getMyClass());
            }
        }
        if (result.size() != 0) {
            throw new UmlRule004Exception(result);
        }
    }
    
    public void checkForUml005() throws UmlRule005Exception {
        for (ClassManager manager : classId.values()) {
            if (manager.checkForUml005()) {
                throw new UmlRule005Exception();
            }
        }
        for (InterfaceManager interfaceManager : interfaceId.values()) {
            if (interfaceManager.checkForUml005()) {
                throw new UmlRule005Exception();
            }
        }
    }
    
    public void checkForUml006() throws UmlRule006Exception {
        for (InterfaceManager manager : interfaceId.values()) {
            if (manager.checkForUml006()) {
                throw new UmlRule006Exception();
            }
        }
    }
}
