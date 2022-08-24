package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.vector.Vector3d;

import java.util.EnumSet;

public class CockatriceAIWander extends Goal {
    private final EntityCockatrice cockatrice;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private final double speed;
    private int executionChance;
    private boolean mustUpdate;

    public CockatriceAIWander(EntityCockatrice creatureIn, double speedIn) {
        this(creatureIn, speedIn, 20);
    }

    public CockatriceAIWander(EntityCockatrice creatureIn, double speedIn, int chance) {
        this.cockatrice = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!cockatrice.canMove()) {
            return false;
        }
        if (cockatrice.getCommand() != 3 && cockatrice.getCommand() != 0) {
            return false;
        }
        if (!this.mustUpdate) {
            if (this.cockatrice.getRandom().nextInt(executionChance) != 0) {
                return false;
            }
        }
        Vector3d Vector3d = RandomPositionGenerator.getPos(this.cockatrice, 10, 7);
        if (Vector3d == null) {
            return false;
        } else {
            this.xPosition = Vector3d.x;
            this.yPosition = Vector3d.y;
            this.zPosition = Vector3d.z;
            this.mustUpdate = false;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.cockatrice.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.cockatrice.getNavigation().moveTo(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int newchance) {
        this.executionChance = newchance;
    }
}