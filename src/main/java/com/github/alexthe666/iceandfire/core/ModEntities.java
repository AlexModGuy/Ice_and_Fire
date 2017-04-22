package com.github.alexthe666.iceandfire.core;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.*;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {

    public static void registerSpawnable(Class entityClass, String name, int id, int mainColor, int subColor) {
        EntityRegistry.registerModEntity(entityClass, name, id, IceAndFire.INSTANCE, 64, 3, true, mainColor, subColor);
    }

    public static void registerUnspawnable(Class entityClass, String name, int id) {
        EntityRegistry.registerModEntity(entityClass, name, id, IceAndFire.INSTANCE, 64, 3, true);
    }

    public static void init() {
        registerUnspawnable(EntityDragonEgg.class, "dragonegg", 1);
        registerUnspawnable(EntityDragonArrow.class, "dragonarrow", 2);
        registerUnspawnable(EntityDragonSkull.class, "dragonskull", 3);
        registerUnspawnable(EntityDragonFire.class, "dragonfire", 4);
        registerSpawnable(EntityFireDragon.class, "firedragon", 5, 0X340000, 0XA52929);
        registerUnspawnable(EntityDragonIceProjectile.class, "dragonice", 6);
        registerSpawnable(EntityIceDragon.class, "icedragon", 7, 0XB5DDFB, 0X7EBAF0);
        registerUnspawnable(EntityDragonFireCharge.class, "dragonfirecharge", 8);
        registerUnspawnable(EntityDragonIceCharge.class, "dragonicecharge", 9);
        registerSpawnable(EntitySnowVillager.class, "snowvillager", 10, 0X3C2A23, 0X70B1CF);

    }
}
