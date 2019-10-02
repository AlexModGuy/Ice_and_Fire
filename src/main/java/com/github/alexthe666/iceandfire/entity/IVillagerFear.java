package com.github.alexthe666.iceandfire.entity;

public interface IVillagerFear {

    default boolean shouldFear() {
        return true;
    }
}
