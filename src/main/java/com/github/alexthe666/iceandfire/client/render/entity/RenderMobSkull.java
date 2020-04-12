package com.github.alexthe666.iceandfire.client.render.entity;

import com.github.alexthe666.iceandfire.client.model.*;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.entity.EntityMobSkull;
import com.github.alexthe666.iceandfire.enums.EnumSkullType;
import com.google.common.collect.Maps;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderMobSkull extends Render<EntityMobSkull> {

    private static final Map<String, ResourceLocation> SKULL_TEXTURE_CACHE = Maps.newHashMap();
    private ModelHippogryph hippogryphModel;
    private ModelCyclops cyclopsModel;
    private ModelCockatrice cockatriceModel;
    private ModelStymphalianBird stymphalianBirdModel;
    private ModelTroll trollModel;
    private ModelAmphithere amphithereModel;
    private ModelHydraHead hydraModel;
    private IceAndFireTabulaModel seaSerpentModel;

    public RenderMobSkull(RenderManager renderManager, ModelBase seaSerpentModel) {
        super(renderManager);
        this.hippogryphModel = new ModelHippogryph();
        this.cyclopsModel = new ModelCyclops();
        this.cockatriceModel = new ModelCockatrice();
        this.stymphalianBirdModel = new ModelStymphalianBird();
        this.trollModel = new ModelTroll();
        this.amphithereModel = new ModelAmphithere();
        this.seaSerpentModel = (IceAndFireTabulaModel) seaSerpentModel;
        this.hydraModel = new ModelHydraHead(0);
    }

    private static void setRotationAngles(ModelRenderer cube, float rotX, float rotY, float rotZ) {
        cube.rotateAngleX = rotX;
        cube.rotateAngleY = rotY;
        cube.rotateAngleZ = rotZ;
    }

    public void doRender(EntityMobSkull entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(entity.getYaw(), 0, -1, 0);
        float f = 0.0625F;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(1.0F, -1.0F, 1.0F);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(entity);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        float size = 1.0F;
        GlStateManager.scale(size, size, size);
        GlStateManager.translate(0, entity.isOnWall() ? -0.24F : -0.12F, 0.5F);

        renderForEnum(entity.getSkullType(), entity.isOnWall());
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    private void renderForEnum(EnumSkullType skull, boolean onWall) {
        switch (skull) {
            case HIPPOGRYPH:
                GL11.glTranslatef(0, -0.0F, -0.2F);
                GL11.glScalef(1.2F, 1.2F, 1.2F);
                hippogryphModel.resetToDefaultPose();
                setRotationAngles(hippogryphModel.Head, onWall ? (float) Math.toRadians(50F) : (float) Math.toRadians(-5), 0, 0);
                hippogryphModel.Head.render(0.0625F);
                break;
            case CYCLOPS:
                GL11.glTranslatef(0, 1.8F, -0.5F);
                GL11.glScalef(2.25F, 2.25F, 2.25F);
                cyclopsModel.resetToDefaultPose();
                setRotationAngles(cyclopsModel.Head, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                cyclopsModel.Head.render(0.0625F);
                break;
            case COCKATRICE:
                if (onWall) {
                    GL11.glTranslatef(0, 0F, 0.35F);
                }
                cockatriceModel.resetToDefaultPose();
                setRotationAngles(cockatriceModel.head, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                cockatriceModel.head.render(0.0625F);
                break;
            case STYMPHALIAN:
                if (!onWall) {
                    GL11.glTranslatef(0, 0F, -0.35F);
                }
                stymphalianBirdModel.resetToDefaultPose();
                setRotationAngles(stymphalianBirdModel.HeadBase, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                stymphalianBirdModel.HeadBase.render(0.0625F);
                break;
            case TROLL:
                GL11.glTranslatef(0, 1F, -0.35F);
                if (onWall) {
                    GL11.glTranslatef(0, 0F, 0.35F);
                }
                trollModel.resetToDefaultPose();
                setRotationAngles(trollModel.head, onWall ? (float) Math.toRadians(50F) : (float) Math.toRadians(-20), 0, 0);
                trollModel.head.render(0.0625F);
                break;
            case AMPHITHERE:
                GL11.glTranslatef(0, -0.2F, 0.7F);
                GL11.glScalef(2.0F, 2.0F, 2.0F);
                amphithereModel.resetToDefaultPose();
                setRotationAngles(amphithereModel.Head, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                amphithereModel.Head.render(0.0625F);
                break;
            case SEASERPENT:
                GL11.glTranslatef(0, -0.5F, 0.8F);
                GL11.glScalef(2.5F, 2.5F, 2.5F);
                seaSerpentModel.resetToDefaultPose();
                setRotationAngles(seaSerpentModel.getCube("Head"), onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                seaSerpentModel.getCube("Head").render(0.0625F);
                break;
            case HYDRA:
                GL11.glTranslatef(0, -0.2F, -0.1F);
                GL11.glScalef(2.0F, 2.0F, 2.0F);
                hydraModel.resetToDefaultPose();
                setRotationAngles(hydraModel.Head1, onWall ? (float) Math.toRadians(50F) : 0F, 0, 0);
                hydraModel.Head1.render(0.0625F);
                break;
        }
    }

    protected ResourceLocation getEntityTexture(EntityMobSkull entity) {
        String s = "iceandfire:textures/models/skulls/skull_" + entity.getSkullType().name().toLowerCase() + ".png";
        ResourceLocation resourcelocation = SKULL_TEXTURE_CACHE.get(s);
        if (resourcelocation == null) {
            resourcelocation = new ResourceLocation(s);
            SKULL_TEXTURE_CACHE.put(s, resourcelocation);
        }
        return resourcelocation;
    }

}
