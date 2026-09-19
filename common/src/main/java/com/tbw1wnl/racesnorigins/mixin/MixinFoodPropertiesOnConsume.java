package com.tbw1wnl.racesnorigins.mixin;

import com.tbw1wnl.racesnorigins.modifier.DietHolder;
import com.tbw1wnl.racesnorigins.tags.ModTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * Cancels the nutrition/saturation gain (and eating sound) from {@link FoodProperties#onConsume}
 * when the eater has an active {@code racesnorigins:diet} restriction that the food violates - the
 * item is still consumed normally (this only touches the listener that grants hunger/saturation),
 * matching the "consumable but without effect" behavior decided for diet violations. Runs
 * identically on client and server since both independently call this same method when a player
 * eats (see {@code FoodProperties#onConsume} in the decompiled sources) - the client's copy of
 * {@link DietHolder}'s state is kept in sync via a payload for exactly this reason.
 */
@Mixin(FoodProperties.class)
public abstract class MixinFoodPropertiesOnConsume {

    @Inject(method = "onConsume", at = @At("HEAD"), cancellable = true)
    private void racesnorigins$onConsume(Level level, LivingEntity user, ItemStack stack, Consumable consumable, CallbackInfo ci) {
        if (!(user instanceof DietHolder holder)) {
            return;
        }
        Optional<String> restriction = holder.racesnorigins$dietRestriction();
        if (restriction.isEmpty()) {
            return;
        }
        boolean isMeat = stack.typeHolder().is(ModTags.MEAT);
        boolean allowed = isMeat == "carnivore".equals(restriction.get());
        if (!allowed) {
            ci.cancel();
        }
    }
}
