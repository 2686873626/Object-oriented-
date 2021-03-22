package com.oocourse.spec3.main;

import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    
    private HashMap<Integer, Person> people = new HashMap<Integer, Person>(800);
    private HashMap<Integer, Group>  groups = new HashMap<Integer, Group>();
    private HashMap<Integer, Integer> money = new HashMap<Integer, Integer>(800);
    private HashMap<Integer, Integer> root  = new HashMap<Integer, Integer>(800);
    private HashSet<Integer> arrival = new HashSet<Integer>(800);
    private ArrayList<Integer> road = new ArrayList<Integer>();
    
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
        money.put(personId, 0);
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
        Person person = getPerson(id);
        for (Person travel : people.values()) {
            if (person.compareTo(travel) > 0) {
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
    
    public int queryAgeSum(int l, int r) {
        int result = 0;
        for (Person travel : people.values()) {
            if (travel.getAge() >= l && travel.getAge() <= r) {
                result++;
            }
        }
        return result;
    }
    
    public void delFromGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        Group dstGroup = getGroup(id2);
        if (dstGroup == null) {
            throw new GroupIdNotFoundException();
        }
        Person dstPerson = getPerson(id1);
        if (dstPerson == null) {
            throw new PersonIdNotFoundException();
        }
        if (!dstGroup.hasPerson(dstPerson)) {
            throw new EqualPersonIdException();
        }
        if (((MyGroup) dstGroup).getPeopleSum() >= 1111) {
            return;
        }
        dstGroup.delPerson(dstPerson);
    }
    
    public int queryMinPath(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        if (id1 == id2) {
            return 0;
        }
        if (!isCircle(id1, id2)) {
            return -1;
        }
        PriorityQueue<Node> queue = new PriorityQueue<Node>();
        queue.add(new Node((MyPerson)getPerson(id1), 0));
        HashMap<MyPerson, Integer> arrive = new HashMap<MyPerson, Integer>();
        arrive.put((MyPerson)getPerson(id1), 0);
        while (!queue.isEmpty()) {
            Node temp = queue.poll();
            MyPerson person = temp.getPerson();
            int len2 = temp.getValue();
            if (person.getId() == id2) {
                return len2;
            }
            if (arrive.get(person) >= len2) {
                ArrayList<Person> scan = person.getAcquaintance();
                for (Person scan0 : scan) {
                    MyPerson travel = (MyPerson)scan0;
                    int len1 = person.queryValue(travel);
                    if (!arrive.containsKey(travel)) {
                        arrive.put(travel, len1 + len2);
                        queue.add(new Node(travel, len1 + len2));
                    } else {
                        if (len1 + len2 < arrive.get(travel)) {
                            arrive.replace(travel, len1 + len2);
                            queue.add(new Node(travel, len1 + len2));
                        }
                    }
                }
            }
        }
        return -1;
    }
    
    public boolean queryStrongLinked(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        if (id1 == id2) {
            return true;
        }
        if (!isCircle(id1, id2)) {
            return false;
        }
        arrival.clear();
        if (!getPerson(id1).isLinked(getPerson(id2))) {
            road.clear();
            findRoad(id1, id2);
            road.remove(0);
            for (Integer scan : road) {
                arrival.clear();
                arrival.add(scan);
                if (!dfs(id1, id2)) {
                    return false;
                }
            }
            return true;
        } else {
            arrival.add(id1);
            for (Integer scan : ((MyPerson) getPerson(id1)).getAcquaintanceId()) {
                if (scan == id2) {
                    continue;
                }
                if (dfs(scan, id2)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean findRoad(int now, int end) {
        if (now == end) {
            return true;
        }
        arrival.add(now);
        Person currentPerson = getPerson(now);
        for (Integer scan : ((MyPerson) currentPerson).getAcquaintanceId()) {
            if (!arrival.contains(scan)) {
                if (findRoad(scan, end)) {
                    road.add(scan);
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean dfs(int now, int end) {
        if (now == end) {
            return true;
        }
        arrival.add(now);
        Person currentPerson = getPerson(now);
        for (Integer scan : ((MyPerson) currentPerson).getAcquaintanceId()) {
            if (!arrival.contains(scan)) {
                if (dfs(scan, end)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public int queryBlockSum() {
        int result = 0;
        for (Integer travel : root.keySet()) {
            if (root.get(travel).equals(travel)) {
                result++;
            }
        }
        return result;
    }
    
    public void borrowFrom(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualPersonIdException {
        if (!contains(id1) || !contains(id2)) {
            throw new PersonIdNotFoundException();
        }
        if (id1 == id2) {
            throw new EqualPersonIdException();
        }
        int borrowed = money.get(id1);
        int borrow = money.get(id2);
        money.replace(id1, borrowed - value);
        money.replace(id2, borrow + value);
    }
    
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new PersonIdNotFoundException();
        }
        return money.get(id);
    }
}
