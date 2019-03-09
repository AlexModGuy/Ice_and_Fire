package com.github.alexthe666.iceandfire.entity;

public class EntityDragonPart extends EntityMutlipartPart {
    private EntityDragonBase dragon;

    public EntityDragonPart(EntityDragonBase dragon, float radius, float angleYaw, float offsetY, float sizeX, float sizeY, float damageMultiplier) {
        super(dragon, radius, angleYaw, offsetY, sizeX, sizeY, damageMultiplier);
        this.dragon = dragon;
        this.isImmuneToFire = dragon instanceof EntityFireDragon;
    }

    public void collideWithNearbyEntities() {
        if(!dragon.isModelDead()){
            super.collideWithNearbyEntities();
        }
    }
}
