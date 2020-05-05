package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.ai.StymphalianBirdAIAirTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;

public class StymphalianBirdFlock {
    private EntityStymphalianBird leader;
    private ArrayList<EntityStymphalianBird> members = new ArrayList<>();
    private BlockPos leaderTarget;
    private BlockPos prevLeaderTarget;
    private Random random;
    private int distance = 15;

    private StymphalianBirdFlock() {
    }

    public static StymphalianBirdFlock createFlock(EntityStymphalianBird bird) {
        StymphalianBirdFlock flock = new StymphalianBirdFlock();
        flock.leader = bird;
        flock.members = new ArrayList<>();
        flock.members.add(bird);
        flock.leaderTarget = bird.airTarget;
        flock.random = bird.getRNG();
        return flock;
    }

    @Nullable
    public static StymphalianBirdFlock getNearbyFlock(EntityStymphalianBird bird) {
        float d0 = IafConfig.stymphalianBirdFlockLength;
        List<Entity> list = bird.world.getEntitiesInAABBexcluding(bird, (new AxisAlignedBB(bird.posX, bird.posY, bird.posZ, bird.posX + 1.0D, bird.posY + 1.0D, bird.posZ + 1.0D)).grow(d0, 10.0D, d0), EntityStymphalianBird.STYMPHALIAN_PREDICATE);
        Collections.sort(list, new EntityAINearestAttackableTarget.Sorter(bird));
        if (!list.isEmpty()) {
            Iterator<Entity> itr = list.iterator();
            while (itr.hasNext()) {
                Entity entity = itr.next();
                if (entity instanceof EntityStymphalianBird) {
                    EntityStymphalianBird other = (EntityStymphalianBird) entity;
                    if (other.flock != null) {
                        return other.flock;
                    }
                }

            }
        }
        return null;
    }

    public boolean isLeader(EntityStymphalianBird bird) {
        return leader != null && leader == bird;
    }

    public void addToFlock(EntityStymphalianBird bird) {
        this.members.add(bird);
    }

    public void update() {
        if (!this.members.isEmpty() && (this.leader == null || this.leader.isDead)) {
            this.leader = members.get(random.nextInt(members.size()));
        }
        if (leader != null && !leader.isDead) {
            this.prevLeaderTarget = this.leaderTarget;
            this.leaderTarget = leader.airTarget;
        }
    }

    public void onLeaderAttack(LivingEntity attackTarget) {
        for (EntityStymphalianBird bird : members) {
            if (bird.getAttackTarget() == null && !isLeader(bird)) {
                bird.setAttackTarget(attackTarget);
            }
        }
    }

    public EntityStymphalianBird getLeader() {
        return leader;
    }


    public void setTarget(BlockPos target) {
        this.leaderTarget = target;
        for (EntityStymphalianBird bird : members) {
            if (!isLeader(bird)) {
                bird.airTarget = StymphalianBirdAIAirTarget.getNearbyAirTarget(bird);
            }
        }
    }

    public void setFlying(boolean flying) {
        for (EntityStymphalianBird bird : members) {
            if (!isLeader(bird)) {
                bird.setFlying(flying);
            }
        }
    }

    public void setFearTarget(LivingEntity living) {
        for (EntityStymphalianBird bird : members) {
            bird.setVictor(living);
        }
    }
}
