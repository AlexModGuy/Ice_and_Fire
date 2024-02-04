package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.api.event.DragonFireDamageWorldEvent;
import com.github.alexthe666.iceandfire.block.*;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import com.github.alexthe666.iceandfire.entity.util.BlockLaunchExplosion;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;

public class IafDragonDestructionManager {
    public static void destroyAreaBreath(final Level level, final BlockPos center, final EntityDragonBase dragon) {
        if (MinecraftForge.EVENT_BUS.post(new DragonFireDamageWorldEvent(dragon, center.getX(), center.getY(), center.getZ()))) {
            return;
        }

        int statusDuration;
        float damageScale;

        if (dragon.dragonType == DragonType.FIRE) {
            statusDuration = 5 + dragon.getDragonStage() * 5;
            damageScale = (float) IafConfig.dragonAttackDamageFire;
        } else if (dragon.dragonType == DragonType.ICE) {
            statusDuration = 50 * dragon.getDragonStage();
            damageScale = (float) IafConfig.dragonAttackDamageIce;
        } else if (dragon.dragonType == DragonType.LIGHTNING) {
            statusDuration = 3;
            damageScale = (float) IafConfig.dragonAttackDamageLightning;
        } else {
            return;
        }

        double damageRadius = 3.5;
        boolean canBreakBlocks = ForgeEventFactory.getMobGriefingEvent(level, dragon);

        if (dragon.getDragonStage() <= 3) {
            BlockPos.betweenClosedStream(center.offset(-1, -1, -1), center.offset(1, 1, 1)).forEach(position -> {
                if (level.getBlockEntity(position) instanceof TileEntityDragonforgeInput forge) {
                    forge.onHitWithFlame();
                    return;
                }

                if (canBreakBlocks && DragonUtils.canGrief(dragon) && dragon.getRandom().nextBoolean()) {
                    attackBlock(level, dragon, position);
                }
            });
        } else {
            final int radius = dragon.getDragonStage() == 4 ? 2 : 3;
            final int x = radius + level.random.nextInt(1);
            final int y = radius + level.random.nextInt(1);
            final int z = radius + level.random.nextInt(1);
            final float f = (float) (x + y + z) * 0.333F + 0.5F;
            final float ff = f * f;

            damageRadius = 2.5F + f * 1.2F;

            BlockPos.betweenClosedStream(center.offset(-x, -y, -z), center.offset(x, y, z)).forEach(position -> {
                if (level.getBlockEntity(position) instanceof TileEntityDragonforgeInput forge) {
                    forge.onHitWithFlame();
                    return;
                }

                if (canBreakBlocks && center.distSqr(position) <= ff) {
                    if (DragonUtils.canGrief(dragon) && level.random.nextFloat() > (float) center.distSqr(position) / ff) {
                        attackBlock(level, dragon, position);
                    }
                }
            });
        }

        DamageSource damageSource = getDamageSource(dragon);
        float stageDamage = dragon.getDragonStage() * damageScale;

        level.getEntitiesOfClass(
                LivingEntity.class,
                new AABB(
                        (double) center.getX() - damageRadius,
                        (double) center.getY() - damageRadius,
                        (double) center.getZ() - damageRadius,
                        (double) center.getX() + damageRadius,
                        (double) center.getY() + damageRadius,
                        (double) center.getZ() + damageRadius
                )
        ).forEach(target -> {
            if (!DragonUtils.onSameTeam(dragon, target) && !dragon.is(target) && dragon.hasLineOfSight(target)) {
                target.hurt(damageSource, stageDamage);
                applyDragonEffect(target, dragon, statusDuration);
            }
        });
    }

    public static void destroyAreaCharge(final Level level, final BlockPos center, final EntityDragonBase dragon) {
        if (dragon == null) {
            return;
        }

        if (MinecraftForge.EVENT_BUS.post(new DragonFireDamageWorldEvent(dragon, center.getX(), center.getY(), center.getZ()))) {
            return;
        }

        int x = 2;
        int y = 2;
        int z = 2;

        boolean canBreakBlocks = DragonUtils.canGrief(dragon) && ForgeEventFactory.getMobGriefingEvent(level, dragon);

        if (canBreakBlocks) {
            if (dragon.getDragonStage() <= 3) {
                BlockPos.betweenClosedStream(center.offset(-x, -y, -z), center.offset(x, y, z)).forEach(position -> {
                    BlockState state = level.getBlockState(position);

                    if (state.getBlock() instanceof IDragonProof) {
                        return;
                    }

                    if (dragon.getRandom().nextFloat() * 3 > center.distSqr(position) && DragonUtils.canDragonBreak(state, dragon)) {
                        level.destroyBlock(position, false);
                    }

                    if (dragon.getRandom().nextBoolean()) {
                        attackBlock(level, dragon, position, state);
                    }
                });
            } else {
                final int radius = dragon.getDragonStage() == 4 ? 2 : 3;
                x = radius + level.random.nextInt(2);
                y = radius + level.random.nextInt(2);
                z = radius + level.random.nextInt(2);
                final float f = (float) (x + y + z) * 0.333F + 0.5F;
                final float ff = f * f;

                destroyBlocks(level, center, x, y, z, ff, dragon);

                x++;
                y++;
                z++;

                BlockPos.betweenClosedStream(center.offset(-x, -y, -z), center.offset(x, y, z)).forEach(position -> {
                    if (center.distSqr(position) <= ff) {
                        attackBlock(level, dragon, position);
                    }
                });
            }
        }

        final int statusDuration;

        if (dragon.dragonType == DragonType.FIRE) {
            statusDuration = 15;
        } else if (dragon.dragonType == DragonType.ICE) {
            statusDuration = 400;
        } else if (dragon.dragonType == DragonType.LIGHTNING) {
            statusDuration = 9;
        } else {
            return;
        }

        final float stageDamage = Math.max(1, dragon.getDragonStage() - 1) * 2F;
        DamageSource damageSource = getDamageSource(dragon);

        level.getEntitiesOfClass(
                LivingEntity.class,
                new AABB(
                        (double) center.getX() - x,
                        (double) center.getY() - y,
                        (double) center.getZ() - z,
                        (double) center.getX() + x,
                        (double) center.getY() + y,
                        (double) center.getZ() + z
                )
        ).forEach(target -> {
            if (!dragon.isAlliedTo(target) && !dragon.is(target) && dragon.hasLineOfSight(target)) {
                target.hurt(damageSource, stageDamage);
                applyDragonEffect(target, dragon, statusDuration);
            }
        });

        if (IafConfig.explosiveDragonBreath) {
            causeExplosion(level, center, dragon, damageSource, dragon.getDragonStage());
        }
    }

    private static DamageSource getDamageSource(final EntityDragonBase dragon) {
        Player player = dragon.getRidingPlayer();

        if (dragon.dragonType == DragonType.FIRE) {
            return player != null ? IafDamageRegistry.causeIndirectDragonFireDamage(dragon, player) : IafDamageRegistry.causeDragonFireDamage(dragon);
        } else if (dragon.dragonType == DragonType.ICE) {
            return player != null ? IafDamageRegistry.causeIndirectDragonIceDamage(dragon, player) : IafDamageRegistry.causeDragonIceDamage(dragon);
        } else if (dragon.dragonType == DragonType.LIGHTNING) {
            return player != null ? IafDamageRegistry.causeIndirectDragonLightningDamage(dragon, player) : IafDamageRegistry.causeDragonLightningDamage(dragon);
        } else {
            return dragon.level().damageSources().mobAttack(dragon);
        }
    }

    private static void attackBlock(final Level level, final EntityDragonBase dragon, final BlockPos position, final BlockState state) {
        if (state.getBlock() instanceof IDragonProof || !DragonUtils.canDragonBreak(state, dragon)) {
            return;
        }

        BlockState transformed;

        if (dragon.dragonType == DragonType.FIRE) {
            transformed = transformBlockFire(state);
        } else if (dragon.dragonType == DragonType.ICE) {
            transformed = transformBlockIce(state);
        } else if (dragon.dragonType == DragonType.LIGHTNING) {
            transformed = transformBlockLightning(state);
        } else {
            return;
        }

        if (!transformed.is(state.getBlock())) {
            level.setBlockAndUpdate(position, transformed);
        }

        Block elementalBlock;
        boolean doPlaceBlock;

        if (dragon.dragonType == DragonType.FIRE) {
            elementalBlock = Blocks.FIRE;
            doPlaceBlock = dragon.getRandom().nextBoolean();
        } else if (dragon.dragonType == DragonType.ICE) {
            elementalBlock = IafBlockRegistry.DRAGON_ICE_SPIKES.get();
            doPlaceBlock = dragon.getRandom().nextInt(9) == 0;
        } else {
            return;
        }

        BlockState stateAbove = level.getBlockState(position.above());

        if (doPlaceBlock && transformed.isSolid() && stateAbove.getFluidState().isEmpty() && !stateAbove.canOcclude() && state.canOcclude() && DragonUtils.canDragonBreak(stateAbove, dragon)) {
            level.setBlockAndUpdate(position.above(), elementalBlock.defaultBlockState());
        }
    }

    private static void attackBlock(final Level level, final EntityDragonBase dragon, final BlockPos position) {
        attackBlock(level, dragon, position, level.getBlockState(position));
    }

    private static void applyDragonEffect(final LivingEntity target, final EntityDragonBase dragon, int statusDuration) {
        if (dragon.dragonType == DragonType.FIRE) {
            target.setSecondsOnFire(statusDuration);
        } else if (dragon.dragonType == DragonType.ICE) {
            EntityDataProvider.getCapability(target).ifPresent(data -> data.frozenData.setFrozen(target, statusDuration));
        } else if (dragon.dragonType == DragonType.LIGHTNING) {
            double x = dragon.getX() - target.getX();
            double y = dragon.getZ() - target.getZ();
            target.knockback((double) statusDuration / 10, x, y);
        }
    }

    private static void causeExplosion(Level world, BlockPos center, EntityDragonBase destroyer, DamageSource source, int stage) {
        Explosion.BlockInteraction mode = ForgeEventFactory.getMobGriefingEvent(world, destroyer) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP;
        BlockLaunchExplosion explosion = new BlockLaunchExplosion(world, destroyer, source, center.getX(), center.getY(), center.getZ(), Math.min(2, stage - 2), mode);
        explosion.explode();
        explosion.finalizeExplosion(true);
    }

    private static void destroyBlocks(Level world, BlockPos center, int x, int y, int z, double radius2, Entity destroyer) {
        BlockPos.betweenClosedStream(center.offset(-x, -y, -z), center.offset(x, y, z)).forEach(pos -> {
            if (center.distSqr(pos) <= radius2) {
                BlockState state = world.getBlockState(pos);

                if (state.getBlock() instanceof IDragonProof) {
                    return;
                }

                if (world.random.nextFloat() * 3 > (float) center.distSqr(pos) / radius2 && DragonUtils.canDragonBreak(state, destroyer)) {
                    world.destroyBlock(pos, false);
                }
            }
        });
    }

    public static BlockState transformBlockFire(BlockState in) {
        if (in.getBlock() instanceof SpreadingSnowyDirtBlock) {
            return IafBlockRegistry.CHARRED_GRASS.get().defaultBlockState().setValue(BlockReturningState.REVERTS, true);
        } else if (in.is(Blocks.DIRT)) {
            return IafBlockRegistry.CHARRED_DIRT.get().defaultBlockState().setValue(BlockReturningState.REVERTS, true);
        } else if (in.is(BlockTags.SAND) && in.getBlock() == Blocks.GRAVEL) {
            return IafBlockRegistry.CHARRED_GRAVEL.get().defaultBlockState().setValue(BlockFallingReturningState.REVERTS, true);
        } else if (in.is(BlockTags.BASE_STONE_OVERWORLD) && (in.getBlock() == Blocks.COBBLESTONE || in.getBlock().getDescriptionId().contains("cobblestone"))) {
            return IafBlockRegistry.CHARRED_COBBLESTONE.get().defaultBlockState().setValue(BlockReturningState.REVERTS, true);
        } else if (in.is(BlockTags.BASE_STONE_OVERWORLD) && in.getBlock() != IafBlockRegistry.CHARRED_COBBLESTONE.get()) {
            return IafBlockRegistry.CHARRED_STONE.get().defaultBlockState().setValue(BlockReturningState.REVERTS, true);
        } else if (in.getBlock() == Blocks.DIRT_PATH) {
            return IafBlockRegistry.CHARRED_DIRT_PATH.get().defaultBlockState().setValue(BlockCharedPath.REVERTS, true);
        } else if (in.is(BlockTags.LOGS) || in.is(BlockTags.PLANKS)) {
            return IafBlockRegistry.ASH.get().defaultBlockState();
        } else if (in.is(BlockTags.LEAVES) || in.is(BlockTags.FLOWERS) || in.is(BlockTags.CROPS) || in.getBlock() == Blocks.SNOW) {
            return Blocks.AIR.defaultBlockState();
        }
        return in;
    }

    public static BlockState transformBlockIce(BlockState in) {
        if (in.getBlock() instanceof SpreadingSnowyDirtBlock) {
            return IafBlockRegistry.FROZEN_GRASS.get().defaultBlockState().setValue(BlockReturningState.REVERTS, true);
        } else if (in.is(BlockTags.DIRT) && in.getBlock() == Blocks.DIRT || in.is(BlockTags.SNOW)) {
            return IafBlockRegistry.FROZEN_DIRT.get().defaultBlockState().setValue(BlockReturningState.REVERTS, true);
        } else if (in.is(BlockTags.SAND) && in.getBlock() == Blocks.GRAVEL) {
            return IafBlockRegistry.FROZEN_GRAVEL.get().defaultBlockState().setValue(BlockFallingReturningState.REVERTS, true);
        } else if (in.is(BlockTags.SAND) && in.getBlock() != Blocks.GRAVEL) {
            return in;
        } else if (in.is(BlockTags.BASE_STONE_OVERWORLD) && (in.getBlock() == Blocks.COBBLESTONE || in.getBlock().getDescriptionId().contains("cobblestone"))) {
            return IafBlockRegistry.FROZEN_COBBLESTONE.get().defaultBlockState().setValue(BlockReturningState.REVERTS, true);
        } else if (in.is(BlockTags.BASE_STONE_OVERWORLD) && in.getBlock() != IafBlockRegistry.FROZEN_COBBLESTONE.get()) {
            return IafBlockRegistry.FROZEN_STONE.get().defaultBlockState().setValue(BlockReturningState.REVERTS, true);
        } else if (in.getBlock() == Blocks.DIRT_PATH) {
            return IafBlockRegistry.FROZEN_DIRT_PATH.get().defaultBlockState().setValue(BlockCharedPath.REVERTS, true);
        } else if (in.is(BlockTags.LOGS) || in.is(BlockTags.PLANKS)) {
            return IafBlockRegistry.FROZEN_SPLINTERS.get().defaultBlockState();
        } else if (in.is(Blocks.WATER)) {
            return Blocks.ICE.defaultBlockState();
        } else if (in.is(BlockTags.LEAVES) || in.is(BlockTags.FLOWERS) || in.is(BlockTags.CROPS) || in.getBlock() == Blocks.SNOW) {
            return Blocks.AIR.defaultBlockState();
        }
        return in;
    }

    public static BlockState transformBlockLightning(BlockState in) {
        if (in.getBlock() instanceof SpreadingSnowyDirtBlock) {
            return IafBlockRegistry.CRACKLED_GRASS.get().defaultBlockState().setValue(BlockReturningState.REVERTS, true);
        } else if (in.is(BlockTags.DIRT) && in.getBlock() == Blocks.DIRT) {
            return IafBlockRegistry.CRACKLED_DIRT.get().defaultBlockState().setValue(BlockReturningState.REVERTS, true);
        } else if (in.is(BlockTags.SAND) && in.getBlock() == Blocks.GRAVEL) {
            return IafBlockRegistry.CRACKLED_GRAVEL.get().defaultBlockState().setValue(BlockFallingReturningState.REVERTS, true);
        } else if (in.is(BlockTags.BASE_STONE_OVERWORLD) && (in.getBlock() == Blocks.COBBLESTONE || in.getBlock().getDescriptionId().contains("cobblestone"))) {
            return IafBlockRegistry.CRACKLED_COBBLESTONE.get().defaultBlockState().setValue(BlockReturningState.REVERTS, true);
        } else if (in.is(BlockTags.BASE_STONE_OVERWORLD) && in.getBlock() != IafBlockRegistry.CRACKLED_COBBLESTONE.get()) {
            return IafBlockRegistry.CRACKLED_STONE.get().defaultBlockState().setValue(BlockReturningState.REVERTS, true);
        } else if (in.getBlock() == Blocks.DIRT_PATH) {
            return IafBlockRegistry.CRACKLED_DIRT_PATH.get().defaultBlockState().setValue(BlockCharedPath.REVERTS, true);
        } else if (in.is(BlockTags.LOGS) || in.is(BlockTags.PLANKS)) {
            return IafBlockRegistry.ASH.get().defaultBlockState();
        } else if (in.is(BlockTags.LEAVES) || in.is(BlockTags.FLOWERS) || in.is(BlockTags.CROPS) || in.getBlock() == Blocks.SNOW) {
            return Blocks.AIR.defaultBlockState();
        }
        return in;
    }
}
