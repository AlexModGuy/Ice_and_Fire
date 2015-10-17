package com.github.alexthe666.iceandfire.core;

import java.util.Random;

import net.minecraft.entity.EntityList;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityDragonFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;

public class ModEntities {
	
	public static void registerSpawnable(Class entityClass, String name, int mainColor, int subColor, int range){
		int entityId = EntityRegistry.findGlobalUniqueEntityId();
		long x = name.hashCode();
		Random random = new Random(x);
		EntityRegistry.registerGlobalEntityID(entityClass, name, entityId);
		EntityRegistry.registerModEntity(entityClass, name, entityId, IceAndFire.instance, range, 1, true);
		EntityList.entityEggs.put(Integer.valueOf(entityId), new EntityList.EntityEggInfo(entityId, mainColor, subColor));
	}
	public static void registerUnspawnable(Class entityClass, String name){
		int entityId = EntityRegistry.findGlobalUniqueEntityId();
		EntityRegistry.registerGlobalEntityID(entityClass, name, entityId);
		EntityRegistry.registerModEntity(entityClass, name, entityId, IceAndFire.instance, 64, 1, true);

	}
	public static void init() {
		registerSpawnable(EntityFireDragon.class, "iceandfire.firedragon", 0X340000, 0XA52929, 64);	
		registerUnspawnable(EntityDragonEgg.class, "iceandfire.dragonegg");	
		registerUnspawnable(EntityDragonArrow.class, "iceandfire.dragonarrow");	
		registerUnspawnable(EntityDragonSkull.class, "iceandfire.dragonskull");	
		registerUnspawnable(EntityDragonFire.class, "iceandfire.dragonfire");	

	}
}
