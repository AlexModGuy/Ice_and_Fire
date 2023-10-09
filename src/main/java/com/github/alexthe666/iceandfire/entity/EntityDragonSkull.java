package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EntityDragonSkull extends Animal implements IBlacklistedFromStatues, IDeadMob {

    private static final EntityDataAccessor<Integer> DRAGON_TYPE = SynchedEntityData.defineId(EntityDragonSkull.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DRAGON_AGE = SynchedEntityData.defineId(EntityDragonSkull.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DRAGON_STAGE = SynchedEntityData.defineId(EntityDragonSkull.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DRAGON_DIRECTION = SynchedEntityData.defineId(EntityDragonSkull.class, EntityDataSerializers.FLOAT);

    public final float minSize = 0.3F;
    public final float maxSize = 8.58F;

    public EntityDragonSkull(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        // setScale(this.getDragonAge());
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 10)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0D);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource i) {
        return i.getEntity() != null && super.isInvulnerableTo(i);
    }

    @Override
    public boolean isNoAi() {
        return true;
    }

    public boolean isOnWall() {
        return this.level().isEmptyBlock(this.blockPosition().below());
    }

    public void onUpdate() {
        this.yBodyRotO = 0;
        this.yHeadRotO = 0;
        this.yBodyRot = 0;
        this.yHeadRot = 0;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(DRAGON_TYPE, 0);
        this.getEntityData().define(DRAGON_AGE, 0);
        this.getEntityData().define(DRAGON_STAGE, 0);
        this.getEntityData().define(DRAGON_DIRECTION, 0F);
    }

    public float getYaw() {
        return this.getEntityData().get(DRAGON_DIRECTION).floatValue();
    }

    public void setYaw(float var1) {
        this.getEntityData().set(DRAGON_DIRECTION, var1);
    }

    public int getDragonType() {
        return this.getEntityData().get(DRAGON_TYPE).intValue();
    }

    public void setDragonType(int var1) {
        this.getEntityData().set(DRAGON_TYPE, var1);
    }

    public int getStage() {
        return this.getEntityData().get(DRAGON_STAGE).intValue();
    }

    public void setStage(int var1) {
        this.getEntityData().set(DRAGON_STAGE, var1);
    }

    public int getDragonAge() {
        return this.getEntityData().get(DRAGON_AGE).intValue();
    }

    public void setDragonAge(int var1) {
        this.getEntityData().set(DRAGON_AGE, var1);
    }

    @Override
    public SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return null;
    }

    @Override
    public boolean hurt(@NotNull DamageSource var1, float var2) {
        this.turnIntoItem();
        return super.hurt(var1, var2);
    }

    public void turnIntoItem() {
        if (isRemoved())
            return;
        this.remove(RemovalReason.DISCARDED);
        ItemStack stack = new ItemStack(getDragonSkullItem());
        stack.setTag(new CompoundTag());
        stack.getTag().putInt("Stage", this.getStage());
        stack.getTag().putInt("DragonAge", this.getDragonAge());
        if (!this.level().isClientSide)
            this.spawnAtLocation(stack, 0.0F);

    }

    public Item getDragonSkullItem() {
        switch (getDragonType()) {
            case 0:
                return IafItemRegistry.DRAGON_SKULL_FIRE.get();
            case 1:
                return IafItemRegistry.DRAGON_SKULL_ICE.get();
            case 2:
                return IafItemRegistry.DRAGON_SKULL_LIGHTNING.get();
            default:
                return IafItemRegistry.DRAGON_SKULL_FIRE.get();
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
        return null;
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            this.setYaw(player.getYRot());
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setDragonType(compound.getInt("Type"));
        this.setStage(compound.getInt("Stage"));
        this.setDragonAge(compound.getInt("DragonAge"));
        this.setYaw(compound.getFloat("DragonYaw"));
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Type", this.getDragonType());
        compound.putInt("Stage", this.getStage());
        compound.putInt("DragonAge", this.getDragonAge());
        compound.putFloat("DragonYaw", this.getYaw());
        super.addAdditionalSaveData(compound);
    }

    public float getDragonSize() {
        float step;
        step = (minSize - maxSize) / (125);

        if (this.getDragonAge() > 125) {
            return this.minSize + (step * 125);
        }

        return this.minSize + (step * this.getDragonAge());
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
    }

    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }

    @Override
    public boolean isMobDead() {
        return true;
    }

    public int getDragonStage() {
        return Math.max(getStage(), 1);
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}
