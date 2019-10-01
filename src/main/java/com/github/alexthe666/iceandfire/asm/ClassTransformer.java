package com.github.alexthe666.iceandfire.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.lwjgl.Sys;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;

public class ClassTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("net.minecraft.network.NetHandlerPlayServer")) {
            System.out.println("ICE AD FIRE found " + transformedName);
            return patchNetHandlerPlayServer(name, basicClass, !name.equals(transformedName));
        } else {
            return basicClass;
        }
    }

    private byte[] patchNetHandlerPlayServer(String name, byte[] basicClass, boolean obf) {
        String processVehicleMove;
        String entity;
        if (obf) {
            processVehicleMove = "func_184338_a";
            entity = "Lnet/minecraft/entity/Entity;";
        } else {
            processVehicleMove = "processVehicleMove";
            entity = "Lnet/minecraft/entity/Entity;";
        }
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        List<MethodNode> methods = classNode.methods;

        for (MethodNode m : methods) {
            if (m.name.equals(processVehicleMove)) {
                System.out.println("Found method " + name + "." + m.name + "" + m.desc);
                InsnList code = m.instructions;
                //MethodInsnNode method = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/github/alexthe666/iceandfire/asm/IceAndFireCoreUtils", "getFastestEntityMotionSpeed", "()D", false);
                boolean notFirst = false;
                for (int i = 0; i < code.size(); i++) {
                    if (code.get(i) instanceof LdcInsnNode) {
                        LdcInsnNode lin = (LdcInsnNode) code.get(i);
                        if (lin.cst instanceof Double && ((Double) lin.cst).doubleValue() == 0.0625D) {
                            if (notFirst) {
                                System.out.println(i);
                                //code.set(lin, method);
                            } else {
                                notFirst = true;
                            }
                        }
                    }
                }
            }
        }
        ClassWriter writer = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        classNode.accept(writer);

        return writer.toByteArray();
    }
}