package fr.themode.asm.builder;

import fr.themode.asm.enums.Modifier;
import jdk.internal.org.objectweb.asm.Opcodes;

public class Reachable implements Opcodes {

    private Modifier[] modifiers;

    public void setModifiers(Modifier... modifiers) {
        this.modifiers = modifiers;
    }

    public Modifier[] getModifiers() {
        return modifiers;
    }

    public int getModifiersValue() {
        if (modifiers == null)
            return 0;
        int value = 0;
        for (Modifier modifier : modifiers)
            value += modifier.getValue();
        return value;
    }

    public boolean hasModifier(Modifier modifier) {
        return (getModifiersValue() & modifier.getValue()) != 0;
    }

    public boolean isStatic() {
        return hasModifier(Modifier.STATIC);
    }

}
