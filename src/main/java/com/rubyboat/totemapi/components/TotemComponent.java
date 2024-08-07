package com.rubyboat.totemapi.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rubyboat.totemapi.TotemAPI;
import com.rubyboat.totemapi.effects.TotemEffect;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public record TotemComponent(TotemEffect effect, boolean worksInVoid, int heartsRemaining) {
    public static Registry<TotemEffectType> EFFECTS = new SimpleRegistry<>(RegistryKey.ofRegistry(TotemAPI.identifierOf( "effects")), Lifecycle.stable(), false);
    public static Codec<TotemComponent> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                TotemEffect.CODEC.fieldOf("effect").forGetter(TotemComponent::effect),
                Codec.BOOL.fieldOf("works_in_void").orElse(false).forGetter(TotemComponent::worksInVoid),
                Codec.INT.fieldOf("hearts_remaining").forGetter(TotemComponent::heartsRemaining)
        ).apply(instance, TotemComponent::new)
    );
    public static PacketCodec<ByteBuf, TotemComponent> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.codec(TotemEffect.CODEC), TotemComponent::effect, PacketCodecs.codec(Codec.BOOL), TotemComponent::worksInVoid, PacketCodecs.codec(Codec.INT), TotemComponent::heartsRemaining, TotemComponent::new);


    public void onUse(LivingEntity user, ItemStack stack) {
        user.setFireTicks(0);
        user.clearStatusEffects();
        user.setAir(user.getMaxAir());
        user.setVelocity(new Vec3d(0,0,0));
        user.fallDistance = 0;
        effect.onDeath(user, user.getWorld(), stack);
        user.setHealth(heartsRemaining);
    }
}
