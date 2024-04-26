package totemlib.rubyboat;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class Main implements ModInitializer {
    public static String MOD_ID = "totemapi";
    public static DamageSource VOID = new DamageSource("totemapi.void".toString());
    @Override
    public void onInitialize() {
        new TotemItem(new FabricItemSettings(), new Identifier(MOD_ID, "test_totem"), new ArrayList<StatusEffectInstance>(), 10, null, true);
    }
}
