package com.github.alexthe666.iceandfire.pathfinding;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.world.Region;

public class NodeProcessorMyrmex extends WalkNodeProcessor {

    public void func_225578_a_(Region p_225578_1_, MobEntity p_225578_2_) {
        super.func_225578_a_(p_225578_1_, p_225578_2_);
        this.entitySizeX = 1;
        this.entitySizeY = 1;
        this.entitySizeZ = 1;
    }
}
