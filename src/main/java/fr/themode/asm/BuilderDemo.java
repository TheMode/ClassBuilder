package fr.themode.asm;

import fr.themode.asm.builder.*;
import fr.themode.asm.builder.flow.FlowControl;
import fr.themode.asm.enums.Modifier;
import fr.themode.asm.method.CallableMethod;
import fr.themode.asm.method.MethodFinder;
import fr.themode.asm.utils.ClassVersion;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static fr.themode.asm.builder.MethodBuilder.createMethod;

public class BuilderDemo {

    public static void main(String[] args) {
        ClassBuilder classBuilder = ClassBuilder.createClass(ClassVersion.V1_8, "com.package.example.SampleClass");
        classBuilder.setModifiers(Modifier.PUBLIC);

        //FieldBuilder field = createField(String.class, "fieldName", Parameter.literal("value"));
        //field.setModifiers(Modifier.PUBLIC, Modifier.STATIC);
        //classBuilder.addField(field);

        ConstructorBuilder constructor = ConstructorBuilder.createConstructor();
        constructor.setModifiers(Modifier.PUBLIC);

        classBuilder.addConstructor(constructor);

        MethodBuilder method = createMethod("main", void.class, String.class, int.class, Float.class);
        method.setModifiers(Modifier.PUBLIC);
        method.addStatement(Statement.createVariable(String.class, "varTest", Parameter.literal("aa")));

        CallableMethod print = MethodFinder.getStaticField(System.class, "out", PrintStream.class).getMethod("println", void.class, String.class).asCallable();

        //BooleanExpression[] conditions = BooleanExpression.multi(BooleanExpression.not_equal(Parameter.variable("varTest"), Parameter.literal(2)));
        BooleanExpression[] conditions = BooleanExpression.multi(BooleanExpression.not_null(Parameter.variable("varTest")));
        Statement[] statements = Statement.multi(Statement.setVariable("varTest", Parameter.literal(5)), Statement.callMethod(print, Parameter.literal("CONDITION TRUE")));
        FlowControl flow = FlowControl.if_(conditions, statements).else_(Statement.callMethod(print, Parameter.literal("CONDITION FALSE")));
        method.addStatements(Statement.createFlowControl(flow));

        method.addStatement(Statement.setVariable("varTest", Parameter.literal(3)));

        //method.addStatement(Statement.setField("fieldName", Parameter.literal("im a const")));//method.addStatement(Statement.callMethod(print, Parameter.argument(0)));

        //CallableMethod argMethod = MethodFinder.getStaticMethod(UUID.class, "randomUUID", UUID.class).getMethod("toString", String.class).asCallable();
        // CallableMethod argMethod = MethodFinder.getSuperMethod(classBuilder, "toString", String.class).asCallable();
        // method.addStatement(Statement.callMethod(method.asCallable(classBuilder), Parameter.method(argMethod), Parameter.constant(2)));

        // CallableMethod loop = MethodFinder.getStaticMethod(classBuilder.getInternalName(), "main", void.class.getName(), String.class.getName(), int.class.getName()).asCallable();
        // method.addStatement(Statement.callMethod(loop, Parameter.constant("IM A CONST"), Parameter.constant(2)));

        classBuilder.addMethod(method);

        Class result = classBuilder.load();

        try {
            Object obj = result.newInstance();

            Method m = result.getMethod("main", String.class, int.class, Float.class);
            m.setAccessible(true);
            m.invoke(obj, "stringArg", 554, 2f);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
