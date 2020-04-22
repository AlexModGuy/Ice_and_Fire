package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemHippocampusSlapper extends ItemSword {

    public ItemHippocampusSlapper() {
        super(IafItemRegistry.hippocampus_sword_tools);
        this.setTranslationKey("iceandfire.hippocampus_slapper");
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setRegistryName(IceAndFire.MODID, "hippocampus_slapper");
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase targetEntity, EntityLivingBase attacker) {
        targetEntity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 2));
        targetEntity.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 100, 2));
        targetEntity.playSound(SoundEvents.ENTITY_GUARDIAN_FLOP, 3, 1);

        return super.hitEntity(stack, targetEntity, attacker);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("item.iceandfire.legendary_weapon.desc"));
        tooltip.add(I18n.format("item.iceandfire.hippocampus_slapper.desc_0"));
        tooltip.add(I18n.format("item.iceandfire.hippocampus_slapper.desc_1"));
    }
}