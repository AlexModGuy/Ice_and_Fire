package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.api.event.DragonFireDamageWorldEvent;
import com.github.alexthe666.iceandfire.block.*;
import com.github.alexthe666.iceandfire.entity.props.FrozenProperties;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import com.github.alexthe666.iceandfire.entity.util.BlockLaunchExplosion;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpreadableSnowyDirtBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;

// TODO: This can be refactored/simplified a lot more

public class IafDragonDestructionManager {

    public static void destroyAreaFire(World world, BlockPos center, EntityDragonBase destroyer) {
        if (MinecraftForge.EVENT_BUS.post(new DragonFireDamageWorldEvent(destroyer, center.getX(), center.getY(), center.getZ())))
            return;
        Entity cause = destroyer.getRidingPlayer() != null ? destroyer.getRidingPlayer() : destroyer;
        DamageSource source = IafDamageRegistry.causeDragonFireDamage(cause);
        int stage = destroyer.getDragonStage();
        double damageRadius = 3.5D;
        float dmgScale = (float) IafConfig.dragonAttackDamageFire;

        if (stage <= 3) {
            BlockPos.getAllInBox(center.add(-1, -1, -1), center.add(1, 1, 1)).forEach(pos -> {
                if (world.getTileEntity(pos) instanceof TileEntityDragonforgeInput) {
                    ((TileEntityDragonforgeInput) world.getTileEntity(pos)).onHitWithFlame();
                    return;
                }
                if (IafConfig.dragonGriefing != 2 && world.rand.nextBoolean()) {
                    fireAttackBlock(world, pos);
                }
            });
        } else {
            final int radius = stage == 4 ? 2 : 3;
            final int j = radius + world.rand.nextInt(1);
            final int k = radius + world.rand.nextInt(1);
            final int l = radius + world.rand.nextInt(1);
            final float f = (float) (j + k + l) * 0.333F + 0.5F;
            final float ff = f * f;

            damageRadius = 2.5F + f * 1.2F;
            BlockPos.getAllInBox(center.add(-j, -k, -l), center.add(j, k, l)).forEach(pos -> {
                if (world.getTileEntity(pos) instanceof TileEntityDragonforgeInput) {
                    ((TileEntityDragonforgeInput) world.getTileEntity(pos)).onHitWithFlame();
                    return;
                }
                if (center.distanceSq(pos) <= ff) {
                    if (IafConfig.dragonGriefing != 2 && world.rand.nextFloat() > (float) center.distanceSq(pos) / ff) {
                        fireAttackBlock(world, pos);
                    }
                }
            });
        }

        final float stageDmg = stage * dmgScale;
        final int statusDuration = 5 + stage * 5;
        world.getEntitiesWithinAABB(
            LivingEntity.class,
            new AxisAlignedBB(
                (double) center.getX() - damageRadius,
                (double) center.getY() - damageRadius,
                (double) center.getZ() - damageRadius,
                (double) center.getX() + damageRadius,
                (double) center.getY() + damageRadius,
                (double) center.getZ() + damageRadius
            )
        ).stream().forEach(livingEntity -> {
            if (!DragonUtils.onSameTeam(destroyer, livingEntity) && !destroyer.isEntityEqual(livingEntity) && destroyer.canEntityBeSeen(livingEntity)) {
                livingEntity.attackEntityFrom(source, stageDmg);
                livingEntity.setFire(statusDuration);
            }
        });
    }

    public static void destroyAreaIce(World world, BlockPos center, EntityDragonBase destroyer) {
        if (MinecraftForge.EVENT_BUS.post(new DragonFireDamageWorldEvent(destroyer, center.getX(), center.getY(), center.getZ())))
            return;
        Entity cause = destroyer.getRidingPlayer() != null ? destroyer.getRidingPlayer() : destroyer;
        DamageSource source = IafDamageRegistry.causeDragonIceDamage(cause);
        int stage = destroyer.getDragonStage();
        double damageRadius = 3.5D;
        float dmgScale = (float) IafConfig.dragonAttackDamageIce;

        if (stage <= 3) {
            BlockPos.getAllInBox(center.add(-1, -1, -1), center.add(1, 1, 1)).forEach(pos -> {
                if (world.getTileEntity(pos) instanceof TileEntityDragonforgeInput) {
                    ((TileEntityDragonforgeInput) world.getTileEntity(pos)).onHitWithFlame();
                    return;
                }
                if (IafConfig.dragonGriefing != 2 && world.rand.nextBoolean()) {
                    iceAttackBlock(world, pos);
                }
            });
        } else {
            final int radius = stage == 4 ? 2 : 3;
            final int j = radius + world.rand.nextInt(1);
            final int k = radius + world.rand.nextInt(1);
            final int l = radius + world.rand.nextInt(1);
            final float f = (float) (j + k + l) * 0.333F + 0.5F;
            final float ff = f * f;

            damageRadius = 2.5F + f * 1.2F;
            BlockPos.getAllInBox(center.add(-j, -k, -l), center.add(j, k, l)).forEach(pos -> {
                if (world.getTileEntity(pos) instanceof TileEntityDragonforgeInput) {
                    ((TileEntityDragonforgeInput) world.getTileEntity(pos)).onHitWithFlame();
                    return;
                }
                if (center.distanceSq(pos) <= ff) {
                    if (IafConfig.dragonGriefing != 2 && world.rand.nextFloat() > (float) center.distanceSq(pos) / ff) {
                        iceAttackBlock(world, pos);
                    }
                }
            });
        }

        final float stageDmg = stage * dmgScale;
        final int statusDuration = 50 * stage;
        world.getEntitiesWithinAABB(
            LivingEntity.class,
            new AxisAlignedBB(
                (double) center.getX() - damageRadius,
                (double) center.getY() - damageRadius,
                (double) center.getZ() - damageRadius,
                (double) center.getX() + damageRadius,
                (double) center.getY() + damageRadius,
                (double) center.getZ() + damageRadius
            )
        ).stream().forEach(livingEntity -> {
            if (!DragonUtils.onSameTeam(destroyer, livingEntity) && !destroyer.isEntityEqual(livingEntity) && destroyer.canEntityBeSeen(livingEntity)) {
                livingEntity.attackEntityFrom(source, stageDmg);
                FrozenProperties.setFrozenFor(livingEntity, statusDuration);
            }
        });
    }

    public static void destroyAreaLightning(World world, BlockPos center, EntityDragonBase destroyer) {
        if (MinecraftForge.EVENT_BUS.post(new DragonFireDamageWorldEvent(destroyer, center.getX(), center.getY(), center.getZ())))
            return;
        Entity cause = destroyer.getRidingPlayer() != null ? destroyer.getRidingPlayer() : destroyer;
        DamageSource source = IafDamageRegistry.causeDragonLightningDamage(cause);
        int stage = destroyer.getDragonStage();
        double damageRadius = 3.5D;
        float dmgScale = (float) IafConfig.dragonAttackDamageLightning;

        if (stage <= 3) {
            BlockPos.getAllInBox(center.add(-1, -1, -1), center.add(1, 1, 1)).forEach(pos -> {
                if (world.getTileEntity(pos) instanceof TileEntityDragonforgeInput) {
                    ((TileEntityDragonforgeInput) world.getTileEntity(pos)).onHitWithFlame();
                    return;
                }
                if (IafConfig.dragonGriefing != 2 && world.rand.nextBoolean()) {
                    lightningAttackBlock(world, pos);
                }
            });
        } else {
            int radius = stage == 4 ? 2 : 3;
            int j = radius + world.rand.nextInt(1);
            int k = radius + world.rand.nextInt(1);
            int l = radius + world.rand.nextInt(1);
            float f = (float) (j + k + l) * 0.333F + 0.5F;
            final float ff = f * f;

            damageRadius = 2.5F + f * 1.2F;
            BlockPos.getAllInBox(center.add(-j, -k, -l), center.add(j, k, l)).forEach(pos -> {
                if (world.getTileEntity(pos) instanceof TileEntityDragonforgeInput) {
                    ((TileEntityDragonforgeInput) world.getTileEntity(pos)).onHitWithFlame();
                    return;
                }
                if (center.distanceSq(pos) <= ff) {
                    if (IafConfig.dragonGriefing != 2 && world.rand.nextFloat() > (float) center.distanceSq(pos) / ff) {
                        lightningAttackBlock(world, pos);
                    }
                }
            });
        }

        final float stageDmg = stage * dmgScale;
        world.getEntitiesWithinAABB(
            LivingEntity.class,
            new AxisAlignedBB(
                (double) center.getX() - damageRadius,
                (double) center.getY() - damageRadius,
                (double) center.getZ() - damageRadius,
                (double) center.getX() + damageRadius,
                (double) center.getY() + damageRadius,
                (double) center.getZ() + damageRadius
            )
        ).stream().forEach(livingEntity -> {
            if (!DragonUtils.onSameTeam(destroyer, livingEntity) && !destroyer.isEntityEqual(livingEntity) && destroyer.canEntityBeSeen(livingEntity)) {
                livingEntity.attackEntityFrom(source, stageDmg);
                double d1 = destroyer.getPosX() - livingEntity.getPosX();
                double d0 = destroyer.getPosZ() - livingEntity.getPosZ();
                livingEntity.applyKnockback(0.3F, d1, d0);
            }
        });
    }

    public static void destroyAreaFireCharge(World world, BlockPos center, EntityDragonBase destroyer) {
        if (destroyer != null) {
            if (MinecraftForge.EVENT_BUS.post(new DragonFireDamageWorldEvent(destroyer, center.getX(), center.getY(), center.getZ())))
                return;
            Entity cause = destroyer.getRidingPlayer() != null ? destroyer.getRidingPlayer() : destroyer;
            DamageSource source = IafDamageRegistry.causeDragonFireDamage(cause);
            int stage = destroyer.getDragonStage();
            int j = 2;
            int k = 2;
            int l = 2;

            if (stage <= 3) {
                BlockPos.getAllInBox(center.add(-j, -k, -l), center.add(j, k, l)).forEach(pos -> {
                    if (world.rand.nextFloat() * 3 > center.distanceSq(pos) &&
                        !(world.getBlockState(pos).getBlock() instanceof IDragonProof) &&
                        DragonUtils.canDragonBreak(world.getBlockState(pos).getBlock())) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                    if (world.rand.nextBoolean()) {
                        fireAttackBlock(world, pos);
                    }
                });
            } else {
                final int radius = stage == 4 ? 2 : 3;
                j = radius + world.rand.nextInt(2);
                k = radius + world.rand.nextInt(2);
                l = radius + world.rand.nextInt(2);
                final float f = (float) (j + k + l) * 0.333F + 0.5F;
                final float ff = f * f;

                destroyBlocks(world, center, j, k, l, ff);

                j++;
                k++;
                l++;
                BlockPos.getAllInBox(center.add(-j, -k, -l), center.add(j, k, l)).forEach(pos -> {
                    if (center.distanceSq(pos) <= ff) {
                        fireAttackBlock(world, pos);
                    }
                });
            }

            final float stageDmg = Math.max(1, stage - 1) * 2F;
            final int statusDuration = 15;
            world.getEntitiesWithinAABB(
                LivingEntity.class,
                new AxisAlignedBB(
                    (double) center.getX() - j,
                    (double) center.getY() - k,
                    (double) center.getZ() - l,
                    (double) center.getX() + j,
                    (double) center.getY() + k,
                    (double) center.getZ() + l
                )
            ).stream().forEach(livingEntity -> {
                if (!destroyer.isOnSameTeam(livingEntity) && !destroyer.isEntityEqual(livingEntity) && destroyer.canEntityBeSeen(livingEntity)) {
                    livingEntity.attackEntityFrom(source, stageDmg);
                    livingEntity.setFire(statusDuration);
                }
            });
            if (IafConfig.explosiveDragonBreath)
                causeExplosion(world, center, destroyer, source, stage);
        }
    }

    public static void destroyAreaIceCharge(World world, BlockPos center, EntityDragonBase destroyer) {
        if (destroyer != null) {
            if (MinecraftForge.EVENT_BUS.post(new DragonFireDamageWorldEvent(destroyer, center.getX(), center.getY(), center.getZ())))
                return;
            Entity cause = destroyer.getRidingPlayer() != null ? destroyer.getRidingPlayer() : destroyer;
            DamageSource source = IafDamageRegistry.causeDragonIceDamage(cause);
            int stage = destroyer.getDragonStage();
            int j = 2;
            int k = 2;
            int l = 2;

            if (stage <= 3) {
                BlockPos.getAllInBox(center.add(-j, -k, -l), center.add(j, k, l)).forEach(pos -> {
                    if (world.rand.nextFloat() * 3 > center.distanceSq(pos) &&
                        !(world.getBlockState(pos).getBlock() instanceof IDragonProof) && DragonUtils.canDragonBreak(world.getBlockState(pos).getBlock())) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                    if (world.rand.nextBoolean()) {
                        iceAttackBlock(world, pos);
                    }
                });
            } else {
                int radius = stage == 4 ? 2 : 3;
                j = radius + world.rand.nextInt(2);
                k = radius + world.rand.nextInt(2);
                l = radius + world.rand.nextInt(2);
                final float f = (float) (j + k + l) * 0.333F + 0.5F;
                final float ff = f * f;

                destroyBlocks(world, center, j, k, l, ff);

                j++;
                k++;
                l++;
                BlockPos.getAllInBox(center.add(-j, -k, -l), center.add(j, k, l)).forEach(pos -> {
                    if (center.distanceSq(pos) <= ff) {
                        iceAttackBlock(world, pos);
                    }
                });
            }

            final float stageDmg = Math.max(1, stage - 1) * 2F;
            final int statusDuration = 400;
            world.getEntitiesWithinAABB(
                LivingEntity.class,
                new AxisAlignedBB(
                    (double) center.getX() - j,
                    (double) center.getY() - k,
                    (double) center.getZ() - l,
                    (double) center.getX() + j,
                    (double) center.getY() + k,
                    (double) center.getZ() + l
                )
            ).stream().forEach(livingEntity -> {
                if (!destroyer.isOnSameTeam(livingEntity) && !destroyer.isEntityEqual(livingEntity) && destroyer.canEntityBeSeen(livingEntity)) {
                    livingEntity.attackEntityFrom(source, stageDmg);
                    FrozenProperties.setFrozenFor(livingEntity, statusDuration);
                }
            });
            if (IafConfig.explosiveDragonBreath)
                causeExplosion(world, center, destroyer, source, stage);
        }
    }


    public static void destroyAreaLightningCharge(World world, BlockPos center, EntityDragonBase destroyer) {
        if (destroyer != null) {
            if (MinecraftForge.EVENT_BUS.post(new DragonFireDamageWorldEvent(destroyer, center.getX(), center.getY(), center.getZ())))
                return;
            Entity cause = destroyer.getRidingPlayer() != null ? destroyer.getRidingPlayer() : destroyer;
            DamageSource source = IafDamageRegistry.causeDragonLightningDamage(cause);
            int stage = destroyer.getDragonStage();
            int j = 2;
            int k = 2;
            int l = 2;

            if (stage <= 3) {
                BlockPos.getAllInBox(center.add(-j, -k, -l), center.add(j, k, l)).forEach(pos -> {
                    if (Math.pow(world.rand.nextFloat() * 7F, 2) > center.distanceSq(pos)
                        && !(world.getBlockState(pos).getBlock() instanceof IDragonProof) && DragonUtils.canDragonBreak(world.getBlockState(pos).getBlock())) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState());
                    }
                    if (Math.pow(world.rand.nextFloat() * 7F, 2) > center.distanceSq(pos)
                        && !(world.getBlockState(pos).getBlock() instanceof IDragonProof) && DragonUtils.canDragonBreak(world.getBlockState(pos).getBlock())) {
                        BlockState transformState = transformBlockLightning(world.getBlockState(pos));
                        world.setBlockState(pos, transformState);
                    }
                });
            } else {
                int radius = stage == 4 ? 2 : 3;
                j = radius + world.rand.nextInt(2);
                k = radius + world.rand.nextInt(2);
                l = radius + world.rand.nextInt(2);
                float f = (float) (j + k + l) * 0.333F + 0.5F;
                final float ff = f * f;

                destroyBlocks(world, center, j, k, l, ff);

                j++;
                k++;
                l++;
                BlockPos.getAllInBox(center.add(-j, -k, -l), center.add(j, k, l)).forEach(pos -> {
                    if (center.distanceSq(pos) <= ff) {
                        if (!(world.getBlockState(pos).getBlock() instanceof IDragonProof) && DragonUtils.canDragonBreak(world.getBlockState(pos).getBlock())) {
                            BlockState transformState = transformBlockLightning(world.getBlockState(pos));
                            world.setBlockState(pos, transformState);
                        }
                    }
                });
            }

            final float stageDmg = Math.max(1, stage - 1) * 2F;
            world.getEntitiesWithinAABB(
                LivingEntity.class,
                new AxisAlignedBB(
                    (double) center.getX() - j,
                    (double) center.getY() - k,
                    (double) center.getZ() - l,
                    (double) center.getX() + j,
                    (double) center.getY() + k,
                    (double) center.getZ() + l
                )
            ).stream().forEach(livingEntity -> {
                if (!destroyer.isOnSameTeam(livingEntity) && !destroyer.isEntityEqual(livingEntity) && destroyer.canEntityBeSeen(livingEntity)) {
                    livingEntity.attackEntityFrom(source, stageDmg);
                    double d1 = destroyer.getPosX() - livingEntity.getPosX();
                    double d0 = destroyer.getPosZ() - livingEntity.getPosZ();
                    livingEntity.applyKnockback(0.9F, d1, d0);
                }
            });
            if (IafConfig.explosiveDragonBreath)
                causeExplosion(world, center, destroyer, source, stage);
        }
    }

    private static void causeExplosion(World world, BlockPos center, EntityDragonBase destroyer, DamageSource source, int stage) {
        Explosion.Mode mode = ForgeEventFactory.getMobGriefingEvent(world, destroyer) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
        BlockLaunchExplosion explosion = new BlockLaunchExplosion(world, destroyer, source, center.getX(), center.getY(), center.getZ(), Math.min(2, stage - 2), mode);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
    }


    private static void fireAttackBlock(World world, BlockPos pos) {
        if (!(world.getBlockState(pos).getBlock() instanceof IDragonProof) && DragonUtils.canDragonBreak(world.getBlockState(pos).getBlock())) {
            BlockState transformState = transformBlockFire(world.getBlockState(pos));
            if (transformState.getBlock() != world.getBlockState(pos).getBlock())
                world.setBlockState(pos, transformState);
            if (world.rand.nextBoolean() && transformState.getMaterial().isSolid() &&
                world.getFluidState(pos.up()).isEmpty() && !world.getBlockState(pos.up()).isSolid() &&
                world.getBlockState(pos).isSolid() && DragonUtils.canDragonBreak(world.getBlockState(pos.up()).getBlock())) {
                world.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
            }
        }
    }

    private static void iceAttackBlock(World world, BlockPos pos) {
        if (!(world.getBlockState(pos).getBlock() instanceof IDragonProof) && DragonUtils.canDragonBreak(world.getBlockState(pos).getBlock())) {
            BlockState transformState = transformBlockIce(world.getBlockState(pos));
            if (transformState.getBlock() != world.getBlockState(pos).getBlock())
                world.setBlockState(pos, transformState);
            if (world.rand.nextInt(9) == 0 && transformState.getMaterial().isSolid() &&
                world.getFluidState(pos.up()).isEmpty() && !world.getBlockState(pos.up()).isSolid() &&
                world.getBlockState(pos).isSolid() && DragonUtils.canDragonBreak(world.getBlockState(pos.up()).getBlock())) {
                world.setBlockState(pos.up(), IafBlockRegistry.DRAGON_ICE_SPIKES.getDefaultState());
            }
        }
    }

    private static void lightningAttackBlock(World world, BlockPos pos) {
        if (!(world.getBlockState(pos).getBlock() instanceof IDragonProof) && DragonUtils.canDragonBreak(world.getBlockState(pos).getBlock())) {
            BlockState transformState = transformBlockLightning(world.getBlockState(pos));
            if (transformState.getBlock() != world.getBlockState(pos).getBlock()) {
                world.setBlockState(pos, transformState);
            }
        }
    }

    private static void destroyBlocks(World world, BlockPos center, int x, int y, int z, double radius2) {
        BlockPos.getAllInBox(center.add(-x, -y, -z), center.add(x, y, z)).forEach(pos -> {
            if (center.distanceSq(pos) <= radius2) {
                if (world.rand.nextFloat() * 3 > (float) center.distanceSq(pos) / radius2 && !(world.getBlockState(pos).getBlock() instanceof IDragonProof) && DragonUtils.canDragonBreak(world.getBlockState(pos).getBlock())) {
                    world.setBlockState(pos, Blocks.AIR.getDefaultState());
                }
            }
        });
    }

    public static BlockState transformBlockFire(BlockState in) {
        if (in.getBlock() instanceof SpreadableSnowyDirtBlock) {
            return IafBlockRegistry.CHARRED_GRASS.getDefaultState().with(BlockReturningState.REVERTS, true);
        } else if (in.getMaterial() == Material.EARTH && in.getBlock() == Blocks.DIRT) {
            return IafBlockRegistry.CHARRED_DIRT.getDefaultState().with(BlockReturningState.REVERTS, true);
        } else if (in.getMaterial() == Material.SAND && in.getBlock() == Blocks.GRAVEL) {
            return IafBlockRegistry.CHARRED_GRAVEL.getDefaultState().with(BlockFallingReturningState.REVERTS, true);
        } else if (in.getMaterial() == Material.ROCK && (in.getBlock() == Blocks.COBBLESTONE || in.getBlock().getTranslationKey().contains("cobblestone"))) {
            return IafBlockRegistry.CHARRED_COBBLESTONE.getDefaultState().with(BlockReturningState.REVERTS, true);
        } else if (in.getMaterial() == Material.ROCK && in.getBlock() != IafBlockRegistry.CHARRED_COBBLESTONE) {
            return IafBlockRegistry.CHARRED_STONE.getDefaultState().with(BlockReturningState.REVERTS, true);
        } else if (in.getBlock() == Blocks.GRASS_PATH) {
            return IafBlockRegistry.CHARRED_GRASS_PATH.getDefaultState().with(BlockCharedPath.REVERTS, true);
        } else if (in.getMaterial() == Material.WOOD) {
            return IafBlockRegistry.ASH.getDefaultState();
        } else if (in.getMaterial() == Material.LEAVES || in.getMaterial() == Material.PLANTS || in.getBlock() == Blocks.SNOW) {
            return Blocks.AIR.getDefaultState();
        }
        return in;
    }

    public static BlockState transformBlockIce(BlockState in) {
        if (in.getBlock() instanceof SpreadableSnowyDirtBlock) {
            return IafBlockRegistry.FROZEN_GRASS.getDefaultState().with(BlockReturningState.REVERTS, true);
        } else if (in.getMaterial() == Material.EARTH && in.getBlock() == Blocks.DIRT || in.getMaterial() == Material.SNOW_BLOCK) {
            return IafBlockRegistry.FROZEN_DIRT.getDefaultState().with(BlockReturningState.REVERTS, true);
        } else if (in.getMaterial() == Material.SAND && in.getBlock() == Blocks.GRAVEL) {
            return IafBlockRegistry.FROZEN_GRAVEL.getDefaultState().with(BlockFallingReturningState.REVERTS, true);
        } else if (in.getMaterial() == Material.SAND && in.getBlock() != Blocks.GRAVEL) {
            return in;
        } else if (in.getMaterial() == Material.ROCK && (in.getBlock() == Blocks.COBBLESTONE || in.getBlock().getTranslationKey().contains("cobblestone"))) {
            return IafBlockRegistry.FROZEN_COBBLESTONE.getDefaultState().with(BlockReturningState.REVERTS, true);
        } else if (in.getMaterial() == Material.ROCK && in.getBlock() != IafBlockRegistry.FROZEN_COBBLESTONE) {
            return IafBlockRegistry.FROZEN_STONE.getDefaultState().with(BlockReturningState.REVERTS, true);
        } else if (in.getBlock() == Blocks.GRASS_PATH) {
            return IafBlockRegistry.FROZEN_GRASS_PATH.getDefaultState().with(BlockCharedPath.REVERTS, true);
        } else if (in.getMaterial() == Material.WOOD) {
            return IafBlockRegistry.FROZEN_SPLINTERS.getDefaultState();
        } else if (in.getMaterial() == Material.WATER) {
            return Blocks.ICE.getDefaultState();
        } else if (in.getMaterial() == Material.LEAVES || in.getMaterial() == Material.PLANTS || in.getBlock() == Blocks.SNOW) {
            return Blocks.AIR.getDefaultState();
        }
        return in;
    }

    public static BlockState transformBlockLightning(BlockState in) {
        if (in.getBlock() instanceof SpreadableSnowyDirtBlock) {
            return IafBlockRegistry.CRACKLED_GRASS.getDefaultState().with(BlockReturningState.REVERTS, true);
        } else if (in.getMaterial() == Material.EARTH && in.getBlock() == Blocks.DIRT) {
            return IafBlockRegistry.CRACKLED_DIRT.getDefaultState().with(BlockReturningState.REVERTS, true);
        } else if (in.getMaterial() == Material.SAND && in.getBlock() == Blocks.GRAVEL) {
            return IafBlockRegistry.CRACKLED_GRAVEL.getDefaultState().with(BlockFallingReturningState.REVERTS, true);
        } else if (in.getMaterial() == Material.ROCK && (in.getBlock() == Blocks.COBBLESTONE || in.getBlock().getTranslationKey().contains("cobblestone"))) {
            return IafBlockRegistry.CRACKLED_COBBLESTONE.getDefaultState().with(BlockReturningState.REVERTS, true);
        } else if (in.getMaterial() == Material.ROCK && in.getBlock() != IafBlockRegistry.CRACKLED_COBBLESTONE) {
            return IafBlockRegistry.CRACKLED_STONE.getDefaultState().with(BlockReturningState.REVERTS, true);
        } else if (in.getBlock() == Blocks.GRASS_PATH) {
            return IafBlockRegistry.CRACKLED_GRASS_PATH.getDefaultState().with(BlockCharedPath.REVERTS, true);
        } else if (in.getMaterial() == Material.WOOD) {
            return IafBlockRegistry.ASH.getDefaultState();
        } else if (in.getMaterial() == Material.LEAVES || in.getMaterial() == Material.PLANTS || in.getBlock() == Blocks.SNOW) {
            return Blocks.AIR.getDefaultState();
        }
        return in;
    }

}
