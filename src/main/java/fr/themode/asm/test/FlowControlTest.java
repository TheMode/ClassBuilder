package fr.themode.asm.test;

import java.util.UUID;

public class FlowControlTest {

    public void test() {
        a(UUID.randomUUID().toString(), UUID.randomUUID().toString());

        boolean bool = true;
        if (bool) {
            String test = "";
        }

        int arg = 5;
        if (arg > 5) {
            arg = 6;
        } else if (arg < 2) {
            arg = 7;
        } else if (arg == 3) {
            arg = 8;
        } else {
            arg = 9;
        }
    }

    public void a(String a1, String a2) {

    }

}
