package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
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
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityDragonSkull extends EntityAnimal implements IBlacklistedFromStatues, IDeadMob {

    private static final DataParameter<Integer> DRAGON_TYPE = EntityDataManager.createKey(EntityDragonSkull.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DRAGON_AGE = EntityDataManager.createKey(EntityDragonSkull.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DRAGON_STAGE = EntityDataManager.createKey(EntityDragonSkull.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DRAGON_DIRECTION = EntityDataManager.createKey(EntityDragonSkull.class, DataSerializers.FLOAT);

    public final float minSize = 0.3F;
    public final float maxSize = 8.58F;

    public EntityDragonSkull(World worldIn) {
        super(worldIn);
        this.setSize(0.9F, 0.65F);
        this.ignoreFrustumCheck = true;
        // setScale(this.getDragonAge());
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
        this.getDataManager().register(DRAGON_TYPE, Integer.valueOf(0));
        this.getDataManager().register(DRAGON_AGE, Integer.valueOf(0));
        this.getDataManager().register(DRAGON_STAGE, Integer.valueOf(0));
        this.getDataManager().register(DRAGON_DIRECTION, Float.valueOf(0F));
    }

    public float getYaw() {
        return this.getDataManager().get(DRAGON_DIRECTION).floatValue();
    }

    public void setYaw(float var1) {
        this.getDataManager().set(DRAGON_DIRECTION, var1);
    }

    public int getType() {
        return this.getDataManager().get(DRAGON_TYPE).intValue();
    }

    public void setType(int var1) {
        this.getDataManager().set(DRAGON_TYPE, var1);
    }

    public int getStage() {
        return this.getDataManager().get(DRAGON_STAGE).intValue();
    }

    public void setStage(int var1) {
        this.getDataManager().set(DRAGON_STAGE, var1);
    }

    public int getDragonAge() {
        return this.getDataManager().get(DRAGON_AGE).intValue();
    }

    public void setDragonAge(int var1) {
        this.getDataManager().set(DRAGON_AGE, var1);
    }

    @Override
    public SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return null;
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
        ItemStack stack = new ItemStack(IafItemRegistry.dragon_skull, 1, getType());
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger("Stage", this.getStage());
        stack.getTagCompound().setInteger("DragonAge", this.getDragonAge());
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
        this.setType(compound.getInteger("Type"));
        this.setStage(compound.getInteger("Stage"));
        this.setDragonAge(compound.getInteger("DragonAge"));
        this.setYaw(compound.getFloat("DragonYaw"));
        super.readEntityFromNBT(compound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("Type", this.getType());
        compound.setInteger("Stage", this.getStage());
        compound.setInteger("DragonAge", this.getDragonAge());
        compound.setFloat("DragonYaw", this.getYaw());
        super.writeEntityToNBT(compound);
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
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected void collideWithEntity(Entity entity) {
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
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
    public boolean isNoDespawnRequired() {
        return true;
    }

    @Override
    protected boolean canDespawn() {
        return false;
    }
}
