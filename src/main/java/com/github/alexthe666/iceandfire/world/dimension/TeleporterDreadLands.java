package com.github.alexthe666.iceandfire.world.dimension;

import com.github.alexthe666.iceandfire.entity.MiscEntityProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ITeleporter;

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
                    setPos = properties.lastEnteredDreadPortal;
                }
            }
            if(setPos == null){
                setPos = world.getSpawnPoint();
            }
            entity.setPositionAndRotation(setPos.getX(), setPos.getY() + 0.5D, setPos.getZ(), 0, 0);
        }else{
            entity.setPositionAndRotation(0, 110, 0, 0, 0);
        }
    }
}
