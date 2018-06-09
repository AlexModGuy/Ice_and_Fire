package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.entity.ai.CyclopsAIAttackMelee;
import com.github.alexthe666.iceandfire.entity.ai.CyclopsAITargetSheepPlayers;
import com.github.alexthe666.iceandfire.entity.ai.TrollAIFleeSun;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.event.EventLiving;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityTroll extends EntityMob implements IAnimatedEntity {

    private int animationTick;
    private Animation currentAnimation;
    private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityTroll.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> WEAPON = EntityDataManager.<Integer>createKey(EntityTroll.class, DataSerializers.VARINT);
    public static Animation ANIMATION_STRIKE_HORIZONTAL = Animation.create(20);
    public static Animation ANIMATION_STRIKE_VERTICAL = Animation.create(20);
    public float stoneProgress;

    public EntityTroll(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 3.5F);
    }

    protected void initEntityAI() {
        this.tasks.addTask(1, new TrollAIFleeSun(this, 1.0D));
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, true));
        this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F, 1.0F));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
        ((PathNavigateGround) this.getNavigator()).setAvoidSun(true);
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(IceAndFire.CONFIG.trollAttackStrength);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(IceAndFire.CONFIG.trollMaxHealth);
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1D);

    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getRNG().nextBoolean()) {
            this.setAnimation(ANIMATION_STRIKE_VERTICAL);

        } else {
            this.setAnimation(ANIMATION_STRIKE_HORIZONTAL);
        }
        return true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(WEAPON, 0);
    }

    private int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    public void setType(EnumTroll variant) {
        this.setVariant(variant.ordinal());
    }

    public EnumTroll getType() {
        return EnumTroll.values()[getVariant()];
    }

    private int getWeapon() {
        return this.dataManager.get(WEAPON);
    }

    private void setWeapon(int variant) {
        this.dataManager.set(WEAPON, variant);
    }

    public void setWeaponType(EnumTroll.Weapon variant) {
        this.setWeapon(variant.ordinal());
    }

    public EnumTroll.Weapon getWeaponType() {
        return EnumTroll.Weapon.values()[getWeapon()];
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setInteger("Weapon", this.getWeapon());
        compound.setFloat("StoneProgress", stoneProgress);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        this.setWeapon(compound.getInteger("Weapon"));
        this.stoneProgress = compound.getFloat("StoneProgress");
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setType(EnumTroll.getBiomeType(world.getBiome(this.getPosition())));
        this.setWeaponType(EnumTroll.getWeaponForType(this.getType()));
        return livingdata;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        boolean stone = EntityGorgon.isStoneMob(this);
        if (stone && stoneProgress < 20.0F) {
            stoneProgress += 2F;
            System.out.println(world.isRemote);
        } else if (!stone && stoneProgress > 0.0F) {
            stoneProgress -= 2F;
        }
        if (!stone && this.getHealth() < this.getMaxHealth() && this.ticksExisted % 30 == 0) {
            this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 30, 1));
        }
        if (this.world.isDaytime() && !this.world.isRemote) {
            float f = this.getBrightness();
            BlockPos blockpos = this.getRidingEntity() instanceof EntityBoat ? (new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ)).up() : new BlockPos(this.posX, (double) Math.round(this.posY), this.posZ);
            if (f > 0.5F && this.world.canSeeSky(blockpos)) {
                StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(this, StoneEntityProperties.class);
                if (properties != null && !properties.isStone) {
                    properties.isStone = true;
                    this.motionX = 0;
                    this.motionY = 0;
                    this.motionZ = 0;
                    this.setAnimation(NO_ANIMATION);
                    this.playSound(ModSounds.GORGON_TURN_STONE, 1, 1);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
            float weaponX = (float) (posX + 1.9F * Math.cos((renderYawOffset + 90) * Math.PI / 180));
            float weaponZ = (float) (posZ + 1.9F * Math.sin((renderYawOffset + 90) * Math.PI / 180));
            float weaponY = (float) (posY + (0.2F));
            IBlockState state = world.getBlockState(new BlockPos(weaponX, weaponY - 1, weaponZ));
            for (int i = 0; i < 20; i++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                if (state.getMaterial().isSolid() && world.isRemote) {
                    this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, weaponX + (this.getRNG().nextFloat() - 0.5F), weaponY + (this.getRNG().nextFloat() - 0.5F), weaponZ + (this.getRNG().nextFloat() - 0.5F), motionX, motionY, motionZ, new int[]{Block.getIdFromBlock(state.getBlock())});
                }
            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) < 4D && this.getAnimationTick() == 10) {
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        }
        if (this.getAnimation() == ANIMATION_STRIKE_HORIZONTAL && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) < 4D && this.getAnimationTick() == 10) {
            this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
            float f1 = 0.5F;
            float f2 = this.getAttackTarget().moveForward;
            float f3 = 0.6F;
            float f4 = MathHelper.sqrt(f2 * f2 + f3 * f3);

            if (f4 < 1.0F) {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 = f2 * f4;
            f3 = f3 * f4;
            float f5 = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float f6 = MathHelper.cos(this.rotationYaw * 0.017453292F);
            float f7 = f2 * f6 - f3 * f5;
            float f8 = f3 * f6 + f2 * f5;
            this.getAttackTarget().motionX = f5;
            this.getAttackTarget().motionZ = f6;
            this.getAttackTarget().motionY = 0.4F;
        }
        if (this.getNavigator().noPath() && this.getAttackTarget() != null && this.getDistanceSq(this.getAttackTarget()) > 3 && this.getDistanceSq(this.getAttackTarget()) < 30 && this.world.getGameRules().getBoolean("mobGriefing")) {
            this.faceEntity(this.getAttackTarget(), 30, 30);
            if (this.getAnimation() == NO_ANIMATION && this.rand.nextInt(15) == 0) {
                this.setAnimation(ANIMATION_STRIKE_VERTICAL);
            }
            if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
                float weaponX = (float) (posX + 1.9F * Math.cos((renderYawOffset + 90) * Math.PI / 180));
                float weaponZ = (float) (posZ + 1.9F * Math.sin((renderYawOffset + 90) * Math.PI / 180));
                float weaponY = (float) (posY + (this.getEyeHeight() / 2));
                IBlockState state = world.getBlockState(new BlockPos(weaponX, weaponY, weaponZ));
                TrollExplosion explosion = new TrollExplosion(world, this, weaponX, weaponY, weaponZ, 1F + this.getRNG().nextFloat());
                explosion.doExplosionA();
                explosion.doExplosionB(true);
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1, 1);

            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 2.5F, 0.5F);

        }
        if (this.getAnimation() == ANIMATION_STRIKE_HORIZONTAL && this.getAnimationTick() == 10) {
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 2.5F, 0.5F);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
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
        return new Animation[]{NO_ANIMATION, ANIMATION_STRIKE_HORIZONTAL, ANIMATION_STRIKE_VERTICAL};
    }
}
