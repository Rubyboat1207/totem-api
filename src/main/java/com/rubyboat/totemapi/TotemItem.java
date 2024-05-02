package com.rubyboat.totemapi;

import com.rubyboat.totemapi.effects.TotemEffect;
import net.minecraft.client.item.TooltipType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class TotemItem extends Item {
    public ArrayList<StatusEffectInstance> effects;
    public int healthRemaining;
    public TotemEffect totemEffect;
    public static ArrayList<TotemItem> totems;
    public boolean canUseInVoid = false;

    public TotemItem(Settings settings, Identifier identifier, ArrayList<StatusEffectInstance> effects, int healthRemaining, TotemEffect nutz) {
        super(settings.maxCount(1));
        this.effects = effects;
        this.healthRemaining = healthRemaining;
        this.totemEffect = nutz;
        Registry.register(Registries.ITEM, identifier, this);
    }

    public TotemItem(Settings settings, Identifier identifier, ArrayList<StatusEffectInstance> effects, int healthRemaining, TotemEffect nutz, boolean canUseInVoid) {
        super(settings.maxCount(1));
        this.effects = effects;
        this.healthRemaining = healthRemaining;
        this.totemEffect = nutz;
        Registry.register(Registries.ITEM, identifier, this);
        this.canUseInVoid = canUseInVoid;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        ArrayList<StatusEffectInstance> goodEffect = new ArrayList<>();
        ArrayList<StatusEffectInstance> badEffect = new ArrayList<>();

        for(StatusEffectInstance effect : effects)
        {

            if(effect.getEffectType().value().isBeneficial())
            {
                goodEffect.add(effect);
            }else
            {
                badEffect.add(effect);
            }
        }
        if(goodEffect.size() > 0)
        {
            tooltip.add(Text.of("§2Beneficial Effects:"));
            for(StatusEffectInstance effect : goodEffect)
            {
                tooltip.add(Text.of("   §a" + effect.getEffectType().value().getName().getString() + " " + (effect.getAmplifier() + 1) +" (" + (effect.getDuration()/20) + "s)"));
            }
        }
        if(!badEffect.isEmpty())
        {
            tooltip.add(Text.of("§4Negative Effects:"));
            for(StatusEffectInstance effect : badEffect)
            {
                tooltip.add(Text.of("   §c" + effect.getEffectType().value().getName().getString() + " (" + (effect.getDuration()/20) + "s) lvl " + (effect.getAmplifier() + 1)));
            }
        }
        if(totemEffect != null)
        {
            tooltip.add(Text.of("§dExtra Effects:"));
            String[] tt2 = totemEffect.getTooltip().split("/lb");
            for(String str : tt2)
            {
                tooltip.add(Text.of("    §5" + str));
            }
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    public void onUse(LivingEntity user, ItemStack stack)
    {
        user.setFireTicks(0);
        user.clearStatusEffects();
        user.setAir(user.getMaxAir());
        user.setVelocity(new Vec3d(0,0,0));
        user.fallDistance = 0;
        for(StatusEffectInstance effect : effects)
        {
            user.addStatusEffect(new StatusEffectInstance(effect));
        }
        if(totemEffect != null)
        {
            totemEffect.onDeath(user, user.getWorld(), stack);
        }
        user.setHealth(healthRemaining);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if(totemEffect != null)
        {
            return totemEffect.onClick(context.getPlayer(), context.getWorld(), context.getBlockPos(), context.getStack());
        }
        return ActionResult.PASS;
    }

    public static ItemStack getActiveCustomTotem(PlayerEntity player) {
        Hand[] var1 = Hand.values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            Hand hand = var1[var3];
            ItemStack itemStack = player.getStackInHand(hand);
            if(itemStack == null) {
                return null;
            }
            if (itemStack.getItem() instanceof TotemItem || itemStack.get(TotemAPI.TOTEM_COMPONENT) != null) {
                return itemStack;
            }
        }
        return null;
    }
}
