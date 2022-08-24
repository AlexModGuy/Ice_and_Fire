package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BlockIceSpikes extends Block {
    protected static final VoxelShape AABB = Block.box(1, 0, 1, 15, 8, 15);
    public Item itemBlock;

    public BlockIceSpikes() {
        super(
            Properties
                .of(Material.ICE_SOLID)
                .noOcclusion()
                .dynamicShape()
                .randomTicks()
                .sound(SoundType.GLASS)
                .strength(2.5F)
                .harvestLevel(1)
                .harvestTool(ToolType.PICKAXE)
                .requiresCorrectToolForDrops()
        );

        this.setRegistryName(IceAndFire.MODID, "dragon_ice_spikes");
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }

    private boolean isValidGround(BlockState blockState, IWorldReader worldIn, BlockPos blockpos) {
        return blockState.canOcclude();
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    public void stepOn(World worldIn, BlockPos pos, Entity entityIn) {
        if (!(entityIn instanceof EntityIceDragon)) {
            entityIn.hurt(DamageSource.CACTUS, 1);
            if (entityIn instanceof LivingEntity && entityIn.getDeltaMovement().x != 0 && entityIn.getDeltaMovement().z != 0) {
                ((LivingEntity) entityIn).knockback(0.5F, entityIn.getDeltaMovement().x, entityIn.getDeltaMovement().z);
            }
        }
    }

    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

}
