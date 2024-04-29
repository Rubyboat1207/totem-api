package com.rubyboat.totemapi;

import com.rubyboat.totemapi.components.TotemComponent;
import com.rubyboat.totemapi.components.TotemEffectType;
import com.rubyboat.totemapi.effects.AOEEffect;
import com.rubyboat.totemapi.effects.AndEffect;
import com.rubyboat.totemapi.effects.ExplodeEffect;
import com.rubyboat.totemapi.effects.StatusEffectTotemEffect;
import net.fabricmc.api.ModInitializer;

import net.minecraft.component.DataComponentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class TotemAPI implements ModInitializer {
	public static String MOD_ID = "totem_api";
	public static final TotemEffectType AND_EFFECT_TYPE = new TotemEffectType(AndEffect.CODEC);
	public static final TotemEffectType EXPLODE_EFFECT_TYPE = new TotemEffectType(ExplodeEffect.CODEC);
	public static final TotemEffectType STATUS_EFFECT_TYPE = new TotemEffectType(StatusEffectTotemEffect.CODEC);
	public static final TotemEffectType AOE_EFFECT_TYPE = new TotemEffectType(AOEEffect.CODEC);

	public static DataComponentType<TotemComponent> TOTEM_COMPONENT = registerComponent("totem_component", builder -> builder.codec(TotemComponent.CODEC).packetCodec(TotemComponent.PACKET_CODEC).cache());

	private static <T> DataComponentType<T> registerComponent(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
		return Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(MOD_ID, id), ((DataComponentType.Builder)builderOperator.apply(DataComponentType.builder())).build());
	}

	@Override
	public void onInitialize() {
		Registry.register(TotemComponent.EFFECTS, new Identifier(MOD_ID, "and"), AND_EFFECT_TYPE);
		Registry.register(TotemComponent.EFFECTS, new Identifier(MOD_ID, "explode"), EXPLODE_EFFECT_TYPE);
		Registry.register(TotemComponent.EFFECTS, new Identifier(MOD_ID, "effect"), STATUS_EFFECT_TYPE);
		Registry.register(TotemComponent.EFFECTS, new Identifier(MOD_ID, "area_of_effect"), AOE_EFFECT_TYPE);

		new TotemItem(new Item.Settings(), new Identifier(MOD_ID, "test_totem"), new ArrayList<StatusEffectInstance>(), 10, null, true);
	}
}