package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import scala.Int;

import javax.annotation.Nullable;

public class EntityMobSkull extends EntityAnimal implements IBlacklistedFromStatues, IDeadMob {

    private static final DataParameter<Float> SKULL_DIRECTION = EntityDataManager.<Float>createKey(EntityMobSkull.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> SKULL_ENUM = EntityDataManager.<Integer>createKey(EntityMobSkull.class, DataSerializers.VARINT);

    public EntityMobSkull(World worldIn) {
        super(worldIn);
        this.setSize(0.9F, 0.65F);
        this.ignoreFrustumCheck = true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0);
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10);
    }

    @Override
    public boolean isEntityInvulnerable(DamageSource i) {
        return i.getTrueSource() != null;
    }

    @Override
    public boolean isAIDisabled() {
        return true;
    }

    public boolean isOnWall() {
        return this.world.isAirBlock(this.getPosition().down());
    }

    public void onUpdate() {
        this.prevRenderYawOffset = 0;
        this.prevRotationYawHead = 0;
        this.renderYawOffset = 0;
        this.rotationYawHead = 0;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(SKULL_DIRECTION, Float.valueOf(0F));
        this.getDataManager().register(SKULL_ENUM, Integer.valueOf(0));
    }

    public float getYaw() {
        return this.getDataManager().get(SKULL_DIRECTION).floatValue();
    }

    public void setYaw(float var1) {
        this.getDataManager().set(SKULL_DIRECTION, var1);
    }

    private int getEnumOrdinal() {
        return this.getDataManager().get(SKULL_ENUM).intValue();
    }

    private void setEnumOrdinal(int var1) {
        this.getDataManager().set(SKULL_ENUM, var1);
    }

    public EnumSkullType getSkullType() {
        return EnumSkullType.values()[MathHelper.clamp(getEnumOrdinal(), 0, EnumSkullType.values().length - 1)];
    }

    public void setSkullType(EnumSkullType skullType) {
        setEnumOrdinal(skullType.ordinal());
    }

    @Override
    public boolean attackEntityFrom(DamageSource var1, float var2) {
        this.turnIntoItem();
        return super.attackEntityFrom(var1, var2);
    }

    public void turnIntoItem() {
        if (isDead)
            return;
        this.setDead();
        ItemStack stack = new ItemStack(getSkullType().skull_item, 1, 0);
        if (!this.world.isRemote)
            this.entityDropItem(stack, 0.0F);
    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (player.isSneaking()) {
            this.setYaw(player.rotationYaw);
        }
        return super.processInteract(player, hand);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.setYaw(compound.getFloat("SkullYaw"));
        this.setEnumOrdinal(compound.getInteger("SkullType"));
        super.readEntityFromNBT(compound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setFloat("SkullYaw", this.getYaw());
        compound.setInteger("SkullType", this.getEnumOrdinal());
        super.writeEntityToNBT(compound);
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entity) {
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
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }
}
