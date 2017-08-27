package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
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

public class TileEntityPixieHouse extends TileEntity implements ITickable {

    public int houseType;
    public boolean hasPixie;
    public int pixieType;
    public int ticksExisted;
    public NonNullList<ItemStack> pixieItems = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("HouseType", houseType);
        compound.setBoolean("HasPixie", hasPixie);
        compound.setInteger("PixieType", pixieType);
        ItemStackHelper.saveAllItems(compound, this.pixieItems);
        return super.writeToNBT(compound);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new SPacketUpdateTileEntity(pos, 1, tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    public void readFromNBT(NBTTagCompound compound) {
        houseType = compound.getInteger("HouseType");
        hasPixie = compound.getBoolean("HasPixie");
        pixieType = compound.getInteger("PixieType");
        this.pixieItems = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, pixieItems);
        super.readFromNBT(compound);
    }

    @Override
    public void update() {
        ticksExisted++;
        if(!world.isRemote && this.hasPixie && new Random().nextInt(100) == 0){
            releasePixie();
        }
    }

    public void releasePixie(){
        EntityPixie pixie = new EntityPixie(this.world);
        pixie.setPositionAndRotation(this.pos.getX() + 0.5F, this.pos.getY() + 1F, this.pos.getZ() + 0.5F, new Random().nextInt(360), 0);
        pixie.setHeldItem(EnumHand.MAIN_HAND, pixieItems.get(0));
        pixie.setColor(this.pixieType);
        if(!world.isRemote){
            world.spawnEntity(pixie);
        }
        this.hasPixie = false;
        this.pixieType = 0;
        pixie.ticksUntilHouseAI = 500;
        if(!world.isRemote){
            IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageUpdatePixieHouse(pos.toLong(), false, 0));
        }
    }
}
