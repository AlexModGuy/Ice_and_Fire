package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.pathfinding.raycoms.MNode;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.Pathfinding;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Message to sync some path over to the client.
 */
public class MessageSyncPath
{
    /**
     * Set of visited nodes.
     */
    public static Set<MNode> lastDebugNodesVisited = new HashSet<>();

    /**
     * Set of not visited nodes.
     */
    public static Set<MNode> lastDebugNodesNotVisited = new HashSet<>();

    /**
     * Set of chosen nodes for the path.
     */
    public static Set<MNode> lastDebugNodesPath = new HashSet<>();

    /**
     * Default constructor.
     */
    public MessageSyncPath()
    {
        super();
    }

    /**
     * Create a new path message with the filled pathpoints.
     */
    public MessageSyncPath(final Set<MNode> lastDebugNodesVisited, final Set<MNode> lastDebugNodesNotVisited, final Set<MNode> lastDebugNodesPath) {
        super();
        MessageSyncPath.lastDebugNodesVisited = lastDebugNodesVisited;
        MessageSyncPath.lastDebugNodesNotVisited = lastDebugNodesNotVisited;
        MessageSyncPath.lastDebugNodesPath = lastDebugNodesPath;
    }

    public void write(final FriendlyByteBuf buf) {
        buf.writeInt(lastDebugNodesVisited.size());
        for (final MNode MNode : lastDebugNodesVisited) {
            MNode.serializeToBuf(buf);
        }

        buf.writeInt(lastDebugNodesNotVisited.size());
        for (final MNode MNode : lastDebugNodesNotVisited) {
            MNode.serializeToBuf(buf);
        }

        buf.writeInt(lastDebugNodesPath.size());
        for (final MNode MNode : lastDebugNodesPath) {
            MNode.serializeToBuf(buf);
        }
    }

    public static MessageSyncPath read(final FriendlyByteBuf buf) {
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            lastDebugNodesVisited.add(new MNode(buf));
        }

        size = buf.readInt();
        for (int i = 0; i < size; i++) {
            lastDebugNodesNotVisited.add(new MNode(buf));
        }

        size = buf.readInt();
        for (int i = 0; i < size; i++) {
            lastDebugNodesPath.add(new MNode(buf));
        }
        return new MessageSyncPath();
    }

    public static class Handler {
        public Handler() {
        }

        public static void handle(MessageSyncPath message, Supplier<NetworkEvent.Context> context) {
            Pathfinding.lastDebugNodesVisited = lastDebugNodesVisited;
            Pathfinding.lastDebugNodesNotVisited = lastDebugNodesNotVisited;
            Pathfinding.lastDebugNodesPath = lastDebugNodesPath;
            context.get().setPacketHandled(true);
        }
    }

    public LogicalSide getExecutionSide()
    {
        return LogicalSide.CLIENT;
    }

}
