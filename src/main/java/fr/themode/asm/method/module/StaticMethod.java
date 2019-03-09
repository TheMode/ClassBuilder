package fr.themode.asm.method.module;

import fr.themode.asm.method.CallableModule;
import fr.themode.asm.method.MethodInstruction;
import fr.themode.asm.utils.DescriptorUtils;

import java.util.LinkedList;

public class StaticMethod extends CallableModule {

    public StaticMethod(LinkedList<MethodInstruction> instructions, String lastType, String clazz, String methodName, String type, String... parameters) {
        super(instructions, lastType, parameters.length);
        addInstruction(clazz, methodName, DescriptorUtils.getDescriptor(type, parameters));
        setLastType(type);
    }

    private void addInstruction(String clazz, String methodName, String descriptor) {
        getInstructions().add(new MethodInstruction().getStaticMethod(clazz, methodName, descriptor));
    }

}
