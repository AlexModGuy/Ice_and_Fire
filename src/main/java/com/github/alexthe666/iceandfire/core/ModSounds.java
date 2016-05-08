package com.github.alexthe666.iceandfire.core;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

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
	public static int soundID = 367;
	
	public static void init(){
		dragon_hatch = getRegisteredSoundEvent("iceandfire:dragonegg.hatch");
		firedragon_child_idle = getRegisteredSoundEvent("iceandfire:firedragon.child.idle");
		firedragon_child_hurt = getRegisteredSoundEvent("iceandfire:firedragon.child.hurt");
		firedragon_child_death = getRegisteredSoundEvent("iceandfire:firedragon.child.death");
		firedragon_child_roar = getRegisteredSoundEvent("iceandfire:firedragon.child.roar");
		firedragon_teen_idle = getRegisteredSoundEvent("iceandfire:firedragon.teen.idle");
		firedragon_teen_hurt = getRegisteredSoundEvent("iceandfire:firedragon.teen.hurt");
		firedragon_teen_death = getRegisteredSoundEvent("iceandfire:firedragon.teen.death");
		firedragon_teen_roar = getRegisteredSoundEvent("iceandfire:firedragon.teen.roar");
		firedragon_adult_idle = getRegisteredSoundEvent("iceandfire:firedragon.adult.idle");
		firedragon_adult_hurt = getRegisteredSoundEvent("iceandfire:firedragon.adult.hurt");
		firedragon_adult_death = getRegisteredSoundEvent("iceandfire:firedragon.adult.death");
		firedragon_adult_roar = getRegisteredSoundEvent("iceandfire:firedragon.adult.roar");
	}

    private static SoundEvent getRegisteredSoundEvent(String id){
    	registerSound(id);
        SoundEvent soundevent = (SoundEvent)SoundEvent.soundEventRegistry.getObject(new ResourceLocation(id));

        if (soundevent == null)
        {
            throw new IllegalStateException("Invalid Sound requested: " + id);
        }
        else
        {
            return soundevent;
        }
    }
    
    private static void registerSound(String soundNameIn){
        ResourceLocation resourcelocation = new ResourceLocation(soundNameIn);
        SoundEvent.soundEventRegistry.register(soundID++, resourcelocation, new SoundEvent(resourcelocation));
    }
}
