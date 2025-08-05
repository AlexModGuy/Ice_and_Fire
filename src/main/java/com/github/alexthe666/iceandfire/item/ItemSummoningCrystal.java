package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.world.DragonPosWorldData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.world.ForgeChunkManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemSummoningCrystal extends Item {
    public static Map<UUID, ItemSummoningCrystal> DELAYED_SUMMONS = new HashMap<>();

    private UUID dragonUuid;
    private BlockPos dragonTargetPosition;
    private float dragonTargetYaw;
    private Player summoningPlayer;
    private InteractionHand summoningHand;
    private ServerLevel serverWorld;
    private BlockPos dragonOriginPosition;
    private ItemStack stack;
    private long summoningTime = 0;

    public ItemSummoningCrystal() {
        super(new Item.Properties()/*.tab(IceAndFire.TAB_ITEMS)*/.stacksTo(1));
    }

    private static CompoundTag getDragonTag(ItemStack stack) {
        if (stack.getItem() instanceof ItemSummoningCrystal && stack.getTag() != null) {
            for (String tagInfo : stack.getTag().getAllKeys()) {
                if (tagInfo.contains("Dragon")) {
                    return stack.getTag().getCompound(tagInfo);
                }
            }
        }
        return null;
    }

    public static boolean hasDragon(ItemStack stack) {
        return getDragonTag(stack) != null;
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Level world, @NotNull Player player) {
        itemStack.setTag(new CompoundTag());
    }

    public ItemStack onItemUseFinish(Level worldIn, LivingEntity LivingEntity) {
        return new ItemStack(this);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {

        boolean flag = false;
        String desc = "entity.iceandfire.fire_dragon";
        if (stack.getItem() == IafItemRegistry.SUMMONING_CRYSTAL_ICE.get()) {
            desc = "entity.iceandfire.ice_dragon";
        }
        if (stack.getItem() == IafItemRegistry.SUMMONING_CRYSTAL_LIGHTNING.get()) {
            desc = "entity.iceandfire.lightning_dragon";
        }
        if (stack.getTag() != null) {
            for (String tagInfo : stack.getTag().getAllKeys()) {
                if (tagInfo.contains("Dragon")) {
                    CompoundTag dragonTag = stack.getTag().getCompound(tagInfo);
                    String dragonName = I18n.get(desc);
                    if (!dragonTag.getString("CustomName").isEmpty()) {
                        dragonName = dragonTag.getString("CustomName");
                    }
                    tooltip.add(Component.translatable("item.iceandfire.summoning_crystal.bound", dragonName).withStyle(ChatFormatting.GRAY));
                    flag = true;
                }
            }
        }
        if (!flag) {
            tooltip.add(Component.translatable("item.iceandfire.summoning_crystal.desc_0").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("item.iceandfire.summoning_crystal.desc_1").withStyle(ChatFormatting.GRAY));

        }

    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide) {
            return InteractionResult.PASS;
        }

        this.serverWorld = (ServerLevel) context.getLevel();
        if (serverWorld.dimension() != Level.OVERWORLD) {
            return InteractionResult.PASS;
        }
        this.summoningPlayer = context.getPlayer();
        this.summoningHand = context.getHand();
        this.stack = summoningPlayer.getItemInHand(summoningHand);
        this.dragonTargetYaw = summoningPlayer.getYRot();
        this.dragonTargetPosition = context.getClickedPos().relative(context.getClickedFace());

        if (stack.getItem() != this) {
            return InteractionResult.PASS;
        }

        CompoundTag dragonTag = getDragonTag(stack);
        if (dragonTag == null) {
            return InteractionResult.PASS;
        }

        this.dragonUuid = dragonTag.getUUID("DragonUUID");

        IceAndFire.LOGGER.info("Trying to summon dragon {} {}", this.dragonUuid, dragonTag.getString("CustomName"));
        Entity entity = serverWorld.getEntity(this.dragonUuid);
        // If the dragon is already loaded, summon it immediately
        if (entity != null) {
            summonEntity(entity, this.serverWorld, this.dragonTargetPosition, this.dragonTargetYaw);
            return InteractionResult.PASS;
        }

        // early exit if we are not allowed to load chunks to summon
        if (!IafConfig.chunkLoadSummonCrystal) {
            IceAndFire.LOGGER.info("Dragon entity {} not loaded, and chunk loading is disabled", this.dragonUuid);
            this.displayClientError();
            return InteractionResult.PASS;
        }

        DragonPosWorldData data = DragonPosWorldData.get(this.serverWorld);
        if (data == null) {
            IceAndFire.LOGGER.warn("Unable to load DragonPosWorldData for world {}", this.serverWorld);
            this.displayClientError();
            return InteractionResult.PASS;
        }

        this.dragonOriginPosition = data.getDragonPos(this.dragonUuid);

        if (this.dragonOriginPosition == null) {
            IceAndFire.LOGGER.warn("Summoning dragon origin position unknown for dragon {}", this.dragonUuid);
            this.displayClientError();
            return InteractionResult.PASS;
        }

        ChunkPos pos = new ChunkPos(this.dragonOriginPosition);
        IceAndFire.LOGGER.info("Dragon entity not loaded, loading chunk {}", pos);

        // try to load the chunk, and mark the entity to be summoned as soon as it gets added to the world
        if (ForgeChunkManager.forceChunk(serverWorld, IceAndFire.MODID, this.summoningPlayer, pos.x, pos.z, true, false)) {
            this.summoningTime = serverWorld.getGameTime();
            DELAYED_SUMMONS.put(this.dragonUuid, this);
        } else {
            IceAndFire.LOGGER.warn("Failed to force load chunk with Dragon {}", this.dragonUuid);
        }

        return InteractionResult.PASS;
    }

    public void summonEntity(Entity entity, Level worldIn, BlockPos offsetPos, float yaw) {
        entity.moveTo(offsetPos.getX() + 0.5D, offsetPos.getY() + 0.5D, offsetPos.getZ() + 0.5D, yaw, 0);
        if (entity instanceof EntityDragonBase) {
            ((EntityDragonBase) entity).setCrystalBound(false);
        }
        if (IafConfig.chunkLoadSummonCrystal) {
            DragonPosWorldData data = DragonPosWorldData.get(worldIn);
            if (data != null) {
                data.removeDragon(entity.getUUID());
            }
        }
        this.summoningPlayer.playSound(SoundEvents.ENDERMAN_TELEPORT, 1, 1);
        this.summoningPlayer.playSound(SoundEvents.GLASS_BREAK, 1, 1);
        this.summoningPlayer.swing(this.summoningHand);
        this.summoningPlayer.displayClientMessage(Component.translatable("message.iceandfire.dragonTeleport"), true);
        stack.setTag(new CompoundTag());
    }

    public void delayedSummon() {
        DELAYED_SUMMONS.remove(this.dragonUuid);
        ChunkPos pos = new ChunkPos(this.dragonOriginPosition);

        // make delayed summons expire if for some reason the chunk loading, or entity loading takes too long
        // wouldn't want the dragon to summon minutes later at the original position
        final long SUMMON_DELAY_TOLERANCE = 2_000;
        if (this.serverWorld.getGameTime() - SUMMON_DELAY_TOLERANCE > this.summoningTime) {
            IceAndFire.LOGGER.info("Dragon summon timed out for dragon {}; unloading chunk {}", dragonUuid, pos);
        } else {
            Entity entity = serverWorld.getEntity(this.dragonUuid);
            if (entity != null) {
                if (this.summoningPlayer.isAlive()) {
                    summonEntity(entity, this.serverWorld, this.dragonTargetPosition, this.dragonTargetYaw);
                    IceAndFire.LOGGER.info("Summoned dragon {} and unloading chunk {}", dragonUuid, pos);
                } else {
                    IceAndFire.LOGGER.info("Player died since summoning {}; unloading chunk {}", dragonUuid, pos);
                }
            }
        }

        ForgeChunkManager.forceChunk(this.serverWorld, IceAndFire.MODID, this.summoningPlayer, pos.x, pos.z, false, false);

        this.dragonUuid = null;
        this.summoningPlayer = null;
        this.summoningHand = null;
        this.stack = null;
        this.dragonTargetPosition = null;
        this.serverWorld = null;
        this.dragonOriginPosition = null;
        this.summoningTime = 0;
    }

    private void displayClientError() {
        summoningPlayer.displayClientMessage(Component.translatable("message.iceandfire.noDragonTeleport"), true);
    }
}
