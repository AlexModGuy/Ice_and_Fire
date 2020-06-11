package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.item.ItemMobSkull;
import net.minecraft.item.Item;

public enum EnumSkullType {
    HIPPOGRYPH,
    CYCLOPS,
    COCKATRICE,
    STYMPHALIAN,
    TROLL,
    AMPHITHERE,
    SEASERPENT,
    HYDRA;

    public String itemResourceName;
    public Item skull_item;

    EnumSkullType() {
        itemResourceName = this.name().toLowerCase() + "_skull";
    }

    public static void initItems() {
        for (EnumSkullType skull : EnumSkullType.values()) {
            skull.skull_item = new ItemMobSkull(skull);

        }
    }
}
