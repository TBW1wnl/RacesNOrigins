package com.tbw1wnl.racesnorigins.modifier;

import java.util.Optional;

/**
 * Implemented on {@code LivingEntity} via mixin. Backs {@link com.tbw1wnl.racesnorigins.modifier.types.DietModifierTrait}:
 * mirrored onto both the server's {@code ServerPlayer} and the owning client's {@code LocalPlayer}
 * (synced via a payload, same reasoning as {@link GlideHolder}) since the food-consumption mixin
 * this gates runs identically on both sides.
 */
public interface DietHolder {

    Optional<String> racesnorigins$dietRestriction();

    void racesnorigins$setDietRestriction(Optional<String> restriction);
}
