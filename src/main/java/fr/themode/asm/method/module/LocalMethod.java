package fr.themode.asm.method.module;

import fr.themode.asm.method.CallableModule;
import fr.themode.asm.method.MethodInstruction;
import fr.themode.asm.utils.DescriptorUtils;

import java.util.LinkedList;

public class LocalMethod extends CallableModule {

    public LocalMethod(LinkedList<MethodInstruction> instructions, String lastType, String className, String methodName, String type, String... parameters) {
        super(instructions, lastType, parameters.length);
        addInstruction(className, methodName, DescriptorUtils.getDescriptor(type, parameters));
        setLastType(type);
    }

    private void addInstruction(String className, String methodName, String descriptor) {
        getInstructions().add(new MethodInstruction().getLocal());
        getInstructions().add(new MethodInstruction().getVirtualMethod(className, methodName, descriptor));
    }

}
