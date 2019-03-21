package fr.themode.asm.builder;

import fr.themode.asm.builder.flow.FlowControl;
import fr.themode.asm.method.CallableMethod;
import fr.themode.asm.utils.ClassConverter;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class Statement implements Opcodes {

    private static Label nextLabel;

    private StatementCallback callback;

    private int line;

    public Statement(StatementCallback callback) {
        this.callback = callback;
    }

    public static Statement[] multi(Statement... statements) {
        return statements;
    }

    public static Statement createVariable(String type, String varName, Parameter defaultValue) {
        return new Statement((classBuilder, method, visitor) -> {
            int storeIndex = method.generateStoreIndex();
            defaultValue.push(classBuilder, method, visitor);
            visitor.visitVarInsn(defaultValue.getStoreOpcode(classBuilder, method), storeIndex);

            method.setVarStoreIndex(varName, storeIndex);
            method.setVarType(varName, ClassConverter.getName(type));
        });
    }

    public static Statement createVariable(Class type, String varName, Parameter defaultValue) {
        return createVariable(ClassConverter.getName(type), varName, defaultValue);
    }

    public static Statement createVariable(String type, String varName) {
        return createVariable(ClassConverter.getName(type), varName, Parameter.literal(null));
    }

    public static Statement createVariable(Class type, String varName) {
        return createVariable(ClassConverter.getName(type), varName, Parameter.literal(null));
    }

    public static Statement setVariable(String varName, Parameter parameter) {
        return new Statement((classBuilder, method, visitor) -> {
            int index = method.getVarStoreIndex(varName);
            parameter.push(classBuilder, method, visitor);
            visitor.visitVarInsn(parameter.getStoreOpcode(classBuilder, method), index);
        });
    }

    public static Statement setField(String fieldName, Parameter parameter) {
        return new Statement((classBuilder, method, visitor) -> {
            boolean isStatic = classBuilder.isFieldStatic(fieldName);
            String type = classBuilder.getFieldDescriptor(fieldName);
            if (!isStatic)
                visitor.visitVarInsn(ALOAD, 0);
            parameter.push(classBuilder, method, visitor);
            visitor.visitFieldInsn(isStatic ? PUTSTATIC : PUTFIELD, classBuilder.getInternalName(), fieldName, type);
        });
    }

    public static Statement callMethod(CallableMethod callableMethod, Parameter... parameters) {
        return new Statement((classBuilder, method, visitor) -> {
            callableMethod.load(classBuilder, method, visitor, parameters);
        });
    }

    public static Statement createFlowControl(FlowControl flowControl) {
        return new Statement((classBuilder, method, visitor) -> {
            flowControl.loadToWriter(classBuilder, method, visitor);
        });
    }

    public static Statement returnParameter(Parameter parameter) {
        return new Statement((classBuilder, method, visitor) -> {
            parameter.push(classBuilder, method, visitor);
            visitor.visitInsn(parameter.getReturnOpcode(classBuilder, method));
        });
    }

    public static Statement returnVoid() {
        return new Statement((classBuilder, method, visitor) -> {
            visitor.visitInsn(RETURN);
        });
    }

    public static void setNextLabel(Label nextLabel) {
        Statement.nextLabel = nextLabel;
    }

    public void append(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor) {
        Label label = null;
        if (nextLabel != null) {
            label = nextLabel;
            nextLabel = null;
        } else {
            label = new Label();
        }

        visitor.visitLabel(label);
        visitor.visitLineNumber(line, label);

        this.callback.append(classBuilder, method, visitor);
    }

    // Debug information
    public void setLineNumber(int line) {
        this.line = line;
    }

    public interface StatementCallback {
        void append(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor);
    }

}
