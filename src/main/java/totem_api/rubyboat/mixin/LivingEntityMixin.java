package totem_api.rubyboat.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import totem_api.rubyboat.Main;
import totem_api.rubyboat.TotemItem;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import totem_api.rubyboat.components.TotemComponent;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Shadow public abstract void setHealth(float health);

    @Shadow public abstract boolean clearStatusEffects();

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Inject(at = @At("HEAD"), method = "tryUseTotem", cancellable = true)
    private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        var world = ((LivingEntity) ((Object) (this))).getWorld();
        if (source == world.getDamageSources().outOfWorld()) {
            if(((LivingEntity)(Object)this).isPlayer()){
                ItemStack itemStack = null;
                Hand[] var4 = Hand.values();
                int var5 = var4.length;
                TotemComponent component = null;

                for (int var6 = 0; var6 < var5; ++var6) {
                    Hand hand = var4[var6];
                    ItemStack itemStack2 = this.getStackInHand(hand);
                    TotemComponent component2 = itemStack2.get(Main.TOTEM_COMPONENT);
                    if (itemStack2.isOf(Items.TOTEM_OF_UNDYING) || itemStack2.getItem() instanceof TotemItem || component2 != null) {
                        itemStack = itemStack2.copy();
                        component = component2;
                        itemStack2.decrement(1);
                        break;
                    }
                }

                if(itemStack == null)
                {
                    cir.setReturnValue(false);
                }
                if(!(itemStack.getItem() instanceof TotemItem))
                {
                    cir.setReturnValue(false);
                }
                if(component == null) {
                    if(((TotemItem)itemStack.getItem()).canUseInVoid)
                    {
                        TotemItem totem =  (TotemItem) itemStack.getItem();
                        totem.onUse(((LivingEntity) (Object) this), itemStack);
                        world.sendEntityStatus(((LivingEntity) (Object) this), (byte) 100);
                        cir.setReturnValue(true);
                    }
                }else {
                    if(component.worksInVoid())
                    {
                        component.onUse(((LivingEntity) (Object) this), itemStack);
                        world.sendEntityStatus(((LivingEntity) (Object) this), (byte) 100);
                        cir.setReturnValue(true);
                    }
                }

            }
        }
        else {
            ItemStack itemStack = null;
            Hand[] var4 = Hand.values();
            int var5 = var4.length;
            TotemComponent component = null;

            for (int var6 = 0; var6 < var5; ++var6) {
                Hand hand = var4[var6];
                ItemStack itemStack2 = this.getStackInHand(hand);
                TotemComponent component2 = itemStack2.get(Main.TOTEM_COMPONENT);
                if (itemStack2.isOf(Items.TOTEM_OF_UNDYING) || itemStack2.getItem() instanceof TotemItem || component2 != null) {
                    itemStack = itemStack2.copy();
                    component = component2;
                    itemStack2.decrement(1);
                    break;
                }
            }
            if (itemStack != null) {
                if (itemStack.isOf(Items.TOTEM_OF_UNDYING)) {

                    this.setHealth(1.0F);
                    this.clearStatusEffects();
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 900, 1));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 100, 1));
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 800, 0));
                    world.sendEntityStatus(((LivingEntity) (Object) this), (byte) 35);
                }
                if((itemStack.getItem() instanceof TotemItem)) {
                    if(((TotemItem)itemStack.getItem()).canUseInVoid)
                    {
                        TotemItem totem =  (TotemItem) itemStack.getItem();
                        totem.onUse(((LivingEntity) (Object) this), itemStack);
                        world.sendEntityStatus(((LivingEntity) (Object) this), (byte) 100);
                        cir.setReturnValue(true);
                    }
                }else if(component != null) {
                    if(component.worksInVoid())
                    {
                        component.onUse(((LivingEntity) (Object) this), itemStack);
                        world.sendEntityStatus(((LivingEntity) (Object) this), (byte) 100);
                        cir.setReturnValue(true);
                    }
                }
            }

            cir.setReturnValue(itemStack != null);
        }
    }
}
