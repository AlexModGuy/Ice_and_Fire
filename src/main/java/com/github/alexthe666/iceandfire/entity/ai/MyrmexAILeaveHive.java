package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexWorker;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class MyrmexAILeaveHive extends EntityAIBase {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private Path path;
    private BlockPos nextEntrance = BlockPos.ORIGIN;

    public MyrmexAILeaveHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if(!this.myrmex.canMove() || !this.myrmex.canMove() || !this.myrmex.shouldLeaveHive() || this.myrmex.canSeeSky() || !this.myrmex.getHeldItem(EnumHand.MAIN_HAND).isEmpty()  || this.myrmex instanceof EntityMyrmexWorker && ((EntityMyrmexWorker)this.myrmex).holdingBaby() || this.myrmex.isEnteringHive){
            return false;
        }
        MyrmexHive village = MyrmexWorldData.get(this.myrmex.world).getNearestVillage(new BlockPos(this.myrmex), 100);
        if (village == null) {
            return false;
        } else {
            nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.world, village.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRNG(), true));
            this.path = this.myrmex.getNavigator().getPathToPos(nextEntrance);
            return this.path != null;
        }
    }

    public boolean shouldContinueExecuting() {
        if(myrmex.canSeeSky() && this.myrmex.getDistanceSq(nextEntrance) <= 3 || this.myrmex.shouldEnterHive()){
            return false;
        }
        return !this.myrmex.getNavigator().noPath() && this.myrmex.getDistanceSq(nextEntrance) > 3 && this.myrmex.shouldLeaveHive();
    }

    public void startExecuting() {
        this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
    }

    public void resetTask() {
        nextEntrance = BlockPos.ORIGIN;
        this.myrmex.getNavigator().setPath(null, this.movementSpeed);
    }
}