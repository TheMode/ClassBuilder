package fr.themode.asm.builder;

import jdk.internal.org.objectweb.asm.Label;

public class BooleanExpression {

    private static final int EQUAL = 1;

    private Parameter param1;
    private Parameter param2;
    private int type;

    private BooleanExpression(Parameter param1, Parameter param2, int type) {
        if (param1.getType() == Parameter.ParameterType.CONSTANT && param2.getType() == Parameter.ParameterType.CONSTANT)
            throw new IllegalArgumentException("You shouldn't compare two constant value (do it yourself)");
        this.param1 = param1;
        this.param2 = param2;
        this.type = type;
    }


    public static BooleanExpression[] multi(BooleanExpression... expressions) {
        return expressions;
    }

    public static BooleanExpression equal(Parameter param1, Parameter param2) {
        return new BooleanExpression(param1, param2, EQUAL);
    }

    protected void loadToWriter(ClassBuilder classBuilder, Label jumpLabel) {
        // TODO load both parameters
        // TODO cast if necessary
        // TODO jump wth jumpLabel as location
    }

}
