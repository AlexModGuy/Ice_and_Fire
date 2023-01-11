package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TileEntityEggInIce extends BlockEntity {
    public EnumDragonEgg type;
    public int age;
    public int ticksExisted;
    @Nullable
    public UUID ownerUUID;
    // boolean to prevent time in a bottle shenanigans
    private boolean spawned;

    public TileEntityEggInIce(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.EGG_IN_ICE.get(), pos, state);
    }

    public static void tickEgg(Level level, BlockPos pos, BlockState state, TileEntityEggInIce entityEggInIce) {
        entityEggInIce.age++;
        if (entityEggInIce.age >= IafConfig.dragonEggTime && entityEggInIce.type != null && !entityEggInIce.spawned) {
            if (!level.isClientSide) {
                EntityIceDragon dragon = new EntityIceDragon(level);
                dragon.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                dragon.setVariant(entityEggInIce.type.ordinal() - 4);
                dragon.setGender(ThreadLocalRandom.current().nextBoolean());
                dragon.setTame(true);
                dragon.setHunger(50);
                dragon.setOwnerUUID(entityEggInIce.ownerUUID);
                level.addFreshEntity(dragon);
                entityEggInIce.spawned = true;
                level.destroyBlock(pos, false);
                level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            }

        }
        entityEggInIce.ticksExisted++;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        if (type != null) {
            tag.putByte("Color", (byte) type.ordinal());
        } else {
            tag.putByte("Color", (byte) 0);
        }
        tag.putInt("Age", age);
        if (ownerUUID == null) {
            tag.putString("OwnerUUID", "");
        } else {
            tag.putUUID("OwnerUUID", ownerUUID);
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        type = EnumDragonEgg.values()[tag.getByte("Color")];
        age = tag.getInt("Age");
        UUID s = null;

        if (tag.hasUUID("OwnerUUID")) {
            s = tag.getUUID("OwnerUUID");
        } else {
            try {
                String s1 = tag.getString("OwnerUUID");
                s = OldUsersConverter.convertMobOwnerIfNecessary(this.level.getServer(), s1);
            } catch (Exception ignored) {
            }
        }
        if (s != null) {
            ownerUUID = s;
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag parentNBTTagCompound) {
        this.load(parentNBTTagCompound);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbtTagCompound = new CompoundTag();
        saveAdditional(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbtTagCompound = new CompoundTag();
        saveAdditional(nbtTagCompound);
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());   // read from the nbt in the packet
    }

    public void spawnEgg() {
        if (type != null) {
            EntityDragonEgg egg = new EntityDragonEgg(IafEntityRegistry.DRAGON_EGG.get(), level);
            egg.setEggType(type);
            egg.setPos(worldPosition.getX() + 0.5, worldPosition.getY() + 1, worldPosition.getZ() + 0.5);
            egg.setOwnerId(this.ownerUUID);
            if (!level.isClientSide) {
                level.addFreshEntity(egg);
            }
        }
    }
}
