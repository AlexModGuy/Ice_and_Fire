package com.github.alexthe666.iceandfire.access;

import net.ilexiconn.llibrary.server.asm.RuntimePatcher;
import net.minecraft.entity.player.EntityPlayer;
import org.objectweb.asm.tree.*;

public class IceAndFireRuntimePatcher extends RuntimePatcher {

    @Override
    public void onInit() {
            this.patchClass(EntityPlayer.class)
                    .patchMethod("updateRidden", void.class)
                    .apply(RuntimePatcher.Patch.REPLACE_NODE, data -> {
                        if (data.node.getOpcode() == INVOKEVIRTUAL) {
                            MethodInsnNode methodNode = (MethodInsnNode) data.node;
                            if (methodNode.name.equals(IceAndFireForgeLoading.development ? "dismountRidingEntity" : "p") && methodNode.desc.equals("()V")) {
                                return true;
                            }
                        }
                        return false;
                    }, method -> method.method(INVOKESTATIC, IceAndFireHooks.class, "dismount", EntityPlayer.class, void.class));
            }
        }

