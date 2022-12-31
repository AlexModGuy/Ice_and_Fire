package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityLectern;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageUpdateLectern {

    public long blockPos;
    public int selectedPages1;
    public int selectedPages2;
    public int selectedPages3;
    public boolean updateStack;
    public int pageOrdinal;

    public MessageUpdateLectern(long blockPos, int selectedPages1, int selectedPages2, int selectedPages3, boolean updateStack, int pageOrdinal) {
        this.blockPos = blockPos;
        this.selectedPages1 = selectedPages1;
        this.selectedPages2 = selectedPages2;
        this.selectedPages3 = selectedPages3;
        this.updateStack = updateStack;
        this.pageOrdinal = pageOrdinal;

    }

    public MessageUpdateLectern() {
    }

    public static MessageUpdateLectern read(FriendlyByteBuf buf) {
        return new MessageUpdateLectern(buf.readLong(), buf.readInt(), buf.readInt(), buf.readInt(), buf.readBoolean(), buf.readInt());
    }

    public static void write(MessageUpdateLectern message, FriendlyByteBuf buf) {
        buf.writeLong(message.blockPos);
        buf.writeInt(message.selectedPages1);
        buf.writeInt(message.selectedPages2);
        buf.writeInt(message.selectedPages3);
        buf.writeBoolean(message.updateStack);
        buf.writeInt(message.pageOrdinal);
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageUpdateLectern message, Supplier<NetworkEvent.Context> context) {
            context.get().setPacketHandled(true);
            Player player = context.get().getSender();
            if(context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT){
                player = IceAndFire.PROXY.getClientSidePlayer();
            }
            if (player != null) {
                if (player.level != null) {
                    BlockPos pos = BlockPos.of(message.blockPos);
                    if (player.level.getBlockEntity(pos) != null) {
                        if (player.level.getBlockEntity(pos) instanceof TileEntityLectern) {
                            TileEntityLectern lectern = (TileEntityLectern) player.level.getBlockEntity(pos);
                            if (message.updateStack) {
                                ItemStack bookStack = lectern.getItem(0);
                                if (bookStack.getItem() == IafItemRegistry.BESTIARY.get()) {
                                    EnumBestiaryPages.addPage(EnumBestiaryPages.fromInt(message.pageOrdinal), bookStack);
                                }
                                lectern.randomizePages(bookStack, lectern.getItem(1));
                            } else {
                                lectern.selectedPages[0] = EnumBestiaryPages.fromInt(message.selectedPages1);
                                lectern.selectedPages[1] = EnumBestiaryPages.fromInt(message.selectedPages2);
                                lectern.selectedPages[2] = EnumBestiaryPages.fromInt(message.selectedPages3);
                            }

                        }
                    }
                }
            }
        }
    }

}