package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class IafContainerRegistry {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister
        .create(ForgeRegistries.CONTAINERS, IceAndFire.MODID);

    public static final RegistryObject<MenuType<ContainerLectern>> IAF_LECTERN_CONTAINER = register(
        () -> new MenuType<>(ContainerLectern::new), "iaf_lectern");
    public static final RegistryObject<MenuType<ContainerPodium>> PODIUM_CONTAINER = register(
        () -> new MenuType<>(ContainerPodium::new), "podium");
    public static final RegistryObject<MenuType<ContainerDragon>> DRAGON_CONTAINER = register(
        () -> new MenuType<>(ContainerDragon::new), "dragon");
    public static final RegistryObject<MenuType<ContainerHippogryph>> HIPPOGRYPH_CONTAINER = register(
        () -> new MenuType<>(ContainerHippogryph::new), "hippogryph");
    public static final RegistryObject<MenuType<ContainerHippocampus>> HIPPOCAMPUS_CONTAINER = register(
        () -> new MenuType<>(ContainerHippocampus::new), "hippocampus");
    public static final RegistryObject<MenuType<ContainerDragonForge>> DRAGON_FORGE_CONTAINER = register(
        () -> new MenuType<>(ContainerDragonForge::new), "dragon_forge");

    public static <C extends AbstractContainerMenu> RegistryObject<MenuType<C>> register(Supplier<MenuType<C>> type,
                                                                                         String name) {
        return CONTAINERS.register(name, type);
    }

}
