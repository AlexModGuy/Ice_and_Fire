package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.EntityHippogryphEgg;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class ItemHippogryphEgg extends Item {

	public ItemHippogryphEgg() {
		this.setCreativeTab(IceAndFire.TAB);
		this.setUnlocalizedName("iceandfire.hippogryph_egg");
		this.setRegistryName(IceAndFire.MODID, "hippogryph_egg");
		GameRegistry.register(this);
	}

	public static ItemStack createEggStack(EnumHippogryphTypes parent1, EnumHippogryphTypes parent2) {
		EnumHippogryphTypes eggType = new Random().nextBoolean() ? parent1 : parent2;
		ItemStack stack = new ItemStack(ModItems.hippogryph_egg);
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("Type", eggType.ordinal());
		stack.setTagCompound(nbt);
		return stack;
	}

	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		itemStack.setTagCompound(new NBTTagCompound());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		for (EnumHippogryphTypes type : EnumHippogryphTypes.values()) {
			if (!type.developer) {
				subItems.add(createEggStack(type, type));
			}
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

		if (!playerIn.capabilities.isCreativeMode) {
			itemstack.shrink(1);
		}

		worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if (!worldIn.isRemote) {
			EntityHippogryphEgg entityegg = new EntityHippogryphEgg(worldIn, playerIn, itemstack);
			entityegg.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
			worldIn.spawnEntity(entityegg);
		}

		playerIn.addStat(StatList.getObjectUseStats(this));
		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean f) {
		if (stack.getTagCompound() != null) {
			String type = EnumHippogryphTypes.values()[stack.getTagCompound().getInteger("Type")].name().toLowerCase();
			list.add(StatCollector.translateToLocal("entity.hippogryph." + type));
		}
	}
}
