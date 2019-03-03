package fr.themode.asm.method;

import fr.themode.asm.method.module.FieldMethod;
import fr.themode.asm.method.module.StaticField;
import fr.themode.asm.method.module.StaticMethod;

public interface IMethodFinder {

    StaticMethod getStaticMethod(Class clazz, String methodName, Class type, Class... parameters);

    StaticMethod getStaticMethod(String clazz, String methodName, String type, String... parameters);


    StaticField getStaticField(Class clazz, String fieldName, Class type);

    StaticField getStaticField(String clazz, String fieldName, String type);


    FieldMethod getMethod(String methodName, Class type, Class... parameters);

    FieldMethod getMethod(String methodName, String type, String... parameters);

}
