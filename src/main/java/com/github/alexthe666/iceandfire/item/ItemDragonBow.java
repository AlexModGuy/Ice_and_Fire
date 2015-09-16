package com.github.alexthe666.iceandfire.item;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;

public class ItemDragonBow extends Item {


	public ItemDragonBow(){
		this.maxStackSize = 1;
		this.setMaxDamage(584);
		this.setCreativeTab(IceAndFire.tab);
		this.setUnlocalizedName("iceandfire.dragonbone_bow");
		GameRegistry.registerItem(this, "dragonbone_bow");
	}

	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft)
	{
		int j = this.getMaxItemUseDuration(stack) - timeLeft;
		net.minecraftforge.event.entity.player.ArrowLooseEvent event = new net.minecraftforge.event.entity.player.ArrowLooseEvent(playerIn, stack, j);
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return;
		j = event.charge;

		boolean flag = playerIn.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;

		if (flag || playerIn.inventory.hasItem(ModItems.dragonbone_arrow))
		{
			float f = (float)j / 20.0F;
			f = (f * f + f * 2.0F) / 3.0F;

			if ((double)f < 0.1D)
			{
				return;
			}

			if (f > 1.0F)
			{
				f = 1.0F;
			}

			EntityDragonArrow entityarrow = new EntityDragonArrow(worldIn, playerIn, f * 2.0F);

			if (f == 1.0F)
			{
				entityarrow.setIsCritical(true);
			}

			int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);

			if (k > 0)
			{
				entityarrow.setDamage(entityarrow.getDamage() + (double)k * 0.5D + 0.5D);
			}

			int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);

			if (l > 0)
			{
				entityarrow.setKnockbackStrength(l);
			}

			if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0)
			{
				entityarrow.setFire(100);
			}

			stack.damageItem(1, playerIn);
			worldIn.playSoundAtEntity(playerIn, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

			if (flag)
			{
				entityarrow.canBePickedUp = 2;
			}
			else
			{
				playerIn.inventory.consumeInventoryItem(ModItems.dragonbone_arrow);
			}

			playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);

			if (!worldIn.isRemote)
			{
				worldIn.spawnEntityInWorld(entityarrow);
			}
		}
	}

	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
	{
		ModelResourceLocation modelresourcelocation = new ModelResourceLocation("iceandfire:dragonbone_bow", "inventory");
		int ticksInUse = stack.getMaxItemUseDuration() - useRemaining;
		if(stack.getItem() == this && player.getItemInUse() != null)
		{
			if(ticksInUse > 17)
			{
				modelresourcelocation = new ModelResourceLocation("iceandfire:dragonbone_bow_pulling_2", "inventory");
			}
			else if(ticksInUse > 13)
			{
				modelresourcelocation = new ModelResourceLocation("iceandfire:dragonbone_bow_pulling_1", "inventory");
			}
			else if(ticksInUse > 0)
			{
				modelresourcelocation = new ModelResourceLocation("iceandfire:dragonbone_bow_pulling_0", "inventory");
			}
		}
		return modelresourcelocation;
	}

	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn)
	{
		return stack;
	}

	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 72000;
	}

	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}

	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
	{
		net.minecraftforge.event.entity.player.ArrowNockEvent event = new net.minecraftforge.event.entity.player.ArrowNockEvent(playerIn, itemStackIn);
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return event.result;

		if (playerIn.capabilities.isCreativeMode || playerIn.inventory.hasItem(ModItems.dragonbone_arrow))
		{
			playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		}

		return itemStackIn;
	}

	public int getItemEnchantability()
	{
		return 1;
	}
}
