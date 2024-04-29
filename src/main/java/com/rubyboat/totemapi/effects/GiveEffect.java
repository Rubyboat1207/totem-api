package com.rubyboat.totemapi.effects;

import com.rubyboat.totemapi.effects.TotemEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GiveEffect extends TotemEffect {
    ItemStack item;
    public GiveEffect(ItemStack item)
    {
        this.item = item;
    }


    @Override
    public void onDeath(LivingEntity user, World world, ItemStack stack) {
        if(user.isPlayer())
        {
            ((PlayerEntity) user).getInventory().insertStack(item);
        }
    }

    @Override
    public String getTooltip() {
        return "gives the user " + (item.getCount() == 1 ? "a" : item.getCount()) + " " + item.getName().getString();
    }
}
