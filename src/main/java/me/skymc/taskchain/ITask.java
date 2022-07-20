package me.skymc.taskchain;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface ITask {

    boolean isSync();

    JavaPlugin getInstance();

    @NotNull CompletableFuture<Boolean> execute();
}