package fr.themode.asm.method;

import fr.themode.asm.builder.MethodBuilder;
import fr.themode.asm.builder.Parameter;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.util.Collection;

public class CallableMethod {

    private Collection<MethodInstruction> instructions;
    private MethodInstruction invokable;

    private int count;

    public CallableMethod(Collection<MethodInstruction> instructions, MethodInstruction invokable, int argsCount) {
        this.instructions = instructions;
        this.invokable = invokable;

        this.count = argsCount;
    }

    public void load(MethodBuilder method, MethodVisitor visitor, Parameter... parameters) {
        int length = parameters.length;
        if (count != length)
            throw new IllegalArgumentException("Method should take " + count + " args instead of " + length);
        for (MethodInstruction instruction : instructions) {
            instruction.load(visitor);
        }

        for (Parameter parameter : parameters) {
            parameter.push(method, visitor);
        }

        invokable.load(visitor);
    }

}
