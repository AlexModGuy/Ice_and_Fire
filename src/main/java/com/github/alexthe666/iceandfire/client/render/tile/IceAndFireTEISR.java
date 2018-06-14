package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.item.ItemTrollWeapon;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IceAndFireTEISR extends TileEntityItemStackRenderer {

    private RenderTrollWeapon renderTrollWeapon = new RenderTrollWeapon();

    public void renderByItem(ItemStack itemStackIn) {
        if(itemStackIn.getItem() instanceof ItemTrollWeapon){
            ItemTrollWeapon weaponItem = (ItemTrollWeapon)itemStackIn.getItem();
            renderTrollWeapon.renderItem(weaponItem.weapon, 0, 0, 0, 0.0F, 0, 0.0F);
        }
    }
}
