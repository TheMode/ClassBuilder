package fr.themode.asm.method.module;

import fr.themode.asm.builder.ClassBuilder;
import fr.themode.asm.method.CallableModule;
import fr.themode.asm.method.MethodInstruction;
import fr.themode.asm.utils.DescriptorUtils;

import java.util.LinkedList;

public class LocalMethod extends CallableModule {

    public LocalMethod(LinkedList<MethodInstruction> instructions, String lastType, ClassBuilder classBuilder, String methodName, String type, String... parameters) {
        super(instructions, lastType, parameters.length);
        addInstruction(classBuilder, methodName, DescriptorUtils.getDescriptor(type, parameters));
        setLastType(type);
    }

    public LocalMethod(LinkedList<MethodInstruction> instructions, String lastType, ClassBuilder classBuilder, String methodName, Class type, Class... parameters) {
        super(instructions, lastType, parameters.length);
        addInstruction(classBuilder, methodName, DescriptorUtils.getDescriptor(type, parameters));
        setLastType(type);
    }

    private void addInstruction(ClassBuilder classBuilder, String methodName, String descriptor) {
        getInstructions().add(new MethodInstruction().getLocal());
        getInstructions().add(new MethodInstruction().getMethod(classBuilder.getInternalName(), methodName, descriptor));
    }

}
