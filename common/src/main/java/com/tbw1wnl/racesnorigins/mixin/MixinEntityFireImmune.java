package com.tbw1wnl.racesnorigins.mixin;

import com.tbw1wnl.racesnorigins.modifier.FireImmunityHolder;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntityFireImmune implements FireImmunityHolder {

    @Unique
    private boolean racesnorigins$fireImmune = false;

    @Override
    public boolean racesnorigins$isFireImmune() {
        return this.racesnorigins$fireImmune;
    }

    @Override
    public void racesnorigins$setFireImmune(boolean fireImmune) {
        this.racesnorigins$fireImmune = fireImmune;
    }

    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
    private void racesnorigins$onFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if (this.racesnorigins$fireImmune) {
            cir.setReturnValue(true);
        }
    }
}
