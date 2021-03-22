package com.oocourse.spec1.main;

import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;

import java.math.BigInteger;
import java.util.ArrayList;

public class MyNetwork implements Network {
    
    private ArrayList<Person> people = new ArrayList<>();
    
    public boolean contains(int id) {
        for (Person travel : people) {
            if (travel.getId() == id) {
                return true;
            }
        }
        return false;
    }
    
    public Person getPerson(int id) {
        for (Person travel : people) {
            if (travel.getId() == id) {
                return travel;
            }
        }
        return null;
    }
    
    public void addPerson(Person person) throws EqualPersonIdException {
        for (Person travel : people) {
            if (travel.equals(person)) {
                throw new EqualPersonIdException();
            }
        }
        people.add(person);
    }
    
    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        if (id1 == id2) {
            return;
        }
        if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new EqualRelationException();
        }
        ((MyPerson) getPerson(id1)).putAcquaintance(getPerson(id2), value);
        ((MyPerson) getPerson(id2)).putAcquaintance(getPerson(id1), value);
    }
    
    public int queryValue(int id1, int id2)  throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new RelationNotFoundException();
        }
        return getPerson(id1).queryValue(getPerson(id2));
    }
    
    public BigInteger queryConflict(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        return getPerson(id1).getCharacter().xor(getPerson(id2).getCharacter());
    }
    
    public int queryAcquaintanceSum(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new PersonIdNotFoundException();
        }
        return getPerson(id).getAcquaintanceSum();
    }
    
    public int compareAge(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        return getPerson(id1).getAge() - getPerson(id2).getAge();
    }
    
    public int compareName(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        return getPerson(id1).compareTo(getPerson(id2));
    }
    
    public int queryPeopleSum() {
        return people.size();
    }
    
    public int queryNameRank(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new PersonIdNotFoundException();
        }
        int result = 1;
        for (Person travel : people) {
            if (getPerson(id).compareTo(travel) > 0) {
                result++;
            }
        }
        return result;
    }
    
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        return bfs(people.indexOf(getPerson(id1)), people.indexOf(getPerson(id2)));
    }
    
    private boolean bfs(int start, int end) {
        boolean[] flag = new boolean[people.size()];
        ArrayList<Integer> queue = new ArrayList<>();
        queue.add(start);
        flag[start] = true;
        while (queue.size() != 0) {
            int tmp = queue.get(0);
            queue.remove(0);
            if (tmp == end) {
                return true;
            }
            for (int i = 0; i < people.size(); i++) {
                if (!flag[i] && people.get(tmp).isLinked(people.get(i))) {
                    flag[i] = true;
                    queue.add(i);
                }
            }
        }
        return false;
    }
}
