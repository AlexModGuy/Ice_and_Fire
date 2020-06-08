package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IceAndFire.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IafContainerRegistry {

    public static final ContainerType IAF_LECTERN_CONTAINER = register(new ContainerType(ContainerLectern::new), "iaf_lectern");
    public static final ContainerType PODIUM_CONTAINER = register(new ContainerType(ContainerPodium::new), "podium");
    public static final ContainerType DRAGON_CONTAINER = register(new ContainerType(ContainerDragon::new), "dragon");
    public static final ContainerType HIPPOGRYPH_CONTAINER = register(new ContainerType(ContainerHippogryph::new), "hippogryph");
    public static final ContainerType HIPPOCAMPUS_CONTAINER = register(new ContainerType(ContainerHippocampus::new), "hippocampus");
    public static final ContainerType MYRMEX_CACOON_CONTAINER = register(new ContainerType(ContainerMyrmexCocoon::new), "myrmex_cacoon");
    public static final ContainerType DRAGON_FORGE_CONTAINER = register(new ContainerType(ContainerDragonForge::new), "dragon_forge");

    public static ContainerType register(ContainerType type, String name) {
        type.setRegistryName(name);
        return type;
    }
}
