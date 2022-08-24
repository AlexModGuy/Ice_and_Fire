package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.entity.util.MyrmexTrades;
import com.github.alexthe666.iceandfire.world.gen.WorldGenMyrmexHive;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;

public class EntityMyrmexQueen extends EntityMyrmexBase {

    public static final Animation ANIMATION_BITE = Animation.create(15);
    public static final Animation ANIMATION_STING = Animation.create(15);
    public static final Animation ANIMATION_EGG = Animation.create(20);
    public static final Animation ANIMATION_DIGNEST = Animation.create(45);
    public static final ResourceLocation DESERT_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_queen_desert");
    public static final ResourceLocation JUNGLE_LOOT = new ResourceLocation("iceandfire", "entities/myrmex_queen_jungle");
    private static final ResourceLocation TEXTURE_DESERT = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_desert_queen.png");
    private static final ResourceLocation TEXTURE_JUNGLE = new ResourceLocation("iceandfire:textures/models/myrmex/myrmex_jungle_queen.png");
    private static final DataParameter<Boolean> HASMADEHOME = EntityDataManager.defineId(EntityMyrmexQueen.class, DataSerializers.BOOLEAN);
    private int eggTicks = 0;

    public EntityMyrmexQueen(EntityType<EntityMyrmexQueen> t, World worldIn) {
        super(t, worldIn);
    }

    @Override
    @Nullable
    protected ResourceLocation getDefaultLootTable() {
        return isJungle() ? JUNGLE_LOOT : DESERT_LOOT;
    }

    @Override
    protected int getExperienceReward(PlayerEntity player) {
        return 20;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HASMADEHOME, Boolean.valueOf(true));
    }

    @Override
    protected VillagerTrades.ITrade[] getLevel1Trades() {
        return isJungle() ? MyrmexTrades.JUNGLE_QUEEN.get(1) : MyrmexTrades.DESERT_QUEEN.get(1);
    }

    @Override
    protected VillagerTrades.ITrade[] getLevel2Trades() {
        return isJungle() ? MyrmexTrades.JUNGLE_QUEEN.get(2) : MyrmexTrades.DESERT_QUEEN.get(2);
    }

    @Override
    public void setCustomName(ITextComponent name) {
        if (this.getHive() != null) {
            if (!this.getHive().colonyName.equals(name.getContents())) {
                this.getHive().colonyName = name.getString();
            }
        }
        super.setCustomName(name);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("EggTicks", eggTicks);
        tag.putBoolean("MadeHome", this.hasMadeHome());

    }

    @Override
    public void readAdditionalSaveData(CompoundNBT tag) {
        super.readAdditionalSaveData(tag);
        this.eggTicks = tag.getInt("EggTicks");
        this.setMadeHome(tag.getBoolean("MadeHome"));
    }

    public boolean hasMadeHome() {
        return this.entityData.get(HASMADEHOME).booleanValue();
    }

    public void setMadeHome(boolean madeHome) {
        this.entityData.set(HASMADEHOME, madeHome);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getAnimation() == ANIMATION_DIGNEST) {
            spawnGroundEffects(3);
        }
        if (this.getHive() != null) {
            this.getHive().tick(0, level);
        }

        if (hasMadeHome() && this.getGrowthStage() >= 2 && !this.canSeeSky()) {
            eggTicks++;
        } else if (this.canSeeSky()) {
            this.setAnimation(ANIMATION_DIGNEST);
            if (this.getAnimationTick() == 42) {
                int down = Math.max(15, this.blockPosition().getY() - 20 + this.getRandom().nextInt(10));
                BlockPos genPos = new BlockPos(this.getX(), down, this.getZ());
                if (!MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, genPos.getX(), genPos.getY(), genPos.getZ()))) {
                    WorldGenMyrmexHive hiveGen = new WorldGenMyrmexHive(true, this.isJungle(), NoFeatureConfig.CODEC);
                    if (!level.isClientSide && level instanceof ServerWorld) {
                        hiveGen.placeSmallGen((ServerWorld) level, this.getRandom(), genPos);
                    }
                    this.setMadeHome(true);
                    this.moveTo(genPos.getX(), down, genPos.getZ(), 0, 0);
                    this.addEffect(new EffectInstance(Effects.INVISIBILITY, 30));
                    this.setHive(hiveGen.hive);
                    for (int i = 0; i < 3; i++) {
                        EntityMyrmexWorker worker = new EntityMyrmexWorker(IafEntityRegistry.MYRMEX_WORKER.get(),
                            level);
                        worker.copyPosition(this);
                        worker.setHive(this.getHive());
                        worker.setJungleVariant(this.isJungle());
                        if (!level.isClientSide) {
                            level.addFreshEntity(worker);
                        }
                    }
                    return;
                }
            }
        }
        if (!level.isClientSide && eggTicks > IafConfig.myrmexPregnantTicks && this.getHive() == null || !level.isClientSide && this.getHive() != null && this.getHive().repopulate() && eggTicks > IafConfig.myrmexPregnantTicks) {
            float radius = -5.25F;
            float angle = (0.01745329251F * this.yBodyRot);
            double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
            double extraZ = radius * MathHelper.cos(angle);
            BlockPos eggPos = new BlockPos(this.getX() + extraX, this.getY() + 0.75F, this.getZ() + extraZ);
            if (level.isEmptyBlock(eggPos)) {
                this.setAnimation(ANIMATION_EGG);
                if (this.getAnimationTick() == 10) {
                    EntityMyrmexEgg egg = new EntityMyrmexEgg(IafEntityRegistry.MYRMEX_EGG.get(), this.level);
                    egg.setJungle(this.isJungle());
                    int caste = getRandomCaste(level, this.getRandom(), getHive() == null || getHive().reproduces);
                    egg.setMyrmexCaste(caste);
                    egg.moveTo(this.getX() + extraX, this.getY() + 0.75F, this.getZ() + extraZ, 0, 0);
                    if (getHive() != null) {
                        egg.hiveUUID = this.getHive().hiveUUID;
                    }
                    if (!level.isClientSide) {
                        level.addFreshEntity(egg);
                    }
                    eggTicks = 0;
                }

            }


        }
        if (this.getAnimation() == ANIMATION_BITE && this.getTarget() != null && this.getAnimationTick() == 6) {
            this.playBiteSound();
            if (this.getAttackBounds().intersects(this.getTarget().getBoundingBox())) {
                this.getTarget().hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue()));
            }
        }
        if (this.getAnimation() == ANIMATION_STING && this.getAnimationTick() == 0) {
            this.playStingSound();
        }
        if (this.getAnimation() == ANIMATION_STING && this.getTarget() != null && this.getAnimationTick() == 6) {
            if (this.getAttackBounds().intersects(this.getTarget().getBoundingBox())) {
                LivingEntity attackTarget = this.getTarget();
                this.getTarget().hurt(DamageSource.mobAttack(this), ((int) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue() * 2));
                this.getTarget().addEffect(new EffectInstance(Effects.POISON, 200, 2));
                this.getTarget().hasImpulse = true;
                float f = MathHelper.sqrt(0.5 * 0.5 + 0.5 * 0.5);
                this.getTarget().hasImpulse = true;
                attackTarget.setDeltaMovement(attackTarget.getDeltaMovement().multiply(0.5D, 1, 0.5D));
                attackTarget.setDeltaMovement(attackTarget.getDeltaMovement().add(-0.5 / f * 4, 1, -0.5 / f * 4));

                if (this.getTarget().isOnGround()) {
                    attackTarget.setDeltaMovement(attackTarget.getDeltaMovement().add(0, 0.4, 0));
                }
            }
        }

    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || this.getAnimation() == ANIMATION_DIGNEST;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(0, new MyrmexAITradePlayer(this));
        this.goalSelector.addGoal(0, new MyrmexAILookAtTradePlayer(this));
        this.goalSelector.addGoal(1, new MyrmexAIAttackMelee(this, 1.0D, true));
        this.goalSelector.addGoal(3, new MyrmexAIReEnterHive(this, 1.0D));
        this.goalSelector.addGoal(4, new MyrmexAIWanderHiveCenter(this, 1.0D));
        this.goalSelector.addGoal(5, new MyrmexQueenAIWander(this, 1D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(7, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new MyrmexAIDefendHive(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new MyrmexAIAttackPlayers(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true, new Predicate<LivingEntity>() {
            @Override
            public boolean apply(@Nullable LivingEntity entity) {
                return entity != null && !EntityMyrmexBase.haveSameHive(EntityMyrmexQueen.this, entity) && DragonUtils.isAlive(entity) && !(entity instanceof IMob);
            }
        }));

    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public boolean isInHive() {
        if (getHive() != null) {
            for (BlockPos pos : getHive().getAllRooms()) {
                if (isCloseEnoughToTarget(MyrmexHive.getGroundedPos(getLevel(), pos), 300))
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldMoveThroughHive() {
        return false;
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 120D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.2D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, IafConfig.myrmexBaseAttackStrength * 3.5D)
            //FOLLOW RANGE
            .add(Attributes.FOLLOW_RANGE, 128.0D)
            //ARMOR
            .add(Attributes.ARMOR, 15.0D);
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
        return 1.75F;
    }

    @Override
    public int getCasteImportance() {
        return 3;
    }

    @Override
    public boolean shouldLeaveHive() {
        return false;
    }

    @Override
    public boolean shouldEnterHive() {
        return true;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getGrowthStage() < 2) {
            return false;
        }
        if (this.getAnimation() != ANIMATION_STING && this.getAnimation() != ANIMATION_BITE) {
            this.setAnimation(this.getRandom().nextBoolean() ? ANIMATION_STING : ANIMATION_BITE);
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

    @Override
    public boolean canMove() {
        return super.canMove() && this.hasMadeHome();
    }

    public void spawnGroundEffects(float size) {
        for (int i = 0; i < size * 3; i++) {
            for (int i1 = 0; i1 < 10; i1++) {
                double motionX = getRandom().nextGaussian() * 0.07D;
                double motionY = getRandom().nextGaussian() * 0.07D;
                double motionZ = getRandom().nextGaussian() * 0.07D;
                float radius = size * random.nextFloat();
                float angle = (0.01745329251F * this.yBodyRot) * 3.14F * random.nextFloat();
                double extraX = radius * MathHelper.sin((float) (Math.PI + angle));
                double extraY = 0.8F;
                double extraZ = radius * MathHelper.cos(angle);

                BlockState BlockState = this.level.getBlockState(new BlockPos(MathHelper.floor(this.getX() + extraX), MathHelper.floor(this.getY() + extraY) - 1, MathHelper.floor(this.getZ() + extraZ)));
                if (BlockState.getMaterial() != Material.AIR) {
                    if (level.isClientSide) {
                        level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, BlockState), true, this.getX() + extraX, this.getY() + extraY, this.getZ() + extraZ, motionX, motionY, motionZ);
                    }
                }
            }
        }
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{ANIMATION_PUPA_WIGGLE, ANIMATION_BITE, ANIMATION_STING, ANIMATION_EGG, ANIMATION_DIGNEST};
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
