package com.github.alexthe666.iceandfire.access;

import net.minecraft.entity.player.EntityPlayer;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;

public class IceAndFireHooks {
	/**
	 * Called when a player is sneaking. 
	 */
	public static void dismount(EntityPlayer player){
		System.out.println("i");
		if(player.ridingEntity != null && player.ridingEntity instanceof EntityDragonBase){
			
		}else{
			player.mountEntity(null);
		}
	}
}
