package fr.themode.asm.builder;

import fr.themode.asm.utils.ClassConverter;
import fr.themode.asm.utils.DescriptorUtils;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.FieldVisitor;

public class FieldBuilder extends Reachable {

    private String type;
    private String fieldName;
    private Parameter defaultValue;

    public FieldBuilder(String type, String fieldName, Parameter defaultValue) {
        this.type = type;
        this.fieldName = fieldName;
        this.defaultValue = defaultValue;
    }

    public static FieldBuilder createField(String type, String fieldName, Parameter defaultValue) {
        return new FieldBuilder(ClassConverter.getName(type), fieldName, defaultValue);
    }

    public static FieldBuilder createField(Class type, String fieldName, Parameter defaultValue) {
        return createField(ClassConverter.getName(type), fieldName, defaultValue);
    }

    public static FieldBuilder createField(String type, String fieldName) {
        return createField(type, fieldName, null);
    }

    public static <T> FieldBuilder createField(Class<T> type, String fieldName) {
        return createField(type, fieldName, null);
    }

    protected void loadToWriter(ClassWriter classWriter, FieldVisitor visitor) {
        int modifier = getModifiersValue();
        String typeDescriptor = getDescriptor();
        visitor = classWriter.visitField(modifier, fieldName, typeDescriptor, null, null);
        visitor.visitEnd();
        // default value handled in ConstructorBuilder class
    }

    public String getType() {
        return type;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Parameter getDefaultValue() {
        return defaultValue;
    }

    protected String getDescriptor() {
        return DescriptorUtils.getDescriptor(type);
    }
}
