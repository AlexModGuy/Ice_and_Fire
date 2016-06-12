package com.github.alexthe666.iceandfire.core;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSounds {
	public static SoundEvent dragon_hatch;
	public static SoundEvent firedragon_child_idle;
	public static SoundEvent firedragon_child_hurt;
	public static SoundEvent firedragon_child_death;
	public static SoundEvent firedragon_child_roar;
	public static SoundEvent firedragon_teen_idle;
	public static SoundEvent firedragon_teen_hurt;
	public static SoundEvent firedragon_teen_death;
	public static SoundEvent firedragon_teen_roar;
	public static SoundEvent firedragon_adult_idle;
	public static SoundEvent firedragon_adult_hurt;
	public static SoundEvent firedragon_adult_death;
	public static SoundEvent firedragon_adult_roar;

	public static void init() {
		dragon_hatch = registerSound("iceandfire:dragonegg.hatch");
		firedragon_child_idle = registerSound("iceandfire:firedragon.child.idle");
		firedragon_child_hurt = registerSound("iceandfire:firedragon.child.hurt");
		firedragon_child_death = registerSound("iceandfire:firedragon.child.death");
		firedragon_child_roar = registerSound("iceandfire:firedragon.child.roar");
		firedragon_teen_idle = registerSound("iceandfire:firedragon.teen.idle");
		firedragon_teen_hurt = registerSound("iceandfire:firedragon.teen.hurt");
		firedragon_teen_death = registerSound("iceandfire:firedragon.teen.death");
		firedragon_teen_roar = registerSound("iceandfire:firedragon.teen.roar");
		firedragon_adult_idle = registerSound("iceandfire:firedragon.adult.idle");
		firedragon_adult_hurt = registerSound("iceandfire:firedragon.adult.hurt");
		firedragon_adult_death = registerSound("iceandfire:firedragon.adult.death");
		firedragon_adult_roar = registerSound("iceandfire:firedragon.adult.roar");
	}


	private static SoundEvent registerSound(String soundNameIn) {
		ResourceLocation resourcelocation = new ResourceLocation(soundNameIn);
		return GameRegistry.register(new SoundEvent(resourcelocation), resourcelocation);
	}
}
