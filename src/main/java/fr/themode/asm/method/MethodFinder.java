package fr.themode.asm.method;

import fr.themode.asm.builder.ClassBuilder;
import fr.themode.asm.method.module.*;
import fr.themode.asm.utils.ClassConverter;

import java.util.LinkedList;

public class MethodFinder {

    public static LocalMethod getLocalMethod(String className, String methodName, String type, String... parameters) {
        return new LocalMethod(new LinkedList<>(), null, ClassConverter.getName(className), methodName, type, parameters);
    }

    public static LocalMethod getLocalMethod(ClassBuilder classBuilder, String methodName, Class type, Class... parameters) {
        return getLocalMethod(classBuilder.getInternalName(), methodName, ClassConverter.getName(type), ClassConverter.getNames(parameters));
    }

    public static LocalMethod getLocalMethod(ClassBuilder classBuilder, String methodName, String type, String... parameters) {
        return getLocalMethod(classBuilder.getInternalName(), methodName, type, parameters);
    }


    public static SuperMethod getSuperMethod(String superClass, String methodName, String type, String... parameters) {
        return new SuperMethod(new LinkedList<>(), null, ClassConverter.getName(superClass), methodName, type, parameters);
    }

    public static SuperMethod getSuperMethod(ClassBuilder classBuilder, String methodName, Class type, Class... parameters) {
        return getSuperMethod(classBuilder.getSuperclass(), methodName, ClassConverter.getName(type), ClassConverter.getNames(parameters));
    }

    public static SuperMethod getSuperMethod(ClassBuilder classBuilder, String methodName, String type, String... parameters) {
        return getSuperMethod(classBuilder.getSuperclass(), methodName, type, parameters);
    }


    public static InitializationMethod newInstance(String className, String... parameters) {
        return new InitializationMethod(new LinkedList<>(), null, className, parameters);
    }

    public static InitializationMethod newInstance(Class className, Class... parameters) {
        return newInstance(ClassConverter.getName(className), ClassConverter.getNames(parameters));
    }


    public static StaticMethod getStaticMethod(String clazz, String methodName, String type, String... parameters) {
        return new StaticMethod(new LinkedList<>(), null, clazz, methodName, type, parameters);
    }

    public static StaticMethod getStaticMethod(Class clazz, String methodName, Class type, Class... parameters) {
        return getStaticMethod(ClassConverter.getName(clazz), methodName, ClassConverter.getName(type), ClassConverter.getNames(parameters));
    }


    public static StaticField getStaticField(String clazz, String fieldName, String type) {
        return new StaticField(new LinkedList<>(), null, clazz, fieldName, type);
    }

    public static StaticField getStaticField(Class clazz, String fieldName, Class type) {
        return getStaticField(ClassConverter.getName(clazz), fieldName, ClassConverter.getName(type));
    }

}
