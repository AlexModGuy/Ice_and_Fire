package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import com.google.common.collect.Lists;

import java.util.List;

import jdk.nashorn.internal.ir.Block;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;

public class MyrmexAIMoveThroughHive extends EntityAIBase {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private Path path;
    private final boolean isNocturnal;
    private BlockPos villageCenter = BlockPos.ORIGIN;

    public MyrmexAIMoveThroughHive(EntityMyrmexBase entityIn, double movementSpeedIn, boolean isNocturnalIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.isNocturnal = isNocturnalIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if (this.isNocturnal && this.myrmex.world.isDaytime()) {
            return false;
        } else {
            MyrmexHive village = MyrmexWorldData.get(this.myrmex.world).getNearestVillage(new BlockPos(this.myrmex), 0);

            if (village == null) {
                return false;
            } else {
                villageCenter = village.getCenter();
                Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.myrmex, 10, 7, new Vec3d(villageCenter));
                if (vec3d == null) {
                    return false;
                } else {
                    this.path = this.myrmex.getNavigator().getPathToXYZ(vec3d.x, vec3d.y, vec3d.z);
                    return this.path != null;
                }
            }
        }
    }

    public boolean shouldContinueExecuting() {
        if (this.myrmex.getNavigator().noPath()) {
            return false;
        } else {
            float f = this.myrmex.width + 4.0F;
            return this.myrmex.getDistanceSq(villageCenter) > (double) (f * f);
        }
    }

    public void startExecuting() {
        this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
    }

    public void resetTask() {
        villageCenter = BlockPos.ORIGIN;
    }
}