package totem_api.rubyboat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.component.DataComponentType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import totem_api.rubyboat.components.TotemComponent;
import totem_api.rubyboat.components.TotemEffectType;
import totem_api.rubyboat.effects.AndEffect;
import totem_api.rubyboat.effects.ExplodeEffect;

import java.util.ArrayList;
import java.util.function.UnaryOperator;

public class Main implements ModInitializer {
    public static String MOD_ID = "totem_api";
    public TotemEffectType AND_EFFECT_TYPE = new TotemEffectType(AndEffect.CODEC);
    public TotemEffectType EXPLODE_EFFECT_TYPE = new TotemEffectType(ExplodeEffect.CODEC);

    public static DataComponentType<TotemComponent> TOTEM_COMPONENT = registerComponent("totem_component", builder -> builder.codec(TotemComponent.CODEC).packetCodec(TotemComponent.PACKET_CODEC).cache());

    private static <T> DataComponentType<T> registerComponent(String id, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, new Identifier(MOD_ID, id), ((DataComponentType.Builder)builderOperator.apply(DataComponentType.builder())).build());
    }

    @Override
    public void onInitialize() {
        Registry.register(TotemComponent.EFFECTS, new Identifier(MOD_ID, "and"), AND_EFFECT_TYPE);
        Registry.register(TotemComponent.EFFECTS, new Identifier(MOD_ID, "explode"), EXPLODE_EFFECT_TYPE);

        new TotemItem(new Item.Settings(), new Identifier(MOD_ID, "test_totem"), new ArrayList<StatusEffectInstance>(), 10, null, true);
    }
}
