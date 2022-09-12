package com.github.alexthe666.iceandfire.inventory;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class ContainerHippocampus extends AbstractContainerMenu {
    private final Container hippocampusInventory;
    private final EntityHippocampus hippocampus;
    private final Player player;

    public ContainerHippocampus(int i, Inventory playerInventory) {
        this(i, new SimpleContainer(18), playerInventory, null);
    }

    public ContainerHippocampus(int id, Container ratInventory, Inventory playerInventory, EntityHippocampus hippocampus) {
        super(IafContainerRegistry.HIPPOCAMPUS_CONTAINER.get(), id);
        this.hippocampusInventory = ratInventory;
        if (hippocampus == null && IceAndFire.PROXY.getReferencedMob() instanceof EntityHippocampus) {
            hippocampus = (EntityHippocampus) IceAndFire.PROXY.getReferencedMob();
        }
        this.hippocampus = hippocampus;
        this.player = playerInventory.player;
        hippocampusInventory.startOpen(player);
        this.addSlot(new Slot(hippocampusInventory, 0, 8, 18) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == Items.SADDLE && !this.hasItem();
            }

            @Override
            public void setChanged() {
                if (ContainerHippocampus.this.hippocampus != null) {
                    ContainerHippocampus.this.hippocampus.refreshInventory();
                }
            }

            @Override
            public boolean isActive() {
                return true;
            }
        });
        this.addSlot(new Slot(hippocampusInventory, 1, 8, 36) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() == Blocks.CHEST.asItem() && !this.hasItem();
            }

            @Override
            public void setChanged() {
                if (ContainerHippocampus.this.hippocampus != null) {
                    ContainerHippocampus.this.hippocampus.refreshInventory();
                }
            }

            @Override
            public boolean isActive() {
                return true;
            }
        });
        this.addSlot(new Slot(hippocampusInventory, 2, 8, 52) {

            @Override
            public boolean mayPlace(ItemStack stack) {
                return EntityHippocampus.getIntFromArmor(stack) != 0;
            }

            @Override
            public void setChanged() {
                if (ContainerHippocampus.this.hippocampus != null) {
                    ContainerHippocampus.this.hippocampus.refreshInventory();
                }
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @Override
            public boolean isActive() {
                return true;
            }
        });

        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 5; ++l) {
                this.addSlot(new Slot(hippocampusInventory, 3 + l + k * 5, 80 + l * 18, 18 + k * 18) {
                    @Override
                    public boolean isActive() {
                        return ContainerHippocampus.this.hippocampus != null && ContainerHippocampus.this.hippocampus.isChested();
                    }

                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return ContainerHippocampus.this.hippocampus != null && ContainerHippocampus.this.hippocampus.isChested();
                    }
                });
            }
        }

        for (int i1 = 0; i1 < 3; ++i1) {
            for (int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(player.getInventory(), k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }

        for (int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(player.getInventory(), j1, 8 + j1 * 18, 142));
        }

    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.hippocampusInventory.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.hippocampusInventory.getContainerSize(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemstack1) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(2).mayPlace(itemstack1) && !this.getSlot(2).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }

            } else if (this.getSlot(0).mayPlace(itemstack1)) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.hippocampusInventory.getContainerSize() <= 3 || !this.moveItemStackTo(itemstack1, 3, this.hippocampusInventory.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.hippocampusInventory.stillValid(playerIn) && this.hippocampus.isAlive() && this.hippocampus.distanceTo(playerIn) < 8.0F;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.hippocampusInventory.stopOpen(playerIn);
    }
}