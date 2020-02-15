package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouseModel;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

import java.util.Random;
import java.util.UUID;

public class TileEntityPixieHouse extends TileEntity implements ITickable {

    private static final float PARTICLE_WIDTH = 0.3F;
    private static final float PARTICLE_HEIGHT = 0.6F;
    public int houseType;
    public boolean hasPixie;
    public boolean tamedPixie;
    public UUID pixieOwnerUUID;
    public int pixieType;
    public int ticksExisted;
    public NonNullList<ItemStack> pixieItems = NonNullList.withSize(1, ItemStack.EMPTY);
    private Random rand;

    public TileEntityPixieHouse() {
        this.rand = new Random();
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("HouseType", houseType);
        compound.setBoolean("HasPixie", hasPixie);
        compound.setInteger("PixieType", pixieType);
        compound.setBoolean("TamedPixie", tamedPixie);
        if (pixieOwnerUUID != null) {
            compound.setUniqueId("PixieOwnerUUID", pixieOwnerUUID);
        }
        ItemStackHelper.saveAllItems(compound, this.pixieItems);
        return compound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new SPacketUpdateTileEntity(pos, 1, tag);
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
        if (!world.isRemote) {
            IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageUpdatePixieHouseModel(pos.toLong(), packet.getNbtCompound().getInteger("HouseType")));
        }
    }

    public void readFromNBT(NBTTagCompound compound) {
        houseType = compound.getInteger("HouseType");
        hasPixie = compound.getBoolean("HasPixie");
        pixieType = compound.getInteger("PixieType");
        tamedPixie = compound.getBoolean("TamedPixie");
        pixieOwnerUUID = compound.getUniqueId("TicksExisted");
        this.pixieItems = NonNullList.withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, pixieItems);
        super.readFromNBT(compound);
    }

    @Override
    public void update() {
        ticksExisted++;
        if (!world.isRemote && this.hasPixie && new Random().nextInt(100) == 0) {
            releasePixie();
        }
        if (this.world.isRemote && this.hasPixie) {
            IceAndFire.PROXY.spawnParticle("if_pixie", this.pos.getX() + 0.5F + (double) (this.rand.nextFloat() * PARTICLE_WIDTH * 2F) - (double) PARTICLE_WIDTH, this.pos.getY() + (double) (this.rand.nextFloat() * PARTICLE_HEIGHT), this.pos.getZ() + 0.5F + (double) (this.rand.nextFloat() * PARTICLE_WIDTH * 2F) - (double) PARTICLE_WIDTH, EntityPixie.PARTICLE_RGB[this.pixieType][0], EntityPixie.PARTICLE_RGB[this.pixieType][1], EntityPixie.PARTICLE_RGB[this.pixieType][2]);
        }
    }

    public void releasePixie() {
        EntityPixie pixie = new EntityPixie(this.world);
        pixie.setPositionAndRotation(this.pos.getX() + 0.5F, this.pos.getY() + 1F, this.pos.getZ() + 0.5F, new Random().nextInt(360), 0);
        pixie.setHeldItem(EnumHand.MAIN_HAND, pixieItems.get(0));
        pixie.setColor(this.pixieType);
        if (!world.isRemote) {
            world.spawnEntity(pixie);
        }
        this.hasPixie = false;
        this.pixieType = 0;
        pixie.ticksUntilHouseAI = 500;
        pixie.setTamed(this.tamedPixie);
        pixie.setOwnerId(this.pixieOwnerUUID);
        if (!world.isRemote) {
            IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageUpdatePixieHouse(pos.toLong(), false, 0));
        }
    }
}
