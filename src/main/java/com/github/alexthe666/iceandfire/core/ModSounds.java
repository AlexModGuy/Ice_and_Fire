package com.github.alexthe666.iceandfire.core;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.github.alexthe666.iceandfire.IceAndFire.MODID;

public class ModSounds {

	@GameRegistry.ObjectHolder("dragon_hatch")
	public static final SoundEvent DRAGON_HATCH = createSoundEvent("dragon_hatch");

	@GameRegistry.ObjectHolder("firedragon_breath")
	public static final SoundEvent FIREDRAGON_BREATH = createSoundEvent("firedragon_breath");

	@GameRegistry.ObjectHolder("icedragon_breath")
	public static final SoundEvent ICEDRAGON_BREATH = createSoundEvent("icedragon_breath");

	@GameRegistry.ObjectHolder("firedragon_child_idle")
	public static final SoundEvent FIREDRAGON_CHILD_IDLE = createSoundEvent("firedragon_child_idle");

	@GameRegistry.ObjectHolder("firedragon_child_hurt")
	public static final SoundEvent FIREDRAGON_CHILD_HURT = createSoundEvent("firedragon_child_hurt");

	@GameRegistry.ObjectHolder("firedragon_child_death")
	public static final SoundEvent FIREDRAGON_CHILD_DEATH = createSoundEvent("firedragon_child_death");

	@GameRegistry.ObjectHolder("firedragon_child_roar")
	public static final SoundEvent FIREDRAGON_CHILD_ROAR = createSoundEvent("firedragon_child_roar");

	@GameRegistry.ObjectHolder("firedragon_teen_roar")
	public static final SoundEvent FIREDRAGON_TEEN_ROAR = createSoundEvent("firedragon_teen_roar");

	@GameRegistry.ObjectHolder("firedragon_teen_idle")
	public static final SoundEvent FIREDRAGON_TEEN_IDLE = createSoundEvent("firedragon_teen_idle");

	@GameRegistry.ObjectHolder("firedragon_teen_death")
	public static final SoundEvent FIREDRAGON_TEEN_DEATH = createSoundEvent("firedragon_teen_death");

	@GameRegistry.ObjectHolder("firedragon_teen_hurt")
	public static final SoundEvent FIREDRAGON_TEEN_HURT = createSoundEvent("firedragon_teen_hurt");

	@GameRegistry.ObjectHolder("firedragon_adult_roar")
	public static final SoundEvent FIREDRAGON_ADULT_ROAR = createSoundEvent("firedragon_adult_roar");

	@GameRegistry.ObjectHolder("firedragon_adult_idle")
	public static final SoundEvent FIREDRAGON_ADULT_IDLE = createSoundEvent("firedragon_adult_idle");

	@GameRegistry.ObjectHolder("firedragon_adult_death")
	public static final SoundEvent FIREDRAGON_ADULT_DEATH = createSoundEvent("firedragon_adult_death");

	@GameRegistry.ObjectHolder("firedragon_adult_hurt")
	public static final SoundEvent FIREDRAGON_ADULT_HURT = createSoundEvent("firedragon_adult_hurt");

	@GameRegistry.ObjectHolder("icedragon_child_idle")
	public static final SoundEvent ICEDRAGON_CHILD_IDLE = createSoundEvent("icedragon_child_idle");

	@GameRegistry.ObjectHolder("icedragon_child_hurt")
	public static final SoundEvent ICEDRAGON_CHILD_HURT = createSoundEvent("icedragon_child_hurt");

	@GameRegistry.ObjectHolder("icedragon_child_death")
	public static final SoundEvent ICEDRAGON_CHILD_DEATH = createSoundEvent("icedragon_child_death");

	@GameRegistry.ObjectHolder("icedragon_child_roar")
	public static final SoundEvent ICEDRAGON_CHILD_ROAR = createSoundEvent("icedragon_child_roar");

	@GameRegistry.ObjectHolder("icedragon_teen_roar")
	public static final SoundEvent ICEDRAGON_TEEN_ROAR = createSoundEvent("icedragon_teen_roar");

	@GameRegistry.ObjectHolder("icedragon_teen_idle")
	public static final SoundEvent ICEDRAGON_TEEN_IDLE = createSoundEvent("icedragon_teen_idle");

	@GameRegistry.ObjectHolder("icedragon_teen_death")
	public static final SoundEvent ICEDRAGON_TEEN_DEATH = createSoundEvent("icedragon_teen_death");

	@GameRegistry.ObjectHolder("icedragon_teen_hurt")
	public static final SoundEvent ICEDRAGON_TEEN_HURT = createSoundEvent("icedragon_teen_hurt");

	@GameRegistry.ObjectHolder("icedragon_adult_roar")
	public static final SoundEvent ICEDRAGON_ADULT_ROAR = createSoundEvent("icedragon_adult_roar");

	@GameRegistry.ObjectHolder("icedragon_adult_idle")
	public static final SoundEvent ICEDRAGON_ADULT_IDLE = createSoundEvent("icedragon_adult_idle");

	@GameRegistry.ObjectHolder("icedragon_adult_death")
	public static final SoundEvent ICEDRAGON_ADULT_DEATH = createSoundEvent("icedragon_adult_death");

	@GameRegistry.ObjectHolder("icedragon_adult_hurt")
	public static final SoundEvent ICEDRAGON_ADULT_HURT = createSoundEvent("icedragon_adult_hurt");

	@GameRegistry.ObjectHolder("dragonflute")
	public static final SoundEvent DRAGONFLUTE = createSoundEvent("dragonflute");

	@GameRegistry.ObjectHolder("hippogryph_idle")
	public static final SoundEvent HIPPOGRYPH_IDLE = createSoundEvent("hippogryph_idle");

	@GameRegistry.ObjectHolder("hippogryph_hurt")
	public static final SoundEvent HIPPOGRYPH_HURT = createSoundEvent("hippogryph_hurt");

	@GameRegistry.ObjectHolder("hippogryph_die")
	public static final SoundEvent HIPPOGRYPH_DIE = createSoundEvent("hippogryph_die");

	@GameRegistry.ObjectHolder("gorgon_idle")
	public static final SoundEvent GORGON_IDLE = createSoundEvent("gorgon_idle");

	@GameRegistry.ObjectHolder("gorgon_hurt")
	public static final SoundEvent GORGON_HURT = createSoundEvent("gorgon_hurt");

	@GameRegistry.ObjectHolder("gorgon_die")
	public static final SoundEvent GORGON_DIE = createSoundEvent("gorgon_die");

	@GameRegistry.ObjectHolder("gorgon_attack")
	public static final SoundEvent GORGON_ATTACK = createSoundEvent("gorgon_attack");

	@GameRegistry.ObjectHolder("gorgon_petrify")
	public static final SoundEvent GORGON_PETRIFY = createSoundEvent("gorgon_petrify");

	@GameRegistry.ObjectHolder("gorgon_turn_stone")
	public static final SoundEvent GORGON_TURN_STONE = createSoundEvent("gorgon_turn_stone");

	@GameRegistry.ObjectHolder("pixie_idle")
	public static final SoundEvent PIXIE_IDLE = createSoundEvent("pixie_idle");

	@GameRegistry.ObjectHolder("pixie_hurt")
	public static final SoundEvent PIXIE_HURT = createSoundEvent("pixie_hurt");

	@GameRegistry.ObjectHolder("pixie_die")
	public static final SoundEvent PIXIE_DIE = createSoundEvent("pixie_die");

	@GameRegistry.ObjectHolder("pixie_taunt")
	public static final SoundEvent PIXIE_TAUNT = createSoundEvent("pixie_taunt");

	@GameRegistry.ObjectHolder("gold_pile_step")
	public static final SoundEvent GOLD_PILE_STEP = createSoundEvent("gold_pile_step");

	@GameRegistry.ObjectHolder("gold_pile_break")
	public static final SoundEvent GOLD_PILE_BREAK = createSoundEvent("gold_pile_break");

	private static SoundEvent createSoundEvent(final String soundName) {
		final ResourceLocation soundID = new ResourceLocation(MODID, soundName);
		return new SoundEvent(soundID).setRegistryName(soundID);
	}

	@Mod.EventBusSubscriber(modid = MODID)
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
			event.getRegistry().registerAll(
					DRAGON_HATCH,
					FIREDRAGON_BREATH,
					ICEDRAGON_BREATH,
					FIREDRAGON_CHILD_IDLE,
					FIREDRAGON_CHILD_HURT,
					FIREDRAGON_CHILD_DEATH,
					FIREDRAGON_CHILD_ROAR,
					FIREDRAGON_TEEN_ROAR,
					FIREDRAGON_TEEN_IDLE,
					FIREDRAGON_TEEN_HURT,
					FIREDRAGON_TEEN_DEATH,
					FIREDRAGON_ADULT_ROAR,
					FIREDRAGON_ADULT_IDLE,
					FIREDRAGON_ADULT_HURT,
					FIREDRAGON_ADULT_DEATH,
					ICEDRAGON_CHILD_IDLE,
					ICEDRAGON_CHILD_HURT,
					ICEDRAGON_CHILD_DEATH,
					ICEDRAGON_CHILD_ROAR,
					ICEDRAGON_TEEN_ROAR,
					ICEDRAGON_TEEN_IDLE,
					ICEDRAGON_TEEN_HURT,
					ICEDRAGON_TEEN_DEATH,
					ICEDRAGON_ADULT_ROAR,
					ICEDRAGON_ADULT_IDLE,
					ICEDRAGON_ADULT_HURT,
					ICEDRAGON_ADULT_DEATH,
					DRAGONFLUTE,
					HIPPOGRYPH_DIE,
					HIPPOGRYPH_IDLE,
					HIPPOGRYPH_HURT,
					GORGON_DIE,
					GORGON_IDLE,
					GORGON_HURT,
					GORGON_ATTACK,
					GORGON_TURN_STONE,
					GORGON_PETRIFY,
					PIXIE_DIE,
					PIXIE_HURT,
					PIXIE_IDLE,
					PIXIE_TAUNT,
					GOLD_PILE_STEP,
					GOLD_PILE_BREAK
			);
		}
	}
}
