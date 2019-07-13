package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonHornStatic extends Item implements Interactable {

	public ItemDragonHornStatic() {
		this.maxStackSize = 1;
		this.setCreativeTab(IceAndFire.TAB_ITEMS);
		this.setTranslationKey("iceandfire.dragon_horn");
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

	@Override
	public boolean processInteract(EntityPlayer player, Entity entity, EnumHand hand, ItemStack stack) {
		if (entity instanceof EntityDragonBase) {
			EntityDragonBase dragon = (EntityDragonBase) entity;
			if (!dragon.world.isRemote && dragon.isOwner(player) && !dragon.isStoned()) {
				dragon.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 3, 1.25F);
				ItemStack stack1 = new ItemStack(dragon.isFire ? ModItems.dragon_horn_fire : ModItems.dragon_horn_ice);
				stack1.setTagCompound(new NBTTagCompound());
				dragon.writeEntityToNBT(stack1.getTagCompound());
				player.setHeldItem(hand, stack1);
				dragon.setDead();
				return true;
			}
		}
		return false;
	}
}
