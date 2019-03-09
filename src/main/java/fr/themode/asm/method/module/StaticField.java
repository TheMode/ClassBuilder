package fr.themode.asm.method.module;

import fr.themode.asm.method.FinderModule;
import fr.themode.asm.method.MethodInstruction;
import fr.themode.asm.utils.DescriptorUtils;

import java.util.LinkedList;

public class StaticField extends FinderModule {

    public StaticField(LinkedList<MethodInstruction> instructions, String lastType, String clazz, String fieldName, String type) {
        super(instructions, lastType);
        addInstruction(clazz, fieldName, DescriptorUtils.getDescriptor(type));
        setLastType(type);
    }

    private void addInstruction(String clazz, String fieldName, String descriptor) {
        getInstructions().add(new MethodInstruction().getStaticField(clazz, fieldName, descriptor));
    }
}