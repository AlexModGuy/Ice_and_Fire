package com.github.alexthe666.iceandfire.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.math.BlockPos;

public class MyrmexDeleteButton extends GuiButton {
    public BlockPos pos;
    public MyrmexDeleteButton(int id, int x, int y, BlockPos pos, String delete) {
        super(id, x, y, 50, 20, delete);
        this.pos = pos;
    }
}
