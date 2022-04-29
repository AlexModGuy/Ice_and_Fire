package com.github.alexthe666.iceandfire.inventory;

import java.util.function.Supplier;

import com.github.alexthe666.iceandfire.IceAndFire;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class IafContainerRegistry {

    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister
        .create(ForgeRegistries.CONTAINERS, IceAndFire.MODID);

    public static final RegistryObject<ContainerType<ContainerLectern>> IAF_LECTERN_CONTAINER = register(
        () -> new ContainerType<>(ContainerLectern::new), "iaf_lectern");
    public static final RegistryObject<ContainerType<ContainerPodium>> PODIUM_CONTAINER = register(
        () -> new ContainerType<>(ContainerPodium::new), "podium");
    public static final RegistryObject<ContainerType<ContainerDragon>> DRAGON_CONTAINER = register(
        () -> new ContainerType<>(ContainerDragon::new), "dragon");
    public static final RegistryObject<ContainerType<ContainerHippogryph>> HIPPOGRYPH_CONTAINER = register(
        () -> new ContainerType<>(ContainerHippogryph::new), "hippogryph");
    public static final RegistryObject<ContainerType<ContainerHippocampus>> HIPPOCAMPUS_CONTAINER = register(
        () -> new ContainerType<>(ContainerHippocampus::new), "hippocampus");
    public static final RegistryObject<ContainerType<ContainerDragonForge>> DRAGON_FORGE_CONTAINER = register(
        () -> new ContainerType<>(ContainerDragonForge::new), "dragon_forge");

    public static <C extends Container> RegistryObject<ContainerType<C>> register(Supplier<ContainerType<C>> type,
        String name) {
        return CONTAINERS.register(name, type);
    }

}
