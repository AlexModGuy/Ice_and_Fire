package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouseModel;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieJar;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class TileEntityJar extends TileEntity implements ITickableTileEntity {

    private static final float PARTICLE_WIDTH = 0.3F;
    private static final float PARTICLE_HEIGHT = 0.6F;
    public boolean hasPixie;
    public boolean prevHasProduced;
    public boolean hasProduced;
    public boolean tamedPixie;
    public UUID pixieOwnerUUID;
    public int pixieType;
    public int ticksExisted;
    public NonNullList<ItemStack> pixieItems = NonNullList.withSize(1, ItemStack.EMPTY);
    public float rotationYaw;
    public float prevRotationYaw;
    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler> downHandler = PixieJarInvWrapper
        .create(this);
    private final Random rand;

    public TileEntityJar() {
        super(IafTileEntityRegistry.PIXIE_JAR.get());
        this.rand = new Random();
        this.hasPixie = true;
    }

    public TileEntityJar(boolean empty) {
        super(IafTileEntityRegistry.PIXIE_JAR.get());
        this.rand = new Random();
        this.hasPixie = !empty;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putBoolean("HasPixie", hasPixie);
        compound.putInt("PixieType", pixieType);
        compound.putBoolean("HasProduced", hasProduced);
        compound.putBoolean("TamedPixie", tamedPixie);
        if (pixieOwnerUUID != null) {
            compound.putUUID("PixieOwnerUUID", pixieOwnerUUID);
        }
        compound.putInt("TicksExisted", ticksExisted);
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
            IceAndFire.sendMSGToAll(new MessageUpdatePixieHouseModel(worldPosition.asLong(), packet.getTag().getInt("PixieType")));
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        hasPixie = compound.getBoolean("HasPixie");
        pixieType = compound.getInt("PixieType");
        hasProduced = compound.getBoolean("HasProduced");
        ticksExisted = compound.getInt("TicksExisted");
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
        if (this.level.isClientSide && this.hasPixie) {
            IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie, this.worldPosition.getX() + 0.5F + (double) (this.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH, this.worldPosition.getY() + (double) (this.rand.nextFloat() * PARTICLE_HEIGHT), this.worldPosition.getZ() + 0.5F + (double) (this.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH, EntityPixie.PARTICLE_RGB[this.pixieType][0], EntityPixie.PARTICLE_RGB[this.pixieType][1], EntityPixie.PARTICLE_RGB[this.pixieType][2]);
        }
        if (ticksExisted % 24000 == 0 && !this.hasProduced && this.hasPixie) {
            this.hasProduced = true;
            if (!this.getLevel().isClientSide) {
                IceAndFire.sendMSGToAll(new MessageUpdatePixieJar(worldPosition.asLong(), hasProduced));
            }
        }
        if (this.hasPixie && hasProduced != prevHasProduced && ticksExisted > 5) {
            if (!this.getLevel().isClientSide) {
                IceAndFire.sendMSGToAll(new MessageUpdatePixieJar(worldPosition.asLong(), hasProduced));
            } else {
                level.playLocalSound(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5, IafSoundRegistry.PIXIE_HURT, SoundCategory.BLOCKS, 1, 1, false);
            }
        }
        prevRotationYaw = rotationYaw;
        if (rand.nextInt(30) == 0) {
            this.rotationYaw = (rand.nextFloat() * 360F) - 180F;
        }
        if (this.hasPixie && ticksExisted % 40 == 0 && this.rand.nextInt(6) == 0 && level.isClientSide) {
            this.level.playLocalSound(this.worldPosition.getX() + 0.5D, this.worldPosition.getY() + 0.5D, this.worldPosition.getZ() + 0.5, IafSoundRegistry.PIXIE_IDLE, SoundCategory.BLOCKS, 1, 1, false);
        }
        prevHasProduced = hasProduced;
    }

    public void releasePixie() {
        EntityPixie pixie = new EntityPixie(IafEntityRegistry.PIXIE.get(), this.level);
        pixie.absMoveTo(this.worldPosition.getX() + 0.5F, this.worldPosition.getY() + 1F, this.worldPosition.getZ() + 0.5F, new Random().nextInt(360), 0);
        pixie.setItemInHand(Hand.MAIN_HAND, pixieItems.get(0));
        pixie.setColor(this.pixieType);
        level.addFreshEntity(pixie);
        this.hasPixie = false;
        this.pixieType = 0;
        pixie.ticksUntilHouseAI = 500;
        pixie.setTame(this.tamedPixie);
        pixie.setOwnerUUID(this.pixieOwnerUUID);

        if (!level.isClientSide) {
            IceAndFire.sendMSGToAll(new MessageUpdatePixieHouse(worldPosition.asLong(), false, 0));
        }
    }

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (facing == Direction.DOWN
            && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return downHandler.cast();
        return super.getCapability(capability, facing);
    }
}
