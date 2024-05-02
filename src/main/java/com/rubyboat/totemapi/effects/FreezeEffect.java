package com.rubyboat.totemapi.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rubyboat.totemapi.TotemAPI;
import com.rubyboat.totemapi.components.TotemEffectType;
import com.rubyboat.totemapi.effects.TotemEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FreezeEffect extends TotemEffect {
    public static MapCodec<FreezeEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
                Codec.INT.fieldOf("ticks").forGetter(FreezeEffect::getFreezeTicks)
        ).apply(instance, FreezeEffect::new)
    );
    int freezeTicks = 20;

    private int getFreezeTicks() {
        return freezeTicks;
    }

    public FreezeEffect(int freezeTicks)
    {
        this.freezeTicks = freezeTicks;
    }

    @Override
    public void onDeath(LivingEntity user, World world, ItemStack stack) {
        user.setFrozenTicks(freezeTicks);
    }

    @Override
    public String getTooltip() {
        return "Gives you the shivers for " + Math.floor((double)freezeTicks / 20) + " seeconds";
    }

    @Override
    public TotemEffectType getType() {
        return TotemAPI.FREEZE_EFFECT_TYPE;
    }
}
