package fr.themode.asm.builder;

import fr.themode.asm.utils.ClassConverter;
import fr.themode.asm.utils.DescriptorUtils;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodBuilder<T> extends Reachable implements Opcodes {

    private String methodName;
    private String type;
    private String[] parameters;

    private String methodDescriptor;

    private Map<String, Integer> varIndexes;
    private Map<String, Class> varTypes;
    private int storeIndex;

    private List<Statement> statements;

    private MethodBuilder(String methodName, String type, String... parameters) {
        this.methodName = methodName;
        this.type = type;
        this.parameters = parameters;

        this.varIndexes = new HashMap<>();
        this.varTypes = new HashMap<>();
        this.statements = new ArrayList<>();

        setupDescriptor();
        setupIndexes();
    }

    public static MethodBuilder createMethod(String methodName, String type, String... parameters) {
        return new MethodBuilder(methodName, type, parameters);
    }

    public static <T> MethodBuilder createMethod(String methodName, Class<T> type, Class... parameters) {
        return new MethodBuilder<T>(methodName, ClassConverter.getName(type), ClassConverter.getNames(parameters));
    }

    public void loadToWriter(ClassWriter classWriter, MethodVisitor visitor) {
        int modifier = getModifiersValue();
        String descriptor = getMethodDescriptor();
        // null = signature(generic), exceptions
        visitor = classWriter.visitMethod(modifier, methodName, descriptor, null, null);
        visitor.visitCode();

        setupStatements(visitor);

        // TODO return implementation
        visitor.visitInsn(RETURN);

        visitor.visitMaxs(0, 0);
        visitor.visitEnd();
    }

    private void setupStatements(MethodVisitor visitor) {
        // TODO flow control
        for (Statement statement : statements) {
            statement.append(this, visitor);
        }
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

    public boolean isStatic() {
        return getModifiersValue() == ACC_STATIC;
    }

    private void setupDescriptor() {
        this.methodDescriptor = DescriptorUtils.getDescriptor(type, parameters);
    }

    private void setupIndexes() {
        // 0 = this
        // 1,2... = parameters
        storeIndex = parameters.length;
    }

    // Statement utils
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
