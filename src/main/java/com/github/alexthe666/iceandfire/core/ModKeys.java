package com.github.alexthe666.iceandfire.core;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import org.lwjgl.input.Keyboard;

public class ModKeys {
	public static KeyBinding dragon_fireAttack;
	public static KeyBinding dragon_strike;
	public static KeyBinding dragon_dismount;

	public static void init() {
		dragon_fireAttack = new KeyBinding("key.dragon_fireAttack", Keyboard.KEY_R, "key.categories.gameplay");
		dragon_strike = new KeyBinding("key.dragon_strike", Keyboard.KEY_G, "key.categories.gameplay");
		dragon_dismount = new KeyBinding("key.dragon_dismount", Keyboard.KEY_X, "key.categories.gameplay");
		ClientRegistry.registerKeyBinding(dragon_fireAttack);
		ClientRegistry.registerKeyBinding(dragon_strike);
		ClientRegistry.registerKeyBinding(dragon_dismount);

	}
}
