package fr.themode.asm.method.module;

import fr.themode.asm.method.CallableModule;
import fr.themode.asm.method.MethodInstruction;
import fr.themode.asm.utils.DescriptorUtils;

import java.util.LinkedList;

public class InitializationMethod extends CallableModule {

    private static final String TYPE = "void";

    public InitializationMethod(LinkedList<MethodInstruction> instructions, String lastType, String className, String... parameters) {
        super(instructions, lastType, parameters.length);
        addInstruction(className, DescriptorUtils.getDescriptor(TYPE, parameters));
    }

    private void addInstruction(String className, String descriptor) {
        getInstructions().add(new MethodInstruction().initialization(className, descriptor));
        setLastType(TYPE);
    }

}
