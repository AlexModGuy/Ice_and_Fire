package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.MyrmexTrades;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemMyrmexEgg;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityMyrmexWorker extends EntityMyrmexBase {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_STING = Animation.create(15);
    public static final ResourceLocation DESERT_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_worker_desert");
    public static final ResourceLocation JUNGLE_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_worker_jungle");
    private static final ResourceLocation TEXTURE_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_worker.png");
    private static final ResourceLocation TEXTURE_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_worker.png");
    public boolean keepSearching = true;

    public EntityMyrmexWorker(EntityType<EntityMyrmexWorker> t, World worldIn) {
        super(t, worldIn);
    }

    @Override
    @Nullable
    protected ResourceLocation getDefaultLootTable() {
        return isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    public void die(DamageSource cause) {
        if (!this.level.isClientSide && !this.getItemInHand(Hand.MAIN_HAND).isEmpty()) {
            this.spawnAtLocation(this.getItemInHand(Hand.MAIN_HAND), 0);
            this.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
        }
        super.die(cause);
    }

    @Override
    protected int getExperienceReward(PlayerEntity player) {
        return 3;
    }

    @Override
    public boolean isSmallerThanBlock() {
        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        /*if (this.getAnimation() == ANIMATION_BITE && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            this.playBiteSound();
            if (this.getAttackBounds().intersects(this.getAttackTarget().getBoundingBox())) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && this.getAnimationTick() == 0) {
            this.playStingSound();
        }
        if (this.getAnimation() == ANIMATION_STING && this.getAttackTarget() != null && this.getAnimationTick() == 6) {
            if (this.getAttackBounds().intersects(this.getAttackTarget().getBoundingBox())) {
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * 2));
                this.getAttackTarget().addPotionEffect(new EffectInstance(Effects.POISON, 60, 1));
            }
        }*/
        if (!this.getItemInHand(Hand.MAIN_HAND).isEmpty()) {
            if (this.getItemInHand(Hand.MAIN_HAND).getItem() instanceof ItemMyrmexEgg) {
                boolean isJungle = this.getItemInHand(Hand.MAIN_HAND).getItem() == IafItemRegistry.MYRMEX_JUNGLE_EGG;
                CompoundNBT tag = this.getItemInHand(Hand.MAIN_HAND).getTag();
                int metadata = 0;
                if (tag != null) {
                    metadata = tag.getInt("EggOrdinal");
                }
                EntityMyrmexEgg egg = new EntityMyrmexEgg(IafEntityRegistry.MYRMEX_EGG.get(), level);
                egg.copyPosition(this);
                egg.setJungle(isJungle);
                egg.setMyrmexCaste(metadata);
                if (!level.isClientSide) {
                    level.addFreshEntity(egg);
                }
                egg.startRiding(this);
                this.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            }
        }
        if (!this.getPassengers().isEmpty()) {
            for (Entity entity : this.getPassengers()) {
                if (entity instanceof EntityMyrmexBase && ((EntityMyrmexBase) entity).getGrowthStage() >= 2) {
                    entity.stopRiding();
                }
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(0, new MyrmexAITradePlayer(this));
        this.goalSelector.addGoal(0, new MyrmexAILookAtTradePlayer(this));
        this.goalSelector.addGoal(1, new MyrmexAIAttackMelee(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MyrmexAIStoreBabies(this, 1.0D));
        this.goalSelector.addGoal(3, new MyrmexAIStoreItems(this, 1.0D));
        this.goalSelector.addGoal(4, new MyrmexAIReEnterHive(this, 1.0D));
        this.goalSelector.addGoal(4, new MyrmexAILeaveHive(this, 1.0D));
        this.goalSelector.addGoal(6, new MyrmexAIForage(this,2));
        this.goalSelector.addGoal(7, new MyrmexAIMoveThroughHive(this, 1.0D));
        this.goalSelector.addGoal(8, new MyrmexAIWander(this, 1D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(10, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new MyrmexAIDefendHive(this));
        this.targetSelector.addGoal(2, new MyrmexAIForageForItems(this));
        this.targetSelector.addGoal(3, new MyrmexAIPickupBabies(this));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new MyrmexAIAttackPlayers(this));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity entity) {
                return EntityMyrmexWorker.this.getMainHandItem().isEmpty() && entity != null && !EntityMyrmexBase.haveSameHive(EntityMyrmexWorker.this, entity) && DragonUtils.isAlive(entity) && !(entity instanceof IMob);
            }
        }));


    }

    @Override
    public boolean shouldWander() {
        return super.shouldWander() && this.canSeeSky();
    }

    @Override
    protected VillagerTrades.ITrade[] getLevel1Trades() {
        return isJungle() ? MyrmexTrades.JUNGLE_WORKER.get(1) : MyrmexTrades.DESERT_WORKER.get(1);
    }

    @Override
    protected VillagerTrades.ITrade[] getLevel2Trades() {
        return isJungle() ? MyrmexTrades.JUNGLE_WORKER.get(2) : MyrmexTrades.DESERT_WORKER.get(2);
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 20)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.3D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, IafConfig.myrmexBaseAttackStrength)
            //FOLLOW RANGE
            .add(Attributes.FOLLOW_RANGE, 32D)
            //ARMOR
            .add(Attributes.ARMOR, 4D);
    }

    @Override
    public AttributeModifierMap.MutableAttribute getConfigurableAttributes() {
        return bakeAttributes();
    }

    @Override
    public ResourceLocation getAdultTexture() {
        return isJungle() ? TEXTURE_JUNGLE : TEXTURE_DESERT;
    }

    @Override
    public float getModelScale() {
        return 0.6F;
    }

    @Override
    public boolean shouldLeaveHive() {
        return !holdingSomething();
    }

    @Override
    public boolean shouldEnterHive() {
        return holdingSomething() || (!level.isDay() && !IafConfig.myrmexHiveIgnoreDaytime);
    }

    @Override
    public boolean shouldMoveThroughHive() {
        return !shouldLeaveHive() && !holdingSomething();
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getGrowthStage() < 2) {
            return false;
        }

        if (this.getAnimation() != ANIMATION_STING && this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_STING : ANIMATION_BITE);
            float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
            this.setLastHurtMob(entityIn);
            boolean flag = entityIn.hurt(DamageSource.mobAttack(this), f);
            if (this.getAnimation() == ANIMATION_STING && flag) {
                this.playStingSound();
                if (entityIn instanceof LivingEntity) {
                    ((LivingEntity) entityIn).addEffect(new EffectInstance(Effects.POISON, 200, 2));
                    this.setTarget((LivingEntity) entityIn);
                }
            } else {
                this.playBiteSound();
            }
            if (!this.level.isClientSide && this.getRandom().nextInt(3) == 0 && this.getItemInHand(Hand.MAIN_HAND) != ItemStack.EMPTY) {
                this.spawnAtLocation(this.getItemInHand(Hand.MAIN_HAND), 0);
                this.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
            }
            if (!this.getPassengers().isEmpty()) {
                for (Entity entity : this.getPassengers()) {
                    entity.stopRiding();
                }
            }
            return true;
        }
        return false;
    }


    public boolean holdingSomething() {
        return this.getHeldEntity() != null || !this.getItemInHand(Hand.MAIN_HAND).isEmpty() || this.getTarget() != null;
    }

    public boolean holdingBaby() {
        return this.getHeldEntity() != null && (this.getHeldEntity() instanceof EntityMyrmexBase || this.getHeldEntity() instanceof EntityMyrmexEgg);
    }

    @Override
    public int getCasteImportance() {
        return 0;
    }
    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING};
    }

    @Override
    public void positionRider(Entity passenger) {
        super.positionRider(passenger);
        if (this.hasPassenger(passenger)) {
            yBodyRot = yRot;
            float radius = 1.05F;
            float angle = (0.01745329251F * this.yBodyRot);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            passenger.setPos(this.getX() + extraX, this.getY() + 0.25F, this.getZ() + extraZ);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (amount >= 1.0D && !this.level.isClientSide && this.getRandom().nextInt(3) == 0 && this.getItemInHand(Hand.MAIN_HAND) != ItemStack.EMPTY) {
            this.spawnAtLocation(this.getItemInHand(Hand.MAIN_HAND), 0);
            this.setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
        }
        if (amount >= 1.0D && !this.getPassengers().isEmpty()) {
            for (Entity entity : this.getPassengers()) {
                entity.stopRiding();
            }
        }
        return super.hurt(source, amount);
    }

    public Entity getHeldEntity() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    public void onPickupItem(ItemEntity itemEntity) {
        Item item = itemEntity.getItem().getItem();
        if (item == IafItemRegistry.MYRMEX_JUNGLE_RESIN && this.isJungle() || item == IafItemRegistry.MYRMEX_DESERT_RESIN && !this.isJungle()) {

            PlayerEntity owner = null;
            try {
                if (itemEntity.getThrower() != null) {
                    owner = this.level.getPlayerByUUID(itemEntity.getThrower());
                }
            } catch (Exception e) {
                IceAndFire.LOGGER.warn("Myrmex picked up resin that wasn't thrown!");
            }
            if (owner != null && this.getHive() != null) {
                this.getHive().modifyPlayerReputation(owner.getUUID(), 5);
                this.playSound(SoundEvents.SLIME_SQUISH, 1, 1);
                if (!level.isClientSide) {
                    level.addFreshEntity(new ExperienceOrbEntity(level, owner.getX(), owner.getY(), owner.getZ(), 1 + random.nextInt(3)));
                }
            }
        }
    }

    @Override
    public int getVillagerXp() {
        return 0;
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }
}
