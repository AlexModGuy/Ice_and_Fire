package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

public class MessageSyncEffects {
    private int entityID;
    private Collection<MobEffectInstance> effects;


    /**
     * Update the potion effects between server and client
     * server transmit this msg to inform the client of the potion effects on an entity
     * client transmit this msg to request an update on an entity
     * @param dragon
     */
    public MessageSyncEffects(LivingEntity dragon) {
        this.entityID = dragon.getId();
        this.effects = dragon.getActiveEffects();
    }

    public MessageSyncEffects(int entityID, Collection<MobEffectInstance> effects) {
        this.entityID = entityID;
        this.effects = effects;
    }


    public static MessageSyncEffects decoder(FriendlyByteBuf buffer) {
        int entityID = buffer.readInt();
        Collection<MobEffectInstance> effects = buffer.readCollection(ArrayList::new, friendlyByteBuf -> {
            return MobEffectInstance.load(friendlyByteBuf.readNbt());
        });
        return new MessageSyncEffects(entityID, effects);
    }

    public void encoder(FriendlyByteBuf buffer) {
        buffer.writeInt(entityID);
        buffer.writeCollection(effects, (friendlyByteBuf, mobEffectInstance) -> {
            friendlyByteBuf.writeNbt(mobEffectInstance.save(new CompoundTag()));
        });

    }

    public boolean handler(Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() -> {
            contextSupplier.get().setPacketHandled(true);

            if (contextSupplier.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
                Entity entity = Minecraft.getInstance().level.getEntity(entityID);
                if (entity instanceof LivingEntity dragon) {
                    Iterator<MobEffectInstance> iterator = dragon.getActiveEffects().iterator();

                    boolean flag;
                    for(flag = false; iterator.hasNext(); flag = true) {
                        MobEffectInstance effect = iterator.next();
                        iterator.remove();
                    }
                    for (MobEffectInstance effect :
                            effects) {
                        dragon.forceAddEffect(effect, null);
                    }
                }
            } else if (contextSupplier.get().getDirection() == NetworkDirection.PLAY_TO_SERVER) {
                ServerPlayer serverPlayer = contextSupplier.get().getSender();
                if (serverPlayer != null) {
                    Entity entity = serverPlayer.level.getEntity(entityID);
                    if (entity instanceof LivingEntity dragon) {
                        IceAndFire.sendMSGToPlayer(new MessageSyncEffects(dragon), serverPlayer);
                    }
                }
            }
        });
        return true;
    }

}
