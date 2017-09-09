package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemAmbrosia extends ItemFood {

	public ItemAmbrosia() {
		super(5, 0.6F, false);
		this.setCreativeTab(IceAndFire.TAB);
		this.setUnlocalizedName("iceandfire.ambrosia");
		this.setRegistryName(IceAndFire.MODID, "ambrosia");
		this.setMaxStackSize(1);
		GameRegistry.register(this);
	}

	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 3600, 2));
		player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 3600, 2));
		player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 3600, 2));
		player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 3600, 2));
	}

	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		super.onItemUseFinish(stack, worldIn, entityLiving);
		return new ItemStack(Items.BOWL);
	}
}
