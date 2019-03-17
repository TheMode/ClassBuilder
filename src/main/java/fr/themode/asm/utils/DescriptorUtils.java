package fr.themode.asm.utils;

import jdk.internal.org.objectweb.asm.Type;

public class DescriptorUtils {

    public static final String INTEGER = "I";
    public static final String LONG = "J";
    public static final String CHAR = "C";
    public static final String SHORT = "S";
    public static final String FLOAT = "F";
    public static final String DOUBLE = "D";
    public static final String BYTE = "B";
    public static final String BOOLEAN = "Z";

    public static String getDescriptor(Class type, Class... parameters) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++)
                stringBuffer.append(getDescriptor(parameters[i]));
        }
        stringBuffer.append(')');
        stringBuffer.append(getDescriptor(type));
        return stringBuffer.toString();
    }

    public static String getDescriptor(String type, String... parameters) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++)
                stringBuffer.append(getDescriptor(parameters[i]));
        }
        stringBuffer.append(')');
        stringBuffer.append(getDescriptor(type));
        return stringBuffer.toString();
    }

    public static String getDescriptor(Class clazz) {
        return Type.getDescriptor(clazz);
    }

    public static String getDescriptor(String internalName) {
        if (internalName.equals("int")) {
            return INTEGER;
        } else if (internalName.equals("long")) {
            return LONG;
        } else if (internalName.equals("void")) {
            return "V";
        } else if (internalName.equals("boolean")) {
            return BOOLEAN;
        } else if (internalName.equals("byte")) {
            return BYTE;
        } else if (internalName.equals("char")) {
            return CHAR;
        } else if (internalName.equals("short")) {
            return SHORT;
        } else if (internalName.equals("double")) {
            return DOUBLE;
        } else if (internalName.equals("float")) {
            return FLOAT;
        }

        return Type.getObjectType(internalName).getDescriptor().replace(".", "/");
    }

}
