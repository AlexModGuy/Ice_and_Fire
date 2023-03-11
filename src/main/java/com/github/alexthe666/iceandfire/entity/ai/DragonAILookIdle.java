package com.github.alexthe666.iceandfire.entity.ai;

import java.util.EnumSet;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;

public class DragonAILookIdle extends Goal {
    private EntityDragonBase dragon;
    private double lookX;
    private double lookZ;
    private int idleTime;

    public DragonAILookIdle(EntityDragonBase prehistoric) {
        this.dragon = prehistoric;
        this.setMutexFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.dragon.canMove() || dragon.getAnimation() == EntityDragonBase.ANIMATION_SHAKEPREY || dragon.isFuelingForge()) {
            return false;
        }
        return this.dragon.getRNG().nextFloat() < 0.02F;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.idleTime >= 0 && this.dragon.canMove();
    }

    @Override
    public void startExecuting() {
        final double d0 = (Math.PI * 2D) * this.dragon.getRNG().nextDouble();
        this.lookX = MathHelper.cos((float) d0);
        this.lookZ = MathHelper.sin((float) d0);
        this.idleTime = 20 + this.dragon.getRNG().nextInt(20);
    }

    @Override
    public void tick() {
    	if (this.idleTime > 0) {
    		--this.idleTime;
    	}
        this.dragon.getLookController().setLookPosition(this.dragon.getPosX() + this.lookX, this.dragon.getPosY() + this.dragon.getEyeHeight(), this.dragon.getPosZ() + this.lookZ, this.dragon.getHorizontalFaceSpeed(), this.dragon.getVerticalFaceSpeed());
    }
}