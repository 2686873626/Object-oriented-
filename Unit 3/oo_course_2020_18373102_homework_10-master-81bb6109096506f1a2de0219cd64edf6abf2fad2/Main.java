package com.oocourse.spec2.main;

public class Main {
    
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class);
        runner.run();
    }
}