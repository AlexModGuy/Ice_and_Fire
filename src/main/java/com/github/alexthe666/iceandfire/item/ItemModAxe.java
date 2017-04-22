package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.*;
import com.github.alexthe666.iceandfire.client.*;
import com.github.alexthe666.iceandfire.core.*;
import com.google.common.collect.*;
import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.registry.*;

import java.util.*;

public class ItemModAxe extends ItemTool {
	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet (new Block[]{Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.OAK_DOOR, Blocks.WOODEN_PRESSURE_PLATE});

	public ItemModAxe (ToolMaterial toolmaterial, String gameName, String name) {
		super (toolmaterial, EFFECTIVE_ON);
		this.damageVsEntity = toolmaterial == ModItems.boneTools ? 8 : 6;
		this.attackSpeed = -3;
		this.setUnlocalizedName (name);
		this.setCreativeTab (IceAndFire.TAB);
		GameRegistry.registerItem (this, gameName);
	}

	@Override
	public boolean hitEntity (ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (this == ModItems.silver_axe) {
			if (target.getCreatureAttribute () == EnumCreatureAttribute.UNDEAD) {
				target.attackEntityFrom (DamageSource.magic, 2);
			}
		}
		return super.hitEntity (stack, target, attacker);
	}

	@Override
	public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean f) {
		if (this == ModItems.silver_axe)
			list.add (TextFormatting.GREEN + StatCollector.translateToLocal ("silvertools.hurt"));
	}

	@Override
	public float getStrVsBlock (ItemStack stack, IBlockState state) {
		Material material = state.getMaterial ();
		return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getStrVsBlock (stack, state) : this.efficiencyOnProperMaterial;
	}
}
