package com.github.alexthe666.iceandfire.misc;

import net.ilexiconn.llibrary.common.animation.Animation;
import net.ilexiconn.llibrary.common.animation.IAnimated;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.messagee.MessageCorrectAnimation;

public class AnimationTicker
{
    public int animationId;
    public int duration;

    public AnimationTicker(int id, int d)
    {
        animationId = id;
        duration = d;
    }

    public static void sendAnimationPacket(IAnimated entity, Animation animation)
    {
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) return;
        entity.setAnimation(animation);
        IceAndFire.channel.sendToAll(new MessageCorrectAnimation((byte) animation.animationId, ((Entity) entity).getEntityId()));
    }

    public static void tickAnimations(IAnimated entity)
    {
        if (entity.getAnimation() == null) entity.setAnimation(entity.animations()[0]);
        else
        {
            if (entity.getAnimation().animationId != 0)
            {
                if (entity.getAnimationTick() == 0) sendAnimationPacket(entity, entity.getAnimation());
                if (entity.getAnimationTick() < entity.getAnimation().duration) entity.setAnimationTick(entity.getAnimationTick() + 1);
                if (entity.getAnimationTick() == entity.getAnimation().duration)
                {
                    entity.setAnimationTick(0);
                    entity.setAnimation(entity.animations()[0]);
                }
            }
        }
    }
}
