package com.github.alexthe666.iceandfire.world.feature;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class SpawnWanderingCyclops extends Feature<NoFeatureConfig> {

    public SpawnWanderingCyclops(Codec<NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(ISeedReader worldIn, ChunkGenerator p_230362_3_, Random rand, BlockPos position, NoFeatureConfig p_230362_6_) {
        if (!IafWorldRegistry.isDimensionListedForMobs(worldIn)) {
            return false;
        }
        position = worldIn.getHeightmapPos(Heightmap.Type.WORLD_SURFACE_WG, position.offset(8, 0, 8));

        if (IafConfig.generateWanderingCyclops && IafWorldRegistry.isFarEnoughFromSpawn(worldIn, position)) {
            if (rand.nextInt(IafConfig.spawnWanderingCyclopsChance + 1) == 0 && rand.nextInt(12) == 0) {
                EntityCyclops cyclops = IafEntityRegistry.CYCLOPS.get().create(worldIn.getLevel());
                cyclops.setPos(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                cyclops.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(position), SpawnReason.SPAWNER, null, null);
                worldIn.addFreshEntity(cyclops);
                for (int i = 0; i < 3 + rand.nextInt(3); i++) {
                    SheepEntity sheep = EntityType.SHEEP.create(worldIn.getLevel());
                    sheep.setPos(position.getX() + 0.5F, position.getY() + 1, position.getZ() + 0.5F);
                    sheep.setColor(SheepEntity.getRandomSheepColor(rand));
                    worldIn.addFreshEntity(sheep);
                }
            }
        }

        return false;
    }
}
