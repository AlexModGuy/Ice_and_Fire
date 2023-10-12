package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class IafTabRegistry {

    public static final DeferredRegister<CreativeModeTab> TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, IceAndFire.MODID);

    public static final List<Supplier<? extends Block>> TAB_BLOCKS_LIST = new ArrayList<>();
    public static final List<Supplier<? extends Item>> TAB_ITEMS_LIST = new ArrayList<>();
    public static final RegistryObject<CreativeModeTab> TAB_BLOCKS = TAB_REGISTER.register("blocks", () -> CreativeModeTab.builder()
            // Set name of tab to display
            .title(Component.translatable("itemGroup." + IceAndFire.MODID + ".blocks"))
            // Set icon of creative tab
            .icon(() -> new ItemStack(IafBlockRegistry.DRAGON_SCALE_RED.get()))
            // properly order tab after spawn egg tab
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            // Add default items to tab
            .displayItems((params, output) -> {
                TAB_BLOCKS_LIST.forEach(block -> output.accept(block.get()));
            })
            .build()
    );

    public static final RegistryObject<CreativeModeTab> TAB_ITEMS = TAB_REGISTER.register("items", () -> CreativeModeTab.builder()
            // Set name of tab to display
            .title(Component.translatable("itemGroup." + IceAndFire.MODID + ".items"))
            // Set icon of creative tab
            .icon(() -> new ItemStack(IafItemRegistry.DRAGON_SKULL_FIRE.get()))
            // properly order tab after block tab
            .withTabsBefore(TAB_BLOCKS.getKey())
            // Add default items to tab
            .displayItems((params, output) -> {
                TAB_ITEMS_LIST.forEach(block -> output.accept(block.get()));
            })
            .build()
    );
}
