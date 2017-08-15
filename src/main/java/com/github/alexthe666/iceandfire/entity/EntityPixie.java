package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.ai.PixieAIFlee;
import com.github.alexthe666.iceandfire.entity.ai.PixieAITempt;
import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityPixie extends EntityTameable {

    private BlockPos housePos;
    private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityPixie.class, DataSerializers.VARINT);
    public NonNullList<ItemStack> heldItem = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
    public boolean slowSpeed = false;

    public EntityPixie(World worldIn) {
        super(worldIn);
        this.moveHelper = new EntityPixie.AIMoveControl(this);
        this.setSize(0.4F, 0.8F);
        this.experienceValue = 3;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(COLOR, 0);
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
        if (!this.isInWater()) {
            this.handleWaterMovement();
        }

        if (onGroundIn) {
            if (this.fallDistance > 0.0F) {
                state.getBlock().onFallenUpon(this.world, pos, this, this.fallDistance);
            }

            this.fallDistance = 0.0F;
        }
        else if (y < 0.0D) {
            this.fallDistance = (float)((double)this.fallDistance - y);
        }
    }

    public void fall(float distance, float damageMultiplier) {}

    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.4D));
        this.tasks.addTask(2, new AIMoveRandom());
        this.tasks.addTask(3, new PixieAITempt(this, 1.0D));
        this.tasks.addTask(3, new PixieAIFlee(this, EntityPlayer.class, 10, new Predicate<EntityPlayer>() {
            @Override
            public boolean apply(@Nullable EntityPlayer entity) {
                return true;
            }
        }));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setColor(this.rand.nextInt(5));
        return livingdata;
    }

    public void onLivingUpdate(){
        super.onLivingUpdate();
        this.motionY += 0.08D;

    }


    public int getColor() {
        return this.getDataManager().get(COLOR);
    }

    public void setColor(int color) {
        this.getDataManager().set(COLOR, color);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.setColor(compound.getInteger("Color"));
        this.heldItem = NonNullList.<ItemStack>withSize(1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.heldItem);
        super.readEntityFromNBT(compound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("Color", this.getColor());
        ItemStackHelper.saveAllItems(compound, this.heldItem);
        super.writeEntityToNBT(compound);
    }


    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    public BlockPos getHousePos() {
        return housePos;
    }

    public static BlockPos getPositionRelativetoGround(Entity entity, World world, double x, double z, Random rand){
        if(world.getTopSolidOrLiquidBlock(new BlockPos(x, entity.posY, z)) != null){
            return world.getTopSolidOrLiquidBlock(new BlockPos(x, entity.posY, z)).up(rand.nextInt(3) + 1);
        }
        return new BlockPos(x, entity.posY, z);
    }


    class AIMoveControl extends EntityMoveHelper {
        public AIMoveControl(EntityPixie pixie) {
            super(pixie);
            this.speed = 0.75F;
        }

        public void onUpdateMoveHelper() {
            if(EntityPixie.this.slowSpeed){
                this.speed = 0.4F;
            }
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double d0 = this.posX - EntityPixie.this.posX;
                double d1 = this.posY - EntityPixie.this.posY;
                double d2 = this.posZ - EntityPixie.this.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = (double) MathHelper.sqrt(d3);

                if (d3 < EntityPixie.this.getEntityBoundingBox().getAverageEdgeLength()) {
                    this.action = EntityMoveHelper.Action.WAIT;
                    EntityPixie.this.motionX *= 0.5D;
                    EntityPixie.this.motionY *= 0.5D;
                    EntityPixie.this.motionZ *= 0.5D;
                } else {
                    EntityPixie.this.motionX += d0 / d3 * 0.05D * this.speed;
                    EntityPixie.this.motionY += d1 / d3 * 0.05D * this.speed;
                    EntityPixie.this.motionZ += d2 / d3 * 0.05D * this.speed;

                    if (EntityPixie.this.getAttackTarget() == null) {
                        EntityPixie.this.rotationYaw = -((float) MathHelper.atan2(EntityPixie.this.motionX, EntityPixie.this.motionZ)) * (180F / (float) Math.PI);
                        EntityPixie.this.renderYawOffset = EntityPixie.this.rotationYaw;
                    } else {
                        double d4 = EntityPixie.this.getAttackTarget().posX - EntityPixie.this.posX;
                        double d5 = EntityPixie.this.getAttackTarget().posZ - EntityPixie.this.posZ;
                        EntityPixie.this.rotationYaw = -((float) MathHelper.atan2(d4, d5)) * (180F / (float) Math.PI);
                        EntityPixie.this.renderYawOffset = EntityPixie.this.rotationYaw;
                    }
                }
            }
        }
    }

    class AIMoveRandom extends EntityAIBase {
        public AIMoveRandom() {
            this.setMutexBits(1);
        }

        public boolean shouldExecute() {
            return !EntityPixie.this.getMoveHelper().isUpdating() && EntityPixie.this.rand.nextInt(4) == 0;
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void updateTask() {
            BlockPos blockpos = EntityPixie.this.getHousePos() == null ? EntityPixie.this.getPosition() : EntityPixie.this.getHousePos();

            if (blockpos == null) {
                blockpos = new BlockPos(EntityPixie.this);
            }

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = EntityPixie.getPositionRelativetoGround(EntityPixie.this, EntityPixie.this.world, EntityPixie.this.posX + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.posZ + EntityPixie.this.rand.nextInt(15) - 7, EntityPixie.this.rand);

                if (EntityPixie.this.world.isAirBlock(blockpos1)) {
                    EntityPixie.this.moveHelper.setMoveTo((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 0.25D);

                    if (EntityPixie.this.getAttackTarget() == null) {
                        EntityPixie.this.getLookHelper().setLookPosition((double) blockpos1.getX() + 0.5D, (double) blockpos1.getY() + 0.5D, (double) blockpos1.getZ() + 0.5D, 180.0F, 20.0F);

                    }
                }
            }
        }
    }
}
