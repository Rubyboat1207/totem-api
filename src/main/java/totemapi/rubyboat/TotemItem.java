package totemapi.rubyboat;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import totemapi.rubyboat.effects.DeezNuts;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TotemItem extends Item {

    public ArrayList<StatusEffectInstance> effects;
    public int healthRemaining;
    public DeezNuts nutz;
    public static ArrayList<TotemItem> totems;
    public boolean canUseInVoid = false;

    public TotemItem(Settings settings, Identifier identifier, ArrayList<StatusEffectInstance> effects, int healthRemaining, @Nullable DeezNuts nutz) {
        super(settings.maxCount(1));
        this.effects = effects;
        this.healthRemaining = healthRemaining;
        this.nutz = nutz;
        Registry.register(Registry.ITEM, identifier, this);
    }

    public TotemItem(Settings settings, Identifier identifier, ArrayList<StatusEffectInstance> effects, int healthRemaining, @Nullable DeezNuts nutz, boolean canUseInVoid) {
        super(settings.maxCount(1));
        this.effects = effects;
        this.healthRemaining = healthRemaining;
        this.nutz = nutz;
        Registry.register(Registry.ITEM, identifier, this);
        this.canUseInVoid = canUseInVoid;
    }

    @Override
    public void appendTooltip(ItemStack stack, @org.jetbrains.annotations.Nullable World world, List<Text> tooltip, TooltipContext context) {
        ArrayList<StatusEffectInstance> goodEffect = new ArrayList<>();
        ArrayList<StatusEffectInstance> badEffect = new ArrayList<>();
        for(StatusEffectInstance effect : effects)
        {
            if(effect.getEffectType().isBeneficial())
            {
                goodEffect.add(effect);
            }else
            {
                badEffect.add(effect);
            }
        }
        if(goodEffect.size() > 0)
        {
            tooltip.add(Text.of("§2Beneficial Effects:"));
            for(StatusEffectInstance effect : goodEffect)
            {
                tooltip.add(Text.of("   §a" + effect.getEffectType().getName().getString() + " " + (effect.getAmplifier() + 1) +" (" + (effect.getDuration()/20) + "s)"));
            }
        }
        if(badEffect.size() > 0)
        {
            tooltip.add(Text.of("§4Negative Effects:"));
            for(StatusEffectInstance effect : badEffect)
            {
                tooltip.add(Text.of("   §c" + effect.getEffectType().getName().getString() + " (" + (effect.getDuration()/20) + "s) lvl " + (effect.getAmplifier() + 1)));
            }
        }
        if(nutz != null)
        {
            tooltip.add(Text.of("§dExtra Effects:"));
            String[] tt2 = nutz.getTooltip().split("/lb");
            for(String str : tt2)
            {
                tooltip.add(Text.of("    §5" + str));
            }
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    public void onUse(LivingEntity user, ItemStack stack)
    {
        user.setFireTicks(0);
        user.clearStatusEffects();
        user.setAir(user.getMaxAir());
        user.setVelocity(new Vec3d(0,0,0));
        user.fallDistance = 0;
        for(StatusEffectInstance effect : effects)
        {
            user.addStatusEffect(new StatusEffectInstance(effect));
        }
        if(nutz != null)
        {
            nutz.onDeath(user, user.getWorld(), stack);
        }
        user.setHealth(healthRemaining);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {

        if(nutz != null)
        {
            return nutz.onClick(context.getPlayer(), context.getWorld(), context.getBlockPos(), context.getStack());
        }
        return ActionResult.PASS;
    }
}
