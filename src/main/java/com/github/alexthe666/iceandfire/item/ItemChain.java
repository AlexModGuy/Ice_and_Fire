package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.client.StatCollector;
import com.github.alexthe666.iceandfire.entity.ChainEntityProperties;
import com.github.alexthe666.iceandfire.entity.EntityChainTie;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.client.util.ITooltipFlag;
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
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemChain extends Item {

    private boolean sticky;

    public ItemChain(boolean sticky) {
        this.sticky = sticky;
        
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        this.setTranslationKey(sticky ? "iceandfire.chain" : "iceandfire.chain_sticky");
        this.setRegistryName(IceAndFire.MODID, sticky ? "chain" : "chain_sticky");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(StatCollector.translateToLocal("item.iceandfire.chain.desc_0"));
        tooltip.add(StatCollector.translateToLocal("item.iceandfire.chain.desc_1"));
        if (!sticky) {
            tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("item.iceandfire.chain_sticky.desc_2"));
            tooltip.add(TextFormatting.GREEN + StatCollector.translateToLocal("item.iceandfire.chain_sticky.desc_3"));
        }
    }


    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        ChainEntityProperties chainProperties = EntityPropertiesHandler.INSTANCE.getProperties(target, ChainEntityProperties.class);
        if (chainProperties != null) {
            if(chainProperties.isConnectedToEntity(target, playerIn)|| chainProperties.wasJustDisconnected){
                chainProperties.wasJustDisconnected = false;
                return true;
            }else{
                if(!sticky){//for some reason, this is false for sticky ones
                    double d0 = 60.0D;
                    double i = playerIn.posX;
                    double j = playerIn.posY;
                    double k = playerIn.posZ;
                    boolean flag = false;
                    for (EntityLiving entityliving : playerIn.world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB((double) i - d0, (double) j - d0, (double) k - d0, (double) i + d0, (double) j + d0, (double) k + d0))) {
                        ChainEntityProperties otherChainProperties = EntityPropertiesHandler.INSTANCE.getProperties(entityliving, ChainEntityProperties.class);
                        if (otherChainProperties != null && otherChainProperties.isChained() && otherChainProperties.isConnectedToEntity(entityliving, playerIn)) {
                            chainProperties.addChain(target, entityliving);
                            chainProperties.removeChain(target, playerIn);
                            otherChainProperties.removeChain(target, playerIn);
                            flag = true;
                        }
                    }
                    if(!flag){
                        chainProperties.addChain(target, playerIn);
                    }
                }else{
                    chainProperties.addChain(target, playerIn);
                }
                if(!playerIn.isCreative()){
                    stack.shrink(1);
                }
            }
            chainProperties.updateConnectedEntities(target);
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
            if (chainProperties != null && chainProperties.isChained() && chainProperties.isConnectedToEntity(entityliving, player)) {
                if (entityleashknot == null) {
                    entityleashknot = EntityChainTie.createKnot(worldIn, fence);
                }
                chainProperties.addChain(entityliving, entityleashknot);
                chainProperties.removeChain(entityliving, player);
                flag = true;
            }
        }

        return flag;
    }
}
