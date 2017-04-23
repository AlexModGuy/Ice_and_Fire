package com.github.alexthe666.iceandfire.world.village;

import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureVillagePieces.*;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

import java.util.*;

public class IAFVillage implements IVillageCreationHandler {

    @Override
    public PieceWeight getVillagePieceWeight(Random random, int i) {
        return null;
    }

    @Override
    public Class<?> getComponentClass() {
        return null;
    }

    @Override
    public Village buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5) {
        return null;
    }

}
