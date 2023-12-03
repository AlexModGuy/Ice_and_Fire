package com.github.alexthe666.iceandfire.entity.util;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class BlockLaunchExplosion extends Explosion {
    private final float size;
    private final Level world;
    private final double x;
    private final double y;
    private final double z;
    private final BlockInteraction mode;

    public BlockLaunchExplosion(Level world, Mob entity, double x, double y, double z, float size) {
        this(world, entity, x, y, z, size, BlockInteraction.DESTROY);
    }

    public BlockLaunchExplosion(Level world, Mob entity, double x, double y, double z, float size, BlockInteraction mode) {
        this(world, entity, null, x, y, z, size, mode);
    }

    public BlockLaunchExplosion(Level world, Mob entity, DamageSource source, double x, double y, double z, float size, BlockInteraction mode) {
        super(world, entity, source, null, x, y, z, size, false, mode);
        this.world = world;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.mode = mode;
    }

    private static void handleExplosionDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> dropPositionArray, ItemStack stack, BlockPos pos) {
        int i = dropPositionArray.size();

        for (int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPos> pair = dropPositionArray.get(j);
            ItemStack itemstack = pair.getFirst();
            if (ItemEntity.areMergable(itemstack, stack)) {
                ItemStack itemstack1 = ItemEntity.merge(itemstack, stack, 16);
                dropPositionArray.set(j, Pair.of(itemstack1, pair.getSecond()));
                if (stack.isEmpty()) {
                    return;
                }
            }
        }

        dropPositionArray.add(Pair.of(stack, pos));
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    @Override
    public void finalizeExplosion(boolean spawnParticles) {
        if (world.isClientSide) {
            this.world.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F, false);
        }

        boolean flag = this.mode != BlockInteraction.KEEP;
        if (spawnParticles) {
            if (!(this.size < 2.0F) && flag) {
                this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            } else {
                this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0D, 0.0D, 0.0D);
            }
        }

        if (flag) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList<>();
            Collections.shuffle(this.getToBlow(), ThreadLocalRandom.current());

            for (BlockPos blockpos : this.getToBlow()) {
                BlockState blockstate = this.world.getBlockState(blockpos);
                if (!blockstate.isAir()) {
                    BlockPos blockpos1 = blockpos.immutable();
                    this.world.getProfiler().push("explosion_blocks");

                    Vec3 Vector3d = new Vec3(this.x, this.y, this.z);
                    blockstate.onBlockExploded(this.world, blockpos, this);
                    FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, world);
                    fallingBlockEntity.setStartPos(blockpos1);
                    fallingBlockEntity.setPos(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D, blockpos1.getZ() + 0.5D);
                    double d5 = fallingBlockEntity.getX() - this.x;
                    double d7 = fallingBlockEntity.getEyeY() - this.y;
                    double d9 = fallingBlockEntity.getZ() - this.z;
                    float f3 = this.size * 2.0F;
                    double d12 = Math.sqrt(fallingBlockEntity.distanceToSqr(Vector3d)) / f3;
                    double d14 = getSeenPercent(Vector3d, fallingBlockEntity);
                    double d11 = (1.0D - d12) * d14;
                    fallingBlockEntity.setDeltaMovement(fallingBlockEntity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
                    this.world.getProfiler().pop();
                }
            }

            for (Pair<ItemStack, BlockPos> pair : objectarraylist) {
                Block.popResource(this.world, pair.getSecond(), pair.getFirst());
            }
        }
    }

}