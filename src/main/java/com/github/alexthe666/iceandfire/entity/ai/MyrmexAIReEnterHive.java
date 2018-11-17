package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;

public class MyrmexAIReEnterHive extends EntityAIBase {
    private final EntityMyrmexBase myrmex;
    private final double movementSpeed;
    private Path path;
    private BlockPos nextEntrance = BlockPos.ORIGIN;
    private boolean first = true;

    public MyrmexAIReEnterHive(EntityMyrmexBase entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        if(!this.myrmex.canMove() || this.myrmex.shouldLeaveHive() || !this.myrmex.canSeeSky() || !first || !this.myrmex.canSeeSky()){
            return false;
        }
        MyrmexHive village = this.myrmex.getHive();
        if (village == null) {
            village = MyrmexWorldData.get(this.myrmex.world).getNearestHive(new BlockPos(this.myrmex), 500);
        }
        if (village == null) {
            return false;
        } else {
            nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.world, village.getClosestEntranceToEntity(this.myrmex, this.myrmex.getRNG(), false));
            this.path = this.myrmex.getNavigator().getPathToPos(nextEntrance);
            first = true;
            return this.path != null;
        }
    }

    public void updateTask(){
        this.path = this.myrmex.getNavigator().getPathToPos(nextEntrance);
        this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);

        if(this.myrmex.getDistanceSq(nextEntrance) < 9 && first){
            MyrmexHive village = MyrmexWorldData.get(this.myrmex.world).getNearestHive(new BlockPos(this.myrmex), 100);
            if(village != null){
                nextEntrance = MyrmexHive.getGroundedPos(this.myrmex.world, village.getClosestEntranceBottomToEntity(this.myrmex, this.myrmex.getRNG()));
                first = false;
                this.myrmex.getNavigator().clearPath();
                this.path = this.myrmex.getNavigator().getPathToPos(nextEntrance);
                this.myrmex.getNavigator().setPath(this.path, this.movementSpeed);
            }
        }
        if(this.myrmex.getDistanceSq(nextEntrance) < 5 && !first){
            this.myrmex.isEnteringHive = false;
        }else{
            this.myrmex.isEnteringHive = true;
        }
    }

    public boolean shouldContinueExecuting() {
        if(this.myrmex.getDistanceSq(nextEntrance) < 3 && !first){
            return false;
        }
        return this.myrmex.shouldEnterHive();
    }

    public void resetTask() {
        nextEntrance = BlockPos.ORIGIN;
        this.myrmex.getNavigator().setPath(null, this.movementSpeed);
        first = true;
    }
}