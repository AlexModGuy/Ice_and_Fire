package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageMultipartInteract {

    public int creatureID;
    public float dmg;

    public MessageMultipartInteract(int creatureID, float dmg) {
        this.creatureID = creatureID;
        this.dmg = dmg;
    }

    public MessageMultipartInteract() {
    }

    public static MessageMultipartInteract read(FriendlyByteBuf buf) {
        return new MessageMultipartInteract(buf.readInt(), buf.readFloat());
    }

    public static void write(MessageMultipartInteract message, FriendlyByteBuf buf) {
        buf.writeInt(message.creatureID);
        buf.writeFloat(message.dmg);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageMultipartInteract message, final Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();

            context.enqueueWork(() -> {
                Player player = context.getSender();

                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }

                if (player != null) {
                    Entity entity = player.level().getEntity(message.creatureID);

                    if (entity instanceof LivingEntity livingEntity) {
                        double dist = player.distanceTo(livingEntity);

                        if (dist < 100) {
                            if (message.dmg > 0F) {
                                livingEntity.hurt(player.level().damageSources().mobAttack(player), message.dmg);
                            } else {
                                livingEntity.interact(player, InteractionHand.MAIN_HAND);
                            }
                        }
                    }
                }
            });

            context.setPacketHandled(true);
        }
    }
}