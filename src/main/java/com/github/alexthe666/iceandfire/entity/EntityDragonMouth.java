package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

/**Part for Dragon's Mouth*/
public class EntityDragonMouth extends Entity
{
    public final EntityDragonBase entityDragonObj;
    public final String partName;

    public EntityDragonMouth(EntityDragonBase parent, String partName)
    {
        super(parent.worldObj);
        this.setSize(0.2F, 0.2F);
        this.entityDragonObj = parent;
        this.partName = partName;
    }

    protected void entityInit() {}

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {}

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {}

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return false;
    }

    /**
     * Returns true if Entity argument is equal to this Entity
     */
    public boolean isEntityEqual(Entity entityIn)
    {
        return this == entityIn || this.entityDragonObj == entityIn;
    }
}