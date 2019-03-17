package fr.themode.asm.method;

import fr.themode.asm.builder.ClassBuilder;
import fr.themode.asm.builder.MethodBuilder;
import fr.themode.asm.builder.Parameter;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.util.Collection;

public class CallableMethod {

    private Collection<MethodInstruction> instructions;
    private MethodInstruction invokable;

    private String type;
    private int count;

    public CallableMethod(Collection<MethodInstruction> instructions, MethodInstruction invokable, String type, int argsCount) {
        this.instructions = instructions;
        this.invokable = invokable;

        this.type = type;
        this.count = argsCount;
    }

    public void load(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor, Parameter... parameters) {
        int length = parameters.length;
        if (count != length)
            throw new IllegalArgumentException("Method should take " + count + " args instead of " + length);

        for (MethodInstruction instruction : instructions) {
            instruction.load(visitor);
        }

        for (Parameter parameter : parameters) {
            parameter.push(classBuilder, method, visitor);
        }

        invokable.load(visitor);
    }

    public String getType() {
        return type;
    }

    public int getArgumentCount() {
        return count;
    }
}
