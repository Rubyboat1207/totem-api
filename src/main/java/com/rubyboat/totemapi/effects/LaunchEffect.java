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

public class LaunchEffect extends TotemEffect {
    public static final MapCodec<LaunchEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
                Codec.DOUBLE.fieldOf("velocity").forGetter(LaunchEffect::getVelocity)
        ).apply(instance, LaunchEffect::new)
    );

    public double getVelocity() {
        return velocity;
    }
    private final double velocity;

    public LaunchEffect(double velocity)
    {
        this.velocity = velocity;
    }


    @Override
    public void onDeath(LivingEntity user, World world, ItemStack stack) {
        super.onDeath(user, world, stack);
        user.addVelocity(0,velocity,0);
    }

    @Override
    public String getTooltip() {
        return "launches you with a force of " + velocity;
    }

    @Override
    public TotemEffectType getType() {
        return TotemAPI.LAUNCH_EFFECT_TYPE;
    }
}
