package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
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
        this.setDefaultState(this.blockState.getBaseState().with(PLAYER_PLACED, Boolean.valueOf(false)));
    }

    @Override
    public float getBlockHardness(BlockState blockState, World worldIn, BlockPos pos) {
        return blockState.get(PLAYER_PLACED) ? super.getBlockHardness(blockState, worldIn, pos) : -1;
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        if(stack.getItem() == IafItemRegistry.DREAD_KEY){
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
            if(worldIn.getBlockState(pos).getBlock() == IafBlockRegistry.DREADWOOD_PLANKS || worldIn.getBlockState(pos).getBlock() == IafBlockRegistry.DREADWOOD_PLANKS_LOCK){
                worldIn.destroyBlock(pos, false);
                for(EnumFacing facing : EnumFacing.values()){
                    deleteNearbyWood(worldIn, pos.offset(facing), startPos);
                }
            }
        }
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(PLAYER_PLACED, Boolean.valueOf(meta > 0));
    }

    public int getMetaFromState(BlockState state) {
        return state.get(PLAYER_PLACED).booleanValue() ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PLAYER_PLACED);
    }

    public BlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().with(PLAYER_PLACED, true);
    }
}
