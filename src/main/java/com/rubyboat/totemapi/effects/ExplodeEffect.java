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

public class ExplodeEffect extends TotemEffect {
    public static final MapCodec<ExplodeEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
                Codec.INT.fieldOf("power").forGetter(ExplodeEffect::getPower)
        ).apply(instance, ExplodeEffect::new)
    );

    public int power;

    private int getPower() {
        return power;
    }

    public ExplodeEffect(int power){
        this.power = power;
    }

    public void onDeath(LivingEntity user, World world, ItemStack stack) {
        user.setInvulnerable(true);
        world.createExplosion(user, user.getX(), user.getY(), user.getZ(), power, World.ExplosionSourceType.TNT);
        user.setInvulnerable(false);
    }

    @Override
    public String getTooltip() {
        return "Explodes with a power level of " + power;
    }

    @Override
    public TotemEffectType getType() {
        return TotemAPI.EXPLODE_EFFECT_TYPE;
    }
}
