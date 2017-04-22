package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.BlockFluidBase;

public class DragonAIBreakBlocks extends EntityAIBase {
    private EntityDragonBase dragon;

    public DragonAIBreakBlocks(EntityDragonBase dragon) {
        this.dragon = dragon;
    }

    @Override
    public boolean shouldExecute() {
        return dragon.getDragonStage() > 2 && !dragon.isModelDead();
    }

    public void updateTask() {
        for (int a = (int) Math.round(dragon.getEntityBoundingBox().minX) - 1; a <= (int) Math.round(dragon.getEntityBoundingBox().maxX) + 1; a++) {
            for (int b = (int) Math.round(dragon.getEntityBoundingBox().minY) + (dragon.isFlying() || dragon.isHovering() ? 0 : 1); (b <= (int) Math.round(dragon.getEntityBoundingBox().maxY) + 5) && (b <= 127); b++) {
                for (int c = (int) Math.round(dragon.getEntityBoundingBox().minZ) - 1; c <= (int) Math.round(dragon.getEntityBoundingBox().maxZ) + 1; c++) {
                    BlockPos pos = new BlockPos(a, b, c);
                    IBlockState state = dragon.world.getBlockState(pos);
                    if (state.getBlockHardness(dragon.world, pos) > -1 && state.getBlockHardness(dragon.world, pos) < 5 && !dragon.world.isAirBlock(pos) && state.getMaterial() != Material.PLANTS && !(state.getBlock() instanceof BlockFluidBase || state.getBlock() instanceof BlockLiquid)) {
                        switch (IceAndFire.CONFIG.dragonGriefing) {
                            case 2:
                                break;
                            case 1:
                                if (state.getBlockHardness(dragon.world, pos) < 1.5F) {
                                    dragon.world.setBlockToAir(pos);
                                }
                                break;
                            case 0:
                                if (dragon.getDragonStage() > 3) {
                                    if (state.getBlockHardness(dragon.world, pos) < 1.5F) {
                                        dragon.world.setBlockToAir(pos);
                                    }
                                } else {
                                    if (state.getBlockHardness(dragon.world, pos) <= 5F) {
                                        dragon.world.setBlockToAir(pos);
                                    }
                                }
                                break;
                        }
                        return;
                    }
                }
            }
        }
    }

}
