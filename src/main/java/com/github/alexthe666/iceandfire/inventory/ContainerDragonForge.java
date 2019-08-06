package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.core.ModItems;
import com.github.alexthe666.iceandfire.core.ModRecipes;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerDragonForge extends SyncedFieldContainer {

    private final IInventory tileFurnace;
    private int cookTime;
    private boolean isFire;
    public ContainerDragonForge(InventoryPlayer playerInventory, IInventory furnaceInventory) {
        super(furnaceInventory);
        this.tileFurnace = furnaceInventory;
        isFire = ((TileEntityDragonforge)tileFurnace).isFire;
        this.addSlotToContainer(new Slot(furnaceInventory, 0, 68, 34));
        this.addSlotToContainer(new Slot(furnaceInventory, 1, 86, 34));
        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, furnaceInventory, 2, 148, 35));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileFurnace.isUsableByPlayer(playerIn);
    }
}
