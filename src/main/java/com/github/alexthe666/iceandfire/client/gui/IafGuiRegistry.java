package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.inventory.IafContainerRegistry;
import net.minecraft.client.gui.screens.MenuScreens;

public class IafGuiRegistry {

    public static void register() {
        MenuScreens.register(IafContainerRegistry.IAF_LECTERN_CONTAINER.get(), GuiLectern::new);
        MenuScreens.register(IafContainerRegistry.PODIUM_CONTAINER.get(), GuiPodium::new);
        MenuScreens.register(IafContainerRegistry.DRAGON_CONTAINER.get(), GuiDragon::new);
        MenuScreens.register(IafContainerRegistry.HIPPOGRYPH_CONTAINER.get(), GuiHippogryph::new);
        MenuScreens.register(IafContainerRegistry.HIPPOCAMPUS_CONTAINER.get(), GuiHippocampus::new);
        MenuScreens.register(IafContainerRegistry.DRAGON_FORGE_CONTAINER.get(), GuiDragonForge::new);
    }
}
