package com.oocourse.spec2.main;

import java.math.BigInteger;
import java.util.HashMap;

public class MyPerson implements Person {
    
    private int id;
    private String name;
    private BigInteger character;
    private int age;
    private HashMap<Integer, Person> acquaintance = new HashMap<>();
    private HashMap<Integer, Integer> value = new HashMap<>();
    
    public MyPerson(int id, String name, BigInteger character, int age) {
        this.id = id;
        this.name = name;
        this.character = character;
        this.age = age;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public BigInteger getCharacter() {
        return this.character;
    }
    
    public int getAge() {
        return this.age;
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) {
            return false;
        }
        return (((Person) obj).getId() == this.id);
    }
    
    public boolean isLinked(Person person) {
        if (person.getId() == this.id) {
            return true;
        }
        return acquaintance.containsKey(person.getId());
    }
    
    public int queryValue(Person person) {
        if (acquaintance.containsKey(person.getId())) {
            return value.get(person.getId());
        }
        return 0;
    }
    
    public int getAcquaintanceSum() {
        return acquaintance.size();
    }
    
    public int compareTo(Person p2) {
        return this.name.compareTo(p2.getName());
    }
    
    public void putAcquaintance(Person person, int value) {
        acquaintance.put(person.getId(), person);
        this.value.put(person.getId(), value);
    }
}