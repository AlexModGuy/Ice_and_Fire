package com.github.alexthe666.iceandfire.core;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.lwjgl.input.Keyboard;

public class ModKeys {
	public static KeyBinding dragon_up;
	public static KeyBinding dragon_down;
	public static KeyBinding dragon_fireAttack;
	public static KeyBinding dragon_strike;

	public static void init() {
		dragon_up = new KeyBinding("key.dragon_up", Keyboard.KEY_F, "key.categories.gameplay");
		dragon_down = new KeyBinding("key.dragon_down", Keyboard.KEY_G, "key.categories.gameplay");
		dragon_fireAttack = new KeyBinding("key.dragon_fireAttack", Keyboard.KEY_C, "key.categories.gameplay");
		dragon_strike = new KeyBinding("key.dragon_strike", Keyboard.KEY_R, "key.categories.gameplay");
		ClientRegistry.registerKeyBinding(dragon_up);
		ClientRegistry.registerKeyBinding(dragon_down);
		ClientRegistry.registerKeyBinding(dragon_fireAttack);
		ClientRegistry.registerKeyBinding(dragon_strike);

	}
}
