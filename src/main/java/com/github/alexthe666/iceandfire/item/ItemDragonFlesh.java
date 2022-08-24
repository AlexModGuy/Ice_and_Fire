package com.github.alexthe666.iceandfire.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class ItemDragonFlesh extends ItemGenericFood {

    int dragonType;

    public ItemDragonFlesh(int dragonType) {
        super(8, 0.8F, true, false, false, getNameForType(dragonType));
        this.dragonType = dragonType;
    }

    private static String getNameForType(int dragonType) {
        switch (dragonType){
            case 0:
                return "fire_dragon_flesh";
            case 1:
                return "ice_dragon_flesh";
            case 2:
                return "lightning_dragon_flesh";
        }
        return "fire_dragon_flesh";
    }

    public void onFoodEaten(ItemStack stack, World worldIn, LivingEntity livingEntity) {
        if (!worldIn.isClientSide) {
            if (dragonType == 0) {
                livingEntity.setSecondsOnFire(5);
            } else if (dragonType == 1) {
                livingEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 2));
            } else {
                if (!livingEntity.level.isClientSide) {
                    LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(livingEntity.level);
                    lightningboltentity.moveTo(livingEntity.position());
                    if (!livingEntity.level.isClientSide) {
                        livingEntity.level.addFreshEntity(lightningboltentity);
                    }
                }
            }
        }
    }
}
