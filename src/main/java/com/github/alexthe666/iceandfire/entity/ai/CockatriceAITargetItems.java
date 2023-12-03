package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.util.IAFMath;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;

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

    public CockatriceAITargetItems(EntityCockatrice creature, int chance, boolean checkSight, boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.theNearestAttackableTargetSorter = new DragonAITargetItems.Sorter(creature);
        this.targetChance = chance;
        this.targetEntitySelector = (Predicate<ItemEntity>) item -> item != null && !item.getItem().isEmpty() && item.getItem().is(IafItemTags.HEAL_COCKATRICE);
    }

    @Override
    public boolean canUse() {

        if (this.targetChance > 0 && this.mob.getRandom().nextInt(this.targetChance) != 0) {
            return false;
        }

        if ((!((EntityCockatrice) this.mob).canMove()) || this.mob.getHealth() >= this.mob.getMaxHealth()) {
            list = IAFMath.emptyItemEntityList;
            return false;
        }

        if (this.mob.level().getGameTime() % 4 == 0) // only update the list every 4 ticks
            list = this.mob.level().getEntitiesOfClass(ItemEntity.class,
                this.getTargetableArea(this.getFollowDistance()), this.targetEntitySelector);

        if (list.isEmpty()) {
            return false;
        } else {
            list.sort(this.theNearestAttackableTargetSorter);
            this.targetEntity = list.get(0);
            return true;
        }
    }

    protected AABB getTargetableArea(double targetDistance) {
        return this.mob.getBoundingBox().inflate(targetDistance, 4.0D, targetDistance);
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.targetEntity.getX(), this.targetEntity.getY(),
            this.targetEntity.getZ(), 1);
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetEntity == null || !this.targetEntity.isAlive()) {
            this.stop();
        } else if (this.mob.distanceToSqr(this.targetEntity) < 1) {
            EntityCockatrice cockatrice = (EntityCockatrice) this.mob;
            this.targetEntity.getItem().shrink(1);
            this.mob.playSound(SoundEvents.GENERIC_EAT, 1, 1);
            cockatrice.heal(8);
            cockatrice.setAnimation(EntityCockatrice.ANIMATION_EAT);
            stop();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

}
