package com.github.alexthe666.iceandfire.core;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSounds {
    public static SoundEvent dragon_hatch;
    public static SoundEvent firedragon_breath;
    public static SoundEvent icedragon_breath;
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
    public static SoundEvent icedragon_child_idle;
    public static SoundEvent icedragon_child_hurt;
    public static SoundEvent icedragon_child_death;
    public static SoundEvent icedragon_child_roar;
    public static SoundEvent icedragon_teen_idle;
    public static SoundEvent icedragon_teen_hurt;
    public static SoundEvent icedragon_teen_death;
    public static SoundEvent icedragon_teen_roar;
    public static SoundEvent icedragon_adult_idle;
    public static SoundEvent icedragon_adult_hurt;
    public static SoundEvent icedragon_adult_death;
    public static SoundEvent icedragon_adult_roar;
    public static SoundEvent dragonflute;
    public static SoundEvent hippogryph_idle;
    public static SoundEvent hippogryph_hurt;
    public static SoundEvent hippogryph_die;

    public static void init() {
        dragon_hatch = registerSound("dragonegg.hatch");
        firedragon_breath = registerSound("firedragon.breath");
        icedragon_breath = registerSound("icedragon.breath");
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
        icedragon_child_idle = registerSound("icedragon.child.idle");
        icedragon_child_hurt = registerSound("icedragon.child.hurt");
        icedragon_child_death = registerSound("icedragon.child.death");
        icedragon_child_roar = registerSound("icedragon.child.roar");
        icedragon_teen_idle = registerSound("icedragon.teen.idle");
        icedragon_teen_hurt = registerSound("icedragon.teen.hurt");
        icedragon_teen_death = registerSound("icedragon.teen.death");
        icedragon_teen_roar = registerSound("icedragon.teen.roar");
        icedragon_adult_idle = registerSound("icedragon.adult.idle");
        icedragon_adult_hurt = registerSound("icedragon.adult.hurt");
        icedragon_adult_death = registerSound("icedragon.adult.death");
        icedragon_adult_roar = registerSound("icedragon.adult.roar");
        dragonflute = registerSound("dragonflute");
        hippogryph_idle = registerSound("hippogryph.idle");
        hippogryph_hurt = registerSound("hippogryph.hurt");
        hippogryph_die = registerSound("hippogryph.die");

    }

    private static SoundEvent registerSound(String sound) {
        return GameRegistry.register(new SoundEvent(new ResourceLocation("iceandfire", sound)).setRegistryName(new ResourceLocation("iceandfire", sound)));

    }
}
