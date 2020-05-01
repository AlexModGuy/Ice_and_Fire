package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;

public class IafEntityRegistry {

    public static final EntityType<EntityDragonEgg> DRAGON_EGG = registerEntity(EntityType.Builder.create(EntityDragonEgg::new, EntityClassification.MISC).size(0.45F, 0.55F), "dragon_egg");
    public static final EntityType<EntityDragonArrow> DRAGON_ARROW = registerEntity(EntityType.Builder.create(EntityDragonArrow::new, EntityClassification.MISC).size(0.5F, 0.5F), "dragon_arrow");
    public static final EntityType<EntityDragonSkull> DRAGON_ARROW = registerEntity(EntityType.Builder.create(EntityDragonSkull::new, EntityClassification.MISC).size(0.9F, 0.65F), "dragon_skull");
    public static final EntityType<EntityFireDragon> FIRE_DRAGON = registerEntity(EntityType.Builder.create(EntityFireDragon::new, EntityClassification.CREATURE).size(0.78F, 1.2F), "fire_dragon");
    public static final EntityType<EntityIceDragon> ICE_DRAGON = registerEntity(EntityType.Builder.create(EntityIceDragon::new, EntityClassification.CREATURE).size(0.78F, 1.2F), "ice_dragon");


    private static final EntityType registerEntity(EntityType.Builder builder, String entityName){
        ResourceLocation nameLoc = new ResourceLocation(RatsMod.MODID, entityName);
        return (EntityType) builder.build(entityName).setRegistryName(nameLoc);
    }

    public static void init() {
        EntityPropertiesHandler.INSTANCE.registerProperties(StoneEntityProperties.class);
        EntityPropertiesHandler.INSTANCE.registerProperties(MiscEntityProperties.class);
        EntityPropertiesHandler.INSTANCE.registerProperties(FrozenEntityProperties.class);
        EntityPropertiesHandler.INSTANCE.registerProperties(SirenEntityProperties.class);
        EntityPropertiesHandler.INSTANCE.registerProperties(ChickenEntityProperties.class);
        EntityPropertiesHandler.INSTANCE.registerProperties(ChainEntityProperties.class);
        if (IafConfig.spawnHippogryphs) {
            for (EnumHippogryphTypes type : EnumHippogryphTypes.values()) {
                if (!type.developer) {
                    for (Biome biome : Biome.REGISTRY) {
                        if (biome != null && BiomeDictionary.hasType(biome, BiomeDictionary.Type.HILLS)) {
                            List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.CREATURE);
                            spawnList.add(new Biome.SpawnListEntry(EntityHippogryph.class, IafConfig.hippogryphSpawnRate, 1, 1));
                        }
                    }
                }
            }
        }
        if (IafConfig.spawnDeathWorm) {
            for (Biome biome : Biome.REGISTRY) {
                if (biome != null && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.BEACH) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA)) {
                    List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.CREATURE);
                    spawnList.add(new Biome.SpawnListEntry(EntityDeathWorm.class, IafConfig.deathWormSpawnRate, 1, 3));
                }
            }
        }
        if (IafConfig.spawnTrolls) {
            for (EnumTroll type : EnumTroll.values()) {
                for (Biome biome : Biome.REGISTRY) {
                    if (biome != null && BiomeDictionary.hasType(biome, type.spawnBiome)) {
                        List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.MONSTER);
                        spawnList.add(new Biome.SpawnListEntry(EntityTroll.class, IafConfig.trollSpawnRate, 1, 1));
                    }
                }
            }
        }
        if (IafConfig.spawnLiches) {
            for (Biome biome : Biome.REGISTRY) {
                if (biome != null && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SNOWY)) {
                    List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.MONSTER);
                    spawnList.add(new Biome.SpawnListEntry(EntityDreadLich.class, IafConfig.lichSpawnRate, 1, 1));
                }
            }
        }
        if (IafConfig.spawnCockatrices) {
            for (Biome biome : Biome.REGISTRY) {
                if (biome != null && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SPARSE)) {
                    List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.CREATURE);
                    spawnList.add(new Biome.SpawnListEntry(EntityCockatrice.class, IafConfig.cockatriceSpawnRate, 1, 2));
                }
            }
        }
        if (IafConfig.spawnAmphitheres) {
            for (Biome biome : Biome.REGISTRY) {
                if (biome != null && BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
                    List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.CREATURE);
                    spawnList.add(new Biome.SpawnListEntry(EntityAmphithere.class, IafConfig.amphithereSpawnRate, 1, 3));
                }
            }
        }
    }
}
