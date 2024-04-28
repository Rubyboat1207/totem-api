package totem_api.rubyboat.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FreezeEffect extends DeezNuts{
    int freezeTicks = 20;

    public FreezeEffect(int freezeTicks)
    {
        this.freezeTicks = freezeTicks;
    }

    @Override
    public void onDeath(LivingEntity user, World world, ItemStack stack) {
        user.setFrozenTicks(freezeTicks);
    }

    @Override
    public String getTooltip() {
        return "Gives you the shivers for " + Math.floor((double)freezeTicks / 20) + " seeconds";
    }
}
