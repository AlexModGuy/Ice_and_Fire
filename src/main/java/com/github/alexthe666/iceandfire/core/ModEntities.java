package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.*;
import com.github.alexthe666.iceandfire.enums.EnumHippogryphTypes;
import com.github.alexthe666.iceandfire.enums.EnumTroll;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;

public class ModEntities {
/*
	public static void registerSpawnable(Class entityClass, String name, int id, int mainColor, int subColor) {
		EntityRegistry.registerModEntity(new ResourceLocation(IceAndFire.MODID, name), entityClass, name, id, IceAndFire.INSTANCE, 64, 3, true, mainColor, subColor);
	}

	public static void registerUnspawnable(Class entityClass, String name, int id) {
		EntityRegistry.registerModEntity(new ResourceLocation(IceAndFire.MODID, name), entityClass, name, id, IceAndFire.INSTANCE, 64, 3, true);
	}
*/
	public static void init() {
		EntityPropertiesHandler.INSTANCE.registerProperties(StoneEntityProperties.class);
		EntityPropertiesHandler.INSTANCE.registerProperties(MiscEntityProperties.class);
		EntityPropertiesHandler.INSTANCE.registerProperties(FrozenEntityProperties.class);
		EntityPropertiesHandler.INSTANCE.registerProperties(SirenEntityProperties.class);
		EntityPropertiesHandler.INSTANCE.registerProperties(ChickenEntityProperties.class);
		EntityPropertiesHandler.INSTANCE.registerProperties(ChainEntityProperties.class);
		/*registerUnspawnable(EntityDragonEgg.class, "dragonegg", 1);
		registerUnspawnable(EntityDragonArrow.class, "dragonarrow", 2);
		registerUnspawnable(EntityDragonSkull.class, "dragonskull", 3);
		registerUnspawnable(EntityDragonFire.class, "dragonfire", 4);
		registerSpawnable(EntityFireDragon.class, "firedragon", 5, 0X340000, 0XA52929);
		registerUnspawnable(EntityDragonIceProjectile.class, "dragonice", 6);
		registerSpawnable(EntityIceDragon.class, "icedragon", 7, 0XB5DDFB, 0X7EBAF0);
		registerUnspawnable(EntityDragonFireCharge.class, "dragonfirecharge", 8);
		registerUnspawnable(EntityDragonIceCharge.class, "dragonicecharge", 9);
		registerSpawnable(EntitySnowVillager.class, "snowvillager", 10, 0X3C2A23, 0X70B1CF);
		registerUnspawnable(EntityHippogryphEgg.class, "hippogryphegg", 11);
		registerSpawnable(EntityHippogryph.class, "hippogryph", 12, 0XD8D8D8, 0XD1B55D);
		registerUnspawnable(EntityStoneStatue.class, "stonestatue", 13);
		registerSpawnable(EntityGorgon.class, "gorgon", 14, 0XD0D99F, 0X684530);
		registerSpawnable(EntityPixie.class, "if_pixie", 15, 0XFF7F89, 0XE2CCE2);
		registerSpawnable(EntityCyclops.class, "cyclops", 17, 0XBBAA92, 0X594729);
		registerSpawnable(EntitySiren.class, "siren", 18, 0X8EE6CA, 0XF2DFC8);
		registerSpawnable(EntityHippocampus.class, "hippocampus", 19, 0X4491C7, 0X4FC56B);
		registerSpawnable(EntityDeathWorm.class, "deathworm", 20, 0XD1CDA3, 0X423A3A);
		registerUnspawnable(EntityDeathWormEgg.class, "deathwormegg", 21);
		registerSpawnable(EntityCockatrice.class, "if_cockatrice", 22, 0X8F5005, 0X4F5A23);
		registerUnspawnable(EntityCockatriceEgg.class, "if_cockatriceegg", 23);
		registerSpawnable(EntityStymphalianBird.class, "stymphalianbird", 24, 0X744F37, 0X9E6C4B);
		registerUnspawnable(EntityStymphalianFeather.class, "stymphalianfeather", 25);
		registerUnspawnable(EntityStymphalianArrow.class, "stymphalianarrow", 26);
		registerSpawnable(EntityTroll.class, "if_troll", 27, 0X3D413D, 0X58433A);*/
		if (IceAndFire.CONFIG.spawnHippogryphs) {
			for (EnumHippogryphTypes type : EnumHippogryphTypes.values()) {
				if (!type.developer) {
					for (Biome biome : Biome.REGISTRY) {
						if (biome != null && BiomeDictionary.hasType(biome, BiomeDictionary.Type.HILLS)) {
							List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.CREATURE);
							spawnList.add(new Biome.SpawnListEntry(EntityHippogryph.class, IceAndFire.CONFIG.hippogryphSpawnRate, 1, 1));
						}
					}
				}
			}
		}
		if (IceAndFire.CONFIG.spawnDeathWorm) {
			for (Biome biome : Biome.REGISTRY) {
				if (biome != null && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.DRY) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.BEACH) && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA)) {
					List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.CREATURE);
					spawnList.add(new Biome.SpawnListEntry(EntityDeathWorm.class, IceAndFire.CONFIG.deathWormSpawnRate, 1, 3));
				}
			}
		}
		if (IceAndFire.CONFIG.spawnTrolls) {
			for (EnumTroll type : EnumTroll.values()) {
				for (Biome biome : Biome.REGISTRY) {
					if (biome != null && BiomeDictionary.hasType(biome, type.spawnBiome)) {
						List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.MONSTER);
						spawnList.add(new Biome.SpawnListEntry(EntityTroll.class, IceAndFire.CONFIG.trollSpawnRate, 1, 1));
					}
				}
			}
		}
		if (IceAndFire.CONFIG.spawnCockatrices) {
			for (Biome biome : Biome.REGISTRY) {
				if (biome != null && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SAVANNA) && BiomeDictionary.hasType(biome, BiomeDictionary.Type.SPARSE)) {
					List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.CREATURE);
					spawnList.add(new Biome.SpawnListEntry(EntityCockatrice.class, IceAndFire.CONFIG.cockatriceSpawnRate, 1, 2));
				}
			}
		}
		if (IceAndFire.CONFIG.spawnAmphitheres) {
			for (Biome biome : Biome.REGISTRY) {
				if (biome != null && BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)) {
					List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.CREATURE);
					spawnList.add(new Biome.SpawnListEntry(EntityAmphithere.class, IceAndFire.CONFIG.amphithereSpawnRate, 1, 3));
				}
			}
		}
	}
}
