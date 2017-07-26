package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityStoneStatue extends EntityLiving {

    private Entity model;
    private Class<? extends Entity> modelClass;
    private int crackAmount;

    public EntityStoneStatue(World worldIn) {
        this(worldIn, new EntityBat(worldIn));
    }

    public EntityStoneStatue(World worldIn, Entity model) {
        super(worldIn);
        this.model = model;
        this.modelClass = model.getClass();
        if(model != null){
            this.setSize(model.width, model.height);
        }else{
            this.setSize(0.8F, 1.9F);
        }
        if(model instanceof EntityStoneStatue){
            this.setDead();
        }
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
        tag.setString("ModelEntityClassPath", this.modelClass.getName());

        if(model != null){
            NBTTagList nbttaglist = new NBTTagList();
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            model.writeToNBT(nbttagcompound);
            nbttaglist.appendTag(nbttagcompound);
            tag.setTag("ModelEntity", nbttaglist);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        this.setCrackAmount(tag.getByte("CrackAmount"));
        try {
            Class<?> cls = Class.forName(tag.getString("ModelEntityClassPath"));
            this.modelClass = (Class<? extends Entity>)cls;
            if(modelClass == EntityStoneStatue.class){
                this.setDead();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            this.setDead();
        }catch (ClassCastException e) {
            e.printStackTrace();
            this.setDead();
        }
        if(modelClass != null){
            NBTTagList nbttaglist = tag.getTagList("ModelEntity", 10);
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(0);
            Entity entity = null;
            if (Entity.class.isAssignableFrom(this.modelClass)) {
                try {
                    entity = this.modelClass.getDeclaredConstructor(World.class).newInstance(world);
                } catch (ReflectiveOperationException e) {
                    e.printStackTrace();
                    this.setDead();
                }
            }
            if(entity != null) {
                this.model = entity;
                this.model.readFromNBT(nbttagcompound);
            }
        }

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
        return super.attackEntityFrom(source, amount);
    }

    public Entity getModel() {
        return model;
    }

    public void setModel(Entity model) {
        this.model = model;
        this.modelClass = model.getClass();
        if(model != null){
            this.setSize(model.width, model.height);
        }else{
            this.setSize(0.8F, 1.9F);
        }
        if(model instanceof EntityStoneStatue){
            this.setDead();
        }
    }

    public int getCrackAmount() {
        return crackAmount;
    }

    public void setCrackAmount(int crackAmount) {
        this.crackAmount = crackAmount;
    }


}
