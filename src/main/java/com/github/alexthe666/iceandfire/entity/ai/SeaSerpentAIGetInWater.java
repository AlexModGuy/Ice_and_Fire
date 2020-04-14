package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntitySeaSerpent;
import com.github.alexthe666.iceandfire.entity.EntitySiren;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class SeaSerpentAIGetInWater extends EntityAIBase {

    private EntitySeaSerpent serpent;
    private final double movementSpeed;
    private final World world;
    private double shelterX;
    private double shelterY;
    private double shelterZ;

    public SeaSerpentAIGetInWater(EntitySeaSerpent serpent, double movementSpeedIn) {
        this.setMutexBits(0);
        this.movementSpeed = movementSpeedIn;
        this.world = serpent.world;
        this.serpent = serpent;
    }

    public boolean shouldExecute() {
        if (serpent.isInWater()) {
            return false;
        } else {
            Vec3d vec3d = this.findPossibleShelter();
            if (vec3d == null) {
                return false;
            } else {
                this.shelterX = vec3d.x;
                this.shelterY = vec3d.y;
                this.shelterZ = vec3d.z;
                return true;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return !this.serpent.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.serpent.getNavigator().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
    }

    @Nullable
    public Vec3d findPossibleShelter() {
        return findPossibleShelter(15, 12);
    }

    @Nullable
    protected Vec3d findPossibleShelter(int xz, int y) {
        Random random = this.serpent.getRNG();
        BlockPos blockpos = new BlockPos(this.serpent.posX, this.serpent.getEntityBoundingBox().minY, this.serpent.posZ);

        for (int i = 0; i < 10; ++i) {
            BlockPos blockpos1 = blockpos.add(random.nextInt(xz * 2) - xz, random.nextInt(y) + 2, random.nextInt(xz * 2) - xz);
            while(this.world.isAirBlock(blockpos1) && blockpos1.getY() > 3){
                blockpos1 = blockpos1.down();
            }
            if (this.world.getBlockState(blockpos1).getMaterial() == Material.WATER) {
                return new Vec3d((double) blockpos1.getX(), (double) blockpos1.getY(), (double) blockpos1.getZ());
            }
        }

        return null;
    }
}
