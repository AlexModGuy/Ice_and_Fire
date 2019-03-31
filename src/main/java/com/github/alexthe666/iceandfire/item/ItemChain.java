package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ChainEntityProperties;
import com.github.alexthe666.iceandfire.entity.EntityChainTie;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockWall;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemChain extends Item {

    public ItemChain() {
        this.setCreativeTab(IceAndFire.TAB);
        this.setTranslationKey("iceandfire.chain");
        this.setRegistryName(IceAndFire.MODID, "chain");
    }

    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(target, ChainEntityProperties.class);
        if (chainProperties != null) {
            if(chainProperties.isConnectedToEntity(playerIn)){
                chainProperties.removeChain(playerIn);
                if(!playerIn.isCreative()){
                    stack.grow(1);
                }
            }else{
                chainProperties.addChain(playerIn);
                if(!playerIn.isCreative()){
                    stack.shrink(1);
                }
            }
            chainProperties.updateConnectedEntities();
            return true;
        }
        return false;
    }

    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (!(block instanceof BlockWall)) {
            return EnumActionResult.PASS;
        } else {
            if (!worldIn.isRemote) {
                attachToFence(player, worldIn, pos);
            }

            return EnumActionResult.SUCCESS;
        }
    }

    public static boolean attachToFence(EntityPlayer player, World worldIn, BlockPos fence) {
        EntityChainTie entityleashknot = EntityChainTie.getKnotForPosition(worldIn, fence);
        boolean flag = false;
        double d0 = 30.0D;
        int i = fence.getX();
        int j = fence.getY();
        int k = fence.getZ();

        for (EntityLiving entityliving : worldIn.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
            ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(entityliving, ChainEntityProperties.class);
            if (chainProperties != null && chainProperties.isChained() && chainProperties.isConnectedToEntity(player)) {
                if (entityleashknot == null) {
                    entityleashknot = EntityChainTie.createKnot(worldIn, fence);
                }
                chainProperties.addChain(entityleashknot);
                chainProperties.removeChain(player);
                flag = true;
            }
        }

        return flag;
    }
}
