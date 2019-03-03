package fr.themode.asm;

import fr.themode.asm.builder.*;
import fr.themode.asm.enums.Modifier;
import fr.themode.asm.method.CallableMethod;
import fr.themode.asm.method.MethodFinder;
import fr.themode.asm.utils.ClassVersion;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static fr.themode.asm.builder.FieldBuilder.createField;
import static fr.themode.asm.builder.MethodBuilder.createMethod;

public class BuilderDemo {

    public static void main(String[] args) {
        ClassBuilder classBuilder = ClassBuilder.createClass(ClassVersion.V1_8, "com.package.example.SampleClass");
        classBuilder.setModifiers(Modifier.PUBLIC);

        FieldBuilder field = createField(String.class, "fieldName");
        field.setModifiers(Modifier.PUBLIC);
        classBuilder.addField(field);

        MethodBuilder method = createMethod("main", void.class, String.class, int.class);
        method.setModifiers(Modifier.PUBLIC, Modifier.STATIC);
        method.addStatement(Statement.createVariable(String.class, "stringTest", "default value"));

        CallableMethod print = MethodFinder.getStaticField(System.class, "out", PrintStream.class).getMethod("println", void.class, String.class).asCallable();
        method.addStatement(Statement.callMethod(print, Parameter.argument(0)));
        // method.addStatement(Statement.callMethod(print, Parameter.constant("IM A CONST")));

        // CallableMethod loop = MethodFinder.getStaticMethod(classBuilder.getInternalName(), "main", void.class.getName(), String.class.getName(), int.class.getName()).asCallable();
        // method.addStatement(Statement.callMethod(loop, Parameter.constant("IM A CONST"), Parameter.constant(2)));

        classBuilder.addMethod(method);

        Class result = classBuilder.load();

        try {
            Method m = result.getMethod("main", String.class, int.class);
            m.setAccessible(true);
            m.invoke(null, "stringArg", 554);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
