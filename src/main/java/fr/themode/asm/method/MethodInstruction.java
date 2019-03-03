package fr.themode.asm.method;

import fr.themode.asm.utils.ClassConverter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class MethodInstruction implements Opcodes {
    private static final int LOCAL = 0;

    private static final int STATIC_FIELD = 1;
    private static final int STATIC_METHOD = 2;

    private static final int VIRTUAL_METHOD = 3;

    private int opcode;

    // Static information
    private String clazz;
    private String descriptor;

    private String identifier;

    public void load(MethodVisitor visitor) {
        switch (opcode) {
            case LOCAL:
                // push "this" to the stack
                visitor.visitVarInsn(ALOAD, 0);
                break;
            case STATIC_FIELD:
                visitor.visitFieldInsn(GETSTATIC, clazz, identifier, descriptor);
                break;
            case STATIC_METHOD:
                visitor.visitMethodInsn(INVOKESTATIC, clazz, identifier, descriptor, false);
                break;
            case VIRTUAL_METHOD:
                visitor.visitMethodInsn(INVOKEVIRTUAL, clazz, identifier, descriptor, false);
                break;
        }
    }

    public MethodInstruction getLocal() {
        opcode = LOCAL;
        return this;
    }

    public MethodInstruction getStaticField(String clazz, String fieldName, String descriptor) {
        opcode = STATIC_FIELD;
        fill(clazz, fieldName, descriptor);
        return this;
    }

    public MethodInstruction getStaticField(Class clazz, String fieldName, String descriptor) {
        return getStaticField(ClassConverter.getName(clazz), fieldName, descriptor);
    }

    public MethodInstruction getStaticMethod(String clazz, String methodName, String descriptor) {
        opcode = STATIC_METHOD;
        fill(ClassConverter.getName(clazz), methodName, descriptor);
        return this;
    }

    public MethodInstruction getStaticMethod(Class clazz, String methodName, String descriptor) {
        return getStaticMethod(ClassConverter.getName(clazz), methodName, descriptor);
    }

    public MethodInstruction getMethod(String clazz, String methodName, String descriptor) {
        opcode = VIRTUAL_METHOD;
        fill(ClassConverter.getName(clazz), methodName, descriptor);
        return this;
    }

    public MethodInstruction getMethod(Class clazz, String methodName, String descriptor) {
        return getMethod(ClassConverter.getName(clazz), methodName, descriptor);
    }

    private void fill(String clazz, String identifier, String descriptor) {
        this.clazz = clazz;
        this.identifier = identifier;
        this.descriptor = descriptor;
    }

}