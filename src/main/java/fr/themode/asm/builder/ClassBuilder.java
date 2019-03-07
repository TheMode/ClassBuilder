package fr.themode.asm.builder;

import fr.themode.asm.loader.DynamicClassLoader;
import fr.themode.asm.utils.ClassConverter;
import fr.themode.asm.utils.ClassPrint;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassBuilder extends Reachable {

    private byte[] bytes;

    private ClassWriter classWriter;
    private FieldVisitor fieldVisitor;
    private MethodVisitor methodVisitor;
    private AnnotationVisitor annotationVisitor;

    private int version;
    private String className;
    private Class superClass;
    private Class[] interfaces;

    private List<FieldBuilder> fields;
    private List<ConstructorBuilder> constructors;
    private List<MethodBuilder> methods;

    private ClassBuilder(int version, String className, Class superClass) {
        this.classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        this.version = version;
        this.className = className;
        this.superClass = superClass;

        this.fields = new ArrayList<>();
        this.constructors = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    public static ClassBuilder createClass(int version, String className) {
        return new ClassBuilder(version, className, Object.class);
    }

    public void addField(FieldBuilder field) {
        this.fields.add(field);
    }

    public void addConstructor(ConstructorBuilder constructor) {
        this.constructors.add(constructor);
    }

    public void addMethod(MethodBuilder method) {
        this.methods.add(method);
    }

    public void setSuperClass(Class superClass) {
        this.superClass = superClass;
    }

    public Class load() {
        setup();
        setupFields();
        setupConstructors();
        // TODO constructors
        // TODO field default value
        setupMethods();
        this.classWriter.visitEnd();
        this.bytes = classWriter.toByteArray();

        // Debug
        ClassPrint.printASM(getBytes());

        DynamicClassLoader classLoader = new DynamicClassLoader();
        Class result = classLoader.defineClass(className, bytes);
        return result;
    }

    private void setup() {
        int modifierValue = getModifiersValue();
        classWriter.visit(version, modifierValue, getInternalName(), null, ClassConverter.getName(superClass), getInterfacesInternalName());
        // TODO visitSource ?
    }

    private void setupFields() {
        for (FieldBuilder field : fields) {
            field.loadToWriter(classWriter, fieldVisitor);
        }
    }

    private void setupConstructors() {
        for (ConstructorBuilder constructor : constructors) {
            constructor.loadToWriter(this);
        }
    }

    private void setupMethods() {
        for (MethodBuilder method : methods) {
            method.loadToWriter(this);
        }
    }

    @Override
    public int getModifiersValue() {
        int value = super.getModifiersValue();
        // Add super if not already defined
        return (value & ACC_SUPER) == 0 ? value + ACC_SUPER : value;
    }

    public String getInternalName() {
        return ClassConverter.getName(className);
    }

    public String getSuperclass() {
        return ClassConverter.getName(superClass);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public List<FieldBuilder> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public List<ConstructorBuilder> getConstructors() {
        return Collections.unmodifiableList(constructors);
    }

    public List<MethodBuilder> getMethods() {
        return Collections.unmodifiableList(methods);
    }

    public ClassWriter getClassWriter() {
        return classWriter;
    }

    public FieldVisitor getFieldVisitor() {
        return fieldVisitor;
    }

    public MethodVisitor getMethodVisitor() {
        return methodVisitor;
    }

    protected FieldBuilder findField(String fieldName) {
        for (FieldBuilder field : getFields()) {
            if (field.getFieldName().equals(fieldName))
                return field;
        }
        throw new IllegalArgumentException("Field " + fieldName + " do not exist!");
    }

    protected String findFieldDescriptor(String fieldName) {
        return findField(fieldName).getDescriptor();
    }

    protected boolean isFieldStatic(String fieldName) {
        return findField(fieldName).isStatic();
    }

    private String[] getInterfacesInternalName() {
        if (interfaces == null)
            return null;
        return ClassConverter.getNames(interfaces);
    }

}
