package com.tbw1wnl.racesnorigins.mixin;

import com.tbw1wnl.racesnorigins.modifier.DietHolder;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntityDiet implements DietHolder {

    @Unique
    private Optional<String> racesnorigins$dietRestriction = Optional.empty();

    @Override
    public Optional<String> racesnorigins$dietRestriction() {
        return this.racesnorigins$dietRestriction;
    }

    @Override
    public void racesnorigins$setDietRestriction(Optional<String> restriction) {
        this.racesnorigins$dietRestriction = restriction;
    }
}
