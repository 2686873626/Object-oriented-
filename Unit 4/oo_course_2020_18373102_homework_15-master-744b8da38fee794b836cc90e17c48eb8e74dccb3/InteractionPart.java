package com.oocourse.uml3.interact.format;

import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlEndpoint;
import com.oocourse.uml3.models.elements.UmlInteraction;
import com.oocourse.uml3.models.elements.UmlLifeline;
import com.oocourse.uml3.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class InteractionPart {
    private HashMap<String, ArrayList<String>> interactionName = new HashMap<>();
    private HashMap<String, InteractionManager> interactionId = new HashMap<>();
    private ArrayList<UmlLifeline> lifelineList = new ArrayList<>();
    private ArrayList<UmlMessage> messageList = new ArrayList<>();
    private ArrayList<UmlEndpoint> endpointList = new ArrayList<>();
    
    public void addElement(UmlElement element) {
        if (element instanceof UmlLifeline) {
            lifelineList.add((UmlLifeline) element);
        } else if (element instanceof UmlMessage) {
            messageList.add((UmlMessage) element);
        } else if (element instanceof UmlEndpoint) {
            endpointList.add((UmlEndpoint) element);
        } else if (element instanceof UmlInteraction) {
            initInteraction(element);
        }
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
    
    public void fillInteraction() {
        generateInteraction();
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
}
