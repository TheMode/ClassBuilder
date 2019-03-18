package fr.themode.asm.builder.flow.module;

import fr.themode.asm.builder.BooleanExpression;
import fr.themode.asm.builder.ClassBuilder;
import fr.themode.asm.builder.MethodBuilder;
import fr.themode.asm.builder.Statement;
import fr.themode.asm.builder.flow.FlowControl;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

public class IfControl extends FlowControl {

    private FlowHandler mainHandler;

    public IfControl(BooleanExpression[] conditions, Statement... statements) {
        this.mainHandler = createHandler(conditions, statements);
    }

    public ElseIfControl else_if(BooleanExpression[] conditions, Statement... statements) {
        return new ElseIfControl(mainHandler, createHandler(conditions, statements));
    }

    public ElseIfControl else_if(BooleanExpression condition, Statement... statements) {
        return else_if(new BooleanExpression[]{condition}, statements);
    }

    public ElseControl else_(Statement... statements) {
        return new ElseControl(mainHandler, new ArrayList<>(), statements);
    }

    @Override
    public void loadToWriter(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor) {
        Label label = loadMain(classBuilder, method, visitor);
        // Next instructions
        Statement.setNextLabel(label);
    }

    private Label loadMain(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor) {
        // Represents the next label to visit
        Label label = new Label();
        // Load the main condition
        mainHandler.loadToWriter(classBuilder, method, visitor, label);
        return label;
    }

    private Label loadElseIf(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor, List<FlowHandler> list, Label label, Label gotoLabel) {
        // Else if labels
        //Statement.setNextLabel(label);
        for (FlowHandler handler : list) {
            visitor.visitLabel(label);
            label = new Label();
            handler.loadToWriter(classBuilder, method, visitor, label);
            visitor.visitJumpInsn(Opcodes.GOTO, gotoLabel);
        }
        return label;
    }

    private FlowHandler createHandler(BooleanExpression[] conditions, Statement[] statements) {
        FlowHandler handler = new FlowHandler();
        handler.conditions = conditions;
        handler.statements = statements;
        return handler;
    }

    public class ElseIfControl extends FlowControl {
        private FlowHandler mainHandler;
        private List<FlowHandler> list;

        private ElseIfControl(FlowHandler main, FlowHandler current) {
            this.mainHandler = main;
            this.list = new ArrayList<>();
            this.list.add(current);
        }

        public ElseIfControl else_if(BooleanExpression[] conditions, Statement... statements) {
            this.list.add(createHandler(conditions, statements));
            return this;
        }

        public ElseIfControl else_if(BooleanExpression condition, Statement... statements) {
            return else_if(new BooleanExpression[]{condition}, statements);
        }

        public ElseControl else_(Statement... statements) {
            return new ElseControl(mainHandler, list, statements);
        }

        @Override
        public void loadToWriter(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor) {
            // TODO loadMain jump to elseif
            Label label = loadMain(classBuilder, method, visitor);
            Label gotoLabel = new Label();
            visitor.visitJumpInsn(Opcodes.GOTO, gotoLabel);

            label = loadElseIf(classBuilder, method, visitor, list, label, gotoLabel);

            // Next instructions
            Statement.setNextLabel(gotoLabel);
        }
    }

    public class ElseControl extends FlowControl {

        private FlowHandler mainHandler;
        private List<FlowHandler> list;
        private Statement[] elseStatements;

        private ElseControl(FlowHandler main, List<FlowHandler> list, Statement[] elseStatements) {
            this.mainHandler = main;
            this.list = list;
            this.elseStatements = elseStatements;
        }

        @Override
        public void loadToWriter(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor) {
            Label label = loadMain(classBuilder, method, visitor);
            Label gotoLabel = new Label();
            visitor.visitJumpInsn(Opcodes.GOTO, gotoLabel);

            label = loadElseIf(classBuilder, method, visitor, list, label, gotoLabel);

            // Else label
            Statement.setNextLabel(label);
            for (Statement statement : elseStatements) {
                statement.append(classBuilder, method, visitor);
            }

            Statement.setNextLabel(gotoLabel);
        }
    }

    private class FlowHandler {
        private BooleanExpression[] conditions;
        private Statement[] statements;

        private void loadToWriter(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor, Label jumpLabel) {
            for (BooleanExpression condition : conditions) {
                condition.loadToWriter(classBuilder, method, visitor, jumpLabel);
            }

            for (Statement statement : statements) {
                statement.append(classBuilder, method, visitor);
            }
        }
    }
}
