package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityChainTie;
import com.github.alexthe666.iceandfire.entity.props.ChainProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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

import javax.annotation.Nullable;
import java.util.List;

public class ItemChain extends Item {

    private final boolean sticky;

    public ItemChain(boolean sticky) {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS));
        this.sticky = sticky;
    }

    public static boolean attachToFence(Player player, Level worldIn, BlockPos fence) {
        EntityChainTie entityleashknot = EntityChainTie.getKnotForPosition(worldIn, fence);
        boolean flag = false;
        double d0 = 30.0D;
        int i = fence.getX();
        int j = fence.getY();
        int k = fence.getZ();

        for (LivingEntity livingEntity : worldIn.getEntitiesOfClass(LivingEntity.class, new AABB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
            if (ChainProperties.isChainedTo(livingEntity, player)) {
                if (entityleashknot == null) {
                    entityleashknot = EntityChainTie.createTie(worldIn, fence);
                }
                ChainProperties.removeChain(livingEntity, player);
                ChainProperties.attachChain(livingEntity, entityleashknot);
                flag = true;
            }
        }

        return flag;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.iceandfire.chain.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.add(new TranslatableComponent("item.iceandfire.chain.desc_1").withStyle(ChatFormatting.GRAY));
        if (sticky) {
            tooltip.add(new TranslatableComponent("item.iceandfire.chain_sticky.desc_2").withStyle(ChatFormatting.GREEN));
            tooltip.add(new TranslatableComponent("item.iceandfire.chain_sticky.desc_3").withStyle(ChatFormatting.GREEN));
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player playerIn, LivingEntity target, InteractionHand hand) {
        if (ChainProperties.isChainedTo(target, playerIn)) {
            return InteractionResult.SUCCESS;
        } else {
            if (sticky) {

                double d0 = 60.0D;
                double i = playerIn.getX();
                double j = playerIn.getY();
                double k = playerIn.getZ();
                boolean flag = false;
                List<LivingEntity> nearbyEntities = playerIn.level.getEntitiesOfClass(LivingEntity.class, new AABB(i - d0, j - d0, k - d0, i + d0, j + d0, k + d0));
                if (playerIn.isCrouching()) {
                    ChainProperties.clearChainData(target);
                    for (LivingEntity livingEntity : nearbyEntities) {
                        if (ChainProperties.isChainedTo(livingEntity, target)) {
                            ChainProperties.removeChain(livingEntity, target);
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
                for (LivingEntity livingEntity : nearbyEntities) {
                    if (ChainProperties.isChainedTo(livingEntity, playerIn)) {
                        ChainProperties.removeChain(target, playerIn);
                        ChainProperties.removeChain(livingEntity, playerIn);
                        ChainProperties.attachChain(livingEntity, target);
                        flag = true;
                    }
                }
                if (!flag) {
                    ChainProperties.attachChain(target, playerIn);
                }
            } else {
                ChainProperties.attachChain(target, playerIn);
            }
            if (!playerIn.isCreative()) {
                stack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }

    public InteractionResult useOn(UseOnContext context) {
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
