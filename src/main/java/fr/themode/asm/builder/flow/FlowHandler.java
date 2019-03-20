package fr.themode.asm.builder.flow;

import fr.themode.asm.builder.BooleanExpression;
import fr.themode.asm.builder.ClassBuilder;
import fr.themode.asm.builder.MethodBuilder;
import fr.themode.asm.builder.Statement;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;

public class FlowHandler {

    private BooleanExpression[] conditions;
    private Statement[] statements;

    public FlowHandler(BooleanExpression[] conditions, Statement[] statements) {
        this.conditions = conditions;
        this.statements = statements;
    }

    public void loadToWriter(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor, Label jumpLabel) {
        for (BooleanExpression condition : conditions) {
            condition.loadToWriter(classBuilder, method, visitor, jumpLabel);
        }

        for (Statement statement : statements) {
            statement.append(classBuilder, method, visitor);
        }
    }

}
