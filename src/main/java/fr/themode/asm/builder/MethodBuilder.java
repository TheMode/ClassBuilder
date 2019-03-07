package fr.themode.asm.builder;

import fr.themode.asm.method.CallableMethod;
import fr.themode.asm.method.MethodFinder;
import fr.themode.asm.utils.ClassConverter;
import fr.themode.asm.utils.DescriptorUtils;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodBuilder extends Reachable {

    private String methodName;
    private String type;
    private String[] parameters;

    private String methodDescriptor;

    private Map<String, Integer> varIndexes;
    private Map<String, Class> varTypes;
    private int storeIndex;

    private List<Statement> statements;

    protected MethodBuilder(String methodName, String type, String... parameters) {
        this.methodName = methodName;
        this.type = ClassConverter.getName(type);
        this.parameters = ClassConverter.getNames(parameters);

        this.varIndexes = new HashMap<>();
        this.varTypes = new HashMap<>();
        this.statements = new ArrayList<>();

        setupDescriptor();
        setupIndexes();
    }

    public static MethodBuilder createMethod(String methodName, String type, String... parameters) {
        return new MethodBuilder(methodName, type, parameters);
    }

    public static MethodBuilder createMethod(String methodName, Class type, Class... parameters) {
        return new MethodBuilder(methodName, type.getName(), ClassConverter.getNames(parameters));
    }

    protected void loadToWriter(ClassBuilder classBuilder) {
        ClassWriter classWriter = classBuilder.getClassWriter();
        MethodVisitor methodVisitor;

        int modifier = getModifiersValue();
        String descriptor = getMethodDescriptor();
        // null = signature(generic), exceptions
        methodVisitor = classWriter.visitMethod(modifier, methodName, descriptor, null, null);
        methodVisitor.visitCode();

        setupStatements(classBuilder, methodVisitor);

        // TODO return implementation
        methodVisitor.visitInsn(RETURN);

        methodVisitor.visitMaxs(0, 0);
        methodVisitor.visitEnd();
    }

    protected void setupStatements(ClassBuilder classBuilder, MethodVisitor visitor) {
        // TODO flow control
        for (Statement statement : statements) {
            statement.append(classBuilder, this, visitor);
        }
    }

    public CallableMethod asCallable(String className) {
        if (isStatic()) {
            return MethodFinder.getStaticMethod(className, methodName, type, parameters).asCallable();
        } else {
            return MethodFinder.getLocalMethod(className, methodName, type, parameters).asCallable();
        }
    }

    public CallableMethod asCallable(ClassBuilder classBuilder) {
        return asCallable(classBuilder.getInternalName());
    }

    public void addStatement(Statement statement) {
        this.statements.add(statement);
    }

    public String getMethodName() {
        return methodName;
    }

    public String getType() {
        return type;
    }

    public String[] getParameters() {
        return parameters;
    }

    public String getMethodDescriptor() {
        return methodDescriptor;
    }

    private void setupDescriptor() {
        this.methodDescriptor = DescriptorUtils.getDescriptor(type, parameters);
    }

    private void setupIndexes() {
        // 0 = this
        // 1,2... = parameters
        storeIndex = parameters == null ? 0 : parameters.length;
    }

    protected int generateStoreIndex() {
        return ++storeIndex;
    }

    protected int getVarStoreIndex(String varName) {
        return varIndexes.get(varName);
    }

    protected void setVarStoreIndex(String varName, int index) {
        this.varIndexes.put(varName, index);
    }

    protected Class getVarType(String varName) {
        return varTypes.get(varName);
    }

    protected void setVarType(String varName, Class type) {
        this.varTypes.put(varName, type);
    }

}
