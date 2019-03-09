package fr.themode.asm.builder;

import fr.themode.asm.method.CallableMethod;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class Statement implements Opcodes {

    private StatementCallback callback;

    private int line;

    public Statement(StatementCallback callback) {
        this.callback = callback;
    }

    public static Statement[] multi(Statement... statements) {
        return statements;
    }

    public static <T> Statement createVariable(Class<T> type, String varName, Parameter defaultValue) {
        return new Statement((classBuilder, method, visitor) -> {
            int storeIndex = method.generateStoreIndex();
            defaultValue.push(classBuilder, method, visitor);
            visitor.visitVarInsn(ASTORE, storeIndex);
            method.setVarStoreIndex(varName, storeIndex);
        });
    }

    // Debug information
    public void setLineNumber(int line) {
        this.line = line;
    }

    public static <T> Statement setVariable(String varName, Parameter parameter) {
        return new Statement((classBuilder, method, visitor) -> {
            int index = method.getVarStoreIndex(varName);
            parameter.push(classBuilder, method, visitor);
            visitor.visitVarInsn(ASTORE, index);
        });
    }

    public static <T> Statement createVariable(Class<T> type, String varName) {
        return createVariable(type, varName, Parameter.constant(null));
    }

    public static <T> Statement setField(String fieldName, Parameter parameter) {
        return new Statement((classBuilder, method, visitor) -> {
            boolean isStatic = classBuilder.isFieldStatic(fieldName);
            String type = classBuilder.getFieldDescriptor(fieldName);
            if (!isStatic)
                visitor.visitVarInsn(ALOAD, 0);
            parameter.push(classBuilder, method, visitor);
            visitor.visitFieldInsn(isStatic ? PUTSTATIC : PUTFIELD, classBuilder.getInternalName(), fieldName, type);
        });
    }

    public static <T> Statement callMethod(CallableMethod callableMethod, Parameter... parameters) {
        return new Statement((classBuilder, method, visitor) -> {
            callableMethod.load(classBuilder, method, visitor, parameters);
        });
    }

    public void append(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor) {
        Label label = new Label();
        visitor.visitLabel(label);
        visitor.visitLineNumber(line, label);
        this.callback.append(classBuilder, method, visitor);
    }

    public interface StatementCallback {
        void append(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor);
    }

}
