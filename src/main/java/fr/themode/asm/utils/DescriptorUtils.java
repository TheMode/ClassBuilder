package fr.themode.asm.utils;

import jdk.internal.org.objectweb.asm.Type;

public class DescriptorUtils {

    public static String getDescriptor(Class type, Class... parameters) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (int i = 0; i < parameters.length; i++)
            stringBuffer.append(getDescriptor(parameters[i]));
        stringBuffer.append(')');
        stringBuffer.append(getDescriptor(type));
        return stringBuffer.toString();
    }

    public static String getDescriptor(String type, String... parameters) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (int i = 0; i < parameters.length; i++)
            stringBuffer.append(getDescriptor(parameters[i]));
        stringBuffer.append(')');
        stringBuffer.append(getDescriptor(type));
        return stringBuffer.toString();
    }

    public static String getDescriptor(Class clazz) {
        return Type.getDescriptor(clazz);
    }

    public static String getDescriptor(String internalName) {
        if (internalName.equals("int")) {
            return "I";
        } else if (internalName.equals("void")) {
            return "V";
        } else if (internalName.equals("boolean")) {
            return "Z";
        } else if (internalName.equals("byte")) {
            return "B";
        } else if (internalName.equals("char")) {
            return "C";
        } else if (internalName.equals("short")) {
            return "S";
        } else if (internalName.equals("double")) {
            return "D";
        } else if (internalName.equals("float")) {
            return "F";
        }
        return Type.getObjectType(internalName).getDescriptor().replace(".", "/");
    }

}
