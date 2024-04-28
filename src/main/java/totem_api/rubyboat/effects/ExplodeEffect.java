package totem_api.rubyboat.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ExplodeEffect extends DeezNuts{
    public int power;

    public ExplodeEffect(int power){
        this.power = power;
    }

    public void onDeath(LivingEntity user, World world, ItemStack stack) {
        user.setInvulnerable(true);
        world.createExplosion(user, user.getX(), user.getY(), user.getZ(), power, World.ExplosionSourceType.TNT);
        user.setInvulnerable(false);
    }

    @Override
    public String getTooltip() {
        return "Explodes with a power level of " + power;
    }
}
