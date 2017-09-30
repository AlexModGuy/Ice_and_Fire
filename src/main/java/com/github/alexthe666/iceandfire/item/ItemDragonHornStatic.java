package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonHornStatic extends Item {

	public ItemDragonHornStatic() {
		this.maxStackSize = 1;
		this.setCreativeTab(IceAndFire.TAB);
		this.setUnlocalizedName("iceandfire.dragon_horn");
		this.setRegistryName(IceAndFire.MODID, "dragon_horn");
	}

	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		itemStack.setTagCompound(new NBTTagCompound());
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
	}
}
