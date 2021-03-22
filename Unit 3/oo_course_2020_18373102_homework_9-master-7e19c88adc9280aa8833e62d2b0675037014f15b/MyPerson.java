package com.oocourse.spec1.main;

import java.math.BigInteger;
import java.util.ArrayList;

public class MyPerson implements Person {
    
    private int id;
    private String name;
    private BigInteger character;
    private int age;
    private ArrayList<Person> acquaintance = new ArrayList<>();
    private ArrayList<Integer> value = new ArrayList<>();
    
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
        for (Person travel : acquaintance) {
            if (travel.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }
    
    public int queryValue(Person person) {
        for (int i = 0; i < acquaintance.size(); i++) {
            if (person.getId() == acquaintance.get(i).getId()) {
                return value.get(i);
            }
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
        acquaintance.add(person);
        this.value.add(value);
    }
}
