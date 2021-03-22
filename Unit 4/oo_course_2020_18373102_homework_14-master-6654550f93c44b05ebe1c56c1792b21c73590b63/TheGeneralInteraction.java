package com.oocourse.uml2.interact.format;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ConflictQueryTypeException;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlEvent;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;
import com.oocourse.uml2.models.elements.UmlOpaqueBehavior;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TheGeneralInteraction implements UmlGeneralInteraction {
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
    
    private HashMap<String, ArrayList<String>> machineName = new HashMap<>();
    private HashMap<String, MachineManager> machineId = new HashMap<>();
    private ArrayList<UmlElement> stateList = new ArrayList<>();
    private ArrayList<UmlRegion> regionList = new ArrayList<>();
    private ArrayList<UmlEvent> eventList = new ArrayList<>();
    private HashMap<String, TransitionManager> transitionId = new HashMap<>();
    private ArrayList<UmlOpaqueBehavior> behaviorList = new ArrayList<>();
    
    private HashMap<String, ArrayList<String>> interactionName = new HashMap<>();
    private HashMap<String, InteractionManager> interactionId = new HashMap<>();
    private ArrayList<UmlLifeline> lifelineList = new ArrayList<>();
    private ArrayList<UmlMessage> messageList = new ArrayList<>();
    private ArrayList<UmlEndpoint> endpointList = new ArrayList<>();
    
    public TheGeneralInteraction(UmlElement... elements) {
        for (UmlElement element : elements) {
            generateClass(element);
        }
        fillClass();
    }
    
    private void generateClass(UmlElement element) {
        String name = element.getName();
        String id = element.getId();
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
        } else if (element instanceof UmlStateMachine) {
            initMachine(element);
        } else if (element instanceof UmlFinalState || element instanceof UmlPseudostate
                || element instanceof UmlState) {
            stateList.add(element);
        } else if (element instanceof UmlRegion) {
            regionList.add((UmlRegion) element);
        } else if (element instanceof UmlEvent) {
            eventList.add((UmlEvent) element);
        } else if (element instanceof UmlTransition) {
            transitionId.put(element.getId(), new TransitionManager((UmlTransition) element));
        } else if (element instanceof UmlOpaqueBehavior) {
            behaviorList.add((UmlOpaqueBehavior) element);
        } else if (element instanceof UmlLifeline) {
            lifelineList.add((UmlLifeline) element);
        } else if (element instanceof UmlMessage) {
            messageList.add((UmlMessage) element);
        } else if (element instanceof UmlEndpoint) {
            endpointList.add((UmlEndpoint) element);
        } else if (element instanceof UmlInteraction) {
            initInteraction(element);
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
    
    private void initInteraction(UmlElement element) {
        String name = element.getName();
        String id = element.getId();
        if (interactionName.containsKey(name)) {
            interactionName.get(name).add(id);
        } else {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(id);
            interactionName.put(name, temp);
        }
        interactionId.put(id, new InteractionManager((UmlInteraction) element));
    }
    
    private void fillClass() {
        dealAssociation();
        dealAttribute();
        dealGeneralization();
        dealInterfaceRealization();
        generateOperation();
        generateRegion();
        generateInteraction();
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
        for (UmlAttribute attribute : attributeList) {
            String parent = attribute.getParentId();
            if (classId.containsKey(parent)) {
                classId.get(parent).addAttribute(attribute);
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
            }
        }
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
        for (UmlElement element : stateList) {
            machineId.get(translate.get(element.getParentId())).addState(element);
        }
        for (TransitionManager transitionManager : transitionId.values()) {
            machineId.get(translate.get(transitionManager.getParentId()))
                    .addTransition(transitionManager);
        }
    }
    
    private void generateInteraction() {
        for (UmlLifeline lifeline : lifelineList) {
            interactionId.get(lifeline.getParentId()).addLifeline(lifeline);
        }
        for (UmlEndpoint endpoint : endpointList) {
            interactionId.get(endpoint.getParentId()).addEndpoint(endpoint);
        }
        for (UmlMessage message : messageList) {
            interactionId.get(message.getParentId()).addMessage(message);
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
        ClassManager manager = searchClass(className);
        if ((dec[0] && dec[1]) || (dec[2] && dec[3])) {
            throw new ConflictQueryTypeException();
        }
        return manager.queryMode(dec);
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
    
    public int getStateCount(String stateMachineName)
            throws StateMachineDuplicatedException, StateMachineNotFoundException {
        MachineManager machineManager = searchMachine(stateMachineName);
        return machineManager.queryStateCount();
    }
    
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        InteractionManager interactionManager = searchInteraction(interactionName);
        return interactionManager.queryParticipantCount();
    }
    
    public int getMessageCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        InteractionManager interactionManager = searchInteraction(interactionName);
        return interactionManager.queryMessageCount();
    }
    
    public int getIncomingMessageCount(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        InteractionManager interactionManager = searchInteraction(interactionName);
        return interactionManager.queryIncomingMessageCount(interactionName, lifelineName);
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
    
    private InteractionManager searchInteraction(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!this.interactionName.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        ArrayList<String> list = this.interactionName.get(interactionName);
        if (list.size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        return interactionId.get(list.get(0));
    }
}
