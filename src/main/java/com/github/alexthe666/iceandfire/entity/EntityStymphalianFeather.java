package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.IceAndFireConfig;
import com.github.alexthe666.iceandfire.core.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityStymphalianFeather extends EntityArrow{

    public EntityStymphalianFeather(World worldIn) {
        super(worldIn);
    }

    public EntityStymphalianFeather(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
        this.setDamage(IceAndFire.CONFIG.stymphalianBirdFeatherAttackStength);
    }

    public void setDead() {
        super.setDead();
        if(!world.isRemote && this.rand.nextInt(25) == 0){
            this.entityDropItem(getArrowStack(), 0.1F);
        }
    }

    public void onUpdate() {
        super.onUpdate();
        if(this.ticksExisted > 100){
            this.setDead();
        }
    }
    protected void onHit(RayTraceResult raytraceResultIn) {
        if(this.shootingEntity instanceof EntityStymphalianBird && raytraceResultIn.entityHit != null && raytraceResultIn.entityHit instanceof EntityStymphalianBird){
            return;
        }else{
            super.onHit(raytraceResultIn);
            if(raytraceResultIn.entityHit != null && raytraceResultIn.entityHit instanceof  EntityLivingBase){
                EntityLivingBase entitylivingbase = (EntityLivingBase)raytraceResultIn.entityHit;
                entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() - 1);
            }

        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ModItems.stymphalian_bird_feather);
    }
}
