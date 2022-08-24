package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityChainTie;
import com.github.alexthe666.iceandfire.entity.props.ChainProperties;
import net.minecraft.block.Block;
import net.minecraft.block.WallBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemChain extends Item {

    private final boolean sticky;

    public ItemChain(boolean sticky) {
        super(new Item.Properties().tab(IceAndFire.TAB_ITEMS));
        this.sticky = sticky;
        this.setRegistryName(IceAndFire.MODID, sticky ? "chain_sticky" : "chain");
    }

    public static boolean attachToFence(PlayerEntity player, World worldIn, BlockPos fence) {
        EntityChainTie entityleashknot = EntityChainTie.getKnotForPosition(worldIn, fence);
        boolean flag = false;
        double d0 = 30.0D;
        int i = fence.getX();
        int j = fence.getY();
        int k = fence.getZ();

        for (LivingEntity livingEntity : worldIn.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
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
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.chain.desc_0").withStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.chain.desc_1").withStyle(TextFormatting.GRAY));
        if (sticky) {
            tooltip.add(new TranslationTextComponent("item.iceandfire.chain_sticky.desc_2").withStyle(TextFormatting.GREEN));
            tooltip.add(new TranslationTextComponent("item.iceandfire.chain_sticky.desc_3").withStyle(TextFormatting.GREEN));
        }
    }

    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (ChainProperties.isChainedTo(target, playerIn)) {
            return ActionResultType.SUCCESS;
        } else {
            if (sticky) {

                double d0 = 60.0D;
                double i = playerIn.getX();
                double j = playerIn.getY();
                double k = playerIn.getZ();
                boolean flag = false;
                List<LivingEntity> nearbyEntities = playerIn.level.getEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(i - d0, j - d0, k - d0, i + d0, j + d0, k + d0));
                if (playerIn.isCrouching()) {
                    ChainProperties.clearChainData(target);
                    for (LivingEntity livingEntity : nearbyEntities) {
                        if (ChainProperties.isChainedTo(livingEntity, target)) {
                            ChainProperties.removeChain(livingEntity, target);
                        }
                    }
                    return ActionResultType.SUCCESS;
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
        return ActionResultType.SUCCESS;
    }

    public ActionResultType useOn(ItemUseContext context) {
        Block block = context.getLevel().getBlockState(context.getClickedPos()).getBlock();

        if (!(block instanceof WallBlock)) {
            return ActionResultType.PASS;
        } else {
            if (!context.getLevel().isClientSide) {
                attachToFence(context.getPlayer(), context.getLevel(), context.getClickedPos());
            }
            return ActionResultType.SUCCESS;
        }
    }
}
