package com.github.alexthe666.iceandfire.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.inventory.ContainerDragon;

@SideOnly(Side.CLIENT)
public class GuiDragon extends GuiContainer
{
    private static final ResourceLocation texture = new ResourceLocation("iceandfire:textures/gui/dragon.png");
    private IInventory playerInventory;
    private IInventory dragonInv;
    private EntityDragonBase dragon;
    private float mousePosx;
    private float mousePosY;

    public GuiDragon(IInventory playerInv, EntityDragonBase dragon)
    {
        super(new ContainerDragon(dragon, Minecraft.getMinecraft().thePlayer));
        this.playerInventory = playerInv;
        this.dragonInv = dragon.inv;
        this.dragon = dragon;
        this.allowUserInput = false;
        this.ySize = 193;
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
    	String s1 = this.dragonInv.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s1, this.xSize / 2 - this.fontRendererObj.getStringWidth(s1) / 2, 6, 4210752);
        String s3 = dragon.getCustomNameTag().length() == 0 ? StatCollector.translateToLocal("dragon.unnamed") : StatCollector.translateToLocal("dragon.name") + dragon.getCustomNameTag();
        this.fontRendererObj.drawString(s3, this.xSize / 2 - this.fontRendererObj.getStringWidth(s3) / 2, 77, 0XFFFFFF);
        String s2 = StatCollector.translateToLocal("dragon.health")+ dragon.getHealth() + "/" + dragon.getMaxHealth();
        this.fontRendererObj.drawString(s2, this.xSize / 2 - this.fontRendererObj.getStringWidth(s2) / 2, 86, 0XFFFFFF);
        String s4 = StatCollector.translateToLocal("dragon.stage") + dragon.getStage();
        this.fontRendererObj.drawString(s4, this.xSize / 2 - this.fontRendererObj.getStringWidth(s4) / 2, 94, 0XFFFFFF);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        drawEntityOnScreen(k + 88, l + 55, 23, (float)(k + 51) - this.mousePosx, (float)(l + 75 - 50) - this.mousePosY, this.dragon);
      

    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.mousePosx = (float)mouseX;
        this.mousePosY = (float)mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, EntityDragonBase entity)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.scale(1 / entity.getDragonSize(), 1 / entity.getDragonSize(), 1 / entity.getDragonSize());
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = entity.renderYawOffset;
        float f3 = entity.rotationYaw;
        float f4 = entity.rotationPitch;
        float f5 = entity.prevRotationYawHead;
        float f6 = entity.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        entity.renderYawOffset = (float)Math.atan((double)(mouseX / 40.0F)) * 20.0F;
        entity.rotationYaw = (float)Math.atan((double)(mouseX / 40.0F)) * 40.0F;
        entity.rotationPitch = -((float)Math.atan((double)(mouseY / 40.0F))) * 20.0F;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        entity.renderYawOffset = f2;
        entity.rotationYaw = f3;
        entity.rotationPitch = f4;
        entity.prevRotationYawHead = f5;
        entity.rotationYawHead = f6;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

}