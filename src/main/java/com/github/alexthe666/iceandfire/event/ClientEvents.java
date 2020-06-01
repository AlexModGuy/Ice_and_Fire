package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.ClientProxy;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.gui.IceAndFireMainMenu;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.client.render.entity.ICustomStoneLayer;
import com.github.alexthe666.iceandfire.client.render.entity.RenderCockatrice;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerChainedEntity;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntity;
import com.github.alexthe666.iceandfire.client.render.entity.layer.LayerStoneEntityCrack;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Random;

public class ClientEvents {

    private static final ResourceLocation SIREN_SHADER = new ResourceLocation("iceandfire:shaders/post/siren.json");
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation("textures/blocks/frosted_ice_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("textures/blocks/frosted_ice_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("textures/blocks/frosted_ice_2.png");
    private static final ResourceLocation TEXTURE_3 = new ResourceLocation("textures/blocks/frosted_ice_3.png");
    private static final ResourceLocation CHAIN_TEXTURE = new ResourceLocation("iceandfire:textures/models/misc/chain_link.png");
    private Random rand = new Random();

    public static void initializeStoneLayer() {
        for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : Minecraft.getMinecraft().getRenderManager().entityRenderMap.entrySet()) {
            Render render = entry.getValue();
            if (render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) {
                ((RenderLivingBase) render).addLayer(new LayerStoneEntity((RenderLivingBase) render));
                ((RenderLivingBase) render).addLayer(new LayerStoneEntityCrack((RenderLivingBase) render));
                ((RenderLivingBase) render).addLayer(new LayerChainedEntity(render));
            }
        }

        Field renderingRegistryField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), "INSTANCE", "INSTANCE"));
        Field entityRendersField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), "entityRenderers", "entityRenderers"));
        Field entityRendersOldField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), "entityRenderersOld", "entityRenderersOld"));
        RenderingRegistry registry = null;
        try {
            Field modifier = Field.class.getDeclaredField("modifiers");
            modifier.setAccessible(true);
            registry = (RenderingRegistry) renderingRegistryField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (registry != null) {
            Map<Class<? extends Entity>, IRenderFactory<? extends Entity>> entityRenders = null;
            Map<Class<? extends Entity>, Render<? extends Entity>> entityRendersOld = null;
            try {
                Field modifier1 = Field.class.getDeclaredField("modifiers");
                modifier1.setAccessible(true);
                entityRenders = (Map<Class<? extends Entity>, IRenderFactory<? extends Entity>>) entityRendersField.get(registry);
                entityRendersOld = (Map<Class<? extends Entity>, Render<? extends Entity>>) entityRendersOldField.get(registry);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (entityRenders != null) {
                for (Map.Entry<Class<? extends Entity>, IRenderFactory<? extends Entity>> entry : entityRenders.entrySet()) {
                    if (entry.getValue() != null) {
                        try {
                            Render render = entry.getValue().createRenderFor(Minecraft.getMinecraft().getRenderManager());
                            if (render != null && render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) {
                                LayerRenderer stoneLayer = render instanceof ICustomStoneLayer ? ((ICustomStoneLayer) render).getStoneLayer((RenderLivingBase) render) : new LayerStoneEntity((RenderLivingBase) render);
                                LayerRenderer crackLayer = render instanceof ICustomStoneLayer ? ((ICustomStoneLayer) render).getCrackLayer((RenderLivingBase) render) : new LayerStoneEntityCrack((RenderLivingBase) render);
                                ((RenderLivingBase) render).addLayer(stoneLayer);
                                ((RenderLivingBase) render).addLayer(crackLayer);
                                ((RenderLivingBase) render).addLayer(new LayerChainedEntity(render));
                            }
                        } catch (NullPointerException exp) {
                            IceAndFire.logger.warn("Ice and Fire: Could not apply stone render layer to " + entry.getKey().getSimpleName() + ", someone isn't registering their renderer properly... <.<");
                        }
                    }

                }
            }
            if (entityRendersOld != null) {
                for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : entityRendersOld.entrySet()) {
                    Render render = entry.getValue();
                    if (render instanceof RenderLivingBase && EntityLiving.class.isAssignableFrom(entry.getKey())) {
                        LayerRenderer stoneLayer = render instanceof ICustomStoneLayer ? ((ICustomStoneLayer) render).getStoneLayer((RenderLivingBase) render) : new LayerStoneEntity((RenderLivingBase) render);
                        LayerRenderer crackLayer = render instanceof ICustomStoneLayer ? ((ICustomStoneLayer) render).getCrackLayer((RenderLivingBase) render) : new LayerStoneEntityCrack((RenderLivingBase) render);
                        ((RenderLivingBase) render).addLayer(stoneLayer);
                        ((RenderLivingBase) render).addLayer(crackLayer);
                        ((RenderLivingBase) render).addLayer(new LayerChainedEntity(render));
                    }
                }
            }
        }

    }

    private static ResourceLocation getIceTexture(int ticksFrozen) {
        if (ticksFrozen < 100) {
            if (ticksFrozen < 50) {
                if (ticksFrozen < 20) {
                    return TEXTURE_3;
                }
                return TEXTURE_2;
            }
            return TEXTURE_1;
        }
        return TEXTURE_0;
    }

    public static void renderAABB(AxisAlignedBB boundingBox, double x, double y, double z) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        double maxX = boundingBox.maxX * 0.625F;
        double minX = boundingBox.minX * 0.625F;
        double maxY = boundingBox.maxY * 0.625F;
        double minY = boundingBox.minY * 0.625F;
        double maxZ = boundingBox.maxZ * 0.625F;
        double minZ = boundingBox.minZ * 0.625F;
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(minX - maxX, maxY - minY).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(maxX - minX, maxY - minY).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(maxX - minX, minY - maxY).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(minX - maxX, minY - maxY).normal(0.0F, 0.0F, -1.0F).endVertex();

        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(minX - maxX, minY - maxY).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(maxX - minX, minY - maxY).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(maxX - minX, maxY - minY).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(minX - maxX, maxY - minY).normal(0.0F, 0.0F, 1.0F).endVertex();

        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(minX - maxX, minY - maxY).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(maxX - minX, minY - maxY).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(maxX - minX, maxZ - minZ).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(minX - maxX, maxZ - minZ).normal(0.0F, -1.0F, 0.0F).endVertex();

        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(minX - maxX, minY - maxY).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(maxX - minX, minY - maxY).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(maxX - minX, maxZ - minZ).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(minX - maxX, maxZ - minZ).normal(0.0F, 1.0F, 0.0F).endVertex();

        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).tex(minX - maxX, minY - maxY).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).tex(minX - maxX, maxY - minY).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).tex(maxX - minX, maxY - minY).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).tex(maxX - minX, minY - maxY).normal(-1.0F, 0.0F, 0.0F).endVertex();

        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).tex(minX - maxX, minY - maxY).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).tex(minX - maxX, maxY - minY).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).tex(maxX - minX, maxY - minY).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).tex(maxX - minX, minY - maxY).normal(1.0F, 0.0F, 0.0F).endVertex();
        tessellator.draw();
    }

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (player.getRidingEntity() != null) {
            if (player.getRidingEntity() instanceof EntityDragonBase) {
                int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
                EntityDragonBase dragon = (EntityDragonBase) player.getRidingEntity();
                float scale = ((EntityDragonBase) player.getRidingEntity()).getRenderSize() / 3;
                if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 1) {
                    if (currentView == 0) {
                    } else if (currentView == 1) {
                        GL11.glTranslatef(scale * 0.5F, 0F, -scale * 3F);
                    } else if (currentView == 2) {
                        GL11.glTranslatef(0, 0F, -scale * 3F);
                    } else if (currentView == 3) {
                        GL11.glTranslatef(scale * 0.5F, 0F, -scale * 0.5F);
                    }
                }
                if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
                    if (currentView == 0) {
                    } else if (currentView == 1) {
                        GL11.glTranslatef(-scale * 1.2F, 0F, 5);
                    } else if (currentView == 2) {
                        GL11.glTranslatef(scale * 1.2F, 0F, 5);
                    } else if (currentView == 3) {
                        GL11.glTranslatef(0, 0F, scale * 3F);
                    }
                }

            }

        }
    }

    protected double translateToBody(RenderLiving renderer) {
        double roll = 0;
        roll += postRenderRoll(((IceAndFireTabulaModel) renderer.getMainModel()).getCube("BodyUpper"), 0.0625F);
        roll += postRenderRoll(((IceAndFireTabulaModel) renderer.getMainModel()).getCube("Neck1"), 0.0625F);
        return roll;
    }

    public double postRenderRoll(AdvancedModelRenderer renderer, float scale) {
        return renderer.rotateAngleZ * (180F / (float) Math.PI);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (player.world.isRemote && IafKeybindRegistry.dragon_change_view.isPressed()) {
                int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
                if (currentView + 1 > 3) {
                    currentView = 0;
                } else {
                    currentView++;
                }
                IceAndFire.PROXY.setDragon3rdPersonView(currentView);
            }

            SirenEntityProperties sirenProps = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), SirenEntityProperties.class);
            if (player.world.isRemote && sirenProps != null) {
                EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
                EntitySiren siren = sirenProps.getSiren(event.getEntityLiving().world);
                if (siren == null) {
                    sirenProps.isCharmed = false;
                }
                if (sirenProps.isCharmed) {
                    if (player.world.isRemote && rand.nextInt(40) == 0) {
                        IceAndFire.PROXY.spawnParticle("siren_appearance", player.posX, player.posY, player.posZ, 0, 0, 0);
                    }

                    if (IceAndFire.CONFIG.sirenShader && sirenProps.isCharmed && !renderer.isShaderActive()) {
                        renderer.loadShader(SIREN_SHADER);
                    }

                }
                if (IceAndFire.CONFIG.sirenShader && !sirenProps.isCharmed && renderer != null && renderer.getShaderGroup() != null && renderer.getShaderGroup().getShaderGroupName() != null && SIREN_SHADER.toString().equals(renderer.getShaderGroup().getShaderGroupName())) {
                    renderer.stopUseShader();
                }
            }
        }
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
        if (event.getEntity().getRidingEntity() != null && event.getEntity().getRidingEntity() instanceof EntityDragonBase) {
            if (ClientProxy.currentDragonRiders.contains(event.getEntity().getUniqueID()) || event.getEntity() == Minecraft.getMinecraft().player && Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
                event.setCanceled(true);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post(event.getEntity(), event.getRenderer(), event.getPartialRenderTick(), event.getX(), event.getY(), event.getZ()));
            }
        }

        if (event.getEntity() instanceof EntityLiving && !event.getEntity().isInvisible()) {
            StoneEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), StoneEntityProperties.class);
            if (properties != null && properties.isStone) {
                event.getEntity().setInvisible(true);
            }
        }
    }

    @SubscribeEvent
    public void onPostRenderLiving(RenderLivingEvent.Post event) {
        EntityLivingBase entity = event.getEntity();
        ChainEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, ChainEntityProperties.class);
        if (properties != null) {
            if (!properties.connectedEntities.isEmpty()) {
                if (!properties.alreadyIgnoresCamera) {
                    entity.ignoreFrustumCheck = true;
                }
                GlStateManager.pushMatrix();
                GlStateManager.disableCull();
                GlStateManager.enableNormalize();
                GlStateManager.depthMask(true);
                GlStateManager.disableLighting();
                int light = entity.world.getCombinedLight(new BlockPos(entity), 0);
                int i1 = light % 65536;
                int j1 = light / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) i1, (float) j1);
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                event.getRenderer().bindTexture(CHAIN_TEXTURE);
                double posX = Minecraft.getMinecraft().player.prevPosX + (Minecraft.getMinecraft().player.posX - Minecraft.getMinecraft().player.prevPosX) * (double) event.getPartialRenderTick();
                double posY = Minecraft.getMinecraft().player.prevPosY + (Minecraft.getMinecraft().player.posY - Minecraft.getMinecraft().player.prevPosY) * (double) event.getPartialRenderTick();
                double posZ = Minecraft.getMinecraft().player.prevPosZ + (Minecraft.getMinecraft().player.posZ - Minecraft.getMinecraft().player.prevPosZ) * (double) event.getPartialRenderTick();
                for (Entity chainer : properties.connectedEntities) {
                    GlStateManager.pushMatrix();
                    double chainPosX = chainer.prevPosX + (chainer.posX - chainer.prevPosX) * (double) event.getPartialRenderTick();
                    double chainPosY = chainer.prevPosY + (chainer.posY - chainer.prevPosY) * (double) event.getPartialRenderTick();
                    double chainPosZ = chainer.prevPosZ + (chainer.posZ - chainer.prevPosZ) * (double) event.getPartialRenderTick();
                    GlStateManager.translate(chainPosX - posX, chainPosY - posY, chainPosZ - posZ);
                    GlStateManager.translate(0, 0.75, 0);
                    double height = (double) chainer.getEyeHeight() * 0.75F;
                    Vec3d vec3d = this.getChainPosition(entity, height, event.getPartialRenderTick());
                    if (entity instanceof EntityDragonBase) {
                        vec3d = this.getChainPosition(entity, ((EntityDragonBase) entity).getRenderSize() * 0.15F, event.getPartialRenderTick());
                    }
                    Vec3d vec3d1 = this.getChainPosition(chainer, height, event.getPartialRenderTick());
                    Vec3d vec3d2 = vec3d.subtract(vec3d1);
                    double d0 = vec3d2.length();
                    vec3d2 = vec3d2.normalize();
                    float f5 = (float) acos(vec3d2.y);
                    float f6 = -(float) IAFMath.atan2_accurate(vec3d2.z, vec3d2.x);
                    GlStateManager.rotate((((float) Math.PI / 2F) + f6) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f5 * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
                    double d1 = 0D;
                    int j = 225;
                    int k = 225;
                    int l = 225;
                    float texture_scale = 0.3F;
                    double d12 = 0.0D + Math.cos(d1 + Math.PI) * texture_scale;
                    double d13 = 0.0D + Math.sin(d1 + Math.PI) * texture_scale;
                    double d14 = 0.0D + Math.cos(d1 + 0.0D) * texture_scale;
                    double d15 = 0.0D + Math.sin(d1 + 0.0D) * texture_scale;

                    double d16 = 0.0D + Math.cos(d1 + (Math.PI / 2D)) * texture_scale;
                    double d17 = 0.0D + Math.sin(d1 + (Math.PI / 2D)) * texture_scale;
                    double d18 = 0.0D + Math.cos(d1 + (Math.PI * 3D / 2D)) * texture_scale;
                    double d19 = 0.0D + Math.sin(d1 + (Math.PI * 3D / 2D)) * texture_scale;
                    double d22 = 0.0F;
                    double d23 = d0 * 1 - texture_scale + d22;
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                    bufferbuilder.pos(d12, d0, d13).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
                    bufferbuilder.pos(d12, 0.0D, d13).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
                    bufferbuilder.pos(d14, 0.0D, d15).tex(0.0D, d22).color(j, k, l, 255).endVertex();
                    bufferbuilder.pos(d14, d0, d15).tex(0.0D, d23).color(j, k, l, 255).endVertex();
                    bufferbuilder.pos(d16, d0, d17).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
                    bufferbuilder.pos(d16, 0.0D, d17).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
                    bufferbuilder.pos(d18, 0.0D, d19).tex(0.0D, d22).color(j, k, l, 255).endVertex();
                    bufferbuilder.pos(d18, d0, d19).tex(0.0D, d23).color(j, k, l, 255).endVertex();
                    tessellator.draw();
                    GlStateManager.popMatrix();
                }

                GlStateManager.disableBlend();
                GlStateManager.enableCull();
                GlStateManager.enableLighting();
                GlStateManager.disableNormalize();
                GlStateManager.popMatrix();

            } else {
                if (!properties.alreadyIgnoresCamera && entity.ignoreFrustumCheck) {
                    entity.ignoreFrustumCheck = false;
                }
            }
        }
        FrozenEntityProperties frozenProps = EntityPropertiesHandler.INSTANCE.getProperties(entity, FrozenEntityProperties.class);
        if (frozenProps != null && frozenProps.isFrozen) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableNormalize();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            float sideExpand = 0.25F;
            AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(event.getEntity().getRenderBoundingBox().minX - event.getEntity().posX + event.getX() - sideExpand, event.getEntity().getRenderBoundingBox().minY - event.getEntity().posY + event.getY(), event.getEntity().getRenderBoundingBox().minZ - event.getEntity().posZ + event.getZ() - sideExpand, event.getEntity().getRenderBoundingBox().maxX - event.getEntity().posX + event.getX() + sideExpand, event.getEntity().getRenderBoundingBox().maxY - event.getEntity().posY + event.getY() + sideExpand, event.getEntity().getRenderBoundingBox().maxZ - event.getEntity().posZ + event.getZ() + sideExpand);
            event.getRenderer().bindTexture(getIceTexture(frozenProps.ticksUntilUnfrozen));
            renderAABB(axisalignedbb1, 0, 0, 0);
            GlStateManager.disableBlend();
            GlStateManager.disableNormalize();
        }
        MiscEntityProperties miscProps = EntityPropertiesHandler.INSTANCE.getProperties(entity, MiscEntityProperties.class);
        if (miscProps != null && miscProps.glarers.size() > 0) {
            float f = 1.0F;// ((float) miscProps.clientSideAttackTime + event.getPartialRenderTick()) / (float) 80;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            event.getRenderer().bindTexture(RenderCockatrice.TEXTURE_BEAM);
            GlStateManager.glTexParameteri(3553, 10242, 10497);
            GlStateManager.glTexParameteri(3553, 10243, 10497);
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            float f1 = 240.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            float f2 = (float) entity.world.getTotalWorldTime() + event.getPartialRenderTick();
            float f3 = f2 * 0.15F % 1.0F;
            float f4 = entity.getEyeHeight();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 1.2F, 0);
            for (Entity entitylivingbase : miscProps.glarers) {
                Vec3d vec3d = this.getChainPosition(entitylivingbase, (double) entitylivingbase.getEyeHeight() * 0.75D, event.getPartialRenderTick());
                Vec3d vec3d1 = this.getChainPosition(entity, f4, event.getPartialRenderTick());
                Vec3d vec3d2 = vec3d1.subtract(vec3d);
                double d0 = vec3d2.length() + 1.0D;
                vec3d2 = vec3d2.normalize();
                float f5 = (float) acos(vec3d2.y);
                float f6 = (float) IAFMath.atan2_accurate(vec3d2.z, vec3d2.x);
                GlStateManager.rotate((((float) Math.PI / 2F) + -f6) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(f5 * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
                double d1 = (double) f2 * 0.05D * -1.5D;
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                int j = 225;
                int k = 225;
                int l = 225;
                double d4 = 0.0D + Math.cos(d1 + 2.356194490192345D) * 0.282D;
                double d5 = 0.0D + Math.sin(d1 + 2.356194490192345D) * 0.282D;
                double d6 = 0.0D + Math.cos(d1 + (Math.PI / 4D)) * 0.282D;
                double d7 = 0.0D + Math.sin(d1 + (Math.PI / 4D)) * 0.282D;
                double d8 = 0.0D + Math.cos(d1 + 3.9269908169872414D) * 0.282D;
                double d9 = 0.0D + Math.sin(d1 + 3.9269908169872414D) * 0.282D;
                double d10 = 0.0D + Math.cos(d1 + 5.497787143782138D) * 0.282D;
                double d11 = 0.0D + Math.sin(d1 + 5.497787143782138D) * 0.282D;
                double d12 = 0.0D + Math.cos(d1 + Math.PI) * 0.2D;
                double d13 = 0.0D + Math.sin(d1 + Math.PI) * 0.2D;
                double d14 = 0.0D + Math.cos(d1 + 0.0D) * 0.2D;
                double d15 = 0.0D + Math.sin(d1 + 0.0D) * 0.2D;
                double d16 = 0.0D + Math.cos(d1 + (Math.PI / 2D)) * 0.2D;
                double d17 = 0.0D + Math.sin(d1 + (Math.PI / 2D)) * 0.2D;
                double d18 = 0.0D + Math.cos(d1 + (Math.PI * 3D / 2D)) * 0.2D;
                double d19 = 0.0D + Math.sin(d1 + (Math.PI * 3D / 2D)) * 0.2D;
                double d22 = -1.0F + f3;
                double d23 = d0 * 2.5D + d22;
                bufferbuilder.pos(d12, d0, d13).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
                bufferbuilder.pos(d12, 0.0D, d13).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
                bufferbuilder.pos(d14, 0.0D, d15).tex(0.0D, d22).color(j, k, l, 255).endVertex();
                bufferbuilder.pos(d14, d0, d15).tex(0.0D, d23).color(j, k, l, 255).endVertex();
                bufferbuilder.pos(d16, d0, d17).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
                bufferbuilder.pos(d16, 0.0D, d17).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
                bufferbuilder.pos(d18, 0.0D, d19).tex(0.0D, d22).color(j, k, l, 255).endVertex();
                bufferbuilder.pos(d18, d0, d19).tex(0.0D, d23).color(j, k, l, 255).endVertex();
                double d24 = 0.0D;
                if (entity.ticksExisted % 2 == 0) {
                    d24 = 0.5D;
                }
                bufferbuilder.pos(d4, d0, d5).tex(0.5D, d24 + 0.5D).color(j, k, l, 255).endVertex();
                bufferbuilder.pos(d6, d0, d7).tex(1.0D, d24 + 0.5D).color(j, k, l, 255).endVertex();
                bufferbuilder.pos(d10, d0, d11).tex(1.0D, d24).color(j, k, l, 255).endVertex();
                bufferbuilder.pos(d8, d0, d9).tex(0.5D, d24).color(j, k, l, 255).endVertex();
                tessellator.draw();
            }
            GlStateManager.disableBlend();
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
        }
        if (event.getEntity() instanceof EntityLiving) {
            StoneEntityProperties stoneProps = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntity(), StoneEntityProperties.class);
            if (stoneProps != null && stoneProps.isStone) {
                event.getEntity().setInvisible(false);
            }
        }
    }

    private double acos(double x) {
        return (-0.69813170079773212 * x * x - 0.87266462599716477) * x + 1.5707963267948966;
    }

    private Vec3d getChainPosition(Entity entityLivingBaseIn, double p_177110_2_, float p_177110_4_) {
        double d0 = entityLivingBaseIn.lastTickPosX + (entityLivingBaseIn.posX - entityLivingBaseIn.lastTickPosX) * (double) p_177110_4_;
        double d1 = p_177110_2_ + entityLivingBaseIn.lastTickPosY + (entityLivingBaseIn.posY - entityLivingBaseIn.lastTickPosY) * (double) p_177110_4_;
        double d2 = entityLivingBaseIn.lastTickPosZ + (entityLivingBaseIn.posZ - entityLivingBaseIn.lastTickPosZ) * (double) p_177110_4_;
        return new Vec3d(d0, d1, d2);
    }

    @SubscribeEvent
    public void onGuiOpened(GuiOpenEvent event) {
        if (IceAndFire.CONFIG.customMainMenu && event.getGui() instanceof GuiMainMenu && !(event.getGui() instanceof IceAndFireMainMenu)) {
            event.setGui(new IceAndFireMainMenu());
        }
    }

    @SubscribeEvent
    public void onEntityMount(EntityMountEvent event) {
        if (IceAndFire.CONFIG.dragonAuto3rdPerson) {
            if (event.getEntityBeingMounted() instanceof EntityDragonBase && event.getWorldObj().isRemote && event.getEntityMounting() == Minecraft.getMinecraft().player) {
                EntityDragonBase dragon = (EntityDragonBase)event.getEntityBeingMounted();
                if(dragon.isTamed() && dragon.isOwner(Minecraft.getMinecraft().player)){
                    if (event.isDismounting()) {
                        Minecraft.getMinecraft().gameSettings.thirdPersonView = IceAndFire.PROXY.getPreviousViewType();
                    } else {
                        IceAndFire.PROXY.setPreviousViewType(Minecraft.getMinecraft().gameSettings.thirdPersonView);
                        Minecraft.getMinecraft().gameSettings.thirdPersonView = 1;
                        IceAndFire.PROXY.setDragon3rdPersonView(2);
                    }
                }
            }
        }
    }
}