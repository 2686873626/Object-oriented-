package com.oocourse.uml2;

import com.oocourse.uml2.interact.AppRunner;
import com.oocourse.uml2.interact.format.TheGeneralInteraction;

public class Main {
    
    public static void main(String[] args) throws Exception {
        AppRunner appRunner = AppRunner.newInstance(TheGeneralInteraction.class);
        appRunner.run(args);
    }
}
