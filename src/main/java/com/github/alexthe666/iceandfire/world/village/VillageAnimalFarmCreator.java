package com.github.alexthe666.iceandfire.world.village;

import net.minecraft.util.Direction;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;
import java.util.Random;

public class VillageAnimalFarmCreator implements VillagerRegistry.IVillageCreationHandler {

    @Override
    public StructureVillagePieces.PieceWeight getVillagePieceWeight(Random random, int i) {
        return new StructureVillagePieces.PieceWeight(ComponentAnimalFarm.class, 50, i + random.nextInt(1));
    }

    @Override
    public Class<?> getComponentClass() {
        return ComponentAnimalFarm.class;
    }

    @Override
    public StructureVillagePieces.Village buildComponent(StructureVillagePieces.PieceWeight villagePiece, StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int p1, int p2, int p3, Direction facing, int p5) {
        return ComponentAnimalFarm.buildComponent(startPiece, pieces, random, p1, p2, p3, facing, p5);
    }

}
