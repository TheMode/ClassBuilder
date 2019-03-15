package fr.themode.asm.builder;

import fr.themode.asm.method.CallableMethod;
import fr.themode.asm.utils.DescriptorUtils;
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

    public static Parameter literal(int value) {
        return LDCLiteral(value);
    }

    public static Parameter literal(float value) {
        return LDCLiteral(value);
    }

    public static Parameter literal(long value) {
        return LDCLiteral(value);
    }

    public static Parameter literal(double value) {
        return LDCLiteral(value);
    }

    public static Parameter literal(String value) {
        return LDCLiteral(value);
    }

    private static Parameter LDCLiteral(Object constant) {
        return new Parameter(ParameterType.LITERAL, null, constant);
    }

    public static Parameter method(CallableMethod method, Parameter... parameters) {
        Parameter parameter = new Parameter(ParameterType.METHOD, null, null);
        parameter.method = method;
        parameter.parameters = parameters;
        return parameter;
    }

    // TODO operator (for primitives and string with StringBuilder)

    public void push(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor) {
        switch (type) {
            case LOCAL:
                visitor.visitVarInsn(ALOAD, 0);
                break;
            case FIELD:
                boolean isStatic = classBuilder.isFieldStatic(name);
                String type = classBuilder.getFieldDescriptor(name);
                if (!isStatic)
                    visitor.visitVarInsn(ALOAD, 0);
                visitor.visitFieldInsn(isStatic ? GETSTATIC : GETFIELD, classBuilder.getInternalName(), name, type);
                break;
            case VARIABLE:
                int index = method.getVarStoreIndex(name);
                // TODO iload etc... (primitives check)
                visitor.visitVarInsn(ALOAD, index);
                break;
            case ARGUMENT:
                visitor.visitVarInsn(ALOAD, (int) value + (method.isStatic() ? 0 : 1));
                break;
            case METHOD:
                this.method.load(classBuilder, method, visitor, parameters);
                break;
            case LITERAL:
                // TODO CONST_1/2/3 etc...
                visitor.visitLdcInsn(value == null ? ACONST_NULL : value);
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

    protected String getTypeDescriptor(ClassBuilder classBuilder, MethodBuilder method) {
        switch (getType()) {
            case LOCAL:
                return DescriptorUtils.getDescriptor(classBuilder.getInternalName());
            case FIELD:
                return classBuilder.getField(name).getDescriptor();
            case VARIABLE:
                return DescriptorUtils.getDescriptor(method.getVarType(name));
            case ARGUMENT:
                return DescriptorUtils.getDescriptor(method.getParameters()[(int) value]);
            case METHOD:
                return DescriptorUtils.getDescriptor(method.getType());
            case LITERAL:
                return DescriptorUtils.getDescriptor(value.getClass());

        }
        return null;
    }

    public enum ParameterType {
        LOCAL, FIELD, VARIABLE, ARGUMENT, METHOD, LITERAL
    }

}
