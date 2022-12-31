package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

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
                .requiresCorrectToolForDrops()
        );
    }

    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return !stateIn.canSurvive(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
    }

    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    private boolean isValidGround(BlockState blockState, LevelReader worldIn, BlockPos blockpos) {
        return blockState.canOcclude();
    }

    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AABB;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AABB;
    }

    public void stepOn(Level worldIn, BlockPos pos, Entity entityIn) {
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
