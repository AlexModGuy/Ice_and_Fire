package com.github.alexthe666.iceandfire.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientRegistry;

public class IafKeybindRegistry {
    public static KeyMapping dragon_fireAttack;
    public static KeyMapping dragon_strike;
    public static KeyMapping dragon_down;
    public static KeyMapping dragon_change_view;

    public static void init() {
        // Minecraft instance is null during data gen
        if (Minecraft.getInstance() == null)
            return;
        dragon_fireAttack = new KeyMapping("key.dragon_fireAttack", 82, "key.categories.gameplay");
        dragon_strike = new KeyMapping("key.dragon_strike", 71, "key.categories.gameplay");
        dragon_down = new KeyMapping("key.dragon_down", 88, "key.categories.gameplay");
        dragon_change_view = new KeyMapping("key.dragon_change_view", 296, "key.categories.misc");
        ClientRegistry.registerKeyBinding(dragon_fireAttack);
        ClientRegistry.registerKeyBinding(dragon_strike);
        ClientRegistry.registerKeyBinding(dragon_down);
        ClientRegistry.registerKeyBinding(dragon_change_view);
    }
}
