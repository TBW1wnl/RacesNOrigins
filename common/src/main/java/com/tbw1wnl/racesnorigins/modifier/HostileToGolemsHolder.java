package com.tbw1wnl.racesnorigins.modifier;

/**
 * Implemented on {@code LivingEntity} via mixin. Backs {@link com.tbw1wnl.racesnorigins.modifier.types.HostileToGolemsModifierTrait}:
 * a runtime-only flag, recomputed every time trait modifiers are (re)applied, read by
 * {@code MixinIronGolemHostility} to decide which nearby players an iron golem should turn its
 * existing anger system against. Server-only, like {@link HostileMobNeutralityHolder}.
 */
public interface HostileToGolemsHolder {

    boolean racesnorigins$isHostileToGolems();

    void racesnorigins$setHostileToGolems(boolean hostile);
}
