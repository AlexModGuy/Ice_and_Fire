package com.github.alexthe666.iceandfire.pathfinding;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.FlyingNodeProcessor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Region;

public class NodeProcessorFly extends FlyingNodeProcessor {

    public void prepare(Region p_225578_1_, MobEntity p_225578_2_) {
        super.prepare(p_225578_1_, p_225578_2_);
    }

    public void setEntitySize(float width, float height) {
        this.entityWidth = MathHelper.floor(width + 1.0F);
        this.entityHeight = MathHelper.floor(height + 1.0F);
        this.entityDepth = MathHelper.floor(width + 1.0F);
    }
}
