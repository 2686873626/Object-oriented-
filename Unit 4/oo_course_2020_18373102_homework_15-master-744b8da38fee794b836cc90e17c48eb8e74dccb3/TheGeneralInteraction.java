package com.oocourse.uml3.interact.format;

import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.common.AttributeQueryType;
import com.oocourse.uml3.interact.common.OperationQueryType;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.ConflictQueryTypeException;
import com.oocourse.uml3.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlElement;

import java.util.List;
import java.util.Map;

public class TheGeneralInteraction implements UmlGeneralInteraction {
    private ClassPart classPart = new ClassPart();
    private InteractionPart interactionPart = new InteractionPart();
    private StatePart statePart = new StatePart();
    
    public TheGeneralInteraction(UmlElement... elements) {
        for (UmlElement element : elements) {
            generateClass(element);
        }
        fillClass();
    }
    
    private void generateClass(UmlElement element) {
        classPart.addElement(element);
        statePart.addElement(element);
        interactionPart.addElement(element);
    }
    
    private void fillClass() {
        classPart.fillClass();
        statePart.fillState();
        interactionPart.fillInteraction();
    }
    
    public int getClassCount() {
        return classPart.getClassCount();
    }
    
    public int getClassOperationCount(String className, OperationQueryType[] queryTypes)
            throws ClassNotFoundException, ClassDuplicatedException, ConflictQueryTypeException {
        return classPart.getClassOperationCount(className, queryTypes);
    }
    
    public int getClassAttributeCount(String className, AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classPart.getClassAttributeCount(className, queryType);
    }
    
    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classPart.getClassAssociationCount(className);
    }
    
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classPart.getClassAssociatedClassList(className);
    }
    
    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classPart.getClassOperationVisibility(className, operationName);
    }
    
    public Visibility getClassAttributeVisibility(String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        return classPart.getClassAttributeVisibility(className, attributeName);
    }
    
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classPart.getTopParentClass(className);
    }
    
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classPart.getImplementInterfaceList(className);
    }
    
    public List<AttributeClassInformation> getInformationNotHidden(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classPart.getInformationNotHidden(className);
    }
    
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return interactionPart.getParticipantCount(interactionName);
    }
    
    public int getMessageCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return interactionPart.getMessageCount(interactionName);
    }
    
    public int getIncomingMessageCount(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return interactionPart.getIncomingMessageCount(interactionName, lifelineName);
    }
    
    public int getStateCount(String stateMachineName)
            throws StateMachineDuplicatedException, StateMachineNotFoundException {
        return statePart.getStateCount(stateMachineName);
    }
    
    public int getTransitionCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return statePart.getTransitionCount(stateMachineName);
    }
    
    public int getSubsequentStateCount(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return statePart.getSubsequentStateCount(stateMachineName, stateName);
    }
    
    public void checkForUml001() throws UmlRule001Exception {
        classPart.checkForUml001();
    }
    
    public void checkForUml002() throws UmlRule002Exception {
        classPart.checkForUml002();
    }
    
    public void checkForUml003() throws UmlRule003Exception {
        classPart.checkForUml003();
    }
    
    public void checkForUml004() throws UmlRule004Exception {
        classPart.checkForUml004();
    }
    
    public void checkForUml005() throws UmlRule005Exception {
        classPart.checkForUml005();
    }
    
    public void checkForUml006() throws UmlRule006Exception {
        classPart.checkForUml006();
    }
    
    public void checkForUml007() throws UmlRule007Exception {
        statePart.checkForUml007();
    }
    
    public void checkForUml008() throws UmlRule008Exception {
        statePart.checkForUml008();
    }
}
