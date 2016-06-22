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
		dragon_hatch = registerSound("dragonegg.hatch");
		firedragon_child_idle = registerSound("firedragon.child.idle");
		firedragon_child_hurt = registerSound("firedragon.child.hurt");
		firedragon_child_death = registerSound("firedragon.child.death");
		firedragon_child_roar = registerSound("firedragon.child.roar");
		firedragon_teen_idle = registerSound("firedragon.teen.idle");
		firedragon_teen_hurt = registerSound("firedragon.teen.hurt");
		firedragon_teen_death = registerSound("firedragon.teen.death");
		firedragon_teen_roar = registerSound("firedragon.teen.roar");
		firedragon_adult_idle = registerSound("firedragon.adult.idle");
		firedragon_adult_hurt = registerSound("firedragon.adult.hurt");
		firedragon_adult_death = registerSound("firedragon.adult.death");
		firedragon_adult_roar = registerSound("firedragon.adult.roar");
	}

	private static SoundEvent registerSound(String sound) {
		return GameRegistry.register(new SoundEvent(new ResourceLocation("iceandfire", sound)).setRegistryName(new ResourceLocation("iceandfire", sound)));

	}
}
