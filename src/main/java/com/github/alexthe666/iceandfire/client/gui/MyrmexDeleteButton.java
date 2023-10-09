package com.github.alexthe666.iceandfire.client.gui;

import net.minecraft.client.gui.components.Button;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class MyrmexDeleteButton extends Button {
    public BlockPos pos;

    public MyrmexDeleteButton(int x, int y, BlockPos pos, Component delete, Button.OnPress onPress) {
        super(x, y, 50, 20, delete, onPress, DEFAULT_NARRATION);
        this.pos = pos;
    }
}
