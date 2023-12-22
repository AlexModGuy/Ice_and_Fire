package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityPixie;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouseModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class TileEntityPixieHouse extends BlockEntity {

    private static final float PARTICLE_WIDTH = 0.3F;
    private static final float PARTICLE_HEIGHT = 0.6F;
    private final Random rand;
    public int houseType;
    public boolean hasPixie;
    public boolean tamedPixie;
    public UUID pixieOwnerUUID;
    public int pixieType;
    public NonNullList<ItemStack> pixieItems = NonNullList.withSize(1, ItemStack.EMPTY);

    public TileEntityPixieHouse(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.PIXIE_HOUSE.get(), pos, state);
        this.rand = new Random();
    }

    public static int getHouseTypeFromBlock(Block block) {
        if (block == IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_RED.get()) return 1;
        if (block == IafBlockRegistry.PIXIE_HOUSE_MUSHROOM_BROWN.get()) return 0;
        if (block == IafBlockRegistry.PIXIE_HOUSE_OAK.get()) return 3;
        if (block == IafBlockRegistry.PIXIE_HOUSE_BIRCH.get()) return 2;
        if (block == IafBlockRegistry.PIXIE_HOUSE_SPRUCE.get()) return 5;
        if (block == IafBlockRegistry.PIXIE_HOUSE_DARK_OAK.get()) return 4;
        else return 0;
    }

    public static void tickClient(Level level, BlockPos pos, BlockState state, TileEntityPixieHouse entityPixieHouse) {
        if (entityPixieHouse.hasPixie) {
            IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie,
                pos.getX() + 0.5F + (double) (entityPixieHouse.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH,
                pos.getY() + (double) (entityPixieHouse.rand.nextFloat() * PARTICLE_HEIGHT),
                pos.getZ() + 0.5F + (double) (entityPixieHouse.rand.nextFloat() * PARTICLE_WIDTH * 2F) - PARTICLE_WIDTH,
                EntityPixie.PARTICLE_RGB[entityPixieHouse.pixieType][0], EntityPixie.PARTICLE_RGB[entityPixieHouse.pixieType][1],
                EntityPixie.PARTICLE_RGB[entityPixieHouse.pixieType][2]);
        }
    }

    public static void tickServer(Level level, BlockPos pos, BlockState state, TileEntityPixieHouse entityPixieHouse) {
        if (entityPixieHouse.hasPixie && ThreadLocalRandom.current().nextInt(100) == 0) {
            entityPixieHouse.releasePixie();
        }
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.putInt("HouseType", houseType);
        compound.putBoolean("HasPixie", hasPixie);
        compound.putInt("PixieType", pixieType);
        compound.putBoolean("TamedPixie", tamedPixie);
        if (pixieOwnerUUID != null) {
            compound.putUUID("PixieOwnerUUID", pixieOwnerUUID);
        }
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
            IceAndFire.sendMSGToAll(
                new MessageUpdatePixieHouseModel(worldPosition.asLong(), packet.getTag().getInt("HouseType")));
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    @Override
    public void load(CompoundTag compound) {
        houseType = compound.getInt("HouseType");
        hasPixie = compound.getBoolean("HasPixie");
        pixieType = compound.getInt("PixieType");
        tamedPixie = compound.getBoolean("TamedPixie");
        if (compound.hasUUID("PixieOwnerUUID")) {
            pixieOwnerUUID = compound.getUUID("PixieOwnerUUID");
        }
        this.pixieItems = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, pixieItems);
        super.load(compound);
    }

    public void releasePixie() {
        EntityPixie pixie = new EntityPixie(IafEntityRegistry.PIXIE.get(), this.level);
        pixie.absMoveTo(this.worldPosition.getX() + 0.5F, this.worldPosition.getY() + 1F, this.worldPosition.getZ() + 0.5F,
            ThreadLocalRandom.current().nextInt(360), 0);
        pixie.setItemInHand(InteractionHand.MAIN_HAND, pixieItems.get(0));
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
