package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CockatriceAITargetItems<T extends EntityItem> extends EntityAITarget {
    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super EntityItem> targetEntitySelector;
    private final int targetChance;
    protected EntityItem targetEntity;

    public CockatriceAITargetItems(EntityCockatrice creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public CockatriceAITargetItems(EntityCockatrice creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 0, checkSight, onlyNearby, (Predicate<? super EntityItem>) null);
    }

    public CockatriceAITargetItems(EntityCockatrice creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.targetEntitySelector = new Predicate<EntityItem>() {
            @Override
            public boolean apply(@Nullable EntityItem item) {
                return item instanceof EntityItem && !item.getItem().isEmpty() && (item.getItem().getItem() == Items.ROTTEN_FLESH || FoodUtils.isSeeds(item.getItem()));
            }
        };
    }

    @Override
    public boolean shouldExecute() {

        if (!((EntityCockatrice) this.taskOwner).canMove()) {
            return false;
        }
        if (this.taskOwner.getHealth() >= this.taskOwner.getMaxHealth()) {
            return false;
        }
        List<EntityItem> list = this.taskOwner.world.<EntityItem>getEntitiesWithinAABB(EntityItem.class, this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);

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
            EntityCockatrice cockatrice = (EntityCockatrice) this.taskOwner;
            this.targetEntity.getItem().shrink(1);
            this.taskOwner.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
            cockatrice.heal(8);
            cockatrice.setAnimation(EntityCockatrice.ANIMATION_EAT);
            resetTask();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.taskOwner.getNavigator().noPath();
    }


}