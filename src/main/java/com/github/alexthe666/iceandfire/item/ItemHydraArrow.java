package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityHydraArrow;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemHydraArrow extends ArrowItem {

    public ItemHydraArrow() {
        super(new Item.Properties().group(IceAndFire.TAB_ITEMS));
        this.setRegistryName(IceAndFire.MODID, "hydra_arrow");
    }

    public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
        return new EntityHydraArrow(IafEntityRegistry.HYDRA_ARROW, worldIn, shooter);
    }

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.hydra_arrow.desc").func_240699_a_(TextFormatting.GRAY));
    }
}
