package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.event.EventLiving;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityStymphalianBird extends EntityCreature implements IAnimatedEntity {

    private static final int FLIGHT_CHANCE_PER_TICK = 600;
    private int animationTick;
    private Animation currentAnimation;
    private static final DataParameter<Integer> VICTOR_ENTITY = EntityDataManager.<Integer>createKey(EntityStymphalianBird.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> FLYING = EntityDataManager.<Boolean>createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
    private EntityLivingBase victorEntity;
    private boolean isFlying;
    public float flyProgress;
    public BlockPos airTarget;
    private int flyTicks;

    public EntityStymphalianBird(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 1.5F);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new StymphalianBirdAIFlee(this, EntityLivingBase.class, 10, new Predicate<EntityLivingBase>() {
            @Override
            public boolean apply(@Nullable EntityLivingBase entity) {
                return EntityStymphalianBird.this.hasVictorEntity() && EntityStymphalianBird.this.getVictorEntity().getEntityId() == entity.getEntityId();
            }
        })); this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.5D, false));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new StymphalianBirdAIAirTarget(this));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityLivingBase.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new StymphalianBirdAITarget(this, EntityLivingBase.class,  true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(Math.min(2048, IceAndFire.CONFIG.stymphalianBirdTargetSearchLength));
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0D);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VICTOR_ENTITY, Integer.valueOf(0));
        this.dataManager.register(FLYING, false);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("VictorEntity", this.dataManager.get(VICTOR_ENTITY).intValue());
        tag.setBoolean("Flying", this.isFlying());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setVictorEntity(tag.getInteger("VictorEntity"));
        this.setFlying(tag.getBoolean("Flying"));
    }

    public boolean isFlying() {
        if (world.isRemote) {
            return this.isFlying = this.dataManager.get(FLYING);
        }
        return isFlying;
    }

    public void setFlying(boolean flying) {
        this.dataManager.set(FLYING, flying);
        if (!world.isRemote) {
            this.isFlying = flying;
        }
    }

    public void setVictorEntity(int entityId) {
        this.dataManager.set(VICTOR_ENTITY, Integer.valueOf(entityId));
    }

    public boolean hasVictorEntity() {
        return ((Integer) this.dataManager.get(VICTOR_ENTITY)).intValue() != 0;
    }

    @Nullable
    public Entity getVictorEntity() {
        if (!this.hasVictorEntity()) {
            return null;
        } else if (this.world.isRemote) {
            if (this.victorEntity != null) {
                return this.victorEntity;
            } else {
                Entity entity = this.world.getEntityByID(((Integer) this.dataManager.get(VICTOR_ENTITY)).intValue());
                if (entity instanceof EntityLivingBase) {
                    this.victorEntity = (EntityLivingBase) entity;
                    return this.victorEntity;
                } else {
                    return null;
                }
            }
        } else {
            return this.world.getEntityByID(((Integer) this.dataManager.get(VICTOR_ENTITY)).intValue());
        }
    }

    public boolean isTargetBlocked(Vec3d target) {
        if (target != null) {
            RayTraceResult rayTrace = world.rayTraceBlocks(new Vec3d(this.getPosition()), target, false);
            if (rayTrace != null && rayTrace.hitVec != null) {
                BlockPos pos = new BlockPos(rayTrace.hitVec);
                if (!world.isAirBlock(pos)) {
                    return true;
                }
                return rayTrace != null && rayTrace.typeOfHit != RayTraceResult.Type.BLOCK ;
            }
        }
        return false;
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entityIn instanceof EntityLivingBase) {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase) entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag) {
            if (i > 0 && entityIn instanceof EntityLivingBase) {
                ((EntityLivingBase) entityIn).knockBack(this, (float) i * 0.5F, (double) MathHelper.sin(this.rotationYaw * 0.017453292F), (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0) {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer)) {
                    float f1 = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1) {
                        entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
                        this.world.setEntityState(entityplayer, (byte) 30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }


    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
        boolean flying = this.isFlying() || !this.onGround && this.airTarget != null;
        if (flying && flyProgress < 20.0F) {
            flyProgress += 0.5F;
        } else if (!flying && flyProgress > 0.0F) {
            flyProgress -= 0.5F;
        }
        if (!this.onGround && this.airTarget != null && !world.isRemote) {
            this.setFlying(true);
        }
        if (!this.isFlying() && this.airTarget != null && this.onGround && !world.isRemote) {
            this.airTarget = null;
        }
        if (this.isFlying() && getAttackTarget() == null) {
            flyAround();
        } else if (getAttackTarget() != null) {
            flyTowardsTarget();
        }
        if (this.onGround && flyTicks != 0) {
            flyTicks = 0;
        }
        if (this.isFlying() && this.doesWantToLand() && !world.isRemote) {
            this.setFlying(false);
            if (this.onGround) {
                flyTicks = 0;
                this.setFlying(false);
            }
        }
        if (this.isFlying() && !world.isRemote) {
            this.flyTicks++;
        }
        if ((properties == null || properties != null && !properties.isStone) && !world.isRemote && this.getRNG().nextInt(FLIGHT_CHANCE_PER_TICK) == 0 && !this.isFlying() && this.getPassengers().isEmpty() && !this.isChild() && this.onGround) {
            this.setFlying(true);
            this.flyTicks = 0;
        }
        if(!this.world.isRemote && this.getAttackTarget() != null){
            if(this.world.isAirBlock(new BlockPos(this).up()) && this.world.isAirBlock(new BlockPos(this).up(2))){
                this.setFlying(true);
                this.flyTicks = 0;
            }else{
                this.setFlying(false);
                this.flyTicks = 0;
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    public void flyAround() {
        if (airTarget != null && this.isFlying()) {
            if (!isTargetInAir() || flyTicks > 6000 || !this.isFlying()) {
                airTarget = null;
            }
            flyTowardsTarget();
        }
    }

    public void flyTowardsTarget() {
        if (airTarget != null && isTargetInAir() && this.isFlying() && this.getDistanceSquared(new Vec3d(airTarget.getX(), this.posY, airTarget.getZ())) > 3) {
            double targetX = airTarget.getX() + 0.5D - posX;
            double targetY = Math.min(airTarget.getY(), 256) + 1D - posY;
            double targetZ = airTarget.getZ() + 0.5D - posZ;
            motionX += (Math.signum(targetX) * 0.5D - motionX) * 0.100000000372529 * 2;
            motionY += (Math.signum(targetY) * 0.5D - motionY) * 0.100000000372529 * 2;
            motionZ += (Math.signum(targetZ) * 0.5D - motionZ) * 0.100000000372529 * 2;
            float angle = (float) (Math.atan2(motionZ, motionX) * 180.0D / Math.PI) - 90.0F;
            float rotation = MathHelper.wrapDegrees(angle - rotationYaw);
            moveForward = 0.5F;
            prevRotationYaw = rotationYaw;
            rotationYaw += rotation;
            if (!this.isFlying()) {
                this.setFlying(true);
            }
        } else {
            this.airTarget = null;
        }
        if (airTarget != null && isTargetInAir() && this.isFlying() && this.getDistanceSquared(new Vec3d(airTarget.getX(), this.posY, airTarget.getZ())) < 3 && this.doesWantToLand()) {
            this.setFlying(false);
        }
    }

    public float getDistanceSquared(Vec3d vec3d) {
        float f = (float) (this.posX - vec3d.x);
        float f1 = (float) (this.posY - vec3d.y);
        float f2 = (float) (this.posZ - vec3d.z);
        return f * f + f1 * f1 + f2 * f2;
    }

    protected boolean isTargetInAir() {
        return airTarget != null && ((world.getBlockState(airTarget).getMaterial() == Material.AIR) || world.getBlockState(airTarget).getMaterial() == Material.AIR);
    }

    public boolean doesWantToLand() {
        return this.flyTicks > 6000 || flyTicks > 40 && this.flyProgress == 0;
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[0];
    }
}
