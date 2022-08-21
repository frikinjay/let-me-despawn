package com.frikinjay.lmd.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {

    private boolean pickedItems = false;
    @Shadow private boolean persistenceRequired;

    protected MobMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(at = @At("TAIL"), method = "setItemSlotAndDropWhenKilled")
    private void setItemSlotAndDropWhenKilled(CallbackInfo info) {
        this.pickedItems = true;
        this.persistenceRequired = false;
    }

    @Redirect(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;discard()V"))
    private void yeetusCheckus(Mob instance) {
        if (this.pickedItems) {
            this.dropEquipmentOnDespawn();
            this.discard();
        } else {
            this.discard();
        }
    }

    protected void dropEquipmentOnDespawn() {
        EquipmentSlot[] var4 = EquipmentSlot.values();

        for (EquipmentSlot equipmentSlot : var4) {
            ItemStack itemStack = this.getItemBySlot(equipmentSlot);
            if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack)) {
                this.spawnAtLocation(itemStack);
                this.setItemSlot(equipmentSlot, ItemStack.EMPTY);
            }
        }
    }

}
