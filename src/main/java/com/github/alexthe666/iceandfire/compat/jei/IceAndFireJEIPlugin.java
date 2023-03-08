package com.github.alexthe666.iceandfire.compat.jei;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.compat.jei.firedragonforge.FireDragonForgeCategory;
import com.github.alexthe666.iceandfire.compat.jei.icedragonforge.IceDragonForgeCategory;
import com.github.alexthe666.iceandfire.compat.jei.lightningdragonforge.LightningDragonForgeCategory;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class IceAndFireJEIPlugin implements IModPlugin {

    public static final ResourceLocation MOD = new ResourceLocation("iceandfire:iceandfire");
    public static final ResourceLocation FIRE_DRAGON_FORGE_ID = new ResourceLocation("iceandfire:fire_dragon_forge");
    public static final ResourceLocation ICE_DRAGON_FORGE_ID = new ResourceLocation("iceandfire:ice_dragon_forge");
    public static final ResourceLocation LIGHTNING_DRAGON_FORGE_ID = new ResourceLocation("iceandfire:lightning_dragon_forge");

    private void addDescription(IRecipeRegistration registry, ItemStack itemStack) {
        registry.addIngredientInfo(itemStack, VanillaTypes.ITEM, new TranslatableComponent(itemStack.getDescriptionId() + ".jei_desc"));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        List<DragonForgeRecipe> forgeRecipeList = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(IafRecipeRegistry.DRAGON_FORGE_TYPE);

        List<DragonForgeRecipe> fire = forgeRecipeList.stream().filter(item -> item.getDragonType().equals("fire")).collect(Collectors.toList());
        List<DragonForgeRecipe> ice = forgeRecipeList.stream().filter(item -> item.getDragonType().equals("ice")).collect(Collectors.toList());
        List<DragonForgeRecipe> lightning = forgeRecipeList.stream().filter(item -> item.getDragonType().equals("lightning")).collect(Collectors.toList());

        registry.addRecipes(fire, FIRE_DRAGON_FORGE_ID);
        registry.addRecipes(ice, ICE_DRAGON_FORGE_ID);
        registry.addRecipes(lightning, LIGHTNING_DRAGON_FORGE_ID);

        addDescription(registry, new ItemStack(IafItemRegistry.FIRE_DRAGON_BLOOD.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.ICE_DRAGON_BLOOD.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.LIGHTNING_DRAGON_BLOOD.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_RED.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_BRONZE.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_GRAY.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_GREEN.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_BLUE.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_WHITE.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_SAPPHIRE.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_SILVER.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_ELECTRIC.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_AMYTHEST.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_COPPER.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGONEGG_BLACK.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGON_SKULL_FIRE.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGON_SKULL_ICE.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.DRAGON_SKULL_LIGHTNING.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.FIRE_STEW.get()));
        addDescription(registry, new ItemStack(IafItemRegistry.FROST_STEW.get()));

        for (EnumSkullType skull : EnumSkullType.values()) {
            addDescription(registry, new ItemStack(skull.skull_item.get()));
        }

        registry.addIngredientInfo(IafItemRegistry.PATTERN_FIRE.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_ICE.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_LIGHTNING.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_FIRE_HEAD.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_ICE_HEAD.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_LIGHTNING_HEAD.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_AMPHITHERE.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_BIRD.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_EYE.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_FAE.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_FEATHER.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_GORGON.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_HIPPOCAMPUS.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_HIPPOGRYPH_HEAD.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_MERMAID.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_SEA_SERPENT.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_TROLL.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_WEEZER.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
        registry.addIngredientInfo(IafItemRegistry.PATTERN_DREAD.get().getDefaultInstance(), VanillaTypes.ITEM_STACK, new TranslatableComponent("item.iceandfire.custom_banner.jei_desc"));
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new FireDragonForgeCategory());
        registry.addRecipeCategories(new IceDragonForgeCategory());
        registry.addRecipeCategories(new LightningDragonForgeCategory());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(new ItemStack(IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get()), FIRE_DRAGON_FORGE_ID);
        registry.addRecipeCatalyst(new ItemStack(IafBlockRegistry.DRAGONFORGE_ICE_CORE.get()), ICE_DRAGON_FORGE_ID);
        registry.addRecipeCatalyst(new ItemStack(IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.get()), LIGHTNING_DRAGON_FORGE_ID);
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return MOD;
    }

}
