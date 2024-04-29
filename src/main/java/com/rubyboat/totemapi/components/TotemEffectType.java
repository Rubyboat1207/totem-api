package com.rubyboat.totemapi.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.rubyboat.totemapi.effects.TotemEffect;

public record TotemEffectType(MapCodec<? extends TotemEffect> codec) {
}
