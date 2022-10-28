package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.ChatFormatting;
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


public class BlockSeaSerpentScales extends Block {
    ChatFormatting color;
    String name;

    public BlockSeaSerpentScales(String name, ChatFormatting color) {
        super(
            Properties
                .of(Material.STONE)
                .strength(30F, 500F)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops()
        );

        this.color = color;
        this.name = name;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("sea_serpent." + name).withStyle(color));
    }
}
