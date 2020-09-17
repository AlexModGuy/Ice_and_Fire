package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.inventory.ContainerDragon;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
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

    public static void drawEntityOnScreen(int posX, int posY, float scale, float mouseX, float mouseY, LivingEntity p_228187_5_) {
        float f = (float) Math.atan((double) (mouseX / 40.0F));
        float f1 = (float) Math.atan((double) (mouseY / 40.0F));
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float) posX, (float) posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale((float) scale, (float) scale, (float) scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.multiply(quaternion1);
        matrixstack.rotate(quaternion);
        float f2 = p_228187_5_.renderYawOffset;
        float f3 = p_228187_5_.rotationYaw;
        float f4 = p_228187_5_.rotationPitch;
        float f5 = p_228187_5_.prevRotationYawHead;
        float f6 = p_228187_5_.rotationYawHead;
        p_228187_5_.renderYawOffset = 180.0F + f * 20.0F;
        p_228187_5_.rotationYaw = 180.0F + f * 40.0F;
        p_228187_5_.rotationPitch = -f1 * 20.0F;
        p_228187_5_.rotationYawHead = p_228187_5_.rotationYaw;
        p_228187_5_.prevRotationYawHead = p_228187_5_.rotationYaw;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        quaternion1.conjugate();
        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderermanager.renderEntityStatic(p_228187_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        });
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);
        p_228187_5_.renderYawOffset = f2;
        p_228187_5_.rotationYaw = f3;
        p_228187_5_.rotationPitch = f4;
        p_228187_5_.prevRotationYawHead = f5;
        p_228187_5_.rotationYawHead = f6;
        RenderSystem.popMatrix();
    }


    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            String s1 = dragon.getName().getString();
            FontRenderer font = this.getMinecraft().fontRenderer;
            font.func_238421_b_(matrixStack, s1, this.xSize / 2 - font.getStringWidth(s1) / 2, 6, 4210752);
            String s3 = dragon.getCustomName() == null ? StatCollector.translateToLocal("dragon.unnamed") : StatCollector.translateToLocal("dragon.name") + dragon.getCustomName().getString();
            font.func_238421_b_(matrixStack,s3, this.xSize / 2 - font.getStringWidth(s3) / 2, 75, 0XFFFFFF);
            String s2 = StatCollector.translateToLocal("dragon.health") + " " + Math.floor(Math.min(dragon.getHealth(), dragon.getMaxHealth())) + " / " + dragon.getMaxHealth();
            font.func_238421_b_(matrixStack,s2, this.xSize / 2 - font.getStringWidth(s2) / 2, 84, 0XFFFFFF);
            String s5 = StatCollector.translateToLocal("dragon.gender") + StatCollector.translateToLocal((dragon.isMale() ? "dragon.gender.male" : "dragon.gender.female"));
            font.func_238421_b_(matrixStack,s5, this.xSize / 2 - font.getStringWidth(s5) / 2, 93, 0XFFFFFF);
            String s6 = StatCollector.translateToLocal("dragon.hunger") + dragon.getHunger() + "/100";
            font.func_238421_b_(matrixStack,s6, this.xSize / 2 - font.getStringWidth(s6) / 2, 102, 0XFFFFFF);
            String s4 = StatCollector.translateToLocal("dragon.stage")  + " " + dragon.getDragonStage()  + " " + StatCollector.translateToLocal("dragon.days.front") + dragon.getAgeInDays() + " " + StatCollector.translateToLocal("dragon.days.back");
            font.func_238421_b_(matrixStack,s4, this.xSize / 2 - font.getStringWidth(s4) / 2, 111, 0XFFFFFF);
            String s7 = dragon.getOwner() != null ? StatCollector.translateToLocal("dragon.owner") + dragon.getOwner().getName().getString() : StatCollector.translateToLocal("dragon.untamed");
            font.func_238421_b_(matrixStack,s7, this.xSize / 2 - font.getStringWidth(s7) / 2, 120, 0XFFFFFF);
        }
    }

    public void func_230430_a_(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.func_230446_a_(p_230430_1_);
        this.mousePosx = (float)p_230430_2_;
        this.mousePosY = (float)p_230430_3_;
        super.func_230430_a_(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.func_230459_a_(p_230430_1_, p_230430_2_, p_230430_3_);
    }

    @Override
    protected void func_230450_a_(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.getMinecraft().getTextureManager().bindTexture(texture);
        int k = (this.field_230708_k_ - this.xSize) / 2;
        int l = (this.field_230709_l_ - this.ySize) / 2;
        this.func_238474_b_(matrixStack, k, l, 0, 0, this.xSize, this.ySize);
        Entity entity = IceAndFire.PROXY.getReferencedMob();
        if (entity instanceof EntityDragonBase) {
            EntityDragonBase dragon = (EntityDragonBase) entity;
            float dragonScale = 1F / Math.max(0.0001F, dragon.getRenderScale());
            drawEntityOnScreen(k + 88, l + (int) (0.5F * (dragon.flyProgress)) + 55, dragonScale * 23F, k + 51 - this.mousePosx, l + 75 - 50 - this.mousePosY, dragon);
        }
        this.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
    }


}