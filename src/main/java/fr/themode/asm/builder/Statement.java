package fr.themode.asm.builder;

import fr.themode.asm.method.CallableMethod;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class Statement implements Opcodes {

    private StatementCallback callback;

    private int line;

    public Statement(StatementCallback callback) {
        this.callback = callback;
    }

    public void append(MethodBuilder method, MethodVisitor visitor) {
        // TODO line support
        this.callback.append(method, visitor);
    }

    // Debug information
    public void setLineNumber(int line) {
        this.line = line;
    }

    public static <T> Statement createVariable(Class<T> type, String varName, T defaultValue) {
        return new Statement((method, visitor) -> {
            int storeIndex = method.generateStoreIndex();
            visitor.visitLdcInsn(defaultValue != null ? defaultValue : ACONST_NULL);
            visitor.visitVarInsn(ASTORE, storeIndex);
            method.setVarStoreIndex(varName, storeIndex);
        });
    }

    public static <T> Statement createVariable(Class<T> type, String varName) {
        return createVariable(type, varName, null);
    }

    public static <T> Statement callMethod(CallableMethod callableMethod, Parameter... parameters) {
        return new Statement((method, visitor) -> {
            callableMethod.load(method, visitor, parameters);
        });
    }

    public interface StatementCallback {
        void append(MethodBuilder method, MethodVisitor visitor);
    }

}
