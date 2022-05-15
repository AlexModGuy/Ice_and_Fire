package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.inventory.IafContainerRegistry;
import net.minecraft.client.gui.ScreenManager;

public class IafGuiRegistry {

    public static void register() {
        ScreenManager.registerFactory(IafContainerRegistry.IAF_LECTERN_CONTAINER.get(), GuiLectern::new);
        ScreenManager.registerFactory(IafContainerRegistry.PODIUM_CONTAINER.get(), GuiPodium::new);
        ScreenManager.registerFactory(IafContainerRegistry.DRAGON_CONTAINER.get(), GuiDragon::new);
        ScreenManager.registerFactory(IafContainerRegistry.HIPPOGRYPH_CONTAINER.get(), GuiHippogryph::new);
        ScreenManager.registerFactory(IafContainerRegistry.HIPPOCAMPUS_CONTAINER.get(), GuiHippocampus::new);
        ScreenManager.registerFactory(IafContainerRegistry.DRAGON_FORGE_CONTAINER.get(), GuiDragonForge::new);
    }
}
