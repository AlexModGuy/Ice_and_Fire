package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.item.ItemHippogryphEgg;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class HippogryphAIMate extends EntityAIBase {
    private final EntityHippogryph hippo;
    private final Class<? extends EntityAnimal> mateClass;
    World world;
    int spawnBabyDelay;
    double moveSpeed;
    private EntityHippogryph targetMate;

    public HippogryphAIMate(EntityHippogryph animal, double speedIn) {
        this(animal, speedIn, animal.getClass());
    }

    public HippogryphAIMate(EntityHippogryph hippogryph, double speed, Class<? extends EntityAnimal> mate) {
        this.hippo = hippogryph;
        this.world = hippogryph.world;
        this.mateClass = mate;
        this.moveSpeed = speed;
        this.setMutexBits(3);
    }

    public boolean shouldExecute() {
        if (!this.hippo.isInLove() || this.hippo.isSitting()) {
            return false;
        } else {
            this.targetMate = this.getNearbyMate();
            return this.targetMate != null;
        }
    }

    public boolean shouldContinueExecuting() {
        return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }

    public void resetTask() {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    public void updateTask() {
        this.hippo.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float) this.hippo.getVerticalFaceSpeed());
        this.hippo.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;

        if (this.spawnBabyDelay >= 60 && this.hippo.getDistanceSq(this.targetMate) < 9.0D) {
            this.spawnBaby();
        }
    }

    private EntityHippogryph getNearbyMate() {
        List<EntityHippogryph> list = this.world.getEntitiesWithinAABB(EntityHippogryph.class, this.hippo.getEntityBoundingBox().grow(8.0D));
        double d0 = Double.MAX_VALUE;
        EntityHippogryph entityanimal = null;

        for (EntityHippogryph entityanimal1 : list) {
            if (this.hippo.canMateWith(entityanimal1) && this.hippo.getDistanceSq(entityanimal1) < d0) {
                entityanimal = entityanimal1;
                d0 = this.hippo.getDistanceSq(entityanimal1);
            }
        }

        return entityanimal;
    }

    private void spawnBaby() {
        EntityItem egg = new EntityItem(world, this.hippo.posX, this.hippo.posY, this.hippo.posZ, ItemHippogryphEgg.createEggStack(this.hippo.getEnumVariant(), this.targetMate.getEnumVariant()));

        if (egg != null) {
            EntityPlayer entityplayer = this.hippo.getLoveCause();

            if (entityplayer == null && this.targetMate.getLoveCause() != null) {
                entityplayer = this.targetMate.getLoveCause();
            }

            if (entityplayer != null) {
                entityplayer.addStat(StatList.ANIMALS_BRED);
            }

            this.hippo.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            this.hippo.resetInLove();
            this.targetMate.resetInLove();
            egg.setLocationAndAngles(this.hippo.posX, this.hippo.posY, this.hippo.posZ, 0.0F, 0.0F);
            if (!world.isRemote) {
                this.world.spawnEntity(egg);
            }
            Random random = this.hippo.getRNG();

            for (int i = 0; i < 7; ++i) {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                double d3 = random.nextDouble() * (double) this.hippo.width * 2.0D - (double) this.hippo.width;
                double d4 = 0.5D + random.nextDouble() * (double) this.hippo.height;
                double d5 = random.nextDouble() * (double) this.hippo.width * 2.0D - (double) this.hippo.width;
                this.world.spawnParticle(EnumParticleTypes.HEART, this.hippo.posX + d3, this.hippo.posY + d4, this.hippo.posZ + d5, d0, d1, d2);
            }

            if (this.world.getGameRules().getBoolean("doMobLoot")) {
                this.world.spawnEntity(new EntityXPOrb(this.world, this.hippo.posX, this.hippo.posY, this.hippo.posZ, random.nextInt(7) + 1));
            }
        }
    }
}