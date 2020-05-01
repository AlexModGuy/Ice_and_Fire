package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockReturningState extends Block {
    public static final PropertyBool REVERTS = PropertyBool.create("revert");
    public Item itemBlock;
    private BlockState returnState;

    public BlockReturningState(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, BlockState returnToState) {
        super(materialIn);
        this.setTranslationKey(name);
        this.setHarvestLevel(toolUsed, toolStrength);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(sound);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        setRegistryName(IceAndFire.MODID, gameName);
        this.returnState = returnToState;
        this.setDefaultState(this.blockState.getBaseState().withProperty(REVERTS, Boolean.valueOf(false)));
        this.setTickRandomly(true);
    }

    @SuppressWarnings("deprecation")
    public BlockReturningState(Material materialIn, String gameName, String name, String toolUsed, int toolStrength, float hardness, float resistance, SoundType sound, boolean slippery, BlockState returnToState) {
        super(materialIn);
        this.setTranslationKey(name);
        this.setHarvestLevel(toolUsed, toolStrength);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(sound);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        if (slippery) {
            this.slipperiness = 0.98F;
        }
        setRegistryName(IceAndFire.MODID, gameName);
        this.returnState = returnToState;
        this.setDefaultState(this.blockState.getBaseState().withProperty(REVERTS, Boolean.valueOf(false)));
        this.setTickRandomly(true);
    }

    public BlockReturningState(Material materialIn, String gameName, String name, float hardness, float resistance, SoundType sound, BlockState returnToState) {
        super(materialIn);
        this.setTranslationKey(name);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setSoundType(sound);
        this.setCreativeTab(IceAndFire.TAB_ITEMS);
        setRegistryName(IceAndFire.MODID, gameName);
        this.returnState = returnToState;
        this.setDefaultState(this.blockState.getBaseState().withProperty(REVERTS, Boolean.valueOf(false)));
        this.setTickRandomly(true);
    }

    public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return;
            if (state.getValue(REVERTS) && rand.nextInt(3) == 0) {
                worldIn.setBlockState(pos, returnState);
            }
        }
    }

    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(REVERTS, meta == 1);
    }

    public int getMetaFromState(BlockState state) {
        return state.getValue(REVERTS) ? 1 : 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, REVERTS);
    }

}
