package com.github.alexthe666.iceandfire.pathfinding.raycoms;
/*
    All of this code is used with permission from Raycoms, one of the developers of the minecolonies project.
 */

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.pathfinding.raycoms.pathjobs.AbstractPathJob;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.common.util.LogicalSidedProvider;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;


/**
 * Static class the handles all the Pathfinding.
 */
public final class Pathfinding {
    private static final BlockingQueue<Runnable> jobQueue = new LinkedBlockingDeque<>();
    private static ThreadPoolExecutor executor;

    /**
     * Set of visited nodes.
     */
    public static Set<MNode> lastDebugNodesVisited = new HashSet<>();

    /**
     * Set of not visited nodes.
     */
    public static Set<MNode> lastDebugNodesNotVisited = new HashSet<>();

    /**
     * Set of nodes that belong to the chosen path.
     */
    public static Set<MNode> lastDebugNodesPath = new HashSet<>();

    private Pathfinding() {
        //Hides default constructor.
    }

    public static boolean isDebug() {
        return IafConfig.pathfindingDebug;
    }

    /**
     * Creates a new thread pool for pathfinding jobs
     *
     * @return the threadpool executor.
     */
    public static ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(1, IafConfig.dragonPathfindingThreads, 10, TimeUnit.SECONDS, jobQueue, new IafThreadFactory());
        }
        return executor;
    }

    /**
     * Stops all running threads in this thread pool
     */
    public static void shutdown() {
        getExecutor().shutdownNow();
        jobQueue.clear();
        executor = null;
    }

    /**
     * Add a job to the queue for processing.
     *
     * @param job PathJob
     */
    public static void enqueue(final AbstractPathJob job)
    {
        job.getResult().startJob(getExecutor());
    }

    /**
     * Ice and Fire specific thread factory.
     */
    public static class IafThreadFactory implements ThreadFactory {
        /**
         * Ongoing thread IDs.
         */
        public static int id;

        @Override
        public Thread newThread(final Runnable runnable) {
            BlockableEventLoop<?> workqueue = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
            ClassLoader classLoader;
            if (workqueue.isSameThread()) {
                classLoader = Thread.currentThread().getContextClassLoader();
            } else {
                classLoader = CompletableFuture.supplyAsync(() -> Thread.currentThread().getContextClassLoader(), workqueue).join();
            }
            final Thread thread = new Thread(runnable, "Ice and Fire Pathfinding Worker #" + (id++));
            thread.setDaemon(true);
            thread.setPriority(Thread.MAX_PRIORITY);
            if (thread.getContextClassLoader() != classLoader) {
                IceAndFire.LOGGER.info("Corrected CCL of new Ice and Fire Pathfinding Thread, was: " + thread.getContextClassLoader().toString());
                thread.setContextClassLoader(classLoader);
            }
            thread.setUncaughtExceptionHandler((thread1, throwable) -> IceAndFire.LOGGER.error("Ice and Fire Pathfinding Thread errored! ", throwable));
            return thread;
        }
    }
}
