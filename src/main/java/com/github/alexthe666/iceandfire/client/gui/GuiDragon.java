package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.inventory.ContainerDragon;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiDragon extends ContainerScreen<ContainerDragon> {
    private static final ResourceLocation texture = new ResourceLocation("iceandfire:textures/gui/dragon.png");
    private PlayerInventory playerInventory;
    private ContainerDragon dragonInv;
    private float mousePosx;
    private float mousePosY;

    public GuiDragon(ContainerDragon dragonInv, PlayerInventory playerInv, ITextComponent name) {
        super(dragonInv, playerInv, name);
        this.playerInventory = playerInv;
        this.dragonInv = dragonInv;
        this.ySize = 214;
    }

    public static void drawEntityOnScreen(int x, int y, float scale, float yaw, float pitch, LivingEntity entity) {
        float f = (float) Math.atan(yaw / 40.0F);
        float f1 = (float) Math.atan(pitch / 40.0F);
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float) x, (float) y, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float) scale, (float) scale, (float) scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.multiply(quaternion1);
        matrixstack.rotate(quaternion);
        float f2 = entity.renderYawOffset;
        float f3 = entity.rotationYaw;
        float f4 = entity.rotationPitch;
        float f5 = entity.prevRotationYawHead;
        float f6 = entity.rotationYawHead;
        entity.renderYawOffset = 180.0F + f * 20.0F;
        entity.rotationYaw = 180.0F + f * 40.0F;
        entity.rotationPitch = -f1 * 20.0F;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        quaternion1.conjugate();
        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        entityrenderermanager.renderEntityStatic(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);
        entity.renderYawOffset = f2;
        entity.rotationYaw = f3;
        entity.rotationPitch = f4;
        entity.prevRotationYawHead = f5;
        entity.rotationYawHead = f6;
        RenderSystem.popMatrix();
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            String s1 = dragon.getName().getFormattedText();
            this.font.drawString(s1, this.xSize / 2 - this.font.getStringWidth(s1) / 2, 6, 4210752);
            String s3 = dragon.getCustomName() == null ? StatCollector.translateToLocal("dragon.unnamed") : StatCollector.translateToLocal("dragon.name") + dragon.getCustomName().getFormattedText();
            this.font.drawString(s3, this.xSize / 2 - this.font.getStringWidth(s3) / 2, 75, 0XFFFFFF);
            String s2 = StatCollector.translateToLocal("dragon.health") + " " + Math.floor(Math.min(dragon.getHealth(), dragon.getMaxHealth())) + " / " + dragon.getMaxHealth();
            this.font.drawString(s2, this.xSize / 2 - this.font.getStringWidth(s2) / 2, 84, 0XFFFFFF);
            String s5 = StatCollector.translateToLocal("dragon.gender") + StatCollector.translateToLocal((dragon.isMale() ? "dragon.gender.male" : "dragon.gender.female"));
            this.font.drawString(s5, this.xSize / 2 - this.font.getStringWidth(s5) / 2, 93, 0XFFFFFF);
            String s6 = StatCollector.translateToLocal("dragon.hunger") + dragon.getHunger() + "/100";
            this.font.drawString(s6, this.xSize / 2 - this.font.getStringWidth(s6) / 2, 102, 0XFFFFFF);
            String s4 = StatCollector.translateToLocal("dragon.stage")  + " " + dragon.getDragonStage()  + " " + StatCollector.translateToLocal("dragon.days.front") + dragon.getAgeInDays() + " " + StatCollector.translateToLocal("dragon.days.back");
            this.font.drawString(s4, this.xSize / 2 - this.font.getStringWidth(s4) / 2, 111, 0XFFFFFF);
            String s7 = dragon.getOwner() != null ? StatCollector.translateToLocal("dragon.owner") + dragon.getOwner().getName().getFormattedText() : StatCollector.translateToLocal("dragon.untamed");
            this.font.drawString(s7, this.xSize / 2 - this.font.getStringWidth(s7) / 2, 120, 0XFFFFFF);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.blit(k, l, 0, 0, this.xSize, this.ySize);
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            float dragonScale = 1F / Math.max(0.0001F, dragon.getRenderScale());
            drawEntityOnScreen(k + 88, l + (int) (0.5F * (dragon.flyProgress)) + 55, dragonScale * 23F, k + 51 - this.mousePosx, l + 75 - 50 - this.mousePosY, dragon);
        }

    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.mousePosx = mouseX;
        this.mousePosY = mouseY;
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

}