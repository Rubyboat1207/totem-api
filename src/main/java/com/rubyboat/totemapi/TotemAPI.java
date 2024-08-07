package com.rubyboat.totemapi;

import com.rubyboat.totemapi.components.TotemComponent;
import com.rubyboat.totemapi.components.TotemEffectType;
import com.rubyboat.totemapi.effects.*;
import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.component.ComponentType;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TotemAPI implements ModInitializer {
	public static String MOD_ID = "totem_api";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final TotemEffectType AND_EFFECT_TYPE = new TotemEffectType(AndEffect.CODEC);
	public static final TotemEffectType EXPLODE_EFFECT_TYPE = new TotemEffectType(ExplodeEffect.CODEC);
	public static final TotemEffectType STATUS_EFFECT_TYPE = new TotemEffectType(StatusEffectTotemEffect.CODEC);
	public static final TotemEffectType AOE_EFFECT_TYPE = new TotemEffectType(AOEEffect.CODEC);
	public static final TotemEffectType FREEZE_EFFECT_TYPE = new TotemEffectType(FreezeEffect.CODEC);
	public static final TotemEffectType GIVE_EFFECT_TYPE = new TotemEffectType(GiveEffect.CODEC);
	public static final TotemEffectType LAUNCH_EFFECT_TYPE = new TotemEffectType(LaunchEffect.CODEC);
	public static final TotemEffectType RANDOM_TELEPORT_TYPE = new TotemEffectType(RandomTeleportEffect.CODEC);
	public static final TotemEffectType FUNCTION_TYPE = new TotemEffectType(FunctionEffect.CODEC);

	public static ComponentType<TotemComponent> TOTEM_COMPONENT;

	private static <T> ComponentType<T> registerComponent(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
		return (ComponentType)Registry.register(Registries.DATA_COMPONENT_TYPE, identifierOf(id), ((ComponentType.Builder)builderOperator.apply(ComponentType.builder())).build());
	}

	@Override
	public void onInitialize() {
		Registry.register(TotemComponent.EFFECTS, TotemAPI.identifierOf( "and"), AND_EFFECT_TYPE);
		Registry.register(TotemComponent.EFFECTS, TotemAPI.identifierOf( "explode"), EXPLODE_EFFECT_TYPE);
		Registry.register(TotemComponent.EFFECTS, TotemAPI.identifierOf( "effect"), STATUS_EFFECT_TYPE);
		Registry.register(TotemComponent.EFFECTS, TotemAPI.identifierOf( "area_of_effect"), AOE_EFFECT_TYPE);
		Registry.register(TotemComponent.EFFECTS, TotemAPI.identifierOf( "freeze"), FREEZE_EFFECT_TYPE);
		Registry.register(TotemComponent.EFFECTS, TotemAPI.identifierOf( "give"), GIVE_EFFECT_TYPE);
		Registry.register(TotemComponent.EFFECTS, TotemAPI.identifierOf( "launch"), LAUNCH_EFFECT_TYPE);
		Registry.register(TotemComponent.EFFECTS, TotemAPI.identifierOf( "random_teleport"), RANDOM_TELEPORT_TYPE);
		Registry.register(TotemComponent.EFFECTS, TotemAPI.identifierOf( "function"), FUNCTION_TYPE);

		new TotemItem(new Item.Settings(), TotemAPI.identifierOf( "test_totem"), new ArrayList<StatusEffectInstance>(), 10, null, true);
		TOTEM_COMPONENT = registerComponent("totem_component", builder -> builder.codec(TotemComponent.CODEC).packetCodec(TotemComponent.PACKET_CODEC).cache());
	}



	public static Identifier identifierOf(String path) {
		return Identifier.of(MOD_ID, path);
	}
}