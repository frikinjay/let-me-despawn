package com.frikinjay.letmedespawn.mixin;

import com.frikinjay.almanac.Almanac;
import com.frikinjay.letmedespawn.LetMeDespawn;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Mob.class)
public abstract class MobMixin extends LivingEntity {

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            at = {@At("TAIL")},
            method = {"setItemSlotAndDropWhenKilled"}
    )
    private void letmedespawn$setItemSlotAndDropWhenKilled(EquipmentSlot slot, ItemStack stack, CallbackInfo info) {
        Mob entity = (Mob) (Object) this;
        LetMeDespawn.setPersistence(entity, slot);
    }

    

    @Redirect(
            method = {"checkDespawn"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Mob;discard()V"
            )
    )
    private void letmedespawn$yeetusCheckus(Mob instance) {
        if (Almanac.pickedItems) {
            dropEquipmentOnDiscard(instance);
        }
        this.discard();
    }

    // Hook into the remove method to catch mob removal as this is needed in newer versions & can prevent against lagfixers
    @Inject(
            method = {"remove"},
            at = @At("HEAD")
    )
    private void letmedespawn$onRemove(CallbackInfo info) {
        Mob entity = (Mob) (Object) this;
        // check if they picked  up items
        if (Almanac.pickedItems) {
            // drop equiptment items
            dropEquipmentOnPickup(entity);
        }
    }

    // ported from almanac
    public static void dropEquipmentOnPickup(Mob entity) {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack itemStack = entity.getItemBySlot(equipmentSlot);
            if (!itemStack.isEmpty()) {
                // create a copy of their items and drop it
                ItemStack dropStack = itemStack.copy();
                entity.spawnAtLocation(dropStack);
            }
        }
    }

    // ported from almanac
    public static void dropEquipmentOnDiscard(Mob entity) {
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            
            ItemStack itemStack = entity.getItemBySlot(equipmentSlot);
            if (!itemStack.isEmpty()) {
                entity.spawnAtLocation(itemStack);
                entity.setItemSlot(equipmentSlot, ItemStack.EMPTY);
            }
        }
    }
}