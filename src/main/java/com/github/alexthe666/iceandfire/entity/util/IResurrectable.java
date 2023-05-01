package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.world.item.ItemStack;

public interface IResurrectable {

    /**
     * Determine if the target should be resurrected by the given itemStack
     *
     * @param itemStack The given itemStack used to perform the resurrection ritual
     * @return True if they should, false if not
     */
    default boolean canResurrectBy(ItemStack itemStack) {
        return false;
    }


    /**
     * Perform resurrection
     * @return True if the resurrection is success, false if not
     */
    boolean resurrect();

    /**
     * The itemStack should shrink by this value
     * @return item counts to consume
     */
    default int getResurrectCost() {
        return 1;
    }
}
