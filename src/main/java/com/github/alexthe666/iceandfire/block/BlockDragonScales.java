package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;
import java.util.List;

public class BlockDragonScales extends Block implements IDragonProof {
    EnumDragonEgg type;

    public BlockDragonScales(EnumDragonEgg type) {
        super(
            Properties
                .of(Material.STONE)
                .dynamicShape()
                .strength(30F, 500)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops()
        );

        this.type = type;
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("dragon." + type.toString().toLowerCase()).withStyle(type.color));
    }
}
