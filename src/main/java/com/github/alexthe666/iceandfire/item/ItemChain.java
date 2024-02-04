package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityChainTie;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemChain extends Item {

    private final boolean sticky;

    public ItemChain(boolean sticky) {
        super(new Item.Properties()/*.tab(IceAndFire.TAB_ITEMS)*/);
        this.sticky = sticky;
    }

    public static void attachToFence(Player player, Level worldIn, BlockPos fence) {
        double d0 = 30.0D;
        int i = fence.getX();
        int j = fence.getY();
        int k = fence.getZ();

        for (LivingEntity livingEntity : worldIn.getEntitiesOfClass(LivingEntity.class, new AABB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
            EntityDataProvider.getCapability(livingEntity).ifPresent(data -> {
                if (data.chainData.isChainedTo(player)) {
                    EntityChainTie entityleashknot = EntityChainTie.getKnotForPosition(worldIn, fence);

                    if (entityleashknot == null) {
                        entityleashknot = EntityChainTie.createTie(worldIn, fence);
                    }

                    data.chainData.removeChain(player);
                    data.chainData.attachChain(entityleashknot);
                }
            });
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.iceandfire.chain.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("item.iceandfire.chain.desc_1").withStyle(ChatFormatting.GRAY));
        if (sticky) {
            tooltip.add(Component.translatable("item.iceandfire.chain_sticky.desc_2").withStyle(ChatFormatting.GREEN));
            tooltip.add(Component.translatable("item.iceandfire.chain_sticky.desc_3").withStyle(ChatFormatting.GREEN));
        }
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, @NotNull Player playerIn, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        EntityDataProvider.getCapability(target).ifPresent(targetData -> {
            if (targetData.chainData.isChainedTo(playerIn)) {
                return;
            }

            if (sticky) {
                double d0 = 60.0D;
                double i = playerIn.getX();
                double j = playerIn.getY();
                double k = playerIn.getZ();
                List<LivingEntity> nearbyEntities = playerIn.level().getEntitiesOfClass(LivingEntity.class, new AABB(i - d0, j - d0, k - d0, i + d0, j + d0, k + d0));

                if (playerIn.isCrouching()) {
                    targetData.chainData.clearChains();

                    for (LivingEntity livingEntity : nearbyEntities) {
                        EntityDataProvider.getCapability(livingEntity).ifPresent(nearbyData -> nearbyData.chainData.removeChain(target));
                    }

                    return;
                }

                AtomicBoolean flag = new AtomicBoolean(false);

                for (LivingEntity livingEntity : nearbyEntities) {
                    EntityDataProvider.getCapability(livingEntity).ifPresent(nearbyData -> {
                        if (nearbyData.chainData.isChainedTo(playerIn)) {
                            targetData.chainData.removeChain(playerIn);
                            nearbyData.chainData.removeChain(playerIn);
                            nearbyData.chainData.attachChain(target);

                            flag.set(true);
                        }
                    });
                }

                if (!flag.get()) {
                    targetData.chainData.attachChain(playerIn);
                }
            } else {
                targetData.chainData.attachChain(playerIn);
            }

            if (!playerIn.isCreative()) {
                stack.shrink(1);
            }
        });

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Block block = context.getLevel().getBlockState(context.getClickedPos()).getBlock();

        if (!(block instanceof WallBlock)) {
            return InteractionResult.PASS;
        } else {
            if (!context.getLevel().isClientSide) {
                attachToFence(context.getPlayer(), context.getLevel(), context.getClickedPos());
            }
            return InteractionResult.SUCCESS;
        }
    }
}
