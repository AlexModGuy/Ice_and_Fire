package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TileEntityEggInIce extends TileEntity implements ITickableTileEntity {
    public EnumDragonEgg type;
    public int age;
    public int ticksExisted;
    // boolean to prevent time in a bottle shenanigans
    private boolean spawned;
    @Nullable
    public UUID ownerUUID;

    public TileEntityEggInIce() {
        super(IafTileEntityRegistry.EGG_IN_ICE.get());
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        if (type != null) {
            tag.putByte("Color", (byte) type.ordinal());
        } else {
            tag.putByte("Color", (byte) 0);
        }
        tag.putInt("Age", age);
        if (ownerUUID == null) {
            tag.putString("OwnerUUID", "");
        } else {
            tag.putUniqueId("OwnerUUID", ownerUUID);
        }
        return tag;
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state,tag);
        type = EnumDragonEgg.values()[tag.getByte("Color")];
        age = tag.getInt("Age");
        UUID s = null;

        if (tag.hasUniqueId("OwnerUUID")) {
            s = tag.getUniqueId("OwnerUUID");
        } else {
            try {
                String s1 = tag.getString("OwnerUUID");
                s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.world.getServer(), s1);
            }
            catch (Exception ignored) { }
        }
        if (s != null) {
            ownerUUID = s;
        }
    }

    @Override
    public void handleUpdateTag(BlockState blockState, CompoundNBT parentNBTTagCompound)
    {
        this.read(blockState, parentNBTTagCompound);
    }

    public CompoundNBT getUpdateTag()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return nbtTagCompound;
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        CompoundNBT nbtTagCompound = new CompoundNBT();
        write(nbtTagCompound);
        return new SUpdateTileEntityPacket(this.pos, -1, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState blockState = world.getBlockState(pos);
        read(blockState, pkt.getNbtCompound());   // read from the nbt in the packet
    }

    public void spawnEgg() {
        if (type != null) {
            EntityDragonEgg egg = new EntityDragonEgg(IafEntityRegistry.DRAGON_EGG.get(), world);
            egg.setEggType(type);
            egg.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
            egg.setOwnerId(this.ownerUUID);
            if (!world.isRemote) {
                world.addEntity(egg);
            }
        }
    }

    @Override
    public void tick() {
        age++;
        if (age >= IafConfig.dragonEggTime && type != null && !spawned) {
            if (!world.isRemote) {
                EntityIceDragon dragon = new EntityIceDragon(world);
                dragon.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                dragon.setVariant(type.ordinal() - 4);
                dragon.setGender(ThreadLocalRandom.current().nextBoolean());
                dragon.setTamed(true);
                dragon.setHunger(50);
                dragon.setOwnerId(ownerUUID);
                world.addEntity(dragon);
                spawned = true;
                world.destroyBlock(pos, false);
                world.setBlockState(pos, Blocks.WATER.getDefaultState());
            }

        }
        ticksExisted++;
    }
}
