package com.github.alexthe666.iceandfire.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class EntityDragonPart extends EntityMutlipartPart {
    private EntityDragonBase dragon;

    public EntityDragonPart(EntityDragonBase dragon, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(dragon, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
        this.dragon = dragon;
        this.isImmuneToFire = dragon instanceof EntityFireDragon;
    }

    public void collideWithNearbyEntities() {
    }
}
