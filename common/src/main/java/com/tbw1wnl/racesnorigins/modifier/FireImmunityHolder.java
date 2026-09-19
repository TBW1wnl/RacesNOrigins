package com.tbw1wnl.racesnorigins.modifier;

/**
 * Implemented on {@code Entity} via mixin. Backs {@link com.tbw1wnl.racesnorigins.modifier.types.FireImmunityModifierTrait}:
 * a runtime-only flag recomputed every time trait modifiers are (re)applied, not persisted itself.
 */
public interface FireImmunityHolder {

    boolean racesnorigins$isFireImmune();

    void racesnorigins$setFireImmune(boolean fireImmune);
}
