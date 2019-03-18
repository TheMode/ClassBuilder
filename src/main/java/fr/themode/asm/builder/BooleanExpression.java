package fr.themode.asm.builder;

import fr.themode.asm.utils.DescriptorUtils;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

public class BooleanExpression implements Opcodes {

    private static final int NULL = 0;
    private static final int NOT_NULL = 1;

    private static final int EQUAL = 2;
    private static final int NOT_EQUAL = 3;

    private static final int LESS = 4;
    private static final int LESS_EQUAL = 5;

    private static final int GREATER = 6;
    private static final int GREATER_EQUAL = 7;

    private static final int IS_TRUE = 8;
    private static final int IS_FALSE = 9;

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

    public static BooleanExpression is_null(Parameter param) {
        return new BooleanExpression(param, null, NULL);
    }

    public static BooleanExpression not_null(Parameter param) {
        return new BooleanExpression(param, null, NOT_NULL);
    }

    public static BooleanExpression equal(Parameter param1, Parameter param2) {
        return new BooleanExpression(param1, param2, EQUAL);
    }

    public static BooleanExpression not_equal(Parameter param1, Parameter param2) {
        return new BooleanExpression(param1, param2, NOT_EQUAL);
    }

    public static BooleanExpression less(Parameter param1, Parameter param2) {
        return new BooleanExpression(param1, param2, LESS);
    }

    public static BooleanExpression less_equal(Parameter param1, Parameter param2) {
        return new BooleanExpression(param1, param2, LESS_EQUAL);
    }

    public static BooleanExpression greater(Parameter param1, Parameter param2) {
        return new BooleanExpression(param1, param2, GREATER);
    }

    public static BooleanExpression greater_equal(Parameter param1, Parameter param2) {
        return new BooleanExpression(param1, param2, GREATER_EQUAL);
    }

    public static BooleanExpression is_true(Parameter param1, Parameter param2) {
        return new BooleanExpression(param1, param2, IS_TRUE);
    }

    public static BooleanExpression is_false(Parameter param1, Parameter param2) {
        return new BooleanExpression(param1, param2, IS_FALSE);
    }

    public void loadToWriter(ClassBuilder classBuilder, MethodBuilder method, MethodVisitor visitor, Label jumpLabel) {
        String type1 = param1.getTypeDescriptor(classBuilder, method);

        // param2 is null when condition is NULL/NOT_NULL/IS_TRUE/IS_FALSE
        if (param2 != null) {
            String type2 = param2.getTypeDescriptor(classBuilder, method);

            int[] cast = getCast(type1, type2);
            System.out.println("testtype: " + type1 + " : " + type2);

            param1.push(classBuilder, method, visitor);
            visitCast(visitor, cast[0]);
            param2.push(classBuilder, method, visitor);
            visitCast(visitor, cast[1]);
            compare(visitor);
        } else {
            param1.push(classBuilder, method, visitor);
        }

        visitor.visitJumpInsn(getOpcode(), jumpLabel);
    }

    private int getOpcode() {
        if (type == NULL) {
            return IFNONNULL;
        } else if (type == NOT_NULL) {
            return IFNULL;
        } else if (type == EQUAL) {
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
            throw new IllegalArgumentException("Reference can be used as BooleanExpression only when applied with is_null/equal/not_equal condition");
        }

        if (type == IS_TRUE) {
            return IFEQ;
        } else if (type == IS_FALSE) {
            return IFNE;
        }

        if (mainType.equals(DescriptorUtils.INTEGER)) {
            switch (type) {
                case LESS:
                    return IF_ICMPGE;
                case LESS_EQUAL:
                    return IF_ICMPGT;
                case GREATER:
                    return IF_ICMPLE;
                case GREATER_EQUAL:
                    return IF_ICMPLT;
            }
        } else {
            switch (type) {
                case LESS:
                    return IFGE;
                case LESS_EQUAL:
                    return IFGT;
                case GREATER:
                    return IFLE;
                case GREATER_EQUAL:
                    return IFLT;
            }
        }

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
        } else {
            mainType = type1;
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
