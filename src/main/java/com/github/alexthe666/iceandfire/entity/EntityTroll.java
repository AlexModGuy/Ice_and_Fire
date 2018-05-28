package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityTroll extends EntityMob implements IAnimatedEntity {

    private int animationTick;
    private Animation currentAnimation;
    private EnumTroll type;
    private EnumTroll.Weapon weaponType;
    private static final DataParameter<Integer> VARIANT = EntityDataManager.<Integer>createKey(EntityTroll.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> WEAPON = EntityDataManager.<Integer>createKey(EntityTroll.class, DataSerializers.VARINT);
    public static Animation ANIMATION_STRIKE_HORIZONTAL = Animation.create(20);
    public static Animation ANIMATION_STRIKE_VERTICAL = Animation.create(20);

    public EntityTroll(World worldIn) {
        super(worldIn);
        this.setSize(1.2F, 3.5F);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(VARIANT, 0);
        this.dataManager.register(WEAPON, 0);
    }

    private int getVariant() {
        return this.dataManager.get(VARIANT);
    }

    private void setVariant(int variant) {
        this.dataManager.set(VARIANT, variant);
    }

    public void setType(EnumTroll variant) {
        this.setVariant(variant.ordinal());
    }

    public EnumTroll getType(){
        return EnumTroll.values()[getVariant()];
    }

    private int getWeapon() {
        return this.dataManager.get(WEAPON);
    }

    private void setWeapon(int variant) {
        this.dataManager.set(WEAPON, variant);
    }

    public void setWeaponType(EnumTroll.Weapon variant) {
        this.setWeapon(variant.ordinal());
    }

    public EnumTroll.Weapon getWeaponType(){
        return EnumTroll.Weapon.values()[getWeapon()];
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.getVariant());
        compound.setInteger("Weapon", this.getWeapon());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setVariant(compound.getInteger("Variant"));
        this.setWeapon(compound.getInteger("Weapon"));
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setType(EnumTroll.getBiomeType(world.getBiome(this.getPosition())));
        this.setWeaponType(EnumTroll.getWeaponForType(this.getType()));
        return livingdata;
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
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
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_STRIKE_HORIZONTAL, ANIMATION_STRIKE_VERTICAL};
    }
}
