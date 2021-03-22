package com.oocourse.spec3.main;

public class Node implements Comparable<Node> {
    private MyPerson person;
    private int value;
    
    public Node(MyPerson person, int value) {
        this.person = person;
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public MyPerson getPerson() {
        return person;
    }
    
    @Override
    public int compareTo(Node o) {
        return this.value - o.getValue();
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Node)) {
            return false;
        }
        return person.equals(((Node) o).getPerson());
    }
}
