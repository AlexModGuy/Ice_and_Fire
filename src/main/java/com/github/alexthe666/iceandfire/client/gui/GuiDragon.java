package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.inventory.ContainerDragon;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class GuiDragon extends EffectRenderingInventoryScreen<ContainerDragon> {
    private static final ResourceLocation texture = new ResourceLocation("iceandfire:textures/gui/dragon.png");
    private float mousePosx;
    private float mousePosY;

    public GuiDragon(ContainerDragon dragonInv, Inventory playerInv, Component name) {
        super(dragonInv, playerInv, name);
        this.imageHeight = 214;
    }

    @Override
    protected void renderLabels(@NotNull PoseStack matrixStack, int mouseX, int mouseY) {

    }

    @Override
    public void render(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.mousePosx = mouseX;
        this.mousePosY = mouseY;
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            float dragonScale = 1F / Math.max(0.0001F, dragon.getScale());
            InventoryScreen.renderEntityInInventory(k + 88, l + (int) (0.5F * (dragon.flyProgress)) + 55, (int) (dragonScale * 23F), k + 51 - this.mousePosx, l + 75 - 50 - this.mousePosY, dragon);
        }
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;

            Font font = this.getMinecraft().font;
            String s3 = dragon.getCustomName() == null ? StatCollector.translateToLocal("dragon.unnamed") : StatCollector.translateToLocal("dragon.name") + " " + dragon.getCustomName().getString();
            font.draw(matrixStack, s3, k + this.imageWidth / 2 - font.width(s3) / 2, l + 75, 0XFFFFFF);
            String s2 = StatCollector.translateToLocal("dragon.health") + " " + Math.floor(Math.min(dragon.getHealth(), dragon.getMaxHealth())) + " / " + dragon.getMaxHealth();
            font.draw(matrixStack, s2, k + this.imageWidth / 2 - font.width(s2) / 2, l + 84, 0XFFFFFF);
            String s5 = StatCollector.translateToLocal("dragon.gender") + StatCollector.translateToLocal((dragon.isMale() ? "dragon.gender.male" : "dragon.gender.female"));
            font.draw(matrixStack, s5, k + this.imageWidth / 2 - font.width(s5) / 2, l + 93, 0XFFFFFF);
            String s6 = StatCollector.translateToLocal("dragon.hunger") + dragon.getHunger() + "/100";
            font.draw(matrixStack, s6, k + this.imageWidth / 2 - font.width(s6) / 2, l + 102, 0XFFFFFF);
            String s4 = StatCollector.translateToLocal("dragon.stage") + " " + dragon.getDragonStage() + " " + StatCollector.translateToLocal("dragon.days.front") + dragon.getAgeInDays() + " " + StatCollector.translateToLocal("dragon.days.back");
            font.draw(matrixStack, s4, k + this.imageWidth / 2 - font.width(s4) / 2, l + 111, 0XFFFFFF);
            String s7 = dragon.getOwner() != null ? StatCollector.translateToLocal("dragon.owner") + dragon.getOwner().getName().getString() : StatCollector.translateToLocal("dragon.untamed");
            font.draw(matrixStack, s7, k + this.imageWidth / 2 - font.width(s7) / 2, l + 120, 0XFFFFFF);
        }
    }

    @Override
    protected void renderEffects(PoseStack pPoseStack, int pMouseX, int pMouseY) {

        int i = this.leftPos + this.imageWidth + 2;
        int j = this.width - i;
        if (IceAndFire.PROXY.getReferencedMob() instanceof LivingEntity livingEntity) {
            Collection<MobEffectInstance> collection = livingEntity.getActiveEffects();
            if (!collection.isEmpty() && j >= 32) {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                boolean flag = j >= 120;
                var event = net.minecraftforge.client.ForgeHooksClient.onScreenPotionSize(this);
                if (event != net.minecraftforge.eventbus.api.Event.Result.DEFAULT)
                    flag = event == net.minecraftforge.eventbus.api.Event.Result.DENY; // true means classic mode
                int k = 33;
                if (collection.size() > 5) {
                    k = 132 / (collection.size() - 1);
                }


                Iterable<MobEffectInstance> iterable = collection.stream().filter(net.minecraftforge.client.ForgeHooksClient::shouldRenderEffect).sorted().collect(java.util.stream.Collectors.toList());
                this.renderBackgrounds(pPoseStack, i, k, iterable, flag);
                this.renderIcons(pPoseStack, i, k, iterable, flag);
                if (flag) {
                    this.renderLabels(pPoseStack, i, k, iterable);
                } else if (pMouseX >= i && pMouseX <= i + 33) {
                    int l = this.topPos;
                    MobEffectInstance mobeffectinstance = null;

                    for (MobEffectInstance mobeffectinstance1 : iterable) {
                        if (pMouseY >= l && pMouseY <= l + k) {
                            mobeffectinstance = mobeffectinstance1;
                        }

                        l += k;
                    }

                    if (mobeffectinstance != null) {
                        List<Component> list = List.of(this.getEffectName(mobeffectinstance), new TextComponent(MobEffectUtil.formatDuration(mobeffectinstance, 1.0F)));
                        this.renderTooltip(pPoseStack, list, Optional.empty(), pMouseX, pMouseY);
                    }
                }

            }
        }
    }


}