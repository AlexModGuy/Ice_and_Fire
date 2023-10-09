package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.citadel.server.item.CustomArmorMaterial;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;

public class IafArmorMaterial extends CustomArmorMaterial {

    protected static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
    private final int maxDamageFactor;

    public IafArmorMaterial(String name, int durability, int[] damageReduction, int encantability, SoundEvent sound, float toughness) {
        super(name, durability, damageReduction, encantability, sound, toughness, 0);
        this.maxDamageFactor = durability;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getSlot().getIndex()] * this.maxDamageFactor;
    }

    @Override
    public float getKnockbackResistance(){
        return 0;
    }
}
