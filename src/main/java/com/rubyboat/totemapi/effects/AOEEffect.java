package com.rubyboat.totemapi.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rubyboat.totemapi.TotemAPI;
import com.rubyboat.totemapi.components.TotemEffectType;
import com.rubyboat.totemapi.effects.TotemEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class AOEEffect extends TotemEffect {
    public static final MapCodec<AOEEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.intRange(0, Integer.MAX_VALUE).fieldOf("radius").forGetter(AOEEffect::getRadius),
                    StatusEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(AOEEffect::getEffects)
            ).apply(instance, AOEEffect::new)
    );
    int radius;
    ArrayList<StatusEffectInstance> aoeEffect = new ArrayList<>();

    private AOEEffect(Integer radius, List<StatusEffectInstance> statusEffectInstances) {
        this((int) radius, new ArrayList<>(statusEffectInstances));
    }

    private ArrayList<StatusEffectInstance> getEffects() {
        return aoeEffect;
    }

    private int getRadius() {
        return radius;
    }

    public AOEEffect(int radius, StatusEffectInstance aoeEffect) {
        this.radius = radius;
        this.aoeEffect.add(aoeEffect);
    }
    public AOEEffect(int radius, ArrayList<StatusEffectInstance> aoeEffect) {
        this.radius = radius;
        this.aoeEffect = aoeEffect;
    }

    @Override
    public String getTooltip() {
        String toReturn = "";
        for(StatusEffectInstance effect : aoeEffect)
        {
            toReturn += "AOE: " + effect.getEffectType().value().getName().getString() + " " + (effect.getAmplifier() + 1) + " (" + Math.floor((double) effect.getDuration() / 20) + ") /lb";
        }
        return toReturn;
    }

    @Override
    public TotemEffectType getType() {
        return TotemAPI.AOE_EFFECT_TYPE;
    }

    @Override
    public void onDeath(LivingEntity user, World world, ItemStack stack) {
        Vec3d origin = user.getPos();
        Vec3d pos1 = origin;
        pos1 = pos1.add(radius, radius, radius);
        Vec3d pos2 = origin;
        pos2 = pos2.add(-radius, -radius, -radius);
        Box box = new Box(pos1, pos2);
        List<Entity> entityList = world.getOtherEntities(user, box);
        for(Entity ent : entityList)
        {
            if(ent instanceof LivingEntity)
            {
                LivingEntity livingent = (LivingEntity) ent;
                for(StatusEffectInstance effectInstance : aoeEffect)
                {
                    livingent.addStatusEffect(new StatusEffectInstance(effectInstance), user);
                }
            }
        }
    }
}
