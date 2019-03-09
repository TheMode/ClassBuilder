package fr.themode.asm.test;

public class SimpleClass {

    private float localVar = 5;
    private float testVar = 20;

    public SimpleClass() {
        System.out.println("This: " + this);
        if (localVar > 2.5f) {
            System.out.println("Hey: " + localVar);
            localVar = 99999;
        } else {
            System.out.println("Sorry no");
        }
        System.out.println("Hey i'm called");

        {
            System.out.println("Hey i'm called 2");
        }
    }

    public int testReturn(){
        return 5;
    }

    public static SimpleClass returnTest(){
        return new SimpleClass();
    }

    public void test(String arg1, int arg2){
        System.out.println(arg2);
        test("string,", 50);
        int i = SimpleClass.returnTest().testReturn();
        System.out.println(Math.max(2, 5));
        testReturn();
        testLocal("a", 1);
        return;
    }

    public void testLocal(String a1, int a2){
        String test = "aa";
        String test2 = test;
        test = test2;
    }

}
