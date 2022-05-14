package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.pathfinding.raycoms.Node;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.Pathfinding;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

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
        this.reached = reached;
    }

    public void write(final PacketBuffer buf)
    {
        buf.writeInt(reached.size());
        for (final BlockPos node : reached)
        {
            buf.writeBlockPos(node);
        }

    }

    public static MessageSyncPathReached read(final PacketBuffer buf)
    {
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
        {
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
            for (final Node node : Pathfinding.lastDebugNodesPath)
            {
                if (reached.contains(node.pos))
                {
                    node.setReachedByWorker(true);
                }
            }
            context.get().setPacketHandled(true);
        }
    }

}
