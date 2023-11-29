package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.inventory.ContainerDragonForge;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.stream.Collectors;

public class GuiDragonForge extends AbstractContainerScreen<ContainerDragonForge> {
    private static final ResourceLocation TEXTURE_FIRE = new ResourceLocation("iceandfire:textures/gui/dragonforge_fire.png");
    private static final ResourceLocation TEXTURE_ICE = new ResourceLocation("iceandfire:textures/gui/dragonforge_ice.png");
    private static final ResourceLocation TEXTURE_LIGHTNING = new ResourceLocation("iceandfire:textures/gui/dragonforge_lightning.png");
    private final ContainerDragonForge tileFurnace;
    private final int dragonType;

    public GuiDragonForge(ContainerDragonForge container, Inventory inv, Component name) {
        super(container, inv, name);
        this.tileFurnace = container;
        this.dragonType = tileFurnace.fireType;
    }

    @Override
    protected void renderLabels(PoseStack ms, int mouseX, int mouseY) {
        Font font = this.getMinecraft().font;
        if (tileFurnace != null) {
            String s = I18n.get("block.iceandfire.dragonforge_" + DragonType.getNameFromInt(dragonType) + "_core");
            Screen.drawString(ms, this.font, s, this.imageWidth / 2 - font.width(s) / 2, 6, 4210752/*, false*/);
        }
        Screen.drawString(ms, this.font, this.playerInventoryTitle, 8, this.imageHeight - 96 + 2, 4210752/*, false*/);
    }

    @Override
    protected void renderBg(PoseStack ms, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation texture = TEXTURE_FIRE;
        if (dragonType == 0) {
            texture = TEXTURE_FIRE;
        } else if (dragonType == 1) {
            texture = TEXTURE_ICE;
        } else {
            texture = TEXTURE_LIGHTNING;
        }

        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        blit(ms, k, l, 0, 0, this.imageWidth, this.imageHeight);
        int i1 = this.getCookTime(126);
        blit(ms, k + 12, l + 23, 0, 166, i1, 38);
    }

    private int getCookTime(int p_175381_1_) {
        BlockEntity te = IceAndFire.PROXY.getRefrencedTE();
        int j = 0;

        List<DragonForgeRecipe> recipes = this.getMinecraft().level.getRecipeManager()
                .getAllRecipesFor(IafRecipeRegistry.DRAGON_FORGE_TYPE.get())
                .stream().filter(item ->
                        item.isValidInput(tileFurnace.getSlot(0).getItem()) && item.isValidBlood(tileFurnace.getSlot(1).getItem())).collect(Collectors.toList());
        int maxCookTime = recipes.isEmpty() ? 100 : recipes.get(0).getCookTime();
        if (te instanceof TileEntityDragonforge) {
            j = Math.min(((TileEntityDragonforge) te).cookTime, maxCookTime);
        }
        return j != 0 ? j * p_175381_1_ / maxCookTime : 0;
    }

    @Override
    public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderTooltip(ms, mouseX, mouseY);
    }

}