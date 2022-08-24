package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class EntityDragonSkull extends AnimalEntity implements IBlacklistedFromStatues, IDeadMob {

    private static final DataParameter<Integer> DRAGON_TYPE = EntityDataManager.defineId(EntityDragonSkull.class, DataSerializers.INT);
    private static final DataParameter<Integer> DRAGON_AGE = EntityDataManager.defineId(EntityDragonSkull.class, DataSerializers.INT);
    private static final DataParameter<Integer> DRAGON_STAGE = EntityDataManager.defineId(EntityDragonSkull.class, DataSerializers.INT);
    private static final DataParameter<Float> DRAGON_DIRECTION = EntityDataManager.defineId(EntityDragonSkull.class, DataSerializers.FLOAT);

    public final float minSize = 0.3F;
    public final float maxSize = 8.58F;

    public EntityDragonSkull(EntityType type, World worldIn) {
        super(type, worldIn);
        this.noCulling = true;
        // setScale(this.getDragonAge());
    }

    public boolean isFood(ItemStack stack) {
        return false;
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 10)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0D);
    }

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
        return this.level.isEmptyBlock(this.blockPosition().below());
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
        this.getEntityData().define(DRAGON_TYPE, Integer.valueOf(0));
        this.getEntityData().define(DRAGON_AGE, Integer.valueOf(0));
        this.getEntityData().define(DRAGON_STAGE, Integer.valueOf(0));
        this.getEntityData().define(DRAGON_DIRECTION, Float.valueOf(0F));
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
    public SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return null;
    }

    @Override
    public boolean hurt(DamageSource var1, float var2) {
        this.turnIntoItem();
        return super.hurt(var1, var2);
    }

    public void turnIntoItem() {
        if (removed)
            return;
        this.remove();
        ItemStack stack = new ItemStack(getDragonSkullItem());
        stack.setTag(new CompoundNBT());
        stack.getTag().putInt("Stage", this.getStage());
        stack.getTag().putInt("DragonAge", this.getDragonAge());
        if (!this.level.isClientSide)
            this.spawnAtLocation(stack, 0.0F);

    }

    public Item getDragonSkullItem() {
        switch (getDragonType()) {
            case 0:
                return IafItemRegistry.DRAGON_SKULL_FIRE;
            case 1:
                return IafItemRegistry.DRAGON_SKULL_ICE;
            case 2:
                return IafItemRegistry.DRAGON_SKULL_LIGHTNING;
            default:
                return IafItemRegistry.DRAGON_SKULL_FIRE;
        }
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageable) {
        return null;
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        if (player.isShiftKeyDown()) {
            this.setYaw(player.yRot);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        this.setDragonType(compound.getInt("Type"));
        this.setStage(compound.getInt("Stage"));
        this.setDragonAge(compound.getInt("DragonAge"));
        this.setYaw(compound.getFloat("DragonYaw"));
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
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
    protected void doPush(Entity entity) {
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
