package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModSounds;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.google.common.base.Predicate;
import fossilsarcheology.api.EnumDiet;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityIceDragon extends EntityDragonBase {

	public static Animation ANIMATION_TAILWHACK;
	public static Animation ANIMATION_FIRECHARGE;
	public static float[] growth_stage_1 = new float[]{1F, 3F};
	public static float[] growth_stage_2 = new float[]{3F, 7F};
	public static float[] growth_stage_3 = new float[]{7F, 12.5F};
	public static float[] growth_stage_4 = new float[]{12.5F, 20F};
	public static float[] growth_stage_5 = new float[]{20F, 30F};

	public EntityIceDragon(World worldIn) {
		super(worldIn, EnumDiet.PISCCARNIVORE, 1, 18, 20, 500, 0.2F, 0.5F);
		this.setSize(0.78F, 1.2F);
		this.ignoreFrustumCheck = true;
		ANIMATION_SPEAK = Animation.create(45);
		ANIMATION_BITE = Animation.create(35);
		ANIMATION_SHAKEPREY = Animation.create(65);
		ANIMATION_TAILWHACK = Animation.create(40);
		ANIMATION_FIRECHARGE = Animation.create(40);
		this.growth_stages = new float[][]{growth_stage_1, growth_stage_2, growth_stage_3, growth_stage_4, growth_stage_5};
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.aiSit = new EntityAISit(this));
		this.tasks.addTask(3, new DragonAIAttackMelee(this, 1.5D, true));
		this.tasks.addTask(4, new DragonAIAirTarget(this));
		this.tasks.addTask(6, new DragonAIWander(this, 1.0D));
		this.tasks.addTask(7, new DragonAIWatchClosest(this, EntityLivingBase.class, 6.0F));
		this.tasks.addTask(7, new DragonAILookIdle(this));
		this.tasks.addTask(8, new DragonAIBreakBlocks(this));
		this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(4, new DragonAITarget(this, EntityLivingBase.class, false, new Predicate<Entity>() {
			@Override
			public boolean apply(@Nullable Entity entity) {
				return entity instanceof EntityLivingBase;
			}
		}));
		this.targetTasks.addTask(5, new DragonAITargetItems(this, false));
	}

	@Override
	public String getTexture() {
		if (this.isModelDead()) {
			if (this.getDeathStage() >= (this.getAgeInDays() / 5) / 2) {
				return "iceandfire:textures/models/icedragon/skeleton";
			} else {
				return "iceandfire:textures/models/icedragon/" + this.getVariantName(this.getVariant()) + this.getDragonStage() + "_sleep";
			}
		}
		if (this.isSleeping()) {
			return "iceandfire:textures/models/icedragon/" + this.getVariantName(this.getVariant()) + this.getDragonStage() + "_sleep";
		} else {
			return "iceandfire:textures/models/icedragon/" + this.getVariantName(this.getVariant()) + this.getDragonStage() + "";
		}
	}

	public String getVariantName(int variant) {
		switch (variant) {
		default:
			return "blue_";
		case 1:
			return "white_";
		case 2:
			return "sapphire_";
		case 3:
			return "silver_";
		}
	}

	public Item getVariantScale(int variant) {
		switch (variant) {
		default:
			return ModItems.dragonscales_blue;
		case 1:
			return ModItems.dragonscales_white;
		case 2:
			return ModItems.dragonscales_sapphire;
		case 3:
			return ModItems.dragonscales_silver;
		}
	}

	public Item getVariantEgg(int variant) {
		switch (variant) {
		default:
			return ModItems.dragonegg_blue;
		case 1:
			return ModItems.dragonegg_white;
		case 2:
			return ModItems.dragonegg_sapphire;
		case 3:
			return ModItems.dragonegg_silver;
		}
	}

	public boolean canBeSteered() {
		return true;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		switch (this.getRNG().nextInt(3)) {
		case 0:
			if (this.getAnimation() != this.ANIMATION_BITE) {
				this.setAnimation(this.ANIMATION_BITE);
				return false;
			} else if (this.getAnimationTick() > 15 && this.getAnimationTick() < 25) {
				boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
				this.attackDecision = false;
				return flag;
			}
			break;
		case 1:
			if (entityIn.width < this.width * 0.5F) {
				if (this.getAnimation() != this.ANIMATION_SHAKEPREY) {
					this.setAnimation(this.ANIMATION_SHAKEPREY);
					entityIn.startRiding(this);
					return false;
				}
			} else {
				if (this.getAnimation() != this.ANIMATION_BITE) {
					this.setAnimation(this.ANIMATION_BITE);
					return false;
				} else if (this.getAnimationTick() > 15 && this.getAnimationTick() < 25) {
					boolean flag1 = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
					this.attackDecision = false;
					return flag1;
				}
			}
			break;
		case 2:
			if (this.getAnimation() != this.ANIMATION_TAILWHACK) {
				this.setAnimation(this.ANIMATION_TAILWHACK);
				return false;
			} else if (this.getAnimationTick() > 20 && this.getAnimationTick() < 25) {
				boolean flag2 = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));
				if (entityIn instanceof EntityLivingBase) {
					((EntityLivingBase) entityIn).knockBack(entityIn, 1, 1, 1);
				}
				this.attackDecision = false;
				return flag2;
			}
			break;
		}

		return false;
	}

	public void moveEntityTowards(Entity entity, double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt_double(x * x + y * y + z * z);
		x = x / (double) f;
		y = y / (double) f;
		z = z / (double) f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		x = x * (double) velocity;
		y = y * (double) velocity;
		z = z * (double) velocity;
		entity.motionX = x;
		entity.motionY = y;
		entity.motionZ = z;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.getAttackTarget() != null && !this.isSleeping()) {
			if ((!attackDecision || this.isFlying())) {
				shootIceAtMob(this.getAttackTarget());
			} else {
				if (this.getEntityBoundingBox().expand(this.getRenderSize() / 3, this.getRenderSize() / 3, this.getRenderSize() / 3).intersectsWith(this.getAttackTarget().getEntityBoundingBox())) {
					attackEntityAsMob(this.getAttackTarget());
				}

			}
		} else {
			this.setBreathingFire(false);
		}
	}

	public void riderShootFire(Entity controller) {
		if (this.getRNG().nextInt(5) == 0 && !this.isChild()) {
			if (this.getAnimation() != this.ANIMATION_FIRECHARGE) {
				this.setAnimation(this.ANIMATION_FIRECHARGE);
			} else if (this.getAnimationTick() == 15) {
				rotationYaw = renderYawOffset;
				float headPosX = (float) (posX + 1.8F * getRenderSize() * 0.3F * Math.cos((rotationYaw + 90) * Math.PI / 180));
				float headPosZ = (float) (posZ + 1.8F * getRenderSize() * 0.3F * Math.sin((rotationYaw + 90) * Math.PI / 180));
				float headPosY = (float) (posY + 0.5 * getRenderSize() * 0.3F);

				worldObj.playEvent((EntityPlayer) null, 1016, new BlockPos(this), 0);
				EntityDragonIceCharge entitylargefireball = new EntityDragonIceCharge(worldObj, this, 0, 0, 0);
				float f = (float) 32000 / 20.0F;
				f = (f * f + f * 2.0F) / 3.0F;
				if (f > 1.0F) {
					f = 1.0F;
				}
				entitylargefireball.setAim(entitylargefireball, controller, controller.rotationPitch, controller.rotationYaw, 0.0F, f * 3.0F, 1.0F);
				float size = this.isChild() ? 0.4F : this.isAdult() ? 1.3F : 0.8F;
				entitylargefireball.setSizes(size, size);
				entitylargefireball.setPosition(headPosX, headPosY, headPosZ);
				if (!worldObj.isRemote) {
					worldObj.spawnEntityInWorld(entitylargefireball);
				}

			}
		} else {
			if (this.isBreathingFire()) {
				if (this.isActuallyBreathingFire() && this.ticksExisted % 3 == 0) {
					rotationYaw = renderYawOffset;
					float headPosX = (float) (posX + 1.8F * getRenderSize() * 0.3F * Math.cos((rotationYaw + 90) * Math.PI / 180));
					float headPosZ = (float) (posZ + 1.8F * getRenderSize() * 0.3F * Math.sin((rotationYaw + 90) * Math.PI / 180));
					float headPosY = (float) (posY + 0.5 * getRenderSize() * 0.3F);
					EntityDragonIceProjectile entitylargefireball = new EntityDragonIceProjectile(worldObj, this, 0, 0, 0);
					float f = (float) 32000 / 20.0F;
					f = (f * f + f * 2.0F) / 3.0F;
					if (f > 1.0F) {
						f = 1.0F;
					}
					entitylargefireball.setAim(entitylargefireball, controller, controller.rotationPitch, controller.rotationYaw, 0.0F, f * 3.0F, 1.0F);
					worldObj.playEvent((EntityPlayer) null, 1016, new BlockPos(this), 0);
					float size = this.isChild() ? 0.4F : this.isAdult() ? 1.3F : 0.8F;
					entitylargefireball.setPosition(headPosX, headPosY, headPosZ);
					if (!worldObj.isRemote) {
						worldObj.spawnEntityInWorld(entitylargefireball);
					}
				}
			} else {
				this.setBreathingFire(true);
			}
		}
	}

	private void shootIceAtMob(EntityLivingBase entity) {
		if (!this.attackDecision) {
			if (this.getRNG().nextInt(5) == 0) {
				if (this.getAnimation() != this.ANIMATION_FIRECHARGE) {
					this.setAnimation(this.ANIMATION_FIRECHARGE);
				} else if (this.getAnimationTick() == 15) {
					rotationYaw = renderYawOffset;
					float headPosX = (float) (posX + 1.8F * getRenderSize() * 0.3F * Math.cos((rotationYaw + 90) * Math.PI / 180));
					float headPosZ = (float) (posZ + 1.8F * getRenderSize() * 0.3F * Math.sin((rotationYaw + 90) * Math.PI / 180));
					float headPosY = (float) (posY + 0.5 * getRenderSize() * 0.3F);
					double d2 = entity.posX - headPosX;
					double d3 = entity.posY - headPosY;
					double d4 = entity.posZ - headPosZ;
					worldObj.playEvent(null, 1016, new BlockPos(this), 0);
					EntityDragonIceCharge entitylargefireball = new EntityDragonIceCharge(worldObj, this, d2, d3, d4);
					float size = this.isChild() ? 0.4F : this.isAdult() ? 1.3F : 0.8F;
					entitylargefireball.setSizes(size, size);
					entitylargefireball.setPosition(headPosX, headPosY, headPosZ);
					if (!worldObj.isRemote) {
						worldObj.spawnEntityInWorld(entitylargefireball);
					}
					if (entity.isDead || entity == null) {
						this.setBreathingFire(false);
						this.attackDecision = true;
					}
				}
			} else {
				if (this.isBreathingFire()) {
					if (this.isActuallyBreathingFire() && this.ticksExisted % 3 == 0) {
						rotationYaw = renderYawOffset;
						float headPosX = (float) (posX + 1.8F * getRenderSize() * 0.3F * Math.cos((rotationYaw + 90) * Math.PI / 180));
						float headPosZ = (float) (posZ + 1.8F * getRenderSize() * 0.3F * Math.sin((rotationYaw + 90) * Math.PI / 180));
						float headPosY = (float) (posY + 0.5 * getRenderSize() * 0.3F);
						double d2 = entity.posX - headPosX;
						double d3 = entity.posY - headPosY;
						double d4 = entity.posZ - headPosZ;
						worldObj.playEvent(null, 1016, new BlockPos(this), 0);
						EntityDragonIceProjectile entitylargefireball = new EntityDragonIceProjectile(worldObj, this, d2, d3, d4);
						float size = this.isChild() ? 0.4F : this.isAdult() ? 1.3F : 0.8F;
						entitylargefireball.setPosition(headPosX, headPosY, headPosZ);
						if (!worldObj.isRemote && !entity.isDead) {
							worldObj.spawnEntityInWorld(entitylargefireball);
						}
						System.out.println(this.attackDecision);

						entitylargefireball.setSizes(size, size);
						if (entity.isDead || entity == null) {
							this.setBreathingFire(false);
							this.attackDecision = true;
						}
					}
				} else {
					this.setBreathingFire(true);
				}
			}
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isTeen() ? ModSounds.icedragon_teen_idle : this.isAdult() ? ModSounds.icedragon_adult_idle : ModSounds.icedragon_child_idle;
	}

	@Override
	protected SoundEvent getHurtSound() {
		return this.isTeen() ? ModSounds.icedragon_teen_hurt : this.isAdult() ? ModSounds.icedragon_adult_hurt : ModSounds.icedragon_child_hurt;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return this.isTeen() ? ModSounds.icedragon_teen_death : this.isAdult() ? ModSounds.icedragon_adult_death : ModSounds.icedragon_child_death;
	}

	@Override
	public Animation[] getAnimations() {
		return new Animation[] { IAnimatedEntity.NO_ANIMATION, EntityDragonBase.ANIMATION_EAT, EntityDragonBase.ANIMATION_SPEAK, EntityDragonBase.ANIMATION_BITE, EntityDragonBase.ANIMATION_SHAKEPREY, EntityIceDragon.ANIMATION_TAILWHACK, EntityIceDragon.ANIMATION_FIRECHARGE };
	}

	@Override
	public String getTextureOverlay() {
		return this.isSleeping() || this.isModelDead() ? null : "iceandfire:textures/models/icedragon/" + this.getVariantName(this.getVariant()) + this.getDragonStage() + "_eyes";
	}

}
