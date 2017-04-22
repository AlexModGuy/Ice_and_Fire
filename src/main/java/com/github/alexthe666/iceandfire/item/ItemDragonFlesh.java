package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.registry.*;

public class ItemDragonFlesh extends ItemFood {

	boolean isFire;

	public ItemDragonFlesh (boolean isFire) {
		super (8, 0.8F, true);
		this.setCreativeTab (IceAndFire.TAB);
		this.setUnlocalizedName (isFire ? "iceandfire.fire_dragon_flesh" : "iceandfire.ice_dragon_flesh");
		GameRegistry.registerItem (this, isFire ? "fire_dragon_flesh" : "ice_dragon_flesh");
		this.isFire = isFire;
	}

	protected void onFoodEaten (ItemStack stack, World worldIn, EntityPlayer player) {
		if (!worldIn.isRemote) {
			if (isFire) {
				player.setFire (5);
			} else {
				player.addPotionEffect (new PotionEffect (MobEffects.SLOWNESS, 100, 2));
			}
		}
	}
}
