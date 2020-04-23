package com.github.alexthe666.iceandfire.world.dimension;

import com.github.alexthe666.iceandfire.entity.MiscEntityProperties;
import com.github.alexthe666.iceandfire.world.gen.WorldGenDreadExitPortal;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ITeleporter;

import java.util.Random;

public class TeleporterDreadLands implements ITeleporter {

    public boolean returningToOverworld;

    public TeleporterDreadLands(WorldServer world, boolean overworld) {
        returningToOverworld = overworld;
    }

    public void placeInPortal(Entity entity) {
        entity.motionX = entity.motionY = entity.motionZ = 0.0D;
    }

    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        if(returningToOverworld){
            BlockPos setPos = null;
            if(entity instanceof EntityLivingBase){
                placeInPortal(entity);
                MiscEntityProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(entity, MiscEntityProperties.class);
                if(properties != null){
                    setPos = new BlockPos(properties.lastEnteredDreadPortalX, properties.lastEnteredDreadPortalY, properties.lastEnteredDreadPortalZ);
                }
            }
            if(setPos == null){
                setPos = world.getSpawnPoint();
            }
            entity.setPositionAndRotation(setPos.getX(), setPos.getY() + 0.5D, setPos.getZ(), 0, 0);
        }else{
            WorldGenDreadExitPortal exitPortal = new WorldGenDreadExitPortal();
            BlockPos zeroHeight = new BlockPos(0, 87, 0);

            exitPortal.generate(world, new Random(),  zeroHeight);
            BlockPos playerSetPos = zeroHeight.add(2, 1, 2);
            entity.setPositionAndRotation(playerSetPos.getX(), playerSetPos.getY(), playerSetPos.getZ(), 0, 0);
        }
    }
}
