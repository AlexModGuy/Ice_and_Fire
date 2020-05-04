package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadPortal;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDreadSpawner;
import net.minecraft.block.BlockMobSpawner;
import net.minecraft.block.SoundType;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockDreadSpawner extends SpawnerBlock implements IDreadBlock {

    public BlockDreadSpawner() {
        super();
        this.setHardness(9.0F);
        this.setResistance(10000F);
        this.setTranslationKey("iceandfire.dread_spawner");
        this.setSoundType(SoundType.METAL);
        this.setRegistryName(IceAndFire.MODID, "dread_spawner");
        GameRegistry.registerTileEntity(TileEntityDreadSpawner.class, "dread_spawner");
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityDreadSpawner();
    }

}
