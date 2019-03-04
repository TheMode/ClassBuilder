package fr.themode.asm.method;

import fr.themode.asm.builder.ClassBuilder;
import fr.themode.asm.method.module.LocalMethod;
import fr.themode.asm.method.module.StaticField;
import fr.themode.asm.method.module.StaticMethod;

import java.util.LinkedList;

public class MethodFinder {

    public static LocalMethod getLocalMethod(ClassBuilder classBuilder, String methodName, Class type, Class... parameters) {
        return new LocalMethod(new LinkedList<>(), null, classBuilder, methodName, type, parameters);
    }

    public static LocalMethod getLocalMethod(ClassBuilder classBuilder, String methodName, String type, String... parameters) {
        return new LocalMethod(new LinkedList<>(), null, classBuilder, methodName, type, parameters);
    }

    public static LocalMethod getLocalMethod(String className, String methodName, String type, String... parameters) {
        return new LocalMethod(new LinkedList<>(), null, className, methodName, type, parameters);
    }


    public static StaticMethod getStaticMethod(Class clazz, String methodName, Class type, Class... parameters) {
        return new StaticMethod(new LinkedList<>(), null, clazz, methodName, type, parameters);
    }

    public static StaticMethod getStaticMethod(String clazz, String methodName, String type, String... parameters) {
        return new StaticMethod(new LinkedList<>(), null, clazz, methodName, type, parameters);
    }


    public static StaticField getStaticField(Class clazz, String fieldName, Class type) {
        return new StaticField(new LinkedList<>(), null, clazz, fieldName, type);
    }

    public static StaticField getStaticField(String clazz, String fieldName, String type) {
        return new StaticField(new LinkedList<>(), null, clazz, fieldName, type);
    }

}
