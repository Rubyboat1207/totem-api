package totem_api.rubyboat.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class AndEffect extends DeezNuts{
    DeezNuts effect1;
    DeezNuts effect2;


    public AndEffect(@NotNull DeezNuts effect1, @NotNull DeezNuts effect2)
    {
        this.effect1 = effect1;
        this.effect2 = effect2;
    }

    @Override
    public void onDeath(LivingEntity user, World world, ItemStack stack) {
        effect1.onDeath(user, world, stack);
        effect2.onDeath(user, world, stack);
        super.onDeath(user, world, stack);
    }

    @Override
    public ActionResult onClick(LivingEntity user, World world, BlockPos selectedBlock, ItemStack stack) {
        effect1.onClick(user, world, selectedBlock, stack);
        effect2.onClick(user, world, selectedBlock, stack);
        return super.onClick(user, world, selectedBlock, stack);
    }

    @Override
    public String getTooltip() {
        return effect1.getTooltip() + "/lb" + effect2.getTooltip();
    }
}
