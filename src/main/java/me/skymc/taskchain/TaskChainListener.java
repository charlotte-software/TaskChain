package me.skymc.taskchain;

import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

@AllArgsConstructor
public class TaskChainListener implements Listener {

    private final TaskChainFactory factory;

    @EventHandler
    public void onServerClose(PluginDisableEvent event) {
        if (event.getPlugin().getName().equals(factory.getInstance().getName())) {
            factory.getScheduler().shutdownNow();
        }
    }
}