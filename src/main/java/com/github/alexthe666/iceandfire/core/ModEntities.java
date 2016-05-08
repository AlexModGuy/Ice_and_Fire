package com.github.alexthe666.iceandfire.core;

import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonArrow;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityDragonFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import com.github.alexthe666.iceandfire.entity.EntityFireDragon;

public class ModEntities {

	public static void registerSpawnable(Class entityClass, String name, int id, int mainColor, int subColor){
		EntityRegistry.registerModEntity(entityClass, name, id, IceAndFire.instance, 64, 3, true, mainColor, subColor);
	}
	public static void registerUnspawnable(Class entityClass, String name, int id){
		EntityRegistry.registerModEntity(entityClass, name, id, IceAndFire.instance, 64, 3, true);
	}

	public static void init() {
		registerUnspawnable(EntityDragonEgg.class, "dragonegg", 1);	
		registerUnspawnable(EntityDragonArrow.class, "dragonarrow", 2);	
		registerUnspawnable(EntityDragonSkull.class, "dragonskull", 3);	
		registerUnspawnable(EntityDragonFire.class, "dragonfire", 4);	
		registerSpawnable(EntityFireDragon.class, "firedragon", 5, 0X340000, 0XA52929);	
	}
}
