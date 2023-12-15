package com.github.alexthe666.iceandfire.entity;

import net.minecraft.world.entity.EntityType;

public class DragonType {
    public static final DragonType FIRE = new DragonType("fire");
    public static final DragonType ICE = new DragonType("ice").setPiscivore();
    public static final DragonType LIGHTNING = new DragonType("lightning");

    private String name;
    private boolean piscivore;

    public DragonType(String name) {
        this.name = name;
    }

    public static String getNameFromInt(int type){
        if(type == 2){
            return "lightning";
        }else if (type == 1){
            return "ice";
        }else{
            return "fire";
        }
    }

    public int getIntFromType() {
        if (this == LIGHTNING) {
            return 2;
        } else if (this == ICE) {
            return 1;
        } else {
            return 0;
        }
    }

    public EntityType<? extends EntityDragonBase> getEntity() {
        if (this == LIGHTNING) {
            return IafEntityRegistry.LIGHTNING_DRAGON.get();
        } else if (this == ICE) {
            return IafEntityRegistry.ICE_DRAGON.get();
        }

        return IafEntityRegistry.FIRE_DRAGON.get();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPiscivore() {
        return piscivore;
    }

    public DragonType setPiscivore() {
        piscivore = true;
        return this;
    }
}
