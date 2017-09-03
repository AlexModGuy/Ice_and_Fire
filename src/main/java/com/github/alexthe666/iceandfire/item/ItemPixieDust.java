package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemPixieDust extends ItemFood {

    public ItemPixieDust() {
        super(1, 0.3F, false);
        this.setCreativeTab(IceAndFire.TAB);
        this.setUnlocalizedName("iceandfire.pixie_dust");
        this.setRegistryName(IceAndFire.MODID, "pixie_dust");
        GameRegistry.register(this);
    }

    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 100, 1));
        player.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 100, 1));
    }
}
