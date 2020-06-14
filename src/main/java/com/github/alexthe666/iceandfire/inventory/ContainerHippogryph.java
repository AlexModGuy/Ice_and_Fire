package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerHippogryph extends Container {
    private final IInventory hippogryphInventory;
    private final EntityHippogryph hippogryph;
    private final PlayerEntity player;

    public ContainerHippogryph(int i, PlayerInventory playerInventory) {
        this(i, new Inventory(3), playerInventory, null);
    }

    public ContainerHippogryph(int id, IInventory ratInventory, PlayerInventory playerInventory, EntityHippogryph hippogryph) {
        super(IafContainerRegistry.HIPPOGRYPH_CONTAINER, id);
        this.hippogryphInventory = ratInventory;
        this.hippogryph = hippogryph;
        this.player = playerInventory.player;
        int i = 3;
        hippogryphInventory.openInventory(player);
        int j = -18;
        this.addSlot(new Slot(hippogryphInventory, 0, 8, 18) {
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == Items.SADDLE && !this.getHasStack();
            }

            @OnlyIn(Dist.CLIENT)
            public boolean isEnabled() {
                return true;
            }
        });
        this.addSlot(new Slot(hippogryphInventory, 1, 8, 36) {
            public boolean isItemValid(ItemStack stack) {
                return stack.getItem() == Item.getItemFromBlock(Blocks.CHEST) && !this.getHasStack();
            }

            public void onSlotChanged() {
                if (ContainerHippogryph.this.hippogryph != null) {
                    ContainerHippogryph.this.hippogryph.refreshInventory();
                }
            }

            @OnlyIn(Dist.CLIENT)
            public boolean isEnabled() {
                return true;
            }
        });
        this.addSlot(new Slot(hippogryphInventory, 2, 8, 52) {

            public boolean isItemValid(ItemStack stack) {
                return hippogryph.getIntFromArmor(stack) != 0;
            }

            public int getSlotStackLimit() {
                return 1;
            }

            @OnlyIn(Dist.CLIENT)
            public boolean isEnabled() {
                return true;
            }
        });

        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 5; ++l) {
                this.addSlot(new Slot(hippogryphInventory, 3 + l + k * 5, 80 + l * 18, 18 + k * 18) {
                    @OnlyIn(Dist.CLIENT)
                    public boolean isEnabled() {
                        return ContainerHippogryph.this.hippogryph != null && ContainerHippogryph.this.hippogryph.isChested();
                    }

                    public boolean isItemValid(ItemStack stack) {
                        return ContainerHippogryph.this.hippogryph != null && ContainerHippogryph.this.hippogryph.isChested();
                    }
                });
            }
        }

        for (int i1 = 0; i1 < 3; ++i1) {
            for (int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(player.inventory, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }

        for (int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(player.inventory, j1, 8 + j1 * 18, 142));
        }
    }

    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.hippogryphInventory.isUsableByPlayer(playerIn) && this.hippogryph.isAlive() && this.hippogryph.getDistance(playerIn) < 8.0F;
    }

    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.hippogryphInventory.closeInventory(playerIn);
    }
}