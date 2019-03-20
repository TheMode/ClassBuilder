package fr.themode.asm.builder.flow.module;

import fr.themode.asm.builder.BooleanExpression;
import fr.themode.asm.builder.ClassBuilder;
import fr.themode.asm.builder.MethodBuilder;
import fr.themode.asm.builder.Statement;
import fr.themode.asm.builder.flow.FlowControl;
import fr.themode.asm.builder.flow.FlowHandler;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class WhileControl extends FlowControl {

    private FlowHandler flowHandler;

    public WhileControl(BooleanExpression[] conditions, Statement... statements) {
        this.flowHandler = createHandler(conditions, statements);
    }

    @Override
    public void loadToWriter(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor) {
        Label startLabel = new Label();
        Label jumpLabel = new Label();

        visitor.visitLabel(startLabel);
        flowHandler.loadToWriter(classBuilder, method, visitor, jumpLabel);

        visitor.visitJumpInsn(Opcodes.GOTO, startLabel);

        Statement.setNextLabel(jumpLabel);

    }
}
