package me.skymc.taskchain.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import me.skymc.taskchain.ITask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class SingleTask implements ITask {

    private JavaPlugin instance;

    @Getter
    private Runnable runnable;

    @Getter
    private boolean async;

    @Override
    public boolean async() {
        return async;
    }

    @Override
    public JavaPlugin getInstance() {
        return instance;
    }

    @Override
    @NotNull
    public CompletableFuture<Boolean> execute() {
        val future = new CompletableFuture<Boolean>();
        if (async) {
            Bukkit.getScheduler().runTaskAsynchronously(getInstance(), runnable);
        } else {
            Bukkit.getScheduler().runTask(getInstance(), runnable);
        }
        future.complete(true);
        return future;
    }
}