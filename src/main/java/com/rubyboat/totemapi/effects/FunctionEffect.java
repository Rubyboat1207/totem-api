package com.rubyboat.totemapi.effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rubyboat.totemapi.TotemAPI;
import com.rubyboat.totemapi.components.TotemEffectType;
import net.minecraft.command.CommandExecutionContext;
import net.minecraft.command.ReturnValueConsumer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.MacroException;
import net.minecraft.server.function.Procedure;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import static com.rubyboat.totemapi.TotemAPI.FUNCTION_TYPE;

public class FunctionEffect extends TotemEffect {
    public static final MapCodec<FunctionEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
                Identifier.CODEC.fieldOf("function").forGetter(FunctionEffect::getFunctionIdentifier),
                NbtCompound.CODEC.fieldOf("arguments").orElse(new NbtCompound()).forGetter(FunctionEffect::getArguments)
        ).apply(instance, FunctionEffect::new)
    );
    private final Identifier functionIdentifier;
    private final NbtCompound arguments;

    public FunctionEffect(Identifier function_identifier, NbtCompound arguments) {
        this.functionIdentifier = function_identifier;
        this.arguments = arguments;
    }

    private Identifier getFunctionIdentifier() {
        return functionIdentifier;
    }

    private NbtCompound getArguments() {
        return arguments;
    }

    @Override
    public void onDeath(LivingEntity user, World world, ItemStack stack) {
        MinecraftServer server = world.getServer();
        if(server == null) { // literally will never happen
            TotemAPI.LOGGER.atError().log("Good Luck.");
            return;
        }
        var source = server.getCommandSource();

        var funcOpt = server.getCommandFunctionManager().getFunction(functionIdentifier);
        if(funcOpt.isEmpty()) {
            TotemAPI.LOGGER.atError().log(String.format("Function %s was not found.", functionIdentifier.toString()));
            return;
        }

        var function = funcOpt.get();

        Procedure<ServerCommandSource> commandFunction;
        try {
            commandFunction = function.withMacroReplaced(arguments, source.getDispatcher());
        }catch (MacroException e) {
            TotemAPI.LOGGER.atError().log("Error occurred while executing function totem effect:");
            TotemAPI.LOGGER.atError().log(e.getMessage().getString());
            return;
        }

        CommandManager.callWithContext(source, context -> {

            CommandExecutionContext.enqueueProcedureCall(context, commandFunction, source.withEntity(user).withPosition(user.getPos()), ReturnValueConsumer.EMPTY);
        });
    }

    @Override
    public TotemEffectType getType() {
        return FUNCTION_TYPE;
    }
}
