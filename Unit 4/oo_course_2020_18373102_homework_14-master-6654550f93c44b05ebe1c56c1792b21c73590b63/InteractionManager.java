package com.oocourse.uml2.interact.format;

import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.models.elements.UmlEndpoint;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class InteractionManager {
    private UmlInteraction myInteraction;
    private HashMap<String, ArrayList<String>> lifelineName = new HashMap<>();
    private HashMap<String, UmlLifeline> lifelineId = new HashMap<>();
    private ArrayList<UmlEndpoint> endpointList = new ArrayList<>();
    private ArrayList<UmlMessage> messageList = new ArrayList<>();
    private HashMap<String, Integer> incoming = new HashMap<>();
    
    public InteractionManager(UmlInteraction interaction) {
        myInteraction = interaction;
    }
    
    public void addLifeline(UmlLifeline lifeline) {
        incoming.put(lifeline.getId(), 0);
        if (lifelineName.containsKey(lifeline.getName())) {
            lifelineName.get(lifeline.getName()).add(lifeline.getId());
        } else {
            ArrayList<String> temp = new ArrayList<>();
            temp.add(lifeline.getId());
            lifelineName.put(lifeline.getName(), temp);
        }
        lifelineId.put(lifeline.getId(), lifeline);
    }
    
    public void addEndpoint(UmlEndpoint endpoint) {
        endpointList.add(endpoint);
    }
    
    public void addMessage(UmlMessage message) {
        messageList.add(message);
        String target = message.getTarget();
        if (incoming.containsKey(target)) {
            incoming.put(target, incoming.get(target) + 1);
        }
    }
    
    public int queryParticipantCount() {
        return lifelineId.size();
    }
    
    public int queryMessageCount() {
        return messageList.size();
    }
    
    public int queryIncomingMessageCount(String interactionName, String lifelineName)
        throws LifelineNotFoundException, LifelineDuplicatedException {
        if (!this.lifelineName.containsKey(lifelineName)) {
            throw new LifelineNotFoundException(interactionName, lifelineName);
        }
        ArrayList<String> list = this.lifelineName.get(lifelineName);
        if (list.size() > 1) {
            throw new LifelineDuplicatedException(interactionName, lifelineName);
        }
        return incoming.get(list.get(0));
    }
}
