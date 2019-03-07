package fr.themode.asm.utils;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.util.ASMifier;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;

public class ClassPrint {

    public static void printASM(byte[] bytes) {
        new ClassReader(bytes)
                .accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out)), 0);
    }

    public static void printASM(Class clazz) {
        try {
            new ClassReader(clazz.getResourceAsStream(clazz.getSimpleName() + ".class"))
                    .accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out)), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
