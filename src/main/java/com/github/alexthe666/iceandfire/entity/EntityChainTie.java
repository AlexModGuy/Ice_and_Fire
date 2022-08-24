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
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;

public class EntityChainTie extends HangingEntity {

    public EntityChainTie(EntityType<? extends HangingEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityChainTie(EntityType<? extends HangingEntity> type, World worldIn, BlockPos hangingPositionIn) {
        super(type, worldIn, hangingPositionIn);
        this.setPos(hangingPositionIn.getX() + 0.5D, hangingPositionIn.getY(), hangingPositionIn.getZ() + 0.5D);
        this.forcedLoading = true;
    }

    public static EntityChainTie createTie(World worldIn, BlockPos fence) {
        EntityChainTie entityChainTie = new EntityChainTie(IafEntityRegistry.CHAIN_TIE.get(), worldIn, fence);
        worldIn.addFreshEntity(entityChainTie);
        entityChainTie.playPlacementSound();
        return entityChainTie;
    }

    @Nullable
    public static EntityChainTie getKnotForPosition(World worldIn, BlockPos pos) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        for (EntityChainTie entityleashknot : worldIn.getEntitiesOfClass(EntityChainTie.class,
            new AxisAlignedBB(i - 1.0D, j - 1.0D, k - 1.0D, i + 1.0D, j + 1.0D, k + 1.0D))) {
            if (entityleashknot != null && entityleashknot.getPos() != null && entityleashknot.getPos().equals(pos)) {
                return entityleashknot;
            }
        }
        return null;
    }

    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(MathHelper.floor(x) + 0.5D, MathHelper.floor(y) + 0.5D, MathHelper.floor(z) + 0.5D);
    }

    @Override
    protected void recalculateBoundingBox() {
        this.setPosRaw(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D,
            this.pos.getZ() + 0.5D);
        double xSize = 0.3D;
        double ySize = 0.875D;
        double zSize = xSize;
        this.setBoundingBox(new AxisAlignedBB(this.getX() - xSize, this.getY() - 0.5, this.getZ() - zSize,
            this.getX() + xSize, this.getY() + ySize - 0.5, this.getZ() + zSize));
        if (this.isAddedToWorld() && this.level instanceof net.minecraft.world.server.ServerWorld)
            ((net.minecraft.world.server.ServerWorld) this.level).updateChunkPos(this); // Forge - Process chunk registration after moving.
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && source.getEntity() instanceof PlayerEntity) {
            return super.hurt(source, amount);
        }
        return false;
    }

    @Override
    public int getWidth() {
        return 9;
    }

    @Override
    public int getHeight() {
        return 9;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        BlockPos blockpos = this.getPos();
        compound.putInt("TileX", blockpos.getX());
        compound.putInt("TileY", blockpos.getY());
        compound.putInt("TileZ", blockpos.getZ());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        this.pos = new BlockPos(compound.getInt("TileX"), compound.getInt("TileY"), compound.getInt("TileZ"));
    }

    @Override
    protected float getEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return -0.0625F;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 1024.0D;
    }

    @Override
    public void dropItem(@Nullable Entity brokenEntity) {
        this.playSound(SoundEvents.ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
    }

    @Override
    public void remove() {
        this.remove(false);
    }

    @Override
    public void remove(boolean keepData) {
        super.remove(keepData);
        double d0 = 30D;
        List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(this.getX() - d0, this.getY() - d0, this.getZ() - d0, this.getX() + d0, this.getY() + d0, this.getZ() + d0));
        for (LivingEntity livingEntity : list) {
            if (ChainProperties.isChainedTo(livingEntity, this)) {
                ChainProperties.removeChain(livingEntity, this);
                ItemEntity entityitem = new ItemEntity(this.level, this.getX(), this.getY() + 1, this.getZ(),
                    new ItemStack(IafItemRegistry.CHAIN));
                entityitem.setDefaultPickUpDelay();
                this.level.addFreshEntity(entityitem);
            }
        }
    }

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        if (this.level.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            boolean flag = false;
            double d0 = 30D;
            List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(this.getX() - d0, this.getY() - d0, this.getZ() - d0, this.getX() + d0, this.getY() + d0, this.getZ() + d0));

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
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean survives() {
        return this.level.getBlockState(this.pos).getBlock() instanceof WallBlock;
    }

    @Override
    public void playPlacementSound() {
        this.playSound(SoundEvents.ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
    }
}
