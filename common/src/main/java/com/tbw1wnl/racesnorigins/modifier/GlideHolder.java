package com.tbw1wnl.racesnorigins.modifier;

/**
 * Implemented on {@code LivingEntity} via mixin. Backs the {@code "elytra"} mode of
 * {@link com.tbw1wnl.racesnorigins.modifier.types.FlightModifierTrait}: a runtime-only flag
 * bypassing vanilla's "must have a glide-capable item equipped" check, recomputed every time trait
 * modifiers are (re)applied rather than persisted itself.
 */
public interface GlideHolder {

    boolean racesnorigins$canGlide();

    void racesnorigins$setCanGlide(boolean canGlide);
}
