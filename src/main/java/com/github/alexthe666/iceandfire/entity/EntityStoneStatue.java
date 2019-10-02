package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityStoneStatue extends EntityLiving implements IBlacklistedFromStatues {

    public boolean smallArms;
    private int crackAmount;

    public EntityStoneStatue(World worldIn) {
        super(worldIn);
        this.setSize(0.8F, 1.9F);
    }


    @Nullable
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return this.getCollisionBoundingBox();
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }

    public boolean isAIDisabled() {
        return true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setInteger("CrackAmount", this.crackAmount);
        tag.setBoolean("SmallArms", this.smallArms);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setCrackAmount(tag.getByte("CrackAmount"));
        this.smallArms = tag.getBoolean("SmallArms");
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return source == DamageSource.OUT_OF_WORLD;
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
