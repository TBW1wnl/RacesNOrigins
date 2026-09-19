package com.tbw1wnl.racesnorigins.mixin;

import com.tbw1wnl.racesnorigins.modifier.GlideHolder;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * NeoForge-only: {@code LocalPlayer} overrides {@code canGlide()} with its own client-side
 * -prediction logic (checking the {@code neoforge:gliding_flight} attribute, or equipped items on
 * a vanilla connection) rather than delegating to {@code LivingEntity#canGlide()} - that override
 * is a NeoForge source patch, absent on Fabric's vanilla {@code LocalPlayer} (there,
 * {@code MixinLivingEntityGlide} alone already covers it since Fabric's LocalPlayer simply
 * inherits the LivingEntity method unmodified - confirmed empirically after this NeoForge-specific
 * mixin crashed Fabric's client for targeting a method that doesn't exist there).
 * <p>
 * Same care as {@code MixinLivingEntityGlide}: must still respect the physical conditions that stop
 * gliding (on the ground, riding, under Levitation), since {@code canGlide()} is polled every tick
 * to decide whether to keep gliding, not just to start it.
 */
@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayerGlide {

    @Inject(method = "canGlide()Z", at = @At("HEAD"), cancellable = true)
    private void racesnorigins$onCanGlide(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (((GlideHolder) this).racesnorigins$canGlide()
                && !self.onGround() && !self.isPassenger() && !self.hasEffect(MobEffects.LEVITATION)) {
            cir.setReturnValue(true);
        }
    }
}
