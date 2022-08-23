package com.frikinjay.letmedespawn.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLiving.class)
public abstract class EntityLivingMixin extends EntityLivingBase {

    private boolean pickedItems = false;
    @Shadow private boolean persistenceRequired;

    public EntityLivingMixin(World p_i1594_1_) {
        super(p_i1594_1_);
    }

    @Inject(at = @At("TAIL"), method = "updateEquipmentIfNeeded")
    private void updateEquipmentIfNeeded(CallbackInfo info) {
        this.pickedItems = true;
        this.persistenceRequired = this.hasCustomName();
    }

    @Redirect(method = "despawnEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLiving;setDead()V"))
    private void yeetusCheckus(EntityLiving instance) {
        if (this.pickedItems) {
            this.dropEquipmentOnDespawn();
        }
        this.setDead();
    }

    protected void dropEquipmentOnDespawn()
    {
        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
        {
            ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);

            if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack))
            {
                this.entityDropItem(itemstack, 0.0F);
            }
        }
    }

}
