package com.oocourse.uml1.interact.format;

import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.ConflictQueryTypeException;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlParameter;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheUmlInteraction implements UmlInteraction {
    
    private HashMap<String, ArrayList<String>> className = new HashMap<>();//name->id
    private HashMap<String, ClassManager> classId = new HashMap<>();//id->classManager
    private HashMap<String, InterfaceManager> interfaceId = new HashMap<>();//id->interfaceManager
    private HashMap<String, UmlElement> total = new HashMap<>();//id->class or interface
    private ArrayList<UmlAssociation> associations = new ArrayList<>();
    private HashMap<String, UmlAssociationEnd> associationEnds = new HashMap<>();//id->End
    private ArrayList<UmlAttribute> attributes = new ArrayList<>();
    private ArrayList<UmlGeneralization> generalizations = new ArrayList<>();
    private ArrayList<UmlInterfaceRealization> interfaceRealizations = new ArrayList<>();
    private HashMap<String, OperationManager> operations = new HashMap<>();//id->operation
    private ArrayList<UmlParameter> parameters = new ArrayList<>();
    
    public TheUmlInteraction(UmlElement... elements) {
        for (UmlElement element : elements) {
            generateClass(element);
        }
        fillClass();
    }
    
    private void generateClass(UmlElement element) {
        if (element instanceof UmlClass) {
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
        } else if (element instanceof UmlInterface) {
            interfaceId.put(element.getId(), new InterfaceManager((UmlInterface)element));
            total.put(element.getId(), element);
        } else if (element instanceof UmlAssociation) {
            associations.add((UmlAssociation) element);
        } else if (element instanceof UmlAssociationEnd) {
            associationEnds.put(element.getId(), (UmlAssociationEnd) element);
        } else if (element instanceof UmlAttribute) {
            attributes.add((UmlAttribute) element);
        } else if (element instanceof UmlGeneralization) {
            generalizations.add((UmlGeneralization) element);
        } else if (element instanceof UmlInterfaceRealization) {
            interfaceRealizations.add((UmlInterfaceRealization) element);
        } else if (element instanceof UmlOperation) {
            operations.put(element.getId(), new OperationManager((UmlOperation) element));
        } else if (element instanceof UmlParameter) {
            parameters.add((UmlParameter) element);
        }
    }
    
    private void fillClass() {
        dealAssociation();
        dealAttribute();
        dealGeneralization();
        dealInterfaceRealization();
        generateOperation();
    }
    
    private void dealAssociation() {
        for (UmlAssociation association : associations) {
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
                manager1.addTimes();
            }
            if (class2 instanceof UmlClass) {
                ClassManager manager2 = classId.get(id2);
                if (class1 instanceof UmlClass) {
                    manager2.addAssociation((UmlClass) class1);
                }
                manager2.addTimes();
            }
        }
    }
    
    private void dealAttribute() {
        for (UmlAttribute attribute : attributes) {
            String parent = attribute.getParentId();
            if (classId.containsKey(parent)) {
                classId.get(parent).addAttribute(attribute);
            }
        }
    }
    
    private void dealGeneralization() {
        for (UmlGeneralization generalization : generalizations) {
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
        for (UmlInterfaceRealization interfaceRealization : interfaceRealizations) {
            String sourceId = interfaceRealization.getSource();
            String targetId = interfaceRealization.getTarget();
            classId.get(sourceId).addInterface(interfaceId.get(targetId));
        }
    }
    
    private void generateOperation() {
        for (UmlParameter parameter : parameters) {
            operations.get(parameter.getParentId()).addParameter(parameter);
        }
        for (OperationManager operationManager : operations.values()) {
            String parentId = operationManager.getParentId();
            if (classId.containsKey(parentId)) {
                classId.get(parentId).addOperation(operationManager);
            }
        }
    }
    
    public int getClassCount() {
        return classId.size();
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
        ClassManager manager = existAndUnique(className);
        if ((dec[0] && dec[1]) || (dec[2] && dec[3])) {
            throw new ConflictQueryTypeException();
        }
        return manager.queryMode(dec);
    }
    
    public int getClassAttributeCount(String className, AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        ClassManager manager = existAndUnique(className);
        return manager.queryAttributeCount(queryType);
    }
    
    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        ClassManager manager = existAndUnique(className);
        return manager.queryAssociationCount();
    }
    
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        ClassManager manager = existAndUnique(className);
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
        ClassManager manager = existAndUnique(className);
        return manager.queryOperationVisibility(operationName);
    }
    
    public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        ClassManager manager = existAndUnique(className);
        Visibility result = manager.queryAttributeVisibility(className, attributeName, null);
        if (result == null) {
            throw new AttributeNotFoundException(className, attributeName);
        }
        return result;
    }
    
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        ClassManager manager = existAndUnique(className);
        return manager.queryTopParentClass();
    }
    
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        ClassManager manager = existAndUnique(className);
        HashMap<String, String> result = new HashMap<>();
        manager.queryInterfaceList(result);
        return new ArrayList<>(result.values());
    }
    
    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        ClassManager manager = existAndUnique(className);
        ArrayList<AttributeClassInformation> result = new ArrayList<>();
        manager.queryInformationNotHidden(result);
        return result;
    }
    
    private ClassManager existAndUnique(String className)
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
}
