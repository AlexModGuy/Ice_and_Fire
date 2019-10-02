package com.github.alexthe666.iceandfire.compat.waila;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class HUDHandlerDragon implements IWailaEntityProvider {
    public static int nhearts = 20;
    public static float maxhpfortext = 40.0f;
    public static IWailaEntityProvider INSTANCE = new HUDHandlerDragon();

    @Nonnull
    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        EntityDragonBase dragon = (EntityDragonBase) entity;
        currenttip.add(String.format(I18n.translateToLocal("dragon.stage") + dragon.getDragonStage()));
        if (dragon.isMale()) {
            currenttip.add(String.format(I18n.translateToLocal("dragon.gender.male")));
        } else {
            currenttip.add(String.format(I18n.translateToLocal("dragon.gender.female")));
        }
        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
        if (ent instanceof EntityMutlipartPart)
            ent.writeToNBT(tag);
        return tag;
    }
}