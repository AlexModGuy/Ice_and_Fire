package com.github.alexthe666.iceandfire.util;

import com.github.alexthe666.iceandfire.entity.ISyncMount;
import net.minecraft.entity.Entity;

public class IceAndFireCoreUtils {

    public static double getFastestEntityMotionSpeed(Entity e){
        return e instanceof ISyncMount ? 1.0D : 0.0625D;
    }
}
