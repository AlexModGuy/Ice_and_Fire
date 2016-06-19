package com.github.alexthe666.iceandfire.entity;

import javax.annotation.Nullable;

import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventoryChangedListener;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import fossilsarcheology.api.EnumDiet;
import fossilsarcheology.api.FoodMappings;

public abstract class EntityDragonBase extends EntityTameable implements IAnimatedEntity, IRangedAttackMob, IInventoryChangedListener {

	public double minimumDamage;
	public double maximumDamage;
	public double minimumHealth;
	public double maximumHealth;
	public double minimumSpeed;
	public double maximumSpeed;
	public EnumDiet diet;
	private boolean isSleeping;
	private boolean isSitting;
	private static final DataParameter<Integer> HUNGER = EntityDataManager.<Integer> createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> AGE_TICKS = EntityDataManager.<Integer> createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> GENDER = EntityDataManager.<Boolean> createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer> createKey(EntityDragonBase.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> SLEEPING = EntityDataManager.<Boolean> createKey(EntityDragonBase.class, DataSerializers.BOOLEAN);
	private int ageBoost;
	private int animationTick;
	private Animation currentAnimation;
	protected float minimumSize;
	protected float maximumSize;

	public EntityDragonBase(World world, EnumDiet diet, double minimumDamage, double maximumDamage, double minimumHealth, double maximumHealth, double minimumSpeed, double maximumSpeed) {
		super(world);
		this.diet = diet;
		this.minimumDamage = minimumDamage;
		this.maximumDamage = maximumDamage;
		this.minimumHealth = minimumHealth;
		this.maximumHealth = maximumHealth;
		this.minimumSpeed = minimumSpeed;
		this.maximumSpeed = maximumSpeed;
		this.ageBoost = 1;
		updateAttributes();
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(HUNGER, Integer.valueOf(0));
		this.dataManager.register(AGE_TICKS, Integer.valueOf(0));
		this.dataManager.register(GENDER, Boolean.valueOf(false));
		this.dataManager.register(VARIANT, Integer.valueOf(0));
		this.dataManager.register(SLEEPING, Boolean.valueOf(false));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("Hunger", this.getHunger());
		compound.setInteger("AgeTicks", this.getAgeInTicks());
		compound.setBoolean("Gender", this.isMale());
		compound.setInteger("Variant", this.getVariant());
		compound.setBoolean("Sleeping", this.isSleeping());
		compound.setInteger("AgeBoost", this.ageBoost);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.setHunger(compound.getInteger("Hunger"));
		this.setAgeInTicks(compound.getInteger("AgeTicks"));
		this.setGender(compound.getBoolean("Gender"));
		this.setVariant(compound.getInteger("AgeTicks"));
		this.setSleeping(compound.getBoolean("Gender"));
		this.ageBoost = compound.getInteger("AgeBoost");
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896D);
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
					this.setHunger(this.getHunger() + itemFoodAmount);
					this.ageBoost = 24000;
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

	public void spawnItemCrackParticles(Item item) {
		double motionX = getRNG().nextGaussian() * 0.07D;
		double motionY = getRNG().nextGaussian() * 0.07D;
		double motionZ = getRNG().nextGaussian() * 0.07D;
		float f = (float) (getRNG().nextFloat() * (this.getEntityBoundingBox().maxX - this.getEntityBoundingBox().minX) + this.getEntityBoundingBox().minX);
		float f1 = (float) (getRNG().nextFloat() * (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) + this.getEntityBoundingBox().minY);
		float f2 = (float) (getRNG().nextFloat() * (this.getEntityBoundingBox().maxZ - this.getEntityBoundingBox().minZ) + this.getEntityBoundingBox().minZ);
		this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, f, f1, f2, motionX, motionY, motionZ, new int[] { Item.getIdFromItem(item) });
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		AnimationHandler.INSTANCE.updateAnimations(this);
		if (ageBoost > 1) {
			ageBoost -= 1;
		}
		this.setAgeInTicks(this.getAgeInTicks() + ageBoost);
		if (this.getAgeInTicks() % 24000 == 0) {
			this.updateAttributes();
		}
		if (this.getAgeInTicks() % 1200 == 0) {
			if (this.getHunger() > 0) {
				this.setHunger(this.getHunger() - 1);
			}
		}
	}

	public abstract String getVariantName(int variant);

	public abstract String getTexture();

	public abstract String getTextureOverlay();

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
		this.setAgeInDays(100);
		this.setHunger(50);
		this.updateAttributes();
		this.setVariant(this.getRNG().nextInt(4));
		return livingdata;
	}
	
	@Override
	public void onUpdate() {
		this.setScale(getRenderSize());
		super.onUpdate();
		if (this.getAttackTarget() != null && this.getRidingEntity() == null && this.getAttackTarget().isDead) {
			this.setAttackTarget(null);
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
	public void onInventoryChanged(InventoryBasic invBasic) {
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float cooldown) {
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
		return new Animation[] {};
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}

}
