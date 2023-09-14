package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouseModel;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieJar;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class TileEntityJar extends BlockEntity {

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

    public TileEntityJar(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.PIXIE_JAR.get(), pos, state);
        this.rand = new Random();
        this.hasPixie = true;
    }

    public TileEntityJar(BlockPos pos, BlockState state, boolean empty) {
        super(IafTileEntityRegistry.PIXIE_JAR.get(), pos, state);
        this.rand = new Random();
        this.hasPixie = !empty;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.putBoolean("HasPixie", hasPixie);
        compound.putInt("PixieType", pixieType);
        compound.putBoolean("HasProduced", hasProduced);
        compound.putBoolean("TamedPixie", tamedPixie);
        if (pixieOwnerUUID != null) {
            compound.putUUID("PixieOwnerUUID", pixieOwnerUUID);
        }
        compound.putInt("TicksExisted", ticksExisted);
        ContainerHelper.saveAllItems(compound, this.pixieItems);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        load(packet.getTag());
        if (!level.isClientSide) {
            IceAndFire.sendMSGToAll(new MessageUpdatePixieHouseModel(worldPosition.asLong(), packet.getTag().getInt("PixieType")));
        }
    }

    @Override
    public void load(CompoundTag compound) {
        hasPixie = compound.getBoolean("HasPixie");
        pixieType = compound.getInt("PixieType");
        hasProduced = compound.getBoolean("HasProduced");
        ticksExisted = compound.getInt("TicksExisted");
        tamedPixie = compound.getBoolean("TamedPixie");
        if (compound.hasUUID("PixieOwnerUUID")) {
            pixieOwnerUUID = compound.getUUID("PixieOwnerUUID");
        }
        this.pixieItems = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, pixieItems);
        super.load(compound);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityJar entityJar) {
        entityJar.ticksExisted++;
        if (level.isClientSide && entityJar.hasPixie) {
            IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie,
                pos.getX() + 0.5F + (double) (entityJar.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH,
                pos.getY() + (double) (entityJar.rand.nextFloat() * PARTICLE_HEIGHT),
                pos.getZ() + 0.5F + (double) (entityJar.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH, EntityPixie.PARTICLE_RGB[entityJar.pixieType][0], EntityPixie.PARTICLE_RGB[entityJar.pixieType][1], EntityPixie.PARTICLE_RGB[entityJar.pixieType][2]);
        }
        if (entityJar.ticksExisted % 24000 == 0 && !entityJar.hasProduced && entityJar.hasPixie) {
            entityJar.hasProduced = true;
            if (!level.isClientSide) {
                IceAndFire.sendMSGToAll(new MessageUpdatePixieJar(pos.asLong(), entityJar.hasProduced));
            }
        }
        if (entityJar.hasPixie && entityJar.hasProduced != entityJar.prevHasProduced && entityJar.ticksExisted > 5) {
            if (!level.isClientSide) {
                IceAndFire.sendMSGToAll(new MessageUpdatePixieJar(pos.asLong(), entityJar.hasProduced));
            } else {
                level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, IafSoundRegistry.PIXIE_HURT, SoundSource.BLOCKS, 1, 1, false);
            }
        }
        entityJar.prevRotationYaw = entityJar.rotationYaw;
        if (entityJar.rand.nextInt(30) == 0) {
            entityJar.rotationYaw = (entityJar.rand.nextFloat() * 360F) - 180F;
        }
        if (entityJar.hasPixie && entityJar.ticksExisted % 40 == 0 && entityJar.rand.nextInt(6) == 0 && level.isClientSide) {
            level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5, IafSoundRegistry.PIXIE_IDLE, SoundSource.BLOCKS, 1, 1, false);
        }
        entityJar.prevHasProduced = entityJar.hasProduced;
    }

    public void releasePixie() {
        EntityPixie pixie = new EntityPixie(IafEntityRegistry.PIXIE.get(), this.level);
        pixie.absMoveTo(this.worldPosition.getX() + 0.5F, this.worldPosition.getY() + 1F, this.worldPosition.getZ() + 0.5F, new Random().nextInt(360), 0);
        pixie.setItemInHand(InteractionHand.MAIN_HAND, pixieItems.get(0));
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
    public <T> net.minecraftforge.common.util.@NotNull LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (facing == Direction.DOWN
            && capability == ForgeCapabilities.ITEM_HANDLER)
            return downHandler.cast();
        return super.getCapability(capability, facing);
    }
}
