package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.SoundType;
import net.minecraft.block.WallBlock;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BlockDragonBoneWall extends WallBlock implements IDragonProof {

    public BlockDragonBoneWall(Block.Properties props) {
        super(props);
        this.setHardness(20F);
        this.setResistance(500F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(IceAndFire.TAB_BLOCKS);
        this.setTranslationKey("iceandfire.dragon_bone_wall");
        this.setRegistryName(IceAndFire.MODID, "dragon_bone_wall");
    }

    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this));
    }

}
