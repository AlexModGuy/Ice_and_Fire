package com.github.alexthe666.iceandfire.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;

public class ItemDragonEgg extends Item {
	public EnumDragonEgg type;
	public ItemDragonEgg(String name, EnumDragonEgg type)
	{
		this.setHasSubtypes(true);
		this.setCreativeTab(IceAndFire.tab);
		this.type = type;
		this.setUnlocalizedName("iceandfire.dragonegg");
		this.maxStackSize = 1;
		GameRegistry.registerItem(this, name);
	}

	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		itemStack.setTagCompound(new NBTTagCompound());
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(type.color + StatCollector.translateToLocal("dragon." + type.toString().toLowerCase()));
	}

    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (side != EnumFacing.UP)
		{
			return EnumActionResult.PASS;
		}else{
			EntityDragonEgg egg = new EntityDragonEgg(worldIn);
			egg.setType(type);
			egg.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			if(!worldIn.isRemote){
				worldIn.spawnEntityInWorld(egg);
			}
			if (!player.capabilities.isCreativeMode)
			{
				--stack.stackSize;

				if (stack.stackSize <= 0)
				{
					player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
				}
			}
			return EnumActionResult.SUCCESS;

		}
	}
}
