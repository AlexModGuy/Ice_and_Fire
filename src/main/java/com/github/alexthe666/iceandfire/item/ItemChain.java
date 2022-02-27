package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityChainTie;
import com.github.alexthe666.iceandfire.entity.props.ChainUtil;
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
        super(new Item.Properties().group(IceAndFire.TAB_ITEMS));
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

        for (LivingEntity livingEntity : worldIn.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
            if (ChainUtil.isChainedTo(livingEntity, player)) {
                if (entityleashknot == null) {
                    entityleashknot = EntityChainTie.createTie(worldIn, fence);
                }
                ChainUtil.removeChain(livingEntity, player);
                ChainUtil.attachChain(livingEntity, entityleashknot);
                flag = true;
            }
        }

        return flag;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.iceandfire.chain.desc_0").mergeStyle(TextFormatting.GRAY));
        tooltip.add(new TranslationTextComponent("item.iceandfire.chain.desc_1").mergeStyle(TextFormatting.GRAY));
        if (sticky) {
            tooltip.add(new TranslationTextComponent("item.iceandfire.chain_sticky.desc_2").mergeStyle(TextFormatting.GREEN));
            tooltip.add(new TranslationTextComponent("item.iceandfire.chain_sticky.desc_3").mergeStyle(TextFormatting.GREEN));
        }
    }

    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        if (ChainUtil.isChainedTo(target, playerIn)) {
            return ActionResultType.SUCCESS;
        } else {
            if (sticky) {

                double d0 = 60.0D;
                double i = playerIn.getPosX();
                double j = playerIn.getPosY();
                double k = playerIn.getPosZ();
                boolean flag = false;
                List<LivingEntity> nearbyEntities = playerIn.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(i - d0, j - d0, k - d0, i + d0, j + d0, k + d0));
                if (playerIn.isCrouching()){
                    ChainUtil.clearChainData(target);
                    for (LivingEntity livingEntity : nearbyEntities) {
                        if (ChainUtil.isChainedTo(livingEntity, target)){
                            ChainUtil.removeChain(livingEntity, target);
                        }
                    }
                    return ActionResultType.SUCCESS;
                }
                for (LivingEntity livingEntity : nearbyEntities) {
                    if (ChainUtil.isChainedTo(livingEntity, playerIn)) {
                        ChainUtil.removeChain(target, playerIn);
                        ChainUtil.removeChain(livingEntity, playerIn);
                        ChainUtil.attachChain(livingEntity, target);
                        flag = true;
                    }
                }
                if (!flag) {
                    ChainUtil.attachChain(target, playerIn);
                }
            } else {
                ChainUtil.attachChain(target, playerIn);
            }
            if (!playerIn.isCreative()) {
                stack.shrink(1);
            }
        }
        return ActionResultType.SUCCESS;
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        Block block = context.getWorld().getBlockState(context.getPos()).getBlock();

        if (!(block instanceof WallBlock)) {
            return ActionResultType.PASS;
        } else {
            if (!context.getWorld().isRemote) {
                attachToFence(context.getPlayer(), context.getWorld(), context.getPos());
            }
            return ActionResultType.SUCCESS;
        }
    }
}
