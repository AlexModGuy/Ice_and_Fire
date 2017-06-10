package com.github.alexthe666.iceandfire.access;

import net.minecraft.launchwrapper.IClassTransformer;

public class IceAndFireTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] classBytes) {
        return classBytes;
    }
}
