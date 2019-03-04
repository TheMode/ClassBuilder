package fr.themode.asm;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.util.ASMifier;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;

public class ASMifierDemo {

    public static void main(String[] args){
        printTest();
    }

    private static void printSimple() {
        try {
            new ClassReader(SimpleClass.class.getResourceAsStream("SimpleClass.class"))
                    .accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out)), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printTest() {
        try {
            new ClassReader(FlowControlTest.class.getResourceAsStream("FlowControlTest.class"))
                    .accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out)), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
