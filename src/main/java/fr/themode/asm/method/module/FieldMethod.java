package fr.themode.asm.method.module;

import fr.themode.asm.method.CallableModule;
import fr.themode.asm.method.MethodInstruction;
import fr.themode.asm.utils.DescriptorUtils;

import java.util.LinkedList;

public class FieldMethod extends CallableModule {

    public FieldMethod(LinkedList<MethodInstruction> instructions, String lastType, String methodName, String type, String... parameters) {
        super(instructions, lastType, parameters.length);
        addInstruction(methodName, DescriptorUtils.getDescriptor(type, parameters));
        setLastType(type);
    }

    private void addInstruction(String methodName, String descriptor) {
        getInstructions().add(new MethodInstruction().getVirtualMethod(getLastType(), methodName, descriptor));
    }

}