package com.frikinjay.lmd.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    private boolean pickedItems = false;
    @Shadow private boolean persistent;

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("TAIL"), method = "equipLootStack")
    private void equipLootStack(CallbackInfo info) {
        this.pickedItems = true;
        this.persistent = false;
    }

    @Redirect(method = "checkDespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;discard()V"))
    private void yeetusCheckus(MobEntity instance) {
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
            ItemStack itemStack = this.getEquippedStack(equipmentSlot);
            if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack)) {
                this.dropStack(itemStack);
                this.equipStack(equipmentSlot, ItemStack.EMPTY);
            }
        }
    }

}
