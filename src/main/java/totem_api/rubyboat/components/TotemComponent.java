package totem_api.rubyboat.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import totem_api.rubyboat.Main;
import totem_api.rubyboat.effects.TotemEffect;

import java.util.ArrayList;

public record TotemComponent(TotemEffect effect, boolean worksInVoid, int heartsRemaining) {
    public static Registry<TotemEffectType> EFFECTS = new SimpleRegistry<>(RegistryKey.ofRegistry(new Identifier(Main.MOD_ID, "effects")), Lifecycle.stable(), false);
    public static Codec<TotemComponent> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                TotemEffect.CODEC.fieldOf("effect").forGetter(TotemComponent::effect),
                Codec.BOOL.fieldOf("works_in_void").forGetter(TotemComponent::worksInVoid),
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
