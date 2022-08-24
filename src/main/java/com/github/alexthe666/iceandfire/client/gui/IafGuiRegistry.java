package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.inventory.IafContainerRegistry;
import net.minecraft.client.gui.ScreenManager;

public class IafGuiRegistry {

    public static void register() {
        ScreenManager.register(IafContainerRegistry.IAF_LECTERN_CONTAINER.get(), GuiLectern::new);
        ScreenManager.register(IafContainerRegistry.PODIUM_CONTAINER.get(), GuiPodium::new);
        ScreenManager.register(IafContainerRegistry.DRAGON_CONTAINER.get(), GuiDragon::new);
        ScreenManager.register(IafContainerRegistry.HIPPOGRYPH_CONTAINER.get(), GuiHippogryph::new);
        ScreenManager.register(IafContainerRegistry.HIPPOCAMPUS_CONTAINER.get(), GuiHippocampus::new);
        ScreenManager.register(IafContainerRegistry.DRAGON_FORGE_CONTAINER.get(), GuiDragonForge::new);
    }
}
