package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemDragonFlesh extends ItemFood {

	boolean isFire;

	public ItemDragonFlesh(boolean isFire) {
		super(8, 0.8F, true);
		this.setCreativeTab(IceAndFire.TAB);
		this.setTranslationKey(isFire ? "iceandfire.fire_dragon_flesh" : "iceandfire.ice_dragon_flesh");
		this.setRegistryName(IceAndFire.MODID, isFire ? "fire_dragon_flesh" : "ice_dragon_flesh");
		this.isFire = isFire;
	}

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		if (!worldIn.isRemote) {
			if (isFire) {
				player.setFire(5);
			} else {
				player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 2));
			}
		}
	}
}
