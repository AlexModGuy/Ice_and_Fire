package com.github.alexthe666.iceandfire.item;

import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.core.ModItems;
import com.google.common.collect.Sets;

public class ItemModAxe extends ItemTool {
	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] {Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder, Blocks.wooden_button, Blocks.wooden_pressure_plate});
	private static final float[] ATTACK_DAMAGES = new float[] {6.0F, 8.0F, 8.0F, 8.0F, 6.0F, 8.0F};
	private static final float[] ATTACK_SPEEDS = new float[] { -3.2F, -3.2F, -3.1F, -3.0F, -3.0F, -3.0F};

	public ItemModAxe(ToolMaterial toolmaterial, String gameName, String name){
		super(toolmaterial, EFFECTIVE_ON);
		this.damageVsEntity = ATTACK_DAMAGES[toolmaterial.ordinal() - 1];
		this.attackSpeed = ATTACK_SPEEDS[toolmaterial.ordinal() - 1];	
		this.setUnlocalizedName(name);
		this.setCreativeTab(IceAndFire.tab);
		GameRegistry.registerItem(this, gameName);
	}
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
	{
		if(this == ModItems.silver_axe){
			if(target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD){
				target.attackEntityFrom(DamageSource.magic, 2);
			}
		}
		return super.hitEntity(stack, target, attacker);
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean f) {
		if(this == ModItems.silver_axe)
			list.add(TextFormatting.GREEN + StatCollector.translateToLocal("silvertools.hurt"));
	}

	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		Material material = state.getMaterial();
		return material != Material.wood && material != Material.plants && material != Material.vine ? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
	}
}
