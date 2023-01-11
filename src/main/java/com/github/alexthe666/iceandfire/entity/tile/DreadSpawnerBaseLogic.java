package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

// This class only servers the point of changing the particles spawned
public abstract class DreadSpawnerBaseLogic extends BaseSpawner {

    private short spawnDelay = 20;
    private double spin;
    private double oSpin;

    @Override
    public void clientTick(@NotNull Level p_151320_, @NotNull BlockPos p_151321_) {
        if (!this.isNearPlayer(p_151320_, p_151321_)) {
            this.oSpin = this.spin;
        } else {
            double d0 = (double) p_151321_.getX() + p_151320_.random.nextDouble();
            double d1 = (double) p_151321_.getY() + p_151320_.random.nextDouble();
            double d2 = (double) p_151321_.getZ() + p_151320_.random.nextDouble();
            p_151320_.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            if (this.spawnDelay > 0) {
                --this.spawnDelay;
            }

            this.oSpin = this.spin;
            this.spin = (this.spin + (double) (1000.0F / ((float) this.spawnDelay + 200.0F))) % 360.0D;
        }

    }

    private boolean isNearPlayer(Level p_151344_, BlockPos p_151345_) {
        return p_151344_.hasNearbyAlivePlayer((double) p_151345_.getX() + 0.5D, (double) p_151345_.getY() + 0.5D, (double) p_151345_.getZ() + 0.5D, 20);
    }

    @Override
    public double getSpin() {
        return spin;
    }

    @Override
    public double getoSpin() {
        return oSpin;
    }
}
