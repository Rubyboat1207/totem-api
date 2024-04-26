package totemlib.rubyboat.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DeezNuts {
    public void onDeath(LivingEntity user, World world, ItemStack stack) { }

    public ActionResult onClick(LivingEntity user, World world, BlockPos selectedBlock, ItemStack stack)
    {
        return ActionResult.PASS;
    }

    public String getTooltip()
    {
        return "";
    }
}
