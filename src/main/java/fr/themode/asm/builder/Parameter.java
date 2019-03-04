package fr.themode.asm.builder;

import fr.themode.asm.method.CallableMethod;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class Parameter implements Opcodes {

    private ParameterType type;
    private String name;
    private Object value;

    // Method
    private CallableMethod method;
    private Parameter[] parameters;

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

    public static Parameter method(CallableMethod method, Parameter... parameters) {
        Parameter parameter = new Parameter(ParameterType.METHOD, null, null);
        parameter.method = method;
        parameter.parameters = parameters;
        return parameter;
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
                // TODO static offset ?
                visitor.visitVarInsn(ALOAD, (int) value + (method.isStatic() ? 0 : 1));
                break;
            case METHOD:
                this.method.load(method, visitor, parameters);
                break;
            case CONSTANT:
                // TODO CONST_1/2/3 null etc...
                visitor.visitLdcInsn(value);
                break;
        }
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
        LOCAL, FIELD, VARIABLE, ARGUMENT, METHOD, CONSTANT
    }

}
