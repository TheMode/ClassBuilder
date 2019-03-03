package fr.themode.asm.method.module;

import fr.themode.asm.method.FinderModule;
import fr.themode.asm.method.MethodInstruction;
import fr.themode.asm.utils.ClassConverter;
import fr.themode.asm.utils.DescriptorUtils;

import java.util.LinkedList;

public class StaticField extends FinderModule {

    public StaticField(LinkedList<MethodInstruction> instructions, String lastType, String clazz, String fieldName, String type) {
        super(instructions, lastType);
        setLastType(ClassConverter.getName(type));
        instructions.add(new MethodInstruction().getStaticField(ClassConverter.getName(clazz), fieldName, DescriptorUtils.getDescriptor(type)));
    }

    public StaticField(LinkedList<MethodInstruction> instructions, String lastType, Class clazz, String fieldName, Class type) {
        super(instructions, lastType);
        setLastType(ClassConverter.getName(type));
        instructions.add(new MethodInstruction().getStaticField(clazz, fieldName, DescriptorUtils.getDescriptor(type)));
    }
}