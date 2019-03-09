package fr.themode.asm.method.module;

import fr.themode.asm.method.CallableModule;
import fr.themode.asm.method.MethodInstruction;
import fr.themode.asm.utils.DescriptorUtils;

import java.util.LinkedList;

public class SuperMethod extends CallableModule {

    public SuperMethod(LinkedList<MethodInstruction> instructions, String lastType, String superClass, String methodName, String type, String... parameters) {
        super(instructions, lastType, parameters.length);
        addInstruction(superClass, methodName, DescriptorUtils.getDescriptor(type, parameters));
        setLastType(type);
    }

    private void addInstruction(String className, String methodName, String descriptor) {
        getInstructions().add(new MethodInstruction().getLocal());
        getInstructions().add(new MethodInstruction().getVirtualMethod(className, methodName, descriptor));
    }

}
