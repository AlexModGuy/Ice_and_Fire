package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.server.item.CustomToolMaterial;
import com.github.alexthe666.iceandfire.IafConfig;

public class DragonsteelToolMaterial extends CustomToolMaterial {

    public DragonsteelToolMaterial(String name, int harvestLevel, int durability, float damage, float speed, int enchantability) {
        super(name, harvestLevel, durability, damage, speed, enchantability);
    }

    public int getUses() {
        return IafConfig.dragonsteelBaseDurability;
    }

    public float getAttackDamageBonus() {
        return (float) IafConfig.dragonsteelBaseDamage - 4.0F;
    }

}
