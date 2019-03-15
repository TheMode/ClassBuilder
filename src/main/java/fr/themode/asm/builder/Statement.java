package fr.themode.asm.builder;

import fr.themode.asm.method.CallableMethod;
import fr.themode.asm.utils.ClassConverter;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class Statement implements Opcodes {

    private StatementCallback callback;
    private Label label;

    private int line;

    public Statement(StatementCallback callback) {
        this.callback = callback;
        this.label = new Label();
    }

    public static Statement[] multi(Statement... statements) {
        return statements;
    }

    public static Statement createVariable(String type, String varName, Parameter defaultValue) {
        return new Statement((classBuilder, method, visitor) -> {
            int storeIndex = method.generateStoreIndex();
            defaultValue.push(classBuilder, method, visitor);
            visitor.visitVarInsn(ASTORE, storeIndex);
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
        visitor.visitLabel(label);
        visitor.visitLineNumber(line, label);
        this.callback.append(classBuilder, method, visitor);
    }

    public Label getLabel() {
        return label;
    }

    public interface StatementCallback {
        void append(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor);
    }

}
