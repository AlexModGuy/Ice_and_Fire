package com.github.alexthe666.iceandfire.entity.ai.base;

import com.github.alexthe666.iceandfire.entity.ai.DragonAITargetItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class PickUpTargetGoal<M extends Mob, T extends Entity> extends TargetGoal {
    protected final Predicate<T> targetSelector;

    protected @NotNull List<T> targets;
    protected @Nullable T currentTarget;
    protected int tickCount;
    private final Class<T> rawTargetClass;
    private final Supplier<List<T>> targetsSupplier;

    @SuppressWarnings("unchecked")
    public PickUpTargetGoal(final @NotNull M mob, boolean mustSee, boolean mustReach, final Predicate<T> targetSelector) {
        super(mob, mustSee, mustReach);
        this.targetSelector = targetSelector;
        this.targets = Collections.emptyList();
        this.rawTargetClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.targetsSupplier = () -> mob.getLevel().getEntitiesOfClass(rawTargetClass, getSearchArea(), targetSelector);
        setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        return updateList();
    }

    @Override
    public void start() {
        super.start();
        pickTarget();
    }

    @Override
    public void tick() {
        super.tick();
        tickCount++;
        updateList();
        pickTarget();
    }

    @Override
    public void stop() {
        super.stop();
        currentTarget = null;
        targets = Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    protected M getMob() {
        return (M) mob;
    }

    protected AABB getSearchArea() {
        double searchRange = getSearchRange();
        return mob.getBoundingBox().inflate(searchRange, 4, searchRange);
    }

    protected boolean updateList() {
        targets = updateList(getMob(), targets, targetsSupplier);
        return !targets.isEmpty();
    }

    protected void pickTarget() {
        if (currentTarget != null && !currentTarget.isAlive()) {
            currentTarget = null;
        }

        if (currentTarget == null && !targets.isEmpty()) {
            currentTarget = targets.get(0);
        }

        if (currentTarget != null) {
            setMovement();
        } else {
            stop();
        }
    }

    private List<T> updateList(final M mob, final List<T> targets, final Supplier<List<T>> targetsSupplier) {
        List<T> result = targets;

        if (tickCount % 20 == 0) {
            result = targetsSupplier.get();
            result.removeIf(Entity::isRemoved);
        }

        if (!result.isEmpty()) {
            result.sort(new DragonAITargetItems.Sorter(mob));
        }

        return result;
    }

    abstract protected void setMovement();
    abstract protected double getSearchRange();
}
