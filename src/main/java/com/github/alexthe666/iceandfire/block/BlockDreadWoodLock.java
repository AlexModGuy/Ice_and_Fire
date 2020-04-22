package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDreadWoodLock extends Block implements IDragonProof, IDreadBlock {
    public static final PropertyBool PLAYER_PLACED = PropertyBool.create("player_placed");

    public BlockDreadWoodLock() {
        super(Material.ROCK);
        this.setHardness(4F);
        this.setResistance(1000F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire.dreadwood_planks_lock");
        this.setRegistryName(IceAndFire.MODID, "dreadwood_planks_lock");
        this.setDefaultState(this.blockState.getBaseState().withProperty(PLAYER_PLACED, Boolean.valueOf(false)));
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return blockState.getValue(PLAYER_PLACED) ? super.getBlockHardness(blockState, worldIn, pos) : -1;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        if(stack.getItem() == IafItemRegistry.dread_key){
            if(!playerIn.isCreative()){
                stack.shrink(1);
            }
            deleteNearbyWood(worldIn, pos, pos);
            worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, SoundCategory.BLOCKS, 1, 1, false);
            worldIn.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 1, 2, false);
        }
        return false;
    }

    private void deleteNearbyWood(World worldIn, BlockPos pos, BlockPos startPos) {
        if(pos.getDistance(startPos.getX(), startPos.getY(), startPos.getZ()) < 32){
            if(worldIn.getBlockState(pos).getBlock() == IafBlockRegistry.dreadwood_planks || worldIn.getBlockState(pos).getBlock() == IafBlockRegistry.dreadwood_planks_lock){
                worldIn.destroyBlock(pos, false);
                for(EnumFacing facing : EnumFacing.values()){
                    deleteNearbyWood(worldIn, pos.offset(facing), startPos);
                }
            }
        }
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(PLAYER_PLACED, Boolean.valueOf(meta > 0));
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(PLAYER_PLACED).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PLAYER_PLACED);
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(PLAYER_PLACED, true);
    }
}
