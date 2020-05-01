package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonScales extends Item {
    EnumDragonEgg type;

    public ItemDragonScales(String name, EnumDragonEgg type) {
        this.setHasSubtypes(true);
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.type = type;
        this.setTranslationKey("iceandfire.dragonscales");
        this.setRegistryName(IceAndFire.MODID, name);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(type.color + StatCollector.translateToLocal("dragon." + type.toString().toLowerCase()));
    }

}
