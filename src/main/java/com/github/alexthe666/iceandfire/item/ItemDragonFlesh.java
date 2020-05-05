package com.github.alexthe666.iceandfire.item;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class ItemDragonFlesh extends ItemGenericFood {

    boolean isFire;

    public ItemDragonFlesh(boolean isFire) {
        super(8, 0.8F, true, false, false, isFire ? "fire_dragon_flesh" : "ice_dragon_flesh");
        this.isFire = isFire;
    }

    public void onFoodEaten(ItemStack stack, World worldIn, LivingEntity livingEntity) {
        if (!worldIn.isRemote) {
            if (isFire) {
                livingEntity.setFire(5);
            } else {
                livingEntity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 100, 2));
            }
        }
    }
}
