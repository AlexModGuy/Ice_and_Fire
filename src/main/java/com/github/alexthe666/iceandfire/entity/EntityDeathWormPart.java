package com.github.alexthe666.iceandfire.entity;

import net.ilexiconn.llibrary.server.entity.multipart.PartEntity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class EntityDeathWormPart extends PartEntity {

    public EntityDeathWormPart(EntityLiving parent, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(parent, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
    }

    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        return this.parent.processInitialInteract(player, hand);
    }

    @Override
    public void onUpdate() {
        if(this.parent == null){
            this.setDead();
        }
        super.onUpdate();
    }
}
