package com.tbw1wnl.racesnorigins.mixin;

import com.tbw1wnl.racesnorigins.modifier.GlideHolder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Bypasses vanilla's "must have an elytra (or other glide-capable item) equipped" check for
 * {@code "elytra"}-mode flight, while still respecting the physical conditions that stop gliding
 * (on the ground, riding something, under Levitation) - the injection deliberately doesn't
 * unconditionally return true, since {@code canGlide()} is also polled every tick by
 * {@code updateFallFlying()} to decide whether to KEEP gliding, and bypassing those conditions too
 * meant landing never actually stopped the glide. Targets the no-arg {@code canGlide()} overload
 * explicitly (by descriptor) since NeoForge's source patches add a second {@code canGlide(boolean)}
 * overload on top of it.
 * <p>
 * Also fixes a real crash (confirmed via the actual decompiled vanilla source, not the NeoForge
 * -patched one): {@code updateFallFlying()} periodically picks a random equipped glide-capable item
 * to damage via {@code Util.getRandom(slotsWithGliders, random)}, with no empty-list check in
 * vanilla - since our elytra-mode grants gliding without any such item equipped, that list is
 * always empty for it, and {@code Util.getRandom} on an empty list throws
 * {@code IllegalArgumentException("Bound must be positive")}, crashing the game every 10 ticks
 * while gliding this way.
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntityGlide implements GlideHolder {

    @Unique
    private boolean racesnorigins$canGlide = false;

    @Override
    public boolean racesnorigins$canGlide() {
        return this.racesnorigins$canGlide;
    }

    @Override
    public void racesnorigins$setCanGlide(boolean canGlide) {
        this.racesnorigins$canGlide = canGlide;
    }

    @Inject(method = "canGlide()Z", at = @At("HEAD"), cancellable = true)
    private void racesnorigins$onCanGlide(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (this.racesnorigins$canGlide && !self.onGround() && !self.isPassenger() && !self.hasEffect(MobEffects.LEVITATION)) {
            cir.setReturnValue(true);
        }
    }

    @Redirect(method = "updateFallFlying()V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/util/Util;getRandom(Ljava/util/List;Lnet/minecraft/util/RandomSource;)Ljava/lang/Object;"))
    private Object racesnorigins$safeGetRandomGliderSlot(List<EquipmentSlot> slotsWithGliders, RandomSource random) {
        if (!slotsWithGliders.isEmpty()) {
            return Util.getRandom(slotsWithGliders, random);
        }
        LivingEntity self = (LivingEntity) (Object) this;
        for (EquipmentSlot slot : EquipmentSlot.VALUES) {
            if (self.getItemBySlot(slot).isEmpty()) {
                return slot;
            }
        }
        return EquipmentSlot.MAINHAND;
    }
}
