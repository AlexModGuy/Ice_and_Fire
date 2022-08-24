package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class BlockReturningState extends Block {
    public static final BooleanProperty REVERTS = BooleanProperty.create("revert");
    public Item itemBlock;
    private final BlockState returnState;

    public BlockReturningState(Material materialIn, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, BlockState returnToState) {
        super(
            AbstractBlock.Properties
                .of(materialIn)
                .sound(sound)
                .strength(hardness, resistance)
                .harvestTool(ToolType.get(toolUsed))
                .harvestLevel(toolStrength)
                .randomTicks()
		);

        setRegistryName(IceAndFire.MODID, name);
        this.returnState = returnToState;
        this.registerDefaultState(this.stateDefinition.any().setValue(REVERTS, Boolean.valueOf(false)));
    }

    @SuppressWarnings("deprecation")
    public BlockReturningState(Material materialIn, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, boolean slippery, BlockState returnToState) {
        super(AbstractBlock.Properties.of(materialIn).sound(sound).strength(hardness, resistance).harvestTool(ToolType.get(toolUsed)).harvestLevel(toolStrength).friction(0.98F).randomTicks());
        setRegistryName(IceAndFire.MODID, name);
        this.returnState = returnToState;
        this.registerDefaultState(this.stateDefinition.any().setValue(REVERTS, Boolean.valueOf(false)));
    }

    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isClientSide) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return;
            if (state.getValue(REVERTS) && rand.nextInt(3) == 0) {
                worldIn.setBlockAndUpdate(pos, returnState);
            }
        }
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(REVERTS);
    }
}
