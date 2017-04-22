package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.core.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

public class ItemModPickaxe extends ItemPickaxe {

    public ItemModPickaxe(ToolMaterial toolmaterial, String gameName, String name) {
        super(toolmaterial);
        this.setUnlocalizedName(name);
        this.setCreativeTab(IceAndFire.TAB);
        GameRegistry.registerItem(this, gameName);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (this == ModItems.silver_pickaxe) {
            if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                target.attackEntityFrom(DamageSource.MAGIC, 2);
            }
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean f) {
        if (this == ModItems.silver_pickaxe)
            list.add(TextFormatting.GREEN + StatCollector.translateToLocal("silvertools.hurt"));
    }
}
