package com.bobomico.quartz.plugin;

import com.bobomico.quartz.utils.HookPriority;
import com.bobomico.quartz.utils.ShutdownHookManager;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: com.bobomico.quartz.stevexie.plugin.mall-bobomico-B
 * @URL: https://github.com/huangyueranbbc/QuartzUtils/blob/QuartzUtils_v1.0.0/src/main/java/com/hyr/quartz/plugin/QuartzShutdownHookPlugin.java
 * @Author: DELL
 * @Date: 2019/4/30  12:18
 * @Description: Quartz ShutdownHook插件
 * @version:
 */

public class QuartzShutdownHookPlugin implements SchedulerPlugin {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Data members.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private boolean cleanShutdown = true;

    private final Logger log = LoggerFactory.getLogger(getClass());

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public QuartzShutdownHookPlugin() {
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Interface.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * Determine whether or not the plug-in is configured to cause a clean
     * shutdown of the scheduler.
     *
     * <p>
     * The default value is <code>true</code>.
     * </p>
     *
     * @see Scheduler#shutdown(boolean)
     */
    public boolean isCleanShutdown() {
        return cleanShutdown;
    }

    /**
     * Set whether or not the plug-in is configured to cause a clean shutdown
     * of the scheduler.
     *
     * <p>
     * The default value is <code>true</code>.
     * </p>
     *
     * @see Scheduler#shutdown(boolean)
     */
    public void setCleanShutdown(boolean b) {
        cleanShutdown = b;
    }

    protected Logger getLog() {
        return log;
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * SchedulerPlugin Interface.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Called during creation of the <code>Scheduler</code> in order to give
     * the <code>SchedulerPlugin</code> a chance to initialize.
     * </p>
     *
     * @throws SchedulerException if there is an error initializing.
     */
    public void initialize(String name, final Scheduler scheduler, ClassLoadHelper classLoadHelper)
            throws SchedulerException {

        getLog().info("Registering Quartz shutdown hook.");

        Thread t = new Thread("Quartz Shutdown-Hook "
                + scheduler.getSchedulerName()) {
            @Override
            public void run() {
                getLog().info("Shutting down Quartz...");
                try {
                    synchronized (scheduler){
                        if (!scheduler.isShutdown()) {
                            scheduler.shutdown(isCleanShutdown());
                        }
                    }
                } catch (SchedulerException e) {
                    getLog().error(
                            "Error shutting down Quartz: " + e.getMessage(), e);
                }
            }
        };

        ShutdownHookManager.get().addShutdownHook(t, HookPriority.SCHEDULER_PRIORITY.value());
    }

    public void start() {
        // do nothing.
    }

    /**
     * <p>
     * Called in order to inform the <code>SchedulerPlugin</code> that it
     * should free up all of it's resources because the scheduler is shutting
     * down.
     * </p>
     */
    public void shutdown() {
        // nothing to do in this case (since the scheduler is already shutting
        // down)
    }
}

// EOF