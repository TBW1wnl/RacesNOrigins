package com.tbw1wnl.racesnorigins.mixin;

import com.tbw1wnl.racesnorigins.modifier.AmphibiousHolder;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntityAmphibious implements AmphibiousHolder {

    @Unique
    private int racesnorigins$landTicksRemaining = 0;

    @Override
    public int racesnorigins$landTicksRemaining() {
        return this.racesnorigins$landTicksRemaining;
    }

    @Override
    public void racesnorigins$setLandTicksRemaining(int ticks) {
        this.racesnorigins$landTicksRemaining = ticks;
    }
}
