package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;

public class MyrmexAIWanderHiveCenter extends EntityAIBase {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private Path path;
    private BlockPos target = BlockPos.ORIGIN;

    public MyrmexAIWanderHiveCenter(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if(!this.myrmex.canMove() || !this.myrmex.shouldEnterHive() && !this.myrmex.getNavigator().noPath() || this.myrmex.canSeeSky()){
            return false;
        }
        MyrmexHive village = MyrmexWorldData.get(this.myrmex.world).getNearestVillage(new BlockPos(this.myrmex), 300);
        if (village == null) {
            village = this.myrmex.getHive();
        }
        if (village == null) {
            return false;
        } else {
            target = getNearPos(MyrmexHive.getGroundedPos(this.myrmex.world, village.getCenter()));

            this.path = this.myrmex.getNavigator().getPathToPos(target);
            return this.path != null;
        }
    }

    public boolean shouldContinueExecuting() {
        return !this.myrmex.getNavigator().noPath() && this.myrmex.getDistanceSq(target) > 3 && this.myrmex.shouldEnterHive();
    }

    public void startExecuting() {
        this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
    }

    public void resetTask() {
        target = BlockPos.ORIGIN;
        this.myrmex.getNavigator().setPath(null, this.movementSpeed);

    }

    public BlockPos getNearPos(BlockPos pos){
        return pos.add(this.myrmex.getRNG().nextInt(15) - 7, 0, this.myrmex.getRNG().nextInt(15) - 7);
    }
}