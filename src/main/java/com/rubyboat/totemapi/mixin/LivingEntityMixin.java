package com.rubyboat.totemapi.mixin;

import com.rubyboat.totemapi.TotemAPI;
import com.rubyboat.totemapi.TotemItem;
import com.rubyboat.totemapi.components.TotemComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Shadow public abstract void setHealth(float health);

    @Shadow public abstract boolean clearStatusEffects();

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Inject(at = @At("HEAD"), method = "tryUseTotem", cancellable = true)
    private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;
        var world = entity.getWorld();
        ItemStack itemStack = null;
        TotemComponent component = null;

        for (Hand hand : Hand.values()) {
            ItemStack stack = getStackInHand(hand);
            component = stack.get(TotemAPI.TOTEM_COMPONENT);
            if (stack.isOf(Items.TOTEM_OF_UNDYING) || stack.getItem() instanceof TotemItem || component != null) {
                itemStack = stack.copy();
                stack.decrement(1);
                break;
            }
        }

        if (itemStack == null) {
            cir.setReturnValue(false);
            return;
        }

        boolean handled = false;
        if (source == world.getDamageSources().outOfWorld()) {
            if (entity.isPlayer()) {
                if (component != null && component.worksInVoid()) {
                    component.onUse(entity, itemStack);
                    world.sendEntityStatus(entity, (byte) 100);
                    handled = true;
                } else if (itemStack.getItem() instanceof TotemItem && ((TotemItem)itemStack.getItem()).canUseInVoid) {
                    ((TotemItem)itemStack.getItem()).onUse(entity, itemStack);
                    world.sendEntityStatus(entity, (byte) 100);
                    handled = true;
                }
            }
        } else {
            if (itemStack.isOf(Items.TOTEM_OF_UNDYING)) {
                setHealth(1.0F);
                clearStatusEffects();
                addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
                addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
                addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
                world.sendEntityStatus(entity, (byte) 35);
                handled = true;
            } else if (component != null) {
                component.onUse(entity, itemStack);
                world.sendEntityStatus(entity, (byte) 100);
                handled = true;
            } else if (itemStack.getItem() instanceof TotemItem) {
                ((TotemItem)itemStack.getItem()).onUse(entity, itemStack);
                world.sendEntityStatus(entity, (byte) 100);
                handled = true;
            }
        }

        cir.setReturnValue(handled);
    }
}
