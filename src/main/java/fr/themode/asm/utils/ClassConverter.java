package fr.themode.asm.utils;

public class ClassConverter {

    public static String getName(String clazz) {
        return clazz.replace(".", "/");
    }

    public static String getName(Class clazz) {
        return getName(clazz.getName());
    }

    public static String[] getNames(Class... classes) {
        if (classes == null)
            return null;
        String[] result = new String[classes.length];
        for (int i = 0; i < classes.length; i++) {
            result[i] = ClassConverter.getName(classes[i]);
        }
        return result;
    }

    public static String[] getNames(String... classes) {
        if (classes == null)
            return null;
        for (int i = 0; i < classes.length; i++) {
            classes[i] = ClassConverter.getName(classes[i]);
        }
        return classes;
    }

}
