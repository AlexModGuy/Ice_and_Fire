package com.github.alexthe666.iceandfire.event;

import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.github.alexthe666.iceandfire.core.ModItems;

public class EventLiving {
	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		if (event.getEntityLiving() instanceof EntitySkeleton) {
			if (((EntitySkeleton) event.getEntityLiving()).func_189771_df() == SkeletonType.WITHER) {
				event.getEntityLiving().dropItem(ModItems.witherbone, event.getEntityLiving().getRNG().nextInt(2));
			}
		}
	}

}
