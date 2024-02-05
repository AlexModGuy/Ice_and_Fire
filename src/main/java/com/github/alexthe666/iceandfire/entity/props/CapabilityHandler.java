package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber // TODO :: clone on death (and potentially on entering end portal)
public class CapabilityHandler {
    public static final Capability<EntityData> ENTITY_DATA_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    public static final ResourceLocation ENTITY_DATA = new ResourceLocation(IceAndFire.MODID, "entity_data");

    @SubscribeEvent
    public static void attachCapability(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(ENTITY_DATA, new EntityDataProvider());
        }
    }

    @SubscribeEvent
    public static void handleInitialSync(final EntityJoinLevelEvent event) {
        // TODO :: only sync if values are not the default values?
        // FIXME :: seems like it's not properly initialized on re-join (no chains are rendered (or even attached))?
        syncEntityData(event.getEntity());
    }

    @SubscribeEvent
    public static void removeCachedEntry(final EntityLeaveLevelEvent event) {
        EntityDataProvider.removeCachedEntry(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerStartTracking(final PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof LivingEntity target && event.getEntity() instanceof ServerPlayer serverPlayer) {
            EntityDataProvider.getCapability(target).ifPresent(data -> IceAndFire.sendMSGToPlayer(new SyncEntityData(target.getId(), data.serialize()), serverPlayer));
        }
    }

    @SubscribeEvent
    public static void tickData(final LivingEvent.LivingTickEvent event) {
        EntityDataProvider.getCapability(event.getEntity()).ifPresent(data -> data.tick(event.getEntity()));
    }

    public static void syncEntityData(final Entity entity) {
        if (entity.getLevel().isClientSide() || !(entity instanceof LivingEntity)) {
            return;
        }

        if (entity instanceof ServerPlayer serverPlayer) {
            EntityDataProvider.getCapability(entity).ifPresent(data -> IceAndFire.NETWORK_WRAPPER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayer), new SyncEntityData(entity.getId(), data.serialize())));
        } else {
            EntityDataProvider.getCapability(entity).ifPresent(data -> IceAndFire.NETWORK_WRAPPER.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), new SyncEntityData(entity.getId(), data.serialize())));
        }
    }

    public static @Nullable Player getLocalPlayer() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return Minecraft.getInstance().player;
        }

        return null;
    }
}
