package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.github.alexthe666.iceandfire.entity.util.IDeadMob;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class EntityMobSkull extends AnimalEntity implements IBlacklistedFromStatues, IDeadMob {

    private static final DataParameter<Float> SKULL_DIRECTION = EntityDataManager.defineId(EntityMobSkull.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> SKULL_ENUM = EntityDataManager.defineId(EntityMobSkull.class, DataSerializers.INT);

    public EntityMobSkull(EntityType t, World worldIn) {
        super(t, worldIn);
        this.noCulling = true;
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.createMobAttributes()
            //HEALTH
            .add(Attributes.MAX_HEALTH, 10.0D)
            //SPEED
            .add(Attributes.MOVEMENT_SPEED, 0.0D);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource i) {
        return i.getEntity() != null;
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
        this.getEntityData().define(SKULL_DIRECTION, Float.valueOf(0F));
        this.getEntityData().define(SKULL_ENUM, Integer.valueOf(0));
    }

    public float getYaw() {
        return this.getEntityData().get(SKULL_DIRECTION).floatValue();
    }

    public void setYaw(float var1) {
        this.getEntityData().set(SKULL_DIRECTION, var1);
    }

    private int getEnumOrdinal() {
        return this.getEntityData().get(SKULL_ENUM).intValue();
    }

    private void setEnumOrdinal(int var1) {
        this.getEntityData().set(SKULL_ENUM, var1);
    }

    public EnumSkullType getSkullType() {
        return EnumSkullType.values()[MathHelper.clamp(getEnumOrdinal(), 0, EnumSkullType.values().length - 1)];
    }

    public void setSkullType(EnumSkullType skullType) {
        setEnumOrdinal(skullType.ordinal());
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
        ItemStack stack = new ItemStack(getSkullType().skull_item, 1);
        if (!this.level.isClientSide)
            this.spawnAtLocation(stack, 0.0F);
    }

    public boolean isFood(ItemStack stack) {
        return false;
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
        this.setYaw(compound.getFloat("SkullYaw"));
        this.setEnumOrdinal(compound.getInt("SkullType"));
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        compound.putFloat("SkullYaw", this.getYaw());
        compound.putInt("SkullType", this.getEnumOrdinal());
        super.addAdditionalSaveData(compound);
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

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld serverWorld, AgeableEntity ageable) {
        return null;
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
