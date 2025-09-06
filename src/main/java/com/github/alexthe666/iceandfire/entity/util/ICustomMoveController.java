package com.github.alexthe666.iceandfire.entity.util;

/**
 * A custom byte is used to store custom movement state
 * From hi to low
 * x|x|sprint|dismount    strike|attack|down|up
 */
public interface ICustomMoveController {
    void up(boolean up);

    void down(boolean down);

    void attack(boolean attack);

    void strike(boolean strike);

    void dismount(boolean dismount);

    default void sprint(boolean sprint) {
        // Not implemented
        // Todo :: Implement this for everyone
    }


    void setControlState(byte state);

    byte getControlState();
}
