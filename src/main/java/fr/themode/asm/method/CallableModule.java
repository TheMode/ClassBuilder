package fr.themode.asm.method;

import java.util.LinkedList;

public class CallableModule extends FinderModule {

    private int count;

    public CallableModule(LinkedList<MethodInstruction> instructions, String lastType, int count) {
        super(instructions, lastType);
        this.count = count;
    }

    public CallableMethod asCallable() {
        LinkedList<MethodInstruction> instructions = getInstructions();
        MethodInstruction invokable = instructions.pollLast();
        return new CallableMethod(instructions, invokable, getLastType(), count);
    }
}
