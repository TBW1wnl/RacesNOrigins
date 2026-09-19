package com.tbw1wnl.racesnorigins.modifier;

/**
 * Implemented on {@code LivingEntity} via mixin. Backs {@link com.tbw1wnl.racesnorigins.modifier.types.AmphibiousModifierTrait}:
 * a runtime-only "land air supply" countdown, recomputed every time trait modifiers are (re)applied,
 * not persisted itself - mirrors vanilla's own air-supply field but inverted (drains on land instead
 * of underwater).
 */
public interface AmphibiousHolder {

    int racesnorigins$landTicksRemaining();

    void racesnorigins$setLandTicksRemaining(int ticks);
}
