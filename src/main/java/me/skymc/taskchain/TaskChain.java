package me.skymc.taskchain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import me.skymc.taskchain.impl.PureDelayedTask;
import me.skymc.taskchain.impl.RepeatedTask;
import me.skymc.taskchain.impl.SingleTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnusedReturnValue")
@AllArgsConstructor
public class TaskChain {

    JavaPlugin instance;

    ScheduledExecutorService scheduler;

    @Getter
    Queue<ITask> tasks;

    @NotNull
    public TaskChain delay(long delay) {
        tasks.add(new PureDelayedTask(instance, delay));
        return this;
    }

    @NotNull
    public TaskChain delayedTask(@NotNull Runnable runnable, long delay) {
        delay(delay);
        task(runnable);
        return this;
    }

    @NotNull
    public TaskChain delayedTask(@NotNull Runnable runnable, long delay, boolean async) {
        delay(delay);
        task(runnable, async);
        return this;
    }

    @NotNull
    public TaskChain task(@NotNull Runnable runnable) {
        tasks.add(new SingleTask(instance, runnable, false));
        return this;
    }

    @NotNull
    public TaskChain task(@NotNull Runnable runnable, boolean async) {
        tasks.add(new SingleTask(instance, runnable, async));
        return this;
    }

    @NotNull
    public TaskChain repeatedTask(@NotNull Runnable runnable, @NotNull Callable<Boolean> predicate, long period) {
        tasks.add(new RepeatedTask(instance, runnable, predicate, period, false));
        return this;
    }

    @NotNull
    public TaskChain repeatedTask(@NotNull Runnable runnable, @NotNull Callable<Boolean> predicate, long period, boolean async) {
        tasks.add(new RepeatedTask(instance, runnable, predicate, period, async));
        return this;
    }

    public void execute() {
        scheduler.execute(() -> {
            val task = tasks.poll();
            if (task == null) {
                return;
            }
            if (task instanceof PureDelayedTask) {
                val delayedTask = (PureDelayedTask) task;
                scheduler.schedule(this::execute, delayedTask.getDelay() / 20, TimeUnit.SECONDS);
                return;
            }
            task.execute().whenCompleteAsync((bool, throwable) -> {
                if (bool) {
                    execute();
                }
            });
        });
    }
}