package fr.themode.asm.method;

import fr.themode.asm.utils.ClassConverter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class MethodInstruction implements Opcodes {
    private static final int LOCAL = 0;

    private static final int STATIC_FIELD = 1;
    private static final int STATIC_METHOD = 2;

    private static final int VIRTUAL_METHOD = 3;
    private static final int SPECIAL_METHOD = 4;

    private static final int INITIALIZATION = 5;

    private int opcode;

    // Information
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
            case SPECIAL_METHOD:
                visitor.visitMethodInsn(INVOKESPECIAL, clazz, identifier, descriptor, false);
                break;
            case INITIALIZATION:
                visitor.visitTypeInsn(NEW, clazz);
                visitor.visitInsn(DUP);
                visitor.visitMethodInsn(INVOKESPECIAL, clazz, "<init>", descriptor, false);
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

    public MethodInstruction getStaticMethod(String clazz, String methodName, String descriptor) {
        opcode = STATIC_METHOD;
        fill(ClassConverter.getName(clazz), methodName, descriptor);
        return this;
    }

    public MethodInstruction getVirtualMethod(String clazz, String methodName, String descriptor) {
        opcode = VIRTUAL_METHOD;
        fill(ClassConverter.getName(clazz), methodName, descriptor);
        return this;
    }

    public MethodInstruction getSpecialMethod(String clazz, String methodName, String descriptor) {
        opcode = SPECIAL_METHOD;
        fill(ClassConverter.getName(clazz), methodName, descriptor);
        return this;
    }

    public MethodInstruction initialization(String clazz, String descriptor) {
        opcode = INITIALIZATION;
        fill(ClassConverter.getName(clazz), null, descriptor);
        return this;
    }

    private void fill(String clazz, String identifier, String descriptor) {
        this.clazz = clazz;
        this.identifier = identifier;
        this.descriptor = descriptor;
    }

}