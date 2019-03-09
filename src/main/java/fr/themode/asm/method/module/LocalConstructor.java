package fr.themode.asm.method.module;

import fr.themode.asm.builder.ClassBuilder;
import fr.themode.asm.method.CallableModule;
import fr.themode.asm.method.MethodInstruction;
import fr.themode.asm.utils.DescriptorUtils;

import java.util.LinkedList;

public class LocalConstructor extends CallableModule {

    // TODO utility ? Use with ConstructorBuilder for instantiation

    private static final String TYPE = "void";

    public LocalConstructor(LinkedList<MethodInstruction> instructions, String lastType, String className, String... parameters) {
        super(instructions, lastType, parameters.length);
        addInstruction(className, DescriptorUtils.getDescriptor(TYPE, parameters));
    }

    public LocalConstructor(LinkedList<MethodInstruction> instructions, String lastType, ClassBuilder classBuilder, String... parameters) {
        super(instructions, lastType, parameters.length);
        addInstruction(classBuilder.getInternalName(), DescriptorUtils.getDescriptor(TYPE, parameters));
    }

    public LocalConstructor(LinkedList<MethodInstruction> instructions, String lastType, ClassBuilder classBuilder, Class... parameters) {
        super(instructions, lastType, parameters.length);
        addInstruction(classBuilder.getInternalName(), DescriptorUtils.getDescriptor(void.class, parameters));
    }

    private void addInstruction(String className, String descriptor) {
        getInstructions().add(new MethodInstruction().getLocal());
        getInstructions().add(new MethodInstruction().getSpecialMethod(className, "<init>", descriptor));
        setLastType(TYPE);
    }

}
