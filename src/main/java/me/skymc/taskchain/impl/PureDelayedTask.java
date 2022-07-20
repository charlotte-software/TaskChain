package me.skymc.taskchain.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import me.skymc.taskchain.ITask;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class PureDelayedTask implements ITask {

    private JavaPlugin instance;

    @Getter
    private long delay;

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public JavaPlugin getInstance() {
        return instance;
    }

    @Override
    @NotNull
    public CompletableFuture<Boolean> execute() {
        val future = new CompletableFuture<Boolean>();
        future.complete(true);
        return future;
    }
}