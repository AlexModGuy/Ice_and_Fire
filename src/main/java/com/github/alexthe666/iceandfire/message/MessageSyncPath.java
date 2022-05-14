package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.pathfinding.raycoms.Node;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.Pathfinding;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

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
    public static Set<Node> lastDebugNodesVisited = new HashSet<>();

    /**
     * Set of not visited nodes.
     */
    public static Set<Node> lastDebugNodesNotVisited  = new HashSet<>();

    /**
     * Set of chosen nodes for the path.
     */
    public static Set<Node> lastDebugNodesPath  = new HashSet<>();

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
    public MessageSyncPath(final Set<Node> lastDebugNodesVisited, final Set<Node> lastDebugNodesNotVisited, final Set<Node>  lastDebugNodesPath)
    {
        super();
        this.lastDebugNodesVisited = lastDebugNodesVisited;
        this.lastDebugNodesNotVisited = lastDebugNodesNotVisited;
        this.lastDebugNodesPath = lastDebugNodesPath;
    }

    public void write(final PacketBuffer buf)
    {
        buf.writeInt(lastDebugNodesVisited.size());
        for (final Node node : lastDebugNodesVisited)
        {
            node.serializeToBuf(buf);
        }

        buf.writeInt(lastDebugNodesNotVisited.size());
        for (final Node node : lastDebugNodesNotVisited)
        {
            node.serializeToBuf(buf);
        }

        buf.writeInt(lastDebugNodesPath.size());
        for (final Node node : lastDebugNodesPath)
        {
            node.serializeToBuf(buf);
        }
    }

    public static MessageSyncPath read(final PacketBuffer buf)
    {
        int size = buf.readInt();
        for (int i = 0; i < size; i++)
        {
            lastDebugNodesVisited.add(new Node(buf));
        }

        size = buf.readInt();
        for (int i = 0; i < size; i++)
        {
            lastDebugNodesNotVisited.add(new Node(buf));
        }

        size = buf.readInt();
        for (int i = 0; i < size; i++)
        {
            lastDebugNodesPath.add(new Node(buf));
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
