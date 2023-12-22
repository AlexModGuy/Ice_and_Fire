package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = IceAndFire.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IafEntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
        IceAndFire.MODID);

    public static final RegistryObject<EntityType<EntityDragonPart>> DRAGON_MULTIPART = registerEntity(EntityType.Builder.<EntityDragonPart>of(EntityDragonPart::new, MobCategory.MISC).sized(0.5F, 0.5F).fireImmune().setCustomClientFactory(EntityDragonPart::new), "dragon_multipart");
    public static final RegistryObject<EntityType<EntitySlowPart>> SLOW_MULTIPART = registerEntity(EntityType.Builder.<EntitySlowPart>of(EntitySlowPart::new, MobCategory.MISC).sized(0.5F, 0.5F).fireImmune().setCustomClientFactory(EntitySlowPart::new), "multipart");
    public static final RegistryObject<EntityType<EntityHydraHead>> HYDRA_MULTIPART = registerEntity(EntityType.Builder.<EntityHydraHead>of(EntityHydraHead::new, MobCategory.MISC).sized(0.5F, 0.5F).fireImmune().setCustomClientFactory(EntityHydraHead::new), "hydra_multipart");
    public static final RegistryObject<EntityType<EntityCyclopsEye>> CYCLOPS_MULTIPART = registerEntity(EntityType.Builder.<EntityCyclopsEye>of(EntityCyclopsEye::new, MobCategory.MISC).sized(0.5F, 0.5F).fireImmune().setCustomClientFactory(EntityCyclopsEye::new), "cylcops_multipart");
    public static final RegistryObject<EntityType<EntityDragonEgg>> DRAGON_EGG = registerEntity(EntityType.Builder.of(EntityDragonEgg::new, MobCategory.MISC).sized(0.45F, 0.55F).fireImmune(), "dragon_egg");
    public static final RegistryObject<EntityType<EntityDragonArrow>> DRAGON_ARROW = registerEntity(EntityType.Builder.<EntityDragonArrow>of(EntityDragonArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(EntityDragonArrow::new), "dragon_arrow");
    public static final RegistryObject<EntityType<EntityDragonSkull>> DRAGON_SKULL = registerEntity(EntityType.Builder.of(EntityDragonSkull::new, MobCategory.MISC).sized(0.9F, 0.65F), "dragon_skull");
    public static final RegistryObject<EntityType<EntityFireDragon>> FIRE_DRAGON = registerEntity(EntityType.Builder.<EntityFireDragon>of(EntityFireDragon::new, MobCategory.CREATURE).sized(0.78F, 1.2F).fireImmune().setTrackingRange(256).clientTrackingRange(10), "fire_dragon");
    public static final RegistryObject<EntityType<EntityIceDragon>> ICE_DRAGON = registerEntity(EntityType.Builder.<EntityIceDragon>of(EntityIceDragon::new, MobCategory.CREATURE).sized(0.78F, 1.2F).setTrackingRange(256).clientTrackingRange(10), "ice_dragon");
    public static final RegistryObject<EntityType<EntityLightningDragon>> LIGHTNING_DRAGON = registerEntity(EntityType.Builder.<EntityLightningDragon>of(EntityLightningDragon::new, MobCategory.CREATURE).sized(0.78F, 1.2F).setTrackingRange(256).clientTrackingRange(10), "lightning_dragon");
    public static final RegistryObject<EntityType<EntityDragonFireCharge>> FIRE_DRAGON_CHARGE = registerEntity(EntityType.Builder.<EntityDragonFireCharge>of(EntityDragonFireCharge::new, MobCategory.MISC).sized(0.9F, 0.9F).setCustomClientFactory(EntityDragonFireCharge::new), "fire_dragon_charge");
    public static final RegistryObject<EntityType<EntityDragonIceCharge>> ICE_DRAGON_CHARGE = registerEntity(EntityType.Builder.<EntityDragonIceCharge>of(EntityDragonIceCharge::new, MobCategory.MISC).sized(0.9F, 0.9F).setCustomClientFactory(EntityDragonIceCharge::new), "ice_dragon_charge");
    public static final RegistryObject<EntityType<EntityDragonLightningCharge>> LIGHTNING_DRAGON_CHARGE = registerEntity(EntityType.Builder.<EntityDragonLightningCharge>of(EntityDragonLightningCharge::new, MobCategory.MISC).sized(0.9F, 0.9F).setCustomClientFactory(EntityDragonLightningCharge::new), "lightning_dragon_charge");
    public static final RegistryObject<EntityType<EntityHippogryphEgg>> HIPPOGRYPH_EGG = registerEntity(EntityType.Builder.<EntityHippogryphEgg>of(EntityHippogryphEgg::new, MobCategory.MISC).sized(0.5F, 0.5F), "hippogryph_egg");
    public static final RegistryObject<EntityType<EntityHippogryph>> HIPPOGRYPH = registerEntity(EntityType.Builder.of(EntityHippogryph::new, MobCategory.CREATURE).sized(1.7F, 1.6F).setTrackingRange(128), "hippogryph");
    public static final RegistryObject<EntityType<EntityStoneStatue>> STONE_STATUE = registerEntity(EntityType.Builder.of(EntityStoneStatue::new, MobCategory.CREATURE).sized(0.5F, 0.5F), "stone_statue");
    public static final RegistryObject<EntityType<EntityGorgon>> GORGON = registerEntity(EntityType.Builder.of(EntityGorgon::new, MobCategory.CREATURE).sized(0.8F, 1.99F), "gorgon");
    public static final RegistryObject<EntityType<EntityPixie>> PIXIE = registerEntity(EntityType.Builder.of(EntityPixie::new, MobCategory.CREATURE).sized(0.4F, 0.8F), "pixie");
    public static final RegistryObject<EntityType<EntityCyclops>> CYCLOPS = registerEntity(EntityType.Builder.of(EntityCyclops::new, MobCategory.CREATURE).sized(1.95F, 7.4F).clientTrackingRange(8), "cyclops");
    public static final RegistryObject<EntityType<EntitySiren>> SIREN = registerEntity(EntityType.Builder.of(EntitySiren::new, MobCategory.CREATURE).sized(1.6F, 0.9F), "siren");
    public static final RegistryObject<EntityType<EntityHippocampus>> HIPPOCAMPUS = registerEntity(EntityType.Builder.of(EntityHippocampus::new, MobCategory.CREATURE).sized(1.95F, 0.95F), "hippocampus");
    public static final RegistryObject<EntityType<EntityDeathWorm>> DEATH_WORM = registerEntity(EntityType.Builder.of(EntityDeathWorm::new, MobCategory.CREATURE).sized(0.8F, 0.8F).setTrackingRange(128), "deathworm");
    public static final RegistryObject<EntityType<EntityDeathWormEgg>> DEATH_WORM_EGG = registerEntity(EntityType.Builder.<EntityDeathWormEgg>of(EntityDeathWormEgg::new, MobCategory.MISC).sized(0.5F, 0.5F), "deathworm_egg");
    public static final RegistryObject<EntityType<EntityCockatrice>> COCKATRICE = registerEntity(EntityType.Builder.of(EntityCockatrice::new, MobCategory.CREATURE).sized(1.1F, 1F), "cockatrice");
    public static final RegistryObject<EntityType<EntityCockatriceEgg>> COCKATRICE_EGG = registerEntity(EntityType.Builder.<EntityCockatriceEgg>of(EntityCockatriceEgg::new, MobCategory.MISC).sized(0.5F, 0.5F), "cockatrice_egg");
    public static final RegistryObject<EntityType<EntityStymphalianBird>> STYMPHALIAN_BIRD = registerEntity(EntityType.Builder.of(EntityStymphalianBird::new, MobCategory.CREATURE).sized(1.3F, 1.2F).setTrackingRange(128), "stymphalian_bird");
    public static final RegistryObject<EntityType<EntityStymphalianFeather>> STYMPHALIAN_FEATHER = registerEntity(EntityType.Builder.<EntityStymphalianFeather>of(EntityStymphalianFeather::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(EntityStymphalianFeather::new), "stymphalian_feather");
    public static final RegistryObject<EntityType<EntityStymphalianArrow>> STYMPHALIAN_ARROW = registerEntity(EntityType.Builder.<EntityStymphalianArrow>of(EntityStymphalianArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(EntityStymphalianArrow::new), "stymphalian_arrow");
    public static final RegistryObject<EntityType<EntityTroll>> TROLL = registerEntity(EntityType.Builder.of(EntityTroll::new, MobCategory.MONSTER).sized(1.2F, 3.5F), "troll");
    public static final RegistryObject<EntityType<EntityMyrmexWorker>> MYRMEX_WORKER = registerEntity(EntityType.Builder.of(EntityMyrmexWorker::new, MobCategory.CREATURE).sized(0.9F, 0.9F), "myrmex_worker");
    public static final RegistryObject<EntityType<EntityMyrmexSoldier>> MYRMEX_SOLDIER = registerEntity(EntityType.Builder.of(EntityMyrmexSoldier::new, MobCategory.CREATURE).sized(1.2F, 0.95F), "myrmex_soldier");
    public static final RegistryObject<EntityType<EntityMyrmexSentinel>> MYRMEX_SENTINEL = registerEntity(EntityType.Builder.of(EntityMyrmexSentinel::new, MobCategory.CREATURE).sized(1.3F, 1.95F), "myrmex_sentinel");
    public static final RegistryObject<EntityType<EntityMyrmexRoyal>> MYRMEX_ROYAL = registerEntity(EntityType.Builder.of(EntityMyrmexRoyal::new, MobCategory.CREATURE).sized(1.9F, 1.86F), "myrmex_royal");
    public static final RegistryObject<EntityType<EntityMyrmexQueen>> MYRMEX_QUEEN = registerEntity(EntityType.Builder.of(EntityMyrmexQueen::new, MobCategory.CREATURE).sized(2.9F, 1.86F), "myrmex_queen");
    public static final RegistryObject<EntityType<EntityMyrmexEgg>> MYRMEX_EGG = registerEntity(EntityType.Builder.of(EntityMyrmexEgg::new, MobCategory.MISC).sized(0.45F, 0.55F), "myrmex_egg");
    public static final RegistryObject<EntityType<EntityAmphithere>> AMPHITHERE = registerEntity(EntityType.Builder.of(EntityAmphithere::new, MobCategory.CREATURE).sized(2.5F, 1.25F).setTrackingRange(128).clientTrackingRange(8), "amphithere");
    public static final RegistryObject<EntityType<EntityAmphithereArrow>> AMPHITHERE_ARROW = registerEntity(EntityType.Builder.<EntityAmphithereArrow>of(EntityAmphithereArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(EntityAmphithereArrow::new), "amphithere_arrow");
    public static final RegistryObject<EntityType<EntitySeaSerpent>> SEA_SERPENT = registerEntity(EntityType.Builder.of(EntitySeaSerpent::new, MobCategory.CREATURE).sized(0.5F, 0.5F).setTrackingRange(256).clientTrackingRange(8), "sea_serpent");
    public static final RegistryObject<EntityType<EntitySeaSerpentBubbles>> SEA_SERPENT_BUBBLES = registerEntity(EntityType.Builder.<EntitySeaSerpentBubbles>of(EntitySeaSerpentBubbles::new, MobCategory.MISC).sized(0.9F, 0.9F).setCustomClientFactory(EntitySeaSerpentBubbles::new), "sea_serpent_bubbles");
    public static final RegistryObject<EntityType<EntitySeaSerpentArrow>> SEA_SERPENT_ARROW = registerEntity(EntityType.Builder.<EntitySeaSerpentArrow>of(EntitySeaSerpentArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(EntitySeaSerpentArrow::new), "sea_serpent_arrow");
    public static final RegistryObject<EntityType<EntityChainTie>> CHAIN_TIE = registerEntity(EntityType.Builder.<EntityChainTie>of(EntityChainTie::new, MobCategory.MISC).sized(0.8F, 0.9F), "chain_tie");
    public static final RegistryObject<EntityType<EntityPixieCharge>> PIXIE_CHARGE = registerEntity(EntityType.Builder.<EntityPixieCharge>of(EntityPixieCharge::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(EntityPixieCharge::new), "pixie_charge");
    public static final RegistryObject<EntityType<EntityMyrmexSwarmer>> MYRMEX_SWARMER = registerEntity(EntityType.Builder.of(EntityMyrmexSwarmer::new, MobCategory.CREATURE).sized(0.5F, 0.5F), "myrmex_swarmer");
    public static final RegistryObject<EntityType<EntityTideTrident>> TIDE_TRIDENT = registerEntity(EntityType.Builder.<EntityTideTrident>of(EntityTideTrident::new, MobCategory.MISC).sized(0.85F, 0.5F), "tide_trident");
    public static final RegistryObject<EntityType<EntityMobSkull>> MOB_SKULL = registerEntity(EntityType.Builder.of(EntityMobSkull::new, MobCategory.MISC).sized(0.85F, 0.85F), "mob_skull");
    public static final RegistryObject<EntityType<EntityDreadThrall>> DREAD_THRALL = registerEntity(EntityType.Builder.of(EntityDreadThrall::new, MobCategory.MONSTER).sized(0.6F, 1.8F), "dread_thrall");
    public static final RegistryObject<EntityType<EntityDreadGhoul>> DREAD_GHOUL = registerEntity(EntityType.Builder.of(EntityDreadGhoul::new, MobCategory.MONSTER).sized(0.6F, 1.8F), "dread_ghoul");
    public static final RegistryObject<EntityType<EntityDreadBeast>> DREAD_BEAST = registerEntity(EntityType.Builder.of(EntityDreadBeast::new, MobCategory.MONSTER).sized(1.2F, 0.9F), "dread_beast");
    public static final RegistryObject<EntityType<EntityDreadScuttler>> DREAD_SCUTTLER = registerEntity(EntityType.Builder.of(EntityDreadScuttler::new, MobCategory.MONSTER).sized(1.5F, 1.3F), "dread_scuttler");
    public static final RegistryObject<EntityType<EntityDreadLich>> DREAD_LICH = registerEntity(EntityType.Builder.of(EntityDreadLich::new, MobCategory.MONSTER).sized(0.6F, 1.8F), "dread_lich");
    public static final RegistryObject<EntityType<EntityDreadLichSkull>> DREAD_LICH_SKULL = registerEntity(EntityType.Builder.<EntityDreadLichSkull>of(EntityDreadLichSkull::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(EntityDreadLichSkull::new), "dread_lich_skull");
    public static final RegistryObject<EntityType<EntityDreadKnight>> DREAD_KNIGHT = registerEntity(EntityType.Builder.of(EntityDreadKnight::new, MobCategory.MONSTER).sized(0.6F, 1.8F), "dread_knight");
    public static final RegistryObject<EntityType<EntityDreadHorse>> DREAD_HORSE = registerEntity(EntityType.Builder.of(EntityDreadHorse::new, MobCategory.MONSTER).sized(1.3964844F, 1.6F), "dread_horse");
    public static final RegistryObject<EntityType<EntityHydra>> HYDRA = registerEntity(EntityType.Builder.of(EntityHydra::new, MobCategory.CREATURE).sized(2.8F, 1.39F), "hydra");
    public static final RegistryObject<EntityType<EntityHydraBreath>> HYDRA_BREATH = registerEntity(EntityType.Builder.<EntityHydraBreath>of(EntityHydraBreath::new, MobCategory.MISC).sized(0.9F, 0.9F).setCustomClientFactory(EntityHydraBreath::new), "hydra_breath");
    public static final RegistryObject<EntityType<EntityHydraArrow>> HYDRA_ARROW = registerEntity(EntityType.Builder.<EntityHydraArrow>of(EntityHydraArrow::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(EntityHydraArrow::new), "hydra_arrow");
    public static final RegistryObject<EntityType<EntityGhost>> GHOST = registerEntity(EntityType.Builder.of(EntityGhost::new, MobCategory.MONSTER).sized(0.8F, 1.9F).fireImmune(), "ghost");
    public static final RegistryObject<EntityType<EntityGhostSword>> GHOST_SWORD = registerEntity(EntityType.Builder.<EntityGhostSword>of(EntityGhostSword::new, MobCategory.MISC).sized(0.5F, 0.5F).setCustomClientFactory(EntityGhostSword::new), "ghost_sword");

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(EntityType.Builder<T> builder, String entityName) {
        return ENTITIES.register(entityName, () -> builder.build(entityName));
    }

    @SubscribeEvent
    public static void bakeAttributes(EntityAttributeCreationEvent creationEvent) {
        creationEvent.put(DRAGON_EGG.get(), EntityDragonEgg.bakeAttributes().build());
        creationEvent.put(DRAGON_SKULL.get(), EntityDragonSkull.bakeAttributes().build());
        creationEvent.put(FIRE_DRAGON.get(), EntityFireDragon.bakeAttributes().build());
        creationEvent.put(ICE_DRAGON.get(), EntityIceDragon.bakeAttributes().build());
        creationEvent.put(LIGHTNING_DRAGON.get(), EntityLightningDragon.bakeAttributes().build());
        creationEvent.put(HIPPOGRYPH.get(), EntityHippogryph.bakeAttributes().build());
        creationEvent.put(GORGON.get(), EntityGorgon.bakeAttributes().build());
        creationEvent.put(STONE_STATUE.get(), EntityStoneStatue.bakeAttributes().build());
        creationEvent.put(PIXIE.get(), EntityPixie.bakeAttributes().build());
        creationEvent.put(CYCLOPS.get(), EntityCyclops.bakeAttributes().build());
        creationEvent.put(SIREN.get(), EntitySiren.bakeAttributes().build());
        creationEvent.put(HIPPOCAMPUS.get(), EntityHippocampus.bakeAttributes().build());
        creationEvent.put(DEATH_WORM.get(), EntityDeathWorm.bakeAttributes().build());
        creationEvent.put(COCKATRICE.get(), EntityCockatrice.bakeAttributes().build());
        creationEvent.put(STYMPHALIAN_BIRD.get(), EntityStymphalianBird.bakeAttributes().build());
        creationEvent.put(TROLL.get(), EntityTroll.bakeAttributes().build());
        creationEvent.put(MYRMEX_WORKER.get(), EntityMyrmexWorker.bakeAttributes().build());
        creationEvent.put(MYRMEX_SOLDIER.get(), EntityMyrmexSoldier.bakeAttributes().build());
        creationEvent.put(MYRMEX_SENTINEL.get(), EntityMyrmexSentinel.bakeAttributes().build());
        creationEvent.put(MYRMEX_ROYAL.get(), EntityMyrmexRoyal.bakeAttributes().build());
        creationEvent.put(MYRMEX_QUEEN.get(), EntityMyrmexQueen.bakeAttributes().build());
        creationEvent.put(MYRMEX_EGG.get(), EntityMyrmexEgg.bakeAttributes().build());
        creationEvent.put(MYRMEX_SWARMER.get(), EntityMyrmexSwarmer.bakeAttributes().build());
        creationEvent.put(AMPHITHERE.get(), EntityAmphithere.bakeAttributes().build());
        creationEvent.put(SEA_SERPENT.get(), EntitySeaSerpent.bakeAttributes().build());
        creationEvent.put(MOB_SKULL.get(), EntityMobSkull.bakeAttributes().build());
        creationEvent.put(DREAD_THRALL.get(), EntityDreadThrall.bakeAttributes().build());
        creationEvent.put(DREAD_LICH.get(), EntityDreadLich.bakeAttributes().build());
        creationEvent.put(DREAD_BEAST.get(), EntityDreadBeast.bakeAttributes().build());
        creationEvent.put(DREAD_HORSE.get(), EntityDreadHorse.bakeAttributes().build());
        creationEvent.put(DREAD_GHOUL.get(), EntityDreadGhoul.bakeAttributes().build());
        creationEvent.put(DREAD_KNIGHT.get(), EntityDreadKnight.bakeAttributes().build());
        creationEvent.put(DREAD_SCUTTLER.get(), EntityDreadScuttler.bakeAttributes().build());
        creationEvent.put(HYDRA.get(), EntityHydra.bakeAttributes().build());
        creationEvent.put(GHOST.get(), EntityGhost.bakeAttributes().build());
    }

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        SpawnPlacements.register(HIPPOGRYPH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EntityHippogryph::checkMobSpawnRules);
        SpawnPlacements.register(TROLL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EntityTroll::canTrollSpawnOn);
        SpawnPlacements.register(DREAD_LICH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EntityDreadLich::canLichSpawnOn);
        SpawnPlacements.register(COCKATRICE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EntityCockatrice::checkMobSpawnRules);
        SpawnPlacements.register(AMPHITHERE.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, EntityAmphithere::canAmphithereSpawnOn);
    }

    public static HashMap<String, Boolean> LOADED_ENTITIES;
    static {
    	LOADED_ENTITIES = new HashMap<>();
    	LOADED_ENTITIES.put("HIPPOGRYPH", false);
    	LOADED_ENTITIES.put("DREAD_LICH", false);
    	LOADED_ENTITIES.put("COCKATRICE", false);
    	LOADED_ENTITIES.put("AMPHITHERE", false);
    	LOADED_ENTITIES.put("TROLL_F", false);
    	LOADED_ENTITIES.put("TROLL_S", false);
    	LOADED_ENTITIES.put("TROLL_M", false);
    }
    public static void addSpawners(Holder<Biome> biome, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (IafConfig.spawnHippogryphs && BiomeConfig.test(BiomeConfig.hippogryphBiomes, biome)) {
            builder.getMobSpawnSettings().getSpawner(MobCategory.CREATURE).add(new MobSpawnSettings.SpawnerData(IafEntityRegistry.HIPPOGRYPH.get(), IafConfig.hippogryphSpawnRate, 1, 1));
            LOADED_ENTITIES.put("HIPPOGRYPH", true);
        }
        if (IafConfig.spawnLiches && BiomeConfig.test(BiomeConfig.mausoleumBiomes, biome)) {
            builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(IafEntityRegistry.DREAD_LICH.get(), IafConfig.lichSpawnRate, 1, 1));
            LOADED_ENTITIES.put("DREAD_LICH", true);
        }
        if (IafConfig.spawnCockatrices && BiomeConfig.test(BiomeConfig.cockatriceBiomes, biome)) {
            builder.getMobSpawnSettings().getSpawner(MobCategory.CREATURE).add(new MobSpawnSettings.SpawnerData(IafEntityRegistry.COCKATRICE.get(), IafConfig.cockatriceSpawnRate, 1, 2));
            LOADED_ENTITIES.put("COCKATRICE", true);
        }
        if (IafConfig.spawnAmphitheres && BiomeConfig.test(BiomeConfig.amphithereBiomes, biome)) {
            builder.getMobSpawnSettings().getSpawner(MobCategory.CREATURE).add(new MobSpawnSettings.SpawnerData(IafEntityRegistry.AMPHITHERE.get(), IafConfig.amphithereSpawnRate, 1, 3));
            LOADED_ENTITIES.put("AMPHITHERE", true);
        }
        if (IafConfig.spawnTrolls && (
    		BiomeConfig.test(BiomeConfig.forestTrollBiomes, biome) ||
    		BiomeConfig.test(BiomeConfig.snowyTrollBiomes, biome) ||
    		BiomeConfig.test(BiomeConfig.mountainTrollBiomes, biome)
		)) {
            builder.getMobSpawnSettings().getSpawner(MobCategory.MONSTER).add(new MobSpawnSettings.SpawnerData(IafEntityRegistry.TROLL.get(), IafConfig.trollSpawnRate, 1, 3));
    		if (BiomeConfig.test(BiomeConfig.forestTrollBiomes, biome)) LOADED_ENTITIES.put("TROLL_F", true);
    		if (BiomeConfig.test(BiomeConfig.snowyTrollBiomes, biome)) LOADED_ENTITIES.put("TROLL_S", true);
    		if (BiomeConfig.test(BiomeConfig.mountainTrollBiomes, biome)) LOADED_ENTITIES.put("TROLL_M", true);
        }

    }
}
