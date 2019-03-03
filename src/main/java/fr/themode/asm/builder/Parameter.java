package fr.themode.asm.builder;

import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class Parameter implements Opcodes {

    private ParameterType type;
    private String name;
    private Object value;

    private Parameter(ParameterType type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public static Parameter local() {
        return new Parameter(ParameterType.LOCAL, null, null);
    }

    public static Parameter field(String fieldName) {
        return new Parameter(ParameterType.FIELD, fieldName, null);
    }

    public static Parameter variable(String varName) {
        return new Parameter(ParameterType.VARIABLE, varName, null);
    }

    public static Parameter argument(int index) {
        return new Parameter(ParameterType.ARGUMENT, null, index);
    }

    public static Parameter constant(Object constant) {
        return new Parameter(ParameterType.CONSTANT, null, constant);
    }

    public void push(MethodBuilder method, MethodVisitor visitor) {
        switch (type) {
            case LOCAL:
                visitor.visitVarInsn(ALOAD, 0);
                break;
            case FIELD:
                // TODO fix load (if both parameters need it, load before)
                visitor.visitVarInsn(ALOAD, 0);
                // TODO get descriptor and internal class name
                visitor.visitFieldInsn(GETFIELD, "fr/themode/asm/SimpleClass", name, "F");
                break;
            case VARIABLE:
                int index = method.getVarStoreIndex(name);
                visitor.visitVarInsn(ALOAD, index);
                break;
            case ARGUMENT:
                visitor.visitVarInsn(ALOAD, (int) value + (method.isStatic() ? -1 : 0));
                break;
            case CONSTANT:
                // TODO CONST_1/2/3 etc...
                visitor.visitLdcInsn(value);
                break;
        }
        // TODO parameter push
    }

    public ParameterType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public enum ParameterType {
        LOCAL, FIELD, VARIABLE, ARGUMENT, CONSTANT;
    }

}
