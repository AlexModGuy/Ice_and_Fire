package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.block.BlockGhostChest;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafProcessors;
import com.mojang.serialization.Codec;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;

import javax.annotation.Nullable;
import java.util.Random;

public class GorgonTempleProcessor extends StructureProcessor {

    private float integrity = 1.0F;
    public static final GorgonTempleProcessor INSTANCE = new GorgonTempleProcessor();
    public static final Codec<GorgonTempleProcessor> CODEC = Codec.unit(() -> INSTANCE);
    public GorgonTempleProcessor() {
    }


    public Template.BlockInfo process(IWorldReader worldReader, BlockPos pos, BlockPos pos2, Template.BlockInfo infoIn1, Template.BlockInfo infoIn2, PlacementSettings settings,@Nullable Template template) {
        if(infoIn2.state.getBlock() instanceof StairsBlock) {
            return new Template.BlockInfo(infoIn2.pos, infoIn2.state.with(StairsBlock.WATERLOGGED, false), null);
        }else if(infoIn2.state.getBlock() instanceof WallBlock) {
            return new Template.BlockInfo(infoIn2.pos, infoIn2.state.with(WallBlock.WATERLOGGED, false), null);
        }else if(infoIn2.state.getBlock() instanceof IWaterLoggable){
            return new Template.BlockInfo(infoIn2.pos, infoIn2.state.with(BlockStateProperties.WATERLOGGED, false), null);
        }else{
            BlockPos blockpos = infoIn2.pos;
            boolean flag = worldReader.getBlockState(blockpos).isIn(Blocks.WATER);
            return flag && !Block.isOpaque(infoIn2.state.getShape(worldReader, blockpos)) ? new Template.BlockInfo(blockpos, Blocks.AIR.getDefaultState(), infoIn2.nbt) : infoIn2;
        }
    }


    @Override
    protected IStructureProcessorType getType() {
        return IafProcessors.GORGONTEMPLEPROCESSOR;
    }

}
