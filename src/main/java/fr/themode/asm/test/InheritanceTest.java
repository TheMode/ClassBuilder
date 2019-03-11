package fr.themode.asm.test;

public class InheritanceTest {

    public InheritanceTest() {

        int bool = 1;
        float value = 2;
        if (bool == value && value > bool) {
            System.out.println("SOUT");
        }

        if (test()) {
            System.out.println("SOUT 2");
        }

        System.out.println("OPERATOR TEST");
        int a = 1;
        double b = 2;
        float c = 3;
        double test = (a + b) * c / a;
    }

    private boolean test() {
        return true;
    }
}
