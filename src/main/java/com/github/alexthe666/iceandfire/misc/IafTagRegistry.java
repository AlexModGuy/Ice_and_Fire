package com.github.alexthe666.iceandfire.misc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class IafTagRegistry { // FIXME :: Clean up
    private record EntityTagKeys(ITagManager<EntityType<?>> manager, Map<ResourceLocation, TagKey<EntityType<?>>> keys) {}
    private static volatile EntityTagKeys entityTagKeys;

    public static TagKey<EntityType<?>> entityTag(ITagManager<EntityType<?>> tags, ResourceLocation location) {
        Objects.requireNonNull(tags);
        if (location == null || !CACHED_ENTITY_TAGS.contains(location)) {
            return tags.createTagKey(location);
        }
        EntityTagKeys cache = entityTagKeys;
        if (cache == null || cache.manager != tags) {
            cache = new EntityTagKeys(tags, new ConcurrentHashMap<>());
            entityTagKeys = cache;
        }
        return cache.keys.computeIfAbsent(location, tags::createTagKey);
    }

    public static final ResourceLocation MYRMEX_HARVESTABLES = new ResourceLocation("iceandfire", "myrmex_harvestables");
    public static final ResourceLocation CHICKENS = new ResourceLocation("iceandfire", "chickens");
    public static final ResourceLocation FEAR_DRAGONS = new ResourceLocation("iceandfire", "fear_dragons");
    public static final ResourceLocation SCARES_COCKATRICES = new ResourceLocation("iceandfire", "scares_cockatrices");
    public static final ResourceLocation SHEEP = new ResourceLocation("iceandfire", "sheep");
    public static final ResourceLocation VILLAGERS = new ResourceLocation("iceandfire", "villagers");
    public static final ResourceLocation ICE_DRAGON_TARGETS = new ResourceLocation("iceandfire", "ice_dragon_targets");
    public static final ResourceLocation FIRE_DRAGON_TARGETS = new ResourceLocation("iceandfire", "fire_dragon_targets");
    public static final ResourceLocation LIGHTNING_DRAGON_TARGETS = new ResourceLocation("iceandfire", "lightning_dragon_targets");
    public static final ResourceLocation COCKATRICE_TARGETS = new ResourceLocation("iceandfire", "cockatrice_targets");
    public static final ResourceLocation CYCLOPS_UNLIFTABLES = new ResourceLocation("iceandfire", "cyclops_unliftables");
    public static final ResourceLocation BLINDED = new ResourceLocation("iceandfire", "blinded");

    // The public targeting helper also accepts arbitrary tag names. Keep those on the original path
    // so only this fixed set of Ice and Fire entity tags can occupy the cache.
    private static final Set<ResourceLocation> CACHED_ENTITY_TAGS = Set.of(CHICKENS, FEAR_DRAGONS,
            SCARES_COCKATRICES, SHEEP, VILLAGERS, ICE_DRAGON_TARGETS, FIRE_DRAGON_TARGETS,
            LIGHTNING_DRAGON_TARGETS, COCKATRICE_TARGETS, CYCLOPS_UNLIFTABLES, BLINDED);

}
