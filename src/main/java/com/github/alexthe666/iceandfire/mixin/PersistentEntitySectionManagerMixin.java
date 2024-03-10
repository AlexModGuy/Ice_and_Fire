package com.github.alexthe666.iceandfire.mixin;

import com.github.alexthe666.iceandfire.entity.EntityMutlipartPart;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PersistentEntitySectionManager.class)
public abstract class PersistentEntitySectionManagerMixin<T extends EntityAccess> {
    @Inject(method = "addEntityUuid", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;)V", shift = At.Shift.AFTER, remap = false))
    private void iceandfire$resetParts(final T entity, final CallbackInfoReturnable<Boolean> callback) {
        if (entity instanceof EntityMutlipartPart part) {
            // Just to make sure that the parent will update it
            part.discard();
        }
    }
}
