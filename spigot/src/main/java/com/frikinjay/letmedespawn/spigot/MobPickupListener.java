package com.frikinjay.letmedespawn.spigot;

import com.frikinjay.almanac.Almanac;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class MobPickupListener implements Listener {
    
    @EventHandler
    public void onEntityRemove(EntityRemoveEvent event) {
        Entity entity = event.getEntity();
                
        // check if it was an interactable mob
        if (Almanac.pickedItems && (entity instanceof Villager || entity instanceof ZombieVillager)) {
            
            // check if it was removed from a conflicting plugin (to prevent lagfixers from making you loose items)
            if (event.getCause() == EntityRemoveEvent.Cause.PLUGIN) {
                
                dropEquipmentOnPickup((LivingEntity) entity);
            }
        }
    }
    
    public static void dropEquipmentOnPickup(LivingEntity entity) {
        
        EntityEquipment equipment = entity.getEquipment();
        if (equipment != null) {
            // drop every piece on them
            dropItemIfPresent(entity, equipment.getHelmet(), "HELMET");
            dropItemIfPresent(entity, equipment.getChestplate(), "CHESTPLATE");  
            dropItemIfPresent(entity, equipment.getLeggings(), "LEGGINGS");
            dropItemIfPresent(entity, equipment.getBoots(), "BOOTS");
            dropItemIfPresent(entity, equipment.getItemInMainHand(), "MAIN_HAND");
            dropItemIfPresent(entity, equipment.getItemInOffHand(), "OFF_HAND");
        }
    }
    
    private static void dropItemIfPresent(LivingEntity entity, ItemStack item, String slot) {
        if (item != null && !item.getType().isAir()) {
            ItemStack dropStack = item.clone();
            entity.getWorld().dropItemNaturally(entity.getLocation(), dropStack);
        }
    }
}