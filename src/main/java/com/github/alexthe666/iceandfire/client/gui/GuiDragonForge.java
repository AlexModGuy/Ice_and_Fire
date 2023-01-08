package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.inventory.ContainerDragonForge;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.stream.Collectors;

public class GuiDragonForge extends ContainerScreen<ContainerDragonForge> {
    private static final ResourceLocation TEXTURE_FIRE = new ResourceLocation("iceandfire:textures/gui/dragonforge_fire.png");
    private static final ResourceLocation TEXTURE_ICE = new ResourceLocation("iceandfire:textures/gui/dragonforge_ice.png");
    private static final ResourceLocation TEXTURE_LIGHTNING = new ResourceLocation("iceandfire:textures/gui/dragonforge_lightning.png");
    private final ContainerDragonForge tileFurnace;
    private final int dragonType;

    public GuiDragonForge(ContainerDragonForge container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.tileFurnace = container;
        this.dragonType = tileFurnace.fireType;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        FontRenderer font = this.getMinecraft().fontRenderer;
        if (tileFurnace != null) {
            String s = I18n.format("block.iceandfire.dragonforge_" + DragonType.getNameFromInt(dragonType) + "_core");
            font.drawString(stack, s, this.xSize / 2 - font.getStringWidth(s) / 2, 6, 4210752);
        }
        font.drawString(stack, this.playerInventory.getDisplayName().getString(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (dragonType == 0) {
            this.getMinecraft().getTextureManager().bindTexture(TEXTURE_FIRE);
        } else if (dragonType == 1) {
            this.getMinecraft().getTextureManager().bindTexture(TEXTURE_ICE);
        } else {
            this.getMinecraft().getTextureManager().bindTexture(TEXTURE_LIGHTNING);
        }
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.blit(matrixStack, k, l, 0, 0, this.xSize, this.ySize);
        int i1 = this.getCookTime(126);
        this.blit(matrixStack, k + 12, l + 23, 0, 166, i1, 38);
    }

    private int getCookTime(int p_175381_1_) {
        TileEntity te = IceAndFire.PROXY.getRefrencedTE();
        int j = 0;

        List<DragonForgeRecipe> recipes = this.getMinecraft().world.getRecipeManager()
            .getRecipesForType(IafRecipeRegistry.DRAGON_FORGE_TYPE)
            .stream().filter(item ->
                item.isValidInput(tileFurnace.getSlot(0).getStack()) && item.isValidBlood(tileFurnace.getSlot(1).getStack())).collect(Collectors.toList());
        int maxCookTime = recipes.isEmpty() ? 100 : recipes.get(0).getCookTime();
        if (te instanceof TileEntityDragonforge) {
            j = Math.min(((TileEntityDragonforge) te).cookTime, maxCookTime);
        }
        return j != 0 ? j * p_175381_1_ / maxCookTime : 0;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

}