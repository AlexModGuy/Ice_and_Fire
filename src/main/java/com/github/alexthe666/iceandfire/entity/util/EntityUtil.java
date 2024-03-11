package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class EntityUtil {
    public static void updatePart(@Nullable final EntityMutlipartPart part, @NotNull final LivingEntity parent) {
        if (part == null || !(parent.level() instanceof ServerLevel serverLevel) || parent.isRemoved()) {
            return;
        }

        if (part.shouldExist()) {
            int attempts = /* In case other mods block the addition */ 50;

            // Update UUID if a different entity with the same UUID exists already
            while (attempts > 0 && !serverLevel.addFreshEntity(part)) {
                part.setUUID(Mth.createInsecureUUID(parent.getRandom()));
                attempts--;
            }

            if (attempts == 0 && !part.isAddedToWorld()) {
                if (parent instanceof EntityDragonBase dragon) {
                    ResourceLocation location = Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(dragon.getType()));
                    UUID owner = dragon.getOwnerUUID();

                    String command = "/summon " + location + " ~ ~ ~ {AgeInTicks:" + dragon.getAgeInTicks() + ",Variant:" + dragon.getVariant() + ",Gender:" + dragon.isMale();

                    if (owner != null) {
                        command += ",TamedDragon:true,Owner:" + owner;
                    }

                    command += "}";
                    IceAndFire.LOGGER.info("You can re-summon the dragon with [{}]", command);
                }

                parent.discard();
                IceAndFire.LOGGER.error("Discarded [{}] due to constant UUID clash", parent);
            } else {
                part.setParent(parent);
            }
        }
    }
}
