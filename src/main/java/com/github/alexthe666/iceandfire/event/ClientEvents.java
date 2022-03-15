package com.github.alexthe666.iceandfire.event;

import java.util.Random;

import com.github.alexthe666.iceandfire.ClientProxy;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.client.gui.IceAndFireMainMenu;
import com.github.alexthe666.iceandfire.client.particle.CockatriceBeamRender;
import com.github.alexthe666.iceandfire.client.render.entity.RenderChain;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.props.FrozenProperties;
import com.github.alexthe666.iceandfire.entity.props.MiscProperties;
import com.github.alexthe666.iceandfire.entity.props.SirenProperties;
import com.github.alexthe666.iceandfire.item.ItemCockatriceScepter;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.Pathfinding;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEvents {

    private static final ResourceLocation SIREN_SHADER = new ResourceLocation("iceandfire:shaders/post/siren.json");
    private static final ResourceLocation TEXTURE_0 = new ResourceLocation("textures/block/frosted_ice_0.png");
    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("textures/block/frosted_ice_1.png");
    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("textures/block/frosted_ice_2.png");
    private static final ResourceLocation TEXTURE_3 = new ResourceLocation("textures/block/frosted_ice_3.png");
    private Random rand = new Random();

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event) {
        if(Pathfinding.isDebug()){
            Pathfinding.debugDraw(event.getPartialTicks(), event.getMatrixStack());
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

    public static void renderMovingAABB(AxisAlignedBB boundingBox, MatrixStack stack) {
        Tessellator tessellator = Tessellator.getInstance();
        IVertexBuilder vertexbuffer = tessellator.getBuffer().getVertexBuilder();
        BufferBuilder buffer = tessellator.getBuffer();
        float f3 = 0;
        Matrix4f matrix4f = stack.getLast().getMatrix();
        float maxX = (float) boundingBox.maxX * 0.425F;
        float minX = (float) boundingBox.minX * 0.425F;
        float maxY = (float) boundingBox.maxY * 0.425F;
        float minY = (float) boundingBox.minY * 0.425F;
        float maxZ = (float) boundingBox.maxZ * 0.425F;
        float minZ = (float) boundingBox.minZ * 0.425F;
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).tex(f3 + minX - maxX, f3 + maxY - minY).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).tex(f3 + maxX - minX, f3 + maxY - minY).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 0.0F, -1.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).tex(f3 + maxX - minX, f3 + maxY - minY).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).tex(f3 + minX - maxX, f3 + maxY - minY).color(255, 255, 255, 255).normal(0.0F, 0.0F, 1.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).tex(f3 + maxX - minX, f3 + maxZ - minZ).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).tex(f3 + minX - maxX, f3 + maxZ - minZ).color(255, 255, 255, 255).normal(0.0F, -1.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).tex(f3 + maxX - minX, f3 + maxZ - minZ).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).tex(f3 + minX - maxX, f3 + maxZ - minZ).color(255, 255, 255, 255).normal(0.0F, 1.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ).tex(f3 + minX - maxX, f3 + maxY - minY).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ).tex(f3 + maxX - minX, f3 + maxY - minY).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(-1.0F, 0.0F, 0.0F).endVertex();

        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ).tex(f3 + minX - maxX, f3 + minY - maxY).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ).tex(f3 + minX - maxX, f3 + maxY - minY).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ).tex(f3 + maxX - minX, f3 + maxY - minY).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        vertexbuffer.pos(matrix4f, (float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ).tex(f3 + maxX - minX, f3 + minY - maxY).color(255, 255, 255, 255).normal(1.0F, 0.0F, 0.0F).endVertex();
        tessellator.draw();
    }

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player.getRidingEntity() != null) {
            if (player.getRidingEntity() instanceof EntityDragonBase) {
                int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
                EntityDragonBase dragon = (EntityDragonBase) player.getRidingEntity();
                float scale = ((EntityDragonBase) player.getRidingEntity()).getRenderSize() / 3;
                if (Minecraft.getInstance().gameSettings.getPointOfView() == PointOfView.THIRD_PERSON_BACK) {
                    if (currentView == 0) {
                    } else if (currentView == 1) {
                        event.getInfo().movePosition(-scale * 1.2F, 0F, 0);
                    } else if (currentView == 2) {
                        event.getInfo().movePosition(-scale * 3F, 0F, 0);
                    } else if (currentView == 3) {
                        event.getInfo().movePosition(-scale * 5F, 0F, 0);
                    }
                }
                if (Minecraft.getInstance().gameSettings.getPointOfView() == PointOfView.THIRD_PERSON_FRONT) {
                    if (currentView == 0) {
                    } else if (currentView == 1) {
                        event.getInfo().movePosition(-scale * 1.2F, 0F, 0);
                    } else if (currentView == 2) {
                        event.getInfo().movePosition(-scale * 3F, 0F, 0);
                    } else if (currentView == 3) {
                        event.getInfo().movePosition(-scale * 5F, 0F, 0);
                    }
                }

            }

        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if (player.world.isRemote && IafKeybindRegistry.dragon_change_view.isKeyDown()) {
                int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
                if (currentView + 1 > 3) {
                    currentView = 0;
                } else {
                    currentView++;
                }
                IceAndFire.PROXY.setDragon3rdPersonView(currentView);
            }

            if (player.world.isRemote) {
                GameRenderer renderer = Minecraft.getInstance().gameRenderer;
                EntitySiren siren = SirenProperties.getSiren(player);

                if (IafConfig.sirenShader && siren == null && renderer != null && renderer.getShaderGroup() != null) {
                    if (SIREN_SHADER.toString().equals(renderer.getShaderGroup().getShaderGroupName()))
                        renderer.stopUseShader();
                }

                if (siren == null)
                    return;

                boolean isCharmed = SirenProperties.isCharmed(player);

                if (IafConfig.sirenShader && !isCharmed && renderer != null && renderer.getShaderGroup() != null && SIREN_SHADER.toString().equals(renderer.getShaderGroup().getShaderGroupName())) {
                    renderer.stopUseShader();
                }

                if (isCharmed) {
                    if (player.world.isRemote && rand.nextInt(40) == 0) {
                        IceAndFire.PROXY.spawnParticle("siren_appearance", player.getPosX(), player.getPosY(), player.getPosZ(), siren.getHairColor(), 0, 0);
                    }

                    if (IafConfig.sirenShader && renderer.getShaderGroup() == null) {
                        renderer.loadShader(SIREN_SHADER);
                    }

                }

            }
        }
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
        if (event.getEntity().getRidingEntity() != null && event.getEntity().getRidingEntity() instanceof EntityDragonBase) {
            if (ClientProxy.currentDragonRiders.contains(event.getEntity().getUniqueID()) || event.getEntity() == Minecraft.getInstance().player && Minecraft.getInstance().gameSettings.getPointOfView().func_243192_a()) {
                event.setCanceled(true);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post(event.getEntity(), event.getRenderer(), event.getPartialRenderTick(), event.getMatrixStack(), event.getBuffers(), event.getLight()));
            }
        }
        if (FrozenProperties.isFrozen(event.getEntity())) {
            LivingEntity entity = event.getEntity();
            float sideExpand = -0.125F;
            float sideExpandY = 0.325F;
            AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(-entity.getWidth() / 2F - sideExpand, 0, -entity.getWidth() / 2F - sideExpand, entity.getWidth() / 2F + sideExpand, entity.getHeight() + sideExpandY, entity.getWidth() / 2F + sideExpand);
            event.getMatrixStack().push();
            event.getMatrixStack().push();
            RenderSystem.enableDepthTest();
            Minecraft.getInstance().getTextureManager().bindTexture(getIceTexture(FrozenProperties.ticksUntilUnfrozen(event.getEntity())));
            renderMovingAABB(axisalignedbb1, event.getMatrixStack());
            RenderSystem.disableDepthTest();
            event.getMatrixStack().pop();
            event.getMatrixStack().pop();
        }

    }

    @SubscribeEvent
    public void onPostRenderLiving(RenderLivingEvent.Post event) {
        LivingEntity entity = event.getEntity();
        MiscProperties.getTargetedBy(entity).forEach(caster -> {
            CockatriceBeamRender.render(entity, caster, event.getMatrixStack(), event.getBuffers(), event.getPartialRenderTick());
        });

        RenderChain.render(entity, event.getPartialRenderTick(), event.getMatrixStack(), event.getBuffers(), event.getLight());
    }

    @SubscribeEvent
    public void onGuiOpened(GuiOpenEvent event) {
        if (IafConfig.customMainMenu && event.getGui() instanceof MainMenuScreen && !(event.getGui() instanceof IceAndFireMainMenu)) {
            event.setGui(new IceAndFireMainMenu());
        }
    }

    @SubscribeEvent
    public void onEntityMount(EntityMountEvent event) {
        if (IafConfig.dragonAuto3rdPerson) {
            if (event.getEntityBeingMounted() instanceof EntityDragonBase && event.getWorldObj().isRemote && event.getEntityMounting() == Minecraft.getInstance().player) {
                EntityDragonBase dragon = (EntityDragonBase) event.getEntityBeingMounted();
                if (dragon.isTamed() && dragon.isOwner(Minecraft.getInstance().player)) {
                    if (event.isDismounting()) {
                        Minecraft.getInstance().gameSettings.setPointOfView(PointOfView.values()[IceAndFire.PROXY.getPreviousViewType()]);
                    } else {
                        IceAndFire.PROXY.setPreviousViewType(Minecraft.getInstance().gameSettings.getPointOfView().ordinal());
                        Minecraft.getInstance().gameSettings.setPointOfView(PointOfView.values()[1]);
                        IceAndFire.PROXY.setDragon3rdPersonView(2);
                    }
                }
            }
        }
    }
}