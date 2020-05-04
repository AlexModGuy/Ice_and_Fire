package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.client.render.item.RenderDeathWormGauntlet;
import com.github.alexthe666.iceandfire.item.ItemDeathwormGauntlet;
import com.github.alexthe666.iceandfire.item.ItemTrollWeapon;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@OnlyIn(Dist.CLIENT)
public class IceAndFireTEISR extends TileEntityItemStackRenderer {

    private RenderTrollWeapon renderTrollWeapon = new RenderTrollWeapon();
    private RenderDeathWormGauntlet renderDeathWormGauntlet = new RenderDeathWormGauntlet();
    private RenderDreadPortal renderDreadPortal = new RenderDreadPortal();

    public void renderByItem(ItemStack itemStackIn) {
        if (itemStackIn.getItem() instanceof ItemTrollWeapon) {
            ItemTrollWeapon weaponItem = (ItemTrollWeapon) itemStackIn.getItem();
            renderTrollWeapon.renderItem(weaponItem.weapon, 0, 0, 0, 0.0F, 0, 0.0F);
        }
        if (itemStackIn.getItem() instanceof ItemDeathwormGauntlet) {
            renderDeathWormGauntlet.renderItem(itemStackIn, 0, 0, 0, 0.0F, 0, 0.0F);
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(IafBlockRegistry.DREAD_PORTAL)) {
            renderDreadPortal.render(null, 0, 0, 0, 0.0F, 0, 0.0F);
        }
    }
}
