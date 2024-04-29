package com.rubyboat.totemapi.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rubyboat.totemapi.TotemAPI;
import com.rubyboat.totemapi.components.TotemEffectType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class StatusEffectTotemEffect extends TotemEffect {
    public static final MapCodec<StatusEffectTotemEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
                StatusEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(StatusEffectTotemEffect::getStatusEffects)
        ).apply(instance, StatusEffectTotemEffect::new)
    );
    public ArrayList<StatusEffectInstance> statusEffects;

    public ArrayList<StatusEffectInstance> getStatusEffects() {
        return statusEffects;
    }

    public StatusEffectTotemEffect(List<StatusEffectInstance> statusEffects) {
        this.statusEffects = new ArrayList<>(statusEffects);
    }

    @Override
    public void onDeath(LivingEntity user, World world, ItemStack stack) {
        for(StatusEffectInstance effect : statusEffects)
        {
            user.addStatusEffect(new StatusEffectInstance(effect));
        }
    }

    @Override
    public TotemEffectType getType() {
        return TotemAPI.STATUS_EFFECT_TYPE;
    }
}
