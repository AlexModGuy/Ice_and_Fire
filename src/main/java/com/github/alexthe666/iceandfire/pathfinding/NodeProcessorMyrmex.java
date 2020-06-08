package com.github.alexthe666.iceandfire.pathfinding;

import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Region;

import java.util.EnumSet;
import java.util.Set;

public class NodeProcessorMyrmex extends WalkNodeProcessor {

    public void func_225578_a_(Region p_225578_1_, MobEntity p_225578_2_) {
        super.func_225578_a_(p_225578_1_, p_225578_2_);
        this.entitySizeX = 1;
        this.entitySizeY = 1;
        this.entitySizeZ = 1;
    }
}
