package com.github.alexthe666.iceandfire.world.village;

import com.github.alexthe666.iceandfire.world.WorldGenAnimalFarm;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.util.List;
import java.util.Random;

public class ComponentAnimalFarm extends StructureVillagePieces.Village {

    public ComponentAnimalFarm() {
        super();
    }

    public ComponentAnimalFarm(StructureVillagePieces.Start startPiece, int p2, Random random, StructureBoundingBox structureBox, EnumFacing facing) {
        super();
        this.setCoordBaseMode(facing);
        this.boundingBox = structureBox;
    }

    public static ComponentAnimalFarm buildComponent(StructureVillagePieces.Start startPiece, List<StructureComponent> pieces, Random random, int x, int y, int z, EnumFacing facing, int p5) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 8, 7, 8, facing);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new ComponentAnimalFarm(startPiece, p5, random, structureboundingbox, facing) : null;
    }

    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb) {
        if (this.averageGroundLvl < 0) {
            this.averageGroundLvl = this.getAverageGroundLevel(world, sbb);

            if (this.averageGroundLvl < 0) {
                return true;
            }
            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 7 - 1, 0);
        }
        BlockPos blockpos = new BlockPos(this.getXWithOffset(4, 4), this.getYWithOffset(0), this.getZWithOffset(4, 4));
        return new WorldGenAnimalFarm().generate(world, random, blockpos.up());
    }

}
