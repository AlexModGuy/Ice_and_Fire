package com.github.alexthe666.iceandfire.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class IafKeybindRegistry {
    public static KeyBinding dragon_fireAttack;
    public static KeyBinding dragon_strike;
    public static KeyBinding dragon_down;
    public static KeyBinding dragon_change_view;

    public static void init() {
        dragon_fireAttack = new KeyBinding("key.dragon_fireAttack", 82, "key.categories.gameplay");
        dragon_strike = new KeyBinding("key.dragon_strike", 71, "key.categories.gameplay");
        dragon_down = new KeyBinding("key.dragon_down", 88, "key.categories.gameplay");
        dragon_change_view = new KeyBinding("key.dragon_change_view", 296, "key.categories.misc");
        ClientRegistry.registerKeyBinding(dragon_fireAttack);
        ClientRegistry.registerKeyBinding(dragon_strike);
        ClientRegistry.registerKeyBinding(dragon_down);
        ClientRegistry.registerKeyBinding(dragon_change_view);
    }
}
