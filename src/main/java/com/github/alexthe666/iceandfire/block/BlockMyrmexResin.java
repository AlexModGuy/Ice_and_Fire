package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.sun.jna.WString;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class BlockMyrmexResin extends Block {

    private final boolean sticky;

    public BlockMyrmexResin(boolean sticky) {
        super(
            Properties
                .of(Material.CLAY)
                .strength(2.5F)
                .sound(sticky ? SoundType.SLIME_BLOCK : SoundType.GRAVEL)
        );

        this.sticky = sticky;
    }

    static String name(boolean sticky, String suffix) {
        if (sticky) {
            return "myrmex_resin_sticky_%s".formatted(suffix);
        }
        return "myrmex_resin_%s".formatted(suffix);
    }

    @Deprecated
    public boolean canEntitySpawn(BlockState state, BlockGetter worldIn, BlockPos pos, EntityType<?> type) {
        return false;
    }

    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entity) {
        if (sticky) {
            if (!(entity instanceof EntityMyrmexBase)) {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.4D, 0.4D, 0.4D));
            }

        }
    }
}
