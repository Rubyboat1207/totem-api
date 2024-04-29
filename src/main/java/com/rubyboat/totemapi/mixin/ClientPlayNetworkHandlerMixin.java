package com.rubyboat.totemapi.mixin;

import com.rubyboat.totemapi.TotemItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.network.ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Shadow private ClientWorld world;

    @Inject(at = @At("HEAD"), method = "onEntityStatus", cancellable = true)
    public void onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
        var client = MinecraftClient.getInstance();
        NetworkThreadUtils.forceMainThread(packet, ((net.minecraft.client.network.ClientPlayNetworkHandler)(Object)this), client);
        Entity entity = packet.getEntity(this.world);
        if (entity != null) {
            if (packet.getStatus() == 100) {


                client.particleManager.addEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
                this.world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);
                if (entity == client.player) {
                    client.gameRenderer.showFloatingItem(TotemItem.getActiveCustomTotem(client.player));
                }
            }else if(packet.getStatus() == 98)
            {
                client.gameRenderer.showFloatingItem(client.player.getStackInHand(Hand.MAIN_HAND));
            }
        }

    }


}
