package fr.themode.asm.builder;

import fr.themode.asm.utils.DescriptorUtils;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class BooleanExpression implements Opcodes {

    private static final int EQUAL = 1;
    private static final int NOT_EQUAL = 2;

    private Parameter param1;
    private Parameter param2;
    private int type;

    // Cache
    private String mainType;
    private boolean checkReference;

    private BooleanExpression(Parameter param1, Parameter param2, int type) {
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

    public static BooleanExpression not_equal(Parameter param1, Parameter param2) {
        return new BooleanExpression(param1, param2, NOT_EQUAL);
    }

    public void loadToWriter(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor, Label jumpLabel) {
        String type1 = param1.getTypeDescriptor(classBuilder, method);
        String type2 = param2.getTypeDescriptor(classBuilder, method);

        int[] cast = getCast(type1, type2);
        System.out.println("testtype: " + type1 + " : " + type2);

        param1.push(classBuilder, method, visitor);
        visitCast(visitor, cast[0]);
        param2.push(classBuilder, method, visitor);
        visitCast(visitor, cast[1]);
        compare(visitor);
        visitor.visitJumpInsn(getOpcode(), jumpLabel);
    }

    private int getOpcode() {
        if (type == EQUAL) {
            // Opcode set to "not equal"
            if (checkReference) {
                return IF_ACMPNE;
            } else {
                return IF_ICMPNE;
            }
        } else if (type == NOT_EQUAL) {
            if (checkReference) {
                return IF_ACMPEQ;
            } else {
                return IF_ICMPEQ;
            }
        } else if (checkReference) {
            throw new IllegalArgumentException("Reference can be used as BooleanExpression only when applied with equal/not_equal condition");
        }
        // TODO others
        return 0;
    }

    private void compare(MethodVisitor visitor) {
        switch (mainType) {
            case DescriptorUtils.DOUBLE:
                visitor.visitInsn(DCMPG);
                break;
            case DescriptorUtils.FLOAT:
                visitor.visitInsn(FCMPG);
            case DescriptorUtils.LONG:
                visitor.visitInsn(LCMP);
                break;
            default:
                break;
        }
    }

    private void visitCast(MethodVisitor visitor, int castOpcode) {
        if (castOpcode == 0)
            return;
        visitor.visitInsn(castOpcode);
    }

    private int[] getCast(String type1, String type2) {
        int[] cast = new int[2];

        int priority1 = getPriority(type1);
        int priority2 = getPriority(type2);

        if (priority1 + priority2 == 0) {
            checkReference = true;
        }

        if (priority1 + priority2 > 1) { // Cast only if two numbers are compared
            if (priority1 == priority2) {
                mainType = type1;
            } else {
                int min = Math.min(priority1, priority2);
                String minType = priority1 == min ? type1 : type2;
                String maxType = minType == type1 ? type2 : type1;

                mainType = maxType;
                cast[priority1 == min ? 0 : 1] = getCastOpcode(minType, maxType);
            }
        }

        return cast;
    }

    private int getCastOpcode(String min, String max) {
        switch (min) {
            case DescriptorUtils.FLOAT:
                if (max == DescriptorUtils.DOUBLE) {
                    return F2D;
                }
                break;
            case DescriptorUtils.LONG:
                if (max == DescriptorUtils.DOUBLE) {
                    return L2D;
                } else if (max == DescriptorUtils.FLOAT) {
                    return L2F;
                }
                break;
            case DescriptorUtils.INTEGER:
                if (max == DescriptorUtils.DOUBLE) {
                    return I2D;
                } else if (max == DescriptorUtils.FLOAT) {
                    return I2F;
                } else if (max == DescriptorUtils.LONG) {
                    return I2F;
                }
                break;
        }
        return 0;
    }

    private int getPriority(String type) {
        switch (type) {
            case DescriptorUtils.DOUBLE:
                return 4;
            case DescriptorUtils.FLOAT:
                return 3;
            case DescriptorUtils.LONG:
                return 2;
            case DescriptorUtils.INTEGER:
                return 1;
            default:
                return 0;
        }
    }

}
