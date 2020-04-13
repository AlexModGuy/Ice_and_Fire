package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;

public class IAFLookHelper extends EntityLookHelper {

    public IAFLookHelper(EntityLiving entitylivingIn) {
        super(entitylivingIn);
    }

    @Override
    public void setLookPositionWithEntity(Entity entityIn, float deltaYaw, float deltaPitch)
    {
        try{
            super.setLookPositionWithEntity(entityIn, deltaYaw, deltaPitch);//rarely causes crash with vanilla
        }catch(Exception e){
            IceAndFire.logger.debug(" Stopped a crash from happening relating to faulty looking AI.");
        }
    }
}
