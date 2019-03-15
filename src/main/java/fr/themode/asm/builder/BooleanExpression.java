package fr.themode.asm.builder;

import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class BooleanExpression implements Opcodes {

    private static final int EQUAL = 1;

    private Parameter param1;
    private Parameter param2;
    private int type;

    private int opcode;

    private BooleanExpression(Parameter param1, Parameter param2, int type) {
        this.param1 = param1;
        this.param2 = param2;
        this.type = type;

        setupOpcode();
    }


    public static BooleanExpression[] multi(BooleanExpression... expressions) {
        return expressions;
    }

    public static BooleanExpression equal(Parameter param1, Parameter param2) {
        return new BooleanExpression(param1, param2, EQUAL);
    }

    protected void loadToWriter(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor, Label jumpLabel) {
        param1.push(classBuilder, method, visitor);
        // TODO cast if necessary
        param2.push(classBuilder, method, visitor);
        visitor.visitJumpInsn(opcode, jumpLabel);
    }

    private void setupOpcode() {
        // TODO switch type
        this.opcode = 0;
    }

}
