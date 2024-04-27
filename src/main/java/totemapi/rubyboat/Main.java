package totemapi.rubyboat;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class Main implements ModInitializer {
    public static String MOD_ID = "totemapi";
    @Override
    public void onInitialize() {
        new TotemItem(new Item.Settings(), new Identifier(MOD_ID, "test_totem"), new ArrayList<StatusEffectInstance>(), 10, null, true);
    }
}
