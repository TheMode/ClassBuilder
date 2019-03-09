package fr.themode.asm.builder;

import fr.themode.asm.enums.Modifier;
import fr.themode.asm.method.CallableMethod;
import fr.themode.asm.utils.ClassConverter;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.util.Arrays;
import java.util.List;

public class ConstructorBuilder extends MethodBuilder {

    // TODO asCallable fix ? (check if work)

    private List<Modifier> legalModifiers = Arrays.asList(Modifier.PUBLIC, Modifier.PRIVATE, Modifier.PROTECTED);

    protected ConstructorBuilder(String... parameters) {
        super("<init>", "void", parameters);
    }

    public static ConstructorBuilder createConstructor(String... parameters) {
        return new ConstructorBuilder(parameters);
    }

    public static ConstructorBuilder createConstructor(Class... parameters) {
        return new ConstructorBuilder(ClassConverter.getNames(parameters));
    }

    public static ConstructorBuilder createConstructor() {
        return new ConstructorBuilder(null);
    }

    @Override
    protected void loadToWriter(ClassBuilder classBuilder) {
        ClassWriter classWriter = classBuilder.getClassWriter();
        MethodVisitor methodVisitor;

        int modifier = getModifiersValue();
        String descriptor = getMethodDescriptor();
        // null = signature(generic), exceptions
        methodVisitor = classWriter.visitMethod(modifier, "<init>", descriptor, null, null);
        methodVisitor.visitCode();

        methodVisitor.visitVarInsn(ALOAD, 0);
        // TODO super/this customization
        // TODO push parameters
        methodVisitor.visitMethodInsn(INVOKESPECIAL, classBuilder.getSuperclass(), "<init>", "()V", false);

        for (FieldBuilder fieldBuilder : classBuilder.getFields()) {
            Object defaultValue;
            // TODO static constructor (remove !isStatic condition)
            if (fieldBuilder.isStatic() || (defaultValue = fieldBuilder.getDefaultValue()) == null)
                continue;
            addStatement(Statement.setField(fieldBuilder.getFieldName(), Parameter.constant(defaultValue)));
        }

        setupStatements(classBuilder, methodVisitor);

        methodVisitor.visitInsn(RETURN);

        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }

    @Override
    public CallableMethod asCallable(String className) {
        throw new UnsupportedOperationException("You cannot call constructor apart from instantiation");
    }

    @Override
    public void setModifiers(Modifier... modifiers) {
        for (Modifier modifier : modifiers) {
            if (!legalModifiers.contains(modifier))
                throw new IllegalArgumentException("Illegal constructor modifier: " + modifier.toString());
        }
        super.setModifiers(modifiers);
    }
}
