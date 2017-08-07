package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityStoneStatue extends EntityLiving {

    private int crackAmount;
    public boolean smallArms;

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
        if (source == DamageSource.IN_WALL || source == DamageSource.FALLING_BLOCK) {
            return false;
        }
        this.crackAmount++;
        if(crackAmount > 5){
            for(int i = 0; i < 1 + this.getRNG().nextInt(5); i++){
                this.dropItem(Item.getItemFromBlock(Blocks.COBBLESTONE), 1);
            }
            this.setDead();
        }
        return false;
    }

    public int getCrackAmount() {
        return crackAmount;
    }

    public void setCrackAmount(int crackAmount) {
        this.crackAmount = crackAmount;
    }


}
