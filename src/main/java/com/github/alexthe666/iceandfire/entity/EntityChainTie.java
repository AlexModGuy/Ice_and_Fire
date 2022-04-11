package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.props.ChainProperties;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.block.WallBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.item.HangingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class EntityChainTie extends HangingEntity {

    public EntityChainTie(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public EntityChainTie(EntityType type, World worldIn, BlockPos hangingPositionIn) {
        super(type, worldIn, hangingPositionIn);
        this.setPosition((double) hangingPositionIn.getX() + 0.5D, hangingPositionIn.getY(), (double) hangingPositionIn.getZ() + 0.5D);
        this.forceSpawn = true;
    }

    public static EntityChainTie createTie(World worldIn, BlockPos fence) {
        EntityChainTie entityChainTie = new EntityChainTie(IafEntityRegistry.CHAIN_TIE, worldIn, fence);
        worldIn.addEntity(entityChainTie);
        entityChainTie.playPlaceSound();
        return entityChainTie;
    }

    @Nullable
    public static EntityChainTie getKnotForPosition(World worldIn, BlockPos pos) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        for (EntityChainTie entityleashknot : worldIn.getEntitiesWithinAABB(EntityChainTie.class, new AxisAlignedBB((double) i - 1.0D, (double) j - 1.0D, (double) k - 1.0D, (double) i + 1.0D, (double) j + 1.0D, (double) k + 1.0D))) {
            if (entityleashknot != null && entityleashknot.getHangingPosition() != null && entityleashknot.getHangingPosition().equals(pos)) {
                return entityleashknot;
            }
        }
        return null;
    }

    public void setPosition(double x, double y, double z) {
        super.setPosition((double) MathHelper.floor(x) + 0.5D, (double) MathHelper.floor(y) + 0.5D, (double) MathHelper.floor(z) + 0.5D);
    }

    protected void updateBoundingBox() {
        this.setRawPosition((double) this.hangingPosition.getX() + 0.5D, (double) this.hangingPosition.getY() + 0.5D, (double) this.hangingPosition.getZ() + 0.5D);
        double xSize = 0.3D;
        double ySize = 0.875D;
        double zSize = xSize;
        this.setBoundingBox(new AxisAlignedBB(this.getPosX() - xSize, this.getPosY() - 0.5, this.getPosZ() - zSize,
            this.getPosX() + xSize, this.getPosY() + ySize - 0.5, this.getPosZ() + zSize));
        if (this.isAddedToWorld() && this.world instanceof net.minecraft.world.server.ServerWorld)
            ((net.minecraft.world.server.ServerWorld) this.world).chunkCheck(this); // Forge - Process chunk registration after moving.
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() != null && source.getTrueSource() instanceof PlayerEntity) {
            return super.attackEntityFrom(source, amount);
        }
        return false;
    }

    public int getWidthPixels() {
        return 9;
    }

    public int getHeightPixels() {
        return 9;
    }

    public void writeAdditional(CompoundNBT compound) {
        BlockPos blockpos = this.getHangingPosition();
        compound.putInt("TileX", blockpos.getX());
        compound.putInt("TileY", blockpos.getY());
        compound.putInt("TileZ", blockpos.getZ());
    }

    public void readAdditional(CompoundNBT compound) {
        this.hangingPosition = new BlockPos(compound.getInt("TileX"), compound.getInt("TileY"), compound.getInt("TileZ"));
    }

    protected float getEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return -0.0625F;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 1024.0D;
    }

    public void onBroken(@Nullable Entity brokenEntity) {
        this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
    }

    @Override
    public void remove() {
        this.remove(false);
    }

    @Override
    public void remove(boolean keepData) {
        super.remove(keepData);
        double d0 = 30D;
        List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(this.getPosX() - d0, this.getPosY() - d0, this.getPosZ() - d0, this.getPosX() + d0, this.getPosY() + d0, this.getPosZ() + d0));
        for (LivingEntity livingEntity : list) {
            if (ChainProperties.isChainedTo(livingEntity, this)) {
                ChainProperties.removeChain(livingEntity, this);
                ItemEntity entityitem = new ItemEntity(this.world, this.getPosX(), this.getPosY() + (double) 1, this.getPosZ(), new ItemStack(IafItemRegistry.CHAIN));
                entityitem.setDefaultPickupDelay();
                this.world.addEntity(entityitem);
            }
        }
    }

    @Override
    public ActionResultType processInitialInteract(PlayerEntity player, Hand hand) {
        if (this.world.isRemote) {
            return ActionResultType.SUCCESS;
        } else {
            boolean flag = false;
            double d0 = 30D;
            List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(this.getPosX() - d0, this.getPosY() - d0, this.getPosZ() - d0, this.getPosX() + d0, this.getPosY() + d0, this.getPosZ() + d0));

            for (LivingEntity livingEntity : list) {
                if (ChainProperties.isChainedTo(livingEntity, player)) {
                    ChainProperties.removeChain(livingEntity, player);
                    ChainProperties.attachChain(livingEntity, this);
                    flag = true;
                }
            }

            if (!flag) {
                this.remove();
                return ActionResultType.SUCCESS;
            }

            return ActionResultType.CONSUME;
        }
    }


    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public boolean onValidSurface() {
        return this.world.getBlockState(this.hangingPosition).getBlock() instanceof WallBlock;
    }

    public void playPlaceSound() {
        this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
    }
}
