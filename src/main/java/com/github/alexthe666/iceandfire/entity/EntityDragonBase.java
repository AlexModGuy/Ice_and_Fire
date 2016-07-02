package com.github.alexthe666.iceandfire.entity;

import java.util.Random;

import javax.annotation.Nullable;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.message.MessageDaytime;

import fossilsarcheology.api.EnumDiet;
import fossilsarcheology.api.FoodMappings;

public abstract class EntityDragonBase extends EntityTameable implements IAnimatedEntity {

	public double minimumDamage;
	public double maximumDamage;
	public double minimumHealth;
	public double maximumHealth;
	public double minimumSpeed;
	public double maximumSpeed;
	public EnumDiet diet;
	private boolean isSleeping;
	public float sleepProgress;
	private boolean isSitting;
	private boolean isBreathingFire;
	public float fireBreathProgress;
	private int fireTicks;
	public float flyProgress = 20;
	private static final DataParameter<Integer> HUNGER = EntityDataManager.<Integer> createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> AGE_TICKS = EntityDataManager.<Integer> createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> GENDER = EntityDataManager.<Boolean> createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer> createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> SLEEPING = EntityDataManager.<Boolean> createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> FIREBREATHING = EntityDataManager.<Boolean> createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
	private int animationTick;
	private Animation currentAnimation;
	protected float minimumSize;
	protected float maximumSize;
	public boolean isDaytime;
	public static Animation ANIMATION_EAT;
	public static Animation ANIMATION_SPEAK;
	public static Animation ANIMATION_BITE;
	public static Animation ANIMATION_SHAKEPREY;
	public boolean attackDecision;
	public int animationCycle;

	public EntityDragonBase(World world, EnumDiet diet, double minimumDamage, double maximumDamage, double minimumHealth, double maximumHealth, double minimumSpeed, double maximumSpeed) {
		super(world);
		this.diet = diet;
		this.minimumDamage = minimumDamage;
		this.maximumDamage = maximumDamage;
		this.minimumHealth = minimumHealth;
		this.maximumHealth = maximumHealth;
		this.minimumSpeed = minimumSpeed;
		this.maximumSpeed = maximumSpeed;
		ANIMATION_EAT = Animation.create(20);
		updateAttributes();
	}

	@Override
	public boolean isAIDisabled() {
		return false;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(HUNGER, Integer.valueOf(0));
		this.dataManager.register(AGE_TICKS, Integer.valueOf(0));
		this.dataManager.register(GENDER, Boolean.valueOf(false));
		this.dataManager.register(VARIANT, Integer.valueOf(0));
		this.dataManager.register(SLEEPING, Boolean.valueOf(false));
		this.dataManager.register(FIREBREATHING, Boolean.valueOf(false));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("Hunger", this.getHunger());
		compound.setInteger("AgeTicks", this.getAgeInTicks());
		compound.setBoolean("Gender", this.isMale());
		compound.setInteger("Variant", this.getVariant());
		compound.setBoolean("Sleeping", this.isSleeping());
		compound.setBoolean("FireBreathing", this.isBreathingFire());
		compound.setBoolean("AttackDecision", attackDecision);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setHunger(compound.getInteger("Hunger"));
		this.setAgeInTicks(compound.getInteger("AgeTicks"));
		this.setGender(compound.getBoolean("Gender"));
		this.setVariant(compound.getInteger("Variant"));
		this.setSleeping(compound.getBoolean("Sleeping"));
		this.setBreathingFire(compound.getBoolean("FireBreathing"));
		this.attackDecision = compound.getBoolean("AttackDecision");
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D);
	}

	private void updateAttributes() {
		double healthStep = (maximumHealth - minimumHealth) / (125);
		double attackStep = (maximumDamage - minimumDamage) / (125);
		double speedStep = (maximumSpeed - minimumSpeed) / (125);
		if (this.getAgeInDays() <= 125) {
			this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Math.round(minimumHealth + (healthStep * this.getAgeInDays())));
			this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(Math.round(minimumDamage + (attackStep * this.getAgeInDays())));
			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(minimumSpeed + (speedStep * this.getAgeInDays()));
		}
	}

	public int getHunger() {
		return this.dataManager.get(HUNGER).intValue();
	}

	public void setHunger(int hunger) {
		this.dataManager.set(HUNGER, Integer.valueOf(Math.min(100, hunger)));
	}

	public int getVariant() {
		return this.dataManager.get(VARIANT).intValue();
	}

	public void setVariant(int variant) {
		this.dataManager.set(VARIANT, Integer.valueOf(variant));
	}

	public int getAgeInDays() {
		return this.dataManager.get(AGE_TICKS).intValue() / 24000;
	}

	public void setAgeInDays(int age) {
		this.dataManager.set(AGE_TICKS, Integer.valueOf(age * 24000));
	}

	public int getAgeInTicks() {
		return this.dataManager.get(AGE_TICKS).intValue();
	}

	public void setAgeInTicks(int age) {
		this.dataManager.set(AGE_TICKS, Integer.valueOf(age));
	}

	public boolean isMale() {
		return this.dataManager.get(GENDER).booleanValue();
	}

	public void setGender(boolean male) {
		this.dataManager.set(GENDER, Boolean.valueOf(male));
	}

	public void setSleeping(boolean sleeping) {
		this.dataManager.set(SLEEPING, Boolean.valueOf(sleeping));
		if (!worldObj.isRemote) {
			this.isSleeping = sleeping;
		}
	}

	public boolean isSleeping() {
		if (worldObj.isRemote) {
			boolean isSleeping = this.dataManager.get(SLEEPING).booleanValue();
			this.isSleeping = isSleeping;
			return isSleeping;
		}
		return isSleeping;
	}

	public void setBreathingFire(boolean breathing) {
		this.dataManager.set(FIREBREATHING, Boolean.valueOf(breathing));
		if (!worldObj.isRemote) {
			this.isBreathingFire = breathing;
		}
	}

	public boolean isBreathingFire() {
		if (worldObj.isRemote) {
			boolean breathing = this.dataManager.get(FIREBREATHING).booleanValue();
			this.isBreathingFire = breathing;
			return breathing;
		}
		return isBreathingFire;
	}

	protected boolean canFitPassenger(Entity passenger) {
		return this.getPassengers().size() < 2;
	}

	@Override
	public boolean isSitting() {
		if (worldObj.isRemote) {
			boolean isSitting = (this.dataManager.get(TAMED).byteValue() & 1) != 0;
			this.isSitting = isSitting;
			return isSitting;
		}
		return isSitting;
	}

	@Override
	public void setSitting(boolean sitting) {
		super.setSitting(sitting);
		if (!worldObj.isRemote) {
			this.isSitting = sitting;
		}
	}

	public boolean canMove() {
		return !this.isSitting() && !this.isSleeping();
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() != null) {
				int itemFoodAmount = FoodMappings.instance().getItemFoodAmount(stack.getItem(), diet);
				if (itemFoodAmount > 0 && this.getHunger() < 100) {
					this.growDragon(1);
					this.setHunger(this.getHunger() + itemFoodAmount);
					this.setHealth(Math.min(this.getMaxHealth(), (int) (this.getHealth() + (itemFoodAmount / 10))));
					this.playSound(SoundEvents.ENTITY_GENERIC_EAT, this.getSoundVolume(), this.getSoundPitch());
					this.spawnItemCrackParticles(stack.getItem());
					this.eatFoodBonus(stack);
					if (!player.isCreative()) {
						stack.stackSize--;
					}
					return true;
				}
			}
		}
		return super.processInteract(player, hand, stack);
	}

	public void eatFoodBonus(ItemStack stack) {

	}

	public void growDragon(int ageInDays) {
		this.setAgeInDays(this.getAgeInDays() + ageInDays);
		this.setScaleForAge(false);
		this.updateAttributes();
	}

	public void spawnItemCrackParticles(Item item) {
		double motionX = getRNG().nextGaussian() * 0.07D;
		double motionY = getRNG().nextGaussian() * 0.07D;
		double motionZ = getRNG().nextGaussian() * 0.07D;
		float f = (float) (getRNG().nextFloat() * (this.getEntityBoundingBox().maxX - this.getEntityBoundingBox().minX) + this.getEntityBoundingBox().minX);
		float f1 = (float) (getRNG().nextFloat() * (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) + this.getEntityBoundingBox().minY);
		float f2 = (float) (getRNG().nextFloat() * (this.getEntityBoundingBox().maxZ - this.getEntityBoundingBox().minZ) + this.getEntityBoundingBox().minZ);
		this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, f, f1, f2, motionX, motionY, motionZ, new int[] { Item.getIdFromItem(item) });
	}

	public boolean isDaytime() {
		if (!this.firstUpdate && this.worldObj != null) {
			if (worldObj.isRemote) {
				return isDaytime;
			} else {
				IceAndFire.NETWORK_WRAPPER.sendToAll(new MessageDaytime(this.getEntityId(), this.worldObj.isDaytime()));
				return this.worldObj.isDaytime();
			}
		} else {
			return true;
		}
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (animationCycle < 5) {
			animationCycle++;
		} else {
			animationCycle = 0;
		}
		boolean sleeping = isSleeping();
		if (sleeping && sleepProgress < 20.0F) {
			sleepProgress += 0.5F;
		} else if (!sleeping && sleepProgress > 0.0F) {
			sleepProgress -= 0.5F;
		}
		boolean fireBreathing = isBreathingFire();
		if (fireBreathing && fireBreathProgress < 20.0F) {
			fireBreathProgress += 0.5F;
		} else if (!fireBreathing && fireBreathProgress > 0.0F) {
			fireBreathProgress -= 0.5F;
		}
		AnimationHandler.INSTANCE.updateAnimations(this);
		this.setAgeInTicks(this.getAgeInTicks() + 1);
		if (this.getAgeInTicks() % 24000 == 0) {
			this.updateAttributes();
			this.setScale(this.getRenderSize());
		}
		if (this.getAgeInTicks() % 1200 == 0) {
			if (this.getHunger() > 0) {
				this.setHunger(this.getHunger() - 1);
			}
		}

		if (this.isBreathingFire()) {
			this.fireTicks++;
			if (fireTicks > (this.isChild() ? 60 : this.isAdult() ? 400 : 180)) {
				this.setBreathingFire(false);
				this.attackDecision = true;
				fireTicks = 0;
			}
		}
	}

	public boolean isActuallyBreathingFire() {
		return this.fireTicks > 20 && this.isBreathingFire();
	}

	public abstract String getVariantName(int variant);

	public abstract String getTexture();

	public abstract String getTextureOverlay();

	public void updatePassenger(Entity passenger) {
		if (this.isPassenger(passenger)) {
			if (passenger instanceof EntityPlayer && this.getAttackTarget() != passenger && this.isOwner((EntityPlayer) passenger)) {
				passenger.setPosition(this.posX, this.posY + this.getMountedYOffset() + passenger.getYOffset(), this.posZ);
			} else {
				this.updatePreyInMouth(passenger);
			}
		}
	}

	private void updatePreyInMouth(Entity prey) {
		if (this.getAnimation() == this.ANIMATION_SHAKEPREY) {
			if (this.getAnimationTick() > 55 && prey != null) {
				prey.attackEntityFrom(DamageSource.causeMobDamage(this), ((EntityLivingBase) prey).getMaxHealth() * 2);
				this.attackDecision = !this.attackDecision;
				this.onKillEntity((EntityLivingBase) prey);
			}
			prey.setPosition(this.posX, this.posY + this.getMountedYOffset() + prey.getYOffset(), this.posZ);
			float modTick_0 = this.getAnimationTick() - 15;
			float modTick_1 = this.getAnimationTick() > 15 ? 6 * MathHelper.sin((float) (Math.PI + (modTick_0 * 0.3F))) : 0;
			float modTick_2 = this.getAnimationTick() > 15 ? 15 : this.getAnimationTick();
			this.rotationYaw *= 0;
			prey.rotationYaw = this.rotationYaw + this.rotationYawHead + 180;
			rotationYaw = renderYawOffset;
			float radius = 0.75F * (0.7F * getRenderSize()) * -3;
			float angle = (0.01745329251F * this.renderYawOffset) + 3.15F + (modTick_1 * 1.75F) * 0.05F;
			double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
			double extraZ = (double) (radius * MathHelper.cos(angle));
			double extraY = 0.8F * (getRenderSize() + (modTick_1 * 0.05) + (modTick_2 * 0.05) - 2);
			prey.setPosition(this.posX + extraX, this.posY + extraY, this.posZ + extraZ);
		} else {
			prey.dismountRidingEntity();
		}
	}

	public int getDragonStage() {
		int age = this.getAgeInDays();
		if (age >= 100) {
			return 5;
		} else if (age >= 75) {
			return 4;
		} else if (age >= 50) {
			return 3;
		} else if (age >= 25) {
			return 2;
		} else {
			return 1;
		}
	}

	public boolean isTeen() {
		return getDragonStage() < 4 && getDragonStage() > 2;
	}

	public boolean isAdult() {
		return getDragonStage() >= 4;
	}

	@Override
	@Nullable
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		this.setGender(this.getRNG().nextBoolean());
		this.setAgeInDays(1);
		this.growDragon(25);
		this.setHunger(50);
		this.setVariant(new Random().nextInt(4));
		this.setSleeping(false);
		this.updateAttributes();
		return livingdata;
	}

	@Override
	public boolean attackEntityFrom(DamageSource dmg, float i) {
		if (i > 0) {
			this.setSitting(false);
			this.setSleeping(false);
		}
		return super.attackEntityFrom(dmg, i);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.getAttackTarget() != null && this.getRidingEntity() == null && this.getAttackTarget().isDead) {
			this.setAttackTarget(null);
		}
		if (!this.isInWater() && !this.isSleeping() && !this.isDaytime() && this.getRNG().nextInt(250) == 0 && this.getAttackTarget() == null && this.getPassengers().isEmpty()) {
			this.setSleeping(true);
		}
		if (this.isSleeping() && (this.isInWater() || this.isDaytime() || this.getAttackTarget() != null && !this.getPassengers().isEmpty())) {
			this.setSleeping(false);
		}
	}

	@Override
	public void setScaleForAge(boolean par1) {
		this.setScale(this.getRenderSize());
	}

	public float getRenderSize() {
		float step = (this.maximumSize - this.minimumSize) / ((125 * 24000));

		if (this.getAgeInTicks() > 125 * 24000) {
			return this.minimumSize + ((step) * 125 * 24000);
		}
		return this.minimumSize + ((step * this.getAgeInTicks()));
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

		if (flag) {
			this.applyEnchantments(this, entityIn);
		}

		return flag;
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

	public void playLivingSound() {
		if (this.getAnimation() == this.NO_ANIMATION) {
			this.setAnimation(ANIMATION_SPEAK);
		}
		super.playLivingSound();
	}

	protected void playHurtSound(DamageSource source) {
		if (this.getAnimation() == this.NO_ANIMATION) {
			this.setAnimation(ANIMATION_SPEAK);
		}
		super.playHurtSound(source);
	}

	@Override
	public Animation[] getAnimations() {
		return new Animation[] { IAnimatedEntity.NO_ANIMATION, EntityDragonBase.ANIMATION_EAT };
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}
}
