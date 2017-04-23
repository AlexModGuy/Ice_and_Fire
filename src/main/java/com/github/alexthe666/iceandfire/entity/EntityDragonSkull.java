package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.core.ModItems;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.*;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class EntityDragonSkull extends EntityAnimal {

    private static final DataParameter<Integer> DRAGON_TYPE = EntityDataManager.<Integer>createKey(EntityDragonSkull.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DRAGON_AGE = EntityDataManager.<Integer>createKey(EntityDragonSkull.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> DRAGON_STAGE = EntityDataManager.<Integer>createKey(EntityDragonSkull.class, DataSerializers.VARINT);
    public final float minSize = 0.3F;
    public final float maxSize = 8.58F;
    private float field_98056_d = -1.0F;
    private float field_98057_e;

    public EntityDragonSkull(World worldIn) {
        super(worldIn);
        this.setSize(1.45F, 0.65F);
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
        return i.getEntity() != null;
    }

    @Override
    public boolean isAIDisabled() {
        return true;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(DRAGON_TYPE, 0);
        this.getDataManager().register(DRAGON_AGE, 0);
        this.getDataManager().register(DRAGON_STAGE, 0);

    }

    public int getType() {
        return this.getDataManager().get(DRAGON_TYPE);
    }

    public void setType(int var1) {
        this.getDataManager().set(DRAGON_TYPE, var1);
    }

    public int getStage() {
        return this.getDataManager().get(DRAGON_STAGE);
    }

    public void setStage(int var1) {
        this.getDataManager().set(DRAGON_STAGE, var1);
    }

    public int getDragonAge() {
        return this.getDataManager().get(DRAGON_AGE);
    }

    public void setDragonAge(int var1) {
        this.getDataManager().set(DRAGON_AGE, var1);
    }

    @Override
    public SoundEvent getHurtSound() {
        return null;
    }

    @Override
    public boolean attackEntityFrom(DamageSource var1, float var2) {
        this.turnIntoItem();
        return super.attackEntityFrom(var1, var2);
    }

    public void turnIntoItem() {
        ItemStack stack = new ItemStack(ModItems.dragon_skull, 1, getType());
        stack.setTagCompound(new NBTTagCompound());
        stack.getTagCompound().setInteger("Stage", this.getStage());
        stack.getTagCompound().setInteger("DragonAge", this.getDragonAge());
        if (!this.world.isRemote)
            this.entityDropItem(stack, 0.0F);
        this.setDead();

    }

    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (player.isSneaking()) {
            this.rotationYaw = player.rotationYaw;
        }
        return super.processInteract(player, hand);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.setType(compound.getInteger("Type"));
        this.setStage(compound.getInteger("Stage"));
        this.setDragonAge(compound.getInteger("DragonAge"));
        super.readEntityFromNBT(compound);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("Type", this.getType());
        compound.setInteger("Stage", this.getStage());
        compound.setInteger("DragonAge", this.getDragonAge());
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
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

}
