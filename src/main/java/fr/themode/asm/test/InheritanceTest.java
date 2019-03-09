package fr.themode.asm.test;

import fr.themode.asm.method.MethodFinder;

public class InheritanceTest extends Superclass {

    public static int i = 1;

    static {
        System.out.println("IM STATIC");
    }

    public InheritanceTest(int value) {
        super(value);
        String test = new MethodFinder().toString();
    }
}
