package fr.themode.asm.method;

import fr.themode.asm.method.module.FieldMethod;
import fr.themode.asm.method.module.StaticField;
import fr.themode.asm.method.module.StaticMethod;
import fr.themode.asm.utils.ClassConverter;

import java.util.LinkedList;

public class FinderModule implements IMethodFinder {

    private LinkedList<MethodInstruction> instructions;

    private String lastType;

    public FinderModule(LinkedList<MethodInstruction> instructions, String lastType) {
        this.instructions = instructions;
        this.lastType = lastType;
    }

    @Override
    public StaticMethod getStaticMethod(Class clazz, String methodName, Class type, Class... parameters) {
        return new StaticMethod(getInstructions(), getLastType(), clazz, methodName, type, parameters);
    }

    @Override
    public StaticMethod getStaticMethod(String clazz, String methodName, String type, String... parameters) {
        return new StaticMethod(getInstructions(), getLastType(), clazz, methodName, type, parameters);
    }

    @Override
    public StaticField getStaticField(Class clazz, String fieldName, Class type) {
        return new StaticField(getInstructions(), getLastType(), clazz, fieldName, type);
    }

    @Override
    public StaticField getStaticField(String clazz, String fieldName, String type) {
        return new StaticField(getInstructions(), getLastType(), clazz, fieldName, type);
    }

    @Override
    public FieldMethod getMethod(String methodName, Class type, Class... parameters) {
        return new FieldMethod(getInstructions(), getLastType(), methodName, type, parameters);
    }

    @Override
    public FieldMethod getMethod(String methodName, String type, String... parameters) {
        return new FieldMethod(getInstructions(), getLastType(), methodName, type, parameters);
    }

    protected LinkedList<MethodInstruction> getInstructions() {
        return instructions;
    }

    protected void setLastType(String lastType) {
        this.lastType = ClassConverter.getName(lastType);
    }

    protected void setLastType(Class lastType) {
        this.lastType = ClassConverter.getName(lastType);
    }

    protected String getLastType() {
        return lastType;
    }
}