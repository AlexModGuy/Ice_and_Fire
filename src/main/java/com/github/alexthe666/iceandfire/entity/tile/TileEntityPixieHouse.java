package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouseModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TileEntityPixieHouse extends TileEntity implements ITickableTileEntity {

    private static final float PARTICLE_WIDTH = 0.3F;
    private static final float PARTICLE_HEIGHT = 0.6F;
    public int houseType;
    public boolean hasPixie;
    public boolean tamedPixie;
    public UUID pixieOwnerUUID;
    public int pixieType;
    public int ticksExisted;
    public NonNullList<ItemStack> pixieItems = NonNullList.withSize(1, ItemStack.EMPTY);
    private final Random rand;

    public TileEntityPixieHouse() {
        super(IafTileEntityRegistry.PIXIE_HOUSE.get());
        this.rand = new Random();
    }

    public static int getHouseTypeFromBlock(Block block) {
        if (block == IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED) return 1;
        if (block == IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN) return 0;
        if (block == IafBlockRegistry.PIXIE_HOUSE_OAK) return 3;
        if (block == IafBlockRegistry.PIXIE_HOUSE_BIRCH) return 2;
        if (block == IafBlockRegistry.PIXIE_HOUSE_SPRUCE) return 5;
        if (block == IafBlockRegistry.PIXIE_HOUSE_DARK_OAK) return 4;
        else return 0;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putInt("HouseType", houseType);
        compound.putBoolean("HasPixie", hasPixie);
        compound.putInt("PixieType", pixieType);
        compound.putBoolean("TamedPixie", tamedPixie);
        if (pixieOwnerUUID != null) {
            compound.putUUID("PixieOwnerUUID", pixieOwnerUUID);
        }
        ItemStackHelper.saveAllItems(compound, this.pixieItems);
        return compound;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        load(this.getBlockState(), packet.getTag());
        if (!level.isClientSide) {
            IceAndFire.sendMSGToAll(
                new MessageUpdatePixieHouseModel(worldPosition.asLong(), packet.getTag().getInt("HouseType")));
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        houseType = compound.getInt("HouseType");
        hasPixie = compound.getBoolean("HasPixie");
        pixieType = compound.getInt("PixieType");
        tamedPixie = compound.getBoolean("TamedPixie");
        if (compound.hasUUID("PixieOwnerUUID")) {
            pixieOwnerUUID = compound.getUUID("PixieOwnerUUID");
        }
        this.pixieItems = NonNullList.withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, pixieItems);
        super.load(state, compound);
    }

    @Override
    public void tick() {
        ticksExisted++;
        if (!level.isClientSide && this.hasPixie && ThreadLocalRandom.current().nextInt(100) == 0) {
            releasePixie();
        }
        if (this.level.isClientSide && this.hasPixie) {
            IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie,
                this.worldPosition.getX() + 0.5F + (double) (this.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH,
                this.worldPosition.getY() + (double) (this.rand.nextFloat() * PARTICLE_HEIGHT),
                this.worldPosition.getZ() + 0.5F + (double) (this.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH,
                EntityPixie.PARTICLE_RGB[this.pixieType][0], EntityPixie.PARTICLE_RGB[this.pixieType][1],
                EntityPixie.PARTICLE_RGB[this.pixieType][2]);
        }
    }

    public void releasePixie() {
        EntityPixie pixie = new EntityPixie(IafEntityRegistry.PIXIE.get(), this.level);
        pixie.absMoveTo(this.worldPosition.getX() + 0.5F, this.worldPosition.getY() + 1F, this.worldPosition.getZ() + 0.5F,
            ThreadLocalRandom.current().nextInt(360), 0);
        pixie.setItemInHand(Hand.MAIN_HAND, pixieItems.get(0));
        pixie.setColor(this.pixieType);
        if (!level.isClientSide) {
            level.addFreshEntity(pixie);
        }
        this.hasPixie = false;
        this.pixieType = 0;
        pixie.ticksUntilHouseAI = 500;
        pixie.setTame(this.tamedPixie);
        pixie.setOwnerUUID(this.pixieOwnerUUID);
        if (!level.isClientSide) {
            IceAndFire.sendMSGToAll(new MessageUpdatePixieHouse(worldPosition.asLong(), false, 0));
        }
    }
}
