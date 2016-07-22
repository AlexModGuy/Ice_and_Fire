package com.github.alexthe666.iceandfire.access;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class IceAndFireTransformer implements IClassTransformer {

	@Override
	public byte[] transform(String name, String transformedName, byte[] classBytes) {
		{
			boolean obf;
			ClassNode classNode = new ClassNode();
			if ((obf = "zs".equals(name)) || "net.minecraft.entity.player.EntityPlayer".equals(name)) {
				ClassReader classReader = new ClassReader(classBytes);
				classReader.accept(classNode, 0);
				String updateRiddenName = obf ? "aw" : "updateRidden";
				String updateRiddenDesc = "()V";
				for (int i = 0; i < classNode.methods.size(); i++) {
					MethodNode method = classNode.methods.get(i);
					if (updateRiddenName.equals(method.name) && updateRiddenDesc.equals(method.desc)) {
						InsnList insnList = method.instructions;
						for (int j = 0; j < insnList.size(); j++) {{
								AbstractInsnNode insnNote = method.instructions.get(j);
								if (insnNote.getOpcode() == Opcodes.INVOKEVIRTUAL) {
									MethodInsnNode method_0 = (MethodInsnNode) insnNote;
									if (method_0.name.equals(obf ? "p" : "dismountRidingEntity")) {
										MethodInsnNode method_1 = new MethodInsnNode(Opcodes.INVOKESTATIC, "com/github/alexthe666/iceandfire/access/IceAndFireHooks", "dismount", obf ? "(zs;)V" : "(Lnet/minecraft/entity/player/EntityPlayer;)V", false);
										insnList.insertBefore(method_0, method_1);
										insnList.remove(method_0);
										break;
									}
								}

							}
						}
						break;

					}
				}
				ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
				classNode.accept(classWriter);
				return classWriter.toByteArray();
			}
		}
		return classBytes;
	}

}
