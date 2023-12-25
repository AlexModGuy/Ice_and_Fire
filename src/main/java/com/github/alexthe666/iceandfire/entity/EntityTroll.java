package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.api.event.GenericGriefEvent;
import com.github.alexthe666.iceandfire.entity.ai.TrollAIFleeSun;
import com.github.alexthe666.iceandfire.entity.util.IHasCustomizableAttributes;
import com.github.alexthe666.iceandfire.entity.util.IHumanoid;
import com.github.alexthe666.iceandfire.entity.util.IVillagerFear;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class EntityTroll extends Monster implements IAnimatedEntity, IVillagerFear, IHumanoid, IHasCustomizableAttributes {

    public static final Animation ANIMATION_STRIKE_HORIZONTAL = Animation.create(20);
    public static final Animation ANIMATION_STRIKE_VERTICAL = Animation.create(20);
    public static final Animation ANIMATION_SPEAK = Animation.create(10);
    public static final Animation ANIMATION_ROAR = Animation.create(25);
    public static final ResourceLocation FOREST_LOOT = new ResourceLocation("iceandfire", "entities/troll_forest");
    public static final ResourceLocation FROST_LOOT = new ResourceLocation("iceandfire", "entities/troll_frost");
    public static final ResourceLocation MOUNTAIN_LOOT = new ResourceLocation("iceandfire", "entities/troll_mountain");
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(EntityTroll.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WEAPON = SynchedEntityData.defineId(EntityTroll.class, EntityDataSerializers.INT);
    public float stoneProgress;
    private int animationTick;
    private Animation currentAnimation;
    private boolean avoidSun = true;

    public EntityTroll(EntityType<EntityTroll> t, Level worldIn) {
        super(t, worldIn);
    }

    public static boolean canTrollSpawnOn(EntityType<? extends Mob> typeIn, ServerLevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return worldIn.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(worldIn, pos, randomIn)
            && checkMobSpawnRules(IafEntityRegistry.TROLL.get(), worldIn, reason, pos, randomIn);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, IafConfig.trollMaxHealth)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.35D)
            //ATTACK
            .add(Attributes.ATTACK_DAMAGE, IafConfig.trollAttackStrength)
            //KNOCKBACK RESIST
            .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
            //ARMOR
            .add(Attributes.ARMOR, 9.0D);
    }

    @Override
    public void setConfigurableAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(IafConfig.trollMaxHealth);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(IafConfig.trollAttackStrength);
    }

    private void setAvoidSun(boolean day) {
        if (day && !avoidSun) {
            ((GroundPathNavigation) this.getNavigation()).setAvoidSun(true);
            avoidSun = true;
        }
        if (!day && avoidSun) {
            ((GroundPathNavigation) this.getNavigation()).setAvoidSun(false);
            avoidSun = false;
        }
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.isUnobstructed(this);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, @NotNull MobSpawnType spawnReasonIn) {
        BlockPos pos = this.blockPosition();
        BlockPos heightAt = worldIn.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);
        boolean rngCheck = true;
        return rngCheck && pos.getY() < heightAt.getY() - 10 && super.checkSpawnRules(worldIn, spawnReasonIn);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new TrollAIFleeSun(this, 1.0D));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F, 1.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, false));
        setAvoidSun(true);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entityIn) {
        if (this.getRandom().nextBoolean()) {
            this.setAnimation(ANIMATION_STRIKE_VERTICAL);

        } else {
            this.setAnimation(ANIMATION_STRIKE_HORIZONTAL);
        }
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(WEAPON, 0);
    }

    private int getVariant() {
        return this.entityData.get(VARIANT).intValue();
    }

    private void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public EnumTroll getTrollType() {
        return EnumTroll.values()[getVariant()];
    }

    public void setTrollType(EnumTroll variant) {
        this.setVariant(variant.ordinal());
    }

    private int getWeapon() {
        return this.entityData.get(WEAPON).intValue();
    }

    private void setWeapon(int variant) {
        this.entityData.set(WEAPON, variant);
    }

    public EnumTroll.Weapon getWeaponType() {
        return EnumTroll.Weapon.values()[getWeapon()];
    }

    public void setWeaponType(EnumTroll.Weapon variant) {
        this.setWeapon(variant.ordinal());
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putInt("Weapon", this.getWeapon());
        compound.putFloat("StoneProgress", stoneProgress);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setWeapon(compound.getInt("Weapon"));
        this.stoneProgress = compound.getFloat("StoneProgress");
        this.setConfigurableAttributes();
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setTrollType(EnumTroll.getBiomeType(level().getBiome(this.blockPosition())));
        this.setWeaponType(EnumTroll.getWeaponForType(this.getTrollType()));
        return spawnDataIn;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (source.getMsgId().contains("arrow")) {
            return false;
        }
        return super.hurt(source, damage);
    }

    @Override
    @Nullable
    protected ResourceLocation getDefaultLootTable() {
        switch (this.getTrollType()) {
            case MOUNTAIN:
                return MOUNTAIN_LOOT;
            case FROST:
                return FROST_LOOT;
            case FOREST:
                return FOREST_LOOT;
        }
        return null;
    }

    @Override
    public int getExperienceReward() {
        return 15;
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (this.deathTime == 20 && !this.level().isClientSide) {
            if (IafConfig.trollsDropWeapon) {
                if (this.getRandom().nextInt(3) == 0) {
                    ItemStack weaponStack = new ItemStack(this.getWeaponType().item.get(), 1);
                    weaponStack.hurt(this.getRandom().nextInt(250), this.getRandom(), null);
                    dropItemAt(weaponStack, this.getX(), this.getY(), this.getZ());
                } else {
                    ItemStack brokenDrop = new ItemStack(Blocks.STONE_BRICKS, this.getRandom().nextInt(2) + 1);
                    ItemStack brokenDrop2 = new ItemStack(Blocks.STONE_BRICKS, this.getRandom().nextInt(2) + 1);
                    if (this.getWeaponType() == EnumTroll.Weapon.AXE) {
                        brokenDrop = new ItemStack(Items.STICK, this.getRandom().nextInt(2) + 1);
                        brokenDrop2 = new ItemStack(Blocks.COBBLESTONE, this.getRandom().nextInt(2) + 1);
                    }
                    if (this.getWeaponType() == EnumTroll.Weapon.COLUMN) {
                        brokenDrop = new ItemStack(Blocks.STONE_BRICKS, this.getRandom().nextInt(2) + 1);
                        brokenDrop2 = new ItemStack(Blocks.STONE_BRICKS, this.getRandom().nextInt(2) + 1);
                    }
                    if (this.getWeaponType() == EnumTroll.Weapon.COLUMN_FOREST) {
                        brokenDrop = new ItemStack(Blocks.STONE_BRICKS, this.getRandom().nextInt(2) + 1);
                        brokenDrop2 = new ItemStack(Blocks.STONE_BRICKS, this.getRandom().nextInt(2) + 1);
                    }
                    if (this.getWeaponType() == EnumTroll.Weapon.COLUMN_FROST) {
                        brokenDrop = new ItemStack(Blocks.STONE_BRICKS, this.getRandom().nextInt(2) + 1);
                        brokenDrop2 = new ItemStack(Items.SNOWBALL, this.getRandom().nextInt(4) + 1);
                    }
                    if (this.getWeaponType() == EnumTroll.Weapon.HAMMER) {
                        brokenDrop = new ItemStack(Items.BONE, this.getRandom().nextInt(2) + 1);
                        brokenDrop2 = new ItemStack(Blocks.COBBLESTONE, this.getRandom().nextInt(2) + 1);
                    }
                    if (this.getWeaponType() == EnumTroll.Weapon.TRUNK) {
                        brokenDrop = new ItemStack(Blocks.OAK_LOG, this.getRandom().nextInt(2) + 1);
                        brokenDrop2 = new ItemStack(Blocks.OAK_LOG, this.getRandom().nextInt(2) + 1);
                    }
                    if (this.getWeaponType() == EnumTroll.Weapon.TRUNK_FROST) {
                        brokenDrop = new ItemStack(Blocks.SPRUCE_LOG, this.getRandom().nextInt(4) + 1);
                        brokenDrop2 = new ItemStack(Items.SNOWBALL, this.getRandom().nextInt(4) + 1);
                    }
                    dropItemAt(brokenDrop, this.getX(), this.getY(), this.getZ());
                    dropItemAt(brokenDrop2, this.getX(), this.getY(), this.getZ());

                }
            }
        }
    }

    @Nullable
    private ItemEntity dropItemAt(ItemStack stack, double x, double y, double z) {
        if (stack.getCount() > 0) {
            ItemEntity entityitem = new ItemEntity(this.level(), x, y, z, stack);
            entityitem.setDefaultPickUpDelay();
            this.level().addFreshEntity(entityitem);
            return entityitem;
        }
        return null;

    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().getDifficulty() == Difficulty.PEACEFUL && this.getTarget() instanceof Player) {
            this.setTarget(null);
        }
        boolean stone = EntityGorgon.isStoneMob(this);
        if (stone && stoneProgress < 20.0F) {
            stoneProgress += 2F;
        } else if (!stone && stoneProgress > 0.0F) {
            stoneProgress -= 2F;
        }
        if (!stone && this.getAnimation() == NO_ANIMATION && this.getTarget() != null && this.getRandom().nextInt(100) == 0) {
            this.setAnimation(ANIMATION_ROAR);
        }
        if (this.getAnimation() == ANIMATION_ROAR && this.getAnimationTick() == 5) {
            this.playSound(IafSoundRegistry.TROLL_ROAR, 1, 1);
        }
        if (!stone && this.getHealth() < this.getMaxHealth() && this.tickCount % 30 == 0) {
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 30, 1, false, false));
        }
        setAvoidSun(this.level().isDay());
        if (this.level().isDay() && !this.level().isClientSide) {
            float f = this.level().getBrightness(LightLayer.SKY, this.blockPosition());
            BlockPos blockpos = this.getVehicle() instanceof Boat ? (new BlockPos(this.getBlockX(), this.getBlockY(), this.getBlockZ())).above() : new BlockPos(this.getBlockX(), this.getBlockY(), this.getBlockZ());
            if (f > 0.5F && this.level().canSeeSky(blockpos)) {
                this.setDeltaMovement(0, 0, 0);
                this.setAnimation(NO_ANIMATION);
                this.playSound(IafSoundRegistry.TURN_STONE, 1, 1);
                this.stoneProgress = 20;
                EntityStoneStatue statue = EntityStoneStatue.buildStatueEntity(this);
                statue.getTrappedTag().putFloat("StoneProgress", 20);
                statue.absMoveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                if (!level().isClientSide) {
                    level().addFreshEntity(statue);
                }
                statue.yRotO = this.getYRot();
                statue.setYRot(this.getYRot());
                statue.yHeadRot = this.getYRot();
                statue.yBodyRot = this.getYRot();
                statue.yBodyRotO = this.getYRot();
                this.remove(RemovalReason.KILLED);
            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
            float weaponX = (float) (getX() + 1.9F * Mth.cos((float) ((yBodyRot + 90) * Math.PI / 180)));
            float weaponZ = (float) (getZ() + 1.9F * Mth.sin((float) ((yBodyRot + 90) * Math.PI / 180)));
            float weaponY = (float) (getY() + (0.2F));
            BlockState state = level().getBlockState(BlockPos.containing(weaponX, weaponY - 1, weaponZ));
            for (int i = 0; i < 20; i++) {
                double motionX = getRandom().nextGaussian() * 0.07D;
                double motionY = getRandom().nextGaussian() * 0.07D;
                double motionZ = getRandom().nextGaussian() * 0.07D;
                if (state.isSolid() && level().isClientSide) {
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), weaponX + (this.getRandom().nextFloat() - 0.5F), weaponY + (this.getRandom().nextFloat() - 0.5F), weaponZ + (this.getRandom().nextFloat() - 0.5F), motionX, motionY, motionZ);
                }
            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getTarget() != null && this.distanceToSqr(this.getTarget()) < 4D && this.getAnimationTick() == 10 && this.deathTime <= 0) {
            this.getTarget().hurt(this.level().damageSources().mobAttack(this), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
        }
        if (this.getAnimation() == ANIMATION_STRIKE_HORIZONTAL && this.getTarget() != null && this.distanceToSqr(this.getTarget()) < 4D && this.getAnimationTick() == 10 && this.deathTime <= 0) {
            LivingEntity target = this.getTarget();
            target.hurt(this.level().damageSources().mobAttack(this), (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue());
            float f1 = 0.5F;
            float f2 = target.zza;
            float f3 = 0.6F;
            float f4 = Mth.sqrt(f2 * f2 + f3 * f3);

            if (f4 < 1.0F) {
                f4 = 1.0F;
            }

            f4 = f1 / f4;
            f2 = f2 * f4;
            f3 = f3 * f4;
            float f5 = Mth.sin(this.getYRot() * 0.017453292F);
            float f6 = Mth.cos(this.getYRot() * 0.017453292F);
            // float f7 = f2 * f6 - f3 * f5;
            // float f8 = f3 * f6 + f2 * f5;
            target.setDeltaMovement(f5, f6, 0.4F);
        }
        if (this.getNavigation().isDone() && this.getTarget() != null && this.distanceToSqr(this.getTarget()) > 3 && this.distanceToSqr(this.getTarget()) < 30 && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            this.lookAt(this.getTarget(), 30, 30);
            if (this.getAnimation() == NO_ANIMATION && this.random.nextInt(15) == 0) {
                this.setAnimation(ANIMATION_STRIKE_VERTICAL);
            }
            if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
                float weaponX = (float) (getX() + 1.9F * Mth.cos((float) ((yBodyRot + 90) * Math.PI / 180)));
                float weaponZ = (float) (getZ() + 1.9F * Mth.sin((float) ((yBodyRot + 90) * Math.PI / 180)));
                float weaponY = (float) (getY() + (this.getEyeHeight() / 2));
                Explosion explosion = new Explosion(level(), this, weaponX, weaponY, weaponZ, 1F + this.getRandom().nextFloat(), new ArrayList<>());
                if (!MinecraftForge.EVENT_BUS.post(new GenericGriefEvent(this, weaponX, weaponY, weaponZ))) {
                    explosion.explode();
                    explosion.finalizeExplosion(true);
                }

                this.playSound(SoundEvents.GENERIC_EXPLODE, 1, 1);

            }
        }
        if (this.getAnimation() == ANIMATION_STRIKE_VERTICAL && this.getAnimationTick() == 10) {
            this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 2.5F, 0.5F);

        }
        if (this.getAnimation() == ANIMATION_STRIKE_HORIZONTAL && this.getAnimationTick() == 10) {
            this.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 2.5F, 0.5F);
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playAmbientSound();
    }

    @Override
    protected void playHurtSound(@NotNull DamageSource source) {
        if (this.getAnimation() == this.NO_ANIMATION) {
            this.setAnimation(ANIMATION_SPEAK);
        }
        super.playHurtSound(source);
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
    @Nullable
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.TROLL_IDLE;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return IafSoundRegistry.TROLL_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.TROLL_DIE;
    }


    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_STRIKE_HORIZONTAL, ANIMATION_STRIKE_VERTICAL, ANIMATION_SPEAK, ANIMATION_ROAR};
    }
}
