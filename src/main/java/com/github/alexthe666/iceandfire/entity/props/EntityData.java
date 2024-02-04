package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class EntityData {
    public FrozenData frozenData = new FrozenData();
    public ChainData chainData = new ChainData();
    public SirenData sirenData = new SirenData();

    public void tick(final LivingEntity entity) {
        boolean updateClients = frozenData.tickFrozen(entity);
        updateClients = updateClients || chainData.tickChain(entity);
        updateClients = updateClients || sirenData.tickCharmed(entity);

        if (updateClients && !entity.getLevel().isClientSide()) {
            IceAndFire.sendMSGToAll(new SyncEntityData(entity.getId(), serialize()));
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
