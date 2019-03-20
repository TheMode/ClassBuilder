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

    private void loadElseIf(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor, List<FlowHandler> list, Label label, Label falseLabel, Label gotoLabel) {
        // Else if labels
        for (int i = 0; i < list.size(); i++) {
            FlowHandler handler = list.get(i);
            boolean isLast = i == list.size() - 1;
            visitor.visitLabel(label);
            label = isLast ? falseLabel : new Label();
            handler.loadToWriter(classBuilder, method, visitor, label);
            visitor.visitJumpInsn(Opcodes.GOTO, gotoLabel);
        }
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
            Label label = loadMain(classBuilder, method, visitor);
            Label gotoLabel = new Label();
            visitor.visitJumpInsn(Opcodes.GOTO, gotoLabel);

            loadElseIf(classBuilder, method, visitor, list, label, gotoLabel, gotoLabel);

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

            Label elseLabel = new Label();

            loadElseIf(classBuilder, method, visitor, list, label, elseLabel, gotoLabel);

            // Else label
            Statement.setNextLabel(elseLabel);
            for (Statement statement : elseStatements) {
                statement.append(classBuilder, method, visitor);
            }

            Statement.setNextLabel(gotoLabel);
        }
    }
}
