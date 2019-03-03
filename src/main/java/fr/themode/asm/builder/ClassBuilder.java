package fr.themode.asm.builder;

import fr.themode.asm.loader.DynamicClassLoader;
import fr.themode.asm.utils.ClassConverter;
import fr.themode.asm.utils.ClassPrint;
import jdk.internal.org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.List;

public class ClassBuilder extends Reachable implements Opcodes {

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

    private List<MethodBuilder> methods;

    private ClassBuilder(int version, String className, Class superClass) {
        this.classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        this.version = version;
        this.className = className;
        this.superClass = superClass;

        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    public static ClassBuilder createClass(int version, String className) {
        return new ClassBuilder(version, className, Object.class);
    }

    public void addField(FieldBuilder field) {
        this.fields.add(field);
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

    @Override
    public int getModifiersValue() {
        int value = super.getModifiersValue();
        // Add super if not already defined
        return (value & ACC_SUPER) == 0 ? value + ACC_SUPER : value;
    }

    public String getInternalName() {
        return ClassConverter.getName(className);
    }

    public byte[] getBytes() {
        return bytes;
    }

    private void setupFields() {
        for (FieldBuilder field : fields) {
            field.loadToWriter(classWriter, fieldVisitor);
        }
    }

    private void setupMethods() {
        for (MethodBuilder method : methods) {
            method.loadToWriter(classWriter, methodVisitor);
        }
    }

    private String[] getInterfacesInternalName() {
        if (interfaces == null)
            return null;
        return ClassConverter.getNames(interfaces);
    }

}
