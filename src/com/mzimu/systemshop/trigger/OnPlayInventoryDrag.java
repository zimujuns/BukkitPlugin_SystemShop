package com.mzimu.systemshop.trigger;

import com.mzimu.systemshop.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.Plugin;

public class OnPlayInventoryDrag implements Listener {
    private Plugin plugin;

    public OnPlayInventoryDrag(Plugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void main(InventoryDragEvent inventoryDragEvent){
        if (inventoryDragEvent.getInventory().getTitle().equalsIgnoreCase(Main.titleInventory)){
            inventoryDragEvent.setCancelled(true);
        }
    }
}
