package com.tbw1wnl.racesnorigins.modifier;

import net.minecraft.resources.Identifier;

/**
 * Implemented on {@code LivingEntity} via mixin. Backs
 * {@link com.tbw1wnl.racesnorigins.modifier.types.EffectImmunityModifierTrait}: a runtime-only set
 * of immune effect ids recomputed every time trait modifiers are (re)applied, not persisted itself
 * - same pattern as {@link FireImmunityHolder}, generalized to arbitrary effects.
 */
public interface EffectImmunityHolder {

    boolean racesnorigins$isImmuneTo(Identifier effectId);

    void racesnorigins$setImmune(Identifier effectId, boolean immune);
}
