package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.inventory.ContainerDragon;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiDragon extends AbstractContainerScreen<ContainerDragon> {
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
        int leftPadding = (this.width - this.imageWidth) / 2; //
        int topPadding = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, leftPadding, topPadding, 0, 0, this.imageWidth, this.imageHeight);
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            float dragonScale = 1F / Math.max(0.0001F, dragon.getScale());
            // Origin at top left
            renderEntityInInventory(leftPadding + 88,
                                    topPadding + (int) (0.5F * (dragon.flyProgress)) + 55,
                                    (int) (dragonScale * 23F),
                                    leftPadding + 51 - this.mousePosx,
                                    topPadding + 75 - 50 - this.mousePosY,
                                    dragon
            );
        }
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;

            Font font = this.getMinecraft().font;
            String s3 = dragon.getCustomName() == null ? StatCollector.translateToLocal("dragon.unnamed") : StatCollector.translateToLocal(
                    "dragon.name") + " " + dragon.getCustomName().getString();
            font.draw(matrixStack,
                      s3,
                      leftPadding + this.imageWidth / 2 - font.width(s3) / 2,
                      topPadding + 75,
                      0XFFFFFF
            );
            String s2 = StatCollector.translateToLocal("dragon.health") + " " + Math.floor(Math.min(dragon.getHealth(),
                                                                                                    dragon.getMaxHealth()
            )) + " / " + dragon.getMaxHealth();
            font.draw(matrixStack,
                      s2,
                      leftPadding + this.imageWidth / 2 - font.width(s2) / 2,
                      topPadding + 84,
                      0XFFFFFF
            );
            String s5 = StatCollector.translateToLocal("dragon.gender") + StatCollector.translateToLocal((dragon.isMale() ? "dragon.gender.male" : "dragon.gender.female"));
            font.draw(matrixStack,
                      s5,
                      leftPadding + this.imageWidth / 2 - font.width(s5) / 2,
                      topPadding + 93,
                      0XFFFFFF
            );
            String s6 = StatCollector.translateToLocal("dragon.hunger") + dragon.getHunger() + "/100";
            font.draw(matrixStack,
                      s6,
                      leftPadding + this.imageWidth / 2 - font.width(s6) / 2,
                      topPadding + 102,
                      0XFFFFFF
            );
            String s4 = StatCollector.translateToLocal("dragon.stage") + " " + dragon.getDragonStage() + " " + StatCollector.translateToLocal(
                    "dragon.days.front") + dragon.getAgeInDays() + " " + StatCollector.translateToLocal(
                    "dragon.days.back");
            font.draw(matrixStack,
                      s4,
                      leftPadding + this.imageWidth / 2 - font.width(s4) / 2,
                      topPadding + 111,
                      0XFFFFFF
            );
            String s7 = dragon.getOwner() != null ? StatCollector.translateToLocal("dragon.owner") + dragon.getOwner().getName().getString() : StatCollector.translateToLocal(
                    "dragon.untamed");
            font.draw(matrixStack,
                      s7,
                      leftPadding + this.imageWidth / 2 - font.width(s7) / 2,
                      topPadding + 120,
                      0XFFFFFF
            );
        }

    }

    /**
     * Renders the entity in the inventory<br>
     * Vanilla method have issue dealing with long necks
     * In {@link net.minecraft.client.renderer.entity.LivingEntityRenderer#render}, {@link LivingEntity#yBodyRotO} is used, but it's not properly updated in {@link InventoryScreen#renderEntityInInventory}
     * @param pPosX
     * @param pPosY
     * @param pScale
     * @param pMouseX
     * @param pMouseY
     * @param pLivingEntity
     * @see InventoryScreen#renderEntityInInventory
     */
    public static void renderEntityInInventory(int pPosX, int pPosY, int pScale, float pMouseX, float pMouseY, LivingEntity pLivingEntity) {
        // Origin at top left corner
        float f = (float) Math.atan((double) (pMouseX / 40.0F));
        float f1 = (float) Math.atan((double) (pMouseY / 40.0F));
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate((double) pPosX, (double) pPosY, 1050.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0D, 0.0D, 1000.0D);
        posestack1.scale((float) pScale, (float) pScale, (float) pScale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        posestack1.mulPose(quaternion);

        float entityYBodyRotO = pLivingEntity.yBodyRotO;

        float entityYBodyRot = pLivingEntity.yBodyRot;
        float entityYRot = pLivingEntity.getYRot();
        float entityXRot = pLivingEntity.getXRot();
        float entityYHeadRotO = pLivingEntity.yHeadRotO;
        float entityYHeadRot = pLivingEntity.yHeadRot;
        pLivingEntity.yBodyRot = 180.0F + f * 20.0F;

        pLivingEntity.yBodyRotO = 180.0F + f * 20.0F;

        pLivingEntity.setYRot(180.0F + f * 40.0F);
        pLivingEntity.setXRot(-f1 * 20.0F);
        pLivingEntity.yHeadRot = pLivingEntity.yBodyRot;
        pLivingEntity.yHeadRotO = pLivingEntity.yBodyRot;
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderdispatcher.overrideCameraOrientation(quaternion1);
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderdispatcher.render(pLivingEntity,
                                          0.0D,
                                          0.0D,
                                          0.0D,
                                          0.0F,
                                          1.0F,
                                          posestack1,
                                          multibuffersource$buffersource,
                                          15728880
            );
        });
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        pLivingEntity.yBodyRot = entityYBodyRot;

        pLivingEntity.yBodyRotO = entityYBodyRotO;

        pLivingEntity.setYRot(entityYRot);
        pLivingEntity.setXRot(entityXRot);
        pLivingEntity.yHeadRotO = entityYHeadRotO;
        pLivingEntity.yHeadRot = entityYHeadRot;
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

}