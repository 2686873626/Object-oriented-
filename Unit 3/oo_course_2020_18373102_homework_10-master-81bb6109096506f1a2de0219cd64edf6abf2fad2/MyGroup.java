package com.oocourse.spec2.main;

import java.math.BigInteger;
import java.util.HashMap;

public class MyGroup implements Group {
    
    private int id;
    private HashMap<Integer, Person> people = new HashMap<>();
    private int relationSum = 0;
    private int valueSum = 0;
    private BigInteger conflictSum = BigInteger.ZERO;
    
    public MyGroup(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof Group)) {
            return false;
        }
        return (((Group) obj).getId() == this.id);
    }
    
    public void addPerson(Person person) {
        conflictSum = conflictSum.xor(person.getCharacter());
        relationSum++;
        for (Person travel : people.values()) {
            if (travel.isLinked(person)) {
                relationSum += 2;
                valueSum += 2 * person.queryValue(travel);
            }
        }
        people.put(person.getId(), person);
    }
    
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }
    
    public int getRelationSum() {
        return relationSum;
    }
    
    public int getValueSum() {
        return valueSum;
    }
    
    public BigInteger getConflictSum() {
        return conflictSum;
    }
    
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        int result = 0;
        for (Person person : people.values()) {
            result += person.getAge();
        }
        return (result / people.size());
    }
    
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        int mean = getAgeMean();
        int result = 0;
        for (Person person : people.values()) {
            result += (person.getAge() - mean) * (person.getAge() - mean);
        }
        return (result / people.size());
    }
    
    public int getPeopleSum() {
        return people.size();
    }
    
    public void addRelation(int value) {
        valueSum += 2 * value;
        relationSum += 2;
    }
}
