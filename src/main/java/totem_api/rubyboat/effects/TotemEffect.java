package totem_api.rubyboat.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import totem_api.rubyboat.Main;
import totem_api.rubyboat.components.TotemComponent;
import totem_api.rubyboat.components.TotemEffectType;

import static totem_api.rubyboat.components.TotemComponent.EFFECTS;

public abstract class TotemEffect {
    public static final Codec<TotemEffect> CODEC = Codec.lazyInitialized(TotemEffect::codec);

    public static Codec<TotemEffect> codec() {
        var typeCodec = EFFECTS.getCodec();

        return typeCodec.dispatch("type", TotemEffect::getType, TotemEffectType::codec);
    }


    public void onDeath(LivingEntity user, World world, ItemStack stack) { }

    public ActionResult onClick(LivingEntity user, World world, BlockPos selectedBlock, ItemStack stack)
    {
        return ActionResult.PASS;
    }

    public String getTooltip()
    {
        return "";
    }

    public TotemEffectType getType() {
        return EFFECTS.get(new Identifier(Main.MOD_ID, "none"));
    }
}
