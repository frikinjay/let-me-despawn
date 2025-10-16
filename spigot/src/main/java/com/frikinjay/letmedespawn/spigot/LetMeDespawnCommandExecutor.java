package com.frikinjay.letmedespawn.spigot;

import com.frikinjay.letmedespawn.LetMeDespawn;
import com.frikinjay.letmedespawn.config.LetMeDespawnConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class LetMeDespawnCommandExecutor implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("letmedespawn") || command.getName().equalsIgnoreCase("lmd")) {
            
            // Check if sender has permission (op or specific permission)
            if (!sender.hasPermission("letmedespawn.admin") && !sender.isOp()) {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
                return true;
            }
            
            if (args.length == 0) {
                // Show help/status
                sender.sendMessage(ChatColor.GREEN + "LetMeDespawn v1.4.4");
                sender.sendMessage(ChatColor.YELLOW + "Commands:");
                sender.sendMessage(ChatColor.YELLOW + "/letmedespawn reload - Reload configuration");
                sender.sendMessage(ChatColor.YELLOW + "/letmedespawn config - Show current config");
                return true;
            }
            
            if (args[0].equalsIgnoreCase("reload")) {
                try {
                    LetMeDespawn.config = LetMeDespawnConfig.load();
                    sender.sendMessage(ChatColor.GREEN + "LetMeDespawn configuration reloaded!");
                } catch (Exception e) {
                    sender.sendMessage(ChatColor.RED + "Failed to reload config: " + e.getMessage());
                }
                return true;
            }
            
            if (args[0].equalsIgnoreCase("config")) {
                sender.sendMessage(ChatColor.GREEN + "LetMeDespawn Configuration:");
                sender.sendMessage(ChatColor.YELLOW + "Mob names: " + LetMeDespawn.config.getMobNames());
                sender.sendMessage(ChatColor.YELLOW + "Config file: " + LetMeDespawn.CONFIG_FILE.getAbsolutePath());
                return true;
            }
            
            // Unknown subcommand
            sender.sendMessage(ChatColor.RED + "Unknown command. Use /letmedespawn for help.");
            return true;
        }
        
        return false;
    }
}