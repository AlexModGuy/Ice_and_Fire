package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;

public class AmphithereAIHurtByTarget extends EntityAIHurtByTarget {

    public AmphithereAIHurtByTarget(EntityAmphithere amphithere, boolean help, Class[] classes) {
        super(amphithere, help, classes);
    }

    protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
        EntityAmphithere amphithere = (EntityAmphithere)creatureIn;
        if(amphithere.isTamed() || !(entityLivingBaseIn instanceof EntityPlayer)) {
            amphithere.setAttackTarget(entityLivingBaseIn);
        }
    }
}
