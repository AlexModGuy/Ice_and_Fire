package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class BlockIceSpikes extends Block {
    protected static final VoxelShape AABB = Block.makeCuboidShape(1, 0, 1, 15, 8, 15);
    public Item itemBlock;

    public BlockIceSpikes() {
        super(Properties.create(Material.PACKED_ICE).notSolid().variableOpacity().sound(SoundType.GLASS).hardnessAndResistance(2.5F).harvestLevel(1).harvestTool(ToolType.PICKAXE));
        this.setRegistryName(IceAndFire.MODID, "dragon_ice_spikes");
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABB;
    }

    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (!(entityIn instanceof EntityIceDragon)) {
            entityIn.attackEntityFrom(DamageSource.CACTUS, 1);
            if (entityIn instanceof LivingEntity && entityIn.getMotion().x != 0 && entityIn.getMotion().z != 0) {
                ((LivingEntity) entityIn).knockBack(entityIn, 0.5F, entityIn.getMotion().x, entityIn.getMotion().z);
            }
        }
    }

    public boolean isTransparent(BlockState state) {
        return true;
    }

}
