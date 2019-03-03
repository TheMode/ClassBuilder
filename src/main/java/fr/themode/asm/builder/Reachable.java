package fr.themode.asm.builder;

import fr.themode.asm.enums.Modifier;

public class Reachable {

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
}
