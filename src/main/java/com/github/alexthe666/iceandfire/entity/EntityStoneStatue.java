package com.github.alexthe666.iceandfire.entity;

import javax.annotation.Nullable;

import com.github.alexthe666.iceandfire.entity.util.IBlacklistedFromStatues;
import com.google.common.collect.ImmutableList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityStoneStatue extends LivingEntity implements IBlacklistedFromStatues {

    public boolean smallArms;
    private int crackAmount;

    public EntityStoneStatue(EntityType t, World worldIn) {
        super(t, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MobEntity.func_233666_p_()
                //HEALTH
                .func_233815_a_(Attributes.field_233818_a_, 20)
                //SPEED
                .func_233815_a_(Attributes.field_233821_d_, 0.0D)
                //ATTACK
                .func_233815_a_(Attributes.field_233823_f_, 1.0D);
    }

    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return this.getCollisionBoundingBox();
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getBoundingBox();
    }

    public boolean isAIDisabled() {
        return true;
    }

    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        tag.putInt("CrackAmount", this.crackAmount);
        tag.putBoolean("SmallArms", this.smallArms);
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        this.setCrackAmount(tag.getByte("CrackAmount"));
        this.smallArms = tag.getBoolean("SmallArms");
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return source == DamageSource.OUT_OF_WORLD;
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return ImmutableList.of();
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }

    public int getCrackAmount() {
        return crackAmount;
    }

    public void setCrackAmount(int crackAmount) {
        this.crackAmount = crackAmount;
    }


    @Override
    public boolean canBeTurnedToStone() {
        return false;
    }
}
