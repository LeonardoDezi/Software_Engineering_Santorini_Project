package it.polimi.ingsw.Model;

import org.junit.Test;

public class Phase1Test {
    Phase1 phase;

    @Test
    public void test(){
        phase = new Phase1();
        String s1 = "ciao";
        phase.commands.get(s1).run();
    }

}
