package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.FrozenEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;

public class ItemModAxe extends ItemAxe {

    public ItemModAxe(ToolMaterial toolmaterial, String gameName, String name) {
        super(ToolMaterial.DIAMOND);
        this.toolMaterial = toolmaterial;
        this.attackDamage = toolmaterial.getAttackDamage() + 5;
        this.attackSpeed = -3;
        this.setTranslationKey(name);
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setRegistryName(IceAndFire.MODID, gameName);
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        ItemStack mat = this.toolMaterial.getRepairItemStack();
        if (this.toolMaterial == IaFItemRegistry.silverTools) {
            NonNullList<ItemStack> silverItems = OreDictionary.getOres("ingotSilver");
            for (ItemStack ingot : silverItems) {
                if (OreDictionary.itemMatches(repair, ingot, false)) {
                    return true;
                }
            }
        }
        if (!mat.isEmpty() && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (this == IaFItemRegistry.silver_axe) {
            if (target.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD) {
                target.attackEntityFrom(DamageSource.causeMobDamage(attacker), attackDamage + 3.0F);
            }
        }
        if (this.toolMaterial == IaFItemRegistry.myrmexChitin) {
            if (target.getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD) {
                target.attackEntityFrom(DamageSource.GENERIC, attackDamage + 6.0F);
            }
            if (target instanceof EntityDeathWorm) {
                target.attackEntityFrom(DamageSource.GENERIC, attackDamage + 6.0F);
            }
        }
        if (toolMaterial == IaFItemRegistry.dragonsteel_fire_tools) {
            target.setFire(15);
            target.knockBack(target, 1F, attacker.posX - target.posX, attacker.posZ - target.posZ);
        }
        if (toolMaterial == IaFItemRegistry.dragonsteel_ice_tools) {
            FrozenEntityProperties frozenProps = EntityPropertiesHandler.INSTANCE.getProperties(target, FrozenEntityProperties.class);
            frozenProps.setFrozenFor(300);
            target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 300, 2));
            target.knockBack(target, 1F, attacker.posX - target.posX, attacker.posZ - target.posZ);
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (this == IaFItemRegistry.silver_axe) {
            tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("silvertools.hurt"));
        }
        if (this == IaFItemRegistry.myrmex_desert_axe || this == IaFItemRegistry.myrmex_jungle_axe) {
            tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("myrmextools.hurt"));
        }
        if (this == IaFItemRegistry.dragonsteel_fire_sword) {
            tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("dragon_sword_fire.hurt2"));
        }
        if (this == IaFItemRegistry.dragonsteel_ice_sword) {
            tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("dragon_sword_ice.hurt2"));
        }
        if (toolMaterial == IaFItemRegistry.dragonsteel_fire_tools) {
            tooltip.add(TextFormatting.DARK_RED + StatCollector.translateToLocal("dragon_sword_fire.hurt2"));
        }
        if (toolMaterial == IaFItemRegistry.dragonsteel_ice_tools) {
            tooltip.add(TextFormatting.AQUA + StatCollector.translateToLocal("dragon_sword_ice.hurt2"));
        }
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getDestroySpeed(stack, state) : this.efficiency;
    }

    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker) {
        return true;
    }
}
