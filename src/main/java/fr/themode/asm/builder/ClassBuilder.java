package fr.themode.asm.builder;

import fr.themode.asm.enums.Modifier;
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
import java.util.regex.Pattern;

public class ClassBuilder extends Reachable {

    private byte[] bytes;

    private ClassWriter classWriter;
    private FieldVisitor fieldVisitor;
    private MethodVisitor methodVisitor;
    private AnnotationVisitor annotationVisitor;

    private int version;
    private String className;
    private String superClass;
    private Class[] interfaces;

    private List<FieldBuilder> fields;
    private List<ConstructorBuilder> constructors;
    private List<MethodBuilder> methods;

    private Statement[] staticInit;

    private ClassBuilder(int version, String className, String superClass) {
        this.classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        this.version = version;
        this.className = className;
        this.superClass = superClass;

        this.fields = new ArrayList<>();
        this.constructors = new ArrayList<>();
        this.methods = new ArrayList<>();
    }

    public static ClassBuilder createClass(int version, String className) {
        return new ClassBuilder(version, className, ClassConverter.getName(Object.class));
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

    public void setSuperClass(String superClass) {
        this.superClass = ClassConverter.getName(superClass);
    }

    public void setSuperClass(Class superClass) {
        setSuperClass(ClassConverter.getName(superClass));
    }

    public void setStaticInitialization(Statement... statements) {
        this.staticInit = statements;
    }


    public Class load() {
        setup();
        setupFields();
        setupConstructors();
        setupStaticConstructor();
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
        classWriter.visit(version, modifierValue, getInternalName(), null, superClass, getInterfacesInternalName());

        String[] packageSplit = className.split(Pattern.quote("."));
        String source = packageSplit[packageSplit.length - 1];
        classWriter.visitSource(source + ".java", null);
    }

    private void setupFields() {
        for (FieldBuilder field : fields) {
            field.loadToWriter(classWriter, fieldVisitor);
        }
    }

    private void setupConstructors() {
        if (constructors.isEmpty()) {
            // Add default constructor if empty
            ConstructorBuilder constructor = ConstructorBuilder.createConstructor();
            constructor.setModifiers(Modifier.PUBLIC);

            addConstructor(constructor);
        }

        for (ConstructorBuilder constructor : constructors) {
            constructor.loadToWriter(this);
        }
    }

    private void setupStaticConstructor() {

        MethodBuilder method = MethodBuilder.createMethod("<clinit>", void.class);
        method.setModifiers(Modifier.STATIC);

        for (FieldBuilder field : getFields()) {
            if (field.isStatic()) {
                Parameter defaultValue = field.getDefaultValue();
                if (defaultValue != null)
                    method.addStatement(Statement.setField(field.getFieldName(), defaultValue));
            }
        }

        if (staticInit != null) {
            method.addStatements(staticInit);
        }

        addMethod(method);
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

    protected FieldBuilder findField(String fieldName) {
        if (fieldName == null || fieldName.isEmpty())
            throw new IllegalArgumentException("Field name cannot be null or empty");
        for (FieldBuilder field : getFields()) {
            if (field.getFieldName().equals(fieldName))
                return field;
        }
        throw new IllegalArgumentException("Field " + fieldName + " do not exist!");
    }

    protected String getFieldDescriptor(String fieldName) {
        return findField(fieldName).getDescriptor();
    }

    protected boolean isFieldStatic(String fieldName) {
        return findField(fieldName).isStatic();
    }

    private String[] getInterfacesInternalName() {
        return ClassConverter.getNames(interfaces);
    }

}
