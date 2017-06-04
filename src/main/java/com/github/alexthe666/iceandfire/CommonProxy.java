package com.github.alexthe666.iceandfire;

import com.github.alexthe666.iceandfire.enums.EnumDragonArmor;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy {

    public void preRender() {

    }

    public void render() {
    }

    public void renderArmors(EnumDragonArmor armor) {
    }

    public void spawnParticle(String name, World world, double x, double y, double z, double motX, double motY, double motZ) {
    }

    public void openBestiaryGui(ItemStack book) {
    }

    public Object getArmorModel(int armorId) {
        return null;
    }

    public Object getFontRenderer() {
        return null;
    }

    public <T extends AbstractMessage<T>> void handleMessage(final T message, final MessageContext messageContext) {
        WorldServer world = (WorldServer) messageContext.getServerHandler().player.world;
        world.addScheduledTask(() -> message.onServerReceived(FMLCommonHandler.instance().getMinecraftServerInstance(), message, messageContext.getServerHandler().player, messageContext));
    }
}
