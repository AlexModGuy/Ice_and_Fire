package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;

import java.util.EnumSet;

public class SirenAIVanillaSwimming extends Goal {
    private final EntitySiren entity;

    public SirenAIVanillaSwimming(EntitySiren entityIn) {
        this.entity = entityIn;
        this.setFlags(EnumSet.of(Flag.MOVE));
        if (entityIn.getNavigation() instanceof GroundPathNavigation) {
            entityIn.getNavigation().setCanFloat(true);
        }
    }

    @Override
    public boolean canUse() {
        return (this.entity.isInWater() || this.entity.isInLava()) && this.entity.wantsToSing();
    }

    @Override
    public void tick() {
        if (this.entity.getRandom().nextFloat() < 0.8F) {
            this.entity.getJumpControl().jump();
        }
    }
}