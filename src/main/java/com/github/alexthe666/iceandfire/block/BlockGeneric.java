package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.EntityDreadMob;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class BlockGeneric extends Block {
/*    public BlockGeneric(float hardness, float resistance, SoundType sound) {
        super(
            BlockBehaviour.Properties
                .of()
                .sound(sound)
                .strength(hardness, resistance)
                .requiresCorrectToolForDrops()
        );
    }

    public BlockGeneric(float hardness, float resistance, SoundType sound, boolean slippery) {
        super(
            BlockBehaviour.Properties
                .of()
                .sound(sound)
                .strength(hardness, resistance)
                .friction(0.98F)
        );
    }*/

    public static BlockGeneric builder(float hardness, float resistance, SoundType sound, MapColor color, NoteBlockInstrument instrument, PushReaction reaction, boolean ignited) {
        BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                .mapColor(color)
                .sound(sound)
                .strength(hardness, resistance)
                .requiresCorrectToolForDrops();
        if (instrument != null) {
            props.instrument(instrument);
        }
        if (reaction != null) {
            props.pushReaction(reaction);
        }
        if (ignited) {
            props.ignitedByLava();
        }
        return new BlockGeneric(props);
    }

    public static BlockGeneric builder(float hardness, float resistance, SoundType sound, boolean slippery, MapColor color, NoteBlockInstrument instrument, PushReaction reaction, boolean ignited) {
        BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                .mapColor(color)
                .sound(sound)
                .strength(hardness, resistance)
                .friction(0.98F);
        if (instrument != null) {
            props.instrument(instrument);
        }
        if (reaction != null) {
            props.pushReaction(reaction);
        }
        if (ignited) {
            props.ignitedByLava();
        }
        return new BlockGeneric(props);
    }

    public BlockGeneric(BlockBehaviour.Properties props) {
        super(props);
    }

    public boolean isOpaqueCube(BlockState state) {
        return this != IafBlockRegistry.DRAGON_ICE.get();
    }

    public boolean isFullCube(BlockState state) {
        return this != IafBlockRegistry.DRAGON_ICE.get();
    }

    @Deprecated
    public boolean canEntitySpawn(BlockState state, Entity entityIn) {
        return entityIn instanceof EntityDreadMob || !DragonUtils.isDreadBlock(state);
    }

}
