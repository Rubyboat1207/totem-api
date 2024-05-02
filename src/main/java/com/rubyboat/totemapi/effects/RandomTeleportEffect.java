package com.rubyboat.totemapi.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rubyboat.totemapi.components.TotemEffectType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class RandomTeleportEffect extends TotemEffect {
    public static final MapCodec<RandomTeleportEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.FLOAT.fieldOf("radius").forGetter(RandomTeleportEffect::getRadius)
        ).apply(instance, RandomTeleportEffect::new)
    );

    private float getRadius() {
        return radius;
    }

    private final float radius;

    public RandomTeleportEffect(float radius)
    {
        this.radius = radius;
    }

    @Override
    public void onDeath(LivingEntity user, World world, ItemStack stack) {
        if (!world.isClient) {
            double d = user.getX();
            double e = user.getY();
            double f = user.getZ();

            for(int i = 0; i < radius *2; ++i) {
                double g = user.getX() + (user.getRandom().nextDouble() - 0.5D) * radius *2;
                double h = MathHelper.clamp(user.getY() + (double)(user.getRandom().nextInt((int) radius * 2) - radius / 2), (double)world.getBottomY(), (double)(world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1));
                double j = user.getZ() + (user.getRandom().nextDouble() - 0.5D) * radius *2;
                if (user.hasVehicle()) {
                    user.getVehicle().teleport(g, h, j);
                    SoundEvent soundEvent = user instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                    world.playSound((PlayerEntity)null, d, e, f, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    user.playSound(soundEvent, 1.0F, 1.0F);
                    break;
                }

                if (user.teleport(g, h, j, true)) {
                    SoundEvent soundEvent = user instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                    world.playSound((PlayerEntity)null, d, e, f, soundEvent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    user.playSound(soundEvent, 1.0F, 1.0F);
                    break;
                }
            }
        }
    }

    @Override
    public String getTooltip() {
        return "Teleports you within a radius of " + radius;
    }

    @Override
    public TotemEffectType getType() {
        return null;
    }
}
