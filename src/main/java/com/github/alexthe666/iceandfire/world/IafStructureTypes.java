package com.github.alexthe666.iceandfire.world;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.world.structure.GorgonTempleStructure;
import com.github.alexthe666.iceandfire.world.structure.GraveyardStructure;
import com.github.alexthe666.iceandfire.world.structure.MausoleumStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class IafStructureTypes {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, IceAndFire.MODID);

    public static final RegistryObject<StructureType<GraveyardStructure>> GRAVEYARD = registerType("graveyard", () -> () -> GraveyardStructure.CODEC);
    public static final RegistryObject<StructureType<MausoleumStructure>> MAUSOLEUM = registerType("mausoleum", () -> () -> MausoleumStructure.CODEC);
    public static final RegistryObject<StructureType<GorgonTempleStructure>> GORGON_TEMPLE = registerType("gorgon_temple", () -> () -> GorgonTempleStructure.CODEC);
    private static <P extends Structure> RegistryObject<StructureType<P>> registerType(String name, Supplier<StructureType<P>> factory) {
        return STRUCTURE_TYPES.register(name, factory);
    }
}
