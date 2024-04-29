package com.rubyboat.totemapi.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rubyboat.totemapi.TotemAPI;
import com.rubyboat.totemapi.components.TotemEffectType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class AndEffect extends TotemEffect {
    public static final MapCodec<AndEffect> CODEC = RecordCodecBuilder.mapCodec((instance) ->
        instance.group(
                TotemEffect.CODEC.fieldOf("first").forGetter(AndEffect::first),
                TotemEffect.CODEC.fieldOf("second").forGetter(AndEffect::second)
        ).apply(instance, AndEffect::new)
    );

    TotemEffect effect1;
    TotemEffect effect2;

    private TotemEffect first() {
        return effect1;
    }

    private  TotemEffect second() {
        return effect2;
    }


    public AndEffect(@NotNull TotemEffect effect1, @NotNull TotemEffect effect2)
    {
        this.effect1 = effect1;
        this.effect2 = effect2;
    }

    @Override
    public void onDeath(LivingEntity user, World world, ItemStack stack) {
        effect1.onDeath(user, world, stack);
        effect2.onDeath(user, world, stack);
        super.onDeath(user, world, stack);
    }

    @Override
    public ActionResult onClick(LivingEntity user, World world, BlockPos selectedBlock, ItemStack stack) {
        effect1.onClick(user, world, selectedBlock, stack);
        effect2.onClick(user, world, selectedBlock, stack);
        return super.onClick(user, world, selectedBlock, stack);
    }

    @Override
    public String getTooltip() {
        return effect1.getTooltip() + "/lb" + effect2.getTooltip();
    }

    @Override
    public TotemEffectType getType() {
        return TotemAPI.AND_EFFECT_TYPE;
    }
}
