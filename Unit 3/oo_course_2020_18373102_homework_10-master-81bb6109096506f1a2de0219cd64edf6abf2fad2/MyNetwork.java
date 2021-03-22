package com.oocourse.spec2.main;

import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;

import java.math.BigInteger;
import java.util.HashMap;

public class MyNetwork implements Network {
    
    private HashMap<Integer, Person> people = new HashMap<>();
    private HashMap<Integer, Group>  groups = new HashMap<>();
    private HashMap<Integer, Integer> root = new HashMap<>();
    
    public MyNetwork() {}
    
    public boolean contains(int id) {
        return people.containsKey(id);
    }
    
    public Person getPerson(int id) {
        return people.get(id);
    }
    
    public void addPerson(Person person) throws EqualPersonIdException {
        int personId = person.getId();
        if (contains(personId)) {
            throw new EqualPersonIdException();
        }
        people.put(personId, person);
        root.put(personId,personId);
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
        Person person1 = getPerson(id1);
        Person person2 = getPerson(id2);
        ((MyPerson) person1).putAcquaintance(person2, value);
        ((MyPerson) person2).putAcquaintance(person1, value);
        int lo1 = root.get(id1);
        int lo2 = root.get(id2);
        root.replace(id2, lo1);
        for (int key : root.keySet()) {
            int father = root.get(key);
            if (father == lo2) {
                root.replace(key, lo1);
            }
        }
        for (Group group : groups.values()) {
            if (group.hasPerson(person1) && group.hasPerson(person2)) {
                ((MyGroup) group).addRelation(value);
            }
        }
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
        for (Person travel : people.values()) {
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
        return root.get(id1).equals(root.get(id2));
    }
    /*private boolean bfs(int start, int end) {
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
    }*/
    
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new EqualGroupIdException();
        }
        groups.put(group.getId(), group);
    }
    
    public Group getGroup(int id) {
        return groups.get(id);
    }
    
    public void addtoGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        Group dstGroup = getGroup(id2);
        if (dstGroup == null) {
            throw new GroupIdNotFoundException();
        }
        Person dstPerson = getPerson(id1);
        if (dstPerson == null) {
            throw new PersonIdNotFoundException();
        }
        if (dstGroup.hasPerson(dstPerson)) {
            throw new EqualPersonIdException();
        }
        if (((MyGroup) dstGroup).getPeopleSum() >= 1111) {
            return;
        }
        dstGroup.addPerson(dstPerson);
    }
    
    public int queryGroupSum() {
        return groups.size();
    }
    
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        Group dstGroup = getGroup(id);
        if (dstGroup == null) {
            throw new GroupIdNotFoundException();
        }
        return ((MyGroup) dstGroup).getPeopleSum();
    }
    
    public int queryGroupRelationSum(int id) throws GroupIdNotFoundException {
        Group dstGroup = getGroup(id);
        if (dstGroup == null) {
            throw new GroupIdNotFoundException();
        }
        return dstGroup.getRelationSum();
    }
    
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        Group dstGroup = getGroup(id);
        if (dstGroup == null) {
            throw new GroupIdNotFoundException();
        }
        return dstGroup.getValueSum();
    }
    
    public BigInteger queryGroupConflictSum(int id) throws GroupIdNotFoundException {
        Group dstGroup = getGroup(id);
        if (dstGroup == null) {
            throw new GroupIdNotFoundException();
        }
        return dstGroup.getConflictSum();
    }
    
    public int queryGroupAgeMean(int id) throws GroupIdNotFoundException {
        Group dstGroup = getGroup(id);
        if (dstGroup == null) {
            throw new GroupIdNotFoundException();
        }
        return dstGroup.getAgeMean();
    }
    
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        Group dstGroup = getGroup(id);
        if (dstGroup == null) {
            throw new GroupIdNotFoundException();
        }
        return dstGroup.getAgeVar();
    }
}
