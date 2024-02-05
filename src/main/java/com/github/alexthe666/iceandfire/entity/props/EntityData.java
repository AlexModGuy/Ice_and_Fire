package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.PacketDistributor;

public class EntityData {
    public FrozenData frozenData = new FrozenData();
    public ChainData chainData = new ChainData();
    public SirenData sirenData = new SirenData();

    public void tick(final LivingEntity entity) {
        frozenData.tickFrozen(entity);
        chainData.tickChain(entity);
        sirenData.tickCharmed(entity);

        boolean triggerClientUpdate = frozenData.doesClientNeedUpdate();
        triggerClientUpdate = chainData.doesClientNeedUpdate() || triggerClientUpdate;
        triggerClientUpdate = sirenData.doesClientNeedUpdate() || triggerClientUpdate;

        if (triggerClientUpdate && !entity.getLevel().isClientSide()) {

            if (entity instanceof ServerPlayer serverPlayer) {
                IceAndFire.NETWORK_WRAPPER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayer), new SyncEntityData(entity.getId(), serialize()));
            } else {
                IceAndFire.NETWORK_WRAPPER.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SyncEntityData(entity.getId(), serialize()));
            }
        }
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        frozenData.serialize(tag);
        chainData.serialize(tag);
        sirenData.serialize(tag);
        return tag;
    }

    public void deserialize(final CompoundTag tag) {
        frozenData.deserialize(tag);
        chainData.deserialize(tag);
        sirenData.deserialize(tag);
    }
}
