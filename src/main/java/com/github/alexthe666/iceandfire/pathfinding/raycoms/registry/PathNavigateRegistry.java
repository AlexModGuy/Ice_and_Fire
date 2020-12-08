package com.github.alexthe666.iceandfire.pathfinding.raycoms.registry;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */
import com.github.alexthe666.iceandfire.pathfinding.raycoms.AbstractAdvancedPathNavigate;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.DragonAdvancedPathNavigate;
import com.google.common.collect.Maps;
import net.minecraft.entity.MobEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class PathNavigateRegistry {
    private static final Function<MobEntity, AbstractAdvancedPathNavigate> DEFAULT = (entityLiving -> new DragonAdvancedPathNavigate(entityLiving, entityLiving.world));

    private final Map<Predicate<MobEntity>, Function<MobEntity, AbstractAdvancedPathNavigate>> registry = Maps.newLinkedHashMap();

    public PathNavigateRegistry registerNewPathNavigate(
            final Predicate<MobEntity> selectionPredicate, final Function<MobEntity, AbstractAdvancedPathNavigate> navigateProducer) {
        registry.put(selectionPredicate, navigateProducer);
        return this;
    }

    public AbstractAdvancedPathNavigate getNavigateFor(final MobEntity entityLiving) {
        final List<Predicate<MobEntity>> predicates = new ArrayList<>(registry.keySet());
        Collections.reverse(predicates);

        return predicates.stream().filter(predicate -> predicate.test(entityLiving)).findFirst().map(predicate -> registry.get(predicate)).orElse(DEFAULT).apply(entityLiving);
    }
}
