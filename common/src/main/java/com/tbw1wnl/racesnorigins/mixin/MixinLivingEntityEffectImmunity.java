package com.tbw1wnl.racesnorigins.mixin;

import com.tbw1wnl.racesnorigins.modifier.EffectImmunityHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntityEffectImmunity implements EffectImmunityHolder {

    @Unique
    private final Set<Identifier> racesnorigins$immuneEffects = new HashSet<>();

    @Override
    public boolean racesnorigins$isImmuneTo(Identifier effectId) {
        return this.racesnorigins$immuneEffects.contains(effectId);
    }

    @Override
    public void racesnorigins$setImmune(Identifier effectId, boolean immune) {
        if (immune) {
            this.racesnorigins$immuneEffects.add(effectId);
        } else {
            this.racesnorigins$immuneEffects.remove(effectId);
        }
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void racesnorigins$onCanBeAffected(MobEffectInstance newEffect, CallbackInfoReturnable<Boolean> cir) {
        for (Identifier immune : this.racesnorigins$immuneEffects) {
            if (newEffect.getEffect().is(immune)) {
                cir.setReturnValue(false);
                return;
            }
        }
    }
}
