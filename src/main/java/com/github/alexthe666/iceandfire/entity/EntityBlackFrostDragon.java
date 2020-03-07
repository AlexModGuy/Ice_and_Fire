package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.ai.*;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityBlackFrostDragon extends EntityIceDragon implements IDreadMob {

    public EntityBlackFrostDragon(World worldIn) {
        super(worldIn);
        this.maximumArmor = 70D;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new DragonAIRide(this));
        this.tasks.addTask(1, this.aiSit = new EntityAISit(this));
        this.tasks.addTask(2, new DragonAIMate(this, 1.0D));
        this.tasks.addTask(2, new DragonAIEscort(this, 1.0D));
        this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.5D, false));
        this.tasks.addTask(4, new AquaticAITempt(this, 1.0D, ModItems.frost_stew, false));
        this.tasks.addTask(6, new DragonAIWander(this, 1.0D));
        this.tasks.addTask(7, new DragonAIWatchClosest(this, EntityLivingBase.class, 6.0F));
        this.tasks.addTask(7, new DragonAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(4, new DreadAITargetNonDread(this, EntityLivingBase.class, false));
        this.targetTasks.addTask(5, new DragonAITargetItems(this, false));
    }

    @Nullable
    public Entity getControllingPassenger() {
        if(getCommander() != null && getCommander().isRidingOrBeingRiddenBy(this)){
            return getCommander();
        }
        return super.getControllingPassenger();
    }

    @Override
    public Entity getCommander() {
        return null;
    }

    @Override
    public String getVariantName(int variant) {
        return "blue_";
    }

    public Item getVariantScale(int variant) {
        return ModItems.dragonscales_blue;
    }

    public Item getVariantEgg(int variant) {
        return ModItems.dragonegg_blue;
    }

    public boolean isBreedingItem(@Nullable ItemStack stack) {
        return false;
    }

    public int getDragonStage() {
        return 5;
    }

    public int getAgeInDays() {
        return 125;
    }

    public int getArmorOrdinal(ItemStack stack) {
        return 0;
    }

    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    public boolean isMale() {
        return false;
    }

    public boolean isDaytime() {
        return true;
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setGender(this.getRNG().nextBoolean());
        this.setSleeping(false);
        this.updateAttributes();
        this.growDragon(125);
        this.heal((float) maximumHealth);
        this.usingGroundAttack = true;
        this.setHunger(50);
        return livingdata;
    }
}
