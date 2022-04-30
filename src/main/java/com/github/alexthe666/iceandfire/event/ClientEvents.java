package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.ClientProxy;
import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.client.gui.IceAndFireMainMenu;
import com.github.alexthe666.iceandfire.client.particle.CockatriceBeamRender;
import com.github.alexthe666.iceandfire.client.render.entity.RenderChain;
import com.github.alexthe666.iceandfire.client.render.tile.RenderFrozenState;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import com.github.alexthe666.iceandfire.entity.props.FrozenProperties;
import com.github.alexthe666.iceandfire.entity.props.MiscProperties;
import com.github.alexthe666.iceandfire.entity.props.SirenProperties;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.Pathfinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class ClientEvents {

    private static final ResourceLocation SIREN_SHADER = new ResourceLocation("iceandfire:shaders/post/siren.json");

    private final Random rand = new Random();

    private static boolean shouldCancelRender(LivingEntity living) {
        if (living.getRidingEntity() != null && living.getRidingEntity() instanceof EntityDragonBase) {
            return ClientProxy.currentDragonRiders.contains(living.getUniqueID()) || living == Minecraft.getInstance().player && Minecraft.getInstance().gameSettings.getPointOfView().func_243192_a();
        }
        return false;
    }

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event) {
        if (Pathfinding.isDebug()) {
            Pathfinding.debugDraw(event.getPartialTicks(), event.getMatrixStack());
        }
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
                        event.getInfo().movePosition(-event.getInfo().calcCameraDistance(scale * 1.2F), 0F, 0);
                    } else if (currentView == 2) {
                        event.getInfo().movePosition(-event.getInfo().calcCameraDistance(scale * 3F), 0F, 0);
                    } else if (currentView == 3) {
                        event.getInfo().movePosition(-event.getInfo().calcCameraDistance(scale * 5F), 0F, 0);
                    }
                }
                if (Minecraft.getInstance().gameSettings.getPointOfView() == PointOfView.THIRD_PERSON_FRONT) {
                    if (currentView == 0) {
                    } else if (currentView == 1) {
                        event.getInfo().movePosition(-event.getInfo().calcCameraDistance(scale * 1.2F), 0F, 0);
                    } else if (currentView == 2) {
                        event.getInfo().movePosition(-event.getInfo().calcCameraDistance(scale * 3F), 0F, 0);
                    } else if (currentView == 3) {
                        event.getInfo().movePosition(-event.getInfo().calcCameraDistance(scale * 5F), 0F, 0);
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

                final boolean isCharmed = SirenProperties.isCharmed(player);

                if (IafConfig.sirenShader && !isCharmed && renderer != null && renderer.getShaderGroup() != null && SIREN_SHADER.toString().equals(renderer.getShaderGroup().getShaderGroupName())) {
                    renderer.stopUseShader();
                }

                if (isCharmed) {
                    if (player.world.isRemote && rand.nextInt(40) == 0) {
                        IceAndFire.PROXY.spawnParticle(EnumParticles.Siren_Appearance, player.getPosX(), player.getPosY(), player.getPosZ(), siren.getHairColor(), 0, 0);
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
        if (shouldCancelRender(event.getEntity())) {
            event.setCanceled(true);
        }
        if (FrozenProperties.isFrozen(event.getEntity())) {
            RenderFrozenState.render(event.getEntity(), event.getMatrixStack());
        }

    }

    @SubscribeEvent
    public void onPostRenderLiving(RenderLivingEvent.Post event) {
        if (shouldCancelRender(event.getEntity())) {
            event.setCanceled(true);
        }
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