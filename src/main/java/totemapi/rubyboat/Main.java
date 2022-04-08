package totemapi.rubyboat;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class Main implements ModInitializer {
    public static String MOD_ID = "totem_api";
    //StatusEffects

    @Override
    public void onInitialize() {
        //misc registry
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("activateTotem").executes(context -> {
                ServerCommandSource source = (ServerCommandSource) context.getSource();
                source.getPlayer().damage(DamageSource.GENERIC, 90000);
                return 1;
            }));
        });
    }
}
