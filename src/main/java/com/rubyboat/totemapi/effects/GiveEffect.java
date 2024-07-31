package com.rubyboat.totemapi.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rubyboat.totemapi.TotemAPI;
import com.rubyboat.totemapi.components.TotemEffectType;
import com.rubyboat.totemapi.effects.TotemEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GiveEffect extends TotemEffect {
    public static final MapCodec<GiveEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
                ItemStack.CODEC.fieldOf("item").forGetter(GiveEffect::getItem)
        ).apply(instance, GiveEffect::new)
    );
    private final ItemStack item;

    private ItemStack getItem() {
        return item;
    }

    public GiveEffect(ItemStack item)
    {
        this.item = item;
    }


    @Override
    public void onDeath(LivingEntity user, World world, ItemStack stack) {
        if(user.isPlayer())
        {
            ((PlayerEntity) user).getInventory().insertStack(item.copy());
        }
    }

    @Override
    public String getTooltip() {
        return "gives the user " + (item.getCount() == 1 ? "a" : item.getCount()) + " " + item.getName().getString();
    }

    @Override
    public TotemEffectType getType() {
        return TotemAPI.GIVE_EFFECT_TYPE;
    }
}
