package com.oocourse.uml1;

import com.oocourse.uml1.interact.AppRunner;
import com.oocourse.uml1.interact.format.TheUmlInteraction;

public class Main {
    
    public static void main(String[] args) throws Exception {
        AppRunner appRunner = AppRunner.newInstance(TheUmlInteraction.class);
        appRunner.run(args);
    }
}
