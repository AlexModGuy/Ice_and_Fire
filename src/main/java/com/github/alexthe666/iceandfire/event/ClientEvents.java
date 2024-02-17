package com.github.alexthe666.iceandfire.event;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.ClientProxy;
import com.github.alexthe666.iceandfire.client.IafKeybindRegistry;
import com.github.alexthe666.iceandfire.client.gui.IceAndFireMainMenu;
import com.github.alexthe666.iceandfire.client.particle.CockatriceBeamRender;
import com.github.alexthe666.iceandfire.client.render.entity.RenderChain;
import com.github.alexthe666.iceandfire.client.render.tile.RenderFrozenState;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.util.ICustomMoveController;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.message.MessageDragonControl;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.WorldEventContext;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = IceAndFire.MODID, value = Dist.CLIENT)
public class ClientEvents {

    private static final ResourceLocation SIREN_SHADER = new ResourceLocation("iceandfire:shaders/post/siren.json");

    private final Random rand = new Random();

    private static boolean shouldCancelRender(LivingEntity living) {
        if (living.getVehicle() != null && living.getVehicle() instanceof EntityDragonBase) {
            return ClientProxy.currentDragonRiders.contains(living.getUUID()) || living == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson();
        }
        return false;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderWorldLastEvent(@NotNull final RenderLevelStageEvent event)
    {
        WorldEventContext.INSTANCE.renderWorldLastEvent(event);
    }

    @SubscribeEvent
    public void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        Player player = Minecraft.getInstance().player;
        if (player.getVehicle() != null) {
            if (player.getVehicle() instanceof EntityDragonBase) {
                int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
                float scale = ((EntityDragonBase) player.getVehicle()).getRenderSize() / 3;
                if (Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK ||
                        Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_FRONT) {
                    if (currentView == 1) {
                        event.getCamera().move(-event.getCamera().getMaxZoom(scale * 1.2F), 0F, 0);
                    } else if (currentView == 2) {
                        event.getCamera().move(-event.getCamera().getMaxZoom(scale * 3F), 0F, 0);
                    } else if (currentView == 3) {
                        event.getCamera().move(-event.getCamera().getMaxZoom(scale * 5F), 0F, 0);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (event.getEntity() instanceof ICustomMoveController) {
            Entity entity = event.getEntity();
            ICustomMoveController moveController = ((Entity & ICustomMoveController) event.getEntity());
            if (entity.getVehicle() != null && entity.getVehicle() == mc.player) {
                byte previousState = moveController.getControlState();
                moveController.dismount(mc.options.keyShift.isDown());
                byte controlState = moveController.getControlState();
                if (controlState != previousState) {
                    IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(entity.getId(), controlState, entity.getX(), entity.getY(), entity.getZ()));
                }
            }
        }
        if (event.getEntity() instanceof Player player) {
            if (player.level().isClientSide) {

                if (player.getVehicle() instanceof ICustomMoveController) {
                    Entity entity = player.getVehicle();
                    ICustomMoveController moveController = ((Entity & ICustomMoveController) player.getVehicle());
                    byte previousState = moveController.getControlState();
                    moveController.up(mc.options.keyJump.isDown());
                    moveController.down(IafKeybindRegistry.dragon_down.isDown());
                    moveController.attack(IafKeybindRegistry.dragon_strike.isDown());
                    moveController.dismount(mc.options.keyShift.isDown());
                    moveController.strike(IafKeybindRegistry.dragon_fireAttack.isDown());
                    byte controlState = moveController.getControlState();
                    if (controlState != previousState) {
                        IceAndFire.NETWORK_WRAPPER.sendToServer(new MessageDragonControl(entity.getId(), controlState, entity.getX(), entity.getY(), entity.getZ()));
                    }
                }
            }
            if (player.level().isClientSide && IafKeybindRegistry.dragon_change_view.isDown()) {
                int currentView = IceAndFire.PROXY.getDragon3rdPersonView();
                if (currentView + 1 > 3) {
                    currentView = 0;
                } else {
                    currentView++;
                }
                IceAndFire.PROXY.setDragon3rdPersonView(currentView);
            }

            if (player.level().isClientSide) {
                GameRenderer renderer = Minecraft.getInstance().gameRenderer;

                EntityDataProvider.getCapability(player).ifPresent(data -> {
                    if (IafConfig.sirenShader && data.sirenData.charmedBy == null && renderer.currentEffect() != null) {
                        if (SIREN_SHADER.toString().equals(renderer.currentEffect().getName()))
                            renderer.shutdownEffect();
                    }

                    if (data.sirenData.charmedBy == null) {
                        return;
                    }

                    if (IafConfig.sirenShader && !data.sirenData.isCharmed && renderer.currentEffect() != null && SIREN_SHADER.toString().equals(renderer.currentEffect().getName())) {
                        renderer.shutdownEffect();
                    }

                if (data.sirenData.isCharmed) {
                    if (player.level().isClientSide && rand.nextInt(40) == 0) {
                        IceAndFire.PROXY.spawnParticle(EnumParticles.Siren_Appearance, player.getX(), player.getY(), player.getZ(), data.sirenData.charmedBy.getHairColor(), 0, 0);
                    }

                        if (IafConfig.sirenShader && renderer.currentEffect() == null) {
                            renderer.loadEffect(SIREN_SHADER);
                        }

                    }
                });
            }
        }
    }

    @SubscribeEvent
    public void onPreRenderLiving(RenderLivingEvent.Pre event) {
        if (shouldCancelRender(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPostRenderLiving(RenderLivingEvent.Post event) {
        if (shouldCancelRender(event.getEntity())) {
            event.setCanceled(true);
        }

        LivingEntity entity = event.getEntity();

        EntityDataProvider.getCapability(entity).ifPresent(data -> {
            for (LivingEntity target : data.miscData.getTargetedByScepter()) {
                CockatriceBeamRender.render(entity, target, event.getPoseStack(), event.getMultiBufferSource(), event.getPartialTick());
            }

            if (data.frozenData.isFrozen) {
                RenderFrozenState.render(event.getEntity(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), data.frozenData.frozenTicks);
            }

            RenderChain.render(entity, event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), data.chainData.getChainedTo());
        });
    }

    @SubscribeEvent
    public void onGuiOpened(ScreenEvent.Opening event) {
        if (IafConfig.customMainMenu && event.getScreen() instanceof TitleScreen && !(event.getScreen() instanceof IceAndFireMainMenu)) {
            event.setNewScreen(new IceAndFireMainMenu());
        }
    }

    // TODO: add this to client side config
    public final boolean AUTO_ADAPT_3RD_PERSON = true;

    @SubscribeEvent
    public void onEntityMount(EntityMountEvent event) {
        if (event.getEntityBeingMounted() instanceof EntityDragonBase dragon && event.getLevel().isClientSide && event.getEntityMounting() == Minecraft.getInstance().player) {
            if (dragon.isTame() && dragon.isOwnedBy(Minecraft.getInstance().player)) {
                if (AUTO_ADAPT_3RD_PERSON) {
                    // Auto adjust 3rd person camera's according to dragon's size
                    IceAndFire.PROXY.setDragon3rdPersonView(2);
                }
                if (IafConfig.dragonAuto3rdPerson) {
                    if (event.isDismounting()) {
                        Minecraft.getInstance().options.setCameraType(CameraType.values()[IceAndFire.PROXY.getPreviousViewType()]);
                    } else {
                        IceAndFire.PROXY.setPreviousViewType(Minecraft.getInstance().options.getCameraType().ordinal());
                        Minecraft.getInstance().options.setCameraType(CameraType.values()[1]);
                    }
                }
            }
        }
    }
}