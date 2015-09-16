package com.github.alexthe666.iceandfire.event;

import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.github.alexthe666.iceandfire.core.ModItems;

public class EventLiving {
	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {

		if(event.entityLiving instanceof EntitySkeleton) {
			if(((EntitySkeleton)event.entityLiving).getSkeletonType() == 1){
			event.entityLiving.dropItem(ModItems.witherbone, event.entityLiving.getRNG().nextInt(2));
			}
		}
	}

}
