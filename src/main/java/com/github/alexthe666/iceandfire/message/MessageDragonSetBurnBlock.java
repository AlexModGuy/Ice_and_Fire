package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageDragonSetBurnBlock {
    public int dragonId;
    public boolean breathingFire;
    public int posX;
    public int posY;
    public int posZ;

    public MessageDragonSetBurnBlock(int dragonId, boolean breathingFire, BlockPos pos) {
        this.dragonId = dragonId;
        this.breathingFire = breathingFire;
        posX = pos.getX();
        posY = pos.getY();
        posZ = pos.getZ();
    }

    public static MessageDragonSetBurnBlock read(FriendlyByteBuf buf) {
        return new MessageDragonSetBurnBlock(buf.readInt(), buf.readBoolean(), new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
    }

    public static void write(MessageDragonSetBurnBlock message, FriendlyByteBuf buf) {
        buf.writeInt(message.dragonId);
        buf.writeBoolean(message.breathingFire);
        buf.writeInt(message.posX);
        buf.writeInt(message.posY);
        buf.writeInt(message.posZ);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(final MessageDragonSetBurnBlock message, final Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();

            context.enqueueWork(() -> {
                Player player = context.getSender();

                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }

                if (player != null) {
                    Entity entity = player.level().getEntity(message.dragonId);

                    if (entity instanceof EntityDragonBase dragon) {
                        dragon.setBreathingFire(message.breathingFire);
                        dragon.burningTarget = new BlockPos(message.posX, message.posY, message.posZ);
                    }
                }
            });

            context.setPacketHandled(true);
        }
    }
}