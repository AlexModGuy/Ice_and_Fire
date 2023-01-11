package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import org.jetbrains.annotations.NotNull;

public class IAFLookHelper extends LookControl {

    public IAFLookHelper(Mob LivingEntityIn) {
        super(LivingEntityIn);
    }

    @Override
    public void setLookAt(@NotNull Entity entityIn, float deltaYaw, float deltaPitch) {
        try {
            super.setLookAt(entityIn, deltaYaw, deltaPitch);//rarely causes crash with vanilla
        } catch (Exception e) {
            IceAndFire.LOGGER.warn("Stopped a crash from happening relating to faulty looking AI.");
        }
    }
}
