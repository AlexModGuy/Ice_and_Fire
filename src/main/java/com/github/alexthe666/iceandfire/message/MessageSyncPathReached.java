package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.pathfinding.raycoms.MNode;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.Pathfinding;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Message to sync the reached positions over to the client for rendering.
 */
public class MessageSyncPathReached
{
    /**
     * Set of reached positions.
     */
    public static Set<BlockPos> reached = new HashSet<>();

    /**
     * Default constructor.
     */
    public MessageSyncPathReached()
    {
        super();
    }

    /**
     * Create the message to send a set of positions over to the client side.
     *
     */
    public MessageSyncPathReached(final Set<BlockPos> reached)
    {
        super();
        MessageSyncPathReached.reached = reached;
    }

    public void write(final FriendlyByteBuf buf) {
        buf.writeInt(reached.size());
        for (final BlockPos node : reached) {
            buf.writeBlockPos(node);
        }

    }

    public static MessageSyncPathReached read(final FriendlyByteBuf buf) {
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            reached.add(buf.readBlockPos());
        }
        return new MessageSyncPathReached();
    }

    public LogicalSide getExecutionSide()
    {
        return LogicalSide.CLIENT;
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageSyncPathReached message, Supplier<NetworkEvent.Context> context) {
            for (final MNode MNode : Pathfinding.lastDebugNodesPath) {
                if (reached.contains(MNode.pos)) {
                    MNode.setReachedByWorker(true);
                }
            }
            context.get().setPacketHandled(true);
        }
    }

}
