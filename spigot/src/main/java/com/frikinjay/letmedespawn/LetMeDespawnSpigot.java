package com.frikinjay.letmedespawn.spigot;

import com.frikinjay.letmedespawn.LetMeDespawn;
import org.bukkit.plugin.java.JavaPlugin;

public final class LetMeDespawnSpigot extends JavaPlugin {
    @Override
    public void onEnable() {
        // This code runs when the plugin is enabled on the server.
        // Run our common setup.
        LetMeDespawn.init();
        
        // Register our event listener for villager despawns (in specific cases like lagfixers)
        getServer().getPluginManager().registerEvents(new MobPickupListener(), this);
        
        // Register command executor
        getCommand("letmedespawn").setExecutor(new LetMeDespawnCommandExecutor());
        
        getLogger().info("LetMeDespawn Spigot plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("LetMeDespawn Spigot plugin disabled!");
    }
}
