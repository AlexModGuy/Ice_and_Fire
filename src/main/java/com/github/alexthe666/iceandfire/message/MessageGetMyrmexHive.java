package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.ClientProxy;
import com.github.alexthe666.iceandfire.entity.MyrmexHive;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageGetMyrmexHive extends AbstractMessage<MessageGetMyrmexHive> {

    public MyrmexHive hive;

    public MessageGetMyrmexHive(MyrmexHive hive) {
        this.hive = hive;
    }

    public MessageGetMyrmexHive() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        hive = new MyrmexHive();
        hive.readVillageDataFromNBT(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        CompoundNBT tag = new CompoundNBT();
        if (hive != null) {
            hive.writeVillageDataToNBT(tag);
        }
        ByteBufUtils.writeTag(buf, tag);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClientReceived(Minecraft client, MessageGetMyrmexHive message, EntityPlayer player, MessageContext messageContext) {
        ClientProxy.setReferedClientHive(message.hive);
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageGetMyrmexHive message, EntityPlayer player, MessageContext messageContext) {
        if (player.world != null) {
            MyrmexHive serverHive = MyrmexWorldData.get(player.world).getHiveFromUUID(message.hive.hiveUUID);
            if (serverHive != null) {
                CompoundNBT tag = new CompoundNBT();
                message.hive.writeVillageDataToNBT(tag);
                serverHive.readVillageDataFromNBT(tag);
            }
        }
    }
}