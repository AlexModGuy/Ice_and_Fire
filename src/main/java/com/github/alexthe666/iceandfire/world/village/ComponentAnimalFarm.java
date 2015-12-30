package com.github.alexthe666.iceandfire.world.village;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class ComponentAnimalFarm extends StructureVillagePieces.Village{

	@Override
	public boolean addComponentParts(World worldIn, Random rand, StructureBoundingBox bb) {
		return false;
	}


}
