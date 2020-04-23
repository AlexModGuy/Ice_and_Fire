package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HippogryphAITargetItems<T extends EntityItem> extends EntityAITarget {
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super EntityItem> targetEntitySelector;
    private final int targetChance;
    protected EntityItem targetEntity;

    public HippogryphAITargetItems(EntityCreature creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public HippogryphAITargetItems(EntityCreature creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 20, checkSight, onlyNearby, null);
    }

    public HippogryphAITargetItems(EntityCreature creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.targetEntitySelector = new Predicate<EntityItem>() {
            @Override
            public boolean apply(@Nullable EntityItem item) {
                return item instanceof EntityItem && !item.getItem().isEmpty() && item.getItem().getItem() == Items.RABBIT_FOOT;
            }
        };
    }

    @Override
    public boolean shouldExecute() {

        if (!((EntityHippogryph) this.taskOwner).canMove()) {
            return false;
        }
        List<EntityItem> list = this.taskOwner.world.getEntitiesWithinAABB(EntityItem.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);

        if (list.isEmpty()) {
            return false;
        } else {
            Collections.sort(list, this.theNearestAttackableTargetSorter);
            this.targetEntity = list.get(0);
            return true;
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.taskOwner.getEntityBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void startExecuting() {
        this.taskOwner.getNavigator().tryMoveToXYZ(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ, 1);
        super.startExecuting();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if (this.targetEntity == null || this.targetEntity != null && this.targetEntity.isDead) {
            this.resetTask();
        }
        if (this.targetEntity != null && !this.targetEntity.isDead && this.taskOwner.getDistanceSq(this.targetEntity) < 1) {
            EntityHippogryph hippo = (EntityHippogryph) this.taskOwner;
            this.targetEntity.getItem().shrink(1);
            this.taskOwner.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
            hippo.setAnimation(EntityHippogryph.ANIMATION_EAT);
            hippo.feedings++;
            hippo.heal(4);
            if (hippo.feedings > 3 &&(hippo.feedings > 7 || hippo.getRNG().nextInt(3) == 0) && !hippo.isTamed() && this.targetEntity.getThrower() != null && !this.targetEntity.getThrower().isEmpty() && this.taskOwner.world.getPlayerEntityByName(this.targetEntity.getThrower()) != null) {
                EntityPlayer owner = this.taskOwner.world.getPlayerEntityByName(this.targetEntity.getThrower());
                hippo.setTamed(true);
                hippo.setOwnerId(owner.getUniqueID());
                hippo.setAttackTarget(null);
                hippo.setCommand(1);
                //owner.addStat(ModAchievements.tameHippogryph);
                hippo.setSitting(true);
            }
            resetTask();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.taskOwner.getNavigator().noPath();
    }

    public static class Sorter implements Comparator<Entity> {
        private final Entity theEntity;

        public Sorter(Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_) {
            double d0 = this.theEntity.getDistanceSq(p_compare_1_);
            double d1 = this.theEntity.getDistanceSq(p_compare_2_);
            return d0 < d1 ? -1 : (d0 > d1 ? 1 : 0);
        }
    }
}