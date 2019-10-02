package com.github.alexthe666.iceandfire.compat.waila;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;
import com.google.common.base.Strings;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.config.FormattingConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

import static mcp.mobius.waila.api.SpecialChars.getRenderString;

public class HUDHandlerMultipartMob implements IWailaEntityProvider {
    public static int nhearts = 20;
    public static float maxhpfortext = 40.0f;
    public static IWailaEntityProvider INSTANCE = new HUDHandlerMultipartMob();

    @Nonnull
    @Override
    public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        currenttip.clear();
        EntityMutlipartPart part = (EntityMutlipartPart) entity;
        if (!Strings.isNullOrEmpty(FormattingConfig.entityFormat)) {
            try {
                currenttip.add("\u00a7r" + String.format(FormattingConfig.entityFormat, part.getParent().getName()));
            } catch (Exception e) {
            }
        }
        return currenttip;
    }

    @Nonnull
    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        EntityMutlipartPart part = (EntityMutlipartPart) entity;
        if (config.getConfig("general.showhp") && part.getParent() instanceof EntityLivingBase) {
            nhearts = nhearts <= 0 ? 20 : nhearts;
            float health = part.getParent().getHealth() / 2.0f;
            float maxhp = part.getParent().getMaxHealth() / 2.0f;

            if (part.getParent().getMaxHealth() > maxhpfortext)
                currenttip.add(String.format(I18n.translateToLocal("hud.msg.health") + ": %.0f / %.0f", part.getParent().getHealth(), part.getParent().getMaxHealth()));
            else
                currenttip.add(getRenderString("waila.health", String.valueOf(nhearts), String.valueOf(health), String.valueOf(maxhp)));

            if (part.getParent() instanceof EntityDragonBase) {
                EntityDragonBase dragon = (EntityDragonBase) part.getParent();
                currenttip.add(String.format(I18n.translateToLocal("dragon.stage") + dragon.getDragonStage()));
                if (dragon.isMale()) {
                    currenttip.add(String.format(I18n.translateToLocal("dragon.gender.male")));
                } else {
                    currenttip.add(String.format(I18n.translateToLocal("dragon.gender.female")));
                }
            }
        }

        return currenttip;
    }

    @Nonnull
    @Override
    public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        currenttip.clear();
        if (!Strings.isNullOrEmpty(FormattingConfig.modNameFormat))
            currenttip.add(String.format(FormattingConfig.modNameFormat, "Ice and Fire"));
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