package totem_api.rubyboat.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import totem_api.rubyboat.effects.TotemEffect;

public record TotemEffectType(MapCodec<? extends TotemEffect> codec) {
}
