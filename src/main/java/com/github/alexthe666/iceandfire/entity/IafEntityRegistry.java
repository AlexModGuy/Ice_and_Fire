package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.config.BiomeConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

@Mod.EventBusSubscriber(modid = IceAndFire.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IafEntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES,
            IceAndFire.MODID);

    //@formatter:off
    public static final RegistryObject<EntityType<EntityDragonPart>> DRAGON_MULTIPART = registerEntity(EntityType.Builder.<EntityDragonPart>create(EntityDragonPart::new, EntityClassification.MISC).size(0.5F, 0.5F).immuneToFire().setCustomClientFactory(EntityDragonPart::new), "dragon_multipart");
    public static final RegistryObject<EntityType<EntitySlowPart>> SLOW_MULTIPART = registerEntity(EntityType.Builder.<EntitySlowPart>create(EntitySlowPart::new, EntityClassification.MISC).size(0.5F, 0.5F).immuneToFire().setCustomClientFactory(EntitySlowPart::new), "multipart");
    public static final RegistryObject<EntityType<EntityHydraHead>> HYDRA_MULTIPART = registerEntity(EntityType.Builder.<EntityHydraHead>create(EntityHydraHead::new, EntityClassification.MISC).size(0.5F, 0.5F).immuneToFire().setCustomClientFactory(EntityHydraHead::new), "hydra_multipart");
    public static final RegistryObject<EntityType<EntityCyclopsEye>> CYCLOPS_MULTIPART = registerEntity(EntityType.Builder.<EntityCyclopsEye>create(EntityCyclopsEye::new, EntityClassification.MISC).size(0.5F, 0.5F).immuneToFire().setCustomClientFactory(EntityCyclopsEye::new), "cylcops_multipart");
    public static final RegistryObject<EntityType<EntityDragonEgg>> DRAGON_EGG = registerEntity(EntityType.Builder.create(EntityDragonEgg::new, EntityClassification.MISC).size(0.45F, 0.55F).immuneToFire(), "dragon_egg");
    public static final RegistryObject<EntityType<EntityDragonArrow>> DRAGON_ARROW = registerEntity(EntityType.Builder.<EntityDragonArrow>create(EntityDragonArrow::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityDragonArrow::new), "dragon_arrow");
    public static final RegistryObject<EntityType<EntityDragonSkull>> DRAGON_SKULL = registerEntity(EntityType.Builder.create(EntityDragonSkull::new, EntityClassification.MISC).size(0.9F, 0.65F), "dragon_skull");
    public static final RegistryObject<EntityType<EntityFireDragon>> FIRE_DRAGON = registerEntity(EntityType.Builder.<EntityFireDragon>create(EntityFireDragon::new, EntityClassification.CREATURE).size(0.78F, 1.2F).immuneToFire().setTrackingRange(256), "fire_dragon");
    public static final RegistryObject<EntityType<EntityIceDragon>> ICE_DRAGON = registerEntity(EntityType.Builder.<EntityIceDragon>create(EntityIceDragon::new, EntityClassification.CREATURE).size(0.78F, 1.2F).setTrackingRange(256), "ice_dragon");
    public static final RegistryObject<EntityType<EntityLightningDragon>> LIGHTNING_DRAGON = registerEntity(EntityType.Builder.<EntityLightningDragon>create(EntityLightningDragon::new, EntityClassification.CREATURE).size(0.78F, 1.2F).setTrackingRange(256), "lightning_dragon");
    public static final RegistryObject<EntityType<EntityDragonFireCharge>> FIRE_DRAGON_CHARGE = registerEntity(EntityType.Builder.<EntityDragonFireCharge>create(EntityDragonFireCharge::new, EntityClassification.MISC).size(0.9F, 0.9F).setCustomClientFactory(EntityDragonFireCharge::new), "fire_dragon_charge");
    public static final RegistryObject<EntityType<EntityDragonIceCharge>> ICE_DRAGON_CHARGE = registerEntity(EntityType.Builder.<EntityDragonIceCharge>create(EntityDragonIceCharge::new, EntityClassification.MISC).size(0.9F, 0.9F).setCustomClientFactory(EntityDragonIceCharge::new), "ice_dragon_charge");
    public static final RegistryObject<EntityType<EntityDragonLightningCharge>> LIGHTNING_DRAGON_CHARGE = registerEntity(EntityType.Builder.<EntityDragonLightningCharge>create(EntityDragonLightningCharge::new, EntityClassification.MISC).size(0.9F, 0.9F).setCustomClientFactory(EntityDragonLightningCharge::new), "lightning_dragon_charge");
    public static final RegistryObject<EntityType<EntityHippogryphEgg>> HIPPOGRYPH_EGG = registerEntity(EntityType.Builder.<EntityHippogryphEgg>create(EntityHippogryphEgg::new, EntityClassification.MISC).size(0.5F, 0.5F), "hippogryph_egg");
    public static final RegistryObject<EntityType<EntityStoneStatue>> STONE_STATUE = registerEntity(EntityType.Builder.create(EntityStoneStatue::new, EntityClassification.CREATURE).size(0.5F, 0.5F), "stone_statue");
    public static final RegistryObject<EntityType<EntityGorgon>> GORGON = registerEntity(EntityType.Builder.create(EntityGorgon::new, EntityClassification.CREATURE).size(0.8F, 1.99F), "gorgon");
    public static final RegistryObject<EntityType<EntityPixie>> PIXIE = registerEntity(EntityType.Builder.create(EntityPixie::new, EntityClassification.CREATURE).size(0.4F, 0.8F), "pixie");
    public static final RegistryObject<EntityType<EntityCyclops>> CYCLOPS = registerEntity(EntityType.Builder.create(EntityCyclops::new, EntityClassification.CREATURE).size(1.95F, 7.4F), "cyclops");
    public static final RegistryObject<EntityType<EntityDeathWorm>> DEATH_WORM = registerEntity(EntityType.Builder.create(EntityDeathWorm::new, EntityClassification.CREATURE).size(0.8F, 0.8F).setTrackingRange(128), "deathworm");
    public static final RegistryObject<EntityType<EntityDeathWormEgg>> DEATH_WORM_EGG = registerEntity(EntityType.Builder.<EntityDeathWormEgg>create(EntityDeathWormEgg::new, EntityClassification.MISC).size(0.5F, 0.5F), "deathworm_egg");
    public static final RegistryObject<EntityType<EntityCockatrice>> COCKATRICE = registerEntity(EntityType.Builder.create(EntityCockatrice::new, EntityClassification.CREATURE).size(0.95F, 0.95F), "cockatrice");
    public static final RegistryObject<EntityType<EntityCockatriceEgg>> COCKATRICE_EGG = registerEntity(EntityType.Builder.<EntityCockatriceEgg>create(EntityCockatriceEgg::new, EntityClassification.MISC).size(0.5F, 0.5F), "cockatrice_egg");
    public static final RegistryObject<EntityType<EntityStymphalianBird>> STYMPHALIAN_BIRD = registerEntity(EntityType.Builder.create(EntityStymphalianBird::new, EntityClassification.CREATURE).size(1.3F, 1.2F).setTrackingRange(128), "stymphalian_bird");
    public static final RegistryObject<EntityType<EntityStymphalianFeather>> STYMPHALIAN_FEATHER = registerEntity(EntityType.Builder.<EntityStymphalianFeather>create(EntityStymphalianFeather::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityStymphalianFeather::new), "stymphalian_feather");
    public static final RegistryObject<EntityType<EntityStymphalianArrow>> STYMPHALIAN_ARROW = registerEntity(EntityType.Builder.<EntityStymphalianArrow>create(EntityStymphalianArrow::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityStymphalianArrow::new), "stymphalian_arrow");
    public static final RegistryObject<EntityType<EntityMyrmexWorker>> MYRMEX_WORKER = registerEntity(EntityType.Builder.create(EntityMyrmexWorker::new, EntityClassification.CREATURE).size(0.9F, 0.9F), "myrmex_worker");
    public static final RegistryObject<EntityType<EntityMyrmexSoldier>> MYRMEX_SOLDIER = registerEntity(EntityType.Builder.create(EntityMyrmexSoldier::new, EntityClassification.CREATURE).size(1.2F, 0.95F), "myrmex_soldier");
    public static final RegistryObject<EntityType<EntityMyrmexSentinel>> MYRMEX_SENTINEL = registerEntity(EntityType.Builder.create(EntityMyrmexSentinel::new, EntityClassification.CREATURE).size(1.3F, 1.95F), "myrmex_sentinel");
    public static final RegistryObject<EntityType<EntityMyrmexRoyal>> MYRMEX_ROYAL = registerEntity(EntityType.Builder.create(EntityMyrmexRoyal::new, EntityClassification.CREATURE).size(1.9F, 1.86F), "myrmex_royal");
    public static final RegistryObject<EntityType<EntityMyrmexQueen>> MYRMEX_QUEEN = registerEntity(EntityType.Builder.create(EntityMyrmexQueen::new, EntityClassification.CREATURE).size(2.9F, 1.86F), "myrmex_queen");
    public static final RegistryObject<EntityType<EntityMyrmexEgg>> MYRMEX_EGG = registerEntity(EntityType.Builder.create(EntityMyrmexEgg::new, EntityClassification.MISC).size(0.45F, 0.55F), "myrmex_egg");
    public static final RegistryObject<EntityType<EntityAmphithereArrow>> AMPHITHERE_ARROW = registerEntity(EntityType.Builder.<EntityAmphithereArrow>create(EntityAmphithereArrow::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityAmphithereArrow::new), "amphithere_arrow");
    public static final RegistryObject<EntityType<EntitySeaSerpentBubbles>> SEA_SERPENT_BUBBLES = registerEntity(EntityType.Builder.<EntitySeaSerpentBubbles>create(EntitySeaSerpentBubbles::new, EntityClassification.MISC).size(0.9F, 0.9F).setCustomClientFactory(EntitySeaSerpentBubbles::new), "sea_serpent_bubbles");
    public static final RegistryObject<EntityType<EntitySeaSerpentArrow>> SEA_SERPENT_ARROW = registerEntity(EntityType.Builder.<EntitySeaSerpentArrow>create(EntitySeaSerpentArrow::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntitySeaSerpentArrow::new), "sea_serpent_arrow");
    public static final RegistryObject<EntityType<EntityChainTie>> CHAIN_TIE = registerEntity(EntityType.Builder.<EntityChainTie>create(EntityChainTie::new, EntityClassification.MISC).size(0.8F, 0.9F), "chain_tie");
    public static final RegistryObject<EntityType<EntityPixieCharge>> PIXIE_CHARGE = registerEntity(EntityType.Builder.<EntityPixieCharge>create(EntityPixieCharge::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityPixieCharge::new), "pixie_charge");
    public static final RegistryObject<EntityType<EntityMyrmexSwarmer>> MYRMEX_SWARMER = registerEntity(EntityType.Builder.create(EntityMyrmexSwarmer::new, EntityClassification.CREATURE).size(1.9F, 1.86F), "myrmex_swarmer");
    public static final RegistryObject<EntityType<EntityTideTrident>> TIDE_TRIDENT = registerEntity(EntityType.Builder.<EntityTideTrident>create(EntityTideTrident::new, EntityClassification.MISC).size(0.85F, 0.5F).setCustomClientFactory(EntityTideTrident::new), "tide_trident");
    public static final RegistryObject<EntityType<EntityMobSkull>> MOB_SKULL = registerEntity(EntityType.Builder.create(EntityMobSkull::new, EntityClassification.MISC).size(0.85F, 0.85F), "mob_skull");
    public static final RegistryObject<EntityType<EntityHydraBreath>> HYDRA_BREATH = registerEntity(EntityType.Builder.<EntityHydraBreath>create(EntityHydraBreath::new, EntityClassification.MISC).size(0.9F, 0.9F).setCustomClientFactory(EntityHydraBreath::new), "hydra_breath");
    public static final RegistryObject<EntityType<EntityHydraArrow>> HYDRA_ARROW = registerEntity(EntityType.Builder.<EntityHydraArrow>create(EntityHydraArrow::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityHydraArrow::new), "hydra_arrow");public static final RegistryObject<EntityType<EntityDreadLichSkull>> DREAD_LICH_SKULL = registerEntity(EntityType.Builder.<EntityDreadLichSkull>create(EntityDreadLichSkull::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityDreadLichSkull::new), "dread_lich_skull");


    //ALLTHEMODIUM COMPAT
    public static final EntityType<EntitySeaSerpent> SEA_SERPENT = registerEntityOld(EntityType.Builder.create(EntitySeaSerpent::new, EntityClassification.CREATURE).size(0.5F, 0.5F).setTrackingRange(256), "sea_serpent");
    public static final EntityType<EntitySiren> SIREN = registerEntityOld(EntityType.Builder.create(EntitySiren::new, EntityClassification.CREATURE).size(1.6F, 0.9F), "siren");
    public static final EntityType<EntityHippocampus> HIPPOCAMPUS = registerEntityOld(EntityType.Builder.create(EntityHippocampus::new, EntityClassification.CREATURE).size(1.95F, 0.95F), "hippocampus");
    public static final EntityType<EntityTroll> TROLL = registerEntityOld(EntityType.Builder.create(EntityTroll::new, EntityClassification.MONSTER).size(1.2F, 3.5F), "troll");

    public static final EntityType<EntityAmphithere> AMPHITHERE = registerEntityOld(EntityType.Builder.create(EntityAmphithere::new, EntityClassification.CREATURE).size(2.5F, 1.25F).setTrackingRange(128), "amphithere");
    public static final EntityType<EntityHippogryph> HIPPOGRYPH = registerEntityOld(EntityType.Builder.create(EntityHippogryph::new, EntityClassification.CREATURE).size(1.7F, 1.6F).setTrackingRange(128), "hippogryph");
    public static final EntityType<EntityGhost> GHOST = registerEntityOld(EntityType.Builder.create(EntityGhost::new, EntityClassification.MONSTER).size(0.8F, 1.9F).immuneToFire(), "ghost");
    public static final EntityType<EntityGhostSword> GHOST_SWORD = registerEntityOld(EntityType.Builder.<EntityGhostSword>create(EntityGhostSword::new, EntityClassification.MISC).size(0.5F, 0.5F).setCustomClientFactory(EntityGhostSword::new), "ghost_sword");
    public static final EntityType<EntityHydra> HYDRA = registerEntityOld(EntityType.Builder.create(EntityHydra::new, EntityClassification.CREATURE).size(2.8F, 1.39F), "hydra");
    public static final EntityType<EntityDreadThrall> DREAD_THRALL = registerEntityOld(EntityType.Builder.create(EntityDreadThrall::new, EntityClassification.MONSTER).size(0.6F, 1.8F), "dread_thrall");
    public static final EntityType<EntityDreadGhoul> DREAD_GHOUL = registerEntityOld(EntityType.Builder.create(EntityDreadGhoul::new, EntityClassification.MONSTER).size(0.6F, 1.8F), "dread_ghoul");
    public static final EntityType<EntityDreadBeast> DREAD_BEAST = registerEntityOld(EntityType.Builder.create(EntityDreadBeast::new, EntityClassification.MONSTER).size(1.2F, 0.9F), "dread_beast");
    public static final EntityType<EntityDreadScuttler> DREAD_SCUTTLER = registerEntityOld(EntityType.Builder.create(EntityDreadScuttler::new, EntityClassification.MONSTER).size(1.5F, 1.3F), "dread_scuttler");
    public static final EntityType<EntityDreadLich> DREAD_LICH = registerEntityOld(EntityType.Builder.create(EntityDreadLich::new, EntityClassification.MONSTER).size(0.6F, 1.8F), "dread_lich");
    public static final EntityType<EntityDreadKnight> DREAD_KNIGHT = registerEntityOld(EntityType.Builder.create(EntityDreadKnight::new, EntityClassification.MONSTER).size(0.6F, 1.8F), "dread_knight");
    public static final EntityType<EntityDreadHorse> DREAD_HORSE = registerEntityOld(EntityType.Builder.create(EntityDreadHorse::new, EntityClassification.MONSTER).size(1.3964844F, 1.6F), "dread_horse");

    private static <T extends Entity> EntityType<T> registerEntityOld(EntityType.Builder<T> builder, String entityName) {
        ResourceLocation nameLoc = new ResourceLocation(IceAndFire.MODID, entityName);
        return (EntityType<T>) builder.build(entityName).setRegistryName(nameLoc);
    }

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(EntityType.Builder<T> builder, String entityName) {
        return ENTITIES.register(entityName, () -> builder.build(entityName));
    }

    public static void setup() {
    }
    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(SEA_SERPENT);
        event.getRegistry().register(SIREN);
        event.getRegistry().register(HIPPOCAMPUS);
        event.getRegistry().register(TROLL);
        event.getRegistry().register(AMPHITHERE);
        event.getRegistry().register(HIPPOGRYPH);
        event.getRegistry().register(GHOST);
        event.getRegistry().register(GHOST_SWORD);
        event.getRegistry().register(HYDRA);
        event.getRegistry().register(DREAD_THRALL);
        event.getRegistry().register(DREAD_GHOUL);
        event.getRegistry().register(DREAD_BEAST);
        event.getRegistry().register(DREAD_SCUTTLER);
        event.getRegistry().register(DREAD_LICH);
        event.getRegistry().register(DREAD_KNIGHT);
        event.getRegistry().register(DREAD_HORSE);


    }
    @SubscribeEvent
    public static void bakeAttributes(EntityAttributeCreationEvent creationEvent) {
        creationEvent.put(DRAGON_EGG.get(), EntityDragonEgg.bakeAttributes().create());
        creationEvent.put(DRAGON_SKULL.get(), EntityDragonSkull.bakeAttributes().create());
        creationEvent.put(FIRE_DRAGON.get(), EntityFireDragon.bakeAttributes().create());
        creationEvent.put(ICE_DRAGON.get(), EntityIceDragon.bakeAttributes().create());
        creationEvent.put(LIGHTNING_DRAGON.get(), EntityLightningDragon.bakeAttributes().create());
        creationEvent.put(HIPPOGRYPH, EntityHippogryph.bakeAttributes().create());
        creationEvent.put(GORGON.get(), EntityGorgon.bakeAttributes().create());
        creationEvent.put(STONE_STATUE.get(), EntityStoneStatue.bakeAttributes().create());
        creationEvent.put(PIXIE.get(), EntityPixie.bakeAttributes().create());
        creationEvent.put(CYCLOPS.get(), EntityCyclops.bakeAttributes().create());
        creationEvent.put(SIREN, EntitySiren.bakeAttributes().create());
        creationEvent.put(HIPPOCAMPUS, EntityHippocampus.bakeAttributes().create());
        creationEvent.put(DEATH_WORM.get(), EntityDeathWorm.bakeAttributes().create());
        creationEvent.put(COCKATRICE.get(), EntityCockatrice.bakeAttributes().create());
        creationEvent.put(STYMPHALIAN_BIRD.get(), EntityStymphalianBird.bakeAttributes().create());
        creationEvent.put(TROLL, EntityTroll.bakeAttributes().create());
        creationEvent.put(MYRMEX_WORKER.get(), EntityMyrmexWorker.bakeAttributes().create());
        creationEvent.put(MYRMEX_SOLDIER.get(), EntityMyrmexSoldier.bakeAttributes().create());
        creationEvent.put(MYRMEX_SENTINEL.get(), EntityMyrmexSentinel.bakeAttributes().create());
        creationEvent.put(MYRMEX_ROYAL.get(), EntityMyrmexRoyal.bakeAttributes().create());
        creationEvent.put(MYRMEX_QUEEN.get(), EntityMyrmexQueen.bakeAttributes().create());
        creationEvent.put(MYRMEX_EGG.get(), EntityMyrmexEgg.bakeAttributes().create());
        creationEvent.put(MYRMEX_SWARMER.get(), EntityMyrmexSwarmer.bakeAttributes().create());
        creationEvent.put(AMPHITHERE, EntityAmphithere.bakeAttributes().create());
        creationEvent.put(SEA_SERPENT, EntitySeaSerpent.bakeAttributes().create());
        creationEvent.put(MOB_SKULL.get(), EntityMobSkull.bakeAttributes().create());
        creationEvent.put(DREAD_THRALL, EntityDreadThrall.bakeAttributes().create());
        creationEvent.put(DREAD_LICH, EntityDreadLich.bakeAttributes().create());
        creationEvent.put(DREAD_BEAST, EntityDreadBeast.bakeAttributes().create());
        creationEvent.put(DREAD_HORSE, EntityDreadHorse.bakeAttributes().create());
        creationEvent.put(DREAD_GHOUL, EntityDreadGhoul.bakeAttributes().create());
        creationEvent.put(DREAD_KNIGHT, EntityDreadKnight.bakeAttributes().create());
        creationEvent.put(DREAD_SCUTTLER, EntityDreadScuttler.bakeAttributes().create());
        creationEvent.put(HYDRA, EntityHydra.bakeAttributes().create());
        creationEvent.put(GHOST, EntityGhost.bakeAttributes().create());
    }

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event) {
        EntitySpawnPlacementRegistry.register(HIPPOGRYPH, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityHippogryph::canSpawnOn);
        EntitySpawnPlacementRegistry.register(TROLL, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityTroll::canTrollSpawnOn);
        EntitySpawnPlacementRegistry.register(DREAD_LICH, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityDreadLich::canLichSpawnOn);
        EntitySpawnPlacementRegistry.register(COCKATRICE.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EntityCockatrice::canSpawnOn);
        EntitySpawnPlacementRegistry.register(AMPHITHERE, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING, EntityAmphithere::canAmphithereSpawnOn);
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

    public static void onBiomesLoad(BiomeLoadingEvent event) {
        Biome biome = ForgeRegistries.BIOMES.getValue(event.getName());

        if (IafConfig.spawnHippogryphs && BiomeConfig.test(BiomeConfig.hippogryphBiomes, biome)) {
            event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(IafEntityRegistry.HIPPOGRYPH, IafConfig.hippogryphSpawnRate, 1, 1));
            LOADED_ENTITIES.put("HIPPOGRYPH", true);
        }
        if (IafConfig.spawnLiches && BiomeConfig.test(BiomeConfig.mausoleumBiomes, biome)) {
            event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(IafEntityRegistry.DREAD_LICH, IafConfig.lichSpawnRate, 1, 1));
            LOADED_ENTITIES.put("DREAD_LICH", true);
        }
        if (IafConfig.spawnCockatrices && BiomeConfig.test(BiomeConfig.cockatriceBiomes, biome)) {
            event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(IafEntityRegistry.COCKATRICE.get(), IafConfig.cockatriceSpawnRate, 1, 2));
            LOADED_ENTITIES.put("COCKATRICE", true);
        }
        if (IafConfig.spawnAmphitheres && BiomeConfig.test(BiomeConfig.amphithereBiomes, biome)) {
            event.getSpawns().getSpawner(EntityClassification.CREATURE).add(new MobSpawnInfo.Spawners(IafEntityRegistry.AMPHITHERE, IafConfig.amphithereSpawnRate, 1, 3));
            LOADED_ENTITIES.put("AMPHITHERE", true);
        }
        if (IafConfig.spawnTrolls && (
                BiomeConfig.test(BiomeConfig.forestTrollBiomes, biome) ||
                        BiomeConfig.test(BiomeConfig.snowyTrollBiomes, biome) ||
                        BiomeConfig.test(BiomeConfig.mountainTrollBiomes, biome)
        )) {
            event.getSpawns().getSpawner(EntityClassification.MONSTER).add(new MobSpawnInfo.Spawners(IafEntityRegistry.TROLL, IafConfig.trollSpawnRate, 1, 3));
            if (BiomeConfig.test(BiomeConfig.forestTrollBiomes, biome)) LOADED_ENTITIES.put("TROLL_F", true);
            if (BiomeConfig.test(BiomeConfig.snowyTrollBiomes, biome)) LOADED_ENTITIES.put("TROLL_S", true);
            if (BiomeConfig.test(BiomeConfig.mountainTrollBiomes, biome)) LOADED_ENTITIES.put("TROLL_M", true);
        }
    }
}
