package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class ModRecipes {

    public static void init() {
        OreDictionary.registerOre("ingotSilver", ModItems.silverIngot);
        OreDictionary.registerOre("nuggetSilver", ModItems.silverNugget);
        OreDictionary.registerOre("oreSilver", ModBlocks.silverOre);
        OreDictionary.registerOre("blockSilver", ModBlocks.silverBlock);
        OreDictionary.registerOre("gemSapphire", ModItems.sapphireGem);
        OreDictionary.registerOre("oreSapphire", ModBlocks.sapphireOre);
        OreDictionary.registerOre("blockSilver", ModBlocks.silverBlock);
        OreDictionary.registerOre("boneWither", ModItems.witherbone);
        OreDictionary.registerOre("woolBlock", new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE));
        OreDictionary.registerOre("foodMeat", Items.CHICKEN);
        OreDictionary.registerOre("foodMeat", Items.COOKED_CHICKEN);
        OreDictionary.registerOre("foodMeat", Items.BEEF);
        OreDictionary.registerOre("foodMeat", Items.COOKED_BEEF);
        OreDictionary.registerOre("foodMeat", Items.PORKCHOP);
        OreDictionary.registerOre("foodMeat", Items.COOKED_PORKCHOP);
        OreDictionary.registerOre("foodMeat", Items.MUTTON);
        OreDictionary.registerOre("foodMeat", Items.COOKED_MUTTON);
        OreDictionary.registerOre("foodMeat", Items.RABBIT);
        OreDictionary.registerOre("foodMeat", Items.COOKED_RABBIT);
        OreDictionary.registerOre("boneWithered", ModItems.witherbone);        

        addBanner("firedragon", new ItemStack(ModItems.dragon_skull, 1, 0));
        addBanner("icedragon", new ItemStack(ModItems.dragon_skull, 1, 1));
        GameRegistry.addSmelting(ModBlocks.silverOre, new ItemStack(ModItems.silverIngot), 1);
        GameRegistry.addSmelting(ModBlocks.sapphireOre, new ItemStack(ModItems.sapphireGem), 1);
        ModItems.blindfoldArmor.setRepairItem(new ItemStack(Items.STRING));
        ModItems.silverMetal.setRepairItem(new ItemStack(ModItems.silverIngot));
        ModItems.silverTools.setRepairItem(new ItemStack(ModItems.silverIngot));
        ModItems.boneTools.setRepairItem(new ItemStack(ModItems.witherbone));
        ModItems.fireBoneTools.setRepairItem(new ItemStack(ModItems.witherbone));
        ModItems.iceBoneTools.setRepairItem(new ItemStack(ModItems.witherbone));
        for(EnumDragonArmor armor : EnumDragonArmor.values()){
            armor.armorMaterial.setRepairItem(new ItemStack(EnumDragonArmor.getScaleItem(armor)));
        }
        ModItems.sheep.setRepairItem(new ItemStack(Blocks.WOOL));
        ModItems.earplugsArmor.setRepairItem(new ItemStack(Blocks.WOODEN_BUTTON));
        ModItems.yellow_deathworm.setRepairItem(new ItemStack(ModItems.deathworm_chitin, 1, 0));
        ModItems.white_deathworm.setRepairItem(new ItemStack(ModItems.deathworm_chitin, 1, 1));
        ModItems.red_deathworm.setRepairItem(new ItemStack(ModItems.deathworm_chitin, 1, 2));
        ModItems.trollWeapon.setRepairItem(new ItemStack(Blocks.STONE));
        ModItems.troll_mountain.setRepairItem(new ItemStack(EnumTroll.MOUNTAIN.leather));
        ModItems.troll_forest.setRepairItem(new ItemStack(EnumTroll.FOREST.leather));
        ModItems.troll_frost.setRepairItem(new ItemStack(EnumTroll.FROST.leather));


    }

    public static BannerPattern addBanner(String name, ItemStack craftingStack) {
        Class<?>[] classes = {String.class, String.class, ItemStack.class};
        Object[] names = {name, "iceandfire." + name, craftingStack};
        return EnumHelper.addEnum(BannerPattern.class, name.toUpperCase(), classes, names);
    }

    public static void postInit() {
        NonNullList<ItemStack> bronzeItems = OreDictionary.getOres("nuggetBronze");
        NonNullList<ItemStack> copperItems = OreDictionary.getOres("nuggetCopper");
        if (!bronzeItems.isEmpty()) {
            for (ItemStack bronzeIngot : bronzeItems) {
                if (bronzeIngot != ItemStack.EMPTY) {
                    ItemStack stack = bronzeIngot.copy();
                    GameRegistry.addSmelting(ModItems.stymphalian_bird_feather, bronzeIngot.copy(), 1);

                    break;
                }
            }
        } else if (!copperItems.isEmpty()) {
            for (ItemStack copperIngot : copperItems) {
                if (copperIngot != ItemStack.EMPTY) {
                    GameRegistry.addSmelting(ModItems.stymphalian_bird_feather, copperIngot.copy(), 1);
                    break;
                }
            }
        }
    }
}
