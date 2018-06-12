package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.Vec3d;

public class DeathWormAIWander extends EntityAIWander {

    private EntityDeathWorm worm;
    public DeathWormAIWander(EntityDeathWorm creatureIn, double speedIn) {
        super(creatureIn, speedIn);
        this.worm = creatureIn;
    }

    public boolean shouldExecute(){
        return !worm.isInSand() && super.shouldExecute();
    }

    public boolean shouldContinueExecuting(){
        return !worm.isInSand() && super.shouldContinueExecuting();
    }
}