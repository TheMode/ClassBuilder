package fr.themode.asm.enums;

import jdk.internal.org.objectweb.asm.Opcodes;

public enum Modifier implements Opcodes {

    PUBLIC(ACC_PUBLIC), PRIVATE(ACC_PRIVATE), PROTECTED(ACC_PROTECTED), STATIC(ACC_STATIC), FINAL(ACC_FINAL);

    private int value;

    Modifier(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
