package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.api.FoodUtils;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class CockatriceAITargetItems<T extends ItemEntity> extends TargetGoal {

    protected final DragonAITargetItems.Sorter theNearestAttackableTargetSorter;
    protected final Predicate<? super ItemEntity> targetEntitySelector;
    protected ItemEntity targetEntity;
    protected final int targetChance;
    @Nonnull
    private List<ItemEntity> list = IAFMath.emptyItemEntityList;

    public CockatriceAITargetItems(EntityCockatrice creature, boolean checkSight) {
        this(creature, checkSight, false);
    }

    public CockatriceAITargetItems(EntityCockatrice creature, boolean checkSight, boolean onlyNearby) {
        this(creature, 10, checkSight, onlyNearby, null);
    }

    public CockatriceAITargetItems(EntityCockatrice creature, int chance, boolean checkSight, boolean onlyNearby,
        @Nullable final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.targetChance = chance;
        this.targetEntitySelector = new Predicate<ItemEntity>() {
            @Override
            public boolean test(ItemEntity item) {
                return item != null && !item.getItem().isEmpty()
                    && (item.getItem().getItem() == Items.ROTTEN_FLESH || FoodUtils.isSeeds(item.getItem()));
            }
        };
    }

    @Override
    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.goalOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        }
        if ((!((EntityCockatrice) this.goalOwner).canMove()) || this.goalOwner.getHealth() >= this.goalOwner.getMaxHealth()) {
            list = IAFMath.emptyItemEntityList;
            return false;
        }

        if (this.goalOwner.world.getGameTime() % 4 == 0) // only update the list every 4 ticks
            list = this.goalOwner.world.getEntitiesWithinAABB(ItemEntity.class,
                this.getTargetableArea(this.getTargetDistance()), this.targetEntitySelector);

        if (list.isEmpty()) {
            return false;
        } else {
            list.sort(this.theNearestAttackableTargetSorter);
            this.targetEntity = list.get(0);
            return true;
        }
    }

    protected AxisAlignedBB getTargetableArea(double targetDistance) {
        return this.goalOwner.getBoundingBox().grow(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void startExecuting() {
        this.goalOwner.getNavigator().tryMoveToXYZ(this.targetEntity.getPosX(), this.targetEntity.getPosY(),
            this.targetEntity.getPosZ(), 1);
        super.startExecuting();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity == null || !this.targetEntity.isAlive()) {
            this.resetTask();
        } else if (this.goalOwner.getDistanceSq(this.targetEntity) < 1) {
            EntityCockatrice cockatrice = (EntityCockatrice) this.goalOwner;
            this.targetEntity.getItem().shrink(1);
            this.goalOwner.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1, 1);
            cockatrice.heal(8);
            cockatrice.setAnimation(EntityCockatrice.ANIMATION_EAT);
            resetTask();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.goalOwner.getNavigator().noPath();
    }

}
