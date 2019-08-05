package com.github.alexthe666.iceandfire.compat.waila;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;
import mcp.mobius.waila.addons.core.HUDHandlerEntities;
import mcp.mobius.waila.addons.minecraft.HUDHandlerFurnace;
import mcp.mobius.waila.addons.minecraft.HUDHandlerVanilla;
import mcp.mobius.waila.addons.minecraft.HUDHandlerVillager;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;

@WailaPlugin
public class PluginIceAndFire implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerHeadProvider(HUDHandlerMultipartMob.INSTANCE, EntityMutlipartPart.class);
        registrar.registerBodyProvider(HUDHandlerMultipartMob.INSTANCE, EntityMutlipartPart.class);
        registrar.registerBodyProvider(HUDHandlerDragon.INSTANCE, EntityDragonBase.class);
        registrar.registerTailProvider(HUDHandlerMultipartMob.INSTANCE, EntityMutlipartPart.class);
        registrar.registerNBTProvider(HUDHandlerMultipartMob.INSTANCE, EntityMutlipartPart.class);
    }
}