package fr.themode.asm.builder.flow;

import fr.themode.asm.builder.BooleanExpression;
import fr.themode.asm.builder.ClassBuilder;
import fr.themode.asm.builder.MethodBuilder;
import fr.themode.asm.builder.Statement;
import fr.themode.asm.builder.flow.module.IfControl;
import fr.themode.asm.builder.flow.module.WhileControl;
import jdk.internal.org.objectweb.asm.MethodVisitor;

public class FlowControl {

    public static IfControl if_(BooleanExpression[] conditions, Statement... statements) {
        return new IfControl(conditions, statements);
    }

    public static IfControl if_(BooleanExpression condition, Statement... statements) {
        return if_(new BooleanExpression[]{condition}, statements);
    }

    public static WhileControl while_(BooleanExpression[] conditions, Statement... statements) {
        return new WhileControl(conditions, statements);
    }

    public static WhileControl while_(BooleanExpression condition, Statement... statements) {
        return while_(new BooleanExpression[]{condition}, statements);
    }

    public void loadToWriter(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor) {

    }

    protected FlowHandler createHandler(BooleanExpression[] conditions, Statement[] statements) {
        return new FlowHandler(conditions, statements);
    }

}
